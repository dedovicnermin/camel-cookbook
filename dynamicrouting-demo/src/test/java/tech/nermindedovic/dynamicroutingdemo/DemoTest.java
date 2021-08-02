package tech.nermindedovic.dynamicroutingdemo;

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

    @Produce("direct:start")
    protected ProducerTemplate producer;

    @EndpointInject("mock:a")
    protected MockEndpoint endpointA;

    @EndpointInject("mock:b")
    protected MockEndpoint endpointB;

    @EndpointInject("mock:c")
    protected MockEndpoint endpointC;

    @EndpointInject("mock:other")
    protected MockEndpoint endpointOther;

    @EndpointInject("mock:result")
    protected MockEndpoint endpointResult;

    @Test
    void testDynamicRouter() throws InterruptedException {

        String input = "testing dynamic router";

        endpointA.expectedMessageCount(1);
        endpointB.expectedMessageCount(1);
        endpointC.expectedMessageCount(1);
        endpointOther.expectedMessageCount(1);
        endpointResult.expectedMessageCount(1);
        endpointResult.expectedBodiesReceived(input);
        endpointResult.expectedHeaderReceived("invoked", 4);

        producer.sendBody(input);

        endpointA.assertIsSatisfied();
        endpointB.assertIsSatisfied();
        endpointC.assertIsSatisfied();
        endpointOther.assertIsSatisfied();
        endpointResult.assertIsSatisfied();
    }

}
