package br.com.estudante;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import br.com.estudante.tela.Tela;

public class Servidor extends Thread {
	private boolean vivo = true;
	private List<Socket> telas = new ArrayList<Socket>();
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

		while (true) {
			System.out.println("Servidor Inicializado no IP: " + this.servidor.getInetAddress().getHostAddress() + ":"
					+ this.servidor.getLocalPort());
			try {
				Socket socket = servidor.accept();
				ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream());
				String acao = (String) entrada.readObject();

				switch (acao) {
				case "Criar jogo":
					Jogador novo = (Jogador) entrada.readObject();
					this.jogadores.add(novo);
					principal.adicionarJogador(novo.getNome(), novo.getCivilizacao(), novo.getIpServidor(),
							novo.getSituacao());
					principal.mostrarSituacaoJogador(jogadores.size(), novo.getSituacao());
					telas.add(socket);
					acao = "Default";
					break;
				case "Conectar":
					Jogador novoConectar = (Jogador) entrada.readObject();
					this.jogadores.add(novoConectar);
					telas.add(socket);

					principal.adicionarJogador(novoConectar.getNome(), novoConectar.getCivilizacao(),
							novoConectar.getIpServidor(), novoConectar.getSituacao());
					principal.mostrarSituacaoJogador(jogadores.size(), novoConectar.getSituacao());

					ObjectOutputStream saida = new ObjectOutputStream(socket.getOutputStream());
					saida.writeObject(jogadores);

					

					acao = "Default";
					break;
				}

			} catch (IOException e) {
				System.out.println("Deu erro!");
			} catch (ClassNotFoundException e) {
				System.out.println("Class not found!");
			}

		}

	}

	public boolean isVivo() {
		return vivo;
	}

	public void setVivo(boolean vivo) {
		this.vivo = vivo;
	}

}
