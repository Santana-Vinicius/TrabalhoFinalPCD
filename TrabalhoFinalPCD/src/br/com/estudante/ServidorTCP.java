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
						saida.writeObject(this.jogadores);
						System.out.println("Retornou lista com " + this.jogadores.size() + " jogadores");
						for (ObjectOutputStream jogador : saidas) {
							jogador.writeObject(conectado);
							jogador.writeObject(jogadores.size());
						}
						synchronized (this.saidas) {
							this.saidas.add(saida);
						}
						break;

					}
				}
			} catch (SocketException e) {
			} // Fechado no cliente sem desconectar
			synchronized (this.saidas) {
				this.saidas.remove(saida);
			}
			saida.close();
			entrada.close();
			System.out.println("Cliente desconectado: " + conexao.getInetAddress().getHostAddress());
			this.conexao.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
