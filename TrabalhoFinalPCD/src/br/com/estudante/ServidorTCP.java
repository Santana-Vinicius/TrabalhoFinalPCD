package br.com.estudante;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
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
		Integer i = 0;
		try {
			@SuppressWarnings("resource")
			ServerSocket servidor = new ServerSocket(12345);
			while (true) {
				System.out.println("Giro " + (++i));
				Socket conexao = servidor.accept();
				System.out.println("Opa, alguém se conectou!");
				(new ServidorTCP(conexao, saidas, jogadores)).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {

		Boolean flag = Boolean.TRUE;
		ObjectInputStream entrada = null;
		ObjectOutputStream saida = null;
		while (flag) {

			try {
				entrada = new ObjectInputStream(this.conexao.getInputStream());
				saida = new ObjectOutputStream(this.conexao.getOutputStream());

				String acao = (String) entrada.readObject();
				switch (acao) {
				case "Criar jogo":
					Jogador host = (Jogador) entrada.readObject();
					this.jogadores.add(host);
					System.out.println(this.saidas.size());
					break;

				case "Conectar":
					Jogador conectado = (Jogador) entrada.readObject();
					this.jogadores.add(conectado);
					saida.writeObject(this.jogadores);
					System.out.println(this.saidas.size());
					for (ObjectOutputStream jogador : saidas) {
						jogador.writeObject(conectado);
					}
					break;
				case "Cliente TCP":
					System.out.println("Recebi um TCP");
					synchronized (this.saidas) {
						this.saidas.add(saida);
					}
					break;

				default:
					flag = Boolean.FALSE;
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			saida.close();
			entrada.close();
			System.out.println("Cliente desconectado: " + conexao.getInetAddress().getHostAddress());
			this.conexao.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
