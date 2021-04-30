package br.com.estudante;

import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;

import br.com.estudante.Utils.Utils;
import br.com.estudante.tela.Principal;

public class Fazenda {
	private String nome;
	private int capacidade;
	private Principal principal;
	private String nomeAldeoes;
	private ArrayList<Aldeao> fazendeiros = new ArrayList<Aldeao>();

	Fazenda(String nome, Principal principal) {
		this.setPrincipal(principal);
		this.nomeAldeoes = "";
		setNome(nome);
		setCapacidade(5);
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

	public synchronized int getCapacidade() {
		return capacidade;
	}

	public void setCapacidade(int capacidade) {
		this.capacidade = capacidade;
	}

	public synchronized void setNomeAldeoes() {
		this.nomeAldeoes = "";
		SortedSet<String> nomes = new TreeSet<String>();

		for (Aldeao fazendeiro : fazendeiros)
			nomes.add(String.valueOf(Integer.valueOf(fazendeiro.getNome()) + 1));

		for (String nomeAldeao : nomes) {
			this.nomeAldeoes += nomeAldeao + " ";
		}
		this.principal.mostrarFazenda(Integer.valueOf(this.getNome()), nomeAldeoes);

	}

	public synchronized void addFazendeiro(Aldeao aldeao) {
		boolean tem = false;
		for (Aldeao fazendeiro : fazendeiros) {
			if (fazendeiro.equals(aldeao))
				tem = true;
		}
		if (!tem) {
			this.fazendeiros.add(aldeao);
			this.setNomeAldeoes();
		}
	}

	public synchronized void removeFazendeiro(Aldeao aldeao) {
		this.fazendeiros.remove(aldeao);
		this.setNomeAldeoes();
	}

	public synchronized String getNomeAldeoes() {
		return nomeAldeoes;
	}

	public synchronized int getQtdFazendeiros() {
		return this.fazendeiros.size();
	}

	/*
	 * Funcoes da Fazenda
	 */
	public Integer cultivar(int nivelAldeao) {

		return Utils.calculaComida(nivelAldeao);
	}

}
