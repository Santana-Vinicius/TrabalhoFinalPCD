package br.com.estudante;

public class Aldeao extends Thread {
	private String nome;
	private Enum<Status> status;
	private Fazenda fazenda;
	private Prefeitura prefeitura;
	
	
	/*
	 * Run
	 */
	@Override
	public void run() {
		while (status != Status.SACRIFICADO) {
			
			try {
				Thread.sleep(2000);
				System.out.println(this.getName() + " está " + getStatus());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			switch (status.toString()) {
			case "Cultivando": 
				cultivar();
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
		this.fazenda = fazenda;
	}

	public Prefeitura getPrefeitura() {
		return prefeitura;
	}

	public void setPrefeitura(Prefeitura prefeitura) {
		this.prefeitura = prefeitura;
	}

	/*
	 * Constructor
	 */
	public Aldeao(String name, Prefeitura prefeitura) {
		setNome(name);
		setName("Aldeao " + getNome());
		setStatus(Status.PARADO);
		setPrefeitura(prefeitura);
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
			Integer comidaProduzida = fazenda.cultivar();
			// Sleep de duas horas para Transportar
			Thread.sleep(1000);
			System.out.println(this.getName() + "está indo entregar a comida");
			prefeitura.addUnidadesComida(comidaProduzida);
			System.out.println(this.getName() + "entregou a comida");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
