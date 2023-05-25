package it.prova.pokeronline.service.utente;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.prova.pokeronline.dto.GestioneUtenteDTO;
import it.prova.pokeronline.dto.UtenteDTO;
import it.prova.pokeronline.model.StatoUtente;
import it.prova.pokeronline.model.Tavolo;
import it.prova.pokeronline.model.Utente;
import it.prova.pokeronline.repository.Utente.UtenteRepository;
import it.prova.pokeronline.service.tavolo.TavoloService;


@Service
@Transactional
public class UtenteServiceImpl implements UtenteService {

	@Autowired
	private UtenteRepository repository;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	@Lazy
	private TavoloService tavoloService;

	@Transactional(readOnly = true)
	public List<Utente> listAllUtenti() {
		return (List<Utente>) repository.findAll();
	}

	@Transactional(readOnly = true)
	public Utente caricaSingoloUtente(Long id) {
		return repository.findById(id).orElse(null);
	}

	@Transactional(readOnly = true)
	public Utente caricaSingoloUtenteConRuoli(Long id) {
		return repository.findByIdConRuoli(id).orElse(null);
	}

	@Transactional
	public Utente aggiorna(Utente utenteInstance) {
		// deve aggiornare solo nome, cognome, username, ruoli
		Utente utenteReloaded = repository.findById(utenteInstance.getId()).orElse(null);
		if (utenteReloaded == null)
			throw new RuntimeException("Elemento non trovato");
		utenteReloaded.setNome(utenteInstance.getNome());
		utenteReloaded.setCognome(utenteInstance.getCognome());
		utenteReloaded.setUsername(utenteInstance.getUsername());
		utenteReloaded.setRuoli(utenteInstance.getRuoli());
		return repository.save(utenteReloaded);
	}

	@Transactional
	public void rimuovi(Long idToRemove) {
		
		Utente utenteReloaded = repository.findById(idToRemove).orElse(null);
		
        List<Tavolo> tavoliPresenti = tavoloService.tuttiITavoli();
        
		for (Tavolo tavoloItem : tavoliPresenti) {
			if(tavoloItem.getGiocatori().contains(utenteReloaded))
				tavoloItem.getGiocatori().remove(utenteReloaded);
		}
		
		repository.deleteById(idToRemove);
	}

//	@Transactional(readOnly = true)
//	public List<GestioneUtenteDTO> findByExample(GestioneUtenteDTO example) {		
//		return GestioneUtenteDTO.buildGestioneUtenteDTOListFromModelList(repository.findByExample(example.buildGestioneUtenteModel(true)));
//	}

	@Transactional
	public Utente eseguiAccesso(String username, String password) {
		return repository.findByUsernameAndPasswordAndStato(username, password, StatoUtente.ATTIVO);
	}

	@Transactional(readOnly = true)
	public Utente findByUsernameAndPassword(String username, String password) {
		return repository.findByUsernameAndPassword(username, password);
	}

	@Transactional
	public void changeUserAbilitation(Long utenteInstanceId) {
		Utente utenteInstance = caricaSingoloUtente(utenteInstanceId);
		if (utenteInstance == null)
			throw new RuntimeException("Elemento non trovato.");

		if (utenteInstance.getStato() == null || utenteInstance.getStato().equals(StatoUtente.CREATO))
			utenteInstance.setStato(StatoUtente.ATTIVO);
		else if (utenteInstance.getStato().equals(StatoUtente.ATTIVO))
			utenteInstance.setStato(StatoUtente.DISABILITATO);
		else if (utenteInstance.getStato().equals(StatoUtente.DISABILITATO))
			utenteInstance.setStato(StatoUtente.ATTIVO);
	}

	@Transactional(readOnly = true)
	public Utente findByUsername(String username) {
		return repository.findByUsername(username).orElse(null);
	}

	@Override
	@Transactional
	public UtenteDTO ricarica(Double cifraDaRicaricare) {
		
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Utente utenteInSessione = findByUsername(username);
		
		Double creditoTotale = utenteInSessione.getCreditoAccumulato() + cifraDaRicaricare;
		
		utenteInSessione.setCreditoAccumulato(creditoTotale);
		
		return UtenteDTO.buildUtenteDTOFromModel(utenteInSessione);
	}

	@Override
	@Transactional
	public GestioneUtenteDTO inserisciNuovo(GestioneUtenteDTO utenteInstance) {
		
		
		utenteInstance.setDataRegistrazione(LocalDate.now());
		utenteInstance.setStato(StatoUtente.ATTIVO);
		utenteInstance.setPassword(passwordEncoder.encode(utenteInstance.getPassword()));
		return GestioneUtenteDTO.buildGestioneUtenteDTOFromModel(repository.save(utenteInstance.buildGestioneUtenteModel(true)));
	}

	@Override
	public void inserisciNuovoUtente(Utente instance) {
		instance.setDataRegistrazione(LocalDate.now());
		instance.setStato(StatoUtente.CREATO);
		instance.setPassword(passwordEncoder.encode(instance.getPassword()));
		repository.save(instance);
		
	}

	
//	@Override
//	public GestioneUtenteDTO abilita(Long idDaAbilitare) {
//		
//		Utente utente =  repository.findById(idDaAbilitare).orElse(null);
//		
//	}
	
	




}
