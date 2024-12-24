package com.everoske.RecipeDemo.steps;

import com.everoske.RecipeDemo.ingredients.Ingredient;
import com.everoske.RecipeDemo.ingredients.IngredientController;
import com.everoske.RecipeDemo.ingredients.IngredientRepository;
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
class StepControllerTest {
    StepController stepController;

    List<Step> stepList;

    @BeforeEach
    void setUp(@Mock StepRepository stepRepository) {
        stepController = new StepController(stepRepository);
        stepList = new ArrayList<>();

        stepList.add(new Step(1L, 1L, 1L, "R1: Step 1"));
        stepList.add(new Step(2L, 1L, 2L, "R1: Step 2"));
        stepList.add(new Step(3L, 2L, 1L, "R2: Step 1"));
        stepList.add(new Step(4L, 2L, 2L, "R2: Step 2"));
        stepList.add(new Step(5L, 2L, 3L, "R2: Step 3"));

        lenient().when(stepRepository.fetchStepsByRecipeId(any(Integer.class)))
                .thenAnswer(new Answer<List<Step>>() {
                    @Override
                    public List<Step> answer(InvocationOnMock invocationOnMock) throws Throwable {
                        Integer recipeId = invocationOnMock.getArgument(0);
                        return stepList
                                .stream()
                                .filter(step -> Objects.equals(step.getRecipeId(), Long.valueOf(recipeId)))
                                .toList();
                    }
                });


        lenient().when(stepRepository.fetchStepById(any(Integer.class))).thenAnswer(new Answer<Optional<Step>>() {
            @Override
            public Optional<Step> answer(InvocationOnMock invocationOnMock) throws Throwable {
                Integer id = invocationOnMock.getArgument(0);
                return stepList
                        .stream()
                        .filter(step -> Objects.equals(step.getRecipeId(), Long.valueOf(id)))
                        .findFirst();
            }
        });
        lenient().doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocationOnMock) throws Throwable {
                Step step = invocationOnMock.getArgument(0);
                stepList.add(step);
                return null;
            }
        }).when(stepRepository).createStep(any(Step.class));

        lenient().doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocationOnMock) throws Throwable {
                Step updatedStep = invocationOnMock.getArgument(0);
                Integer id = invocationOnMock.getArgument(1);
                Optional<Step> optional = stepList
                        .stream()
                        .filter(step -> Objects.equals(step.getId(), Long.valueOf(id)))
                        .findFirst();

                if (optional.isPresent()) {
                    int index = stepList.indexOf(optional.get());
                    stepList.set(index, updatedStep);
                }

                return null;
            }
        }).when(stepRepository).updateStep(any(Step.class), any(Integer.class));

        lenient().doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocationOnMock) throws Throwable {
                Integer id = invocationOnMock.getArgument(0);
                Optional<Step> optional = stepList
                        .stream()
                        .filter(step -> Objects.equals(step.getId(), Long.valueOf(id)))
                        .findFirst();
                optional.ifPresent(step -> stepList.remove(step));
                return null;
            }
        }).when(stepRepository).deleteStep(any(Integer.class));

        lenient().doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocationOnMock) throws Throwable {
                Integer recipeId = invocationOnMock.getArgument(0);
                stepList.removeIf(step -> Objects.equals(step.getRecipeId(), Long.valueOf(recipeId)));
                return null;
            }
        }).when(stepRepository).deleteStepsByRecipeId(any(Integer.class));
    }

    @Test
    void shouldFindAllStepsByRecipeId() {
        Assertions.assertEquals(2, stepController.getStepsByRecipeId(1).size());
        Assertions.assertEquals(3, stepController.getStepsByRecipeId(2).size());
    }

    @Test
    void shouldFindStepById() throws Exception {
        Assertions.assertEquals(stepList.get(0), stepController.findStepById(1));
    }

    @Test
    void shouldThrowExceptionIfStepDoesNotExist() {
        Assertions.assertThrows(Exception.class, () -> stepController.findStepById(33));
    }

    @Test
    void shouldCreateStep() {
        Step newStep = new Step(6L, 3L, 1L, "R3: Step 1");
        stepController.createStep(newStep);

        Assertions.assertEquals(6, stepList.size());
        Assertions.assertEquals(newStep, stepList.get(5));
    }

    @Test
    void shouldUpdateStep() {
        Step updatedStep = new Step(1L, 1L, 1L, "R1: Most Important Step");

        stepController.update(updatedStep, 1);
        Assertions.assertEquals(stepList.get(0), updatedStep);
    }

    @Test
    void shouldDeleteStepById() {
        stepController.delete(1);
        Assertions.assertEquals(4, stepList.size());
    }

    @Test
    void shouldDeleteStepsByRecipeId() {
        stepController.deleteStepsByRecipeId(2);
        Assertions.assertEquals(2, stepList.size());
    }
}