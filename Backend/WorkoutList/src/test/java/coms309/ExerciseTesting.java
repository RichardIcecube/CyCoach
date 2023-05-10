package coms309;

import static org.junit.jupiter.api.Assertions.assertEquals;

import coms309.Exercises.Exercise;
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
public class ExerciseTesting {
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
    public void createExercisesTest(){
        try{
            Response response1 = RestAssured.given().header("Content-Type", "application/json").
                    header("charset", "utf-8").
                    body(new JSONObject("{\"exerciseName\": \"Squats\"}").toString()).
                    when().
                    post("/exercises");
            String returnString = response1.getBody().asString();
            assertEquals("{\"message\":\"success\"}", returnString);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    @Test
    @Order(2)
    public void listAllExercisesTest(){
        try{
            Response response = RestAssured.given().when().get("/exercises");
            String returnString = response.getBody().asString();
            assertEquals("[{\"id\":1,\"exerciseName\":\"Squats\"}]", returnString);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    @Order(3)
    public void getExerciseByIdTest(){
        try{
            Response response = RestAssured.given().when().get("/exercises/1");
            String returnString = response.getBody().asString();
            assertEquals("{\"id\":1,\"exerciseName\":\"Squats\"}", returnString);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    @Test
    @Order(4)
    public void updateExerciseTest(){
        try{
            Response response = RestAssured.given().header("Content-Type", "application/json").
            header("charset", "utf-8").
            body(new JSONObject("{\"exerciseName\":\"Deadlift\"}").toString()).
            when().
            put("/exercises/1");
            String returnString = response.getBody().asString();
            assertEquals("{\"id\":1,\"exerciseName\":\"Deadlift\"}", returnString);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    @Test
    @Order(5)
    public void deleteExerciseTest(){
        try{
            Response response = RestAssured.given().when().delete("/exercises/1");
            String returnString = response.getBody().asString();
            assertEquals("{\"message\":\"success\"}", returnString);
            Response response1 = RestAssured.given().when().get("/exercises");
            returnString = response1.getBody().asString();
            assertEquals("[]", returnString);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    @Test
    @Order(6)
    public void setWorkoutListTest(){
        Exercise exercise = new Exercise("Push Ups");
        assertEquals(0, exercise.getWorkoutList().size());
        List<Workout> workoutList = new ArrayList<Workout>();
        workoutList.add(new Workout(3, 5, "", ""));
        exercise.setWorkoutList(workoutList);
        assertEquals(1, exercise.getWorkoutList().size());
    }
    @Test
    @Order(7)
    public void setIdTest(){
        Exercise exercise = new Exercise("Push Ups");
        exercise.setId(20);
        assertEquals(20, exercise.getId());
    }
}
