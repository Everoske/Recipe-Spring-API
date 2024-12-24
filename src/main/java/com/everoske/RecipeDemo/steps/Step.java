package com.everoske.RecipeDemo.steps;

import jakarta.validation.constraints.NotEmpty;

/**
 * Step Data Model.
 */
public class Step {
    private Long id;
    private Long recipeId;
    private Long stepNumber;
    @NotEmpty
    private String description;

    private Step() {
    }

    public Step(Long recipeId, Long stepNumber, String description) {
        this.recipeId = recipeId;
        this.stepNumber = stepNumber;
        this.description = description;
    }

    public Step(Long id, Long recipeId, Long stepNumber, String description) {
        this.id = id;
        this.recipeId = recipeId;
        this.stepNumber = stepNumber;
        this.description = description;
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

    public Long getStepNumber() {
        return stepNumber;
    }

    public void setStepNumber(Long stepNumber) {
        this.stepNumber = stepNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
