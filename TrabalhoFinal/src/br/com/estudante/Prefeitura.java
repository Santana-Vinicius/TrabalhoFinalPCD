package br.com.estudante;

import java.util.ArrayList;

public class Prefeitura {
	private ArrayList<Aldeao> aldeoes = new ArrayList<Aldeao>();
	private ArrayList<Fazenda> fazendas = new ArrayList<Fazenda>();
	private ArrayList<MinaDeOuro> minasDeOuro = new ArrayList<MinaDeOuro>();
	private Templo templo = null;
	private int ouro = 10000;
	private int comida = 15000;

	public void adicionarAldeao(Aldeao aldeao) {
		if (aldeao != null)
			this.aldeoes.add(aldeao);
		else
			System.out.println("Aldeão inválido");
	}

	public Aldeao getAldeao(int aldeao) {

		return this.aldeoes.get(aldeao);
	}

	public void alterarValorComidaOuro(int comida, int ouro) {
		this.comida += comida;
		this.ouro += ouro;
	}

	public void adicionarFazenda(Fazenda fazenda) {
		if (fazenda != null)
			this.fazendas.add(fazenda);
		else
			System.out.println("Aldeão inválido");
	}

	public Fazenda getFazenda(int fazenda) {

		return this.fazendas.get(fazenda);
	}

	public void adicionarMinaDeOuro(MinaDeOuro minaDeOuro) {
		if (minaDeOuro != null)
			this.minasDeOuro.add(minaDeOuro);
		else
			System.out.println("Mina de Ouro inválida");
	}

	public MinaDeOuro getMinaDeOuro(int minaDeOuro) {

		return this.minasDeOuro.get(minaDeOuro);
	}

	public int getOuro() {
		return ouro;
	}

	public void setOuro(int ouro) {
		this.ouro = ouro;
	}

	public int getComida() {
		return comida;
	}

	public void setComida(int comida) {
		this.comida = comida;
	}

	public Templo getTemplo() {
		return templo;
	}

	public void setTemplo(Templo templo) {
		this.templo = templo;
	}

}
