package coms309;

import static org.junit.jupiter.api.Assertions.assertEquals;

import coms309.Athletes.Athlete;
import coms309.Coaches.Coach;
import coms309.Exercises.Exercise;
import coms309.Users.User;
import coms309.Workout.Workout;
import io.restassured.RestAssured;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import io.restassured.response.Response;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.ArrayList;
import java.util.List;


@SpringJUnitConfig(Application.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class CoachTesting {
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
    public void getCoachByIdTest() {
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
            Response response7 = RestAssured.given().when().put("managers/1/coaches/1");
            Response response = RestAssured.given().when().get("/coaches/1");
            String returnString = response.getBody().asString();
            assertEquals("{\"id\":1,\"user\":{\"id\":2,\"firstName\":\"Jeremy\",\"lastName\":\"Baker\",\"emailAddress\":\"jeremybaker@gmail.com\",\"password\":\"golf123\",\"classType\":2}}", returnString);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Test
    @Order(2)
    public void getAthleteListTest() {
        try{
            Response response = RestAssured.given().when().get("/coaches/1/athletes");
            String returnString = response.getBody().asString();
            assertEquals("[{\"id\":1,\"user\":{\"id\":1,\"firstName\":\"Jerry\",\"lastName\":\"Cook\",\"emailAddress\":\"jerrycook@gmail.com\",\"password\":\"baseball321\",\"classType\":1},\"workoutList\":[{\"id\":1,\"sets\":3,\"reps\":5,\"duration\":\"\",\"rest\":\"\",\"video\":null,\"exercise\":{\"id\":1,\"exerciseName\":\"Squats\",\"hibernateLazyInitializer\":{}}}]}]", returnString);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    @Test
    @Order(3)
    public void getWorkoutListTest() {
        try{
            Response response = RestAssured.given().when().get("/coaches/1/workouts");
            String returnString = response.getBody().asString();
            assertEquals("[{\"id\":1,\"sets\":3,\"reps\":5,\"duration\":\"\",\"rest\":\"\",\"video\":null,\"exercise\":{\"id\":1,\"exerciseName\":\"Squats\",\"hibernateLazyInitializer\":{}}}]", returnString);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    @Order(4)
    public void getWorkoutByIdTest() {
        try{
            Response response = RestAssured.given().when().get("/coaches/1/workouts/1");
            String returnString = response.getBody().asString();
            assertEquals("{\"id\":1,\"sets\":3,\"reps\":5,\"duration\":\"\",\"rest\":\"\",\"video\":null,\"exercise\":{\"id\":1,\"exerciseName\":\"Squats\",\"hibernateLazyInitializer\":{}}}", returnString);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @Order(5)
    public void getExerciseListTest() {
        try{
            Response response = RestAssured.given().when().get("/coaches/1/exercises");
            String returnString = response.getBody().asString();
            assertEquals("[{\"id\":1,\"exerciseName\":\"Squats\"}]", returnString);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @Order(6)
    public void getExerciseByIdTest() {
        try{
            Response response = RestAssured.given().when().get("/coaches/1/exercises/1");
            String returnString = response.getBody().asString();
            assertEquals("{\"id\":1,\"exerciseName\":\"Squats\"}", returnString);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Test
    @Order(7)
    public void getAthleteWorkoutListTest() {
        try{
            Response response = RestAssured.given().when().get("/coaches/1/athletes/1/workouts");
            String returnString = response.getBody().asString();
            assertEquals("[{\"id\":1,\"sets\":3,\"reps\":5,\"duration\":\"\",\"rest\":\"\",\"video\":null,\"exercise\":{\"id\":1,\"exerciseName\":\"Squats\",\"hibernateLazyInitializer\":{}}}]", returnString);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Test
    @Order(8)
    public void createExerciseTest(){
        try{
            Response response1 = RestAssured.given().header("Content-Type", "application/json").
                    header("charset", "utf-8").
                    body(new JSONObject("{\"exerciseName\": \"Pull Ups\"}").toString()).
                    when().
                    post("/coaches/1/exercises");
            String returnString = response1.getBody().asString();
            assertEquals("{\"message\":\"success\"}", returnString);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    @Test
    @Order(9)
    public void createWorkoutTest(){
        try{
            Response response = RestAssured.given().header("Content-Type", "application/json").
                    header("charset", "utf-8").
                    body(new JSONObject("{\n" +
                            "    \"sets\": 3,\n" +
                            "    \"reps\": 5,\n" +
                            "    \"duration\": \"\",\n" +
                            "    \"rest\": \"\",\n" +
                            "    \"exercise\": {\"exerciseName\": \"Pull Ups\"}\n" +
                            "}").toString()).
                    when().
                    post("/coaches/1/workouts");
            String returnString = response.getBody().asString();
            assertEquals("{\"message\":\"success\"}", returnString);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    @Test
    @Order(10)
    public void updateExerciseTest(){
        try{
            Response response = RestAssured.given().header("Content-Type", "application/json").
                    header("charset", "utf-8").
                    body(new JSONObject("{\"exerciseName\":\"Deadlift\"}").toString()).
                    when().
                    put("/coaches/1/exercises/1");
            String returnString = response.getBody().asString();
            assertEquals("{\"id\":1,\"exerciseName\":\"Deadlift\"}", returnString);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Test
    @Order(11)
    public void updateWorkoutTest(){
        try{
            Response response = RestAssured.given().header("Content-Type", "application/json").
                    header("charset", "utf-8").
                    body(new JSONObject("{\n" +
                            "    \"sets\": 3,\n" +
                            "    \"reps\": 7,\n" +
                            "    \"duration\": \"\",\n" +
                            "    \"rest\": \"\",\n" +
                            "    \"exercise\": {\"exerciseName\": \"Squats\"}\n" +
                            "}").toString()).
                    when().
                    put("/coaches/1/workouts/1");
            String returnString = response.getBody().asString();
            assertEquals("{\"id\":1,\"sets\":3,\"reps\":7,\"duration\":\"\",\"rest\":\"\",\"video\":null,\"exercise\":{\"id\":3,\"exerciseName\":\"Squats\"}}", returnString);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Test
    @Order(12)
    public void deleteAthleteWorkoutTest(){
        try{
            Response response = RestAssured.given().when().delete("/coaches/1/athletes/1/workouts/1");
            String returnString = response.getBody().asString();
            assertEquals("{\"message\":\"success\"}", returnString);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Test
    @Order(13)
    public void deleteExerciseTest(){
        try{
            Response response = RestAssured.given().when().delete("/coaches/1/exercises/3");
            String returnString = response.getBody().asString();
            assertEquals("{\"message\":\"success\"}", returnString);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Test
    @Order(14)
    public void deleteWorkoutTest(){
        try{
            Response response = RestAssured.given().when().delete("/coaches/1/workouts/2");
            String returnString = response.getBody().asString();
            assertEquals("{\"message\":\"success\"}", returnString);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Test
    @Order(15)
    public void getManagerTest(){
        try{
            Response response = RestAssured.given().when().get("/coaches/1/manager");
            String returnString = response.getBody().asString();
            assertEquals("{\"id\":1,\"user\":{\"id\":3,\"firstName\":\"Johnny\",\"lastName\":\"Chef\",\"emailAddress\":\"johnnychef@gmail.com\",\"password\":\"soccer231\",\"classType\":3}}", returnString);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    @Test
    @Order(16)
    public void methodCoachTest(){
        User user = new User("Richard", "Bach", "rbach@gmail.com", "boxing123", 1, null, null, null);
        Coach coach = new Coach(user);
        coach.setId(20);
        user.setCoach(coach);
        assertEquals(20, coach.getId());
        Athlete athlete = new Athlete();
        coach.addAthlete(athlete);
        assertEquals(1, coach.getAthleteList().size());
        coach.removeAthlete(athlete);
        assertEquals(0, coach.getAthleteList().size());
        coach.addAthlete(athlete);
        coach.setAthleteList(new ArrayList<Athlete>());
        assertEquals(0, coach.getAthleteList().size());
    }
}
