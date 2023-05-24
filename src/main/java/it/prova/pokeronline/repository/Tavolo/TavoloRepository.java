package it.prova.pokeronline.repository.Tavolo;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import it.prova.pokeronline.model.Tavolo;

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
					+ "AND (:datacreazione IS NULL OR t.datacreazione >= :datacreazione))"
					, nativeQuery = true)
	Page<Tavolo> findByExampleNativeWithPagination(@Param("denominazione") String denominazione,
			@Param("esperienzaminima") Integer esperienzaMinima, @Param("ciframinima") Double cifraMinima,
			@Param("datacreazione") LocalDate dataCreazione,Pageable pageable);
	
	
}
