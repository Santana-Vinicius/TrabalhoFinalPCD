package br.com.estudante;

import java.awt.Color;

import br.com.estudante.Utils.Utils;
import br.com.estudante.tela.Tela;

public class Aldeao extends Thread {
	private String nome, tipoConstrucao; // tipoConstrução -> 0 = nada | 1 = Fazenda | 2 = Mina de Ouro | 3 = Templo | 4
	// = Maravilha
	private Enum<Status> status;
	private Fazenda fazenda;
	private MinaOuro minaOuro;
	private Prefeitura prefeitura;
	private int nivel;

	/*
	 * Constructor
	 */
	public Aldeao(String name, Prefeitura prefeitura) {
		setNome(name);
		setName("Aldeao " + getNome());
		setStatus(Status.PARADO);
		setPrefeitura(prefeitura);
		this.nivel = this.getPrefeitura().getNivelAldeoes();
		this.tipoConstrucao = ""; // Não está construindo nada
	}

	/*
	 * Run
	 */
	@Override
	public void run() {
		while (this.status != Status.SACRIFICADO && this.status != Status.MORTO) {
			this.getPrefeitura().getPrincipal().mostrarAldeao(Integer.valueOf(this.getNome()), this.getStatus());
			switch (this.status.toString()) {
			case "Cultivando":
				this.cultivar();
				break;
			case "Minerando":
				this.minerar();
				break;
			case "Construindo":
				this.construir();
				break;
			case "Orando":
				this.orar();
				break;
			case "Parado":
				this.parar();
				break;
			}
		}
		this.prefeitura.getPrincipal().mostrarAldeao(Integer.valueOf(this.nome), this.status.toString());
	}

	/*
	 * Getters and Setters
	 */
	public String getNome() {
		return nome;
	}

	public void setNome(String name) {
		this.nome = name;
	}

	public String getStatus() {
		return status.toString();
	}

	public Enum<Status> getStatusEnum() {
		return status;
	}

	public synchronized void setStatus(Enum<Status> status) {
		if (this.status == null)
			this.status = status;

		else if (!this.status.equals(Status.MORTO) && !this.status.equals(Status.SACRIFICADO))
			this.status = status;
		else
			System.out.println("O aldeão " + nome + " não pode ser ressucitado");
	}

	public synchronized Fazenda getFazenda() {
		return fazenda;
	}

	public synchronized void setFazenda(Fazenda fazenda) {
		if (this.fazenda != fazenda)
			this.fazenda = fazenda;
	}

	public synchronized MinaOuro getMinaOuro() {
		return minaOuro;
	}

	public synchronized void setMinaOuro(MinaOuro minaOuro) {
		if (this.minaOuro != minaOuro)
			this.minaOuro = minaOuro;
	}

	public Prefeitura getPrefeitura() {
		return prefeitura;
	}

	public void setPrefeitura(Prefeitura prefeitura) {
		this.prefeitura = prefeitura;
	}

	public int getNivel() {
		return nivel;
	}

	public void setNivel(int nivel) {
		this.nivel = nivel;
	}

	public synchronized String getTipoConstrucao() {
		return tipoConstrucao;
	}

	public synchronized void setTipoConstrucao(String tipoConstrucao) {
		this.tipoConstrucao = tipoConstrucao;
	}

	/*
	 * toString
	 */
	public String toString() {
		return "STATUS: " + getStatus();
	}

	/*
	 * Funcoes do aldeão
	 */

	public void cultivar() {
		try {
			// Sleep de uma hora para Produzir
			Thread.sleep(500);
			Integer comidaProduzida = fazenda.cultivar(this.nivel);
			// Sleep de duas horas para Transportar
			Fazenda fazendaTemp = this.fazenda;
			fazendaTemp.removeFazendeiro(this);
			this.getPrefeitura().getPrincipal().mostrarFazenda(Integer.valueOf(fazendaTemp.getNome()),
					fazendaTemp.getNomeAldeoes());
			Thread.sleep(Utils.calculaTempoTransporte(this.nivel, 1000));
			fazendaTemp.addFazendeiro(this);
			this.getPrefeitura().getPrincipal().mostrarFazenda(Integer.valueOf(fazendaTemp.getNome()),
					fazendaTemp.getNomeAldeoes());
			prefeitura.addUnidadesComida(comidaProduzida);
			this.prefeitura.getPrincipal().mostrarComida(this.prefeitura.getUnidadesComida());
			this.prefeitura.getPrincipal().mostrarFazenda(Integer.valueOf(this.fazenda.getNome()),
					this.fazenda.getNomeAldeoes());
		} catch (InterruptedException e) {
			this.setStatus(Status.PARADO);
			this.run();
		}

	}

