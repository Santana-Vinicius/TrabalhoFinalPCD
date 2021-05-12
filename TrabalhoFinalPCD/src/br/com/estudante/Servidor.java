package br.com.estudante;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import br.com.estudante.tela.Tela;

public class Servidor extends Thread {
	private boolean vivo = true;
//	private List<ObjectInputStream> vilasConectadas = new ArrayList<ObjectInputStream>();
	private List<Jogador> jogadores = new ArrayList<Jogador>();
	private ServerSocket servidor;
	private Tela principal;

	public Servidor(Tela principal) {
		try {
			this.servidor = new ServerSocket(12345);
			this.principal = principal;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			while (true) {
				System.out.println("Servidor Inicializado no IP: " + this.servidor.getInetAddress().getHostAddress()
						+ ":" + this.servidor.getLocalPort());
				Socket socket = servidor.accept();
				ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream());
				Jogador novo = (Jogador) entrada.readObject();
				this.jogadores.add(novo);

				principal.adicionarJogador(novo.getNome(), novo.getCivilizacao(), novo.getIpServidor(),
						novo.getSituacao());
				principal.mostrarSituacaoJogador(jogadores.size(), novo.getSituacao());

//				ObjectOutputStream saida = new ObjectOutputStream(socket.getOutputStream());

				System.out.println(
						"Cliente conectado: " + socket.getInetAddress().getHostAddress() + ":" + socket.getPort());

			}
		} catch (IOException e) {
			System.out.println("IOException");
		} catch (ClassNotFoundException e) {
			System.out.println("Jogador não encontrado!");
		}

	}

	public boolean isVivo() {
		return vivo;
	}

	public void setVivo(boolean vivo) {
		this.vivo = vivo;
	}

}
