package br.com.estudante;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import br.com.estudante.tela.Tela;

public class ClienteTCP extends Thread {
	private Socket conexao;
	private ObjectOutputStream saida;
	private ClienteTCP clienteRecebe;
	private boolean escutando;
	private Tela cliente;

	public ClienteTCP(Tela cliente) {
		this.cliente = cliente;
	}

	public ClienteTCP(Socket conexao, Tela cliente) {
		this.conexao = conexao;
		this.cliente = cliente;
		this.escutando = true;
	}

	public void run() {
		Jogador jogador;
		ObjectInputStream entrada = null;
		int i = 2;
		try {
			entrada = new ObjectInputStream(this.conexao.getInputStream());
			while (this.escutando) {
				jogador = (Jogador) entrada.readObject();
				cliente.adicionarJogador(jogador.getNome(), jogador.getCivilizacao(), jogador.getIpServidor(),
						jogador.getSituacao());
				cliente.mostrarSituacaoJogador(i, jogador.getSituacao());
				i++;
			}
			entrada.close();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public boolean conectar(String host) {

		try {
			this.conexao = new Socket(host, 12345);
			ObjectOutputStream saida = new ObjectOutputStream(conexao.getOutputStream());
			saida.writeObject("Cliente TCP");
			this.clienteRecebe = new ClienteTCP(conexao, this.cliente);
			this.clienteRecebe.start();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return true;

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