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

	public synchronized String getNome() {
		return nome;
	}

	public synchronized void setNome(String name) {
		this.nome = name;
	}

	public synchronized int getCapacidade() {
		return capacidade;
	}

	public synchronized void setCapacidade(int capacidade) {
		this.capacidade = capacidade;
	}

	public synchronized Tela getPrincipal() {
		return tela;
	}

	public synchronized void setPrincipal(Tela tela) {
		this.tela = tela;
	}

	public synchronized void setNomeAldeoes() {
		this.nomeAldeoes = "";
		SortedSet<String> nomes = new TreeSet<String>();

		if (this.mineradores.size() > 0) {
			for (Aldeao minerador : mineradores)
				nomes.add(String.valueOf(Integer.valueOf(minerador.getNome()) + 1));

			for (String nomeAldeao : nomes) {
				this.nomeAldeoes += nomeAldeao + " ";
			}
		}
		this.tela.mostrarMinaOuro(Integer.valueOf(this.getNome()), nomeAldeoes);
	}

	public synchronized void addMinerador(Aldeao aldeao) {
		boolean tem = false;
		for (Aldeao minerador : getMineradores()) {
			if (minerador.equals(aldeao))
				tem = true;
		}
		if (!tem) {
			this.mineradores.add(aldeao);
			this.setNomeAldeoes();
		}

	}

	public synchronized boolean procuraAldeao(String nome) {
		for (Aldeao aldeao : getMineradores()) {
			if (aldeao.getNome().equals(nome))
				return true;
		}
		return false;
	}

	public synchronized ArrayList<Aldeao> getMineradores() {
		return this.mineradores;
	}

	public synchronized void removeMinerador(Aldeao aldeao) {
//		System.out.println("Qtd mineradores (Mina " + this.getNome() + ") antes: " + this.getQtdMineradores());
		this.getMineradores().remove(aldeao);
//		System.out.println("Qtd mineradores (Mina " + this.getNome() + ") depois: " + this.getQtdMineradores());
		this.setNomeAldeoes();
	}

	public synchronized String getNomeAldeoes() {
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
