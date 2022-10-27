package activities;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;

public class Activity3 {
    //Request and response specification
    RequestSpecification requestSpecification;
    ResponseSpecification responseSpecification;

    @BeforeClass
    public void setUp(){
        //Request specification
        requestSpecification= new RequestSpecBuilder().
                setBaseUri("https://petstore.swagger.io/v2/pet").
               setContentType(ContentType.JSON).build();

        //Response specification
        responseSpecification=new ResponseSpecBuilder().
                expectResponseTime(lessThan(4000L)).
                expectStatusCode(200).expectContentType("application/json").build();


    }
    @DataProvider
    public Object[][] petInfoProvider() {
        // Setting parameters to pass to test case
        Object[][] testData = new Object[][] {
                { 79900, "Niko", "alive" },
                { 79902, "Teddy", "alive" }
        };
        return testData;
    }

    @Test(priority = 1)
    public void postRequestTest(){
        //Request body
        Map<String,Object> reqbody=new HashMap<>();
        reqbody.put("id",79900);
        reqbody.put("name","Niko");
        reqbody.put("status","alive");
        Map<String,Object> reqbody1=new HashMap<>();
        reqbody1.put("id",79902);
        reqbody1.put("name","Teddy");
        reqbody1.put("status","alive");

        //generate response
        Response response=given().spec(requestSpecification).body(reqbody).when().post();
        System.out.println("Response : " +response.asPrettyString());

        Response response1=given().spec(requestSpecification).body(reqbody1).when().post();


        //Assertions
        response.then().spec(responseSpecification).body("status",equalTo("alive"));
        response1.then().spec(responseSpecification).body("status",equalTo("alive"));

    }

    @Test(dataProvider = "petInfoProvider",priority = 2)
    public void getRequestTest(int petId, String name, String status){
        //generate response and assert
        given().spec(requestSpecification).pathParam("petId",petId).
                when().get("/{petId}").then().spec(responseSpecification).body("status",equalTo("alive"));
    }

    @Test(dataProvider = "petInfoProvider",priority = 3)
    public void deleteRequestTest(int petId, String name, String status){
        given().spec(requestSpecification).pathParam("petId",petId).
                when().delete("/{petId}").then().spec(responseSpecification).body("message",equalTo(""+ petId));

    }
}
