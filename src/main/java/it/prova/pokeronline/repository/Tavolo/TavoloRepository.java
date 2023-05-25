package it.prova.pokeronline.repository.Tavolo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import it.prova.pokeronline.model.Tavolo;
import it.prova.pokeronline.model.Utente;

public interface TavoloRepository extends PagingAndSortingRepository<Tavolo, Long>, JpaSpecificationExecutor<Tavolo> {

	@Query("select distinct t from Tavolo t left join fetch t.giocatori where t.id = ?1")
	Optional<Tavolo> findByIdConGiocatori(Long id);

	@Query(value = "SELECT t.* " + "FROM tavolo t "
			+ "WHERE ((:denominazione IS NULL OR LOWER(t.denominazione) LIKE %:denominazione%)  "
			+ "AND (:esperienzaminima IS NULL OR t.esperienzaminima > :esperienzaminima) "
			+ "AND (:ciframinima IS NULL OR t.ciframinima >= :ciframinima) "
			+ "AND (:datacreazione IS NULL OR t.datacreazione >= :datacreazione)) "

			, countQuery = "SELECT t.* " + "FROM tavolo t "
					+ "WHERE ((:denominazione IS NULL OR LOWER(t.denominazione) LIKE %:denominazione%)  "
					+ "AND (:esperienzaminima IS NULL OR t.esperienzaminima > :esperienzaminima) "
					+ "AND (:ciframinima IS NULL OR t.ciframinima >= :ciframinima) "
					+ "AND (:datacreazione IS NULL OR t.datacreazione >= :datacreazione))", nativeQuery = true)
	Page<Tavolo> findByExampleNativeWithPagination(@Param("denominazione") String denominazione,
			@Param("esperienzaminima") Integer esperienzaMinima, @Param("ciframinima") Double cifraMinima,
			@Param("datacreazione") LocalDate dataCreazione, Pageable pageable);

	@Query(value = "select t.* from tavolo t " + "where t.utente_id = :idInSessione and exists"
			+ "(select * from tavolo_giocatori p inner join utente u "
			+ "on p.giocatori_id = u.id where "
			+ "p.tavolo_id = t.id and u.esperienzaaccumulata >= :soglia)", nativeQuery = true)
	List<Tavolo> estraiTavoliConAlmenoUnUtenteAlDiSopraDiSoglia(@Param("idInSessione") Long idInSessione,
			@Param("soglia") Integer soglia);
	
	@Query(value = "SELECT t.* "
			+ "	FROM tavolo AS t "
			+ "	JOIN tavolo_giocatori AS tg ON t.id = tg.tavolo_id "
			+ "	JOIN utente AS u ON tg.giocatori_id = u.id "
			+ "	GROUP BY t.id "
			+ "	ORDER BY SUM(u.esperienzaaccumulata) DESC "
			+ "	LIMIT 1", nativeQuery = true)
	Tavolo trovaTavoloConMassimaEsperienzaGiocatori();
	
	@Query( value = "SELECT distinct u.*\r\n"
			+ "	FROM utente u\r\n"
			+ "	inner JOIN tavolo t ON u.id = t.utente_id\r\n"
			+ "	WHERE t.datacreazione < u.dataregistrazione", nativeQuery = true)
	List<Utente> listaUtentiDateSbagliate();
	
}
