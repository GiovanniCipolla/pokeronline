package it.prova.pokeronline.dto;

public class RiscontroGiocataDTO {

	private int risultato;
	private Integer esperienaGuadagnata;
	
	
	public RiscontroGiocataDTO() {
		super();
	}


	public RiscontroGiocataDTO(int risultato, Integer esperienaGuadagnata) {
		super();
		this.risultato = risultato;
		this.esperienaGuadagnata = esperienaGuadagnata;
	}


	public int getRisultato() {
		return risultato;
	}


	public void setRisultato(int risultato) {
		this.risultato = risultato;
	}


	public Integer getEsperienaGuadagnata() {
		return esperienaGuadagnata;
	}


	public void setEsperienaGuadagnata(Integer esperienaGuadagnata) {
		this.esperienaGuadagnata = esperienaGuadagnata;
	}
	
	

}
