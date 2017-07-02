package br.com.algar.web.rest;

import br.com.algar.repository.TeamRepository;
import com.codahale.metrics.annotation.Timed;
import br.com.algar.domain.TeamPokemon;

import br.com.algar.repository.TeamPokemonRepository;
import br.com.algar.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing TeamPokemon.
 */
@RestController
@RequestMapping("/api")
public class TeamPokemonResource {

    private final Logger log = LoggerFactory.getLogger(TeamPokemonResource.class);

    private static final String ENTITY_NAME = "teamPokemon";

    private final TeamPokemonRepository teamPokemonRepository;

    public TeamPokemonResource(TeamPokemonRepository teamPokemonRepository) {
        this.teamPokemonRepository = teamPokemonRepository;
    }

    /**
     * POST  /team-pokemons : Create a new teamPokemon.
     *
     * @param teamPokemon the teamPokemon to create
     * @return the ResponseEntity with status 201 (Created) and with body the new teamPokemon, or with status 400 (Bad Request) if the teamPokemon has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/team-pokemons")
    @Timed
    public ResponseEntity<TeamPokemon> createTeamPokemon(@RequestBody TeamPokemon teamPokemon) throws URISyntaxException {
        log.debug("REST request to save TeamPokemon : {}", teamPokemon);
        if (teamPokemon.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new teamPokemon cannot already have an ID")).body(null);
        }
        TeamPokemon result = teamPokemonRepository.save(teamPokemon);
        return ResponseEntity.created(new URI("/api/team-pokemons/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /team-pokemons : Updates an existing teamPokemon.
     *
     * @param teamPokemon the teamPokemon to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated teamPokemon,
     * or with status 400 (Bad Request) if the teamPokemon is not valid,
     * or with status 500 (Internal Server Error) if the teamPokemon couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/team-pokemons")
    @Timed
    public ResponseEntity<TeamPokemon> updateTeamPokemon(@RequestBody TeamPokemon teamPokemon) throws URISyntaxException {
        log.debug("REST request to update TeamPokemon : {}", teamPokemon);
        if (teamPokemon.getId() == null) {
            return createTeamPokemon(teamPokemon);
        }
        TeamPokemon result = teamPokemonRepository.save(teamPokemon);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, teamPokemon.getId().toString()))
            .body(result);
    }

    /**
     * GET  /team-pokemons : get all the teamPokemons.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of teamPokemons in body
     */
    @GetMapping("/team-pokemons")
    @Timed
    public List<TeamPokemon> getAllTeamPokemons() {
        log.debug("REST request to get all TeamPokemons");
        return teamPokemonRepository.findAll();
    }

    /**
     * GET  /team-pokemons/:id : get the "id" teamPokemon.
     *
     * @param id the id of the teamPokemon to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the teamPokemon, or with status 404 (Not Found)
     */
    @GetMapping("/team-pokemons/{id}")
    @Timed
    public ResponseEntity<TeamPokemon> getTeamPokemon(@PathVariable Long id) {
        log.debug("REST request to get TeamPokemon : {}", id);
        TeamPokemon teamPokemon = teamPokemonRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(teamPokemon));
    }

    /**
     * DELETE  /team-pokemons/:id : delete the "id" teamPokemon.
     *
     * @param id the id of the teamPokemon to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/team-pokemons/{id}")
    @Timed
    @Transactional
    public ResponseEntity<Void> deleteTeamPokemon(@PathVariable Long id) {
        log.debug("REST request to delete TeamPokemon : {}", id);
        TeamPokemon pokemon = teamPokemonRepository.getOne(id);
        pokemon.getTeam().removePokemons(pokemon);
        teamPokemonRepository.delete(pokemon);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
