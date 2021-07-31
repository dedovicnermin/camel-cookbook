package tech.nermindedovic.camelkafkademo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderProcessor {

    public Order process(Order order) {
        log.info(order.toString());
        order.setProcessed(true);
        return order;
    }

}
