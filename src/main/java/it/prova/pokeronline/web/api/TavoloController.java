package it.prova.pokeronline.web.api;


import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import it.prova.pokeronline.dto.TavoloDTO;
import it.prova.pokeronline.model.Tavolo;
import it.prova.pokeronline.service.tavolo.TavoloService;
import it.prova.pokeronline.web.api.exception.IdNotNullForInsertException;

@RestController
@RequestMapping("/api/tavolo")
public class TavoloController {

	@Autowired
	private TavoloService tavoloService;

	
	@GetMapping
	public List<TavoloDTO> visualizzaTavoli() {
		return tavoloService.listAll();
	}
	

	@GetMapping("/{id}")
	public TavoloDTO visualizza(@PathVariable(required = true) Long id) {
		return tavoloService.visualizzaTavolo(id);
	}

	@GetMapping("/eager/{id}")
	public TavoloDTO visualizzaEager(@PathVariable(required = true) Long id) {
		return tavoloService.visualizzaTavoloEager(id);
	}
	
	@PostMapping("/searchNativeWithPagination")
	public ResponseEntity<Page<TavoloDTO>> searchNativePaginated(@RequestBody TavoloDTO example,
			@RequestParam(defaultValue = "0") Integer pageNo, @RequestParam(defaultValue = "0") Integer pageSize,
			@RequestParam(defaultValue = "id") String sortBy) {

		
		Page<Tavolo> entityPageResults = tavoloService.findByExampleNativeWithPagination(example.buildTavoloModel(),
				pageNo, pageSize, sortBy);

		return new ResponseEntity<Page<TavoloDTO>>(TavoloDTO.fromModelPageToDTOPage(entityPageResults),
				HttpStatus.OK);
	}
	
	@PostMapping("/private")
	public TavoloDTO createNew(@Valid @RequestBody TavoloDTO tavoloInput) {
		
		if (tavoloInput.getId() != null)
			throw new IdNotNullForInsertException("Non è ammesso fornire un id per la creazione");
		
		return tavoloService.creaTavolo(tavoloInput);
	}
	
	@PutMapping("/private/{id}")
	public TavoloDTO aggiorna(@Valid @RequestBody TavoloDTO tavoloInput, @PathVariable(required = true) Long id) {
		
		if (tavoloInput.getId() != null)
			throw new IdNotNullForInsertException("Non è ammesso fornire un id nel body per la modifica");
		
		return tavoloService.aggiorna(tavoloInput, id);
	}
	
	@DeleteMapping("/private/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable(required = true) Long id) {
		tavoloService.eliminaTavolo(id);
	}

	
}
