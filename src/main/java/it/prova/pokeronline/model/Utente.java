package it.prova.pokeronline.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;


@Entity
@Table(name = "utente")
public class Utente {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	@Column(name = "nome")
	private String nome;
	@Column(name = "cognome")
	private String cognome;
	@Column(name = "username")
	private String username;
	@Column(name = "password")
	private String password;
	@Column(name = "dataregistrazione")
	private LocalDate dataRegistrazione;
	@Column(name = "stato")
	private StatoUtente stato;
	@Column(name = "esperienzaaccumulata")
	private Integer esperienzaAccumulata;
	@Column(name = "creditoaccumulato")
	private Double creditoAccumulato;

	@ManyToMany
	@JoinTable(name = "utente_ruolo", joinColumns = @JoinColumn(name = "utente_id", referencedColumnName = "ID"), inverseJoinColumns = @JoinColumn(name = "ruolo_id", referencedColumnName = "ID"))
	private Set<Ruolo> ruoli = new HashSet<>(0);

	public Utente() {
		super();
	}

	public Utente(String username,String password, String nome, String cognome,LocalDate dataRegistrazione, Integer esperienzaAccumulata, Double creditoAccumulato) {
		super();
		this.nome = nome;
		this.cognome = cognome;
		this.username = username;
		this.password = password;
		this.dataRegistrazione = dataRegistrazione;
		this.esperienzaAccumulata = esperienzaAccumulata;
		this.creditoAccumulato = creditoAccumulato;
	}

	

	public Utente(Long id, String username, Integer esperienzaAccumulata, Double creditoAccumulato) {
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

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public LocalDate getDataRegistrazione() {
		return dataRegistrazione;
	}

	public void setDataRegistrazione(LocalDate dataRegistrazione) {
		this.dataRegistrazione = dataRegistrazione;
	}

	public StatoUtente getStato() {
		return stato;
	}

	public void setStato(StatoUtente stato) {
		this.stato = stato;
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

	public Set<Ruolo> getRuoli() {
		return ruoli;
	}

	public void setRuoli(Set<Ruolo> ruoli) {
		this.ruoli = ruoli;
	}

	public boolean isAttivo() {
		return this.stato != null && this.stato.equals(StatoUtente.ATTIVO);
	}

	public boolean isDisabilitato() {
		return this.stato != null && this.stato.equals(StatoUtente.DISABILITATO);
	}
	
	public boolean isAdmin() {
		for (Ruolo ruoloItem : ruoli) {
			if (ruoloItem.getCodice().equals(Ruolo.ROLE_ADMIN))
				return true;
		}
		return false;
	}

}
