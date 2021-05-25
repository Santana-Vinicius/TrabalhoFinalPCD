package br.com.estudante;

import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;

import br.com.estudante.Utils.Utils;
import br.com.estudante.tela.Tela;

public class Fazenda {
	private String nome;
	private int capacidade;
	private Tela tela;
	private String nomeAldeoes;
	private ArrayList<Aldeao> fazendeiros = new ArrayList<Aldeao>();

	Fazenda(String nome, Tela tela) {
		this.setPrincipal(tela);
		this.nomeAldeoes = "";
		setNome(nome);
		setCapacidade(5);
	}

	/*
	 * Getters and Setters
	 */
	public synchronized String getNome() {
		return nome;
	}

	public synchronized void setNome(String name) {
		this.nome = name;
	}

	public synchronized Tela getPrincipal() {
		return tela;
	}

	public synchronized void setPrincipal(Tela tela) {
		this.tela = tela;
	}

	public synchronized int getCapacidade() {
		return capacidade;
	}

	public synchronized void setCapacidade(int capacidade) {
		this.capacidade = capacidade;
	}

	public synchronized void setNomeAldeoes() {
		this.nomeAldeoes = "";
		SortedSet<String> nomes = new TreeSet<String>();

		if (this.fazendeiros.size() > 0) {
			for (Aldeao fazendeiro : fazendeiros) {
				nomes.add(String.valueOf(Integer.valueOf(fazendeiro.getNome()) + 1));
			}

			for (String nomeAldeao : nomes) {
				this.nomeAldeoes += nomeAldeao + " ";
			}
		}
		this.tela.mostrarFazenda(Integer.valueOf(this.getNome()), nomeAldeoes);

	}

	public synchronized void addFazendeiro(Aldeao aldeao) {
		boolean tem = false;
		for (Aldeao fazendeiro : getFazendeiros()) {
			if (fazendeiro.equals(aldeao))
				tem = true;
		}
		if (!tem) {
			this.fazendeiros.add(aldeao);
			this.setNomeAldeoes();
		}
	}

	public synchronized void removeFazendeiro(Aldeao aldeao) {
		this.getFazendeiros().remove(aldeao);
//		System.out.println();
		this.setNomeAldeoes();
	}

	public boolean procuraAldeao(String nome) {
		for (Aldeao aldeao : fazendeiros) {
			if (aldeao.getNome().equals(nome))
				return true;
		}
		return false;
	}

	public synchronized String getNomeAldeoes() {
		return nomeAldeoes;
	}

	public synchronized int getQtdFazendeiros() {
		return this.fazendeiros.size();
	}

	public synchronized ArrayList<Aldeao> getFazendeiros() {
		return this.fazendeiros;
	}

	/*
	 * Funcoes da Fazenda
	 */
	public synchronized Integer cultivar(int nivelAldeao) {

		return Utils.calculaComida(nivelAldeao);
	}

}
