package coms309.Users;
import com.fasterxml.jackson.annotation.JsonIgnore;
import coms309.Athletes.Athlete;
import coms309.Coaches.Coach;
import coms309.Managers.Manager;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

@Entity
public class User {
    @ApiModelProperty(notes = "User ID", example = "1", required = true)
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Id
    private int id;
    @ApiModelProperty(notes = "User First Name", example = "John", required = true)
    private String firstName;
    @ApiModelProperty(notes = "User Last Name", example = "Baker", required = true)
    private String lastName;
    @ApiModelProperty(notes = "User Email Address", example = "jbaker@gmail.com", required = true)
    private String emailAddress;
    @ApiModelProperty(notes = "User Password", example = "boxing123", required = true)
    private String password;
    @ApiModelProperty(notes = "User Class Type", example = "1", required = true)
    private int classType;
    @ApiModelProperty(notes = "Associated Athlete", example = "{\"id\":1,\"firstName\":\"Zane\",\"lastName\":\"Eason\",\"emailAddress\":\"zseason@iastate.edu\",\"password\":\"123\",\"classType\":1}", required = false)
    @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private Athlete athlete;
    @ApiModelProperty(notes = "Associated Coach", example = "{\"id\":1,\"firstName\":\"Zane\",\"lastName\":\"Eason\",\"emailAddress\":\"zseason@iastate.edu\",\"password\":\"123\",\"classType\":2}", required = false)
    @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private Coach coach;
    @ApiModelProperty(notes = "Associated Manager", example = "{\"id\":1,\"firstName\":\"Zane\",\"lastName\":\"Eason\",\"emailAddress\":\"zseason@iastate.edu\",\"password\":\"123\",\"classType\":3}", required = false)
    @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private Manager manager;
    public User()
    {

    }
    public User(String firstName, String lastName, String emailAddress,
                String password, int classType, Athlete athlete, Coach coach, Manager manager)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.password = password;
        this.classType = classType;
        switch(classType)
        {
            case 1:
                this.athlete = athlete;
                this.coach = null;
                this.manager = null;
                break;
            case 2:
                this.athlete = null;
                this.coach = coach;
                this.manager = null;
                break;

            case 3:
                this.athlete = null;
                this.coach = null;
                this.manager = manager;
                break;
        }
    }

    public int getId() { return this.id; }
    public void setId(int id) { this.id = id; }
    public String getFirstName() { return this.firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return this.lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getEmailAddress() { return this.emailAddress; }
    public void setEmailAddress(String emailAddress) { this.emailAddress = emailAddress; }
    public String getPassword() { return this.password; }
    public void setPassword(String password) { this.password = password; }
    public int getClassType() { return classType; }
    public void setClassType(int classType) { this.classType = classType; }
    public Athlete getAthlete() { return this.athlete; }
    public void setAthlete(Athlete athlete) {
        this.athlete = athlete;
        this.coach = null;
        this.manager = null;
    }
    public Coach getCoach() { return this.coach; }
    public void setCoach(Coach coach) {
        this.coach = coach;
        this.athlete = null;
        this.manager = null;
    }
    public Manager getManager() { return this.manager; }
    public void setManager(Manager manager) {
        this.coach = null;
        this.athlete = null;
        this.manager = manager;
    }
}
