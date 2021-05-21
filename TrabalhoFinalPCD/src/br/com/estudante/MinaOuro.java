package br.com.estudante;

import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;

import br.com.estudante.Utils.Utils;
import br.com.estudante.tela.Tela;

public class MinaOuro {
	private String nome;
	private int capacidade;
	private Tela tela;
	private String nomeAldeoes;
	private ArrayList<Aldeao> mineradores = new ArrayList<Aldeao>();

	MinaOuro(String nome, Tela tela) {
		this.setPrincipal(tela);
		this.nomeAldeoes = "";
		setNome(nome);
		setCapacidade(5);
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String name) {
		this.nome = name;
	}

	public synchronized int getCapacidade() {
		return capacidade;
	}

	public void setCapacidade(int capacidade) {
		this.capacidade = capacidade;
	}

	public Tela getPrincipal() {
		return tela;
	}

	public void setPrincipal(Tela tela) {
		this.tela = tela;
	}

	public void setNomeAldeoes() {
		this.nomeAldeoes = "";
		SortedSet<String> nomes = new TreeSet<String>();

		for (Aldeao minerador : mineradores)
			nomes.add(String.valueOf(Integer.valueOf(minerador.getNome()) + 1));

		for (String nomeAldeao : nomes) {
			this.nomeAldeoes += nomeAldeao + " ";
		}
		this.tela.mostrarMinaOuro(Integer.valueOf(this.getNome()), nomeAldeoes);
	}

	public synchronized void addMinerador(Aldeao aldeao) {
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

	public boolean procuraAldeao(String nome) {
		for (Aldeao aldeao : mineradores) {
			if (aldeao.getNome().equals(nome))
				return true;
		}
		return false;
	}

	public ArrayList<Aldeao> getMineradores() {
		return this.mineradores;
	}

	public synchronized void removeMinerador(Aldeao aldeao) {
		this.mineradores.remove(aldeao);
		this.setNomeAldeoes();
	}

	public String getNomeAldeoes() {
		return nomeAldeoes;
	}

	public synchronized int getQtdMineradores() {
		return this.mineradores.size();
	}

	/*
	 * Funcoes da Mina
	 */
	public Integer minerar(int nivelAldeao) {

		return Utils.calculaOuro(nivelAldeao);
	}

}
