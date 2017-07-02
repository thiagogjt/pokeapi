package br.com.algar.web.rest;

import br.com.algar.PokeapiApp;

import br.com.algar.domain.Team;
import br.com.algar.domain.TeamPokemon;
import br.com.algar.domain.TeamPokemonAbility;
import br.com.algar.repository.TeamPokemonAbilityRepository;
import br.com.algar.repository.TeamPokemonRepository;
import br.com.algar.repository.TeamRepository;
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
 * Test class for the TeamPokemonAbilityResource REST controller.
 *
 * @see TeamPokemonAbilityResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PokeapiApp.class)
public class TeamPokemonAbilityResourceIntTest {

    private static final String DEFAULT_ABILITY_ID = "AAAAAAAAAA";
    private static final String UPDATED_ABILITY_ID = "BBBBBBBBBB";

    private static final String DEFAULT_ABILITY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ABILITY_NAME = "BBBBBBBBBB";

    @Autowired
    private TeamPokemonAbilityRepository teamPokemonAbilityRepository;

    @Autowired
    private TeamRepository teamRepository;

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

    @Autowired
    private UserRepository userRepository;

    private MockMvc restTeamPokemonAbilityMockMvc;

    private TeamPokemonAbility teamPokemonAbility;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TeamPokemonAbilityResource teamPokemonAbilityResource = new TeamPokemonAbilityResource(teamPokemonAbilityRepository);
        this.restTeamPokemonAbilityMockMvc = MockMvcBuilders.standaloneSetup(teamPokemonAbilityResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TeamPokemonAbility createEntity(EntityManager em) {
        TeamPokemonAbility teamPokemonAbility = new TeamPokemonAbility()
            .abilityId(DEFAULT_ABILITY_ID)
            .abilityName(DEFAULT_ABILITY_NAME);
        return teamPokemonAbility;
    }

    @Before
    public void initTest() {
        teamPokemonAbility = createEntity(em);
    }

    @Test
    @Transactional
    public void createTeamPokemonAbility() throws Exception {
        int databaseSizeBeforeCreate = teamPokemonAbilityRepository.findAll().size();

        // Create the TeamPokemonAbility
        restTeamPokemonAbilityMockMvc.perform(post("/api/team-pokemon-ability")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(teamPokemonAbility)))
            .andExpect(status().isCreated());

        // Validate the TeamPokemonAbility in the database
        List<TeamPokemonAbility> teamPokemonAbilityList = teamPokemonAbilityRepository.findAll();
        assertThat(teamPokemonAbilityList).hasSize(databaseSizeBeforeCreate + 1);
        TeamPokemonAbility testTeamPokemonAbility = teamPokemonAbilityList.get(teamPokemonAbilityList.size() - 1);
        assertThat(testTeamPokemonAbility.getAbilityId()).isEqualTo(DEFAULT_ABILITY_ID);
        assertThat(testTeamPokemonAbility.getAbilityName()).isEqualTo(DEFAULT_ABILITY_NAME);
    }

    @Test
    @Transactional
    public void createTeamPokemonAbilityWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = teamPokemonAbilityRepository.findAll().size();

