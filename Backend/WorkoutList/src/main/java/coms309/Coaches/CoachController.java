package coms309.Coaches;

import coms309.Athletes.Athlete;
import coms309.Athletes.AthleteRepository;
import coms309.Exercises.ExerciseRepository;
import coms309.Exercises.Exercise;
import coms309.Managers.Manager;
import coms309.Workout.Workout;
import coms309.Workout.WorkoutRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@Api(value = "Coach Controller", description = "REST API related to Coach Entity", tags="CoachController")
@RestController
public class CoachController {

    @Autowired
    CoachRepository coachRepository;

    @Autowired
    AthleteRepository athleteRepository;

    @Autowired
    WorkoutRepository workoutRepository;
    @Autowired
    ExerciseRepository exerciseRepository;
    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @ApiOperation(value = "Get individual Coaches by their Coach ID", response = Coach.class, tags = "getCoachById")
    @GetMapping(path = {"/coaches/{coachId}"})
    public Coach getCoachById(@PathVariable int coachId) { return this.coachRepository.findById(coachId); }
    @ApiOperation(value = "Get a Coach's list of assigned Athletes", response = Athlete.class, tags = "getAthleteList")
    @GetMapping(path = {"/coaches/{coachId}/athletes"})
    public List<Athlete> getAthleteList(@PathVariable int coachId) {
        Coach coach = this.coachRepository.findById(coachId);
        if(coach == null) {
            return null;
        }
        return coach.getAthleteList();
    }
    @ApiOperation(value = "Get the list of Workouts created by a Coach", response = Workout.class, tags = "getWorkoutList")
    @GetMapping(path = {"/coaches/{coachId}/workouts"})
    public List<Workout> getWorkoutList(@PathVariable int coachId) {
        Coach coach = this.coachRepository.findById(coachId);
        if(coach == null) {
            return null;
        }
        return coach.getWorkoutList();
    }
    @ApiOperation(value = "Get a Workout saved in the database by its Workout ID", response = Workout.class, tags = "getWorkoutById")
    @GetMapping(path = {"/coaches/{coachId}/workouts/{workoutId}"})
    public Workout getWorkoutById(@PathVariable int coachId, @PathVariable int workoutId) {
        Coach coach = this.coachRepository.findById(coachId);
        Workout workout = this.workoutRepository.findById(workoutId);
        if(coach == null || workout == null || workout.getCoach() != coach) {
            return null;
        }
        return workout;
    }
    @ApiOperation(value = "Get the list of all saved Exercises in the database", response = Exercise.class, tags = "getExerciseList")
    @GetMapping(path = {"/coaches/{coachId}/exercises"})
    public List<Exercise> getExerciseList(@PathVariable int coachId) {
        Coach coach = this.coachRepository.findById(coachId);
        if(coach == null) {
            return null;
        }
        return exerciseRepository.findAll();
    }
    @ApiOperation(value = "Get an individual Exercise saved in the database by its Exercise ID", response = Exercise.class, tags = "getExerciseById")
    @GetMapping(path = {"/coaches/{coachId}/exercises/{exerciseId}"})
    public Exercise getExerciseById(@PathVariable int coachId, @PathVariable int exerciseId){
        Coach coach = this.coachRepository.findById(coachId);
        if(coach == null){
            return null;
        }
        return exerciseRepository.findById(exerciseId);
    }
    @ApiOperation(value = "Get the Workout List of an Athlete present in the Coach's Athlete List", response = Workout.class, tags = "getAthleteWorkoutList")
    @GetMapping(path = {"/coaches/{coachId}/athletes/{athleteId}/workouts"})
    public List<Workout> getAthleteWorkoutList(@PathVariable int coachId, @PathVariable int athleteId) {
        Coach coach = this.coachRepository.findById(coachId);
        Athlete athlete = this.athleteRepository.findById(athleteId);
        if(coach == null || athlete == null) return null;
        if(!coach.getAthleteList().contains(athlete)) return null;
        return athlete.getWorkoutList();
    }
    @ApiOperation(value = "Create and save a new Workout to the database", response = String.class, tags = "createWorkout")
    @PostMapping(path = "/coaches/{coachId}/workouts")
    String createWorkout(@PathVariable int coachId, @RequestBody Workout workout)
    {
        Coach coach = this.coachRepository.findById(coachId);
        if(workout == null || coach == null)
        {
            return failure;
        }
        if(exerciseRepository.findByName(workout.getExercise().getExerciseName()) != null)
        {
            workout.setExercise(exerciseRepository.findByName((workout.getExercise().getExerciseName())));
        }
        else
        {
            exerciseRepository.save(workout.getExercise());
            workout.setExercise(workout.getExercise());
        }
        workout.setCoach(coach);
        workoutRepository.save(workout);
        return success;
    }
    @ApiOperation(value = "Create and save a new Exercise to the database", response = String.class, tags = "createExercise")
    @PostMapping(path = "/coaches/{coachId}/exercises")
    String createExercise(@PathVariable int coachId, @RequestBody Exercise exercise)
    {
        Coach coach = coachRepository.findById(coachId);
        if(exercise == null || coach == null)
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
    @ApiOperation(value = "Update an existing Exercise saved in the database", response = Exercise.class, tags = "updateExercise")
    @PutMapping(path = "/coaches/{coachId}/exercises/{id}")
    Exercise updateExercise(@PathVariable int coachId, @PathVariable int id, @RequestBody Exercise request)
    {
        Coach coach = coachRepository.findById(coachId);
        Exercise exercise = exerciseRepository.findById(id);
        if(exercise == null || coach == null)
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
    @ApiOperation(value = "Update an existing Workout in the database", response = Workout.class, tags = "updateWorkout")
    @PutMapping(path = "/coaches/{coachId}/workouts/{id}")
    Workout updateWorkout(@PathVariable int coachId, @PathVariable int id, @RequestBody Workout request)
    {
        Coach coach = coachRepository.findById(coachId);
        Workout workout = workoutRepository.findById(id);
        if(workout == null || coach == null)
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

    @ApiOperation(value = "Update an Assigned Athlete's Workout list", response = Workout.class, tags = "updateAthleteWorkoutList")
    @PutMapping(path = {"/coaches/{coachId}/athletes/{athleteId}/workouts/{workoutId}"})
    public List<Workout> updateAthleteWorkoutList(@PathVariable int coachId, @PathVariable int athleteId,
                                                  @PathVariable int workoutId){
        Coach coach = this.coachRepository.findById(coachId);
        Athlete athlete = this.athleteRepository.findById(athleteId);
        Workout workout = this.workoutRepository.findById(workoutId);


        if(coach == null || athlete == null || workout == null)
        {
            return null;
        }

        List<Athlete> athleteList = coach.getAthleteList();

        if(athleteList == null)
        {
            return null;
        }

        for(int i = 0; i < athleteList.size(); i++)
        {
            if(athleteList.get(i).equals(athlete))
            {
                athlete.addWorkout(workout);
                athleteList.set(i, athlete);
                coach.setAthleteList(athleteList);
                coachRepository.save(coach);
                return athleteRepository.findById(athleteId).getWorkoutList();
            }
        }
        return null;
    }

    @ApiOperation(value = "Delete an existing Workout in the database", response = String.class, tags = "deleteWorkout")
    @DeleteMapping(path = {"/coaches/{coachId}/workouts/{workoutId}"})
    public String deleteWorkout(@PathVariable int coachId, @PathVariable int workoutId)
    {
        Coach coach = this.coachRepository.findById(coachId);
        Workout workout = this.workoutRepository.findById(workoutId);
        if(coach == null || workout == null || workout.getCoach() != coach) { return failure; }
        workoutRepository.delete(workout);
        return success;
    }

    @ApiOperation(value = "Delete an existing Exercise in the database", response = String.class, tags = "deleteExercise")
    @DeleteMapping(path = {"/coaches/{coachId}/exercises/{exerciseId}"})
    public String deleteExercise(@PathVariable int coachId, @PathVariable int exerciseId)
    {
        Coach coach = this.coachRepository.findById(coachId);
        Exercise exercise = this.exerciseRepository.findById(exerciseId);
        if(coach == null || exercise == null) { return failure;}
        exerciseRepository.deleteById(exercise.getId());
        return success;
    }

    @ApiOperation(value = "Delete a Workout from an Assigned Athlete's Workout list", response = String.class, tags = "deleteAthleteWorkout")
    @DeleteMapping(path = {"/coaches/{coachId}/athletes/{athleteId}/workouts/{workoutId}"})
    public String deleteAthleteWorkout(@PathVariable int coachId, @PathVariable int athleteId,
                                       @PathVariable int workoutId) {
        Coach coach = this.coachRepository.findById(coachId);
        Athlete athlete = this.athleteRepository.findById(athleteId);
        Workout workout = this.workoutRepository.findById(workoutId);

        List<Athlete> athleteList = coach.getAthleteList();
        for (int i = 0; i < athleteList.size(); i++)
        {
            if(athleteList.get(i).equals(athlete))
            {
                athlete.removeWorkout(workout);
                athleteList.set(i, athlete);
                coach.setAthleteList(athleteList);
                coachRepository.save(coach);
                return success;
            }
        }

        return failure;
    }
    @ApiOperation(value = "Get the Coach's Assigned Manager", response = Manager.class, tags = "getManager")
    @GetMapping(path = {"/coaches/{coachId}/manager"})
    public Manager getManager(@PathVariable int coachId){
        Coach coach = coachRepository.findById(coachId);
        if(coach == null) return null;
        return coach.getManager();
    }
}
