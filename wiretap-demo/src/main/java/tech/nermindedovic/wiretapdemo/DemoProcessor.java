package tech.nermindedovic.wiretapdemo;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Body;
import org.apache.camel.Header;
import org.springframework.stereotype.Component;

@Component("demo-processor")
@Slf4j
public class DemoProcessor {

    public String process(@Header("slaps") int slapCount, @Body String slapMessage) {
        String output = String.format("Slap count : %d %nSlap message : %s%n", slapCount, slapMessage);
        log.info(output);
        return output;
    }


    public String processToUpper(String body) {
        log.info(body);
        return body.toUpperCase();
    }

}
