package br.com.estudante;

import java.util.ArrayList;

import br.com.estudante.tela.Tela;

public class Maravilha extends Thread {
	private ArrayList<Aldeao> construtores = new ArrayList<Aldeao>();
	private Prefeitura prefeitura;
	private Tela tela;
	private Boolean desconectou = false;
	private int qtdTijolos = 0;

	Maravilha(Prefeitura prefeitura, Tela tela) {
		setPrefeitura(prefeitura);
		setPrincipal(tela);
	}

	public void run() {
		while (this.getQtdTijolos() != 100000 && !desconectou) {
			try {
				synchronized (this) {
					this.wait();
				}
			} catch (InterruptedException e) {
				desconectou = true;
			}
			this.tela.mostrarMaravilha(this.qtdTijolos);
		}
		if (!desconectou) {
			this.tela.mostrarMaravilha(this.qtdTijolos);
			this.tela.getClienteTCP().vencer();
		}

	}

	public int getQtdConstrutores() {
		return this.construtores.size();
	}

	public void addConstrutor(Aldeao aldeao) {
		this.construtores.add(aldeao);
	}

	public void removeConstrutor(Aldeao aldeao) {
		this.construtores.remove(aldeao);
	}

	public Prefeitura getPrefeitura() {
		return prefeitura;
	}

	public void procuraRemoveConstrutor(Aldeao aldeao) {
		if (this.construtores.contains(aldeao))
			this.construtores.remove(aldeao);
	}

	public void setPrefeitura(Prefeitura prefeitura) {
		this.prefeitura = prefeitura;
	}

	public ArrayList<Aldeao> getConstrutores() {
		return this.construtores;
	}

	public Tela getPrincipal() {
		return tela;
	}

	public void setPrincipal(Tela tela) {
		this.tela = tela;
	}

	public synchronized int getQtdTijolos() {
		return qtdTijolos;
	}

	public synchronized void setQtdTijolos() {
		this.qtdTijolos++;
	}

	public synchronized void setQtdTijolos(int qtdTijolos) {
		this.qtdTijolos += qtdTijolos;
	}

}
