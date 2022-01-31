package clients;

import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import order.Ingredients;

import java.util.List;

public class OrderClient extends RestAssuredClient{
    private static final String PATH = "api/orders";

    public Response createOrderResponse(Ingredients ingredients, String accessToken) {
        RequestSpecification specification = getBaseSpec();
        Method requestType = Method.POST;
        if (accessToken != null) {
            specification.given().header("Authorization", accessToken);
        }
        return getResponse(specification, requestType, PATH, ingredients);
    }
}
