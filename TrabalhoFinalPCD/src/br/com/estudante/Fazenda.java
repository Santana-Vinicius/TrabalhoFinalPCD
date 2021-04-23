package br.com.estudante;

import br.com.estudante.Utils.Utils;
import br.com.estudante.tela.Principal;

public class Fazenda {
	private String nome;
	private Principal principal;
	private String nomeAldeoes;

	Fazenda(String nome, Principal principal) {
		this.setPrincipal(principal);
		this.nomeAldeoes = "";
		setNome(nome);
	}

	/*
	 * Getters and Setters
	 */
	public String getNome() {
		return nome;
	}

	public void setNome(String name) {
		this.nome = name;
	}

	public Principal getPrincipal() {
		return principal;
	}

	public void setPrincipal(Principal principal) {
		this.principal = principal;
	}

	public void setNomeAldeoes(String nomeAldeoes) {
		this.nomeAldeoes = nomeAldeoes;
	}

	public String getNomeAldeoes() {
		return nomeAldeoes;
	}

	/*
	 * Funcoes da Fazenda
	 */
	public Integer cultivar(int nivelAldeao) {

		return Integer.valueOf(Utils.calculaComida(nivelAldeao));
	}
	

}
