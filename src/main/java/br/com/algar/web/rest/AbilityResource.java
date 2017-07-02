package br.com.algar.web.rest;

import br.com.algar.web.rest.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;
import me.sargunvohra.lib.pokekotlin.client.ErrorResponse;
import me.sargunvohra.lib.pokekotlin.client.PokeApi;
import me.sargunvohra.lib.pokekotlin.model.Ability;
import me.sargunvohra.lib.pokekotlin.model.NamedApiResource;
import me.sargunvohra.lib.pokekotlin.model.NamedApiResourceList;
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
 * REST controller for managing Abilities.
 */
@RestController
@RequestMapping("/api")
public class AbilityResource {

    private final Logger log = LoggerFactory.getLogger(AbilityResource.class);

    private final PokeApi api;

    public AbilityResource(PokeApi api) {
        this.api = api;
    }

    /**
     * GET  /abilities : get all the abilities.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of abilities in body
     */
    @GetMapping("/abilities")
    @Timed
    public ResponseEntity<List<NamedApiResource>> getAllAbilities(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Ability");
        NamedApiResourceList abilityList = api.getAbilityList(pageable.getOffset(), pageable.getPageSize());
        Page<NamedApiResource> page = new PageImpl<>(abilityList.getResults(), pageable, abilityList.getCount()) ;
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/abilities");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /abilities/:id : get the "id" ability.
     *
     * @param id the id of the ability to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the ability, or with status 404 (Not Found)
     */
    @GetMapping("/abilities/{id}")
    @Timed
    public ResponseEntity<Ability> getAbility(@PathVariable String id) {
        log.debug("REST request to get Ability : {}", id);
        Ability ability = null;
        try {
            ability = api.getAbility(id);
        } catch (Throwable e) {
            if (e instanceof ErrorResponse && ((ErrorResponse) e).getCode() == 404) {
                log.debug("Ability : {} not found", id);
            } else {
                throw e;
            }
        }

        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(ability));
    }

}
