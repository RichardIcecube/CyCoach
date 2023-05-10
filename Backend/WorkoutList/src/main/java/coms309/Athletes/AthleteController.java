package coms309.Athletes;

import coms309.Coaches.Coach;
import coms309.Coaches.CoachRepository;
import coms309.Workout.Workout;
import coms309.Workout.WorkoutRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@Api(value = "Athlete Controller", description = "REST API related to Athlete Entity", tags="AthleteController")
@RestController
public class AthleteController {

    @Autowired
    AthleteRepository athleteRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @ApiOperation(value = "Get individual Athletes by their Athlete ID", response = Athlete.class, tags = "getAthleteById")
    @GetMapping(path = {"/athletes/{id}"})
    public Athlete getAthleteById(@PathVariable int id) { return athleteRepository.findById(id); }
    @ApiOperation(value = "Get an Athlete's assigned Coach", response = Coach.class, tags = "getCoach")
    @GetMapping(path = {"/athletes/{id}/coach"})
    public Coach getCoach(@PathVariable int id) {
        Athlete athlete = athleteRepository.findById(id);
        if(athlete == null) return null;
        return athlete.getCoach();
    }
    @ApiOperation(value = "Get an Athlete's workout list", response = Workout.class, tags = "getWorkoutList")
    @GetMapping(path = {"/athletes/{id}/workouts"})
    public List<Workout> getWorkoutList(@PathVariable int id) {
        Athlete athlete = athleteRepository.findById(id);
        if(athlete == null) return null;
        return athlete.getWorkoutList();
    }
}
