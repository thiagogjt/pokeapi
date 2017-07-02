package br.com.algar.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A TeamPokemonAbilities.
 */
@Entity
@Table(name = "team_pokemon_ability")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TeamPokemonAbility implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ability_id")
    private String abilityId;

    @Column(name = "ability_name")
    private String abilityName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    private TeamPokemon teamPokemon;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAbilityId() {
        return abilityId;
    }

    public TeamPokemonAbility abilityId(String abilityId) {
        this.abilityId = abilityId;
        return this;
    }

    public void setAbilityId(String abilityId) {
        this.abilityId = abilityId;
    }

    public String getAbilityName() {
        return abilityName;
    }

    public TeamPokemonAbility abilityName(String abilityName) {
        this.abilityName = abilityName;
        return this;
    }

    public void setAbilityName(String abilityName) {
        this.abilityName = abilityName;
    }

    public TeamPokemon getTeamPokemon() {
        return teamPokemon;
    }

    public TeamPokemonAbility teamPokemon(TeamPokemon teamPokemon) {
        this.teamPokemon = teamPokemon;
        return this;
    }

    public void setTeamPokemon(TeamPokemon teamPokemon) {
        this.teamPokemon = teamPokemon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TeamPokemonAbility teamPokemonAbilities = (TeamPokemonAbility) o;
        if (teamPokemonAbilities.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), teamPokemonAbilities.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TeamPokemonAbilities{" +
            "id=" + getId() +
            ", abilityId='" + getAbilityId() + "'" +
            ", abilityName='" + getAbilityName() + "'" +
            "}";
    }
}
