package tech.nermindedovic.reqresproutingdemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DemoComponent {

    public String process(String message) {
        return message + " " + message.length();
    }

}
