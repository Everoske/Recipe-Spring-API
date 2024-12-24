package com.everoske.RecipeDemo.ingredients;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class IngredientControllerTest {
    IngredientController ingredientController;

    List<Ingredient> ingredientList;

    @BeforeEach
    void setUp(@Mock IngredientRepository ingredientRepository) {
        ingredientController = new IngredientController(ingredientRepository);
        ingredientList = new ArrayList<>();

        ingredientList.add(new Ingredient(1L, 1L, "R1: Ingredient 1", "1 Cup"));
        ingredientList.add(new Ingredient(2L, 1L, "R1: Ingredient 2", "1 Cup"));
        ingredientList.add(new Ingredient(3L, 2L, "R2: Ingredient 1", "1 Cup"));
        ingredientList.add(new Ingredient(4L, 2L, "R2: Ingredient 2", "1 Cup"));
        ingredientList.add(new Ingredient(5L, 2L, "R2: Ingredient 3", "1 Cup"));

        lenient().when(ingredientRepository.fetchIngredientsByRecipeId(any(Integer.class)))
                        .thenAnswer(new Answer<List<Ingredient>>() {
                            @Override
                            public List<Ingredient> answer(InvocationOnMock invocationOnMock) throws Throwable {
                                Integer recipeId = invocationOnMock.getArgument(0);
                                return ingredientList
                                        .stream()
                                        .filter(ingredient -> Objects.equals(ingredient.getRecipeId(), Long.valueOf(recipeId)))
                                        .toList();
                            }
                        });


        lenient().when(ingredientRepository.fetchIngredientById(any(Integer.class))).thenAnswer(new Answer<Optional<Ingredient>>() {
            @Override
            public Optional<Ingredient> answer(InvocationOnMock invocationOnMock) throws Throwable {
                Integer id = invocationOnMock.getArgument(0);
                return ingredientList
                        .stream()
                        .filter(ingredient -> Objects.equals(ingredient.getRecipeId(), Long.valueOf(id)))
                        .findFirst();
            }
        });
        lenient().doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocationOnMock) throws Throwable {
                Ingredient ingredient = invocationOnMock.getArgument(0);
                ingredientList.add(ingredient);
                return null;
            }
        }).when(ingredientRepository).createIngredient(any(Ingredient.class));

        lenient().doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocationOnMock) throws Throwable {
                Ingredient updatedIngredient = invocationOnMock.getArgument(0);
                Integer id = invocationOnMock.getArgument(1);
                Optional<Ingredient> optional = ingredientList
                        .stream()
                        .filter(ingredient -> Objects.equals(ingredient.getId(), Long.valueOf(id)))
                        .findFirst();

                if (optional.isPresent()) {
                    int index = ingredientList.indexOf(optional.get());
                    ingredientList.set(index, updatedIngredient);
                }

                return null;
            }
        }).when(ingredientRepository).updateIngredient(any(Ingredient.class), any(Integer.class));

        lenient().doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocationOnMock) throws Throwable {
                Integer id = invocationOnMock.getArgument(0);
                Optional<Ingredient> optional = ingredientList
                        .stream()
                        .filter(ingredient -> Objects.equals(ingredient.getId(), Long.valueOf(id)))
                        .findFirst();
                optional.ifPresent(ingredient -> ingredientList.remove(ingredient));
                return null;
            }
        }).when(ingredientRepository).deleteIngredient(any(Integer.class));

        lenient().doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocationOnMock) throws Throwable {
                Integer recipeId = invocationOnMock.getArgument(0);
                ingredientList.removeIf(ingredient -> Objects.equals(ingredient.getRecipeId(), Long.valueOf(recipeId)));
                return null;
            }
        }).when(ingredientRepository).deleteIngredientsByRecipeId(any(Integer.class));
    }

    @Test
    void shouldFindAllIngredientsByRecipeId() {
        Assertions.assertEquals(2, ingredientController.getIngredientsByRecipeId(1).size());
        Assertions.assertEquals(3, ingredientController.getIngredientsByRecipeId(2).size());
    }

    @Test
    void shouldFindIngredientById() throws Exception {
        Assertions.assertEquals(ingredientList.get(0), ingredientController.findIngredientById(1));
    }

    @Test
    void shouldThrowExceptionIfIngredientDoesNotExist() {
        Assertions.assertThrows(Exception.class, () -> ingredientController.findIngredientById(33));
    }

    @Test
    void shouldCreateIngredient() {
        Ingredient newIngredient = new Ingredient(6L, 3L, "R3: Ingredient 1", "1 Cup");
        ingredientController.createIngredient(newIngredient);

        Assertions.assertEquals(6, ingredientList.size());
        Assertions.assertEquals(newIngredient, ingredientList.get(5));
    }

    @Test
    void shouldUpdateIngredient() {
        Ingredient updatedIngredient = new Ingredient(1L, 1L, "R1: Best Ingredient", "1 Cup");

        ingredientController.update(updatedIngredient, 1);
        Assertions.assertEquals(ingredientList.get(0), updatedIngredient);
    }

    @Test
    void shouldDeleteIngredientById() {
        ingredientController.delete(1);
        Assertions.assertEquals(4, ingredientList.size());
    }

    @Test
    void shouldDeleteIngredientsByRecipeId() {
        ingredientController.deleteIngredientsByRecipeId(2);
        Assertions.assertEquals(2, ingredientList.size());
    }
}