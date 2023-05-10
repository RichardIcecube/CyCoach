package coms309;

import static org.junit.jupiter.api.Assertions.assertEquals;

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


@SpringJUnitConfig(Application.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class ManagerTesting {

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
    public void getManagersTest()
    {
        try {
            RestAssured.given().header("Content-Type", "application/json").
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

            RestAssured.given().header("Content-Type", "application/json").
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

            Response response = RestAssured.when().get("/managers");

            Response response1 = RestAssured.when().get("/managers/1");

            String returnString = response.getBody().asString();
            String returnString2 = response1.getBody().asString();

            assertEquals("[{\"id\":1,\"user\":{\"id\":3,\"firstName\":\"Johnny\",\"lastName\":\"Chef\",\"emailAddress\":\"johnnychef@gmail.com\",\"password\":\"soccer231\",\"classType\":3}}]", returnString);

            assertEquals("{\"id\":1,\"user\":{\"id\":3,\"firstName\":\"Johnny\",\"lastName\":\"Chef\",\"emailAddress\":\"johnnychef@gmail.com\",\"password\":\"soccer231\",\"classType\":3}}", returnString2);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    @Order(2)
    public void getAllUsersTest()
    {
        try{
            Response response = RestAssured.when().get("/managers/1/users");

            String returnString = response.getBody().asString();

            assertEquals("[{\"id\":1,\"firstName\":\"Jerry\",\"lastName\":\"Cook\",\"emailAddress\":\"jerrycook@gmail.com\",\"password\":\"baseball321\",\"classType\":1},{\"id\":2,\"firstName\":\"Jeremy\",\"lastName\":\"Baker\",\"emailAddress\":\"jeremybaker@gmail.com\",\"password\":\"golf123\",\"classType\":2},{\"id\":3,\"firstName\":\"Johnny\",\"lastName\":\"Chef\",\"emailAddress\":\"johnnychef@gmail.com\",\"password\":\"soccer231\",\"classType\":3}]",returnString);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    @Order(3)
    public void getAllCoachesTest()
    {
        try{
            Response response = RestAssured.when().get("/managers/1/coaches");

            String returnString = response.getBody().asString();

            assertEquals("[{\"id\":1,\"user\":{\"id\":2,\"firstName\":\"Jeremy\",\"lastName\":\"Baker\",\"emailAddress\":\"jeremybaker@gmail.com\",\"password\":\"golf123\",\"classType\":2}}]", returnString);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    @Order(4)
    public void getAllAthletesTest()
    {
        try{
            Response response = RestAssured.when().get("/managers/1/athletes");

            String returnString = response.getBody().asString();

            assertEquals("[{\"id\":1,\"user\":{\"id\":1,\"firstName\":\"Jerry\",\"lastName\":\"Cook\",\"emailAddress\":\"jerrycook@gmail.com\",\"password\":\"baseball321\",\"classType\":1},\"workoutList\":[]}]", returnString);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    @Order(5)
    public void linkAndUnlinkCoachTest()
    {
        try {
            Response response = RestAssured.when().put("/managers/1/coaches/1/unlink");

            Response response2 = RestAssured.when().put("/managers/1/coaches/1");

            Response response3 = RestAssured.when().put("/managers/1/coaches/1");

            Response response4 = RestAssured.when().put("/managers/1/coaches/1/unlink");

            RestAssured.when().put("/managers/1/coaches/1");

            String returnString = response.getBody().asString();
            String returnString2 = response2.getBody().asString();
            String returnString3 = response3.getBody().asString();
            String returnString4 = response4.getBody().asString();

            assertEquals("{\"message\":\"failure\"}" + "\nCoach is not assigned to a manager.", returnString);

            assertEquals("{\"message\":\"success\"}", returnString2);

            assertEquals("{\"message\":\"failure\"}" + "\n Coach is already assigned to this manager.", returnString3);

            assertEquals("{\"message\":\"success\"}", returnString4);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    @Order(6)
    public void linkAndUnlinkAthleteTest()
    {
        try{
            Response response = RestAssured.when().put("/managers/1/athletes/1/unlink");

            Response response2 = RestAssured.when().put("/managers/1/coaches/1/athletes/1");

            Response response3 = RestAssured.when().put("/managers/1/coaches/1/athletes/1");

            Response response4 = RestAssured.when().put("/managers/1/athletes/1/unlink");

            RestAssured.when().put("/managers/1/coaches/1/athletes/1");

            String returnString = response.getBody().asString();
            String returnString2 = response2.getBody().asString();
            String returnString3 = response3.getBody().asString();
            String returnString4 = response4.getBody().asString();

            assertEquals( "{\"message\":\"failure\"}" + "\n Athlete is not assigned to a coach", returnString);

            assertEquals("{\"message\":\"success\"}", returnString2);

            assertEquals("{\"message\":\"failure\"}" + "\n Athlete is already assigned to this coach.", returnString3);

            assertEquals("{\"message\":\"success\"}", returnString4);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    @Order(7)
    public void getRelationalsTests()
    {
        try {
            Response response = RestAssured.when().get("/managers/1/coaches/1/athletes");

            Response response2 = RestAssured.when().get("/managers/1/coachesList");



            String returnString = response.getBody().asString();
            String returnString2 = response2.getBody().asString();


            assertEquals("[{\"id\":1,\"user\":{\"id\":1,\"firstName\":\"Jerry\",\"lastName\":\"Cook\",\"emailAddress\":\"jerrycook@gmail.com\",\"password\":\"baseball321\",\"classType\":1},\"workoutList\":[]}]", returnString);

            assertEquals("[{\"id\":1,\"user\":{\"id\":2,\"firstName\":\"Jeremy\",\"lastName\":\"Baker\",\"emailAddress\":\"jeremybaker@gmail.com\",\"password\":\"golf123\",\"classType\":2}}]",returnString2);



        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    @Order(8)
    public void deleteUserTest()
    {
        try{
            Response response3 = RestAssured.when().delete("/managers/1/users/1");
            String returnString3 = response3.getBody().asString();
            assertEquals("{\"message\":\"success\"}", returnString3);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
