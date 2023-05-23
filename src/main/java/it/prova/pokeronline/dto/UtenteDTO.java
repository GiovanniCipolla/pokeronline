package it.prova.pokeronline.dto;

import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.constraints.NotBlank;

import it.prova.pokeronline.model.Utente;

public class UtenteDTO {

	private Long id;

	@NotBlank(message = "{username.notblank}")
	private String username;

	@NotBlank(message = "{esperienzaAccumulata.notblank}")
	private Integer esperienzaAccumulata;

	@NotBlank(message = "{creditoAccumulato.notblank}")
	private Double creditoAccumulato;

	public UtenteDTO() {
		super();
	}

	public UtenteDTO(Long id, @NotBlank(message = "{username.notblank}") String username,
			@NotBlank(message = "{esperienzaAccumulata.notblank}") Integer esperienzaAccumulata,
			@NotBlank(message = "{creditoAccumulato.notblank}") Double creditoAccumulato) {
		super();
		this.id = id;
		this.username = username;
		this.esperienzaAccumulata = esperienzaAccumulata;
		this.creditoAccumulato = creditoAccumulato;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Integer getEsperienzaAccumulata() {
		return esperienzaAccumulata;
	}

	public void setEsperienzaAccumulata(Integer esperienzaAccumulata) {
		this.esperienzaAccumulata = esperienzaAccumulata;
	}

	public Double getCreditoAccumulato() {
		return creditoAccumulato;
	}

	public void setCreditoAccumulato(Double creditoAccumulato) {
		this.creditoAccumulato = creditoAccumulato;
	}

	public Utente buildUtenteModel() {
		Utente result = new Utente(this.id, this.username, this.esperienzaAccumulata, this.creditoAccumulato);
		return result;
	}

	public static UtenteDTO buildUtenteDTOFromModel(Utente utenteModel) {
		UtenteDTO result = new UtenteDTO(utenteModel.getId(), utenteModel.getUsername(),
				utenteModel.getEsperienzaAccumulata(), utenteModel.getCreditoAccumulato());

		return result;
	}
	

	public static Set<UtenteDTO> createUtenteDTOSetFromModelSet(Set<Utente> modelListInput) {
		return modelListInput.stream().map(utenteEntity -> {
			return UtenteDTO.buildUtenteDTOFromModel(utenteEntity);
		}).collect(Collectors.toSet());
	}

}
