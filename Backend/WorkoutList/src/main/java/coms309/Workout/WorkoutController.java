package coms309.Workout;

import coms309.Coaches.Coach;
import coms309.Coaches.CoachRepository;
import coms309.Exercises.Exercise;
import coms309.Exercises.ExerciseRepository;
import coms309.Users.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

/**
 * @author Richard Bach
 */

@Api(value = "Workout Controller", description = "REST API related to Workout Entity", tags = "WorkoutController")
@RestController
public class WorkoutController {
    @Autowired
    WorkoutRepository workoutRepository;
    @Autowired
    CoachRepository coachRepository;
    @Autowired
    ExerciseRepository exerciseRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @ApiOperation(value = "Get a list of all saved Workouts in the database", response = Workout.class, tags = "listAllWorkouts")
    @GetMapping(path = "/workouts")
    List<Workout> listAllWorkouts()
    {
        return workoutRepository.findAll();
    }

    @ApiOperation(value = "Get a specific workout by id", response = Workout.class, tags="getWorkoutById")
    @GetMapping(path = "/workouts/{id}")
    Workout getWorkoutById(@PathVariable int id)
    {
        return workoutRepository.findById(id);
    }

    @ApiOperation(value = "Create a new workout", response = String.class, tags = "createWorkout")
    @PostMapping(path = "/workouts")
    String createWorkout(@RequestBody Workout workout)
    {
        if(workout == null)
        {
            return failure;
        }
        //What to do about nulls? They are not detected by this
        if(exerciseRepository.findByName(workout.getExercise().getExerciseName()) != null)
        {
            workout.setExercise(exerciseRepository.findByName((workout.getExercise().getExerciseName())));
        }
        else
        {
            exerciseRepository.save(workout.getExercise());
            workout.setExercise(workout.getExercise());
        }
        workoutRepository.save(workout);
        return success;
    }

    @ApiOperation(value = "Update a workout", response = Workout.class, tags = "updateWorkout")
    @PutMapping(path = "/workouts/{id}")
    Workout updateWorkout(@PathVariable int id, @RequestBody Workout request)
    {
        Workout workout = workoutRepository.findById(id);
        if(workout == null)
        {
            return null;
        }
        workout.setReps(request.getReps());
        workout.setRest(request.getRest());
        workout.setDuration(request.getDuration());
        workout.setSets(request.getSets());
        if(exerciseRepository.findByName(request.getExercise().getExerciseName()) != null)
        {
            workout.setExercise(exerciseRepository.findByName(request.getExercise().getExerciseName()));
        }
        else
        {
            exerciseRepository.save(request.getExercise());
            workout.setExercise(request.getExercise());
        }

        workoutRepository.save(workout);
        return workoutRepository.findById(id);
    }

    @ApiOperation(value = "Get a workouts exercise", response = Exercise.class, tags = "getWorkoutExercise")
    @GetMapping(path = "/workouts/{id}/exercises")
    Exercise getWorkoutExercise(@PathVariable int id)
    {
        Workout workout = workoutRepository.findById(id);
        if(workout == null)
        {
            return null;
        }
        return workout.getExercise();
    }

    @ApiOperation(value = "Get a workouts video", response = String.class, tags = "getWorkoutVideo")
    @GetMapping(path = "/workouts/{id}/video")
    String getWorkoutVideo(@PathVariable int id)
    {
        Workout workout = workoutRepository.findById(id);
        if(workout == null) return null;
        return workout.getVideo();
    }

    @ApiOperation(value = "Update a workouts video", response = Workout.class, tags = "updateWorkoutVideo")
    @PutMapping(path = "/workouts/{id}/video")
    Workout updateWorkoutVideo(@PathVariable int id, @RequestBody String videoUrl) {
        Workout workout = workoutRepository.findById(id);
        if(workout == null) return null;
        workout.setVideo(videoUrl);
        workoutRepository.save(workout);
        return workoutRepository.findById(id);
    }

    @ApiOperation(value = "Get a workouts coach", response = Coach.class, tags = "getCoach")
    @GetMapping(path = "/workouts/{id}/coach")
    Coach getCoach(@PathVariable int id){
        Workout workout = workoutRepository.findById(id);
        if(workout == null) return null;
        return workout.getCoach();
    }

    @ApiOperation(value = "Update a coach", response = String.class, tags = "updateCoach")
    @PutMapping(path = "/workouts/{id}/coach")
    String updateCoach(@PathVariable int id, @RequestBody Coach coach){
        Workout workout = workoutRepository.findById(id);
        if(workout == null) return failure;
        workout.setCoach(coach);
        workoutRepository.save(workout);
        return success;
    }

    @ApiOperation(value = "Update a coach by their id", response = String.class, tags = "updateCoachById")
    @PutMapping(path = "/workouts/{workoutId}/coach/{coachId}")
    String updateCoachById(@PathVariable int workoutId, @PathVariable int coachId){
        Workout workout = workoutRepository.findById(workoutId);
        Coach coach = coachRepository.findById(coachId);
        if(coach == null || workout == null) return failure;
        workout.setCoach(coach);
        workoutRepository.save(workout);
        return success;
    }

}

