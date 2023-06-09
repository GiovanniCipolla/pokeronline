package it.prova.pokeronline.service.tavolo;

import java.util.List;

import org.springframework.data.domain.Page;

import it.prova.pokeronline.dto.AlzatiDalTavoloDTO;
import it.prova.pokeronline.dto.RiscontroGiocataDTO;
import it.prova.pokeronline.dto.SvuotaTavoliDTO;
import it.prova.pokeronline.dto.TavoloDTO;
import it.prova.pokeronline.dto.UtenteDTO;
import it.prova.pokeronline.model.Tavolo;

public interface TavoloService {
	
	public List<TavoloDTO> listAll();
	
	public List<Tavolo> tuttiITavoli();
	
	public TavoloDTO creaTavolo(TavoloDTO tavolo);
	
	public TavoloDTO visualizzaTavolo(Long idDaVisualizzare);
	
	public TavoloDTO visualizzaTavoloEager(Long idDaVisualizzare);
	
	public TavoloDTO aggiorna(TavoloDTO tavolo,Long idDaAggiornare);
	
	public void eliminaTavolo(Long idDaEliminare);
	
	public Page<Tavolo> findByExampleNativeWithPagination(Tavolo example, Integer pageNo, Integer pageSize,
			String sortBy);
	
	public Page<Tavolo> cercaTavoliDisponibili( Integer pageNo, Integer pageSize,
			String sortBy);
	
	public TavoloDTO uniscitiAlTavolo(Long idTavolo);
	
	public RiscontroGiocataDTO gioca(Long idTavolo);
	
	public AlzatiDalTavoloDTO alzati(Long idTavolo);
	
	public TavoloDTO lastGame();
	
	public List<TavoloDTO> listaTavoliConSogliaEsperienzaGiocatore(Integer soglia);
	
	public TavoloDTO trovaTavoloConEsperienzaMassima();
	
	public String svotaUtenti(List<SvuotaTavoliDTO> tavoli);
}