	public void minerar() {
		this.getPrefeitura().getPrincipal().mostrarAldeao(Integer.valueOf(this.getNome()), this.getStatus());
		try {
			// Sleep de duas horas para Produzir
			Thread.sleep(1000);
			Integer ouroProduzido = minaOuro.minerar(this.nivel);
			// Sleep de três horas para Transportar
			MinaOuro minaTemp = this.minaOuro;
			minaTemp.removeMinerador(this);
			this.getPrefeitura().getPrincipal().mostrarMinaOuro(Integer.valueOf(minaTemp.getNome()),
					minaTemp.getNomeAldeoes());
			Thread.sleep(Utils.calculaTempoTransporte(this.nivel, 1500));
			minaTemp.addMinerador(this);
			this.prefeitura.getPrincipal().mostrarMinaOuro(Integer.valueOf(this.minaOuro.getNome()),
					this.minaOuro.getNomeAldeoes());
			prefeitura.addUnidadesOuro(ouroProduzido);
			this.prefeitura.getPrincipal().mostrarOuro(this.prefeitura.getUnidadesOuro());
			this.prefeitura.getPrincipal().mostrarMinaOuro(Integer.valueOf(this.minaOuro.getNome()),
					this.minaOuro.getNomeAldeoes());
		} catch (InterruptedException e) {
			this.setStatus(Status.PARADO);
			this.run();
		}

	}

	public void construir() {
		this.getPrefeitura().getPrincipal().mostrarAldeao(Integer.valueOf(this.getNome()), this.getStatus());
		Vila vila = this.prefeitura.getPrincipal().getVila();
		switch (this.tipoConstrucao) {
		case "Fazenda":
			Fazenda fazenda = construirFazenda();
			if (fazenda != null) {
				vila.addFazenda(fazenda);
			}
			this.status = Status.PARADO;
			this.tipoConstrucao = "";
			break;
		case "Mina de ouro":
			MinaOuro minaOuro = construirMina();
			if (minaOuro != null) {
				vila.addMinaOuro(minaOuro);
			}
			this.status = Status.PARADO;
			this.tipoConstrucao = "";
			break;
		case "Templo":
			Templo templo = construirTemplo();
			if (templo != null) {
				this.getPrefeitura().getPrincipal().mostrarTemplo(templo.getNome(), Color.GRAY);
				this.getPrefeitura().getPrincipal().habilitarTemplo();
				vila.setTemplo(templo);
			}
			this.status = Status.PARADO;
			this.tipoConstrucao = "";
			break;
		case "Maravilha":
			Maravilha maravilha = this.getPrefeitura().getPrincipal().getVila().getMaravilha();
			if (maravilha == null) {
				Maravilha maravilhaNova = new Maravilha(this.getPrefeitura(), this.getPrefeitura().getPrincipal());
				this.getPrefeitura().getPrincipal().getVila().setMaravilha(maravilhaNova);
				maravilha = maravilhaNova;
			}
			Boolean construiu = false;
			maravilha.addConstrutor(this);
			while (this.getTipoConstrucao().equals("Maravilha")
					&& this.getPrefeitura().getPrincipal().getBarraMaravilha().getValue() < 50) {
				if (this.getPrefeitura().getUnidadesComida() >= 1 && this.getPrefeitura().getUnidadesOuro() >= 1) {
					try {
						this.getPrefeitura().addUnidadesComida(-1);
						this.getPrefeitura().addUnidadesOuro(-1);
						this.prefeitura.getPrincipal().mostrarOuro(this.prefeitura.getUnidadesOuro());
						this.prefeitura.getPrincipal().mostrarComida(this.prefeitura.getUnidadesComida());
						Thread.sleep(500);
						construiu = true;
						maravilha.setQtdTijolos();
						this.getPrefeitura().getPrincipal().mostrarMaravilha(maravilha.getQtdTijolos());
						synchronized (maravilha) {
							maravilha.notify();
						}
					} catch (InterruptedException e) {
						maravilha.removeConstrutor(this);
						this.setStatus(Status.PARADO);
						this.tipoConstrucao = "";
						this.run();
					}
				} else if (!construiu) {
					String msg = "";

					if (this.prefeitura.getUnidadesComida() < 1 && this.prefeitura.getUnidadesOuro() >= 1)
						msg += "1 de comida";

					else if (this.prefeitura.getUnidadesComida() >= 1 && this.prefeitura.getUnidadesOuro() < 1)
						msg += "1 de ouro";

					else
						msg += "1 de comida e 1 de ouro";

					this.status = Status.PARADO;
					this.tipoConstrucao = "";

					this.prefeitura.getPrincipal().mostrarMensagemErro("Recursos insuficientes",
							"Você precisa de mais " + msg);
				} else {
					this.status = Status.PARADO;
					this.tipoConstrucao = "";
				}
			}

			this.getPrefeitura().getPrincipal().getVila().getMaravilha().removeConstrutor(this);

			break;
		}
		if (this.getPrefeitura().getPrincipal().getVila().getMaravilha() != null)
			this.getPrefeitura().getPrincipal()
					.mostrarMaravilha(this.getPrefeitura().getPrincipal().getVila().getMaravilha().getQtdTijolos());
		this.getPrefeitura().getPrincipal().mostrarAldeao(Integer.valueOf(this.getNome()), this.getStatus());
		this.prefeitura.getPrincipal().mostrarComida(this.prefeitura.getUnidadesComida());
		this.prefeitura.getPrincipal().mostrarOuro(this.prefeitura.getUnidadesOuro());
	}

