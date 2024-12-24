package com.everoske.RecipeDemo.recipe;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.Optional;

@Disabled
@JdbcTest
@Import(RecipeRepository.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RecipeRepositoryTest {
    @Autowired
    RecipeRepository repository;

    @BeforeEach
    void setUp() {
        repository.createRecipe(new Recipe( 1L,
                "Recipe 1", "My Recipe",
                LocalDateTime.of(2024, 11, 27, 8, 30),
                LocalDateTime.of(2024, 11, 27, 8, 30)));

        repository.createRecipe(new Recipe(1L,
                "Recipe 2", "My Recipe",
                LocalDateTime.of(2024, 11, 27, 8, 30),
                LocalDateTime.of(2024, 11, 27, 8, 30)));
    }

    @Test
    void shouldFindAllRecipes() {
        Assertions.assertEquals(2, repository.recipeCount());
    }

    @Test
    void shouldFindRecipeById() {
        Recipe recipe = repository.fetchRecipes().get(0);
        Optional<Recipe> optional = repository.findRecipeById(recipe.getId().intValue());

        Assertions.assertTrue(optional.isPresent());
        Assertions.assertEquals("Recipe 1", optional.get().getName());
    }

    @Test
    void shouldCreateRecipe() {
        Recipe recipe = new Recipe(1L,
                "Recipe 3", "My Recipe",
                LocalDateTime.of(2024, 11, 27, 8, 30),
                LocalDateTime.of(2024, 11, 27, 8, 30));

        int oldLatestId = repository.fetchRecipes().get(1).getId().intValue();
        int newKey = repository.createRecipe(recipe);

        Assertions.assertEquals(newKey, oldLatestId + 1);
        Assertions.assertEquals(3, repository.recipeCount());
    }

    @Test
    void shouldUpdateRecipe() {
        int oldRecipeId = repository.fetchRecipes().get(0).getId().intValue();

        Recipe newRecipe = new Recipe((long) oldRecipeId,
                "Best Recipe", "Best Recipe Ever",
                LocalDateTime.of(2024, 11, 27, 8, 30),
                LocalDateTime.of(2024, 11, 27, 8, 30));

        repository.updateRecipe(newRecipe, oldRecipeId);

        Optional<Recipe> updatedRecipe = repository.findRecipeById(oldRecipeId);

        Assertions.assertTrue(updatedRecipe.isPresent());
        Assertions.assertEquals(newRecipe.getName(), updatedRecipe.get().getName());
    }

    @Test
    void shouldDeleteRecipe() {
        int recipeId = repository.fetchRecipes().get(0).getId().intValue();
        repository.deleteRecipe(recipeId);
        Assertions.assertEquals(1, repository.recipeCount());
    }
}