package orderTests;

import clients.OrderClient;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import order.Ingredients;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.runners.Parameterized.*;

@RunWith(Parameterized.class)
public class OrderNegativeTest {

    private static OrderClient orderClient;

    @Parameter
    public List<String> ingredientsList;

    @Parameter(1)
    public int responseCode;

    @Parameter(2)
    public Boolean isRequestSuccessful;

    @Parameter(3)
    public String expectedMessage;

    @Parameters
    public static Object[][] setUpParameters() {
        orderClient = new OrderClient();
        List<String> randomIngredientsHash = Ingredients.getRandomIngredients();

        return new Object[][] {
                {emptyList(), 400, false, "Ingredient ids must be provided"}, // проверка если передали пустой список ингредиентов
                {randomIngredientsHash, 500, null, null} // проверка если передали некорректные хеши ингридиентов
        };
    }

    @Test
    public void createOrderWithEmptyListOrIncorrectIngredients() {
        Ingredients ingredients = new Ingredients(ingredientsList);
        String accessToken = null;

        Response orderCreationResponse = orderClient.createOrderResponse(ingredients, accessToken);
        ValidatableResponse validatableResponse = orderCreationResponse.then().assertThat().statusCode(responseCode);
        if (isRequestSuccessful != null) {
            validatableResponse.assertThat().body("success", is(isRequestSuccessful));
        }

        if (expectedMessage != null) {
            validatableResponse.assertThat().body("message", equalTo(expectedMessage));
        }
    }
}
