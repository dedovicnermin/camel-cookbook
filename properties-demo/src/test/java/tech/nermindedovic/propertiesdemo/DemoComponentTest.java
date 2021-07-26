package tech.nermindedovic.propertiesdemo;

import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;

import org.apache.camel.test.spring.junit5.CamelSpringTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;


@ContextConfiguration
@CamelSpringTest
class DemoComponentTest  {

    @EndpointInject("mock:end")
    protected MockEndpoint endpoint;

    @Produce("direct:start")
    protected ProducerTemplate producer;


    @EndpointInject("mock:out")
    protected MockEndpoint out;

    @Produce("direct:in")
    protected ProducerTemplate producerInline;





    @Test
    void test() throws InterruptedException {
        String input = "helloWorld";
        String expected = input+input;

        endpoint.expectedBodiesReceived(expected);
        producer.sendBody(input);
        endpoint.assertIsSatisfied();
    }



    @Test
    void inlineProcessorTest() throws InterruptedException {

        out.expectedBodiesReceived("Nermin wants to go against Bruce Lee? CRAZY");
        producerInline.sendBody("Nermin");
        out.assertIsSatisfied();
    }




}