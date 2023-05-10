package coms309.Exercises;

import com.fasterxml.jackson.annotation.JsonIgnore;
import coms309.Workout.Workout;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Exercise {

    @ApiModelProperty(notes = "Exercise ID", example="1", required = true)
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Id
    private int id;

    @ApiModelProperty(notes = "Exercise name", example="Deadlift", required = true)
    @Column(nullable = false)
    private String exerciseName;

    @ApiModelProperty(notes = "List of attached workouts", example="[{\"id\":48,\"sets\":4,\"reps\":12,\"duration\":\"2 min\",\"rest\":\"30 sec\",\"video\":null,\"}]", required = false)
    @OneToMany(mappedBy = "exercise", orphanRemoval = true)
    @JsonIgnore
    private List<Workout> workoutList;


    public Exercise()
    {
        workoutList = new ArrayList<>();
    }
    public Exercise(String exerciseName)
    {
        workoutList = new ArrayList<>();
        this.exerciseName = exerciseName;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getExerciseName() { return this.exerciseName; }
    public void setExerciseName(String exerciseName) { this.exerciseName = exerciseName; }
    public List<Workout> getWorkoutList() { return this.workoutList; }
    public void setWorkoutList(List<Workout> workoutList) { this.workoutList = workoutList; }
}
