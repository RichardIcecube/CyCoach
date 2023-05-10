package coms309.Workout;

import com.fasterxml.jackson.annotation.JsonIgnore;
import coms309.Athletes.Athlete;
import coms309.Coaches.Coach;
import coms309.Exercises.Exercise;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

/**
 * Provides the Definition/Structure for the people row
 *
 * @author Vivek Bengre
 */

@Entity
public class Workout {

    @ApiModelProperty(notes = "Workout ID", example="1", required=true)
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Id
    private int id;
    @ApiModelProperty(notes = "Workout Sets", example="5", required=true)
    private int sets;
    @ApiModelProperty(notes = "Workout Reps", example="10", required=true)
    private int reps;
    @ApiModelProperty(notes = "Workout Duration", example="1 min", required = true)
    private String duration;
    @ApiModelProperty(notes = "Workout Rest", example="20 seconds", required = true)
    private String rest;
    @ApiModelProperty(notes = "Workout Video URL", example="https://www.youtube.com/watch?v=dQw4w9WgXcQ", required = false)
    @Column(nullable = true)
    private String video = null;
    @ApiModelProperty(notes = "Workout Athlete", example="{\"id\":1,\"firstName\":\"Zane\",\"lastName\":\"Eason\",\"emailAddress\":\"zseason@iastate.edu\",\"password\":\"123\",\"classType\":1}", required = false)
    @ManyToOne
    @JoinColumn(
            name = "athlete_id"
    )
    @JsonIgnore
    private Athlete athlete;

    @ApiModelProperty(notes = "Workout Exercise", example="{\"id\":37,\"exerciseName\":\"Deadlift\",\"hibernateLazyInitializer\":{}}", required = true)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;

    @ApiModelProperty(notes = "Coach who created Workout", example = "{\"id\":1,\"user\":{\"id\":2,\"firstName\":\"Jack\",\"lastName\":\"Smith\",\"emailAddress\":\"jacksmith@gmail.com\",\"password\":\"baseball123\",\"classType\":2}}", required = true)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn( name = "coach_id")
    @JsonIgnore
    private Coach coach;

    public Workout()
    {
        
    }

    public Workout(int sets, int reps, String duration, String rest)
    {
        this.reps = reps;
        this.sets = sets;
        this.duration = duration;
        this.rest = rest;
        this.coach = null;
    }

    public int getId() { return this.id; }
    public void setId(int id) { this.id = id; }
    public String getVideo() { return this.video; }
    public void setVideo(String video) { this.video = video; }
    public int getReps() {
        return this.reps;
    }
    public void setReps(int reps) {
        this.reps = reps;
    }
    public int getSets() {
        return this.sets;
    }
    public void setSets(int sets) {
        this.sets = sets;
    }
    public String getDuration() {return this.duration; }
    public void setDuration(String duration) { this.duration = duration; }
    public String getRest() { return this.rest; }
    public void setRest(String rest) { this.rest = rest; }
    public Athlete getAthlete() { return this.athlete; }
    public void setAthlete(Athlete athlete) { this.athlete = athlete; }
    public Coach getCoach() {return this.coach;}
    public void setCoach(Coach coach) {this.coach = coach;}
    public Exercise getExercise() { return this.exercise; }
    public void setExercise(Exercise exercise) { this.exercise = exercise; }
}
