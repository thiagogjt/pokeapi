package br.com.algar.repository;

import br.com.algar.domain.TeamPokemon;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the TeamPokemon entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TeamPokemonRepository extends JpaRepository<TeamPokemon,Long> {
    
}
