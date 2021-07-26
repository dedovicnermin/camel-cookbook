package tech.nermindedovic.propertiesdemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component("demo-component")
@Slf4j
public class DemoComponent {

    public String process(String message) {
        String output = message + message;
        log.info(output);
        return output;
    }



}
