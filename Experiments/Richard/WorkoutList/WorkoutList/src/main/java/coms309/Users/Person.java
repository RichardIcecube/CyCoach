package coms309.Users;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
abstract public class Person {
    @Id
    @GeneratedValue
    private String emailAddress;
    private String userName;
    private String firstName;
    private String lastName;
    private Date dateJoined;
    private String password;

    public Person()
    {

    }
    public Person(String emailAddress, String userName, String firstName, String lastName, String password)
    {
        this.emailAddress = emailAddress;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        dateJoined = new Date();
    }

    public String getEmailAddress() { return emailAddress; }
    public void setEmailAddress(String emailAddress) { this.emailAddress = emailAddress; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getDateJoined() { return dateJoined.toString(); }
    public void setDateJoined(String date)
    { dateJoined = new Date(date); }
    public void setDateJoined()
    { dateJoined = new Date(); }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String toString() { return firstName + " " + lastName + " joined " + dateJoined.toString(); }
}
