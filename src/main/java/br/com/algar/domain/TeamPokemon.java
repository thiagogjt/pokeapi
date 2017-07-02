package br.com.algar.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A TeamPokemon.
 */
@Entity
@Table(name = "team_pokemon")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TeamPokemon implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pokemon_id")
    private String pokemonId;

    @Column(name = "pokemon_name")
    private String pokemonName;

    @ManyToOne
    @JoinColumn(name = "team_id")
    @JsonIgnore
    private Team team;

    @OneToMany(mappedBy = "teamPokemon", fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @Getter
    @Builder.Default
    private Set<TeamPokemonAbility> abilities = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPokemonId() {
        return pokemonId;
    }

    public TeamPokemon pokemonId(String pokemonId) {
        this.pokemonId = pokemonId;
        return this;
    }

    public void setPokemonId(String pokemonId) {
        this.pokemonId = pokemonId;
    }

    public String getPokemonName() {
        return pokemonName;
    }

    public TeamPokemon pokemonName(String pokemonName) {
        this.pokemonName = pokemonName;
        return this;
    }

    public void setPokemonName(String pokemonName) {
        this.pokemonName = pokemonName;
    }

    public Team getTeam() {
        return team;
    }

    public TeamPokemon team(Team team) {
        this.team = team;
        return this;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Set<TeamPokemonAbility> getAbilities() {
        return abilities;
    }

    public TeamPokemon abilities(Set<TeamPokemonAbility> teamPokemonAbility) {
        this.abilities = teamPokemonAbility;
        return this;
    }

    public TeamPokemon addAbility(TeamPokemonAbility teamPokemonAbility) {
        this.abilities.add(teamPokemonAbility);
        teamPokemonAbility.setTeamPokemon(this);
        return this;
    }

    public TeamPokemon removeAbility(TeamPokemonAbility teamPokemonAbility) {
        this.abilities.remove(teamPokemonAbility);
        teamPokemonAbility.setTeamPokemon(null);
        return this;
    }

    public void setAbilities(Set<TeamPokemonAbility> teamPokemonAbilities) {
        teamPokemonAbilities.forEach(teamPokemonAbility -> teamPokemonAbility.setTeamPokemon(this));
        this.abilities = teamPokemonAbilities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TeamPokemon teamPokemon = (TeamPokemon) o;
        if (teamPokemon.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), teamPokemon.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TeamPokemon{" +
            "id=" + getId() +
            ", pokemonId='" + getPokemonId() + "'" +
            ", pokemonName='" + getPokemonName() + "'" +
            "}";
    }
}
