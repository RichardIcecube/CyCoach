package coms309.Users;

import coms309.Athletes.Athlete;
import coms309.Athletes.AthleteRepository;
import coms309.Coaches.Coach;
import coms309.Coaches.CoachRepository;
import coms309.Managers.ManagerRepository;
import coms309.Managers.Manager;
import coms309.Workout.Workout;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(value = "User Controller", description = "REST API related to User Entity", tags="UserController")
@RestController
public class UserController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    CoachRepository coachRepository;
    @Autowired
    AthleteRepository athleteRepository;
    @Autowired
    ManagerRepository managerRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @ApiOperation(value = "Get a list of all saved Users in the database", response = User.class, tags = "getAllUsers")
    @GetMapping( path = {"/users"})
    List<User> getAllUsers() { return this.userRepository.findAll(); }
    @ApiOperation(value = "Get an individual User by User ID", response = User.class, tags = "getUserById")
    @GetMapping( path = {"/users/{id}"})
    User getUserById(@PathVariable int id) { return this.userRepository.findById(id); }
    @ApiOperation(value = "Get a User's Associated Athlete", response = Athlete.class, tags = "getAthlete")
    @GetMapping (path = {"/users/{id}/athlete"})
    Athlete getAthlete(@PathVariable int id) {
        User user = this.userRepository.findById(id);
        if(user == null) return null;
        return user.getAthlete();
    }
    @ApiOperation(value = "Get a User's Associated Athlete's Workout List", response = Workout.class, tags = "getAthleteWorkoutList")
    @GetMapping (path = {"/users/{id}/athlete/workouts"})
    List<Workout> getAthleteWorkoutList(@PathVariable int id) {
        User user = this.userRepository.findById(id);
        if(user == null) return null;
        Athlete athlete = user.getAthlete();
        if(athlete == null) return null;
        return athlete.getWorkoutList();
    }
    @ApiOperation(value = "Get a User's Associated Coach", response = Coach.class, tags = "getCoach")
    @GetMapping (path = {"/users/{id}/coach"})
    Coach getCoach(@PathVariable int id) {
        User user = this.userRepository.findById(id);
        if (user == null) return null;
        return user.getCoach();
    }

    @ApiOperation(value = "Get a User's Associated Coach's Athlete list", response = Athlete.class, tags = "getCoachAthleteList")
    @GetMapping (path = {"/users/{id}/coach/athletes"})
    List<Athlete> getCoachAthleteList(@PathVariable int id){
        User user = this.userRepository.findById(id);
        if(user == null) return null;
        Coach coach = user.getCoach();
        if(coach == null) return null;
        return coach.getAthleteList();
    }
    @ApiOperation(value = "Get a User's Associated Manager", response = Manager.class, tags = "getManager")
    @GetMapping (path = {"/users/{id}/manager"})
    Manager getManager(@PathVariable int id) {
        User user = this.userRepository.findById(id);
        if(user == null) return null;
        return user.getManager();
    }
    @ApiOperation(value = "Create and save a new User in the database", response = String.class, tags = "createUser")
    @PostMapping( path = {"/users"})
    String createUser(@RequestBody User user) {
        if(user == null) {
            return this.failure;
        }
        else {
            switch(user.getClassType())
            {
                case 1:
                    Athlete athlete = new Athlete();
                    user.setAthlete(athlete);
                    athleteRepository.save(athlete);
                    athlete.setUser(user);
                    break;
                case 2:
                    Coach coach = new Coach();
                    user.setCoach(coach);
                    coachRepository.save(coach);
                    coach.setUser(user);
                    break;
                case 3:
                    Manager manager = new Manager();
                    user.setManager(manager);
                    managerRepository.save(manager);
                    manager.setUser(user);
                    break;
            }
            this.userRepository.save(user);
            return this.success;
        }
    }
    @ApiOperation(value = "Update an existing User in the database", response = User.class, tags = "updateUser")
    @PutMapping(path = {"/users/{id}"})
    User updateUser(@PathVariable int id, @RequestBody User request) {
        User user = this.userRepository.findById(id);
        if(user == null) {
            return null;
        }
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmailAddress(request.getEmailAddress());
        user.setPassword(request.getPassword());
        user.setClassType(request.getClassType());
        this.userRepository.save(user);
        return this.userRepository.findById(id);
    }

    @ApiOperation(value = "Delete an existing User in the database", response = String.class, tags = "deleteUser")
    @DeleteMapping(path = {"/users/{id}"})
    String deleteUser(@PathVariable int id)
    {
        this.userRepository.deleteById(id);
        return success;
    }

    @ApiOperation(value = "Get an individual User by their email", response = User.class, tags = "getUserByEmail")
    @GetMapping(path = {"/users/email/{email}"})
    User getUserByEmail(@PathVariable String email)
    {
        for(User user : userRepository.findAll())
        {
            if(user != null && user.getEmailAddress() != null) {
                if (user.getEmailAddress().equals(email)) {
                    return user;
                }
            }
        }
        return null;
    }

    @ApiOperation(value = "Get a User's Associated Class's ID", response = Integer.class, tags = "getClassIDByUserId")
    @GetMapping(path = {"users/{userId}/classID"})
    int getClassIDByUserId(@PathVariable int userId)
    {
        User user = userRepository.findById(userId);
        if(user == null)
        {
            return 0;
        }

        if(user.getClassType() == 1)
        {
            if(user.getAthlete() == null)
            {
                return 0;
            }
            return (int)user.getAthlete().getId();
        }
        else if(user.getClassType() == 2)
        {
            if(user.getCoach() == null)
            {
                return 0;
            }
            return (int)user.getCoach().getId();
        }
        else if(user.getClassType() == 3)
        {
            if(user.getManager() == null)
            {
                return 0;
            }
            return (int)user.getManager().getId();
        }

        return 0;

    }

}
