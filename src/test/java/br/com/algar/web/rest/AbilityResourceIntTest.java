package br.com.algar.web.rest;

import br.com.algar.PokeapiApp;
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

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the AbilityResource REST controller.
 *
 * @see AbilityResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PokeapiApp.class)
public class AbilityResourceIntTest {

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private PokeApi api;

    private MockMvc restAbilityMockMvc;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AbilityResource abilityResource = new AbilityResource(api);
        this.restAbilityMockMvc = MockMvcBuilders.standaloneSetup(abilityResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Test
    public void getAllAbiliies() throws Exception {
        // Get all the abilityList
        restAbilityMockMvc.perform(get("/api/abilities?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(1)))
            .andExpect(jsonPath("$.[*].name").value(hasItem("stench")));
    }

    @Test
    public void getAbilityById() throws Exception {
        // Get the Ability
        restAbilityMockMvc.perform(get("/api/abilities/{id}", 2))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(2))
            .andExpect(jsonPath("$.name").value("drizzle"));
    }

    @Test
    public void getAbilityByName() throws Exception {
        // Get the Ability
        restAbilityMockMvc.perform(get("/api/abilities/{id}", "drizzle"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(2))
            .andExpect(jsonPath("$.name").value("drizzle"));
    }

    @Test
    public void getNonExistingAbility() throws Exception {
        // Get the Ability
        restAbilityMockMvc.perform(get("/api/abilities/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

}
