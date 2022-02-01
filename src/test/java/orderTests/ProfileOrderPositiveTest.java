package orderTests;

import clients.AuthClient;
import clients.IngredientClient;
import clients.OrderClient;
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

import static org.junit.runners.Parameterized.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class ProfileOrderPositiveTest {

    private ProfileOrders profileOrders;
    private AuthClient authClient;
    private ProfileDirector profileDirector;
    private ProfileBuilder profileBuilder;
    private OrderClient orderClient;
    private Profile profile;
    private String accessToken;
    private List<String> allIngredients;

    @Before
    public void setUp() {
        authClient = new AuthClient();
        profileDirector = new ProfileDirector();
        profileBuilder = new ProfileBuilder();
        orderClient = new OrderClient();

        // Создание, регистрация профиля. Получение токена профиля для авторизации
        profileDirector.buildProfile(profileBuilder, ProfileType.FULL);
        profile = profileBuilder.getResult();
        accessToken = authClient.registerProfileResponse(profile).
                then().assertThat().statusCode(200).
                extract().path("accessToken");

        // Получение всех ингридиентов
        allIngredients = IngredientClient.getIngredientsList();

        // Создание объекта для связки профиль-заказ
        profileOrders = new ProfileOrders(profile, new ArrayList<Order>());
    }

    @Parameter
    public int ordersAmount;

    @Parameter(1)
    public int expectedResponseCode;

    @Parameter(2)
    public boolean isResponseSuccessful;

    @Parameters
    public static Object[][] setUpParameters() {
        return new Object[][] {
                {1, 200, true},
                {25, 200, true},
                {50, 200, true},
                {52, 200, true}
        };
    }

    @Test
    public void getOrdersOfAuthorizedProfile() {
        // Добавление заказов для профиля
        for (int i = 0; i < ordersAmount; i++) {
            int maxIngredientIndex = ThreadLocalRandom.current().nextInt(1, allIngredients.size());
            Ingredients ingredients = new Ingredients(allIngredients.subList(0, maxIngredientIndex));
            orderClient.createOrder(profileOrders, ingredients, accessToken);
        }

        Response getProfileOrdersResponse = orderClient.getProfileOrders(accessToken);
        ValidatableResponse validatableResponse = getProfileOrdersResponse.
                then().assertThat().statusCode(expectedResponseCode);
        validatableResponse.assertThat().body("success", is(isResponseSuccessful));

        List<Order> expectedOrders = ordersAmount < 50 ? profileOrders.getOrders() :
                profileOrders.getOrders().subList(0, 50);
        List<LinkedHashMap> actualOrders = validatableResponse.extract().path("orders");

        // Проверка что ожидаемое количество заказов и полученное количество заказов равно
        assertEquals("Size of expected orders list and actual orders list is different",
                expectedOrders.size(), actualOrders.size());

        for (int i = 0; i < actualOrders.size(); i++) {
            Order expectedOrder = expectedOrders.get(i);

            Integer actualOrderNumber = (Integer)actualOrders.get(i).get("number");
            assertEquals(expectedOrder.getNumber(), actualOrderNumber);

            List<String> actualOrderIngredients = (List<String>)actualOrders.get(i).get("ingredients");
            assertEquals(expectedOrder.getIngredients(), actualOrderIngredients);
        }

        validatableResponse.assertThat().body("total", equalTo(ordersAmount));
        validatableResponse.assertThat().body("totalToday", equalTo(ordersAmount));

    }
}
