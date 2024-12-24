package com.everoske.RecipeDemo.steps;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
public class StepController {
    private final StepRepository stepRepository;

    public StepController(StepRepository stepRepository) {
        this.stepRepository = stepRepository;
    }

    @GetMapping("api/v1/recipes/{id}/steps")
    public List<Step> getStepsByRecipeId(@PathVariable Integer id) {
        log.info("GET Request: \"api/v1/recipes/{}/steps\"", id);
        return stepRepository.fetchStepsByRecipeId(id);
    }

    @DeleteMapping("api/v1/recipes/{id}/steps")
    public void deleteStepsByRecipeId(@PathVariable Integer id) {
        log.info("DELETE Request: \"api/v1/recipes/{}/steps\"", id);
        stepRepository.deleteStepsByRecipeId(id);
    }

    @GetMapping("api/v1/steps/{id}")
    public Step findStepById(@PathVariable Integer id) throws Exception {
        log.info("GET Request: \"api/v1/steps/{}\"", id);

        Optional<Step> step = stepRepository.fetchStepById(id);

        if (step.isEmpty()) {
            log.error("Unable to fetch step with id: {}", id);
            throw new Exception("Step Not Found");
        }

        log.info("Step object with id: {} fetched successfully", id);
        return step.get();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("api/v1/steps")
    public void createStep(@Valid @RequestBody Step step) {
        log.info("POST Request: \"api/v1/steps\"");
        stepRepository.createStep(step);
    }

    @PutMapping("api/v1/steps/{id}")
    public void update(@Valid @RequestBody Step step, @PathVariable Integer id) {
        log.info("PUT Request: \"api/v1/steps/{}\"", id);
        stepRepository.updateStep(step, id);
    }

    @DeleteMapping("api/v1/steps/{id}")
    public void delete(@PathVariable Integer id) {
        log.info("Delete Request: \"api/v1/steps/{}\"", id);
        stepRepository.deleteStep(id);
    }
}
