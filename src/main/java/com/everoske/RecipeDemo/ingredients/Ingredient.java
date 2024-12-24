package com.everoske.RecipeDemo.ingredients;

import jakarta.validation.constraints.NotEmpty;

/**
 * Ingredient Data Model.
 */
public class Ingredient {
    private Long id;

    private Long recipeId;
    @NotEmpty
    private String name;
    @NotEmpty
    private String measurement;

    private Ingredient() {}

    public Ingredient(Long recipeId, String name, String measurement) {
        this.recipeId = recipeId;
        this.name = name;
        this.measurement = measurement;
    }

    public Ingredient(Long id, Long recipeId, String name, String measurement) {
        this.id = id;
        this.recipeId = recipeId;
        this.name = name;
        this.measurement = measurement;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMeasurement() {
        return measurement;
    }

    public void setMeasurement(String measurement) {
        this.measurement = measurement;
    }
}
