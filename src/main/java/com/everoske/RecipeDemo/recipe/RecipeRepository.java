package com.everoske.RecipeDemo.recipe;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.security.Key;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class RecipeRepository {
    private final JdbcClient jdbcClient;

    public RecipeRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<Recipe> fetchRecipes() {
        log.debug("Fetching all Recipes from database");
        return jdbcClient.sql("SELECT * FROM recipes")
                .query(Recipe.class)
                .list();
    }

    public Optional<Recipe> findRecipeById(Integer id) {
        log.debug("Fetching Recipe with ID: {} from database", id);
        return jdbcClient
                .sql("SELECT id, user_id, name, description, creation_date, date_modified FROM recipes WHERE id = :id")
                .param("id", id)
                .query(Recipe.class)
                .optional();
    }

    public int createRecipe(Recipe recipe) {
        log.info("Adding Recipe: User ID: {}, Name: \"{}\", Description: \"{}\" to database",
                recipe.getUserId(), recipe.getName(), recipe.getDescription());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int created = jdbcClient
                .sql("INSERT INTO recipes(user_id, name, description, creation_date, date_modified) VALUES(?, ?, ?, ?, ?)")
                .params(List.of(recipe.getUserId(), recipe.getName(), recipe.getDescription(), recipe.getCreationDate(), recipe.getDateModified()))
                .update(keyHolder);

        log.debug("Recipe created with ID: {}", keyHolder.getKey().intValue());

        return keyHolder.getKey().intValue();
    }

    public void updateRecipe(Recipe recipe, Integer id) {
        log.debug("Updating Recipe with ID: {}", id);
        int updated = jdbcClient
                .sql("UPDATE recipes SET user_id = ?, name = ?, description = ?, creation_date = ?, date_modified = ? WHERE id = ?")
                .params(List.of(recipe.getUserId(), recipe.getName(), recipe.getDescription(), recipe.getCreationDate(), recipe.getDateModified(), id))
                .update();
    }

    public void deleteRecipe(Integer id) {
        log.debug("Deleting Recipe with ID: {}", id);
        int deleted = jdbcClient.sql("DELETE FROM recipes WHERE id = :id")
                .param("id", id)
                .update();
    }

    public int recipeCount() {
        return jdbcClient.sql("SELECT * FROM recipes").query().listOfRows().size();
    }
}
