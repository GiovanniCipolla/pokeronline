package it.prova.pokeronline.service.tavolo;

import java.util.List;

import it.prova.pokeronline.dto.TavoloDTO;

public interface TavoloService {
	
	public List<TavoloDTO> listAll();
	
	public TavoloDTO creaTavolo(TavoloDTO tavolo);
	
	public TavoloDTO visualizzaTavolo(Long idDaVisualizzare);
	
	public TavoloDTO visualizzaTavoloEager(Long idDaVisualizzare);
	
	public TavoloDTO aggiorna(TavoloDTO tavolo,Long idDaAggiornare);
	
	public void eliminaTavolo(Long idDaEliminare);
}
