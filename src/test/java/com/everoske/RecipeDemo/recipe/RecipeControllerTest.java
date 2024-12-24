package com.everoske.RecipeDemo.recipe;

import com.everoske.RecipeDemo.ingredients.IngredientRepository;
import com.everoske.RecipeDemo.steps.StepRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class RecipeControllerTest {

    RecipeController recipeController;

    List<Recipe> recipeList;

    @BeforeEach
    void setup(@Mock RecipeRepository recipeRepository) {
        recipeController = new RecipeController(recipeRepository);
        recipeList = new ArrayList<>();

        recipeList.add(new Recipe(1L, 1L,
                "Recipe 1", "My Recipe",
                LocalDateTime.of(2024, 11, 27, 8, 30),
                LocalDateTime.of(2024, 11, 27, 8, 30)));

        recipeList.add(new Recipe(2L, 2L,
                "Recipe 2", "My Recipe",
                LocalDateTime.of(2024, 11, 27, 8, 30),
                LocalDateTime.of(2024, 11, 27, 8, 30)));

        lenient().when(recipeRepository.fetchRecipes()).thenReturn(recipeList);
        lenient().when(recipeRepository.findRecipeById(any(Integer.class))).thenAnswer(new Answer<Optional<Recipe>>() {
            @Override
            public Optional<Recipe> answer(InvocationOnMock invocationOnMock) throws Throwable {
                Integer id = invocationOnMock.getArgument(0);
                return recipeList.stream().filter(recipe -> Objects.equals(recipe.getId(), Long.valueOf(id))).findFirst();
            }
        });
        lenient().doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocationOnMock) throws Throwable {
                Recipe recipe = invocationOnMock.getArgument(0);
                recipeList.add(recipe);
                return null;
            }
        }).when(recipeRepository).createRecipe(any(Recipe.class));

        lenient().doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocationOnMock) throws Throwable {
                Recipe updatedRecipe = invocationOnMock.getArgument(0);
                Integer id = invocationOnMock.getArgument(1);
                Optional<Recipe> optional = recipeList
                        .stream()
                        .filter(recipe -> Objects.equals(recipe.getId(), Long.valueOf(id)))
                        .findFirst();

                if (optional.isPresent()) {
                    int index = recipeList.indexOf(optional.get());
                    recipeList.set(index, updatedRecipe);
                }

                return null;
            }
        }).when(recipeRepository).updateRecipe(any(Recipe.class), any(Integer.class));

        lenient().doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocationOnMock) throws Throwable {
                Integer id = invocationOnMock.getArgument(0);
                Optional<Recipe> optional = recipeList
                        .stream()
                        .filter(recipe -> Objects.equals(recipe.getId(), Long.valueOf(id)))
                        .findFirst();
                optional.ifPresent(recipe -> recipeList.remove(recipe));
                return null;
            }
        }).when(recipeRepository).deleteRecipe(any(Integer.class));
    }

    @Test
    void shouldFindAllRecipes() {
        Assertions.assertEquals(2, recipeController.fetchAll().size());
    }

    @Test
    void shouldFindRecipeById() throws Exception {
        Assertions.assertEquals(recipeList.get(0), recipeController.findById(1));
    }

    @Test
    void shouldThrowExceptionIfRecipeDoesNotExist() {
        Assertions.assertThrows(Exception.class, () -> recipeController.findById(33));
    }

    @Test
    void shouldCreateRecipe() {
        Recipe newRecipe = new Recipe(3L, 1L,
                "Recipe 3", "My Recipe",
                LocalDateTime.of(2024, 11, 27, 8, 30),
                LocalDateTime.of(2024, 11, 27, 8, 30));
        recipeController.createRecipe(newRecipe);

        Assertions.assertEquals(3, recipeList.size());
        Assertions.assertEquals(newRecipe, recipeList.get(2));
    }

    @Test
    void shouldUpdateRecipe() {
        Recipe updatedRecipe = new Recipe(2L, 1L,
                "Best Recipe Ever", "My Best Recipe",
                LocalDateTime.of(2024, 11, 27, 8, 30),
                LocalDateTime.of(2024, 11, 27, 8, 30));

        recipeController.update(updatedRecipe, 2);
        Assertions.assertEquals(recipeList.get(1), updatedRecipe);
    }

    @Test
    void shouldDeleteRecipe() {
        recipeController.delete(1);
        Assertions.assertEquals(1, recipeList.size());
    }
}