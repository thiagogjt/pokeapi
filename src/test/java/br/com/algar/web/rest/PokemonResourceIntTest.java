package br.com.algar.web.rest;

import br.com.algar.PokeapiApp;
import br.com.algar.domain.TeamPokemon;
import br.com.algar.repository.TeamPokemonRepository;
import br.com.algar.web.rest.TeamPokemonResource;
import br.com.algar.web.rest.TestUtil;
import br.com.algar.web.rest.errors.ExceptionTranslator;
import me.sargunvohra.lib.pokekotlin.client.PokeApi;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the PokemonResource REST controller.
 *
 * @see PokemonResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PokeapiApp.class)
public class PokemonResourceIntTest {

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private PokeApi api;

    private MockMvc restPokemonMockMvc;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PokemonResource pokemonResource = new PokemonResource(api);
        this.restPokemonMockMvc = MockMvcBuilders.standaloneSetup(pokemonResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Test
    public void getAllPokemons() throws Exception {
        // Get all the pokemonList
        restPokemonMockMvc.perform(get("/api/pokemons?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(1)))
            .andExpect(jsonPath("$.[*].name").value(hasItem("bulbasaur")));
    }

    @Test
    public void getPokemonById() throws Exception {
        // Get the Pokemon
        restPokemonMockMvc.perform(get("/api/pokemons/{id}", 2))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(2))
            .andExpect(jsonPath("$.name").value("ivysaur"));
    }

    @Test
    public void getPokemonByName() throws Exception {
        // Get the Pokemon
        restPokemonMockMvc.perform(get("/api/pokemons/{id}", "ivysaur"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(2))
            .andExpect(jsonPath("$.name").value("ivysaur"));
    }

    @Test
    public void getNonExistingPokemon() throws Exception {
        // Get the teamPokemon
        restPokemonMockMvc.perform(get("/api/pokemons/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

}
