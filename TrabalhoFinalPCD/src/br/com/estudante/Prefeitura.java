package br.com.estudante;

public class Prefeitura {
	private static Integer numAldeosCriados = 5;
	private Integer unidadesComida;
	private Integer unidadesOuro;
	private Integer oferendasFe;

	/*
	 * Constructor
	 */
	public Prefeitura() {
		this.unidadesComida = Integer.valueOf(150);
		this.unidadesOuro = Integer.valueOf(100);
		this.oferendasFe = Integer.valueOf(0);
	}

	/*
	 * Getters and Setters
	 */
	public Integer getUnidadesComida() {
		return unidadesComida;
	}

	public synchronized void addUnidadesComida(Integer unidadesComida) {
		this.unidadesComida += unidadesComida;
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

	public void addOferendasFe(Integer oferendasFe) {
		this.oferendasFe += oferendasFe;
	}

	/*
	 * Criar aldeao
	 */
	
	public Aldeao criarAldeao() {
		Aldeao novo = new Aldeao(String.valueOf(numAldeosCriados), this);
		System.out.println(novo.getNome());
		numAldeosCriados++;
		return novo;
	}
}
