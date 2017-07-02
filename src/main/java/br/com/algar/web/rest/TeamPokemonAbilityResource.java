package br.com.algar.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.algar.domain.TeamPokemonAbility;

import br.com.algar.repository.TeamPokemonAbilityRepository;
import br.com.algar.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing TeamPokemonAbility.
 */
@RestController
@RequestMapping("/api")
public class TeamPokemonAbilityResource {

    private final Logger log = LoggerFactory.getLogger(TeamPokemonAbilityResource.class);

    private static final String ENTITY_NAME = "teamPokemonAbility";

    private final TeamPokemonAbilityRepository teamPokemonAbilityRepository;

    public TeamPokemonAbilityResource(TeamPokemonAbilityRepository teamPokemonAbilityRepository) {
        this.teamPokemonAbilityRepository = teamPokemonAbilityRepository;
    }

    /**
     * POST  /team-pokemon-ability : Create a new teamPokemonAbility.
     *
     * @param teamPokemonAbility the teamPokemonAbility to create
     * @return the ResponseEntity with status 201 (Created) and with body the new teamPokemonAbility, or with status 400 (Bad Request) if the teamPokemonAbility has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/team-pokemon-ability")
    @Timed
    public ResponseEntity<TeamPokemonAbility> createTeamPokemonAbility(@RequestBody TeamPokemonAbility teamPokemonAbility) throws URISyntaxException {
        log.debug("REST request to save TeamPokemonAbility : {}", teamPokemonAbility);
        if (teamPokemonAbility.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new teamPokemonAbility cannot already have an ID")).body(null);
        }
        TeamPokemonAbility result = teamPokemonAbilityRepository.save(teamPokemonAbility);
        return ResponseEntity.created(new URI("/api/team-pokemon-ability/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /team-pokemon-ability : Updates an existing teamPokemonAbility.
     *
     * @param teamPokemonAbility the teamPokemonAbility to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated teamPokemonAbility,
     * or with status 400 (Bad Request) if the teamPokemonAbility is not valid,
     * or with status 500 (Internal Server Error) if the teamPokemonAbility couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/team-pokemon-ability")
    @Timed
    public ResponseEntity<TeamPokemonAbility> updateTeamPokemonAbility(@RequestBody TeamPokemonAbility teamPokemonAbility) throws URISyntaxException {
        log.debug("REST request to update TeamPokemonAbility : {}", teamPokemonAbility);
        if (teamPokemonAbility.getId() == null) {
            return createTeamPokemonAbility(teamPokemonAbility);
        }
        TeamPokemonAbility result = teamPokemonAbilityRepository.save(teamPokemonAbility);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, teamPokemonAbility.getId().toString()))
            .body(result);
    }

    /**
     * GET  /team-pokemon-ability : get all the teamPokemonAbility.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of teamPokemonAbility in body
     */
    @GetMapping("/team-pokemon-ability")
    @Timed
    public List<TeamPokemonAbility> getAllTeamPokemonAbility() {
        log.debug("REST request to get all TeamPokemonAbility");
        return teamPokemonAbilityRepository.findAll();
    }

    /**
     * GET  /team-pokemon-ability/:id : get the "id" teamPokemonAbility.
     *
     * @param id the id of the teamPokemonAbility to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the teamPokemonAbility, or with status 404 (Not Found)
     */
    @GetMapping("/team-pokemon-ability/{id}")
    @Timed
    public ResponseEntity<TeamPokemonAbility> getTeamPokemonAbility(@PathVariable Long id) {
        log.debug("REST request to get TeamPokemonAbility : {}", id);
        TeamPokemonAbility teamPokemonAbility = teamPokemonAbilityRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(teamPokemonAbility));
    }

    /**
     * DELETE  /team-pokemon-ability/:id : delete the "id" teamPokemonAbility.
     *
     * @param id the id of the teamPokemonAbility to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/team-pokemon-ability/{id}")
    @Timed
    @Transactional
    public ResponseEntity<Void> deleteTeamPokemonAbility(@PathVariable Long id) {
        log.debug("REST request to delete TeamPokemonAbility : {}", id);
        TeamPokemonAbility pokemonAbilities = teamPokemonAbilityRepository.getOne(id);
        pokemonAbilities.getTeamPokemon().removeAbility(pokemonAbilities);
        teamPokemonAbilityRepository.delete(pokemonAbilities);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
