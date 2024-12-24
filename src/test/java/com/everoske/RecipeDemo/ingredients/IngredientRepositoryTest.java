package com.everoske.RecipeDemo.ingredients;

import com.everoske.RecipeDemo.recipe.Recipe;
import com.everoske.RecipeDemo.recipe.RecipeRepository;
import com.everoske.RecipeDemo.steps.Step;
import com.everoske.RecipeDemo.steps.StepRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Disabled
@JdbcTest
@Import({IngredientRepository.class, RecipeRepository.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class IngredientRepositoryTest {
    @Autowired
    RecipeRepository recipeRepository;

    @Autowired
    IngredientRepository ingredientsRepository;

    int recipeKey;


    @BeforeEach
    void setUp() {
        recipeKey = recipeRepository.createRecipe(new Recipe(1L, "Recipe 1",
                "My Recipe", LocalDateTime.now(), LocalDateTime.now()));

        ingredientsRepository.createIngredient(new Ingredient((long) recipeKey, "Ingredient 1", "1 Cup"));
        ingredientsRepository.createIngredient(new Ingredient((long) recipeKey, "Ingredient 2", "1 Cup"));
        ingredientsRepository.createIngredient(new Ingredient((long) recipeKey, "Ingredient 3", "1 Cup"));
    }

    @Test
    void shouldCreateStep() {
        ingredientsRepository.createIngredient(new Ingredient((long) recipeKey, "Ingredient 4", "1 Cup"));
        Assertions.assertEquals(4, ingredientsRepository.getIngredientCountForRecipe(recipeKey));
    }

    @Test
    void shouldFindAllStepsByRecipe() {
        Assertions.assertEquals(3, ingredientsRepository.getIngredientCountForRecipe(recipeKey));
    }

    @Test
    void shouldFindStepById() {
        List<Ingredient> ingredients = ingredientsRepository.fetchIngredientsByRecipeId(recipeKey);
        Ingredient ingredient = ingredients.get(0);
        Optional<Ingredient> retrievedIngredient = ingredientsRepository.fetchIngredientById(ingredient.getId().intValue());
        Assertions.assertTrue(retrievedIngredient.isPresent());
        Assertions.assertEquals(ingredient.getName(), retrievedIngredient.get().getName());
    }

    @Test
    void shouldUpdateStep() {
        List<Ingredient> ingredients = ingredientsRepository.fetchIngredientsByRecipeId(recipeKey);
        Ingredient ingredient = ingredients.get(0);
        Ingredient newIngredient = new Ingredient((long) recipeKey, "Super Ingredient", "10 Cups");
        ingredientsRepository.updateIngredient(newIngredient, ingredient.getId().intValue());
        Optional<Ingredient> retrievedIngredient = ingredientsRepository.fetchIngredientById(ingredient.getId().intValue());
        Assertions.assertTrue(retrievedIngredient.isPresent());
        Assertions.assertEquals(newIngredient.getName(), retrievedIngredient.get().getName());
        Assertions.assertEquals(newIngredient.getMeasurement(), retrievedIngredient.get().getMeasurement());
    }

    @Test
    void shouldDeleteStepById() {
        List<Ingredient> ingredients = ingredientsRepository.fetchIngredientsByRecipeId(recipeKey);
        Ingredient ingredient = ingredients.get(0);
        ingredientsRepository.deleteIngredient(ingredient.getId().intValue());
        Assertions.assertEquals(ingredients.size() - 1, ingredientsRepository.getIngredientCountForRecipe(recipeKey));
    }

    @Test
    void shouldDeleteStepsByRecipeId() {
        ingredientsRepository.deleteIngredientsByRecipeId(recipeKey);
        Assertions.assertEquals(0, ingredientsRepository.getIngredientCountForRecipe(recipeKey));
    }
}