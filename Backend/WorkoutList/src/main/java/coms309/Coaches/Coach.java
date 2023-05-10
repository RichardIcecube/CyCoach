package coms309.Coaches;

import com.fasterxml.jackson.annotation.JsonIgnore;
import coms309.Athletes.Athlete;
import coms309.Managers.Manager;
import coms309.Users.User;
import coms309.Workout.Workout;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
public class Coach {
    @ApiModelProperty(notes = "Coach ID", example = "1", required = true)
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Id
    private int id;

    @ApiModelProperty(notes = "Associated User", example = "{\"id\":1,\"firstName\":\"Zane\",\"lastName\":\"Eason\",\"emailAddress\":\"zseason@iastate.edu\",\"password\":\"123\",\"classType\":2}", required = true)
    @OneToOne
    @JoinColumn( name = "user_id")
    private User user;

    @ApiModelProperty(notes = "Athlete List", example = "[{\"id\":3,\"user\":{\"id\":6,\"firstName\":\"Thomas\",\"lastName\":\"Jefferson\",\"emailAddress\":\"number3@gmail.com\",\"password\":\"3\",\"classType\":1},\"workoutList\":[]},{\"id\":4,\"user\":{\"id\":7,\"firstName\":\"James\",\"lastName\":\"Madison\",\"emailAddress\":\"number4@gmail.com\",\"password\":\"4\",\"classType\":1},\"workoutList\":[]}]", required = false)
    @OneToMany(mappedBy = "coach")
    @JsonIgnore
    private List<Athlete> athleteList;

    @ApiModelProperty(notes = "Created Workout List", example = "[{\"id\":48,\"sets\":4,\"reps\":12,\"duration\":\"2 min\",\"rest\":\"30 sec\",\"video\":null,\"exercise\":{\"id\":37,\"exerciseName\":\"Deadlift\",\"hibernateLazyInitializer\":{}}}]", required = false)
    @OneToMany(mappedBy = "coach", orphanRemoval = true)
    @JsonIgnore
    private List<Workout> workoutList;

    @ApiModelProperty(notes = "Assigned Manager", example = "{\"id\":1,\"firstName\":\"Zane\",\"lastName\":\"Eason\",\"emailAddress\":\"zseason@iastate.edu\",\"password\":\"123\",\"classType\":3}", required = false)
    @ManyToOne
    @JoinColumn(name = "manager_id")
    @JsonIgnore
    private Manager manager;

    public Coach()
    {
        athleteList = new ArrayList<>();
    }
    public Coach(User user)
    {
        athleteList = new ArrayList<>();
        workoutList = new ArrayList<>();
        this.user = user;
    }

    public long getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public List<Athlete> getAthleteList() { return this.athleteList; }
    public void setAthleteList(List<Athlete> athleteList) { this.athleteList = athleteList; }
    public List<Workout> getWorkoutList() {return this.workoutList;}
    public void setWorkoutList(List<Workout> workoutList) { this.workoutList = workoutList;}
    public void addAthlete(Athlete athlete) { this.athleteList.add(athlete); }
    public void removeAthlete(Athlete athlete) { this.athleteList.remove(athlete); }
    public void setUser(User user) { this.user = user; }
    public User getUser() { return this.user; }
    public void setManager(Manager manager) { this.manager = manager; }
    public Manager getManager() {return this.manager;}
}
