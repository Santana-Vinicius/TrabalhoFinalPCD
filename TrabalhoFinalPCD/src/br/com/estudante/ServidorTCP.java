package br.com.estudante;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class ServidorTCP extends Thread {
	private Socket conexao;
	private List<ObjectOutputStream> saidas;
	private List<Jogador> jogadores;

	private ServidorTCP(Socket conexao, List<ObjectOutputStream> saidas, List<Jogador> jogadores) {
		System.out.println("Cliente conectado: " + conexao.getInetAddress().getHostAddress() + ":" + conexao.getPort());
		this.conexao = conexao;
		this.saidas = saidas;
		this.jogadores = jogadores;
	}

	public static void main(String[] args) {
		System.out.println("Servidor levantado...");
		List<ObjectOutputStream> saidas = new ArrayList<ObjectOutputStream>();
		List<Jogador> jogadores = new ArrayList<Jogador>();
		try {
			@SuppressWarnings("resource")
			ServerSocket servidor = new ServerSocket(12345);
			while (true) {
				System.out.println("====Main Server=====");
				System.out.println("Qtd jogadores: " + jogadores.size());
				System.out.println("Qtd saidas: " + saidas.size());
				Socket conexao = servidor.accept();
				(new ServidorTCP(conexao, saidas, jogadores)).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		ObjectInputStream entrada = null;
		ObjectOutputStream saida = null;
		try {
			entrada = new ObjectInputStream(this.conexao.getInputStream());
			saida = new ObjectOutputStream(this.conexao.getOutputStream());
			String acao;
			try {
				while (!(acao = (String) entrada.readObject()).equals("CMD|DESCONECTAR")) {
					switch (acao) {
					case "Criar jogo":
						if (this.jogadores.size() < 1) {
							saida.writeObject(Boolean.FALSE);
							Jogador host = (Jogador) entrada.readObject();
							System.out.println(host.getNome() + " recebido no servidor");
							synchronized (this.jogadores) {
								this.jogadores.add(host);
							}
							System.out.println("QTD JOGADORES: " + jogadores.size());
							synchronized (this.saidas) {
								this.saidas.add(saida);
							}
						} else {
							saida.writeObject(Boolean.TRUE);
							this.saidas.remove(saida);
							saida.close();
							System.out.println("Fecha saída");
							entrada.close();
							System.out.println("Fecha entrada");
							this.conexao.close();
						}
						break;

					case "Conectar":
						Jogador conectado = (Jogador) entrada.readObject();
						System.out.println(conectado.getNome() + " recebido no servidor");
						if (!this.jogadores.get(0).getSituacao().equals("Jogando")) {
							synchronized (this.jogadores) {
								this.jogadores.add(conectado);
							}
							System.out.println("QTD JOGADORES: " + jogadores.size());
							saida.writeObject(Boolean.FALSE);
							saida.writeObject(this.jogadores);
							System.out.println("Retornou lista com " + this.jogadores.size() + " jogadores");
							for (ObjectOutputStream jogador : saidas) {
								jogador.writeObject("Adicionar jogador");
								jogador.writeObject(conectado);
								jogador.writeObject(this.jogadores.size());
							}
							synchronized (this.saidas) {
								this.saidas.add(saida);
							}
						} else {
							saida.writeObject(Boolean.TRUE);
							saida.close();
							System.out.println("Fecha saída");
							entrada.close();
							System.out.println("Fecha entrada");
							this.conexao.close();
						}
						break;
					case "Mensagem":
						String novaMensagem = (String) entrada.readObject();
						for (ObjectOutputStream jogador : saidas) {
							jogador.writeObject("Adicionar mensagem");
							jogador.writeObject(novaMensagem);
						}
						break;

					case "Iniciar jogo":
						for (Jogador jogador : jogadores) {
							jogador.setSituacao("Jogando");
						}
						for (ObjectOutputStream jogador : this.saidas) {
							jogador.writeObject("Iniciar jogo");
						}
						break;

					case "Praga":
						String nomeAlvo = (String) entrada.readObject();
						System.out.println("Nome do alvo: " + nomeAlvo);
						String praga = (String) entrada.readObject();
						System.out.println("Nome da praga: " + praga);
						String nomeAtacante = (String) entrada.readObject();
						System.out.println("Nome do atacante: " + nomeAtacante);
						int i = -1;
						for (Jogador jogador : jogadores) {
							i++;
							if (jogador.getNome().equals(nomeAlvo))
								break;
						}
						ObjectOutputStream saidaAlvo = this.saidas.get(i);
						saidaAlvo.writeObject("Recebendo praga");
						System.out.println("Envia Recebendo praga para o " + nomeAlvo);
						saidaAlvo.writeObject(nomeAtacante);
						System.out.println("Envia o " + nomeAtacante + " para o " + nomeAlvo);
						saidaAlvo.writeObject(praga);
						System.out.println("Envia a praga " + praga + " para o " + nomeAlvo);
						break;

					case "Resultado ataque":
						String retorno = (String) entrada.readObject();
						System.out.println("Recebeu o resultado " + retorno);
						String atacante = (String) entrada.readObject();
						System.out.println("Recebeu o jogador atacante: " + atacante);
						String alvo = (String) entrada.readObject();
						System.out.println("Recebeu o jogador alvo: " + alvo);
						String nomePraga = (String) entrada.readObject();
						System.out.println("Recebeu a praga: " + nomePraga);
						i = -1;
						for (Jogador jogador : jogadores) {
							i++;
							if (jogador.getNome().equals(atacante))
								break;
						}
						ObjectOutputStream saidaAtacante = this.saidas.get(i);
						System.out.println("Pegou a saída do jogador " + i + ": " + this.jogadores.get(i).getNome());
						saidaAtacante.writeObject("Mostra resultado ataque");
						saidaAtacante.writeObject(retorno);
						System.out.println("Retornou " + retorno + " para o atacante");
						saidaAtacante.writeObject(alvo);
						System.out.println("Retornou " + alvo + " para o atacante");
						saidaAtacante.writeObject(nomePraga);
						System.out.println("Retornou " + nomePraga + " para o atacante");
						break;

					case "Vencer":
						Jogador jogadorVencedor = (Jogador) entrada.readObject();

						for (ObjectOutputStream saidaJogador : saidas) {
							saidaJogador.writeObject("Terminar");
							saidaJogador.writeObject(jogadorVencedor);
						}
						break;
					}
				}
			} catch (SocketException e) {
			} // Fechado no cliente sem desconectar
			Jogador jogadorDesconectado = (Jogador) entrada.readObject();

			if ((this.jogadores.get(0)).equals(jogadorDesconectado)) {
				for (int i = this.saidas.size() - 1; i > 0; i--) {
					System.out.println("Manda Fechar para a saída " + i);
					saidas.get(i).writeObject("Fechar");
					saidas.get(i).close();
					System.out.println("Saída " + i + " fechada");
				}
				System.out.println("Manda Fechar para o host ");
				saida.writeObject("Fechar");
				this.saidas.removeAll(saidas);
				System.out.println("Zera a lista de saidas");
				this.jogadores.removeAll(jogadores);
				System.out.println("Zera a lista de jogadores");
				this.saidas = new ArrayList<ObjectOutputStream>();
				this.jogadores = new ArrayList<Jogador>();
			} else {
				System.out.println("Manda Fechar para o jogador " + jogadorDesconectado.getNome());
				saida.writeObject("Fechar");

				System.out.println("QTD JOGADORES ANTES DE REMOVER: " + jogadores.size());
				synchronized (this.jogadores) {
					System.out.println("remove jogador do array");
					this.jogadores.remove(jogadorDesconectado);
				}
				System.out.println("QTD JOGADORES DEPOIS DE REMOVER: " + jogadores.size());

				System.out.println("===============================");

				System.out.println("QTD SAIDAS ANTES DE REMOVER: " + saidas.size());
				synchronized (this.saidas) {
					System.out.println("remove saida do array");
					this.saidas.remove(saida);
				}
				System.out.println("QTD SAIDAS DEPOIS DE REMOVER: " + saidas.size());

				System.out.println("===============================");

				int i = 0;
				for (ObjectOutputStream jogador : this.saidas) {
					System.out.println("Cliente " + i + ": " + jogadores.get(i).getNome());
					System.out.println("Servidor manda \"Remover jogador\"");
					jogador.writeObject("Remover jogador");
					System.out.println("Manda jogador desconectado");
					jogador.writeObject(jogadorDesconectado);
					i++;
				}

			}

			saida.close();
			System.out.println("Fecha saída");
			entrada.close();
			System.out.println("Fecha entrada");
			this.conexao.close();
			System.out.println("Cliente desconectado: " + conexao.getInetAddress().getHostAddress());
		} catch (IOException e) {
			int i = -1;
			if (saida != null) {
				for (ObjectOutputStream saidaJogador : this.saidas) {
					i++;
					if (saidaJogador.equals(saida))
						break;
				}
				saidas.remove(saida);
			}
			System.out.println("i = " + i);
			System.out.println("Qtd jogadores no servidor = " + this.jogadores.size());

			if (this.saidas.size() > 0 && i > -1) {
				Jogador jogadorDesc = this.jogadores.get(i);
				System.out.println("Jogador " + jogadorDesc.getNome() + " selecionado");
				try {
					if (jogadores.get(0).equals(jogadorDesc)) {
						System.out.println("Host está saindo sem desconectar");
						int j = 0;
						for (ObjectOutputStream saidaAux : this.saidas) {
							System.out.println("Manda Fechar para a saída " + j);
							saidaAux.writeObject("Fechar");
							System.out.println("Saída " + j + " fechada");
							saidaAux.close();
							j++;
						}
//						for (ObjectOutputStream saidaAux : this.saidas)
						this.saidas.removeAll(this.saidas);
//						System.out.println("Zera a lista de saidas");
						this.jogadores.removeAll(this.jogadores);
//						System.out.println("Zera a lista de jogadores");
//						this.saidas = new ArrayList<ObjectOutputStream>();
//						this.jogadores = null;
//						this.jogadores = new ArrayList<Jogador>();
						System.out.println("Tamanho saidas: " + this.saidas.size());
						System.out.println("Tamanho jogadores: " + this.jogadores.size());
					} else {
						this.jogadores.remove(jogadorDesc);
						for (ObjectOutputStream jogador : this.saidas) {
							System.out.println("Servidor manda \"Remover jogador\"");
							jogador.writeObject("Remover jogador");
							System.out.println("Manda jogador desconectado");
							jogador.writeObject(jogadorDesc);
						}
					}
				} catch (IOException e1) {
					System.out.println("Não foi possível enviar o jogador desconectado para os clientes");
				}

			}
			try {
				saida.close();
				conexao.close();
				System.out.println("Conexão fechada");
			} catch (IOException e1) {

			}

			System.out.println("Comunicação com o cliente terminou.");
		} catch (ClassNotFoundException e) {
			System.out.println("Conversão deu erro...");
		}
	}
}
