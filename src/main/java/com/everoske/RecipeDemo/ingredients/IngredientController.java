package com.everoske.RecipeDemo.ingredients;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
public class IngredientController {
    private final IngredientRepository ingredientRepository;

    public IngredientController(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    @GetMapping("api/v1/recipes/{id}/ingredients")
    public List<Ingredient> getIngredientsByRecipeId(@PathVariable Integer id) {
        log.info("GET Request: \"api/v1/recipes/{}/ingredients\"", id);
        return ingredientRepository.fetchIngredientsByRecipeId(id);
    }

    @DeleteMapping("api/v1/recipes/{id}/ingredients")
    public void deleteIngredientsByRecipeId(@PathVariable Integer id) {
        log.info("DELETE Request: \"api/v1/recipes/{}/ingredients\"", id);
        ingredientRepository.deleteIngredientsByRecipeId(id);
    }

    @GetMapping("api/v1/ingredients/{id}")
    public Ingredient findIngredientById(@PathVariable Integer id) throws Exception {
        log.info("GET Request: \"api/v1/ingredients/{}\"", id);
        Optional<Ingredient> ingredient = ingredientRepository.fetchIngredientById(id);

        if (ingredient.isEmpty()) {
            log.error("Unable to fetch ingredient with id: {}", id);
            throw new Exception("Ingredient Not Found");
        }

        log.info("Ingredient object with id: {} fetched successfully", id);

        return ingredient.get();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("api/v1/ingredients")
    public void createIngredient(@Valid @RequestBody Ingredient ingredient) {
        log.info("POST Request: \"api/v1/ingredients\"");
        ingredientRepository.createIngredient(ingredient);
    }

    @PutMapping("api/v1/ingredients/{id}")
    public void update(@Valid @RequestBody Ingredient ingredient, @PathVariable Integer id) {
        log.info("PUT Request: \"api/v1/ingredients/{}\"", id);
        ingredientRepository.updateIngredient(ingredient, id);
    }

    @DeleteMapping("api/v1/ingredients/{id}")
    public void delete(@PathVariable Integer id) {
        log.info("Delete Request: \"api/v1/ingredients/{}\"", id);
        ingredientRepository.deleteIngredient(id);
    }
}
