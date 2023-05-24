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

import it.prova.pokeronline.dto.TavoloDTO;
import it.prova.pokeronline.dto.UtenteDTO;
import it.prova.pokeronline.exception.GiocatoriNelTavoloException;
import it.prova.pokeronline.exception.IdNonValidoException;
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
			throw new UtenteNonValidoException();

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
			throw new IdNonValidoException();

		return TavoloDTO.buildTavoloDTOFromModel(tavoloDaVisualizzare, false);

	}

	@Override
	@Transactional(readOnly = true)
	public TavoloDTO visualizzaTavoloEager(Long idDaVisualizzare) {
		Tavolo tavoloDaVisualizzare = repository.findByIdConGiocatori(idDaVisualizzare).orElse(null);

		if (tavoloDaVisualizzare == null)
			throw new IdNonValidoException();

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
			throw new UtenteNonValidoException();

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
			throw new UtenteNonValidoException();

		if (!tavolo.getGiocatori().isEmpty())
			throw new GiocatoriNelTavoloException();

		repository.deleteById(idDaEliminare);

	}

	@Override
	public Page<Tavolo> findByExampleNativeWithPagination(Tavolo example, Integer pageNo, Integer pageSize,
			String sortBy) {

		
		
		return repository.findByExampleNativeWithPagination(example.getDenominazione(), example.getEsperienzaMinima(),
				example.getCifraMinima(), example.getDataCreazione(),
				PageRequest.of(pageNo, pageSize, Sort.by(sortBy)));
	}

}
