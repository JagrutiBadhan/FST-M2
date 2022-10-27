package activities;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class Activity1 {
    String baseURI = "https://petstore.swagger.io/v2/pet";
    int petId;
    @Test(priority = 1)
    public void postRequestTest(){
        //Request body
        Map<String,Object> reqbody=new HashMap<>();
        reqbody.put("id",779);
        reqbody.put("name","Niko");
        reqbody.put("status","alive");
        Response response=given().contentType(ContentType.JSON).body(reqbody).when().post(baseURI);
        System.out.println("Response:  " + response.getBody().asPrettyString());

        //Assertion
        response.then().body("id", equalTo(779));
        response.then().body("name",equalTo("Niko"));
        response.then().body("status",equalTo("alive"));
        petId=response.then().extract().path("id");

    }

    @Test(priority = 2)
    public void getRequestTest(){
        //generate response and assert
        //given().spec(requestSpecification).pathParam("petId",petId).log().all().
               // when().get("/{petId}").then().spec(responseSpecification).log().all().body("status", Matchers.equalTo("alive"));
        Response response=given().contentType(ContentType.JSON).pathParam("petId",petId)
                .when().get(baseURI + "/{petId}");
        System.out.println("Response GET Method:  " + response.getBody().asPrettyString());

        //Assertion
        response.then().body("id", equalTo(779));
        response.then().body("name",equalTo("Niko"));
        response.then().body("status",equalTo("alive"));
    }

    @Test(priority = 3)
    public void deleteRequestTest(){
        Response response=given().contentType(ContentType.JSON).pathParam("petId",petId)
                .when().delete(baseURI + "/{petId}");
        System.out.println("Response DELETE Method:  " + response.getBody().asPrettyString());

        //Assertion
        response.then().statusCode(200);
        response.then().body("message", equalTo(""+petId));

    }
}
