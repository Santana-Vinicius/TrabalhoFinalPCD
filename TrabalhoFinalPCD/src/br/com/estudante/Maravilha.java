package br.com.estudante;

import java.util.ArrayList;

import br.com.estudante.tela.Principal;

public class Maravilha extends Thread {
	private ArrayList<Aldeao> construtores = new ArrayList<Aldeao>();
	private Prefeitura prefeitura;
	private Principal principal;
	private int qtdTijolos = 0;

	Maravilha(Prefeitura prefeitura, Principal principal) {
		setPrefeitura(prefeitura);
		setPrincipal(principal);
	}

	public void run() {
		while (true) {
			if (this.construtores.size() > 0) {
				this.principal.mostrarMaravilha(this.qtdTijolos);
				System.out.println(this.getQtdConstrutores());
			}
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

	public Principal getPrincipal() {
		return principal;
	}

	public void setPrincipal(Principal principal) {
		this.principal = principal;
	}

	public synchronized int getQtdTijolos() {
		return qtdTijolos;
	}

	public synchronized void setQtdTijolos() {
		this.qtdTijolos++;
	}

}
