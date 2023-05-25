package it.prova.pokeronline.service.tavolo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.prova.pokeronline.dto.AlzatiDalTavoloDTO;
import it.prova.pokeronline.dto.RiscontroGiocataDTO;
import it.prova.pokeronline.dto.SvuotaTavoliDTO;
import it.prova.pokeronline.dto.TavoloDTO;
import it.prova.pokeronline.dto.UtenteDTO;
import it.prova.pokeronline.exception.CreditoInsufficienteException;
import it.prova.pokeronline.exception.EsperienzaInsufficienteException;
import it.prova.pokeronline.exception.GiocatoriNelTavoloException;
import it.prova.pokeronline.exception.IdNonValidoException;
import it.prova.pokeronline.exception.ImpossibileGiocareCreditoException;
import it.prova.pokeronline.exception.NessunTavoloADisposizioneLastGameException;
import it.prova.pokeronline.exception.NonSedutoAlTavoloException;
import it.prova.pokeronline.exception.UtenteNonValidoException;
import it.prova.pokeronline.model.Tavolo;
import it.prova.pokeronline.model.Utente;
import it.prova.pokeronline.repository.Tavolo.TavoloRepository;
import it.prova.pokeronline.service.utente.UtenteService;

@Service
@Transactional
public class TavoloServiceImpl implements TavoloService {

	@Autowired
	private TavoloRepository repository;

	@Autowired
	private UtenteService utenteService;

	@Override
	@Transactional
	public TavoloDTO creaTavolo(TavoloDTO tavolo) {

		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Utente utenteInSessione = utenteService.findByUsername(username);

		if (tavolo.getUtenteCreazione() != null && !tavolo.getUtenteCreazione().getUsername().equals(username))
			throw new UtenteNonValidoException("Non è il tuo tavolo");

		tavolo.setUtenteCreazione(UtenteDTO.buildUtenteDTOFromModel(utenteInSessione));

		Tavolo tavoloDaCreare = tavolo.buildTavoloModel();

		if (tavoloDaCreare.getEsperienzaMinima() == null)
			tavoloDaCreare.setEsperienzaMinima(0);

		if (tavoloDaCreare.getCifraMinima() == null)
			tavoloDaCreare.setCifraMinima(1D);

		tavoloDaCreare.setDataCreazione(LocalDate.now());

		repository.save(tavoloDaCreare);

		return TavoloDTO.buildTavoloDTOFromModel(tavoloDaCreare, false);

	}

	@Override
	@Transactional(readOnly = true)
	public TavoloDTO visualizzaTavolo(Long idDaVisualizzare) {

		Tavolo tavoloDaVisualizzare = repository.findById(idDaVisualizzare).orElse(null);

		if (tavoloDaVisualizzare == null)
			throw new IdNonValidoException("non esiste questo tavolo");

		return TavoloDTO.buildTavoloDTOFromModel(tavoloDaVisualizzare, false);

	}

	@Override
	@Transactional(readOnly = true)
	public TavoloDTO visualizzaTavoloEager(Long idDaVisualizzare) {
		Tavolo tavoloDaVisualizzare = repository.findByIdConGiocatori(idDaVisualizzare).orElse(null);

		if (tavoloDaVisualizzare == null)
			throw new IdNonValidoException("non esiste questo tavolo");

		return TavoloDTO.buildTavoloDTOFromModel(tavoloDaVisualizzare, true);
	}

	@Override
	public List<TavoloDTO> listAll() {
		return TavoloDTO.createTavoloDTOListFromModelList((List<Tavolo>) repository.findAll(), true);
	}

