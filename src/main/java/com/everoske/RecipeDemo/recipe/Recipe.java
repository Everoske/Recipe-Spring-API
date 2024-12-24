package com.everoske.RecipeDemo.recipe;

import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDateTime;

/**
 * Recipe Data Model.
 */
public class Recipe {

    private Long id;

    private Long userId;
    @NotEmpty
    private String name;
    private String description;
    private LocalDateTime creationDate;
    private LocalDateTime dateModified;

    public Recipe() {}

    public Recipe(Long userId, String name, String description) {
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.creationDate = LocalDateTime.now();
        this.dateModified = LocalDateTime.now();
    }

    public Recipe(Long userId,
                  String name, String description,
                  LocalDateTime creationDate, LocalDateTime dateModified) {
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.creationDate = creationDate;
        this.dateModified = dateModified;
    }

    public Recipe(Long id, Long userId,
                  String name, String description,
                  LocalDateTime creationDate, LocalDateTime dateModified) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.creationDate = creationDate;
        this.dateModified = dateModified;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getDateModified() {
        return dateModified;
    }

    public void setDateModified(LocalDateTime dateModified) {
        this.dateModified = dateModified;
    }
}
