package coms309.Managers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import coms309.Coaches.Coach;
import coms309.Users.User;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Manager {
    @ApiModelProperty(notes = "Manager ID", example="1", required = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;

    @ApiModelProperty(notes = "Managers user info", example="{\"id\":1,\"firstName\":\"Zane\",\"lastName\":\"Eason\",\"emailAddress\":\"zseason@iastate.edu\",\"password\":\"123\",\"classType\":3}", required = true)
    @OneToOne
    @JoinColumn( name = "user_id")
    private User user;

    @ApiModelProperty(notes = "Managers coach list", example = "{\"id\":1,\"user\":{\"id\":2,\"firstName\":\"Jack\",\"lastName\":\"Smith\",\"emailAddress\":\"jacksmith@gmail.com\",\"password\":\"baseball123\",\"classType\":2}}", required = false)
    @OneToMany( mappedBy = "manager" )
    @JsonIgnore
    private List<Coach> coachList;
    public Manager()
    {
        coachList = new ArrayList<>();
    }
    public Manager(User user)
    {
        coachList = new ArrayList<>();
        this.user = user;
    }

    public long getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setUser(User user) { this.user = user; }
    public User getUser() { return this.user; }
    public List<Coach> getCoachList() { return this.coachList; }
    public void setCoachList(List<Coach> coachList) { this.coachList = coachList; }
}
