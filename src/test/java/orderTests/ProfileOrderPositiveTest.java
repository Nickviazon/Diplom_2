package orderTests;

import clients.AuthClient;
import clients.IngredientClient;
import clients.OrderClient;
import io.qameta.allure.Flaky;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import order.Ingredients;
import order.Order;
import order.ProfileOrders;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import profile.Profile;
import profile.ProfileBuilder;
import profile.ProfileDirector;
import profile.ProfileType;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.Assert.assertTrue;
import static org.junit.runners.Parameterized.*;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class ProfileOrderPositiveTest {

    public ProfileOrders profileOrders;
    public OrderClient orderClient;
    public String accessToken;
    public List<String> allIngredients;

    @Before
    @Step("Init clients, create and register profile, get all ingredients. create profileOrder")
    public void setUp() {
        AuthClient authClient = new AuthClient();
        orderClient = new OrderClient();

        // Создание, регистрация профиля. Получение токена профиля для авторизации
        Profile profile = createNewProfile();
        accessToken = authClient.registerProfileResponse(profile).
                then().assertThat().statusCode(200).
                extract().path("accessToken");

        // Получение всех ингридиентов
        allIngredients = IngredientClient.getIngredientsList();

        // Создание объекта для связки профиль-заказ
        profileOrders = new ProfileOrders(profile, new ArrayList<Order>());
    }

    @Parameter
    public int expectedOrdersAmount;

    @Parameters(name="Get orders of authorized profile returns code 200 with the first 50 orders: orders amount = {0}")
    public static Object[][] setUpParameters() {
        return new Object[][] {
                {1},
                {25},
                {50},
                {52}
        };
    }

    @Flaky
    @Test
    public void getOrdersOfAuthorizedProfileReturns200WithFirst50Orders() {
        // Добавление заказов для профиля
        fillProfileWithOrders(expectedOrdersAmount);

        ValidatableResponse validatableResponse = getProfileOrders(accessToken);
        assertTrue("Response is unsuccessful", validatableResponse.extract().path("success"));

        List<Order> expectedOrders = getExpectedOrders();
        List<LinkedHashMap> actualOrders = validatableResponse.extract().path("orders");

        // Проверка что ожидаемое количество заказов и полученное количество заказов равно
        assertEquals("Size of expected orders list and actual orders list is different",
                expectedOrders.size(), actualOrders.size());

        // Проверка полученных заказов
        checkOrders(expectedOrders, actualOrders);

        int actualOrdersAmount = validatableResponse.extract().path("total");
        assertEquals("Actual orders amount is different from expected", actualOrdersAmount, expectedOrdersAmount);

        int actualOrdersAmountToday = validatableResponse.extract().path("totalToday");
        assertEquals("Actual orders amount today is different from expected", actualOrdersAmountToday, expectedOrdersAmount);
    }

    private ValidatableResponse getProfileOrders( String accessToken) {
        return orderClient.getProfileOrders(accessToken).then().assertThat().statusCode(200);
    }

    private Profile createNewProfile() {
        ProfileDirector profileDirector = new ProfileDirector();
        ProfileBuilder profileBuilder = new ProfileBuilder();
        profileDirector.buildProfile(profileBuilder, ProfileType.FULL);
        return profileBuilder.getResult();
    }

    private void fillProfileWithOrders(int expectedOrdersAmount) {
        for (int i = 0; i < expectedOrdersAmount; i++) {
            int maxIngredientIndex = ThreadLocalRandom.current().nextInt(1, allIngredients.size());
            Ingredients ingredients = new Ingredients(allIngredients.subList(0, maxIngredientIndex));
            orderClient.createOrder(profileOrders, ingredients, accessToken);
        }
    }

    private List<Order> getExpectedOrders() {
        return (expectedOrdersAmount < 50) ? profileOrders.getOrders() : profileOrders.getOrders().subList(0, 50);
    }

    private void checkOrders(List<Order> expectedOrders, List<LinkedHashMap>actualOrders) {
        for (int i = 0; i < actualOrders.size(); i++) {
            Order expectedOrder = expectedOrders.get(i);

            Integer actualOrderNumber = (Integer)actualOrders.get(i).get("number");
            assertEquals("Actual order number is different from expected", expectedOrder.getNumber(), actualOrderNumber);

            List<String> actualOrderIngredients = (List<String>)actualOrders.get(i).get("ingredients");
            assertEquals("Actual order ingredients is different from expected", expectedOrder.getIngredients(), actualOrderIngredients);
        }
    }

}
