package Starting.Users;

public class Athlete extends Person {
    private String workoutList;
    public Athlete()
    {

    }
    public Athlete(String workoutList, Person person)
    {
        this.setDateJoined(person.getDateJoined());
        this.setUserName(person.getUserName());
        this.setFirstName(person.getFirstName());
        this.setLastName(person.getLastName());
        this.workoutList = workoutList;
    }

    public Athlete(String workoutList, String userName, String firstName, String lastName, String dateJoined)
    {
        this.workoutList = workoutList;
        setUserName(userName);
        setFirstName(firstName);
        setLastName(lastName);
        setDateJoined(dateJoined);
    }

    public String getWorkoutList()
    {
        return workoutList;
    }

}
