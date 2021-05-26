package br.com.estudante;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.estudante.tela.Tela;
import tela.enumerador.SituacaoInicio;

public class ClienteTCP extends Thread {
	private Socket conexao;
	private ObjectOutputStream saida;
	private ObjectInputStream entrada;
	private ClienteTCP clienteRecebe;
	private String novaMensagem;
	private boolean escutando;
	private Tela cliente;
	private Jogador jogador;
	private List<Jogador> jogadoresConectados;

	public ClienteTCP(Tela cliente) {
		this.cliente = cliente;
		this.jogadoresConectados = new ArrayList<Jogador>();
	}

	public ClienteTCP(Socket conexao, Tela cliente, ObjectInputStream entrada, ObjectOutputStream saida,
			Jogador jogador, List<Jogador> jogadoresConectados) {
		this.conexao = conexao;
		this.cliente = cliente;
		this.entrada = entrada;
		this.saida = saida;
		this.jogador = jogador;
		this.escutando = true;
		this.jogadoresConectados = jogadoresConectados;
	}

	public void run() {
		this.enviarMensagem(this.jogador.getNome() + " se conectou (" + this.jogador.getIpLocal() + ":"
				+ conexao.getLocalPort() + ")");
		try {
			Jogador jogadorAux = null;
			Jogador jogadorDesc = null;
			while (this.escutando) {
				System.out.println("Esperando o que fazer");
				String fazer = (String) entrada.readObject();
				System.out.println(this.jogador.getNome() + " está indo " + fazer);
				if (fazer.equals("Adicionar jogador")) {
					System.out.println("Esperando alguém");
					jogadorAux = (Jogador) entrada.readObject();
					System.out.println("Recebeu o " + jogadorAux.getNome());
					int numJogador = (int) entrada.readObject();
					System.out.println("Recebeu o número " + numJogador);
					this.cliente.adicionarJogador(jogadorAux.getNome(), jogadorAux.getCivilizacao(),
							jogadorAux.getIpServidor(), jogadorAux.getSituacao());
					this.cliente.mostrarSituacaoJogador(numJogador, jogadorAux.getSituacao());
					this.jogadoresConectados.add(jogadorAux);
				} else if (fazer.equals("Adicionar mensagem")) {
					this.novaMensagem = (String) entrada.readObject();
					this.cliente.adicionarMensagem(this.novaMensagem);
				} else if (fazer.equals("Remover jogador")) {
					System.out.println("Iniciando atualização da lista de jogadores");
					System.out.println("Esperando um jogador desconectado");
					jogadorDesc = (Jogador) entrada.readObject();
					System.out.println("Recebeu o " + jogadorDesc.getNome() + " para desconectar");
					this.jogadoresConectados.remove(jogadorDesc);
					System.out.println("Remove o jogador " + jogadorDesc.getNome());

					System.out.println("Jogadores na lista:");
					for (Jogador jogador2 : this.jogadoresConectados) {
						System.out.println(jogador2.getNome());
					}
					this.cliente.limparJogadores();
					int i = 1;
					for (Jogador jogador2 : this.jogadoresConectados) {
						this.cliente.adicionarJogador(jogador2.getNome(), jogador2.getCivilizacao(),
								jogador2.getIpServidor(), jogador2.getSituacao());
						this.cliente.mostrarSituacaoJogador(i, jogador2.getSituacao());
						System.out.println("Adiciona e mostra o jogador " + jogador2.getNome() + " na posição " + i);
						i++;
					}
					if (jogadorDesc.getSituacao().equals("Jogando")) {
						this.cliente.limparInimigos();

						for (Jogador jogadorInimigo : this.jogadoresConectados) {
							if (!jogadorInimigo.equals(this.jogador))
								this.cliente.adicionarInimigo(jogadorInimigo.getNome());
						}
					}
					System.out.println("Lista atualizada!");
					this.enviarMensagem(jogadorDesc.getNome() + " se desconectou");
				} else if (fazer.equals("Fechar")) {
					this.escutando = false;
					System.out.println("Não está mais escutando");
				} else if (fazer.equals("Iniciar jogo")) {
					this.cliente.limparJogadores();
					int i = 1;
					for (Jogador jogador : jogadoresConectados) {
						jogador.setSituacao("Jogando");
						this.cliente.adicionarJogador(jogador.getNome(), jogador.getCivilizacao(), jogador.getIpLocal(),
								jogador.getSituacao());
						this.cliente.mostrarSituacaoJogador(i, jogador.getSituacao());
						i++;
					}
					for (Jogador jogadorInimigo : this.jogadoresConectados) {
						if (!jogadorInimigo.equals(this.jogador))
							this.cliente.adicionarInimigo(jogadorInimigo.getNome());
					}
					this.cliente.getTpJogo().setSelectedIndex(1);
					this.cliente.getTpJogo().setEnabledAt(1, true);
				} else if (fazer.equals("Recebendo praga")) {
					String jogadorAtacante = (String) this.entrada.readObject();
					switch ((String) this.entrada.readObject()) {
					case "Nuvem de gafanhotos":
						if (this.cliente.getVila().isProtecaoGafanhotos()) {
							this.cliente.mostrarMensagemErro("Tentativa de Ataque", jogadorAtacante
									+ " tentou lançar uma nuvem de gafanhotos em sua vila!\nMas falhou porque você tem proteção!");
							System.out.println("Mostrou a telinha para o atacado");
							this.saida.writeObject("Resultado ataque");
							this.saida.writeObject("Falha");
							System.out.println("Envia sucesso para o servidor >:(");
							this.saida.writeObject(jogadorAtacante);
							this.saida.writeObject(this.jogador.getNome());
							this.saida.writeObject("Nuvem de gafanhotos");
						} else {
							int qtdFazendas = this.cliente.getVila().getQtdFazendas();
							if (qtdFazendas > 1) {
								int metadeFazendas = (qtdFazendas / 2), auxNumFazenda = qtdFazendas - 1;
								Fazenda fazenda;
								int numAldeao;
								for (; auxNumFazenda >= metadeFazendas; auxNumFazenda--) {
									numAldeao = 0;
									fazenda = this.cliente.getVila().getFazenda(auxNumFazenda);
									for (Aldeao aldeao : fazenda.getFazendeiros()) {
										aldeao.setStatus(Status.PARADO);
										this.cliente.mostrarAldeao(numAldeao, aldeao.getStatus());
									}
									this.cliente.getVila().removeFazenda(auxNumFazenda);
								}
								this.cliente.limparFazendas();
								int numFazenda = 0;
								for (Fazenda fazenda2 : this.cliente.getVila().getFazendas()) {
									this.cliente.adicionarFazenda(String.valueOf(numFazenda),
											fazenda2.getNomeAldeoes());
									this.cliente.mostrarFazenda(numFazenda, fazenda2.getNomeAldeoes());
									numFazenda++;
								}
							}
							this.cliente.mostrarMensagemErro("Tentativa de Ataque", jogadorAtacante
									+ " lançou uma nuvem de gafanhotos em sua vila!\nVocê perdeu metade das suas fazendas!");
							System.out.println("Mostrou a telinha para o atacado");
							this.saida.writeObject("Resultado ataque");
							this.saida.writeObject("Sucesso");
							System.out.println("Envia sucesso para o servidor >:(");
							this.saida.writeObject(jogadorAtacante);
							this.saida.writeObject(this.jogador.getNome());
							this.saida.writeObject("Nuvem de gafanhotos");
						}
						break;
					case "Morte dos primogênitos":
						if (this.cliente.getVila().isProtecaoPrimogenitos()) {
							this.cliente.mostrarMensagemErro("Tentativa de Ataque", jogadorAtacante
									+ " tentou lançar a praga Morte dos primogênitos em sua vila!\nMas falhou porque você tem proteção!");
							System.out.println("Mostrou a telinha para o atacado");
							this.saida.writeObject("Resultado ataque");
							this.saida.writeObject("Falha");
							System.out.println("Envia sucesso para o servidor >:(");
							this.saida.writeObject(jogadorAtacante);
							this.saida.writeObject(this.jogador.getNome());
							this.saida.writeObject("Morte dos primogênitos");
						} else {
							int qtdAldeoes = this.cliente.getVila().getQtdAldeoesVivos();
							if (qtdAldeoes > 1) {
								int pos = this.cliente.getVila().getAldeoes().size() - 1;
								qtdAldeoes /= 2;
								Aldeao aldeao;
								while (qtdAldeoes > 0) {
									aldeao = this.cliente.getVila().getAldeoes().get(pos);
									if (!aldeao.getStatus().equals("Morto")
											&& !aldeao.getStatus().equals("Sacrificado")) {
										aldeao.interrupt();
										aldeao.setStatus(Status.MORTO);
										System.out.println(aldeao.getNome() + " agora está " + aldeao.getStatus());
										qtdAldeoes--;
									}
									pos--;
								}
								int i = 0;
								for (Aldeao aldeao2 : this.cliente.getVila().getAldeoes()) {
									this.cliente.mostrarAldeao(i, aldeao2.getStatus());
								}
							}
							this.cliente.mostrarMensagemErro("Tentativa de Ataque", jogadorAtacante
									+ " lançou a praga Morte dos primogênitos em sua vila!\nVocê perdeu metade dos seus aldeões!");
							System.out.println("Mostrou a telinha para o atacado");
							this.saida.writeObject("Resultado ataque");
							this.saida.writeObject("Sucesso");
							System.out.println("Envia sucesso para o servidor >:(");
							this.saida.writeObject(jogadorAtacante);
							this.saida.writeObject(this.jogador.getNome());
							this.saida.writeObject("Morte dos primogênitos");
						}
						break;

					case "Chuva de pedras":
						if (this.cliente.getVila().isProtecaoPedras()) {
							this.cliente.mostrarMensagemErro("Tentativa de Ataque", jogadorAtacante
									+ " tentou lançar uma chuva de pedras em sua vila!\nMas falhou porque você tem proteção!");
							System.out.println("Mostrou a telinha para o atacado");
							this.saida.writeObject("Resultado ataque");
							this.saida.writeObject("Falha");
							System.out.println("Envia sucesso para o servidor >:(");
							this.saida.writeObject(jogadorAtacante);
							this.saida.writeObject(this.jogador.getNome());
							this.saida.writeObject("Chuva de pedras");
						} else {
							int qtdFazendas = this.cliente.getVila().getQtdFazendas();
							if (qtdFazendas > 1) {
								int metadeFazendas = (qtdFazendas / 2), auxNumFazenda = qtdFazendas - 1;
								Fazenda fazenda;
								int numAldeao;
								for (; auxNumFazenda >= metadeFazendas; auxNumFazenda--) {
									numAldeao = 0;
									fazenda = this.cliente.getVila().getFazenda(auxNumFazenda);
									for (Aldeao aldeao : fazenda.getFazendeiros()) {
										aldeao.setStatus(Status.PARADO);
										this.cliente.mostrarAldeao(numAldeao, aldeao.getStatus());
									}
									this.cliente.getVila().removeFazenda(auxNumFazenda);
								}
								this.cliente.limparFazendas();
								int numFazenda = 0;
								for (Fazenda fazenda2 : this.cliente.getVila().getFazendas()) {
									this.cliente.adicionarFazenda(String.valueOf(numFazenda),
											fazenda2.getNomeAldeoes());
									this.cliente.mostrarFazenda(numFazenda, fazenda2.getNomeAldeoes());
									numFazenda++;
								}
							}
							int qtdMinas = this.cliente.getVila().getQtdMinasOuro();
							if (qtdMinas > 1) {
								int metadeMinasOuro = (qtdMinas / 2), auxNumMina = qtdMinas - 1;
								MinaOuro minaOuro;
								int numAldeao;
								for (; auxNumMina >= metadeMinasOuro; auxNumMina--) {
									numAldeao = 0;
									minaOuro = this.cliente.getVila().getMinaOuro(auxNumMina);
									for (Aldeao aldeao : minaOuro.getMineradores()) {
										aldeao.setStatus(Status.PARADO);
										this.cliente.mostrarAldeao(numAldeao, aldeao.getStatus());
									}
									this.cliente.getVila().removeMinaOuro(auxNumMina);
								}
								this.cliente.limparMinas();
								int numMina = 0;
								for (MinaOuro mina : this.cliente.getVila().getMinas()) {
									this.cliente.adicionarMinaOuro(String.valueOf(numMina), mina.getNomeAldeoes());
									this.cliente.mostrarMinaOuro(numMina, mina.getNomeAldeoes());
									numMina++;
								}
							}
							int qtdAldeoes = this.cliente.getVila().getQtdAldeoesVivos();
							if (qtdAldeoes > 1) {
								int pos = this.cliente.getVila().getAldeoes().size() - 1;
								qtdAldeoes /= 2;
								Aldeao aldeao;
								while (qtdAldeoes > 0) {
									aldeao = this.cliente.getVila().getAldeoes().get(pos);
									if (!aldeao.getStatus().equals("Morto")
											&& !aldeao.getStatus().equals("Sacrificado")) {
										aldeao.interrupt();
										aldeao.setStatus(Status.MORTO);
										System.out.println(aldeao.getNome() + " agora está " + aldeao.getStatus());
										qtdAldeoes--;
									}
									pos--;
								}
								int i = 0;
								for (Aldeao aldeao2 : this.cliente.getVila().getAldeoes()) {
									this.cliente.mostrarAldeao(i, aldeao2.getStatus());
								}
							}
							Maravilha maravilha = this.cliente.getVila().getMaravilha();
							if (maravilha != null) {
								int qtdTijolos = maravilha.getQtdTijolos();
								System.out.println("Maravilha tem " + qtdTijolos + " tijolos.");
								if (qtdTijolos > 1) {
									qtdTijolos /= 2;
									maravilha.setQtdTijolos(-qtdTijolos);
									System.out
											.println("Maravilha agora tem " + maravilha.getQtdTijolos() + " tijolos.");
								}
							}
							this.cliente.mostrarMensagemErro("Tentativa de Ataque", jogadorAtacante
									+ " lançou uma chuva de pedras em sua vila!\nVocê perdeu:\nMetade das suas fazendas"
									+ "\nMetade das suas minas de ouro\nMetade da maravilha\nMetade dos aldeões");
							System.out.println("Mostrou a telinha para o atacado");
							this.saida.writeObject("Resultado ataque");
							this.saida.writeObject("Sucesso");
							System.out.println("Envia sucesso para o servidor >:(");
							this.saida.writeObject(jogadorAtacante);
							this.saida.writeObject(this.jogador.getNome());
							this.saida.writeObject("Chuva de pedras");
						}
						break;
					}
				} else if (fazer.equals("Mostra resultado ataque")) {
					String retorno = (String) this.entrada.readObject();
					String nomeAlvo = (String) this.entrada.readObject();
					String praga = (String) this.entrada.readObject();
					System.out.println("Recebeu " + retorno + " do servidor");
					switch (retorno) {
					case "Falha":
						System.out.println("puts, ataque deu ruim");
						this.cliente.mostrarMensagemErro("Situção do Ataque", "Seu ataque à vila do inimigo " + nomeAlvo
								+ " falhou!\n Ele tem uma proteção contra " + praga + "!");
						break;
					case "Sucesso":
						String complemento = "";
						switch (praga) {
						case "Nuvem de gafanhotos":
							complemento = "perdeu metade das fazendas!";
							break;
						case "Morte dos primogênitos":
							complemento = "perdeu metade dos aldeões!";
							break;
						case "Chuva de pedras":
							complemento = "perdeu metade das fazendas, metade das minas de ouro, metade da maravilha e metade dos aldeões!";
							break;
						}
						System.out.println("aeeeeee funfou o ataque");
						this.cliente.mostrarMensagemErro("Situção do Ataque",
								"Seu ataque à vila do inimigo " + nomeAlvo + " foi um sucesso!\n Ele " + complemento);
					}
				} else if (fazer.equals("Terminar")) {
					Jogador jogadorVencedor = (Jogador) entrada.readObject();

					if (!this.jogador.equals(jogadorVencedor)) {
						this.cliente.mostrarMensagemErro("Fim de jogo", "A civilização "
								+ jogadorVencedor.getCivilizacao()
								+ " conseguiu o triunfo e se tornou a maior potência do mundo graças ao governo de "
								+ jogadorVencedor.getNome());
					} else {
						this.cliente.mostrarMensagemErro("Fim de jogo",
								"Graças aos seus esforços, a civilização " + jogadorVencedor.getCivilizacao()
										+ " se tornou a maior potência do mundo\nParabéns!!!");

					}
					if (this.jogador.equals(this.jogadoresConectados.get(0))) {
						this.desconectar();
					}
				}
			}
			this.entrada.close();
			System.out.println("Fecha entrada");
			this.saida.close();
			System.out.println("Fecha saída");
			this.conexao.close();
			System.out.println("Fecha conexão");
		} catch (IOException e) {
			System.out.println("Não foi possível receber o que queria do servidor...");
		} catch (ClassNotFoundException e) {
			System.out.println("Falha na conversão da Stream");
		}
		System.out.println(this.jogador.getNome() + " desconectado.");
		this.cliente.getTpJogo().setSelectedIndex(0);
		this.cliente.getTpJogo().setEnabledAt(1, false);
		this.cliente.limparMensagens();
		this.cliente.limparJogadores();
		this.cliente.limparInimigos();
		this.cliente.setSituacaoInicio(SituacaoInicio.INICIAL_CRIAR);
		this.cliente.habilitarInicio();
		this.cliente.getVila().getPrefeitura().setAcabou(true);
		this.cliente.getVila().getPrefeitura().interrupt();
		System.out.println("Thread " + this.jogador.getNome() + " morreu");
	}

