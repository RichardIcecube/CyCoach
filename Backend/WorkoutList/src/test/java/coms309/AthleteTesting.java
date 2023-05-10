package coms309;

import static org.junit.jupiter.api.Assertions.assertEquals;

import coms309.Athletes.Athlete;
import coms309.Users.User;
import coms309.Workout.Workout;
import io.restassured.RestAssured;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.boot.test.context.SpringBootTest;

import io.restassured.response.Response;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig(Application.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AthleteTesting {


    @BeforeAll
    public void setUp() {
        RestAssured.port = 8080;
        RestAssured.baseURI = "http://localhost";
    }

    @AfterAll
    public void shutDown()
    {

    }
    @Test
    @Order(1)
    public void getAthleteByIdTest() {
        try{
            Response response2 = RestAssured.given().header("Content-Type", "application/json").
                    header("charset", "utf-8").
                    body(new JSONObject("{\n" +
                            "    \"firstName\" : \"Jerry\",\n" +
                            "    \"lastName\" : \"Cook\",\n" +
                            "    \"emailAddress\" : \"jerrycook@gmail.com\",\n" +
                            "    \"password\" : \"baseball321\",\n" +
                            "    \"classType\" : 1\n" +
                            "}").toString()).
                    when().
                    post("/users");

            Response response1 = RestAssured.given().header("Content-Type", "application/json").
                    header("charset", "utf-8").
                    body(new JSONObject("{\n" +
                            "    \"firstName\" : \"Jeremy\",\n" +
                            "    \"lastName\" : \"Baker\",\n" +
                            "    \"emailAddress\" : \"jeremybaker@gmail.com\",\n" +
                            "    \"password\" : \"golf123\",\n" +
                            "    \"classType\" : 2\n" +
                            "}").toString()).
                    when().
                    post("/users");

            Response response3 = RestAssured.given().header("Content-Type", "application/json").
                    header("charset", "utf-8").
                    body(new JSONObject("{\n" +
                            "    \"firstName\" : \"Johnny\",\n" +
                            "    \"lastName\" : \"Chef\",\n" +
                            "    \"emailAddress\" : \"johnnychef@gmail.com\",\n" +
                            "    \"password\" : \"soccer231\",\n" +
                            "    \"classType\" : 3\n" +
                            "}").toString()).
                    when().
                    post("/users");
            Response response4 = RestAssured.given().when().put("/managers/1/coaches/1/athletes/1");
            Response response5 = RestAssured.given().header("Content-Type", "application/json").
                    header("charset", "utf-8").
                    body(new JSONObject("{\n" +
                            "    \"sets\": 3,\n" +
                            "    \"reps\": 5,\n" +
                            "    \"duration\": \"\",\n" +
                            "    \"rest\": \"\",\n" +
                            "    \"exercise\": {\"exerciseName\": \"Squats\"}\n" +
                            "}").toString()).
                    when().
                    post("/coaches/1/workouts");
            Response response6 = RestAssured.given().when().put("/coaches/1/athletes/1/workouts/1");
            Response response = RestAssured.given().when().get("/athletes/1");
            String returnString = response.getBody().asString();
            assertEquals("{\"id\":1,\"user\":{\"id\":1,\"firstName\":\"Jerry\",\"lastName\":\"Cook\",\"emailAddress\":\"jerrycook@gmail.com\",\"password\":\"baseball321\",\"classType\":1},\"workoutList\":[{\"id\":1,\"sets\":3,\"reps\":5,\"duration\":\"\",\"rest\":\"\",\"video\":null,\"exercise\":{\"id\":1,\"exerciseName\":\"Squats\",\"hibernateLazyInitializer\":{}}}]}", returnString);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @Order(2)
    public void getCoachTest() {
        try{
            Response response = RestAssured.given().when().get("/athletes/1/coach");
            String returnString = response.getBody().asString();
            assertEquals("{\"id\":1,\"user\":{\"id\":2,\"firstName\":\"Jeremy\",\"lastName\":\"Baker\",\"emailAddress\":\"jeremybaker@gmail.com\",\"password\":\"golf123\",\"classType\":2}}", returnString);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @Order(3)
    public void getWorkoutList() {
        try {
            Response response = RestAssured.given().when().get("/athletes/1/workouts");
            String returnString = response.getBody().asString();
            assertEquals("[{\"id\":1,\"sets\":3,\"reps\":5,\"duration\":\"\",\"rest\":\"\",\"video\":null,\"exercise\":{\"id\":1,\"exerciseName\":\"Squats\",\"hibernateLazyInitializer\":{}}}]", returnString);
        }catch(Exception e){
            e.printStackTrace();
        }

    }
    @Test
    @Order(4)
    public void modelMethodsTest(){
        User user = new User("Richard", "Bach", "rbach@gmail.com", "boxing123", 1, null, null, null);
        Athlete athlete = new Athlete(user);
        athlete.setUser(user);
        user.setAthlete(athlete);
        assertEquals(user, athlete.getUser());
        athlete.setId(1);
        assertEquals(1, athlete.getId());
        Workout workout = new Workout(3, 5, "", "");
        athlete.addWorkout(workout);
        assertEquals(1, athlete.getWorkoutList().size());
        athlete.removeWorkout(workout);
        assertEquals(0, athlete.getWorkoutList().size());
    }
}