	@Override
	@Transactional
	public TavoloDTO aggiorna(TavoloDTO tavolo, Long idDaAggiornare) {

		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Utente utenteInSessione = utenteService.findByUsername(username);

		if ((tavolo.getUtenteCreazione() != null && !tavolo.getUtenteCreazione().getUsername().equals(username))
				&& !utenteService.findByUsername(username).isAdmin())
			throw new UtenteNonValidoException("utente non valido");

		tavolo.setId(idDaAggiornare);
		tavolo.setUtenteCreazione(UtenteDTO.buildUtenteDTOFromModel(utenteInSessione));

		Tavolo tavoloDaAggiornare = tavolo.buildTavoloModel();

		if (tavoloDaAggiornare.getEsperienzaMinima() == null)
			tavoloDaAggiornare.setEsperienzaMinima(0);

		if (tavoloDaAggiornare.getCifraMinima() == null)
			tavoloDaAggiornare.setCifraMinima(1D);

		repository.save(tavoloDaAggiornare);

		return TavoloDTO.buildTavoloDTOFromModel(tavoloDaAggiornare, true);
	}

	@Override
	@Transactional
	public void eliminaTavolo(Long idDaEliminare) {

		Tavolo tavolo = repository.findById(idDaEliminare).orElse(null);

		String username = SecurityContextHolder.getContext().getAuthentication().getName();

		if (tavolo.getUtenteCreazione() != null && !tavolo.getUtenteCreazione().getUsername().equals(username))
			throw new UtenteNonValidoException("utente non valido");

		if (!tavolo.getGiocatori().isEmpty())
			throw new GiocatoriNelTavoloException("ci sono giocatori nel tavolo");

		repository.deleteById(idDaEliminare);

	}

	@Override
	@Transactional(readOnly = true)
	public Page<Tavolo> findByExampleNativeWithPagination(Tavolo example, Integer pageNo, Integer pageSize,
			String sortBy) {

		return repository.findByExampleNativeWithPagination(example.getDenominazione(), example.getEsperienzaMinima(),
				example.getCifraMinima(), example.getDataCreazione(),
				PageRequest.of(pageNo, pageSize, Sort.by(sortBy)));
	}

	@Override
	@Transactional
	public TavoloDTO uniscitiAlTavolo(Long idTavolo) {

		Tavolo tavolo = repository.findById(idTavolo).orElse(null);

		if (tavolo == null)
			throw new IdNonValidoException("tavolo non valido");

		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Utente utenteInSessione = utenteService.findByUsername(username);

		if (tavolo.getGiocatori().contains(utenteInSessione))
			throw new NonSedutoAlTavoloException("sei gia seduto al tavolo");

		if (utenteInSessione.getCreditoAccumulato() < tavolo.getCifraMinima())
			throw new CreditoInsufficienteException("non hai il credito per entrare al tavolo, povero pezzente");

		if (utenteInSessione.getEsperienzaAccumulata() < tavolo.getEsperienzaMinima())
			throw new EsperienzaInsufficienteException("non hai esperienza per entrare al tavolo");

		tavolo.getGiocatori().add(utenteInSessione);

		return TavoloDTO.buildTavoloDTOFromModel(tavolo, true);

	}

	@Override
	@Transactional
	public RiscontroGiocataDTO gioca(Long idTavolo) {

		Tavolo tavolo = repository.findById(idTavolo).orElse(null);

		if (tavolo == null)
			throw new IdNonValidoException("tavolo non valido");

		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Utente utenteInSessione = utenteService.findByUsername(username);

		if (!tavolo.getGiocatori().contains(utenteInSessione))
			throw new NonSedutoAlTavoloException("non sei seduto a questo tavolo");

		if (utenteInSessione.getCreditoAccumulato() == null || utenteInSessione.getCreditoAccumulato() == 0d) {
			throw new ImpossibileGiocareCreditoException("non hai una lira, non puoi giocare");
		}

		double segno = Math.random();
		if (segno < 0.5)
			segno = segno * -1;
		int somma = (int) (Math.random() * 500);
		int totale = (int) (segno * somma);

		Integer esperienzaGuadagnata = 0;

		if (totale > 0) {
			esperienzaGuadagnata += 5;
			if (totale > 200)
				esperienzaGuadagnata += 6;
			if (totale > 400)
				esperienzaGuadagnata += 7;
			if (totale > 499)
				esperienzaGuadagnata += 8;
		}

		if (totale <= 0) {
			esperienzaGuadagnata += 4;
			if (totale < -200)
				esperienzaGuadagnata += 3;
			if (totale < -400)
				esperienzaGuadagnata += 2;
			if (totale < -499)
				esperienzaGuadagnata += 1;
		}

		utenteInSessione.setEsperienzaAccumulata(esperienzaGuadagnata + utenteInSessione.getEsperienzaAccumulata());

		Double creditoDaInserire = utenteInSessione.getCreditoAccumulato() + totale;

		if (creditoDaInserire < 0) {
			creditoDaInserire = 0D;
		}

		utenteInSessione.setCreditoAccumulato(creditoDaInserire);

		RiscontroGiocataDTO result = new RiscontroGiocataDTO(totale, esperienzaGuadagnata);

		return result;

	}

