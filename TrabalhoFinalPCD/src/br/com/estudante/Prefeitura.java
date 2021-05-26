package br.com.estudante;

import java.awt.Color;

import br.com.estudante.tela.Tela;

public class Prefeitura extends Thread {
	private static Integer numAldeosCriados = 5;
	private Integer unidadesComida;
	private Integer unidadesOuro;
	private Integer oferendasFe;
	private Tela tela;
	private int nivelAldeoes;
	private String fazendo;
	private Boolean acabou = false;
	private Boolean evoluiuAldeao;
	private Boolean evoluiuFazendas;
	private Boolean evoluiuMinas;

	/*
	 * Constructor
	 */
	public Prefeitura(Tela tela) {
		this.unidadesComida = Integer.valueOf(15000);
		this.unidadesOuro = Integer.valueOf(10000);
		this.oferendasFe = Integer.valueOf(0);
		this.tela = tela;
		this.tela.mostrarComida(this.unidadesComida);
		this.tela.mostrarOuro(this.unidadesOuro);
		this.tela.mostrarOferendaFe(this.oferendasFe);
		this.setNivelAldeoes(1);
		this.setEvoluiuAldeao(false);
		this.setEvoluiuFazendas(false);
		this.setEvoluiuMinas(false);
		setFazendo("");
	}

