package tech.nermindedovic.camelkafkademo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component("demo-processor")
@Slf4j
public class DemoProcessor {

    public String process(String message) {
        log.info("DP --> {}", message);
        return "Hi Kafka! - From Camel";

    }

}
