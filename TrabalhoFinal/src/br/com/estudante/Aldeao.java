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
		this.interrupt();
		this.setStatus("Parado");
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
