package coms309;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.restassured.RestAssured;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import io.restassured.response.Response;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@TestMethodOrder(OrderAnnotation.class)
@SpringJUnitConfig(Application.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class UserTesting {

    @BeforeAll
    public void setUp() {
        RestAssured.port = 8080;
        RestAssured.baseURI = "http://localhost";
    }

    @Test
    @Order(1)
    public void createUserTest() {
        try {
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

        Response response = RestAssured.given().header("Content-Type", "application/json").
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

        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        int statusCode2 = response2.getStatusCode();
        assertEquals(200, statusCode2);

        int statusCode3 = response3.getStatusCode();
        assertEquals(200, statusCode3);



        String returnString = response.getBody().asString();
        assertEquals("{\"message\":\"success\"}", returnString);

        String returnString2 = response2.getBody().asString();
        assertEquals("{\"message\":\"success\"}", returnString2);

        String returnString3 = response3.getBody().asString();
        assertEquals("{\"message\":\"success\"}", returnString3);


        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @Order(2)
    public void getAllUsersTest()
    {
        try {

            Response response2 = RestAssured.given().
                    when().
                    get("/users");

            String returnString2 = response2.getBody().asString();
            assertEquals("[{\"id\":1,\"firstName\":\"Jerry\",\"lastName\":\"Cook\",\"emailAddress\":\"jerrycook@gmail.com\",\"password\":\"baseball321\",\"classType\":1},{\"id\":2,\"firstName\":\"Jeremy\",\"lastName\":\"Baker\",\"emailAddress\":\"jeremybaker@gmail.com\",\"password\":\"golf123\",\"classType\":2},{\"id\":3,\"firstName\":\"Johnny\",\"lastName\":\"Chef\",\"emailAddress\":\"johnnychef@gmail.com\",\"password\":\"soccer231\",\"classType\":3}]", returnString2);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    @Test
    @Order(3)
    public void getUserByIdTest()
    {
        try {

            Response response = RestAssured.given().
                    when().
                    get("/users/1");

            String returnString = response.getBody().asString();
            assertEquals("{\"id\":1,\"firstName\":\"Jerry\",\"lastName\":\"Cook\",\"emailAddress\":\"jerrycook@gmail.com\",\"password\":\"baseball321\",\"classType\":1}", returnString);
        } catch(Exception e)
        {
            e.printStackTrace();
        }

    }

    @Test
    @Order(4)
    public void getAthleteTest()
    {
        try
        {
            Response response = RestAssured.given().
                    when().
                    get("/users/2/athlete");

            Response response2 = RestAssured.given().
                    when().
                    get("/users/1/athlete");

            String returnString = response.getBody().asString();
            assertEquals("", returnString);

            String returnString2 = response2.getBody().asString();
            assertEquals("{\"id\":1,\"user\":{\"id\":1,\"firstName\":\"Jerry\",\"lastName\":\"Cook\",\"emailAddress\":\"jerrycook@gmail.com\",\"password\":\"baseball321\",\"classType\":1},\"workoutList\":[]}", returnString2);

        } catch(Exception e)
        {
            e.printStackTrace();
        }

    }

    @Test
    @Order(5)
    public void getAthleteWorkoutListTest()
    {
        try
        {
            Response response = RestAssured.given().
                    when().
                    get("/users/4/athlete/workouts");
            String returnString = response.getBody().asString();

            Response response2 = RestAssured.given().
                    when().
                    get("/users/3/athlete/workouts");
            String returnString2 = response2.getBody().asString();

            Response response3 = RestAssured.given().when().put("/managers/1/coaches/1/athletes/1");
            String returnString3 = response3.getBody().asString();

            Response response4 = RestAssured.given().header("Content-Type", "application/json").
                    header("charset", "utf-8").
                    body(new JSONObject("{\n" +
                            "    \"sets\" : 10, \n" +
                            "    \"reps\" : 15,\n" +
                            "    \"duration\" : \"10 mins\",\n" +
                            "    \"rest\" : \"2 mins\",\n" +
                            "    \"exercise\" : { \"exerciseName\" : \"Push-ups\"}\n" +
                            "}").toString()).
                    when().
                    post("/coaches/1/workouts");
            String returnString4 = response4.getBody().asString();

            Response response5 = RestAssured.given().when().put("/coaches/1/athletes/1/workouts/1");
            String returnString5 = response5.getBody().asString();

            Response response6 = RestAssured.given().when().get("users/1/athlete/workouts");
            String returnString6 = response6.getBody().asString();

            assertEquals("", returnString);

            assertEquals("", returnString2);

            assertEquals("{\"message\":\"success\"}", returnString3);

            assertEquals("{\"message\":\"success\"}", returnString4);

            assertEquals("[{\"id\":1,\"sets\":10,\"reps\":15,\"duration\":\"10 mins\",\"rest\":\"2 mins\",\"video\":null,\"exercise\":{\"id\":1,\"exerciseName\":\"Push-ups\",\"hibernateLazyInitializer\":{}}}]", returnString5);

            assertEquals("[{\"id\":1,\"sets\":10,\"reps\":15,\"duration\":\"10 mins\",\"rest\":\"2 mins\",\"video\":null,\"exercise\":{\"id\":1,\"exerciseName\":\"Push-ups\",\"hibernateLazyInitializer\":{}}}]", returnString6);


        } catch(Exception e){
            e.printStackTrace();
        }
    }

    @Test
    @Order(6)
    public void getCoachTest()
    {
        try
        {
            Response response = RestAssured.given().
                    when().
                    get("/users/1/coach");

            Response response2 = RestAssured.given().
                    when().
                    get("/users/2/coach");

            String returnString = response.getBody().asString();
            String returnString2 = response2.getBody().asString();

            assertEquals("", returnString);

            assertEquals("{\"id\":1,\"user\":{\"id\":2,\"firstName\":\"Jeremy\",\"lastName\":\"Baker\",\"emailAddress\":\"jeremybaker@gmail.com\",\"password\":\"golf123\",\"classType\":2}}",returnString2);

        } catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @Test
    @Order(7)
    public void getCoachAthleteList()
    {
        try
        {
            Response response = RestAssured.given().
                    when().
                    get("/users/1/coach/athletes");

            Response response2 = RestAssured.given().
                    when().
                    get("/users/4/coach/athletes");

            Response response3 = RestAssured.given().
                    when().
                    get("/users/2/coach/athletes");

            String returnString = response.getBody().asString();
            String returnString2 = response2.getBody().asString();
            String returnString3 = response3.getBody().asString();

            assertEquals("", returnString);

            assertEquals("", returnString2);

            assertEquals("[{\"id\":1,\"user\":{\"id\":1,\"firstName\":\"Jerry\",\"lastName\":\"Cook\",\"emailAddress\":\"jerrycook@gmail.com\",\"password\":\"baseball321\",\"classType\":1},\"workoutList\":[{\"id\":1,\"sets\":10,\"reps\":15,\"duration\":\"10 mins\",\"rest\":\"2 mins\",\"video\":null,\"exercise\":{\"id\":1,\"exerciseName\":\"Push-ups\",\"hibernateLazyInitializer\":{}}}]}]", returnString3);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Test
    @Order(8)
    public void getManagerTest()
    {
        try{
            Response response = RestAssured.given().
                    when().
                    get("/users/4/manager");

            Response response2 = RestAssured.given().
                    when().
                    get("/users/3/manager");

            String returnString = response.getBody().asString();
            String returnString2 = response2.getBody().asString();

            assertEquals("", returnString);

            assertEquals("{\"id\":1,\"user\":{\"id\":3,\"firstName\":\"Johnny\",\"lastName\":\"Chef\",\"emailAddress\":\"johnnychef@gmail.com\",\"password\":\"soccer231\",\"classType\":3}}", returnString2);

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Test
    @Order(9)
    public void updateUserTest()
    {
        try{

            Response response2 = RestAssured.given().header("Content-Type", "application/json").
                    header("charset", "utf-8").
                    body(new JSONObject("{\n" +
                            "        \"id\": 1,\n" +
                            "        \"firstName\": \"Zane\",\n" +
                            "        \"lastName\": \"Eason\",\n" +
                            "        \"emailAddress\": \"zseason@iastate.edu\",\n" +
                            "        \"password\": \"123\",\n" +
                            "        \"classType\": 3\n" +
                            "    }").toString()).
                    when().
                    put("/users/3");

            String returnString2 = response2.getBody().asString();

            assertEquals("{\"id\":3,\"firstName\":\"Zane\",\"lastName\":\"Eason\",\"emailAddress\":\"zseason@iastate.edu\",\"password\":\"123\",\"classType\":3}", returnString2);

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Test
    @Order(10)
    public void getClassIDByUserIdTest()
    {
        try{
            Response response = RestAssured.given().
                    when().
                    get("/users/1/classID");

            Response response2 = RestAssured.given().
                    when().
                    get("/users/2/classID");

            Response response3 = RestAssured.given().
                    when().
                    get("/users/3/classID");

            Response response4 = RestAssured.given().
                    when().
                    get("/users/4/classID");

            String returnString = response.getBody().asString();
            String returnString2 = response2.getBody().asString();
            String returnString3 = response3.getBody().asString();
            String returnString4 = response4.getBody().asString();

            assertEquals("1", returnString);
            assertEquals("1", returnString2);
            assertEquals("1", returnString3);
            assertEquals("0", returnString4);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    @Order(11)
    public void deleteUserTest()
    {
        try{
            Response response = RestAssured.given().
                    when().
                    delete("/users/3");

            String returnString = response.getBody().asString();

            assertEquals("{\"message\":\"success\"}", returnString);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    @Order(12)
    public void getUserByEmailTest()
    {
        try{
            Response response = RestAssured.given().
                    when().
                    get("/users/email/joe");

            Response response2 = RestAssured.given().
                    when().
                    get("/users/email/jerrycook@gmail.com");

            String returnString = response.getBody().asString();
            String returnString2 = response2.getBody().asString();

            assertEquals("", returnString);
            assertEquals("{\"id\":1,\"firstName\":\"Jerry\",\"lastName\":\"Cook\",\"emailAddress\":\"jerrycook@gmail.com\",\"password\":\"baseball321\",\"classType\":1}", returnString2);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
