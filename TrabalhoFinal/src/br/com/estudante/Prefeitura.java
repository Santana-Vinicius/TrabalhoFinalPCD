package br.com.estudante;

import java.util.ArrayList;

public class Prefeitura {
private ArrayList<Aldeao> aldeoes = new ArrayList<Aldeao>();
private ArrayList<Fazenda> fazendas = new ArrayList<Fazenda>();
private int ouro = 600;
private int comida = 900;
	
	
	public void adicionarAldeao (Aldeao aldeao) {
		if (aldeao != null)
			this.aldeoes.add(aldeao);
		else
			System.out.println("Aldeão inválido");
	}
	
	public Aldeao getAldeao (int aldeao) {
		
		return this.aldeoes.get(aldeao);
	}
	
	public void adicionarFazenda (Fazenda fazenda) {
		if (fazenda != null)
			this.fazendas.add(fazenda);
		else
			System.out.println("Aldeão inválido");
	}
	
	public Fazenda getFazenda (int fazenda) {
		
		return this.fazendas.get(fazenda);
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

}
