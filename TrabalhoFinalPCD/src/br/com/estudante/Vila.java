package br.com.estudante;

import java.util.ArrayList;

import br.com.estudante.tela.Tela;

public class Vila {
	private Tela tela;
	private Prefeitura prefeitura;
	private Maravilha maravilha;
	private boolean protecaoGafanhotos;
	private boolean protecaoPrimogenitos;
	private boolean protecaoPedras;

	private Templo templo;
	private ArrayList<Aldeao> aldeoes = new ArrayList<Aldeao>();
	private ArrayList<Fazenda> fazendas = new ArrayList<Fazenda>();
	private ArrayList<MinaOuro> minasOuro = new ArrayList<MinaOuro>();

	/*
	 * Constructor da Vila
	 */
	public Vila(Tela tela) {
		this.tela = tela;
		this.prefeitura = new Prefeitura(tela);
		this.prefeitura.start();
		setAldeoes(geraAldeoes());
		addFazenda(new Fazenda("0", this.tela));
		addMinaOuro(new MinaOuro("0", this.tela));
		setProtecaoGafanhotos(false);
		setProtecaoPrimogenitos(false);
		setProtecaoPedras(false);
	}

	public Vila() {
	}

	public boolean isProtecaoGafanhotos() {
		return protecaoGafanhotos;
	}

	public void setProtecaoGafanhotos(boolean protecaoGafanhotos) {
		this.protecaoGafanhotos = protecaoGafanhotos;
	}

	public boolean isProtecaoPrimogenitos() {
		return protecaoPrimogenitos;
	}

	public void setProtecaoPrimogenitos(boolean protecaoPrimogenitos) {
		this.protecaoPrimogenitos = protecaoPrimogenitos;
	}

	public boolean isProtecaoPedras() {
		return protecaoPedras;
	}

	public void setProtecaoPedras(boolean protecaoPedras) {
		this.protecaoPedras = protecaoPedras;
	}

	public String toString() {
		return "UNIDADES DE COMIDA: " + prefeitura.getUnidadesComida() + "\n" + "UNIDADES DE OURO: "
				+ prefeitura.getUnidadesOuro() + "\n";
	}

	public void encerrar() {
		for (Aldeao aldeao : aldeoes) {
			aldeao.setStatus(Status.ENCERRADO);
		}
		Templo templo = this.getTemplo();
		if (templo != null) {
			templo.setAcabou(true);
			templo.interrupt();
		}
	}

	/*
	 * Getters and Setters
	 */
	public Prefeitura getPrefeitura() {
		return prefeitura;
	}

	public ArrayList<Aldeao> getAldeoes() {
		return this.aldeoes;
	}

	public void setPrefeitura(Prefeitura prefeitura) {
		this.prefeitura = prefeitura;
	}

	public Aldeao getAldeao(Integer index) {
		return aldeoes.get(index);
	}

	public void setAldeoes(ArrayList<Aldeao> aldeoes) {
		this.aldeoes = aldeoes;
		for (Aldeao aldeao : aldeoes) {
			tela.adicionarAldeao(aldeao.getNome(), aldeao.getStatus());
			tela.mostrarAldeao(Integer.valueOf(aldeao.getNome()), aldeao.getStatus());
			aldeao.start();
		}
	}

	public void addAldeao(Aldeao novo) {
		this.aldeoes.add(novo);
		tela.adicionarAldeao(novo.getNome(), novo.getStatus());
		novo.start();
		tela.mostrarAldeao(Integer.valueOf(novo.getNome()), novo.getStatus());
	}

	public Fazenda getFazenda(Integer index) {
		return this.fazendas.get(index);
	}

	public void addFazenda(Fazenda novo) {
		this.fazendas.add(novo);
		tela.adicionarFazenda(novo.getNome(), "");
	}

	public void removeFazenda(int index) {
		this.fazendas.remove(index);
	}

	public ArrayList<Fazenda> getFazendas() {
		return this.fazendas;
	}

	public Templo getTemplo() {
		return templo;
	}

	public void setTemplo(Templo templo) {
		this.templo = templo;
		this.getTemplo().start();
	}

	public Maravilha getMaravilha() {
		return maravilha;
	}

	public void setMaravilha(Maravilha maravilha) {
		this.maravilha = maravilha;
		this.tela.habilitarMaravilha();
		maravilha.start();
	}

	public MinaOuro getMinaOuro(Integer index) {
		return this.minasOuro.get(index);
	}

	public void addMinaOuro(MinaOuro novo) {
		this.minasOuro.add(novo);
		tela.adicionarMinaOuro(novo.getNome(), "");
	}

	public void removeMinaOuro(int index) {
		this.minasOuro.remove(index);
	}

	public ArrayList<MinaOuro> getMinas() {
		return this.minasOuro;
	}

	public int getQtdFazendas() {
		return this.fazendas.size();
	}

	public int getQtdMinasOuro() {
		return this.minasOuro.size();
	}

	public int getQtdAldeoes() {
		return this.aldeoes.size();
	}

	public int getQtdAldeoesVivos() {
		int qtd = 0;
		for (Aldeao aldeao : aldeoes) {
			if (!aldeao.getStatus().equals("Sacrificado") && !aldeao.getStatus().equals("Morto"))
				qtd++;
		}
		return qtd;
	}
	/*
	 * Massa de dados para teste
	 */

	private ArrayList<Aldeao> geraAldeoes() {
		ArrayList<Aldeao> aldeoes = new ArrayList<Aldeao>();

		for (Integer i = 0; i < 5; i++)
			aldeoes.add(new Aldeao(String.valueOf(i), this.prefeitura));

		return aldeoes;
	}

	public void subirNivelAldeoes(int nivel) {
		for (Aldeao aldeao : this.aldeoes) {
			aldeao.setNivel(nivel);
		}
	}

	public void subirNivelFazenda() {
		for (Fazenda fazenda : this.fazendas) {
			fazenda.setCapacidade(10);
		}
	}

	public void subirNivelMinasDeOuro() {
		for (MinaOuro minaOuro : this.minasOuro) {
			minaOuro.setCapacidade(10);
		}
	}

}
