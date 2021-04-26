package br.com.estudante;

import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;

import br.com.estudante.tela.Principal;

public class Templo {
	private String nome;
	private Principal principal;
	private String nomeAldeoes;
	private ArrayList<Aldeao> religiosos = new ArrayList<Aldeao>();

	Templo(Principal principal) {
		this.setPrincipal(principal);
		this.nomeAldeoes = "";
		setNome("Templo " + this.principal.getCivilizacao());
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

	public void setNomeAldeoes() {
		synchronized (this) {
			this.nomeAldeoes = "";
			SortedSet<String> nomes = new TreeSet<String>();

			for (Aldeao religioso : religiosos)
				nomes.add(String.valueOf(Integer.valueOf(religioso.getNome()) + 1));

			for (String nomeAldeao : nomes) {
				this.nomeAldeoes += nomeAldeao + " ";
			}
		}
	}

	public synchronized void addReligioso(Aldeao aldeao) {
		boolean tem = false;
		for (Aldeao religioso : religiosos) {
			if (religioso.equals(aldeao))
				tem = true;
		}
		if (!tem) {
			this.religiosos.add(aldeao);
			this.setNomeAldeoes();
		}
	}

	public synchronized void removeReligioso(Aldeao aldeao) {
		this.religiosos.remove(aldeao);
		this.setNomeAldeoes();
	}

	public synchronized String getNomeAldeoes() {
		return nomeAldeoes;
	}

	public synchronized int getQtdReligiosos() {
		return this.religiosos.size();
	}

	/*
	 * Funcoes da Fazenda
	 */
	public Integer orar() {

		return 2;
	}

	public Integer Sacrificar(Aldeao aldeao) {
		this.removeReligioso(aldeao);
		return 100;
	}

}
