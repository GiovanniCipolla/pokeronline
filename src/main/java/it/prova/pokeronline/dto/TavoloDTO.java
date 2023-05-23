package it.prova.pokeronline.dto;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import it.prova.pokeronline.model.Tavolo;

public class TavoloDTO {

	private Long id;

	@Min(0)
	private Integer esperienzaMinima;

	@Min(0)
	private Double cifraMinima;

	@NotBlank(message = "{denominazione.notblank}")
	private String denominazione;

	@JsonIgnoreProperties(value = { "tavoli" })
	private UtenteDTO utenteCreazione;

	@JsonIgnoreProperties(value = { "tavolo" })
	private Set<UtenteDTO> giocatori = new HashSet<>(0);

	public TavoloDTO() {
		super();
	}
	
	

	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public Integer getEsperienzaMinima() {
		return esperienzaMinima;
	}



	public void setEsperienzaMinima(Integer esperienzaMinima) {
		this.esperienzaMinima = esperienzaMinima;
	}



	public Double getCifraMinima() {
		return cifraMinima;
	}



	public void setCifraMinima(Double cifraMinima) {
		this.cifraMinima = cifraMinima;
	}



	public String getDenominazione() {
		return denominazione;
	}



	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}



	public UtenteDTO getUtenteCreazione() {
		return utenteCreazione;
	}



	public void setUtenteCreazione(UtenteDTO utenteCreazione) {
		this.utenteCreazione = utenteCreazione;
	}



	public Set<UtenteDTO> getGiocatori() {
		return giocatori;
	}



	public void setGiocatori(Set<UtenteDTO> giocatori) {
		this.giocatori = giocatori;
	}



	public TavoloDTO(@Min(0) Integer esperienzaMinima, @Min(0) Double cifraMinima,
			@NotBlank(message = "{denominazione.notblank}") String denominazione, UtenteDTO utenteCreazione,
			Set<UtenteDTO> giocatori) {
		super();
		this.esperienzaMinima = esperienzaMinima;
		this.cifraMinima = cifraMinima;
		this.denominazione = denominazione;
		this.utenteCreazione = utenteCreazione;
		this.giocatori = giocatori;
	}

	public TavoloDTO(@NotBlank(message = "{denominazione.notblank}") String denominazione) {
		super();
		this.denominazione = denominazione;
	}

	public TavoloDTO(@Min(0) Integer esperienzaMinima, @Min(0) Double cifraMinima,
			@NotBlank(message = "{denominazione.notblank}") String denominazione) {
		super();
		this.esperienzaMinima = esperienzaMinima;
		this.cifraMinima = cifraMinima;
		this.denominazione = denominazione;
	}

	public TavoloDTO(Long id, @Min(0) Integer esperienzaMinima, @Min(0) Double cifraMinima,
			@NotBlank(message = "{denominazione.notblank}") String denominazione, UtenteDTO utenteCreazione) {
		super();
		this.id = id;
		this.esperienzaMinima = esperienzaMinima;
		this.cifraMinima = cifraMinima;
		this.denominazione = denominazione;
		this.utenteCreazione = utenteCreazione;
	}

	public static TavoloDTO buildTavoloDTOFromModel(Tavolo tavoloModel,boolean includiGiocatori) {
		TavoloDTO result = new TavoloDTO(tavoloModel.getId(), tavoloModel.getEsperienzaMinima(),
				tavoloModel.getCifraMinima(), tavoloModel.getDenominazione(),UtenteDTO.buildUtenteDTOFromModel(tavoloModel.getUtenteCreazione()) );

		if(includiGiocatori)
		result.setGiocatori(UtenteDTO.createUtenteDTOSetFromModelSet(tavoloModel.getGiocatori()));
		return result;
	}
	

	public Tavolo buildTavoloModel() {
		Tavolo result = new Tavolo(this.id, this.esperienzaMinima, this.cifraMinima,
				this.denominazione, this.utenteCreazione.buildUtenteModel());

		return result;
	}

	public static List<TavoloDTO> createTavoloDTOListFromModelList(List<Tavolo> modelListInput, boolean includeUtentiGiocatori) {
		return modelListInput.stream().map(tavoloEntity -> {
			TavoloDTO result = TavoloDTO.buildTavoloDTOFromModel(tavoloEntity,includeUtentiGiocatori);
			if(includeUtentiGiocatori)
				result.setGiocatori(UtenteDTO.createUtenteDTOSetFromModelSet(tavoloEntity.getGiocatori()));
			return result;
		}).collect(Collectors.toList());
	}
	
}
