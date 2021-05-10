package br.com.estudante;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import br.com.estudante.tela.Tela;

public class Templo extends Thread {
	private String nome;
	private Tela tela;
	private String nomeAldeoes;
	private List<String> ataques = new ArrayList<String>();
	private ArrayList<Aldeao> religiosos = new ArrayList<Aldeao>();
	private String tipoEvolucao;

	Templo(Tela tela) {
		this.setPrincipal(tela);
		this.nomeAldeoes = "";
		setNome("Templo " + this.tela.getCivilizacao());
		setTipoEvolucao("");
	}

	public void run() {
		while (true) {
			this.tela.mostrarOferendaFe(this.tela.getVila().getPrefeitura().getOferendasFe());
			if (this.getTipoEvolucao() != "") {
				this.evoluir();
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

	public String getTipoEvolucao() {
		return tipoEvolucao;
	}

	public void setTipoEvolucao(String tipoEvolucao) {
		this.tipoEvolucao = tipoEvolucao;
	}

	public List<String> getAtaques() {
		return ataques;
	}

	public void setAtaques(List<String> ataques) {
		this.ataques = ataques;
	}

	public void setAtaques(String ataque) {
		this.ataques.add(ataque);
	}

	public Tela getPrincipal() {
		return tela;
	}

	public void setPrincipal(Tela tela) {
		this.tela = tela;
	}

	public void setNomeAldeoes() {
		synchronized (this) {
			this.nomeAldeoes = "";
			SortedSet<String> nomes = new TreeSet<String>();

			for (Aldeao religioso : religiosos)
				nomes.add(String.valueOf(Integer.valueOf(religioso.getNome()) + 1));

			for (String nomeAldeao : nomes) {
				this.nomeAldeoes += nomeAldeao + " ";
			}
		}
	}

	public synchronized void addReligioso(Aldeao aldeao) {
		boolean tem = false;
		for (Aldeao religioso : religiosos) {
			if (religioso.equals(aldeao))
				tem = true;
		}
		if (!tem) {
			this.religiosos.add(aldeao);
			this.setNomeAldeoes();
		}
	}

	public synchronized void removeReligioso(Aldeao aldeao) {
		this.religiosos.remove(aldeao);
		this.setNomeAldeoes();
	}

	public void procuraremoveReligioso(Aldeao aldeao) {
		if (this.religiosos.contains(aldeao))
			this.religiosos.remove(aldeao);
	}

	public synchronized String getNomeAldeoes() {
		return nomeAldeoes;
	}

	public synchronized int getQtdReligiosos() {
		return this.religiosos.size();
	}

	/*
	 * Funcoes o Templo
	 */

	private void evoluir() {
		int oferendasFe = this.tela.getVila().getPrefeitura().getOferendasFe();
		switch (this.tipoEvolucao) {
		case "Nuvem de gafanhotos":
			this.evoluirNuvemGafanhoto(oferendasFe);
			break;
		case "Morte dos primogênitos":
			this.evoluirMortePrimogenito(oferendasFe);
			break;
		case "Chuva de pedras":
			this.evoluirChuvaPedras(oferendasFe);
			break;
		case "Proteção contra nuvem de gafanhotos":
			this.evoluirProtecaoGafanhotos(oferendasFe);
			break;
		case "Proteção contra morte dos primogênitos":
			this.evoluirProtecaoPrimogenitos(oferendasFe);
			break;
		case "Proteção contra chuva de pedras":
			this.evoluirProtecaoPedras(oferendasFe);
			break;

		}
	}

	private void evoluirProtecaoGafanhotos(int oferendasFe) {
		if (oferendasFe >= 5000) {
			if (!this.getPrincipal().getVila().isProtecaoGafanhotos()) {
				try {
					Thread.sleep(2500);
					this.getPrincipal().getVila().getPrefeitura().addOferendasFe(-5000);
					this.getPrincipal().getVila().setProtecaoGafanhotos(true);
				} catch (InterruptedException e) {
					this.setTipoEvolucao("");
					this.run();
				}
			} else {
				this.getPrincipal().mostrarMensagemErro("Erro",
						"Proteção contra nuvem de gafanhotos já está habilitada!");
			}
		} else {
			this.getPrincipal().mostrarMensagemErro("Recursos insuficientes",
					"Você precisa de mais " + (5000 - oferendasFe) + " oferendas de fé");
		}
		this.setTipoEvolucao("");
	}

	private void evoluirProtecaoPrimogenitos(int oferendasFe) {
		if (oferendasFe >= 6000) {
			if (!this.getPrincipal().getVila().isProtecaoPrimogenitos()) {
				try {
					Thread.sleep(2500);
					this.getPrincipal().getVila().getPrefeitura().addOferendasFe(-6000);
					this.getPrincipal().getVila().setProtecaoPrimogenitos(true);
				} catch (InterruptedException e) {
					this.setTipoEvolucao("");
					this.run();
				}
			} else {
				this.getPrincipal().mostrarMensagemErro("Erro",
						"Proteção contra morte dos primogênitos já está habilitada!");
			}
		} else {
			this.getPrincipal().mostrarMensagemErro("Recursos insuficientes",
					"Você precisa de mais " + (6000 - oferendasFe) + " oferendas de fé");
		}
		this.setTipoEvolucao("");
	}

	private void evoluirProtecaoPedras(int oferendasFe) {
		if (oferendasFe >= 7000) {
			if (!this.getPrincipal().getVila().isProtecaoPedras()) {
				try {
					Thread.sleep(2500);
					this.getPrincipal().getVila().getPrefeitura().addOferendasFe(-7000);
					this.getPrincipal().getVila().setProtecaoPedras(true);
				} catch (InterruptedException e) {
					this.setTipoEvolucao("");
					this.run();
				}
			} else {
				this.getPrincipal().mostrarMensagemErro("Erro", "Proteção chuva de pedras já está habilitada!");
			}
		} else {
			this.getPrincipal().mostrarMensagemErro("Recursos insuficientes",
					"Você precisa de mais " + (7000 - oferendasFe) + " oferendas de fé");
		}
		this.setTipoEvolucao("");
	}

	public void evoluirNuvemGafanhoto(int oferendasFe) {
		if (oferendasFe >= 1000) {
			if (!this.ataques.contains("Nuvem de gafanhotos")) {
				try {
					Thread.sleep(2500);
					this.getPrincipal().getVila().getPrefeitura().addOferendasFe(-1000);
					this.setAtaques("Nuvem de gafanhotos");
					this.getPrincipal().mostrarAtaques(this.ataques);
				} catch (InterruptedException e) {
					this.setTipoEvolucao("");
					this.run();
				}
			} else {
				this.getPrincipal().mostrarMensagemErro("Erro", "Nuvem de gafanhotos já está habilitada!");
			}
		} else {
			this.getPrincipal().mostrarMensagemErro("Recursos insuficientes",
					"Você precisa de mais " + (1000 - oferendasFe) + " oferendas de fé");
		}
		this.setTipoEvolucao("");
	}

	private void evoluirMortePrimogenito(int oferendasFe) {
		if (oferendasFe >= 1500) {
			if (!this.ataques.contains("Morte dos primogênitos")) {
				try {
					Thread.sleep(2500);
					this.getPrincipal().getVila().getPrefeitura().addOferendasFe(-1500);
					this.setAtaques("Morte dos primogênitos");
					this.getPrincipal().mostrarAtaques(this.ataques);
				} catch (InterruptedException e) {
					this.setTipoEvolucao("");
					this.run();
				}
			} else {
				this.getPrincipal().mostrarMensagemErro("Erro", "Morte dos primogênitos já está habilitado!");
			}
		} else {
			this.getPrincipal().mostrarMensagemErro("Recursos insuficientes",
					"Você precisa de mais " + (1500 - oferendasFe) + " oferendas de fé");
		}
		this.setTipoEvolucao("");
	}

	private void evoluirChuvaPedras(int oferendasFe) {
		if (oferendasFe >= 2000) {
			if (!this.ataques.contains("Chuva de pedras")) {
				try {
					Thread.sleep(2500);
					this.getPrincipal().getVila().getPrefeitura().addOferendasFe(-2000);
					this.setAtaques("Chuva de pedras");
					this.getPrincipal().mostrarAtaques(this.ataques);
				} catch (InterruptedException e) {
					this.setTipoEvolucao("");
					this.run();
				}
			} else {
				this.getPrincipal().mostrarMensagemErro("Erro", "Chuva de pedras já está habilitada!");
			}
		} else {
			this.getPrincipal().mostrarMensagemErro("Recursos insuficientes",
					"Você precisa de mais " + (2000 - oferendasFe) + " oferendas de fé");
		}
		this.setTipoEvolucao("");
	}

	public Integer orar() {

		return 2;
	}

	public Integer Sacrificar(Aldeao aldeao) {
		this.removeReligioso(aldeao);
		return 100;
	}

}
