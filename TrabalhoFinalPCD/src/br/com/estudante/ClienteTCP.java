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

		try {
			Jogador jogadorAux = null;
			Jogador jogadorDesc = null;
			int i = 1;
			while (this.escutando) {
				System.out.println("Esperando o que fazer");
				String fazer = (String) entrada.readObject();
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
					this.cliente.limparJogadores();
					i = 1;
					System.out.println("Esperando um jogador desconectado");
					jogadorDesc = (Jogador) entrada.readObject();
					System.out.println("Recebeu o " + jogadorDesc.getNome() + " para desconectar");
					this.jogadoresConectados.remove(jogadorDesc);
					System.out.println("Remove o jogador " + jogadorDesc.getNome());

					System.out.println("Jogadores na lista:");
					for (Jogador jogador2 : this.jogadoresConectados) {
						System.out.println(jogador2.getNome());
					}

					for (Jogador jogador2 : this.jogadoresConectados) {
						this.cliente.adicionarJogador(jogador2.getNome(), jogador2.getCivilizacao(),
								jogador2.getIpServidor(), jogador2.getSituacao());
						this.cliente.mostrarSituacaoJogador(i, jogador2.getSituacao());
						System.out.println("Adiciona e mostra o jogador " + jogador2.getNome() + " na posição " + i);
						i++;
					}

					System.out.println("Lista atualizada!");

				} else if (fazer.equals("Fechar")) {
					this.escutando = false;
					System.out.println("Não está mais escutando");

				} else if (fazer.equals("Iniciar jogo")) {
					System.out.println("Esperando uma lista");
					List<Jogador> jogadoresSemEsse = this.jogadoresConectados;
					jogadoresSemEsse.remove(this.jogador);
					for (Jogador jogadorInimigo : jogadoresSemEsse) {
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
									+ " tentou lançar uma nuvem de gafanhotos em você!\n Mas falhou porque você tem proteção!");
							this.saida.writeObject("Falha");
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
									+ " lançou uma nuvem de gafanhotos em você!\n Você perdeu metade das suas fazendas!");
						}
					}
				}
			}
			this.entrada.close();
			this.saida.close();
			this.conexao.close();
			System.out.println(this.jogador.getNome() + " desconectado.");
		} catch (IOException e) {
			System.out.println("Não foi possível receber o que queria do servidor...");
		} catch (ClassNotFoundException e) {
			System.out.println("Falha na conversão da Stream");
		}
		this.cliente.getTpJogo().setSelectedIndex(0);
		this.cliente.getTpJogo().setEnabledAt(1, false);
		this.cliente.limparMensagens();
		this.cliente.limparJogadores();
		this.cliente.limparMensagens();
		this.cliente.limparInimigos();
		this.cliente.setSituacaoInicio(SituacaoInicio.INICIAL_CRIAR);
		this.cliente.habilitarInicio();
		System.out.println("Thread " + this.jogador.getNome() + " morreu");
	}

	public boolean criarServidor(String host, Jogador jogadorHost) {
		try {
			this.jogador = jogadorHost;
			this.conexao = new Socket(host, 12345);
			this.saida = new ObjectOutputStream(this.conexao.getOutputStream());
			this.entrada = new ObjectInputStream(this.conexao.getInputStream());
			saida.writeObject("Criar jogo");
			saida.writeObject(this.jogador);
			this.jogadoresConectados.add(this.jogador);
			this.cliente.adicionarJogador(jogadorHost.getNome(), jogadorHost.getCivilizacao(),
					jogadorHost.getIpServidor(), "aguardando jogadores...");
			this.cliente.mostrarSituacaoJogador(1, "aguardando jogadores...");
			this.clienteRecebe = new ClienteTCP(this.conexao, this.cliente, this.entrada, this.saida, this.jogador,
					this.jogadoresConectados);
			this.clienteRecebe.start();
		} catch (IOException e) {
			this.cliente.mostrarMensagemErro("Falha na conexão",
					"Seu ip: " + host + "\nVocê não possui um servidor ativo");
			return false;
		}
		return true;
	}

	public boolean conectar(String host, Jogador novoJogador) {

		try {
			this.jogador = novoJogador;
			this.conexao = new Socket(host, 12345);
			this.saida = new ObjectOutputStream(this.conexao.getOutputStream());
			saida.writeObject("Conectar");
			saida.writeObject(this.jogador);
			this.entrada = new ObjectInputStream(this.conexao.getInputStream());
			try {

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
				this.clienteRecebe = new ClienteTCP(this.conexao, this.cliente, this.entrada, this.saida, this.jogador,
						this.jogadoresConectados);
				this.clienteRecebe.start();
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
			this.saida.writeObject(nomeAlvo);
			this.saida.writeObject(praga);
			this.saida.writeObject(this.jogador.getNome());
			switch ((String) this.entrada.readObject()) {
			case "Falha":
				this.cliente.mostrarMensagemErro("Situção do Ataque", "Seu ataque à vila do inimigo " + nomeAlvo
						+ " falhou!\n Ele tem uma proteção contra " + praga + "!");
				break;
			default:
				String complemento = "";
				switch (praga) {
				case "Nuvem de gafanhotos":
					complemento = "perdeu metade das fazendas!";
					break;
				}
				this.cliente.mostrarMensagemErro("Situção do Ataque",
						"Seu ataque à vila do inimigo " + nomeAlvo + " foi um sucesso!\n Ele " + complemento);
			}

		} catch (IOException e) {
			System.out.println("Comunicação com o servidor falhou");
		} catch (ClassNotFoundException e) {
			System.out.println("Conversão para string falhou");
		}

	}

	public void iniciarJogo() {
		try {
			this.saida.writeObject("Iniciar jogo");
		} catch (IOException e) {
			System.out.println(this.conexao.getInetAddress().getHostAddress() + " falhou em se conectar ao servidor");
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
			this.saida.writeObject(this.jogador.getNome() + "(" + formato.format(new Date()) + "): " + mensagem);
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