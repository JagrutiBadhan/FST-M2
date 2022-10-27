package liveProject;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class GitHubProjectTest {
    RequestSpecification requestSpecification;
    String SSH_key="ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQDdkpTYt6bmlSvt0+jAZRw4DIT+hAVhs7sadHv/bLcH2TkmRM0FwbqPzcR7eahDR1sxYzToUPfhpg/higJww9jgzzJfN/p2bM+yS2SrvJlGBoJib3SG65xCUoxma791+IyVGQ3Pfw6D4Fh7ZAlh2CapxZYGZcnU6ZoEWCiYctRqBbye+RH8NWo4QItkqjo0Hk6K0VsWdZ9m9alOs9zwBOFroh62MY1GKsO3E0K/WuqD6TIsrQErQ3cX5LkK21+YRm5BXX+n4SXNKHHl2WLZ3LSQkvWY4gXhjYQIeilMZV7ID2x30/0PMgy3N52znyiNwUby7WbR2mZUjOaoUkrHUwGJ";
    int SSH_id;
    @BeforeClass
    public void setUp(){
        //Request specification
        //can use setcontentType on the place of addheader setContentType(ContentType.JSON)
        requestSpecification= new RequestSpecBuilder().
                setBaseUri("https://api.github.com").
                setContentType(ContentType.JSON).
                addHeader("Authorization","Bearer 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")
                .build();
    }
    @Test(priority = 1)
    public void postRequestTest(){
        //request body
        Map<String,Object> reqbody=new HashMap<>();
        reqbody.put("title","TestAPIkey");
        reqbody.put("key",SSH_key);

        Response response=given().spec(requestSpecification).log().all().body(reqbody).when().post("/user/keys");
        System.out.println("Response POST Request: " +response.asPrettyString());

        //fetch id
        SSH_id=response.then().extract().path("id");

        //assertions
        response.then().statusCode(201);
    }

    @Test(priority = 2)
    public void getRequestTest(){
            Response response=given().spec(requestSpecification).pathParam("SSH_id",SSH_id).when().get("/user/keys/{SSH_id}");

        System.out.println("Response GET Request: " +response.asPrettyString());

            //assertions
        response.then().statusCode(200);
    }

    @Test(priority = 3)
    public void deleteRequestTest(){
        Response response=given().spec(requestSpecification).pathParam("SSH_id",SSH_id).when().delete("/user/keys/{SSH_id}");

        System.out.println("Response DELETE Request: " +response.asPrettyString());

        //assertions
        response.then().statusCode(204);
    }


}
