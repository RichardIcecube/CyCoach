package coms309.Exercises;

import coms309.Workout.WorkoutRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Api(value = "Exercise Controller", description = "REST API related to Exercise Entity", tags="ExerciseController")
@RestController
public class ExerciseController {

    @Autowired
    ExerciseRepository exerciseRepository;

    @Autowired
    WorkoutRepository workoutRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @ApiOperation(value = "Get a list of all exercises", response = Exercise.class, tags = "listAllExercises")
    @GetMapping( path = "/exercises")
    List<Exercise> listAllExercises() { return exerciseRepository.findAll();}

    @ApiOperation(value = "Get an exercise by id", response = Exercise.class, tags="getExerciseById")
    @GetMapping( path = "/exercises/{id}")
    Exercise getExerciseById(@PathVariable int id) { return exerciseRepository.findById(id); }

    @ApiOperation(value = "Create a new exercise", response = String.class, tags="createExercise")
    @PostMapping(path = "/exercises")
    String createExercise(@RequestBody Exercise exercise)
    {
        if(exercise == null)
        {
            return failure;
        }
        if(exerciseRepository.findByName(exercise.getExerciseName()) != null)
        {
            return failure + "\n That exercise may already exist!";
        }
        exerciseRepository.save(exercise);
        return success;
    }

    @ApiOperation(value = "Update an exercise", response = Exercise.class, tags="updateExercise")
    @PutMapping(path = "/exercises/{id}")
    Exercise updateExercise(@PathVariable int id, @RequestBody Exercise request)
    {
        Exercise exercise = exerciseRepository.findById(id);
        if(exercise == null)
        {
            return null;
        }

        if(exerciseRepository.findByName(request.getExerciseName()) != null)
        {
            return null;
        }
        else
        {
            exercise.setExerciseName(request.getExerciseName());
        }
        exerciseRepository.save(exercise);
        return exerciseRepository.findById(id);
    }

    @ApiOperation(value = "Delete an exercise", response = String.class, tags="deleteExercise")
    @DeleteMapping(path = {"/exercises/{id}"})
    String deleteExercise(@PathVariable int id)
    {
        this.exerciseRepository.deleteById(id);
        return success;
    }

}
