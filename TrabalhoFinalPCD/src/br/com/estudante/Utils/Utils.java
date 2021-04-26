package br.com.estudante.Utils;

public abstract class Utils {

	public static Integer calculaComida(int nivelAldeao) {
		int qtdTotal = 10;
		if (nivelAldeao > 1) {
			for (int i = 1; i < nivelAldeao; i++)
				qtdTotal *= 2;
		}
		return qtdTotal;
	}

	public static Integer calculaTempoTransporte(int nivelAldeao, Integer tempo) {
		int qtdTotal = tempo;
		if (nivelAldeao > 1) {
			for (int i = 1; i < nivelAldeao; i++)
				qtdTotal /= 2;
		}
		return qtdTotal;
	}
	
	public static Integer calculaOuro (int nivelAldeao) {
		int qtdTotal = 5;
		if (nivelAldeao > 1) {
			for (int i = 1; i < nivelAldeao; i++)
				qtdTotal *= 2;
		}
		return qtdTotal;
	}

}
