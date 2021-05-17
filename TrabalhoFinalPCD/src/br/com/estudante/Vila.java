package br.com.estudante;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import br.com.estudante.tela.Tela;

public class Vila {
	private Tela tela;
	private Prefeitura prefeitura;
	private Maravilha maravilha;
	private boolean protecaoGafanhotos;
	private boolean protecaoPrimogenitos;
	private boolean protecaoPedras;

	private Socket socket;
	


	private Templo templo;
	private ArrayList<Aldeao> aldeoes = new ArrayList<Aldeao>();
	private ArrayList<Fazenda> fazendas = new ArrayList<Fazenda>();
	private ArrayList<MinaOuro> minasOuro = new ArrayList<MinaOuro>();

	/*
	 * Constructor da Vila
	 */
	public Vila(Tela tela) {
		System.out.println("Criando a vila!");
		this.tela = tela;
		this.prefeitura = new Prefeitura(tela);
		setAldeoes(geraAldeoes());
		addFazenda(new Fazenda("0", this.tela));
		addMinaOuro(new MinaOuro("0", this.tela));
		setProtecaoGafanhotos(false);
		setProtecaoPrimogenitos(false);
		setProtecaoPedras(false);
	}

	public Vila() {
	}

	/*
	 * To string
	 */

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

	/*
	 * Getters and Setters
	 */
	public Prefeitura getPrefeitura() {
		return prefeitura;
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

	public int getQtdFazendas() {
		return this.fazendas.size();
	}

	public int getQtdMinasOuro() {
		return this.minasOuro.size();
	}
	
	
	public Socket getSocket() {
		return socket;
	}

	public void setSocket(int port) {
		try {
			this.socket = new Socket("localhost", port);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
