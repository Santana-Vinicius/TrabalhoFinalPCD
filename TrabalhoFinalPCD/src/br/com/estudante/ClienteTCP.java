package br.com.estudante;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import br.com.estudante.tela.Tela;

public class ClienteTCP extends Thread {
	private Socket conexao;
	private ObjectOutputStream saida;
	private ObjectInputStream entrada;
	private ClienteTCP clienteRecebe;
	private boolean escutando;
	private Tela cliente;

	public ClienteTCP(Tela cliente) {
		this.cliente = cliente;
	}

	public ClienteTCP(Socket conexao, Tela cliente, ObjectInputStream entrada, ObjectOutputStream saida) {
		this.conexao = conexao;
		this.cliente = cliente;
		this.entrada = entrada;
		this.saida = saida;
		this.escutando = true;
	}

	public void run() {

		try {
			Jogador jogador = null;
			while (this.escutando) {
				System.out.println("Aqui");
				System.out.println("Esperando alguém");
				jogador = (Jogador) entrada.readObject();
				int numJogador = (int) entrada.readObject();
				System.out.println("Recebeu o " + jogador.getNome());
				this.cliente.adicionarJogador(jogador.getNome(), jogador.getCivilizacao(), jogador.getIpServidor(),
						jogador.getSituacao());
				this.cliente.mostrarSituacaoJogador(numJogador, jogador.getSituacao());
			}
			System.out.println("Aqui");
			entrada.close();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void criarServidor(String host, Jogador jogadorHost) {
		try {
			this.conexao = new Socket(host, 12345);
			this.saida = new ObjectOutputStream(this.conexao.getOutputStream());
			this.entrada = new ObjectInputStream(this.conexao.getInputStream());
			saida.writeObject("Criar jogo");
			saida.writeObject(jogadorHost);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.cliente.adicionarJogador(jogadorHost.getNome(), jogadorHost.getCivilizacao(), jogadorHost.getIpServidor(),
				"aguardando jogadores...");
		this.cliente.mostrarSituacaoJogador(1, "aguardando jogadores...");
		this.clienteRecebe = new ClienteTCP(this.conexao, this.cliente, this.entrada, this.saida);
		this.clienteRecebe.start();
	}

	public void conectar(String host, Jogador novoJogador) {

		try {
			this.conexao = new Socket(host, 12345);
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

			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.clienteRecebe = new ClienteTCP(this.conexao, this.cliente, this.entrada, this.saida);
		System.out.println(clienteRecebe + " | " + this.conexao);
		this.clienteRecebe.start();
	}

	public void desconectar() {
		try {
			this.saida.writeObject("CMD|DESCONECTAR");
			this.clienteRecebe.escutando = false;
			this.saida.close();
			this.conexao.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void enviarMensagem(String mensagem) {
		try {
			this.saida.writeObject(mensagem);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Socket getConexao() {
		return conexao;
	}

}