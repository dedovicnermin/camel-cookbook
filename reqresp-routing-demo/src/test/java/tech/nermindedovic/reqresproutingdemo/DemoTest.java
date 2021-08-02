package tech.nermindedovic.reqresproutingdemo;

import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.junit5.CamelSpringTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;

@CamelSpringTest
@ContextConfiguration
class DemoTest {

    @Produce("direct:in")
    protected ProducerTemplate producer;

    @EndpointInject("mock:afterMessageModified")
    protected MockEndpoint endpoint;

    @Test
    void givenMessage_willWaitForResponse_andReturnModifiedMessage() throws InterruptedException {
        String input = "Whether the weather be fine or whether the weather be not, we'll weather the weather whatever the weather, whether we like it or not";
        String expectedReturn = input + " " + input.length();

        endpoint.expectedBodiesReceived(expectedReturn);
        endpoint.expectedMessageCount(1);
        producer.sendBody(input);
        endpoint.assertIsSatisfied();
    }

}
