package com.biblioteca.domain.entities.livro.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO para dados do autor da API OpenLibrary
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenLibraryAuthorDTO {

    @JsonProperty("name")
    private String name;

    @JsonProperty("personal_name")
    private String personalName;

    @JsonProperty("birth_date")
    private String birthDate;

    @JsonProperty("death_date")
    private String deathDate;

    public OpenLibraryAuthorDTO() {
    }

    public String getName() {
        return name != null ? name : personalName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPersonalName() {
        return personalName;
    }

    public void setPersonalName(String personalName) {
        this.personalName = personalName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getDeathDate() {
        return deathDate;
    }

    public void setDeathDate(String deathDate) {
        this.deathDate = deathDate;
    }
}
