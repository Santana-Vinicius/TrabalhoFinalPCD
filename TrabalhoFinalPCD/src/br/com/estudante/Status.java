package br.com.estudante;

public enum Status {
	PARADO("Parado"), CONSTRUINDO("Construindo"), CULTIVANDO("Cultivando"), MINERANDO("Minerando"), ORANDO("Orando"),
	SACRIFICADO("Sacrificado");

	private String description;

	private Status(String description) {
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}

	public String toString() {
		return getDescription();
	}

}
