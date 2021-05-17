package br.com.estudante;

import java.io.Serializable;

public class Jogador implements Serializable {

	private static final long serialVersionUID = 1L;

	private String nome;
	private String civilizacao;
	private String ipServidor;
	private String situacao;
	
	public Jogador() {

	}

	public Jogador(String nome, String civilizacao, String ipServidor, String situacao, Vila vila) {
		setNome(nome);
		setCivilizacao(civilizacao);
		setIpServidor(ipServidor);
		setSituacao(situacao);
	}
	

//	@Override
//	public void run() {
//		System.out.println(this.nome + " INICIOU");
//			try {
//				Socket socket = new Socket("localhost", 12345);
//				ObjectOutputStream saida = new ObjectOutputStream(socket.getOutputStream());
//				saida.writeObject(this.nome);
//				System.out.println("Enviei meu nome");
//				ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream());
//				Jogador novo = (Jogador) entrada.readObject();
//				principal.adicionarJogador(novo.getNome(), novo.getCivilizacao(), novo.getIpServidor(), novo.getSituacao());
//				int i = (int) entrada.readObject();
//				principal.mostrarSituacaoJogador(i, novo.getSituacao());
//				socket.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch(ClassNotFoundException e) {
//				e.printStackTrace();
//			}
//
//		
//	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCivilizacao() {
		return civilizacao;
	}

	public void setCivilizacao(String civilizacao) {
		this.civilizacao = civilizacao;
	}

	public String getIpServidor() {
		return ipServidor;
	}

	public void setIpServidor(String ipServidor) {
		this.ipServidor = ipServidor;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	

}
