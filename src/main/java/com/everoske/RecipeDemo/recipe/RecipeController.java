package com.everoske.RecipeDemo.recipe;

import com.everoske.RecipeDemo.ingredients.Ingredient;
import com.everoske.RecipeDemo.ingredients.IngredientController;
import com.everoske.RecipeDemo.ingredients.IngredientRepository;
import com.everoske.RecipeDemo.steps.Step;
import com.everoske.RecipeDemo.steps.StepRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aot.generate.Generated;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("api/v1/recipes")
public class RecipeController {
    private final RecipeRepository recipeRepository;

    public RecipeController(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @GetMapping("")
    public List<Recipe> fetchAll() {
        log.info("GET Request: \"api/v1/recipes\"");
        return recipeRepository.fetchRecipes();
    }

    @GetMapping("/{id}")
    public Recipe findById(@PathVariable Integer id) throws Exception {
        log.info("GET Request: \"api/v1/recipes/{}\"", id);
        Optional<Recipe> recipe = recipeRepository.findRecipeById(id);

        if (recipe.isEmpty()) {
            log.error("Unable to fetch recipe with id: {}", id);
            throw new Exception("Recipe Not Found");
        }

        log.info("Recipe object with id: {} fetched successfully", id);

        return recipe.get();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public int createRecipe(@Valid @RequestBody Recipe recipe) {
        log.info("POST Request: \"api/v1/recipes\"");
        return recipeRepository.createRecipe(recipe);
    }

    @PutMapping("/{id}")
    public void update(@Valid @RequestBody Recipe recipe, @PathVariable Integer id) {
        log.info("PUT Request: \"api/v1/recipes/{}\"", id);
        recipeRepository.updateRecipe(recipe, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        log.info("DELETE Request: \"api/v1/recipes/{}\"", id);
        recipeRepository.deleteRecipe(id);
    }
}
