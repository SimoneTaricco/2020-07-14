package it.polito.tdp.PremierLeague.model;

public class Adiacenza {
	
	private Team partenza;
	private Team arrivo;
	private double peso;
	
	@Override
	public String toString() {
		return "Adiacenza [partenza=" + partenza + ", arrivo=" + arrivo + ", peso=" + peso + "]";
	}

	public Adiacenza(Team partenza, Team arrivo, double peso) {
		super();
		this.partenza = partenza;
		this.arrivo = arrivo;
		this.peso = peso;
	}

	public Team getPartenza() {
		return partenza;
	}

	public void setPartenza(Team partenza) {
		this.partenza = partenza;
	}

	public Team getArrivo() {
		return arrivo;
	}

	public void setArrivo(Team arrivo) {
		this.arrivo = arrivo;
	}

	public double getPeso() {
		return peso;
	}

	public void setPeso(double peso) {
		this.peso = peso;
	}
	
	

}
