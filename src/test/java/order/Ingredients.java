package order;

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

    public static List<String> getRandomIngredientsList() {
        return Stream
                .generate(() -> RandomStringUtils.randomAlphanumeric(24))
                .limit(5).collect(Collectors.toList());
    }
}
