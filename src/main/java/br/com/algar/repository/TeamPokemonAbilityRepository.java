package br.com.algar.repository;

import br.com.algar.domain.TeamPokemonAbility;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the TeamPokemonAbility entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TeamPokemonAbilityRepository extends JpaRepository<TeamPokemonAbility,Long> {

}
