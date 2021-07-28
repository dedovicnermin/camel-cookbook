package tech.nermindedovic.contentroutingdemo;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.*;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component("content-processor")
@Slf4j
public class ContentProcessor {

    public String process(@Body String message, @Headers Map<String, Object> props) {
        String dest = "destination";
        String route = props.get("route").toString();
        switch (route) {
            case "first":
                props.put(dest, 1);
                break;
            case "second":
                props.put(dest, 2);
                break;
            case "third":
                props.put(dest, 3);
                break;
            default:
                props.put(dest, "error");
                break;
        }
        return message;
    }

}
