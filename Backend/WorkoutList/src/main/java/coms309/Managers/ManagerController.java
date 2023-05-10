package coms309.Managers;

import coms309.Athletes.AthleteRepository;
import coms309.Athletes.Athlete;
import coms309.Coaches.Coach;
import coms309.Coaches.CoachRepository;
import coms309.Users.User;
import coms309.Users.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "Manager Controller", description = "REST API related to Manager Entity", tags = "ManagerController")
@RestController
public class ManagerController {

    @Autowired
    ManagerRepository managerRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AthleteRepository athleteRepository;

    @Autowired
    CoachRepository coachRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @ApiOperation(value = "Get a list of all managers", response = Manager.class, tags = "getAllManagers")
    @GetMapping( path = {"/managers"})
    List<Manager> getAllManagers() { return this.managerRepository.findAll(); }

    @ApiOperation(value = "Get a manager by id", response = Manager.class, tags = "getManagerById")
    @GetMapping( path = {"/managers/{id}"})
    Manager getManagerById(@PathVariable int id) { return this.managerRepository.findById(id); }

    @ApiOperation(value = "Get a list of all users", response = User.class, tags = "getAllUsers")
    @GetMapping( path = {"/managers/{managerId}/users"})
    List<User> getAllUsers(@PathVariable int managerId)
    {
        Manager manager = this.managerRepository.findById(managerId);
        if(manager == null)
        {
            return null;
        }
        return userRepository.findAll();
    }

    @ApiOperation(value = "Link together an athlete and a coach", response = String.class, tags = "linkAthleteCoach")
    @PutMapping( path = {"/managers/{managerId}/coaches/{coachId}/athletes/{athleteId}"})
    public String linkAthleteCoach(@PathVariable int managerId, @PathVariable int coachId, @PathVariable int athleteId)
    {
        Manager manager = managerRepository.findById(managerId);
        Coach coach = coachRepository.findById(coachId);
        Athlete athlete = athleteRepository.findById(athleteId);
        if(manager == null || coach == null || athlete == null)
        {
            return failure;
        }

        if(coach.getAthleteList().contains(athlete))
        {
            return failure + "\n Athlete is already assigned to this coach.";
        }
        
        athlete.setCoach(coach);
        athleteRepository.save(athlete);
        return success;
    }

    @ApiOperation(value = "Unlink an athlete and coach", response = String.class, tags = "unlinkAthlete")
    @PutMapping(path = {"/managers/{managerId}/athletes/{athleteId}/unlink"})
    public String unlinkAthlete(@PathVariable int managerId, @PathVariable int athleteId){
        Manager manager = managerRepository.findById(managerId);
        Athlete athlete = athleteRepository.findById(athleteId);
        if(manager == null || athlete == null) {
            return failure;
        }
        if(athlete.getCoach() == null) return failure + "\n Athlete is not assigned to a coach";
        athlete.setCoach(null);
        athleteRepository.save(athlete);
        return success;
    }

    @ApiOperation(value = "Get a list of all the coaches", response = Coach.class, tags = "getAllCoaches")
    @GetMapping( path = {"/managers/{managerId}/coaches"})
    public List<Coach> getAllCoaches(@PathVariable int managerId)
    {
        Manager manager = managerRepository.findById(managerId);
        if(manager == null)
        {
            return null;
        }
        return coachRepository.findAll();
    }

    @ApiOperation(value = "Get a list of all athletes", response = Athlete.class, tags = "getAllAthletes")
    @GetMapping( path = {"/managers/{managerId}/athletes"})
    public List<Athlete> getAllAthletes(@PathVariable int managerId)
    {
        Manager manager = managerRepository.findById(managerId);
        if(manager == null)
        {
            return null;
        }
        return athleteRepository.findAll();
    }

    @ApiOperation(value = "Get a list of all of a coaches athletes", response = Athlete.class, tags = "getCoachesAthletes")
    @GetMapping( path = {"/managers/{managerId}/coaches/{coachId}/athletes"})
    public List<Athlete> getCoachesAthletes(@PathVariable int managerId, @PathVariable int coachId)
    {
        Manager manager = managerRepository.findById(managerId);
        Coach coach = coachRepository.findById(coachId);
        if(manager == null || coach == null)
        {
            return null;
        }
        return coach.getAthleteList();
    }

    @ApiOperation(value = "Delete a user", response = String.class, tags = "deleteUser")
    @DeleteMapping( path = {"/managers/{managerId}/users/{id}"})
    public String deleteUser(@PathVariable int managerId, @PathVariable int id)
    {
        Manager manager = managerRepository.findById(managerId);
        User user = userRepository.findById(id);
        if(manager == null || user == null)
        {
            return failure;
        }
        userRepository.deleteById(id);
        return success;
    }

    @ApiOperation(value = "Get a list of a managers coaches", response = Coach.class, tags = "getManagersCoaches")
    @GetMapping(path = {"/managers/{managerId}/coachesList"})
    public List<Coach> getManagersCoaches(@PathVariable int managerId){
        Manager manager = managerRepository.findById(managerId);
        if(manager == null) return null;
        return manager.getCoachList();
    }

    @ApiOperation(value = "Link a coach and a manager", response = String.class, tags = "linkCoachManager")
    @PutMapping(path = {"/managers/{managerId}/coaches/{coachId}"})
    public String linkCoachManager(@PathVariable int managerId, @PathVariable int coachId) {
        Manager manager = managerRepository.findById(managerId);
        Coach coach = coachRepository.findById(coachId);
        if(coach == null || manager == null) return null;
        if(manager.getCoachList().contains(coach)){
            return failure + "\n Coach is already assigned to this manager.";
        }
        coach.setManager(manager);
        coachRepository.save(coach);
        return success;
    }

    @ApiOperation(value = "Unlink a coach and a manager", response = String.class, tags = "unlinkCoachManager")
    @PutMapping(path = {"/managers/{managerId}/coaches/{coachId}/unlink"})
    public String unlinkCoachManager(@PathVariable int managerId, @PathVariable int coachId) {
        Manager manager = managerRepository.findById(managerId);
        Coach coach = coachRepository.findById(coachId);
        if(coach == null || manager == null) return null;
        if(coach.getManager() == null) return failure + "\nCoach is not assigned to a manager.";
        coach.setManager(null);
        coachRepository.save(coach);
        return success;
    }
}
