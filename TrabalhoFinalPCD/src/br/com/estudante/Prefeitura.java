package br.com.estudante;

import br.com.estudante.tela.Principal;

public class Prefeitura extends Thread {
	private static Integer numAldeosCriados = 5;
	private Integer unidadesComida;
	private Integer unidadesOuro;
	private Integer oferendasFe;
	private Principal principal;
	private int nivelAldeoes;
	private String tipoEvolucao;

	/*
	 * Constructor
	 */
	public Prefeitura(Principal principal) {
		this.unidadesComida = Integer.valueOf(150);
		this.unidadesOuro = Integer.valueOf(100);
		this.oferendasFe = Integer.valueOf(0);
		this.principal = principal;
		this.principal.mostrarComida(this.unidadesComida);
		this.principal.mostrarOuro(this.unidadesOuro);
		this.principal.mostrarOferendaFe(this.oferendasFe);
		this.setNivelAldeoes(1);
		setTipoEvolucao("");
	}

	public void run() {
		while (true) {
			this.principal.mostrarComida(this.unidadesComida);
			this.principal.mostrarOuro(this.unidadesOuro);
			this.principal.mostrarOferendaFe(this.oferendasFe);
			if (this.getTipoEvolucao() != "") {
				this.evoluir();
			}

		}
	}

	/*
	 * Getters and Setters
	 */
	public synchronized Integer getUnidadesComida() {
		return unidadesComida;
	}

	public synchronized void addUnidadesComida(Integer unidadesComida) {
		if (unidadesComida < 0) {
			if (Math.abs(unidadesComida) <= this.unidadesComida)
				this.unidadesComida += unidadesComida;
		} else
			this.unidadesComida += unidadesComida;

		this.principal.mostrarComida(this.unidadesComida);
	}

	public synchronized Integer getUnidadesOuro() {
		return unidadesOuro;
	}

	public synchronized void addUnidadesOuro(Integer unidadesOuro) {
		if (unidadesOuro < 0) {
			if (Math.abs(unidadesOuro) <= this.unidadesOuro)
				this.unidadesOuro += unidadesOuro;
		} else
			this.unidadesOuro += unidadesOuro;
	}

	public synchronized Integer getOferendasFe() {
		return oferendasFe;
	}

	public Principal getPrincipal() {
		return principal;
	}

	public synchronized int getNivelAldeoes() {
		return nivelAldeoes;
	}

	public synchronized void setNivelAldeoes(int nivelAldeoes) {
		this.nivelAldeoes = nivelAldeoes;
	}

	public String getTipoEvolucao() {
		return tipoEvolucao;
	}

	public void setTipoEvolucao(String tipoEvolucao) {
		this.tipoEvolucao = tipoEvolucao;
	}

	public synchronized void addOferendasFe(Integer oferendasFe) {
		if (oferendasFe < 0) {
			if (Math.abs(oferendasFe) <= this.oferendasFe)
				this.oferendasFe += oferendasFe;
		} else
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

	private void evoluir() {
		switch (this.tipoEvolucao) {
		case "Evolução de aldeão":
			this.evoluirAldeao();
			break;
		case "Evolução de fazenda":
			this.evoluirFazenda();
			break;
		case "Evolução de mina de ouro":
			this.evoluirMinaDeOuro();
			break;

		}
	}

	public void evoluirAldeao() {
		if (this.unidadesComida >= 5000 && this.unidadesOuro >= 5000) {
			try {
				Thread.sleep(5000);
				this.setNivelAldeoes(this.getNivelAldeoes() + 1);
				this.getPrincipal().getVila().subirNivelAldeoes(this.nivelAldeoes);
				this.addUnidadesComida(-5000);
				this.addUnidadesOuro(-5000);
			} catch (InterruptedException e) {
				this.setTipoEvolucao("");
				this.run();
				System.out.println("Erro evolução Aldeão");
			}
		} else {

			String msg = "";

			if (this.getUnidadesComida() < 5000 && this.getUnidadesOuro() >= 5000)
				msg += 5000 - this.getUnidadesComida() + " de comida";

			else if (this.getUnidadesComida() >= 5000 && this.getUnidadesOuro() < 5000)
				msg += 5000 - this.getUnidadesOuro() + " de ouro";

			else
				msg += (5000 - this.getUnidadesComida()) + " de comida e " + (5000 - this.getUnidadesOuro())
						+ " de ouro";

			this.getPrincipal().mostrarMensagemErro("Recursos insuficientes", "Você precisa de mais " + msg);
		}
		this.setTipoEvolucao("");
	}

	public void evoluirFazenda() {
		if (this.unidadesComida >= 500 && this.unidadesOuro >= 5000) {
			if (this.getPrincipal().getVila().getFazenda(0).getCapacidade() != 10) {
				try {
					Thread.sleep(5000);
					this.addUnidadesComida(-500);
					this.addUnidadesOuro(-5000);
					this.getPrincipal().getVila().subirNivelFazenda();
				} catch (InterruptedException e) {
					this.setTipoEvolucao("");
					this.run();
					System.out.println("Erro evolução Fazenda");
				}
			} else {
				this.getPrincipal().mostrarMensagemErro("Erro", "As fazendas já estão no nível máximo!");
			}

		} else {

			String msg = "";

			if (this.getUnidadesComida() < 500 && this.getUnidadesOuro() >= 5000)
				msg += 500 - this.getUnidadesComida() + " de comida";

			else if (this.getUnidadesComida() >= 500 && this.getUnidadesOuro() < 5000)
				msg += 5000 - this.getUnidadesOuro() + " de ouro";

			else
				msg += (500 - this.getUnidadesComida()) + " de comida e " + (5000 - this.getUnidadesOuro())
						+ " de ouro";

			this.getPrincipal().mostrarMensagemErro("Recursos insuficientes", "Você precisa de mais " + msg);
		}
		this.setTipoEvolucao("");
	}

	public void evoluirMinaDeOuro() {
		if (this.unidadesComida >= 2000 && this.unidadesOuro >= 1000) {
			if (this.getPrincipal().getVila().getMinaOuro(0).getCapacidade() != 10) {
				try {
					Thread.sleep(5000);
					this.addUnidadesComida(-2000);
					this.addUnidadesOuro(-1000);
					this.getPrincipal().getVila().subirNivelMinasDeOuro();
				} catch (InterruptedException e) {
					this.setTipoEvolucao("");
					this.run();
					System.out.println("Erro evolução Mina de ouro");
				}
			} else {
				this.getPrincipal().mostrarMensagemErro("Erro", "As minas de ouro já estão no nível máximo!");
			}

		} else {

			String msg = "";

			if (this.getUnidadesComida() < 2000 && this.getUnidadesOuro() >= 1000)
				msg += 2000 - this.getUnidadesComida() + " de comida";

			else if (this.getUnidadesComida() >= 2000 && this.getUnidadesOuro() < 1000)
				msg += 1000 - this.getUnidadesOuro() + " de ouro";

			else
				msg += (2000 - this.getUnidadesComida()) + " de comida e " + (1000 - this.getUnidadesOuro())
						+ " de ouro";

			this.getPrincipal().mostrarMensagemErro("Recursos insuficientes", "Você precisa de mais " + msg);
		}
		this.setTipoEvolucao("");
	}
}