        // Create the TeamPokemonAbility with an existing ID
        teamPokemonAbility.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTeamPokemonAbilityMockMvc.perform(post("/api/team-pokemon-ability")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(teamPokemonAbility)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<TeamPokemonAbility> teamPokemonAbilityList = teamPokemonAbilityRepository.findAll();
        assertThat(teamPokemonAbilityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllTeamPokemonAbility() throws Exception {
        // Initialize the database
        teamPokemonAbilityRepository.saveAndFlush(teamPokemonAbility);

        // Get all the teamPokemonAbilitiesList
        restTeamPokemonAbilityMockMvc.perform(get("/api/team-pokemon-ability?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(teamPokemonAbility.getId().intValue())))
            .andExpect(jsonPath("$.[*].abilityId").value(hasItem(DEFAULT_ABILITY_ID.toString())))
            .andExpect(jsonPath("$.[*].abilityName").value(hasItem(DEFAULT_ABILITY_NAME.toString())));
    }

    @Test
    @Transactional
    public void getTeamPokemonAbility() throws Exception {
        // Initialize the database
        teamPokemonAbilityRepository.saveAndFlush(teamPokemonAbility);

        // Get the teamPokemonAbility
        restTeamPokemonAbilityMockMvc.perform(get("/api/team-pokemon-ability/{id}", teamPokemonAbility.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(teamPokemonAbility.getId().intValue()))
            .andExpect(jsonPath("$.abilityId").value(DEFAULT_ABILITY_ID.toString()))
            .andExpect(jsonPath("$.abilityName").value(DEFAULT_ABILITY_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTeamPokemonAbility() throws Exception {
        // Get the teamPokemonAbility
        restTeamPokemonAbilityMockMvc.perform(get("/api/team-pokemon-ability/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTeamPokemonAbility() throws Exception {
        // Initialize the database
        teamPokemonAbilityRepository.saveAndFlush(teamPokemonAbility);
        int databaseSizeBeforeUpdate = teamPokemonAbilityRepository.findAll().size();

        // Update the teamPokemonAbility
        TeamPokemonAbility updatedTeamPokemonAbility = teamPokemonAbilityRepository.findOne(teamPokemonAbility.getId());
        updatedTeamPokemonAbility = updatedTeamPokemonAbility
            .abilityId(UPDATED_ABILITY_ID)
            .abilityName(UPDATED_ABILITY_NAME);

        restTeamPokemonAbilityMockMvc.perform(put("/api/team-pokemon-ability")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTeamPokemonAbility)))
            .andExpect(status().isOk());

        // Validate the TeamPokemonAbility in the database
        List<TeamPokemonAbility> teamPokemonAbilityList = teamPokemonAbilityRepository.findAll();
        assertThat(teamPokemonAbilityList).hasSize(databaseSizeBeforeUpdate);
        TeamPokemonAbility testTeamPokemonAbility = teamPokemonAbilityList.get(teamPokemonAbilityList.size() - 1);
        assertThat(testTeamPokemonAbility.getAbilityId()).isEqualTo(UPDATED_ABILITY_ID);
        assertThat(testTeamPokemonAbility.getAbilityName()).isEqualTo(UPDATED_ABILITY_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingTeamPokemonAbility() throws Exception {
        int databaseSizeBeforeUpdate = teamPokemonAbilityRepository.findAll().size();

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTeamPokemonAbilityMockMvc.perform(put("/api/team-pokemon-ability")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(teamPokemonAbility)))
            .andExpect(status().isCreated());

        // Validate the TeamPokemonAbility in the database
        List<TeamPokemonAbility> teamPokemonAbilityList = teamPokemonAbilityRepository.findAll();
        assertThat(teamPokemonAbilityList).hasSize(databaseSizeBeforeUpdate + 1);
    }

//    @Test
//    @Transactional
    public void deleteTeamPokemonAbility() throws Exception {
        // Initialize the database
        teamRepository.saveAndFlush(teamPokemonAbility.getTeamPokemon().getTeam());
        int databaseSizeBeforeDelete = teamPokemonAbilityRepository.findAll().size();

        // Get the teamPokemonAbility
        restTeamPokemonAbilityMockMvc.perform(delete("/api/team-pokemon-ability/{id}", teamPokemonAbility.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<TeamPokemonAbility> teamPokemonAbilityList = teamPokemonAbilityRepository.findAll();
        assertThat(teamPokemonAbilityList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TeamPokemonAbility.class);
        TeamPokemonAbility teamPokemonAbility1 = new TeamPokemonAbility();
        teamPokemonAbility1.setId(1L);
        TeamPokemonAbility teamPokemonAbility2 = new TeamPokemonAbility();
        teamPokemonAbility2.setId(teamPokemonAbility1.getId());
        assertThat(teamPokemonAbility1).isEqualTo(teamPokemonAbility2);
        teamPokemonAbility2.setId(2L);
        assertThat(teamPokemonAbility1).isNotEqualTo(teamPokemonAbility2);
        teamPokemonAbility1.setId(null);
        assertThat(teamPokemonAbility1).isNotEqualTo(teamPokemonAbility2);
    }
}
