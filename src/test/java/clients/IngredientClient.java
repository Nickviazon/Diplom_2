package clients;

import io.qameta.allure.Step;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.List;

public class IngredientClient extends RestAssuredClient {

    private static final String PATH = "api/ingredients";

    @Step("Get the ingredients list from the server")
    public static List<String> getIngredientsList() {
        RequestSpecification specification = getBaseSpec();
        Method requestType = Method.GET;
        Response response = getResponse(specification, requestType, PATH, null);
        return response.then().assertThat().statusCode(200).extract().path("data._id");
    }
}