	public boolean criarServidor(String host, Jogador jogadorHost) {
		try {
			this.jogador = jogadorHost;
			this.conexao = new Socket(host, 12345);
			this.saida = new ObjectOutputStream(this.conexao.getOutputStream());
			this.entrada = new ObjectInputStream(this.conexao.getInputStream());
			saida.writeObject("Criar jogo");
			Boolean criou = (Boolean) entrada.readObject();
			System.out.println("Recebeu " + criou);
			if (!criou) {
				saida.writeObject(this.jogador);
				this.jogadoresConectados.add(this.jogador);
				this.cliente.adicionarJogador(jogadorHost.getNome(), jogadorHost.getCivilizacao(),
						jogadorHost.getIpServidor(), "aguardando jogadores...");
				this.cliente.mostrarSituacaoJogador(1, "aguardando jogadores...");
				this.clienteRecebe = new ClienteTCP(this.conexao, this.cliente, this.entrada, this.saida, this.jogador,
						this.jogadoresConectados);
				this.clienteRecebe.start();
			} else {
				return false;
			}
		} catch (IOException e) {
			this.cliente.mostrarMensagemErro("Falha na conexão",
					"Seu ip: " + host + "\nVocê não possui um servidor ativo");
			return false;
		} catch (ClassNotFoundException e) {

		}
		return true;
	}

