package tech.nermindedovic.multicastdemo;

import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.junit5.CamelSpringTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration
@CamelSpringTest
class DemoComponentTest {

    @EndpointInject("mock:end")
    protected MockEndpoint endpoint;

    @EndpointInject("mock:firstService")
    protected MockEndpoint firstServiceEndpoint;

    @EndpointInject("mock:secondService")
    protected MockEndpoint secondServiceEndpoint;

    @EndpointInject("mock:thirdService")
    protected MockEndpoint thirdServiceEndpoint;

    @Produce("direct:start")
    protected ProducerTemplate template;


    @Test
    void testPojo() throws InterruptedException {
        StockTick input = new StockTick("AAPL", 2222.22, false);
        StockTick expected = new StockTick("AAPL", 2222.22, true);

        endpoint.expectedBodiesReceived(expected);
        firstServiceEndpoint.expectedBodiesReceived(input);
        secondServiceEndpoint.expectedBodiesReceived(input);
        thirdServiceEndpoint.expectedBodiesReceived(input);

        template.sendBody(input);

        endpoint.assertIsSatisfied();
        firstServiceEndpoint.assertIsSatisfied();
        secondServiceEndpoint.assertIsSatisfied();
        thirdServiceEndpoint.assertIsSatisfied();
    }






}