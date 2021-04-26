package br.com.estudante;

import br.com.estudante.tela.Principal;

public class Prefeitura {
	private static Integer numAldeosCriados = 5;
	private Integer unidadesComida;
	private Integer unidadesOuro;
	private Integer oferendasFe;
	private Principal principal;

	/*
	 * Constructor
	 */
	public Prefeitura(Principal principal) {
		this.unidadesComida = Integer.valueOf(150);
		this.unidadesOuro = Integer.valueOf(500);
		this.oferendasFe = Integer.valueOf(0);
		this.principal = principal;
		principal.mostrarComida(this.unidadesComida);
		principal.mostrarOuro(this.unidadesOuro);
		principal.mostrarOferendaFe(this.oferendasFe);
	}

	/*
	 * Getters and Setters
	 */
	public Integer getUnidadesComida() {
		return unidadesComida;
	}

	public synchronized void addUnidadesComida(Integer unidadesComida) {
		this.unidadesComida += unidadesComida;
		this.principal.mostrarComida(this.unidadesComida);
	}

	public Integer getUnidadesOuro() {
		return unidadesOuro;
	}

	public void addUnidadesOuro(Integer unidadesOuro) {
		this.unidadesOuro += unidadesOuro;
	}

	public Integer getOferendasFe() {
		return oferendasFe;
	}

	public Principal getPrincipal() {
		return principal;
	}

	public void addOferendasFe(Integer oferendasFe) {
		this.oferendasFe += oferendasFe;
	}

	/*
	 * Criar aldeao
	 */

	public Aldeao criarAldeao() {
		if (this.unidadesComida >= 100) {
			Aldeao novo = new Aldeao(String.valueOf(numAldeosCriados), this);
			numAldeosCriados++;
			this.addUnidadesComida(-100);
			return novo;
		}
		this.principal.mostrarMensagemErro("Recursos insuficientes",
				"Você precisa de mais " + (100 - this.unidadesComida) + " de comida.");
		return null;

	}
}
