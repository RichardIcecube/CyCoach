package coms309.Users;
import java.util.HashMap;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;

import javax.websocket.server.PathParam;

@RestController
public class Manager extends Person{
    HashMap<Coach, Athlete> assignmentList = new HashMap<>();
    HashMap<String, Person> userList = new HashMap<>();
    @PostMapping("/manager/register/{userClass}")
    public @ResponseBody String createUser(@PathVariable String userClass, @RequestBody Person person)
    {
        switch(userClass)
        {
            case "Coach":
                userList.put(person.getUserName(), (Coach)person);
                break;
            case "Athlete":
                userList.put(person.getUserName(), (Athlete)person);
                break;
        }
        return "New " + userClass + " registered: " + person.getUserName() + ", " + person.getFirstName() + " "
                + person.getLastName();
    }
    @PostMapping("manager/assign")
    public @ResponseBody String createAssignment(@RequestBody String athleteUserName, @RequestBody String coachUserName)
    {
        assignmentList.put((Coach)userList.get(coachUserName), (Athlete)userList.get(athleteUserName));
        return "New coaching assignment added: Coach - " + userList.get(coachUserName).toString() + " Athlete - "
                + userList.get(athleteUserName).toString();
    }
    @GetMapping("/manager/roster")
    public @ResponseBody HashMap<String, Person> getUserList() { return userList; }
    @GetMapping("/manager/assign")
    public @ResponseBody HashMap<Coach, Athlete> getAssignmentList() { return assignmentList; }
    @DeleteMapping("/manager/roster/{userName}")
    public @ResponseBody HashMap<String, Person> deleteUser(@PathVariable String userName)
    {
        if(assignmentList.keySet().contains((Coach)userList.get(userName)))
        {
            assignmentList.remove((Coach)userList.get(userName));
            userList.remove(userName);
            return userList;
        }
        Coach c = (Coach)userList.get(((Athlete)userList.get(userName)).getCoachUserName());
        assignmentList.remove(c, (Athlete)userList.get(userName));
        userList.remove(userName);
        return userList;
    }
    @PutMapping("/manager/roster/{userName}")
    public @ResponseBody Person updateUser(@PathVariable String userName, @RequestBody Person person)
    {

        userList.replace(userName, person);
        return userList.get(userName);
    }
}
