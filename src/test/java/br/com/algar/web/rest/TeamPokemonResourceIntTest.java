package br.com.algar.web.rest;

import br.com.algar.PokeapiApp;

import br.com.algar.domain.Team;
import br.com.algar.domain.TeamPokemon;
import br.com.algar.repository.TeamPokemonRepository;
import br.com.algar.repository.UserRepository;
import br.com.algar.web.rest.errors.ExceptionTranslator;

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
 * Test class for the TeamPokemonResource REST controller.
 *
 * @see TeamPokemonResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PokeapiApp.class)
public class TeamPokemonResourceIntTest {

    private static final String DEFAULT_POKEMON_ID = "AAAAAAAAAA";
    private static final String UPDATED_POKEMON_ID = "BBBBBBBBBB";

    private static final String DEFAULT_POKEMON_NAME = "AAAAAAAAAA";
    private static final String UPDATED_POKEMON_NAME = "BBBBBBBBBB";

    @Autowired
    private TeamPokemonRepository teamPokemonRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTeamPokemonMockMvc;

    private TeamPokemon teamPokemon;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TeamPokemonResource teamPokemonResource = new TeamPokemonResource(teamPokemonRepository);
        this.restTeamPokemonMockMvc = MockMvcBuilders.standaloneSetup(teamPokemonResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TeamPokemon createEntity(EntityManager em) {
        TeamPokemon teamPokemon = new TeamPokemon()
            .pokemonId(DEFAULT_POKEMON_ID)
            .pokemonName(DEFAULT_POKEMON_NAME);
        return teamPokemon;
    }

    @Before
    public void initTest() {
        teamPokemon = createEntity(em);
    }

    @Test
    @Transactional
    public void createTeamPokemon() throws Exception {
        int databaseSizeBeforeCreate = teamPokemonRepository.findAll().size();

        // Create the TeamPokemon
        restTeamPokemonMockMvc.perform(post("/api/team-pokemons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(teamPokemon)))
            .andExpect(status().isCreated());

        // Validate the TeamPokemon in the database
        List<TeamPokemon> teamPokemonList = teamPokemonRepository.findAll();
        assertThat(teamPokemonList).hasSize(databaseSizeBeforeCreate + 1);
        TeamPokemon testTeamPokemon = teamPokemonList.get(teamPokemonList.size() - 1);
        assertThat(testTeamPokemon.getPokemonId()).isEqualTo(DEFAULT_POKEMON_ID);
        assertThat(testTeamPokemon.getPokemonName()).isEqualTo(DEFAULT_POKEMON_NAME);
    }

    @Test
    @Transactional
    public void createTeamPokemonWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = teamPokemonRepository.findAll().size();

        // Create the TeamPokemon with an existing ID
        teamPokemon.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTeamPokemonMockMvc.perform(post("/api/team-pokemons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(teamPokemon)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<TeamPokemon> teamPokemonList = teamPokemonRepository.findAll();
        assertThat(teamPokemonList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllTeamPokemons() throws Exception {
        // Initialize the database
        teamPokemonRepository.saveAndFlush(teamPokemon);

        // Get all the teamPokemonList
        restTeamPokemonMockMvc.perform(get("/api/team-pokemons?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(teamPokemon.getId().intValue())))
            .andExpect(jsonPath("$.[*].pokemonId").value(hasItem(DEFAULT_POKEMON_ID.toString())))
            .andExpect(jsonPath("$.[*].pokemonName").value(hasItem(DEFAULT_POKEMON_NAME.toString())));
    }

    @Test
    @Transactional
    public void getTeamPokemon() throws Exception {
        // Initialize the database
        teamPokemonRepository.saveAndFlush(teamPokemon);

        // Get the teamPokemon
        restTeamPokemonMockMvc.perform(get("/api/team-pokemons/{id}", teamPokemon.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(teamPokemon.getId().intValue()))
            .andExpect(jsonPath("$.pokemonId").value(DEFAULT_POKEMON_ID.toString()))
            .andExpect(jsonPath("$.pokemonName").value(DEFAULT_POKEMON_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTeamPokemon() throws Exception {
        // Get the teamPokemon
        restTeamPokemonMockMvc.perform(get("/api/team-pokemons/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTeamPokemon() throws Exception {
        // Initialize the database
        teamPokemonRepository.saveAndFlush(teamPokemon);
        int databaseSizeBeforeUpdate = teamPokemonRepository.findAll().size();

        // Update the teamPokemon
        TeamPokemon updatedTeamPokemon = teamPokemonRepository.findOne(teamPokemon.getId());
        updatedTeamPokemon
            .pokemonId(UPDATED_POKEMON_ID)
            .pokemonName(UPDATED_POKEMON_NAME);

        restTeamPokemonMockMvc.perform(put("/api/team-pokemons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTeamPokemon)))
            .andExpect(status().isOk());

        // Validate the TeamPokemon in the database
        List<TeamPokemon> teamPokemonList = teamPokemonRepository.findAll();
        assertThat(teamPokemonList).hasSize(databaseSizeBeforeUpdate);
        TeamPokemon testTeamPokemon = teamPokemonList.get(teamPokemonList.size() - 1);
        assertThat(testTeamPokemon.getPokemonId()).isEqualTo(UPDATED_POKEMON_ID);
        assertThat(testTeamPokemon.getPokemonName()).isEqualTo(UPDATED_POKEMON_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingTeamPokemon() throws Exception {
        int databaseSizeBeforeUpdate = teamPokemonRepository.findAll().size();

        // Create the TeamPokemon

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTeamPokemonMockMvc.perform(put("/api/team-pokemons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(teamPokemon)))
            .andExpect(status().isCreated());

        // Validate the TeamPokemon in the database
        List<TeamPokemon> teamPokemonList = teamPokemonRepository.findAll();
        assertThat(teamPokemonList).hasSize(databaseSizeBeforeUpdate + 1);
    }

//    @Test
//    @Transactional
    public void deleteTeamPokemon() throws Exception {
        // Initialize the database
        teamPokemonRepository.saveAndFlush(teamPokemon);
        int databaseSizeBeforeDelete = teamPokemonRepository.findAll().size();

        // Get the teamPokemon
        restTeamPokemonMockMvc.perform(delete("/api/team-pokemons/{id}", teamPokemon.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<TeamPokemon> teamPokemonList = teamPokemonRepository.findAll();
        assertThat(teamPokemonList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TeamPokemon.class);
        TeamPokemon teamPokemon1 = new TeamPokemon();
        teamPokemon1.setId(1L);
        TeamPokemon teamPokemon2 = new TeamPokemon();
        teamPokemon2.setId(teamPokemon1.getId());
        assertThat(teamPokemon1).isEqualTo(teamPokemon2);
        teamPokemon2.setId(2L);
        assertThat(teamPokemon1).isNotEqualTo(teamPokemon2);
        teamPokemon1.setId(null);
        assertThat(teamPokemon1).isNotEqualTo(teamPokemon2);
    }
}
