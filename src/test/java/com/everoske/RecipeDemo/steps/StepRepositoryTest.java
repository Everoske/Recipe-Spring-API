package com.everoske.RecipeDemo.steps;

import com.everoske.RecipeDemo.recipe.Recipe;
import com.everoske.RecipeDemo.recipe.RecipeRepository;
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

import static org.junit.jupiter.api.Assertions.*;

@Disabled
@JdbcTest
@Import({StepRepository.class, RecipeRepository.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class StepRepositoryTest {

    @Autowired
    RecipeRepository recipeRepository;

    @Autowired
    StepRepository stepRepository;

    int recipeKey;


    @BeforeEach
    void setUp() {
        recipeKey = recipeRepository.createRecipe(new Recipe(1L, "Recipe 1",
                "My Recipe", LocalDateTime.now(), LocalDateTime.now()));

        stepRepository.createStep(new Step((long) recipeKey, 1L, "Step 1"));
        stepRepository.createStep(new Step((long) recipeKey, 2L, "Step 2"));
        stepRepository.createStep(new Step((long) recipeKey, 3L, "Step 3"));
    }

    @Test
    void shouldCreateStep() {
        stepRepository.createStep(new Step((long) recipeKey, 4L, "Step 4"));
        Assertions.assertEquals(4, stepRepository.getStepCountForRecipe(recipeKey));
    }

    @Test
    void shouldFindAllStepsByRecipe() {
        Assertions.assertEquals(3, stepRepository.getStepCountForRecipe(recipeKey));
    }

    @Test
    void shouldFindStepById() {
        List<Step> steps = stepRepository.fetchStepsByRecipeId(recipeKey);
        Step step = steps.get(0);
        Optional<Step> retrievedStep = stepRepository.fetchStepById(step.getId().intValue());
        Assertions.assertTrue(retrievedStep.isPresent());
        Assertions.assertEquals(step.getStepNumber(), retrievedStep.get().getStepNumber());
    }

    @Test
    void shouldUpdateStep() {
        List<Step> steps = stepRepository.fetchStepsByRecipeId(recipeKey);
        Step step = steps.get(0);
        Step newStep = new Step(step.getRecipeId(), 45L, "Step 5000");
        stepRepository.updateStep(newStep, step.getId().intValue());
        Optional<Step> retrievedStep = stepRepository.fetchStepById(step.getId().intValue());
        Assertions.assertTrue(retrievedStep.isPresent());
        Assertions.assertEquals(newStep.getStepNumber(), retrievedStep.get().getStepNumber());
        Assertions.assertEquals(newStep.getDescription(), retrievedStep.get().getDescription());
    }

    @Test
    void shouldDeleteStepById() {
        List<Step> steps = stepRepository.fetchStepsByRecipeId(recipeKey);
        Step step = steps.get(0);
        stepRepository.deleteStep(step.getId().intValue());
        Assertions.assertEquals(steps.size() - 1, stepRepository.getStepCountForRecipe(recipeKey));
    }

    @Test
    void shouldDeleteStepsByRecipeId() {
        stepRepository.deleteStepsByRecipeId(recipeKey);
        Assertions.assertEquals(0, stepRepository.getStepCountForRecipe(recipeKey));
    }
}