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
		System.out.println("Cliente conectado: " + conexao.getInetAddress() + ":" + conexao.getPort());
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
				Socket conexao = servidor.accept();
				(new ServidorTCP(conexao, saidas, jogadores)).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {

		try {
			ObjectInputStream entrada = new ObjectInputStream(this.conexao.getInputStream());
			ObjectOutputStream saida = new ObjectOutputStream(this.conexao.getOutputStream());
			String acao;
			try {
				while (!(acao = (String) entrada.readObject()).equals("CMD|DESCONECTAR")) {
					switch (acao) {
					case "Criar jogo":
						Jogador host = (Jogador) entrada.readObject();
						System.out.println(host.getNome() + " recebido no servidor");
						synchronized (this.jogadores) {
							this.jogadores.add(host);
						}
						System.out.println("QTD JOGADORES: " + jogadores.size());
						synchronized (this.saidas) {
							this.saidas.add(saida);
						}
						break;

					case "Conectar":
						Jogador conectado = (Jogador) entrada.readObject();
						System.out.println(conectado.getNome() + " recebido no servidor");
						synchronized (this.jogadores) {
							this.jogadores.add(conectado);
						}
						System.out.println("QTD JOGADORES: " + jogadores.size());
						saida.writeObject(this.jogadores);
						System.out.println("Retornou lista com " + this.jogadores.size() + " jogadores");
						for (ObjectOutputStream jogador : saidas) {
							jogador.writeObject("Atualizar lista");
							jogador.writeObject(conectado);
							jogador.writeObject(jogadores.size());
						}
						synchronized (this.saidas) {
							this.saidas.add(saida);
						}
						break;
					case "Mensagem":
						String novaMensagem = (String) entrada.readObject();
						for (ObjectOutputStream jogador : saidas) {
							jogador.writeObject("Adicionar mensagem");
							jogador.writeObject(novaMensagem);
						}
						break;
					}
				}
			} catch (SocketException e) {
			} // Fechado no cliente sem desconectar

			Jogador jogadorDesconectado = (Jogador) entrada.readObject();
			System.out.println("QTD JOGADORES ANTES DE REMOVER: " + jogadores.size());
			synchronized (this.jogadores) {
				System.out.println("remove jogador do array");
				this.jogadores.remove(jogadorDesconectado);
			}
			System.out.println("QTD JOGADORES DEPOIS DE REMOVER: " + jogadores.size());

			System.out.println("manda \"Atualizar lista\"");
			saida.writeObject("Atualizar lista");
			System.out.println("manda jogador null");
			saida.writeObject(null);
			System.out.println("manda true para fechar");
			saida.writeObject(true);

			System.out.println("QTD SAIDAS ANTES DE REMOVER: " + saidas.size());
			synchronized (this.saidas) {
				System.out.println("remove saida do array");
				this.saidas.remove(saida);
			}
			System.out.println("QTD SAIDAS DEPOIS DE REMOVER: " + saidas.size());

			int i = 1;

			for (ObjectOutputStream jogador : saidas) {
				System.out.println("Cliente " + i);
				System.out.println("Servidor manda \"Atualizar lista\"");
				jogador.writeObject("Atualizar lista");
				System.out.println("Manda jogador = null");
				jogador.writeObject(null);
				System.out.println("Manda False para o fechar");
				jogador.writeObject(false);
				System.out.println("Manda lista com " + this.jogadores.size() + " jogadores: ");
				for (Jogador jogador2 : jogadores) {
					System.out.println(jogador2.getNome());
				}
				jogador.writeObject(this.jogadores);
				i++;
			}

			saida.close();
			entrada.close();
			this.conexao.close();
			System.out.println("Cliente desconectado: " + conexao.getInetAddress().getHostAddress());
		} catch (IOException e) {
			System.out.println("Comunicação com o cliente falhou...");
		} catch (ClassNotFoundException e) {
			System.out.println("Conversão deu erro...");
		}
	}
}
