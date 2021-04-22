package br.com.estudante;

public class Aldeao extends Thread {
	private int num;
	private String status = "Parado";

	Aldeao(int num) {
		this.setNum(num);
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		if (this.num >= 0)
			this.num = num;
		else
			System.out.println("Número inserido é inválido");
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

	public void parar() {
		this.setStatus("Parado");
	}
	
	public void cultivar () {
		while (this.status.equals("Cultivando")) {
			/*
			 * 
			 * 
			 * 
			 * 
			 * 
			 * 
			 * 
			 * 
			 */
		}
	}

	public boolean construir(String qual, Prefeitura prefeitura) {
		int comida = prefeitura.getComida(), ouro = prefeitura.getOuro();
		boolean temRecurso = false;
		switch (qual) {
		case "Fazenda":
			if (comida >= 100 && ouro >= 500) {
				prefeitura.adicionarFazenda(new Fazenda());
				prefeitura.alterarValorComidaOuro(-100, -500);
				temRecurso = true;
			}
			break;
		case "Mina de ouro":
			if (comida >= 1000) {
				prefeitura.adicionarMinaDeOuro(new MinaDeOuro());
				prefeitura.alterarValorComidaOuro(-1000, 0);
				temRecurso = true;
			}
			break;
		case "Templo":
			if (prefeitura.getTemplo() != null) {
				if (comida >= 2000 && ouro >= 2000) {

					prefeitura.setTemplo(new Templo());
					prefeitura.alterarValorComidaOuro(-2000, -2000);
					temRecurso = true;
				}
			}
		}
		if (temRecurso)
			return true;
		return false;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
