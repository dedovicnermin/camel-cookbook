package tech.nermindedovic.recipientlistdemo;

import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.junit5.CamelSpringTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;

@CamelSpringTest
@ContextConfiguration
class DemoTest {

    @Produce("direct:start")
    protected ProducerTemplate producer;

    @EndpointInject("mock:debit")
    protected MockEndpoint debitEndpoint;

    @EndpointInject("mock:credit")
    protected MockEndpoint creditEndpoint;

    @EndpointInject("mock:error")
    protected MockEndpoint errorEndpoint;

    @EndpointInject("mock:errorLogger")
    protected MockEndpoint errorLoggerEndpoint;

    @EndpointInject("mock:logger")
    protected MockEndpoint loggerEndpoint;


    @AfterEach
    void reset() {
        debitEndpoint.reset();
        creditEndpoint.reset();
        errorEndpoint.reset();
        errorLoggerEndpoint.reset();
        loggerEndpoint.reset();
    }


    @Test
    void whenBodyContainsDebit_willRouteToCorrectEndpoints() throws InterruptedException {
        String input = "<TransferMessage><transferType>debit</transferType></TransferMessage>";
        debitEndpoint.expectedBodiesReceived(input);
        loggerEndpoint.expectedBodiesReceived(input);
        debitEndpoint.expectedMessageCount(1);
        loggerEndpoint.expectedMessageCount(1);
        creditEndpoint.expectedMessageCount(0);
        errorEndpoint.expectedMessageCount(0);
        errorLoggerEndpoint.expectedMessageCount(0);

        producer.sendBodyAndHeader(input, "paymentType", "debit");

        assertEndpointsSatisfied();
    }

    @Test
    void whenBodyContainsCredit_willRouteToCorrectEndpoints() throws InterruptedException {
        String input = "<TransferMessage><paymentType>credit</paymentType></TransferMessage>";
        creditEndpoint.expectedBodiesReceived(input);
        loggerEndpoint.expectedBodiesReceived(input);
        debitEndpoint.expectedMessageCount(0);
        loggerEndpoint.expectedMessageCount(1);
        creditEndpoint.expectedMessageCount(1);
        errorEndpoint.expectedMessageCount(0);
        errorLoggerEndpoint.expectedMessageCount(0);

        producer.sendBodyAndHeader(input, "paymentType", "credit");

        assertEndpointsSatisfied();
    }

    @Test
    void whenBodyContainsError_willRouteToCorrectEndpoints() throws InterruptedException {
        String input = "<TransferMessage><paymentType>unrecognizable</paymentType></TransferMessage>";
        errorEndpoint.expectedBodiesReceived(input);
        errorLoggerEndpoint.expectedBodiesReceived(input);
        debitEndpoint.expectedMessageCount(0);
        loggerEndpoint.expectedMessageCount(0);
        creditEndpoint.expectedMessageCount(0);
        errorEndpoint.expectedMessageCount(1);
        errorLoggerEndpoint.expectedMessageCount(1);

        producer.sendBodyAndHeader(input, "paymentType", "unrecognizable");

        assertEndpointsSatisfied();
    }

    void assertEndpointsSatisfied() throws InterruptedException {
        debitEndpoint.assertIsSatisfied();
        loggerEndpoint.assertIsSatisfied();
        creditEndpoint.assertIsSatisfied();
        errorEndpoint.assertIsSatisfied();
        errorLoggerEndpoint.assertIsSatisfied();
    }



}
