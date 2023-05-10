package coms309.Users;
import coms309.Workout.Workout;
import java.util.HashMap;
import java.util.Date;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
@RestController
public class Athlete extends Person{
    private HashMap<String, Workout> workoutList;
    private String coachUserName;
    public Athlete()
    {
    }

    public Athlete(HashMap<String, Workout> workoutList, String emailAddress, String userName, String firstName,
                   String lastName, String password, String coachUserName)
    {
        this.setDateJoined();
        this.setEmailAddress(emailAddress);
        this.setUserName(userName);
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setPassword(password);
        this.coachUserName = coachUserName;
        this.workoutList = workoutList;
    }
    @GetMapping("/athlete/workout")
    public @ResponseBody HashMap<String, Workout> getWorkoutList() { return workoutList; }
    public void setWorkoutList(HashMap<String, Workout> workoutList) { this.workoutList = workoutList; }
    public void setCoachUserName(String coachUserName) {this.coachUserName = coachUserName; }
    public String getCoachUserName() { return coachUserName; }
}