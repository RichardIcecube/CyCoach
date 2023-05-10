package Starting.Users;
import java.util.Date;
abstract public class Person {
    private String userName;
    private String firstName;
    private String lastName;
    private String dateJoined;

    public Person()
    {

    }
    public Person(String userName, String firstName, String lastName, String dateJoined)
    {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateJoined = dateJoined;
    }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getDateJoined() { return dateJoined; }
    public void setDateJoined(String date) { dateJoined = date; }
    /*
    public void setDateJoined(int year, int month, int date, int hrs, int min, int sec)
    { dateJoined = new Date(year, month, date, hrs, min, sec); }
    public void setDateJoined(int year, int month, int date, int hrs, int min)
    { dateJoined = new Date(year, month, date, hrs, min); }
    public void setDateJoined(int year, int month, int date)
    { dateJoined = new Date(year, month, date); }

    public void setDateJoined(Date dateJoined)
    {
        this.dateJoined = dateJoined;
    }
    public void setDateJoined()
    { dateJoined = new Date(); }


     */
    public String toString() { return firstName + " " + lastName + " joined " + dateJoined.toString(); }
}
