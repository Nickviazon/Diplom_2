package clients;

import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import order.Ingredients;
import order.Order;
import order.ProfileOrders;

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

    public void createOrder(ProfileOrders profileOrders, Ingredients ingredients, String accessToken) {
        Response orderResponse = createOrderResponse(ingredients, accessToken);
        while (orderResponse.statusCode() == 429) {
            orderResponse = createOrderResponse(ingredients, accessToken);
        }
        ValidatableResponse validatableOrderResponse = orderResponse.then().assertThat().statusCode(200);
        Integer orderNumber = validatableOrderResponse.extract().path("order.number");
        Order createdOrder = new Order(orderNumber, ingredients.getIngredients());
        profileOrders.addOrder(createdOrder);
    }

    public Response getProfileOrders(String accessToken) {
        RequestSpecification specification = getBaseSpec();
        Method requestType = Method.GET;
        Object requestBody = null;
        if (accessToken != null) {
            specification.given().header("Authorization", accessToken);
        }
        return getResponse(specification, requestType, PATH, requestBody);
    }
}
