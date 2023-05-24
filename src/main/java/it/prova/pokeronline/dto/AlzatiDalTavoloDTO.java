package it.prova.pokeronline.dto;

public class AlzatiDalTavoloDTO {

	private String credito;
	private String esperienza;
	
	
	
	public AlzatiDalTavoloDTO() {
		super();
	}
	public AlzatiDalTavoloDTO(String credito, String esperienza) {
		super();
		this.credito = credito;
		this.esperienza = esperienza;
	}
	public String getCredito() {
		return credito;
	}
	public void setCredito(String credito) {
		this.credito = credito;
	}
	public String getEsperienza() {
		return esperienza;
	}
	public void setEsperienza(String esperienza) {
		this.esperienza = esperienza;
	}
	
	
}
