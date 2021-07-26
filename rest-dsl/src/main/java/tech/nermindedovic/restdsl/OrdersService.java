package tech.nermindedovic.restdsl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tech.nermindedovic.restdsl.dto.Order;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class OrdersService {

    private final List<Order> orders = new ArrayList<>();

    @PostConstruct
    private void initDB() {
        orders.add(new Order(67, "Phone", 5000));
        orders.add(new Order(234, "Book", 23.99));
        orders.add(new Order(296, "AC", 1500));
        orders.add(new Order(296, "J's", 4000));
    }

    public Order addOrder(Order order) {
        log.info(order.toString());
        orders.add(order);
        return order;
    }

    public String getOrders() {
        log.info(orders + "");
        return orders.toString();
    }

}
