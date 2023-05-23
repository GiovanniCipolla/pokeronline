package it.prova.pokeronline.repository.Tavolo;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import it.prova.pokeronline.model.Tavolo;

public interface TavoloRepository extends CrudRepository<Tavolo, Long> {

	@Query("select distinct t from Tavolo t left join fetch t.giocatori where t.id = ?1")
	Optional<Tavolo> findByIdConGiocatori(Long id);
	
}
