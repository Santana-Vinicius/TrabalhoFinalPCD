package br.com.estudante;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
	private boolean vivo = true;

	public static void main(String[] args) {

		try {
			ServerSocket servidor = new ServerSocket(12345);
			Socket socket = servidor.accept();
			ObjectOutputStream saida = new ObjectOutputStream(socket.getOutputStream());

		} catch (IOException e) {
		}

	}

	public boolean isVivo() {
		return vivo;
	}

	public void setVivo(boolean vivo) {
		this.vivo = vivo;
	}

}
