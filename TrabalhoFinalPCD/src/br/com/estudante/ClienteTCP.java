package br.com.estudante;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import br.com.estudante.tela.Tela;

public class ClienteTCP extends Thread {
	private Socket conexao;
	private ObjectOutputStream saida;
	private ObjectInputStream entrada;
	private ClienteTCP clienteRecebe;
	private String novaMensagem;
	private boolean escutando;
	private Tela cliente;
	private Jogador jogador;

	public ClienteTCP(Tela cliente) {
		this.cliente = cliente;
	}

	public ClienteTCP(Socket conexao, Tela cliente, ObjectInputStream entrada, ObjectOutputStream saida,
			Jogador jogador) {
		this.conexao = conexao;
		this.cliente = cliente;
		this.entrada = entrada;
		this.saida = saida;
		this.jogador = jogador;
		this.escutando = true;
	}

	public void run() {

		try {
			Jogador jogadorAux = null;
			int i = 1;
			while (this.escutando) {
				System.out.println("Esperando o que fazer");
				String fazer = (String) entrada.readObject();
				if (fazer.equals("Atualizar lista")) {
					System.out.println("Esperando alguém");
					jogadorAux = (Jogador) entrada.readObject();
					if (jogadorAux != null) {
						int numJogador = (int) entrada.readObject();
						System.out.println("Recebeu o " + jogadorAux.getNome());
						this.cliente.adicionarJogador(jogadorAux.getNome(), jogadorAux.getCivilizacao(),
								jogadorAux.getIpServidor(), jogadorAux.getSituacao());
						this.cliente.mostrarSituacaoJogador(numJogador, jogadorAux.getSituacao());
					} else {
						boolean fechar = (Boolean) entrada.readObject();
						if (!fechar) {
							System.out.println("Iniciando atualização da lista de jogadores");
							i = 1;
							this.cliente.limparJogadores();

							@SuppressWarnings("unchecked")
							List<Jogador> jogadores = (List<Jogador>) entrada.readObject();

							System.out.println("Array de tamanho " + jogadores.size() + " recebido");
							for (Jogador jogador2 : jogadores) {
								this.cliente.adicionarJogador(jogador2.getNome(), jogador2.getCivilizacao(),
										jogador2.getIpServidor(), jogador2.getSituacao());
								this.cliente.mostrarSituacaoJogador(i, jogador2.getSituacao());
								i++;
							}
							System.out.println("Jogadores recebidos:");
							for (Jogador jogador2 : jogadores) {
								System.out.println(jogador2.getNome());
							}
							System.out.println("Lista atualizada!");
						} else {
							this.escutando = false;
						}
					}
				} else if (fazer.equals("Adicionar mensagem")) {
					this.novaMensagem = (String) entrada.readObject();
					this.cliente.adicionarMensagem(this.novaMensagem);
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
	}

	public boolean criarServidor(String host, Jogador jogadorHost) {
		try {
			this.jogador = jogadorHost;
			this.conexao = new Socket(host, 12345);
			this.saida = new ObjectOutputStream(this.conexao.getOutputStream());
			this.entrada = new ObjectInputStream(this.conexao.getInputStream());
			saida.writeObject("Criar jogo");
			saida.writeObject(jogadorHost);

			this.cliente.adicionarJogador(jogadorHost.getNome(), jogadorHost.getCivilizacao(),
					jogadorHost.getIpServidor(), "aguardando jogadores...");
			this.cliente.mostrarSituacaoJogador(1, "aguardando jogadores...");
			this.clienteRecebe = new ClienteTCP(this.conexao, this.cliente, this.entrada, this.saida, jogadorHost);
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
			this.conexao = new Socket(host, 12345);
			this.jogador = novoJogador;
			this.saida = new ObjectOutputStream(this.conexao.getOutputStream());
			saida.writeObject("Conectar");
			saida.writeObject(novoJogador);
			this.entrada = new ObjectInputStream(this.conexao.getInputStream());
			try {

				@SuppressWarnings("unchecked")
				List<Jogador> jogadores = (List<Jogador>) entrada.readObject();
				System.out.println("Jogadores recebidos:");

				int i = 1;
				if (jogadores != null) {
					for (Jogador jogador : jogadores) {
						System.out.println(i + " - " + jogador.getNome());
						this.cliente.adicionarJogador(jogador.getNome(), jogador.getCivilizacao(),
								jogador.getIpServidor(), jogador.getSituacao());
						this.cliente.mostrarSituacaoJogador(i, jogador.getSituacao());
						i++;
					}
					System.out.println("Adicionei " + (i - 1) + " jogadores");
				}
				this.clienteRecebe = new ClienteTCP(this.conexao, this.cliente, this.entrada, this.saida, novoJogador);
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

	public void desconectar() {
		try {
			this.saida.writeObject("CMD|DESCONECTAR");
			this.saida.writeObject(this.jogador);
		} catch (IOException e) {
			System.out.println(this.conexao.getInetAddress().getHostAddress() + " desconectado");
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