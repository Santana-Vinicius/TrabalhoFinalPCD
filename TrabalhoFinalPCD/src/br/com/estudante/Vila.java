package br.com.estudante;

import java.util.ArrayList;

import br.com.estudante.tela.Principal;

public class Vila {
	private Principal principal;
	private Prefeitura prefeitura;
	private ArrayList<Aldeao> aldeoes = new ArrayList<Aldeao>();
	private ArrayList<Fazenda> fazendas = new ArrayList<Fazenda>();

	/*
	 * Constructor da Vila
	 */
	public Vila(Principal principal) {
		this.principal = principal;
		this.prefeitura = new Prefeitura(principal);
		setAldeoes(geraAldeoes());
		addFazenda(new Fazenda("0", this.principal));
	}

	public Vila() {
	}

	/*
	 * To string
	 */

	public String toString() {
		// TODO Auto-generated method stub
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

	public ArrayList<Aldeao> getAldeoes() {
		return aldeoes;
	}

	public Aldeao getAldeao(Integer index) {
		return aldeoes.get(index);
	}

	public void setAldeoes(ArrayList<Aldeao> aldeoes) {
		this.aldeoes = aldeoes;
		for (Aldeao aldeao : aldeoes) {
			principal.adicionarAldeao(aldeao.getNome(), aldeao.getStatus());
			principal.mostrarAldeao(Integer.valueOf(aldeao.getNome()), aldeao.getStatus());
			aldeao.start();
		}
	}

	public void addAldeao(Aldeao novo) {
		this.aldeoes.add(novo);
		principal.adicionarAldeao(novo.getNome(), novo.getStatus());
		novo.start();
		principal.mostrarAldeao(Integer.valueOf(novo.getNome()), novo.getStatus());
	}

	public ArrayList<Fazenda> getFazendas() {
		return fazendas;
	}

	public Fazenda getFazenda(Integer index) {
		return this.fazendas.get(index);
	}

	public void setFazendas(ArrayList<Fazenda> fazendas) {
		this.fazendas = fazendas;
	}

	public void addFazenda(Fazenda novo) {
		this.fazendas.add(novo);
		principal.adicionarFazenda(novo.getNome(), "");
	}

	/*
	 * Coisas que acontecem na vila
	 */

//	public void cultivo(Integer idxAldeao, Integer idxFazenda) {
//		
//		Integer comidaProduzida = Integer.valueOf(0);	
//		Aldeao fazendeiro = this.getAldeao(idxAldeao);
//		Fazenda fazenda = this.getFazenda(idxFazenda);
//		
//		fazendeiro.cultivar();
//		this.prefeitura.addUnidadesComida(comidaProduzida);
//	}
//	

	/*
	 * Massa de dados para teste
	 */

	private ArrayList<Aldeao> geraAldeoes() {
		ArrayList<Aldeao> aldeoes = new ArrayList<Aldeao>();

		for (Integer i = 0; i < 5; i++)
			aldeoes.add(new Aldeao(String.valueOf(i), this.prefeitura));

		return aldeoes;
	}


}
