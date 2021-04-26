package br.com.estudante;

import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;

import br.com.estudante.Utils.Utils;
import br.com.estudante.tela.Principal;

public class MinaOuro {
	private String nome;
	private Principal principal;
	private String nomeAldeoes;
	private ArrayList<Aldeao> mineradores = new ArrayList<Aldeao>();

	MinaOuro(String nome, Principal principal) {
		this.setPrincipal(principal);
		this.nomeAldeoes = "";
		setNome(nome);
	}
	
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
	
	public void setNomeAldeoes() {
		this.nomeAldeoes = "";
		SortedSet<String> nomes = new TreeSet<String>();

		for (Aldeao minerador : mineradores)
			nomes.add(String.valueOf(Integer.valueOf(minerador.getNome()) + 1));

		for (String nomeAldeao : nomes) {
			this.nomeAldeoes += nomeAldeao + " ";
		}
		this.principal.mostrarMinaOuro(Integer.valueOf(this.getNome()), nomeAldeoes);
	}

	public void addMinerador(Aldeao aldeao) {
		boolean tem = false;
		for (Aldeao minerador : mineradores) {
			if (minerador.equals(aldeao))
				tem = true;
		}
		if (!tem) {
			this.mineradores.add(aldeao);
			this.setNomeAldeoes();
		}
		
	}

	public void removeMinerador(Aldeao aldeao) {
		this.mineradores.remove(aldeao);
		this.setNomeAldeoes();
	}

	public String getNomeAldeoes() {
		return nomeAldeoes;
	}

	public int getQtdMineradores() {
		return this.mineradores.size();
	}

	/*
	 * Funcoes da Mina
	 */
	public Integer minerar(int nivelAldeao) {

		return Utils.calculaOuro(nivelAldeao);
	}

}
