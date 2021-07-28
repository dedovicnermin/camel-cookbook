package tech.nermindedovic.multicastdemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DemoComponent {

    public StockTick process(StockTick stock) {
        log.info(stock.toString() + "<--- here");
        stock.setProcessed(true);
        return stock;
    }


}
