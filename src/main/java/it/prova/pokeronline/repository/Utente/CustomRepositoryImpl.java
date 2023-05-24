package it.prova.pokeronline.repository.Utente;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Mac;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;

import it.prova.pokeronline.model.Utente;

public class CustomRepositoryImpl  implements CustomUtenteRepository {

	@PersistenceContext
	private EntityManager entityManager;
	
	
	@Override
	public List<Utente> findByExample(Utente example) {

		Map<String, Object> paramaterMap = new HashMap<String, Object>();
		List<String> whereClauses = new ArrayList<String>();

		StringBuilder queryBuilder = new StringBuilder("select u from Utente u left join fetch u.ruoli r where u.id = u.id ");

		if (example.getRuoli() != null && !example.getRuoli().isEmpty()) {
			whereClauses.add(" r in :ruoli ");
			paramaterMap.put("ruoli", example.getRuoli());
		}
		if (StringUtils.isNotEmpty(example.getNome())) {
			whereClauses.add(" u.nome  like :nome ");
			paramaterMap.put("nome", "%" + example.getNome() + "%");
		}
		if (StringUtils.isNotEmpty(example.getCognome())) {
			whereClauses.add(" u.cognome like :cognome ");
			paramaterMap.put("cognome", "%" + example.getCognome() + "%");
		}
		if (StringUtils.isNotEmpty(example.getUsername())) {
			whereClauses.add(" u.username like :username ");
			paramaterMap.put("username", "%" + example.getUsername() + "%");
		}
		
		
		if (example.getCreditoAccumulato() != null) {
			whereClauses.add("u.creditoAccumulato >= :creditoAccumulato ");
			paramaterMap.put("creditoAccumulato", example.getCreditoAccumulato());
		}
		
		if (example.getEsperienzaAccumulata() != null) {
			whereClauses.add("u.esperienzaAccumulata >= :esperienzaAccumulata ");
			paramaterMap.put("esperienzaAccumulata", example.getEsperienzaAccumulata());
		}
		
		if (example.getDataRegistrazione() != null) {
			whereClauses.add("u.dataRegistrazione >= :dataRegistrazione ");
			paramaterMap.put("dataRegistrazione", example.getDataRegistrazione());
		}
		if (example.getStato() != null) {
			whereClauses.add("u.stato >= :stato ");
			paramaterMap.put("stato", example.getStato());
		}
		
		queryBuilder.append(!whereClauses.isEmpty()?" and ":"");
		queryBuilder.append(StringUtils.join(whereClauses, " and "));
		TypedQuery<Utente> typedQuery = entityManager.createQuery(queryBuilder.toString(), Utente.class);

		for (String key : paramaterMap.keySet()) {
			typedQuery.setParameter(key, paramaterMap.get(key));
		}

		return typedQuery.getResultList();
	}

}