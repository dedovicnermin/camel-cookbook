package tech.nermindedovic.wiretapdemo;


import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.junit5.CamelSpringTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration
@CamelSpringTest
class DemoProcessorTest {

    // TODO : remove unused imports

    /* Tapped Mock Endpoint */
    @EndpointInject("mock:tapped")
    protected MockEndpoint tappedEndpoint;

    /* Route 1 Mock Endpoint */
    @EndpointInject("mock:finish")
    protected MockEndpoint endpoint;

    /* Route 2 Mock Endpoint */
    @EndpointInject("mock:end")
    protected MockEndpoint endpoint2;

    /* Route 1 Producer */
    @Produce("direct:start")
    protected ProducerTemplate template;

    /* Route 2 Producer */
    @Produce("direct:begin")
    protected ProducerTemplate template2;




    @Test
    void testDemoProcessorAndWireTap() throws InterruptedException {

        String inputBody = "Big boy slap";
        String inputHeader = "slaps";
        int inputHeaderValue = 3;
        String processedOutputExpected = String.format("Slap count : %d %nSlap message : %s%n", inputHeaderValue, inputBody);


        tappedEndpoint.expectedHeaderReceived(inputHeader, inputHeaderValue);
        tappedEndpoint.expectedBodiesReceived(inputBody);
        endpoint.expectedBodiesReceived(processedOutputExpected);

        template.sendBodyAndHeader(inputBody, inputHeader, inputHeaderValue);

        tappedEndpoint.assertIsSatisfied();
        endpoint.assertIsSatisfied();

    }


    @Test
    void wireTapAndEndpoint_receiveProcessedMessage() throws InterruptedException {
        String input = "boy";
        String expectedOutput = "BOY";

        endpoint2.expectedBodiesReceived(expectedOutput);
        tappedEndpoint.expectedBodiesReceived(expectedOutput);

        template2.sendBody(input);

        endpoint2.assertIsSatisfied();
        tappedEndpoint.assertIsSatisfied();

    }



}