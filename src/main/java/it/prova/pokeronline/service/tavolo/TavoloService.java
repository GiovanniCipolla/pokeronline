package it.prova.pokeronline.service.tavolo;

import java.util.List;

import org.springframework.data.domain.Page;

import it.prova.pokeronline.dto.TavoloDTO;
import it.prova.pokeronline.model.Tavolo;

public interface TavoloService {
	
	public List<TavoloDTO> listAll();
	
	public TavoloDTO creaTavolo(TavoloDTO tavolo);
	
	public TavoloDTO visualizzaTavolo(Long idDaVisualizzare);
	
	public TavoloDTO visualizzaTavoloEager(Long idDaVisualizzare);
	
	public TavoloDTO aggiorna(TavoloDTO tavolo,Long idDaAggiornare);
	
	public void eliminaTavolo(Long idDaEliminare);
	
	public Page<Tavolo> findByExampleNativeWithPagination(Tavolo example, Integer pageNo, Integer pageSize,
			String sortBy);
}
