package tech.nermindedovic.contentroutingdemo;

import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.junit5.CamelSpringTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;



@ContextConfiguration
@CamelSpringTest
class ContentProcessorTest {

    @BeforeEach
    void reset() {
        plainTextEndpoint.reset();
        xmlEndpoint.reset();
        endpointUno.reset();
        endpointDos.reset();
        endpointTres.reset();
        endpointError.reset();
    }

    @EndpointInject("mock:plain")
    protected MockEndpoint plainTextEndpoint;

    @EndpointInject("mock:xml")
    protected MockEndpoint xmlEndpoint;


    @Produce("direct:start")
    protected ProducerTemplate template;


    @Test
    void whenXml_messageGetsRoutedAppropriately() throws InterruptedException {
        String input = "<xml> <message> this is an xml message </message> </xml>";
        xmlEndpoint.expectedBodiesReceived(input);
        plainTextEndpoint.expectedMessageCount(0);

        template.sendBody(input);

        xmlEndpoint.assertIsSatisfied();
        plainTextEndpoint.assertIsSatisfied();
    }


    @Test
    void whenPlainText_willDirectToPlainTextEndpoint() throws InterruptedException {
        String input = "This message is in plain text";
        xmlEndpoint.expectedMessageCount(0);
        plainTextEndpoint.expectedBodiesReceived(input);

        template.sendBody(input);

        xmlEndpoint.assertIsSatisfied();
        plainTextEndpoint.assertIsSatisfied();
    }



    @EndpointInject("mock:uno")
    protected MockEndpoint endpointUno;

    @EndpointInject("mock:dos")
    protected MockEndpoint endpointDos;

    @EndpointInject("mock:tres")
    protected MockEndpoint endpointTres;

    @EndpointInject("mock:error")
    protected MockEndpoint endpointError;

    @Produce("direct:begin")
    protected ProducerTemplate template2;


    @Test
    void whenRouteIsFirst_goesToFirst() throws InterruptedException {
        String input = "Test input";
        String header = "route", value = "first";

        endpointUno.expectedHeaderReceived("destination", 1);
        endpointUno.expectedBodiesReceived(input);

        endpointDos.expectedMessageCount(0);
        endpointTres.expectedMessageCount(0);
        endpointError.expectedMessageCount(0);

        template2.sendBodyAndHeader(input, header, value);

        endpointUno.assertIsSatisfied();
        endpointDos.assertIsSatisfied();
        endpointTres.assertIsSatisfied();
        endpointError.assertIsSatisfied();
    }


    @Test
    void whenRouteIsSecond_goesToSecond() throws InterruptedException {
        String input = "Test input2";
        String header = "route", value = "second";

        endpointUno.expectedMessageCount(0);
        endpointTres.expectedMessageCount(0);
        endpointError.expectedMessageCount(0);

        endpointDos.expectedHeaderReceived("destination", 2);
        endpointDos.expectedBodiesReceived(input);

        template2.sendBodyAndHeader(input, header, value);

        endpointUno.assertIsSatisfied();
        endpointDos.assertIsSatisfied();
        endpointTres.assertIsSatisfied();
        endpointError.assertIsSatisfied();
    }


    @Test
    void whenRouteIsThird_goesToThirdRoute() throws InterruptedException {
        String input = "Test input3";
        String header = "route", value = "third";

        endpointUno.expectedMessageCount(0);
        endpointDos.expectedMessageCount(0);
        endpointError.expectedMessageCount(0);

        endpointTres.expectedHeaderReceived("destination", 3);
        endpointTres.expectedBodiesReceived(input);

        template2.sendBodyAndHeader(input, header, value);

        endpointUno.assertIsSatisfied();
        endpointDos.assertIsSatisfied();
        endpointTres.assertIsSatisfied();
        endpointError.assertIsSatisfied();
    }


    @Test
    void whenRouteIsUnrecognizable_willRouteToError() throws InterruptedException {
        String input = "Test input error";
        String header = "route", value = "unrecognizable route";

        endpointUno.expectedMessageCount(0);
        endpointDos.expectedMessageCount(0);
        endpointTres.expectedMessageCount(0);

        endpointError.expectedHeaderReceived("destination", "error");
        endpointError.expectedBodiesReceived(input);

        template2.sendBodyAndHeader(input, header, value);

        endpointUno.assertIsSatisfied();
        endpointDos.assertIsSatisfied();
        endpointTres.assertIsSatisfied();
        endpointError.assertIsSatisfied();
    }







}