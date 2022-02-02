package clients;

import io.qameta.allure.Step;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class RestAssuredClient {

    public static RequestSpecification getBaseSpec() {
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri("https://stellarburgers.nomoreparties.site/")
                .build();
    }

    @Step("Send {requestType} request")
    public static Response getResponse(
            RequestSpecification specification,
            Method requestType,
            String requestPath,
            Object requestBody) {
        RequestSpecification requestSpecification;
        if (requestBody != null) {
            requestSpecification = given().spec(specification).body(requestBody).when();
        } else {
            requestSpecification = given().spec(specification).when();
        }
        Response returnedResponse;
        switch (requestType) {
            case GET:
                returnedResponse = requestSpecification.get(requestPath);
                break;
            case POST:
                returnedResponse = requestSpecification.post(requestPath);
                break;
            case PUT:
                returnedResponse = requestSpecification.put(requestPath);
                break;
            case PATCH:
                returnedResponse = requestSpecification.patch(requestPath);
                break;
            case DELETE:
                returnedResponse =  requestSpecification.delete(requestPath);
                break;
            default:
                throw new IllegalArgumentException("Trying to call unsupported request type");
        }
        return returnedResponse;
    }
}
