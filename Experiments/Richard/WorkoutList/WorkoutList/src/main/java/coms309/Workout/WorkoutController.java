package coms309.Workout;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;


import java.util.HashMap;

/**
 * @author Richard Bach
 */

@RestController
public class WorkoutController {
    HashMap<String, Workout> workoutList = new  HashMap<>();
    @GetMapping("/workoutJSON")
    public @ResponseBody HashMap<String,Workout> getAllWorkouts() {
        return workoutList;
    }
    @GetMapping("/workout")
    public @ResponseBody String listAllWorkouts()
    {
        String s = "";
        for(String key : workoutList.keySet())
        {
            s += workoutList.get(key).toString() + "\n";
        }
        return s;
    }
    @PostMapping("/workout")
    public @ResponseBody String createWorkout(@RequestBody Workout workout) {
        System.out.println(workout);
        workoutList.put(workout.getExerciseName(), workout);
        return "New workout "+ workout.getExerciseName() + " Saved";
    }
    @GetMapping("/workout/{exerciseName}")
    public @ResponseBody Workout getWorkout(@PathVariable String exerciseName) {
        Workout w = workoutList.get(exerciseName);
        return w;
    }
    @PutMapping("/workout/{exerciseName}")
    public @ResponseBody Workout updateWorkout(@PathVariable String exerciseName, @RequestBody Workout w) {
        workoutList.replace(exerciseName, w);
        return workoutList.get(exerciseName);
    }
    @DeleteMapping("/workout/{exerciseName}")
    public @ResponseBody HashMap<String, Workout> deletePerson(@PathVariable String exerciseName) {
        workoutList.remove(exerciseName);
        return workoutList;
    }
}

