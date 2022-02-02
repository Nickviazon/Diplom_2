package order;

import io.qameta.allure.Step;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Ingredients {
    private final List<String> ingredients;

    public Ingredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    @Step("Create ingredients list with random hash")
    public static List<String> getRandomIngredients() {
        return Stream
                .generate(() -> RandomStringUtils.randomAlphanumeric(24))
                .limit(5).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return String.format("Ingredients(ingredients=%s)", ingredients);
    }
}