	public boolean conectar(String host, Jogador novoJogador) {
		try {
			this.jogador = novoJogador;
			if (host.equals("localhost"))
				host = "127.0.0.1";
			this.conexao = new Socket(host, 12345);
			this.saida = new ObjectOutputStream(this.conexao.getOutputStream());
			saida.writeObject("Conectar");
			saida.writeObject(this.jogador);
			this.entrada = new ObjectInputStream(this.conexao.getInputStream());
			try {
				Boolean comecou = (Boolean) entrada.readObject();
				System.out.println("Recebeu " + comecou);
				if (!comecou) {
					@SuppressWarnings("unchecked")
					List<Jogador> jogadores = (List<Jogador>) entrada.readObject();
					System.out.println("Jogadores recebidos:");
					int i = 1;
					if (jogadores != null) {
						for (Jogador jogadorArray : jogadores) {
							System.out.println(i + " - " + jogadorArray.getNome());
							this.jogadoresConectados.add(jogadorArray);
							this.cliente.adicionarJogador(jogadorArray.getNome(), jogadorArray.getCivilizacao(),
									jogadorArray.getIpServidor(), jogadorArray.getSituacao());
							this.cliente.mostrarSituacaoJogador(i, jogadorArray.getSituacao());
							i++;
						}
						System.out.println("Adicionei " + (i - 1) + " jogadores");
					}
					this.clienteRecebe = new ClienteTCP(this.conexao, this.cliente, this.entrada, this.saida,
							this.jogador, this.jogadoresConectados);
					this.clienteRecebe.start();
				} else {
					return false;
				}
			} catch (ClassNotFoundException e) {
			}
		} catch (UnknownHostException e) {
			this.cliente.mostrarMensagemErro("Falha na conexão", "O ip " + host + " não possui um servidor");
			return false;
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	public void lancarPraga(String praga, String nomeAlvo) {
		try {
			this.saida.writeObject("Praga");
			System.out.println("=================PRAGA===============");
			this.saida.writeObject(nomeAlvo);
			System.out.println("Enviou o " + nomeAlvo + " para o servidor");
			this.saida.writeObject(praga);
			System.out.println("Enviou a praga " + praga + " para o servidor");
			this.saida.writeObject(this.jogador.getNome());
			System.out.println("Eu, " + this.jogador.getNome() + " me enviei para o servidor");
		} catch (IOException e) {
			System.out.println("Comunicação com o servidor falhou");
		}

	}

	public void iniciarJogo() {
		try {
			this.saida.writeObject("Iniciar jogo");
		} catch (IOException e) {
			System.out.println(this.conexao.getInetAddress().getHostAddress() + " falhou em se conectar ao servidor");
		}
	}

	public void vencer() {
		try {
			this.saida.writeObject("Vencer");
			this.saida.writeObject(this.jogador);
		} catch (IOException e) {

		}

	}

	public void desconectar() {
		try {
			this.saida.writeObject("CMD|DESCONECTAR");
			this.saida.writeObject(this.jogador);
		} catch (IOException e) {
			System.out.println(this.conexao.getInetAddress().getHostAddress() + " falhou em se conectar ao servidor");
		}
	}

	public void enviarMensagem(String mensagem) {
		try {
			DateFormat formato = new SimpleDateFormat("HH:mm");
			this.saida.writeObject("Mensagem");
			this.saida.writeObject("(" + formato.format(new Date()) + ")" + this.jogador.getNome() + "["
					+ this.jogador.getCivilizacao() + "]: " + mensagem);
			System.out.println("Mensagem enviada: " + mensagem);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Socket getConexao() {
		return conexao;
	}

	public String getNome() {
		return this.jogador.getNome();
	}

}