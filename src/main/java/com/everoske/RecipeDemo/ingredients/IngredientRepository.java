package com.everoske.RecipeDemo.ingredients;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class IngredientRepository {
    private final JdbcClient jdbcClient;

    public IngredientRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<Ingredient> fetchIngredientsByRecipeId(Integer recipeId) {
        log.info("Fetching all ingredients for Recipe with ID: {}", recipeId);
        return jdbcClient.sql("SELECT * FROM ingredients WHERE recipe_id = :id")
                .param("id", recipeId)
                .query(Ingredient.class)
                .list();
    }

    public Optional<Ingredient> fetchIngredientById(Integer id) {

        return jdbcClient.sql("SELECT id, recipe_id, name, measurement FROM ingredients WHERE id = :id")
                .param("id", id)
                .query(Ingredient.class)
                .optional();
    }

    public void createIngredient(Ingredient ingredient) {
        log.info("Adding Ingredient: Recipe ID: {}, Name: \"{}\", Measurement: \"{}\" to database",
                ingredient.getRecipeId(), ingredient.getName(), ingredient.getMeasurement());

        int created = jdbcClient
                .sql("INSERT INTO ingredients(recipe_id, name, measurement) VALUES(?, ?, ?)")
                .params(List.of(ingredient.getRecipeId(), ingredient.getName(), ingredient.getMeasurement()))
                .update();

        if (created)
    }

    public void updateIngredient(Ingredient ingredient, Integer id) {
        log.info("Updating Ingredient with ID: {} for Recipe with ID: {}", id, ingredient.getRecipeId());

        int updated = jdbcClient
                .sql("UPDATE ingredients SET recipe_id = ?, name = ?, measurement = ? WHERE id = ?")
                .params(List.of(ingredient.getRecipeId(), ingredient.getName(), ingredient.getMeasurement(), id))
                .update();
    }

    public void deleteIngredient(Integer id) {
        log.info("Deleting Ingredient with ID: {}", id);
        int deleted = jdbcClient.sql("DELETE FROM ingredients WHERE id = :id")
                .param("id", id)
                .update();
    }

    public void deleteIngredientsByRecipeId(Integer recipeId) {
        log.info("Deleting all ingredients that belong to Recipe with ID: {}", recipeId);

        int deleted = jdbcClient.sql("DELETE FROM ingredients WHERE recipe_id = :id")
                .param("id", recipeId)
                .update();
    }

    public int getIngredientCountForRecipe(Integer recipeId) {
        return jdbcClient.sql("SELECT * FROM ingredients WHERE recipe_id = :id")
                .param("id", recipeId)
                .query(Ingredient.class)
                .list().size();
    }
}
