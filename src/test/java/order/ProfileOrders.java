package order;

import profile.Profile;

import java.util.List;

public class ProfileOrders {

    private final Profile profile;
    private final List<Order> orders;

    public ProfileOrders(Profile profile, List<Order> orders) {
        this.profile = profile;
        this.orders = orders;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void addOrder(Order order) {
        orders.add(order);
    }
}
