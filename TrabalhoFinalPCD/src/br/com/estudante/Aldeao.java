package br.com.estudante;

import br.com.estudante.Utils.Utils;
import br.com.estudante.tela.Principal;

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
		this.nivel = 1;
		this.tipoConstrucao = ""; // Não está construindo nada
	}

	/*
	 * Run
	 */
	@Override
	public void run() {
		while (this.status != Status.SACRIFICADO) {
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
			}
		}

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

	public void setStatus(Enum<Status> status) {
		this.status = status;
	}

	public Fazenda getFazenda() {
		return fazenda;
	}

	public void setFazenda(Fazenda fazenda) {
		if (this.fazenda != fazenda)
			this.fazenda = fazenda;
	}

	public MinaOuro getMinaOuro() {
		return minaOuro;
	}

	public void setMinaOuro(MinaOuro minaOuro) {
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
			Thread.sleep(Utils.calculaTempoTransporte(this.nivel, 1000));
			synchronized (fazenda) {
				prefeitura.addUnidadesComida(comidaProduzida);
				this.prefeitura.getPrincipal().mostrarComida(this.prefeitura.getUnidadesComida());
			}

		} catch (InterruptedException e) {
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
			Thread.sleep(Utils.calculaTempoTransporte(this.nivel, 1500));
			synchronized (minaOuro) {
				prefeitura.addUnidadesOuro(ouroProduzido);
				this.prefeitura.getPrincipal().mostrarOuro(this.prefeitura.getUnidadesOuro());
			}

		} catch (InterruptedException e) {
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
				this.status = Status.PARADO;
				this.tipoConstrucao = "";
			}

			break;
		case "Mina de ouro":
			MinaOuro minaOuro = construirMina();
			if (minaOuro != null) {
				vila.addMinaOuro(minaOuro);
				this.status = Status.PARADO;
				this.tipoConstrucao = "";
			}
			break;
		}
		this.getPrefeitura().getPrincipal().mostrarAldeao(Integer.valueOf(this.getNome()), this.getStatus());
		this.prefeitura.getPrincipal().mostrarComida(this.prefeitura.getUnidadesComida());
		this.prefeitura.getPrincipal().mostrarOuro(this.prefeitura.getUnidadesOuro());
	}

	public Fazenda construirFazenda() {
		Principal principal = this.prefeitura.getPrincipal();
		if (this.prefeitura.getUnidadesComida() >= 100 && this.prefeitura.getUnidadesOuro() >= 500) {
			try {
				Thread.sleep(3000);
				this.prefeitura.addUnidadesComida(-100);
				this.prefeitura.addUnidadesOuro(-500);
				return new Fazenda(String.valueOf(principal.getVila().getQtdFazendas()), principal);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		String msg = "";

		if (this.prefeitura.getUnidadesComida() < 100 && this.prefeitura.getUnidadesOuro() >= 500)
			msg += 100 - this.prefeitura.getUnidadesComida() + " de comida";

		else if (this.prefeitura.getUnidadesComida() >= 100 && this.prefeitura.getUnidadesOuro() < 500)
			msg += 100 - this.prefeitura.getUnidadesOuro() + " de ouro";

		else
			msg += (100 - this.prefeitura.getUnidadesComida()) + " de comida e "
					+ (100 - this.prefeitura.getUnidadesOuro()) + " de ouro";

		this.prefeitura.getPrincipal().mostrarMensagemErro("Recursos insuficientes", "Você precisa de mais " + msg);
		this.status = Status.PARADO;
		this.tipoConstrucao = "";
		return null;
	}

	public MinaOuro construirMina() {
		Principal principal = this.prefeitura.getPrincipal();
		if (this.prefeitura.getUnidadesComida() >= 1000) {
			try {
				Thread.sleep(3000);
				this.prefeitura.addUnidadesComida(-100);
				this.prefeitura.addUnidadesOuro(-500);
				return new MinaOuro(String.valueOf(principal.getVila().getQtdMinasOuro()), principal);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		this.prefeitura.getPrincipal().mostrarMensagemErro("Recursos insuficientes",
				"Você precisa de mais " + (1000 - this.prefeitura.getUnidadesComida()) + " de comida");
		this.status = Status.PARADO;
		this.tipoConstrucao = "";
		return null;
	}

}
