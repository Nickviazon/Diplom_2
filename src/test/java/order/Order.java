package order;

import java.util.List;

public class Order {
    private final Integer number;
    private final List<String> ingredients;

    public Order(Integer number, List<String> ingredients) {
        this.number = number;
        this.ingredients = ingredients;
    }

    public Integer getNumber() {
        return number;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    @Override
    public String toString() {
        return String.format("Order(number=%s, ingredients=%s)", number, ingredients);
    }
}