	public void run() {
		while (!acabou) {
			try {
				synchronized (this) {
					this.wait();
				}
			} catch (InterruptedException e) {
				acabou = true;
			}
			if (!acabou) {
				this.tela.mostrarComida(this.unidadesComida);
				this.tela.mostrarOuro(this.unidadesOuro);
				this.tela.mostrarOferendaFe(this.oferendasFe);
				this.fazer();
			}
		}
		this.getPrincipal().getVila().encerrar();	
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

		this.tela.mostrarComida(this.unidadesComida);
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

	public Tela getPrincipal() {
		return tela;
	}

	public synchronized int getNivelAldeoes() {
		return nivelAldeoes;
	}

	public synchronized void setNivelAldeoes(int nivelAldeoes) {
		this.nivelAldeoes = nivelAldeoes;
	}

	public String getFazendo() {
		return fazendo;
	}

	public void setFazendo(String fazendo) {
		this.fazendo = fazendo;
	}

	public synchronized void addOferendasFe(Integer oferendasFe) {
		if (oferendasFe < 0) {
			if (Math.abs(oferendasFe) <= this.oferendasFe)
				this.oferendasFe += oferendasFe;
		} else
			this.oferendasFe += oferendasFe;
	}

	public synchronized Boolean getEvoluiuAldeao() {
		return evoluiuAldeao;
	}

	public synchronized void setEvoluiuAldeao(Boolean evoluiuAldeao) {
		this.evoluiuAldeao = evoluiuAldeao;
	}

	public synchronized Boolean getEvoluiuFazendas() {
		return evoluiuFazendas;
	}

	public synchronized void setEvoluiuFazendas(Boolean evoluiuFazendas) {
		this.evoluiuFazendas = evoluiuFazendas;
	}

	public synchronized Boolean getEvoluiuMinas() {
		return evoluiuMinas;
	}

	public synchronized void setEvoluiuMinas(Boolean evoluiuMinas) {
		this.evoluiuMinas = evoluiuMinas;
	}

	/*
	 * Criar aldeao
	 */

	public void criarAldeao() {
		if (this.unidadesComida >= 100) {
			Boolean criado = false;
			try {
				this.tela.mostrarPrefeitura("Criando aldeão...", Color.GREEN);
				this.addUnidadesComida(-100);
				this.tela.mostrarComida(this.getUnidadesComida());
				Thread.sleep(2000);
				this.getPrincipal().getVila().addAldeao(new Aldeao(String.valueOf(numAldeosCriados), this));
				numAldeosCriados++;
				criado = true;
				this.tela.mostrarPrefeitura("Parada", Color.GRAY);
			} catch (InterruptedException e) {
				if (!criado) {
					this.addUnidadesComida(100);
					this.tela.mostrarComida(this.getUnidadesComida());
				}
				this.run();
				this.tela.mostrarPrefeitura("Parada", Color.GRAY);
			}
		} else
			this.tela.mostrarMensagemErro("Recursos insuficientes",
					"Você precisa de mais " + (100 - this.getUnidadesComida()) + " de comida.");
	}

	private void fazer() {
		switch (this.fazendo) {
		case "Evolução de aldeão":
			this.evoluirAldeao();
			break;
		case "Evolução de fazenda":
			this.evoluirFazenda();
			break;
		case "Evolução de mina de ouro":
			this.evoluirMinaDeOuro();
			break;
		case "Criar aldeão":
			this.criarAldeao();
			break;
		}
	}

	public void evoluirAldeao() {
		if (!this.getEvoluiuAldeao()) {
			if (this.unidadesComida >= 5000 && this.unidadesOuro >= 5000) {
				Boolean evoluiu = false;
				try {
					this.tela.mostrarPrefeitura("Evoluindo aldeões...", Color.GREEN);
					this.addUnidadesComida(-5000);
					this.addUnidadesOuro(-5000);
					this.tela.mostrarComida(this.getUnidadesComida());
					this.tela.mostrarOuro(this.getUnidadesOuro());
					Thread.sleep(100 * 500);
					this.setNivelAldeoes(this.getNivelAldeoes() + 1);
					this.getPrincipal().getVila().subirNivelAldeoes(this.nivelAldeoes);
					evoluiu = true;
					this.setEvoluiuAldeao(true);
					this.tela.mostrarPrefeitura("Parada", Color.ORANGE);
				} catch (InterruptedException e) {
					if (!evoluiu) {
						this.addUnidadesComida(5000);
						this.addUnidadesOuro(5000);
						this.tela.mostrarComida(this.getUnidadesComida());
						this.tela.mostrarOuro(this.getUnidadesOuro());
					}
					this.setFazendo("");
					this.run();
					System.out.println("Parou evolução Aldeão");
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
		} else {
			this.getPrincipal().mostrarMensagemErro("Erro", "Os aldeões já estão no nível máximo!");
		}
		this.setFazendo("");
	}

	public void evoluirFazenda() {
		if (!this.getEvoluiuFazendas()) {
			if (this.unidadesComida >= 500 && this.unidadesOuro >= 5000) {
				if (this.getPrincipal().getVila().getFazenda(0).getCapacidade() != 10) {
					Boolean evoluiu = false;
					try {
						this.tela.mostrarPrefeitura("Evoluindo fazendas...", Color.GREEN);
						this.addUnidadesComida(-500);
						this.addUnidadesOuro(-5000);
						this.tela.mostrarComida(this.getUnidadesComida());
						this.tela.mostrarOuro(this.getUnidadesOuro());
						Thread.sleep(100 * 500);
						this.getPrincipal().getVila().subirNivelFazenda();
						evoluiu = true;
						this.tela.mostrarPrefeitura("Parada", Color.ORANGE);
						this.setEvoluiuFazendas(true);
					} catch (InterruptedException e) {
						if (!evoluiu) {
							this.addUnidadesComida(500);
							this.addUnidadesOuro(5000);
							this.tela.mostrarComida(this.getUnidadesComida());
							this.tela.mostrarOuro(this.getUnidadesOuro());
						}
						this.setFazendo("");
						this.run();
						System.out.println("Parou evolução Fazenda");
					}
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
		} else
			this.getPrincipal().mostrarMensagemErro("Erro", "As fazendas já estão no nível máximo!");
		this.setFazendo("");
	}

	public void evoluirMinaDeOuro() {
		if (!this.getEvoluiuMinas()) {
			if (this.unidadesComida >= 2000 && this.unidadesOuro >= 1000) {
				if (this.getPrincipal().getVila().getMinaOuro(0).getCapacidade() != 10) {
					Boolean evoluiu = false;
					try {
						this.tela.mostrarPrefeitura("Evoluindo minas...", Color.GREEN);
						this.addUnidadesComida(-2000);
						this.addUnidadesOuro(-1000);
						this.tela.mostrarComida(this.getUnidadesComida());
						this.tela.mostrarOuro(this.getUnidadesOuro());
						Thread.sleep(100 * 500);
						this.getPrincipal().getVila().subirNivelMinasDeOuro();
						evoluiu = true;
						this.tela.mostrarPrefeitura("Parada", Color.ORANGE);
						this.setEvoluiuMinas(true);
					} catch (InterruptedException e) {
						if (!evoluiu) {
							this.addUnidadesComida(2000);
							this.addUnidadesOuro(1000);
							this.tela.mostrarComida(this.getUnidadesComida());
							this.tela.mostrarOuro(this.getUnidadesOuro());
						}
						this.setFazendo("");
						this.run();
						System.out.println("Parou evolução Mina de ouro");
					}
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
		} else
			this.getPrincipal().mostrarMensagemErro("Erro", "As minas de ouro já estão no nível máximo!");

		this.setFazendo("");
	}

	public Boolean acabou() {
		return acabou;
	}

	public void setAcabou(Boolean acabou) {
		this.acabou = acabou;
	}
}
