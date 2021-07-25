package tech.nermindedovic.restdsl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component("processor-component")
@Slf4j
public class ProcessorComponent {

    public String greeting() {
        log.info("@GREETINGS");
        return "Greetings good sir!";
    }

}
