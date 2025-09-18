package com.biblioteca.domain.entities.livro.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * DTO para resposta da API OpenLibrary
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenLibraryResponseDTO {

    @JsonProperty("title")
    private String title;

    @JsonProperty("authors")
    private List<Author> authors;

    @JsonProperty("publishers")
    private List<Publisher> publishers;

    @JsonProperty("publish_date")
    private String publishDate;

    @JsonProperty("number_of_pages")
    private Integer numberOfPages;

    @JsonProperty("isbn_10")
    private List<String> isbn10;

    @JsonProperty("isbn_13")
    private List<String> isbn13;

    @JsonProperty("subjects")
    private List<Subject> subjects;

    public String getTitle() {
        return title;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public List<Publisher> getPublishers() {
        return publishers;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public Integer getNumberOfPages() {
        return numberOfPages;
    }

    public List<String> getIsbn10() {
        return isbn10;
    }

    public List<String> getIsbn13() {
        return isbn13;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Author {
        @JsonProperty("name")
        private String name;

        public String getName() {
            return name;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Publisher {
        @JsonProperty("name")
        private String name;

        public String getName() {
            return name;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Subject {
        @JsonProperty("name")
        private String name;

        public String getName() {
            return name;
        }
    }
}
