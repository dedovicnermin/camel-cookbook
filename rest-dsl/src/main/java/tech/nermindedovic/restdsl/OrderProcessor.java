package tech.nermindedovic.restdsl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;
import tech.nermindedovic.restdsl.dto.Order;

import java.util.List;

@Component("order-processor")
@RequiredArgsConstructor
@Slf4j
public class OrderProcessor implements Processor {

    private final OrdersService component;

    @Override
    public void process(Exchange exchange)  {
        log.info(exchange.getIn().getBody() + "");
        component.addOrder(exchange.getIn().getBody(Order.class));
    }

    public String getOrders() {
        return component.getOrders();
    }
}
