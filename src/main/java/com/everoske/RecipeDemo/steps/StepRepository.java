package com.everoske.RecipeDemo.steps;

import com.everoske.RecipeDemo.ingredients.Ingredient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class StepRepository {
    private final JdbcClient jdbcClient;

    public StepRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<Step> fetchStepsByRecipeId(Integer recipeId) {
        log.info("Fetching all steps for Recipe with ID: {}", recipeId);

        return jdbcClient.sql("SELECT * FROM steps WHERE recipe_id = :id")
                .param("id", recipeId)
                .query(Step.class)
                .list();
    }

    public Optional<Step> fetchStepById(Integer id) {
        return jdbcClient.sql("SELECT id, recipe_id, step_number, description FROM steps WHERE id = :id")
                .param("id", id)
                .query(Step.class)
                .optional();
    }

    public void createStep(Step step) {
        log.info("Adding Step: Recipe ID: {}, Step Number: {}, Description: \"{}\" to database",
                step.getRecipeId(), step.getStepNumber(), step.getDescription());

        int created = jdbcClient
                .sql("INSERT INTO steps(recipe_id, step_number, description) VALUES(?, ?, ?)")
                .params(List.of(step.getRecipeId(), step.getStepNumber(), step.getDescription()))
                .update();
    }

    public void updateStep(Step step, Integer id) {
        log.info("Updating Step with ID: {} for Recipe with ID: {}", id, step.getRecipeId());

        int updated = jdbcClient
                .sql("UPDATE steps SET recipe_id = ?, step_number = ?, description = ? WHERE id = ?")
                .params(List.of(step.getRecipeId(), step.getStepNumber(), step.getDescription(), id))
                .update();
    }

    public void deleteStep(Integer id) {
        log.info("Deleting Step with ID: {}", id);

        int deleted = jdbcClient.sql("DELETE FROM steps WHERE id = :id")
                .param("id", id)
                .update();
    }

    public void deleteStepsByRecipeId(Integer recipeId) {
        log.info("Deleting all steps that belong to Recipe with ID: {}", recipeId);

        int deleted = jdbcClient.sql("DELETE FROM steps WHERE recipe_id = :id")
                .param("id", recipeId)
                .update();
    }

    public int getStepCountForRecipe(Integer recipeId) {
        return jdbcClient.sql("SELECT * FROM steps WHERE recipe_id = :id")
                .param("id", recipeId)
                .query(Step.class)
                .list().size();
    }
}