	@Override
	public AlzatiDalTavoloDTO alzati(Long idTavolo) {

		Tavolo tavolo = repository.findById(idTavolo).orElse(null);

		if (tavolo == null)
			throw new IdNonValidoException("tavolo non valido");

		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Utente utenteInSessione = utenteService.findByUsername(username);

		if (!tavolo.getGiocatori().contains(utenteInSessione))
			throw new NonSedutoAlTavoloException("non sei seduto al tavolo , quindi non puoi alzarti");

		tavolo.getGiocatori().remove(utenteInSessione);

		AlzatiDalTavoloDTO result = new AlzatiDalTavoloDTO();

		result.setCredito(
				"Sei uscito dal tavolo , il tuo credito attuale e'" + utenteInSessione.getCreditoAccumulato());
		result.setEsperienza(
				"Sei uscito dal tavolo , la tua esperienza attuale e'" + utenteInSessione.getEsperienzaAccumulata());

		return result;
	}

	@Override
	public TavoloDTO lastGame() {

		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Utente utenteInSessione = utenteService.findByUsername(username);

		List<Tavolo> tavoliPresenti = (List<Tavolo>) repository.findAll();

		for (Tavolo tavoloItem : tavoliPresenti) {
			if (tavoloItem.getGiocatori().contains(utenteInSessione))
				return TavoloDTO.buildTavoloDTOFromModel(tavoloItem, true);
		}

		throw new NessunTavoloADisposizioneLastGameException("non eri seduto in nessun tavolo");
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Tavolo> cercaTavoliDisponibili(Integer pageNo, Integer pageSize, String sortBy) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Utente utenteInSessione = utenteService.findByUsername(username);
		Tavolo example = new Tavolo();
		example.setCifraMinima(utenteInSessione.getCreditoAccumulato());
		example.setEsperienzaMinima(utenteInSessione.getEsperienzaAccumulata());

		return repository.findByExampleNativeWithPagination(example.getDenominazione(), example.getEsperienzaMinima(),
				example.getCifraMinima(), example.getDataCreazione(),
				PageRequest.of(pageNo, pageSize, Sort.by(sortBy)));
	}

	@Override
	@Transactional(readOnly = true)
	public List<Tavolo> tuttiITavoli() {
		return (List<Tavolo>) repository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public List<TavoloDTO> listaTavoliConSogliaEsperienzaGiocatore(Integer soglia) {
		
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Utente utenteInSessione = utenteService.findByUsername(username);

		return TavoloDTO.createTavoloDTOListFromModelList(
				repository.estraiTavoliConAlmenoUnUtenteAlDiSopraDiSoglia(utenteInSessione.getId(), soglia), true);
	}

	@Override
	public TavoloDTO trovaTavoloConEsperienzaMassima() {
		return TavoloDTO.buildTavoloDTOFromModel(repository.trovaTavoloConMassimaEsperienzaGiocatori(), true);
	}

	@Override
	public String svotaUtenti(List<SvuotaTavoliDTO> tavoli) {
		
		repository.svuotaTavoliCreatiDaUtenti(SvuotaTavoliDTO.createListStringToDTO(tavoli));
		
		return "fatto";
	}
	
	

}
