package coms309.Athletes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import coms309.Coaches.Coach;
import coms309.Users.User;
import coms309.Workout.Workout;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

@Entity
public class Athlete {

    @ApiModelProperty(notes = "Athlete ID", example = "1", required = true)
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Id
    private int id;

    @ApiModelProperty(notes = "Associated User", example = "{\"id\":1,\"firstName\":\"Zane\",\"lastName\":\"Eason\",\"emailAddress\":\"zseason@iastate.edu\",\"password\":\"123\",\"classType\":1}", required = true)
    @OneToOne
    @JoinColumn( name = "user_id")
    private User user;

    @ApiModelProperty(notes = "Assigned Coach", example = "{\"id\":1,\"user\":{\"id\":2,\"firstName\":\"Jack\",\"lastName\":\"Smith\",\"emailAddress\":\"jacksmith@gmail.com\",\"password\":\"baseball123\",\"classType\":2}}", required = false)
    @ManyToOne
    @JoinColumn(name = "coach_id")
    @JsonIgnore
    private Coach coach;

    @ApiModelProperty(notes = "Workout List", example = "[{\"id\":48,\"sets\":4,\"reps\":12,\"duration\":\"2 min\",\"rest\":\"30 sec\",\"video\":null,\"exercise\":{\"id\":37,\"exerciseName\":\"Deadlift\",\"hibernateLazyInitializer\":{}}}]", required = false)
    @OneToMany
    @JoinColumn(name = "workout_id")
    private List<Workout> workoutList;
    public Athlete()
    {
        workoutList = new ArrayList<>();
    }
    public Athlete(User user) {
        workoutList = new ArrayList<>();
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public Coach getCoach() { return this.coach; }
    public void setCoach(Coach coach) { this.coach = coach; }
    public List<Workout> getWorkoutList() { return this.workoutList; }
    public void addWorkout(Workout workout) { this.workoutList.add(workout); }
    public void removeWorkout(Workout workout) { this.workoutList.remove(workout); }
    public User getUser() { return this.user; }
    public void setUser(User user) { this.user = user; }

}
