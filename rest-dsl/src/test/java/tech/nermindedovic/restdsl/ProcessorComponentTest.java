package tech.nermindedovic.restdsl;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProcessorComponentTest{

    @Autowired
    TestRestTemplate template;

    @Test
    void greetings() {
        ResponseEntity<String> entity = template.exchange(RequestEntity.get("/greetings").build(), String.class);
        assertThat(entity.getBody()).isEqualTo("Greetings good sir!");
    }

}
