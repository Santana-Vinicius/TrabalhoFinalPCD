package br.com.estudante;

public class Fazenda {
	private String name;

	/*
	 * Getters and Setters
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/*
	 * Funcoes da Fazenda
	 */

	/*
	 * Dentro de uma fazenda, leva-se 1 hora para produzir 10 unidades Produzir
	 * comida
	 */

	public Integer cultivar() {

		return Integer.valueOf(10);
	}

}