	private Fazenda construirFazenda() {
		Tela tela = this.prefeitura.getPrincipal();
		if (this.prefeitura.getUnidadesComida() >= 100 && this.prefeitura.getUnidadesOuro() >= 500) {
			System.out.println("Vai construir uma fazenda");
			try {
				Thread.sleep(3000);
				this.prefeitura.addUnidadesComida(-100);
				this.prefeitura.addUnidadesOuro(-500);
				return new Fazenda(String.valueOf(tela.getVila().getQtdFazendas()), tela);
			} catch (InterruptedException e) {
				this.setStatus(Status.PARADO);
				this.tipoConstrucao = "";
				this.run();
				return null;
			}
		}

		String msg = "";

		if (this.prefeitura.getUnidadesComida() < 100 && this.prefeitura.getUnidadesOuro() >= 500)
			msg += 100 - this.prefeitura.getUnidadesComida() + " de comida";

		else if (this.prefeitura.getUnidadesComida() >= 100 && this.prefeitura.getUnidadesOuro() < 500)
			msg += 500 - this.prefeitura.getUnidadesOuro() + " de ouro";

		else
			msg += (100 - this.prefeitura.getUnidadesComida()) + " de comida e "
					+ (500 - this.prefeitura.getUnidadesOuro()) + " de ouro";

		this.prefeitura.getPrincipal().mostrarMensagemErro("Recursos insuficientes", "Você precisa de mais " + msg);

		this.status = Status.PARADO;
		this.tipoConstrucao = "";
		return null;
	}

	private MinaOuro construirMina() {
		Tela tela = this.prefeitura.getPrincipal();
		if (this.prefeitura.getUnidadesComida() >= 1000) {
			try {
				Thread.sleep(3000);
				this.prefeitura.addUnidadesComida(-100);
				this.prefeitura.addUnidadesOuro(-500);
				return new MinaOuro(String.valueOf(tela.getVila().getQtdMinasOuro()), tela);
			} catch (InterruptedException e) {
				this.setStatus(Status.PARADO);
				this.tipoConstrucao = "";
				this.run();
				return null;
			}
		}
		this.prefeitura.getPrincipal().mostrarMensagemErro("Recursos insuficientes",
				"Você precisa de mais " + (1000 - this.prefeitura.getUnidadesComida()) + " de comida");
		this.status = Status.PARADO;
		this.tipoConstrucao = "";
		return null;
	}

	private Templo construirTemplo() {
		Tela tela = this.prefeitura.getPrincipal();
		if (this.prefeitura.getUnidadesComida() >= 2000 && this.prefeitura.getUnidadesOuro() >= 2000) {
			try {
				Thread.sleep(5000); // ALTERAR
				this.prefeitura.addUnidadesComida(-2000);
				this.prefeitura.addUnidadesOuro(-2000);
				return new Templo(tela);
			} catch (InterruptedException e) {
				this.setStatus(Status.PARADO);
				this.tipoConstrucao = "";
				this.run();
				return null;
			}
		}
		String msg = "";

		if (this.prefeitura.getUnidadesComida() < 2000 && this.prefeitura.getUnidadesOuro() >= 2000)
			msg += 2000 - this.prefeitura.getUnidadesComida() + " de comida";

		else if (this.prefeitura.getUnidadesComida() >= 2000 && this.prefeitura.getUnidadesOuro() < 2000)
			msg += 2000 - this.prefeitura.getUnidadesOuro() + " de ouro";

		else
			msg += (2000 - this.prefeitura.getUnidadesComida()) + " de comida e "
					+ (2000 - this.prefeitura.getUnidadesOuro()) + " de ouro";

		this.prefeitura.getPrincipal().mostrarMensagemErro("Recursos insuficientes", "Você precisa de mais " + msg);
		return null;
	}

	public void orar() {
		this.getPrefeitura().getPrincipal().mostrarAldeao(Integer.valueOf(this.getNome()), this.getStatus());
		try {
			// Sleep de cinco horas para Produzir
			Thread.sleep(2500);
			Integer oferendaProduzida = this.getPrefeitura().getPrincipal().getVila().getTemplo().orar();
			synchronized (this.getPrefeitura().getPrincipal().getVila().getTemplo()) {
				prefeitura.addOferendasFe(oferendaProduzida);
				this.prefeitura.getPrincipal().mostrarOferendaFe(this.prefeitura.getOferendasFe());
			}
		} catch (InterruptedException e) {
			this.setStatus(Status.PARADO);
			this.run();
		}
	}

	public synchronized void parar() {
		try {
			System.out.println("O aldeão " + this.getNome() + " está parado");
			this.wait();
		} catch (InterruptedException e) {
			this.setStatus(Status.PARADO);
			this.run();
		}
	}

}
