package br.com.algar.web.rest;

import br.com.algar.web.rest.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;
import me.sargunvohra.lib.pokekotlin.client.ErrorResponse;
import me.sargunvohra.lib.pokekotlin.client.PokeApi;
import me.sargunvohra.lib.pokekotlin.model.NamedApiResource;
import me.sargunvohra.lib.pokekotlin.model.NamedApiResourceList;
import me.sargunvohra.lib.pokekotlin.model.Pokemon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Pokemons.
 */
@RestController
@RequestMapping("/api")
public class PokemonResource {

    private final Logger log = LoggerFactory.getLogger(PokemonResource.class);

    private final PokeApi api;

    public PokemonResource(PokeApi api) {
        this.api = api;
    }

    /**
     * GET  /pokemons : get all the pokemons.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of pokemons in body
     */
    @GetMapping("/pokemons")
    @Timed
    public ResponseEntity<List<NamedApiResource>> getAllPokemons(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Pokemons");
        NamedApiResourceList pokemonList = api.getPokemonList(pageable.getOffset(), pageable.getPageSize());
        Page<NamedApiResource> page = new PageImpl<>(pokemonList.getResults(), pageable, pokemonList.getCount()) ;
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/pokemons");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /pokemon/:id : get the "id" pokemon.
     *
     * @param id the id of the pokemon to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the pokemon, or with status 404 (Not Found)
     */
    @GetMapping("/pokemons/{id}")
    @Timed
    public ResponseEntity<Pokemon> getPokemon(@PathVariable String id) {
        log.debug("REST request to get Pokemon : {}", id);
        Pokemon pokemon = null;
        try {
            pokemon = api.getPokemon(id);
        } catch (Throwable e) {
            if (e instanceof ErrorResponse && ((ErrorResponse) e).getCode() == 404) {
                log.debug("Pokemon : {} not found", id);
            } else {
                throw e;
            }
        }

        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(pokemon));
    }

}
