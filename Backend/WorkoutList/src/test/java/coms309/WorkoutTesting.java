package coms309;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import javax.swing.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringJUnitConfig(Application.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class WorkoutTesting {

    @BeforeAll
    public void setUp()
    {
        RestAssured.port = 8080;
        RestAssured.baseURI = "http://localhost";
    }

    @Test
    @Order(1)
    public void createWorkoutTest()
    {
        try{
            Response response = RestAssured.given().header("Content-Type", "application/json").
                    header("charset", "utf-8").
                    body(new JSONObject("{\n" +
                            "    \"sets\" : 1, \n" +
                            "    \"reps\" : 5,\n" +
                            "    \"duration\" : \"1 mins\",\n" +
                            "    \"rest\" : \"10 sec\",\n" +
                            "    \"exercise\" : { \"exerciseName\" : \"Sit-ups\"}\n" +
                            "}").toString()).
                    when().
                    post("/workouts");
            String returnString = response.getBody().asString();

            Response response2 = RestAssured.given().header("Content-Type", "application/json").
                    header("charset", "utf-8").
                    body(new JSONObject("{\n" +
                            "    \"sets\" : 5, \n" +
                            "    \"reps\" : 10,\n" +
                            "    \"duration\" : \"2 mins\",\n" +
                            "    \"rest\" : \"10 mins\",\n" +
                            "    \"exercise\" : { \"exerciseName\" : \"Sit-ups\"}\n" +
                            "}").toString()).
                    when().
                    post("/workouts");
            String returnString2 = response.getBody().asString();

            assertEquals("{\"message\":\"success\"}", returnString);
            assertEquals("{\"message\":\"success\"}", returnString2);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    @Order(2)
    public void updateTests()
    {
        try{
            Response response = RestAssured.given().header("Content-Type", "application/json").
                    header("charset", "utf-8").
                    body(new JSONObject("{\n" +
                            "    \"sets\" : 20, \n" +
                            "    \"reps\" : 30,\n" +
                            "    \"duration\" : \"2 mins\",\n" +
                            "    \"rest\" : \"10 sec\",\n" +
                            "    \"exercise\" : { \"exerciseName\" : \"Sit-ups\"}\n" +
                            "}").toString()).
                    when().
                    put("/workouts/2");

            Response response2 = RestAssured.given().header("Content-Type", "application/json").
                    header("charset", "utf-8").
                    body(new JSONObject("{\n" +
                            "    \"sets\" : 20, \n" +
                            "    \"reps\" : 30,\n" +
                            "    \"duration\" : \"2 mins\",\n" +
                            "    \"rest\" : \"10 sec\",\n" +
                            "    \"exercise\" : { \"exerciseName\" : \"Push-ups\"}\n" +
                            "}").toString()).
                    when().
                    put("/workouts/2");

            RestAssured.given().header("Content-Type", "application/json").
                    header("charset", "utf-8").
                    body(new JSONObject("{\n" +
                            "    \"firstName\" : \"Jerry\",\n" +
                            "    \"lastName\" : \"Cook\",\n" +
                            "    \"emailAddress\" : \"jerrycook@gmail.com\",\n" +
                            "    \"password\" : \"baseball321\",\n" +
                            "    \"classType\" : 2\n" +
                            "}").toString()).
                    when().
                    post("/users");

            RestAssured.given().header("Content-Type", "application/json").
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
                    body(new JSONObject("{\"id\":2,\"user\":{\"id\":2,\"firstName\":\"Jeremy\",\"lastName\":\"Baker\",\"emailAddress\":\"jeremybaker@gmail.com\",\"password\":\"golf123\",\"classType\":2}}").toString()).
                    when().
                    put("/workouts/2/coach");

            Response response4 = RestAssured.given().
                    when().
                    put("/workouts/1/coach/1");

            Response response5 = RestAssured.given().header("Content-Type", "application/json").
                    header("charset", "utf-8").
                    body("https://www.youtube.com/watch?v=dQw4w9WgXcQ").
                    when().
                    put("/workouts/1/video");


            String returnString = response.getBody().asString();
            String returnString2 = response2.getBody().asString();
            String returnString3 = response3.getBody().asString();
            String returnString4 = response4.getBody().asString();
            String returnString5 = response5.getBody().asString();

            assertEquals("{\"id\":2,\"sets\":20,\"reps\":30,\"duration\":\"2 mins\",\"rest\":\"10 sec\",\"video\":null,\"exercise\":{\"id\":1,\"exerciseName\":\"Sit-ups\",\"hibernateLazyInitializer\":{}}}", returnString);
            assertEquals("{\"id\":2,\"sets\":20,\"reps\":30,\"duration\":\"2 mins\",\"rest\":\"10 sec\",\"video\":null,\"exercise\":{\"id\":2,\"exerciseName\":\"Push-ups\"}}", returnString2);
            assertEquals("{\"message\":\"success\"}", returnString3);
            assertEquals("{\"message\":\"success\"}", returnString4);
            assertEquals("{\"id\":1,\"sets\":1,\"reps\":5,\"duration\":\"1 mins\",\"rest\":\"10 sec\",\"video\":\"https://www.youtube.com/watch?v=dQw4w9WgXcQ\",\"exercise\":{\"id\":1,\"exerciseName\":\"Sit-ups\",\"hibernateLazyInitializer\":{}}}", returnString5);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    @Order(3)
    public void listAllWorkoutsTest()
    {
        try{
            Response response = RestAssured.given().
                    when().
                    get("/workouts");

            String returnString = response.getBody().asString();

            assertEquals("[{\"id\":1,\"sets\":1,\"reps\":5,\"duration\":\"1 mins\",\"rest\":\"10 sec\",\"video\":\"https://www.youtube.com/watch?v=dQw4w9WgXcQ\",\"exercise\":{\"id\":1,\"exerciseName\":\"Sit-ups\",\"hibernateLazyInitializer\":{}}},{\"id\":2,\"sets\":20,\"reps\":30,\"duration\":\"2 mins\",\"rest\":\"10 sec\",\"video\":null,\"exercise\":{\"id\":2,\"exerciseName\":\"Push-ups\",\"hibernateLazyInitializer\":{}}}]", returnString);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    @Order(4)
    public void getWorkoutByIdTest()
    {
        try{
            Response response = RestAssured.given().
                    when().
                    get("/workouts/2");

            String returnString = response.getBody().asString();

            assertEquals("{\"id\":2,\"sets\":20,\"reps\":30,\"duration\":\"2 mins\",\"rest\":\"10 sec\",\"video\":null,\"exercise\":{\"id\":2,\"exerciseName\":\"Push-ups\",\"hibernateLazyInitializer\":{}}}", returnString);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    @Order(5)
    public void getWorkoutExercise()
    {
        try{
            Response response = RestAssured.given().
                    when().
                    get("/workouts/1/exercises");

            String returnString = response.getBody().asString();

            assertEquals("{\"id\":1,\"exerciseName\":\"Sit-ups\",\"hibernateLazyInitializer\":{}}", returnString);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    @Order(6)
    public void getWorkoutVideo()
    {
        try{
            Response response = RestAssured.given().
                    when().
                    get("/workouts/1/video");

            String returnString = response.getBody().asString();

            assertEquals("https://www.youtube.com/watch?v=dQw4w9WgXcQ", returnString);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    @Order(7)
    public void getCoachTest()
    {
        try{
            Response response = RestAssured.given().
                    when().
                    get("/workouts/2/coach");

            String returnString = response.getBody().asString();

            assertEquals("{\"id\":2,\"user\":{\"id\":2,\"firstName\":\"Jeremy\",\"lastName\":\"Baker\",\"emailAddress\":\"jeremybaker@gmail.com\",\"password\":\"golf123\",\"classType\":2},\"hibernateLazyInitializer\":{}}", returnString);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
