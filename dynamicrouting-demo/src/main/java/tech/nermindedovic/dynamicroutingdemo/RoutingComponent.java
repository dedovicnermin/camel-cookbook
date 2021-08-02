package tech.nermindedovic.dynamicroutingdemo;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Headers;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class RoutingComponent {

    public String process(@Headers Map<String, Object> properties) {
        int invoked = 0;
        Object current = properties.get("invoked");
        if (current != null) {
            invoked = Integer.parseInt(current.toString());
        }
        invoked++;
        properties.put("invoked", invoked);
        log.info(invoked + "");

        switch (invoked) {
            case 1:
                return "mock:a";
            case 2:
                return "mock:b,mock:c";
            case 3:
                return "mock:other";
            case 4:
                return "mock:result";
            default:
                return null;
        }
    }

}
