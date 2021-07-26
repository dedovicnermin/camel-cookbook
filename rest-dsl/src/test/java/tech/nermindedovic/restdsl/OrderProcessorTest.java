package tech.nermindedovic.restdsl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import tech.nermindedovic.restdsl.dto.Order;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderProcessorTest {

    @Autowired
    TestRestTemplate template;

    private List<Order> orders = new ArrayList<>();

    @BeforeEach
    void populateOrders() {
        orders = new ArrayList<>();
        orders.add(new Order(67, "Phone", 5000));
        orders.add(new Order(234, "Book", 23.99));
        orders.add(new Order(296, "AC", 1500));
        orders.add(new Order(296, "J's", 4000));
    }

    @Test
    void whenRetrievingOrders_returnsListOfOrdersSuccessfully() {
        ResponseEntity<String> exchange = template.exchange(RequestEntity.get("/getOrders").build(), String.class);
        String body = exchange.getBody();
        assertThat(body).isEqualTo(orders.toString());
    }


    @Test
    void whenAddingAnOrder_willAddOrderToList() {
        Order test = new Order(21223, "test", 33.33);
        orders.add(test);
        template.exchange(RequestEntity.post("/addOrder").body(test), Order.class);

        ResponseEntity<String> exchange = template.exchange(RequestEntity.get("/getOrders").build(), String.class);
        String body = exchange.getBody();
        assertThat(body).isEqualTo(orders.toString());


    }

}