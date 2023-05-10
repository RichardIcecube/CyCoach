package coms309.Workout;


/**
 * Provides the Definition/Structure for the people row
 *
 * @author Vivek Bengre
 */

public class Workout {

    private String exerciseName;

    private int reps;

    private int sets;

    public Workout()
    {
        
    }

    public Workout(String exerciseName, int reps, int sets)
    {
        this.exerciseName = exerciseName;
        this.reps = reps;
        this.sets = sets;
    }

    public String getExerciseName() {
        return this.exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

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

    @Override
    public String toString()
    {
        return exerciseName + " "
               + sets + " sets of "
               + reps;
    }
}
