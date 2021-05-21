package br.com.estudante;

import java.io.Serializable;

public class Jogador implements Serializable {

	private static final long serialVersionUID = 1L;

	private String nome;
	private String civilizacao;
	private String ipServidor;
	private String situacao;
	private String ipLocal;
	
	public Jogador() {

	}

	public Jogador(String nome, String civilizacao, String ipServidor, String ipLocal, String situacao, Vila vila) {
		setNome(nome);
		setCivilizacao(civilizacao);
		setIpServidor(ipServidor);
		setSituacao(situacao);
		setIpLocal(ipLocal);
	}

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

	public String getIpLocal() {
		return ipLocal;
	}

	public void setIpLocal(String ipLocal) {
		this.ipLocal = ipLocal;
	}

	

}
