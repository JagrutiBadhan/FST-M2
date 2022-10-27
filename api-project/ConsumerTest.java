package liveProject;

import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

@ExtendWith(PactConsumerTestExt.class)
public class ConsumerTest {
        //Create headers
    Map<String,String> headers=new HashMap<>();
    //Set the resource path
    String resourcePath="/api/users";

    //Create contract
    @Pact(consumer="UserConsumer", provider="UserProvider")
    public RequestResponsePact createPact(PactDslWithProvider builder){
        //set the headers
        headers.put("Content-Type","application/json");
    //Create body
    DslPart requestResponseBody=new PactDslJsonBody()
            .numberType("id",123)
            .stringType("firstName","Jagruti")
            .stringType("lastName","Badhan")
            .stringType("email","abc@gmail.com");

    //record interact to pact
    return builder.given("A request to create a user")
            .uponReceiving("A request to create a user")
            .method("POST")
            .path(resourcePath)
            .headers(headers)
            .body(requestResponseBody)
            .willRespondWith()
            .status(201)
            .body(requestResponseBody)
            .toPact();

    }

    @Test
    @PactTestFor(providerName = "UserProvider",port = "8282")
    public void consumerTest(){
        //base URI
        String baseURI="http://localhost:8282"+resourcePath;
        //Request body
        Map<String,Object>requestBody=new HashMap<>();
        requestBody.put("id",123);
        requestBody.put("firstName","Jagruti");
        requestBody.put("lastName","Badhan");
        requestBody.put("email","abc@gmail.com");

        //Generate response
        given().headers(headers).body(requestBody).log().all()
                .when().post(baseURI)
                .then().statusCode(201).log().all();

    }

}
