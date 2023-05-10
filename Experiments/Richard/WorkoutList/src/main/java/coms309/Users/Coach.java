package coms309.Users;
import coms309.Workout.Workout;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.HashMap;
import java.lang.String;
@RestController
public class Coach extends Person{
    private HashMap<String, Workout> workoutList = new HashMap<>();
    private HashMap<String, Athlete> athleteList;

    public Coach() {

    }
    public Coach(HashMap<String, Athlete> athleteList, String emailAddress, String userName, String firstName,
                 String lastName, String password)
    {
        this.athleteList = athleteList;
        this.setEmailAddress(emailAddress);
        this.setUserName(userName);
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setPassword(password);
    }
    @GetMapping("/coach/workoutJSON")
    public @ResponseBody HashMap<String, Workout> getAllWorkout() { return workoutList; }
    @GetMapping("/coach/athletes")
    public @ResponseBody HashMap<String, Athlete> getAllAthletes() {return athleteList; }
    @PostMapping("/coach/workout")
    public @ResponseBody String createWorkout(@RequestBody Workout workout)
    {
        System.out.println(workout.getExerciseName());
        workoutList.put(workout.getExerciseName(), workout);
        return "New workout added: " + workout.getExerciseName();
    }
    @GetMapping("/coach/workout/{exerciseName}")
    public @ResponseBody Workout getWorkout(@PathVariable String exerciseName)
    {
        Workout w = workoutList.get(exerciseName);
        return w;
    }
    @PutMapping("/coach/workout/{exerciseName}")
    public @ResponseBody HashMap<String, Workout> updateWorkout(@PathVariable String exerciseName, @RequestBody Workout workout)
    {
        workoutList.replace(exerciseName, workout);
        return workoutList;
    }
    @DeleteMapping("/coach/workout/{exerciseName}")
    public @ResponseBody HashMap<String, Workout> deleteWorkout(@PathVariable String exerciseName)
    {
        workoutList.remove(exerciseName);
        return workoutList;
    }

}
