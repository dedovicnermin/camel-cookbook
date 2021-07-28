package tech.nermindedovic.camelkafkademo;


import org.apache.camel.EndpointInject;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import java.util.HashMap;


@SpringBootTest(properties = "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}")
@ContextConfiguration
@EmbeddedKafka(partitions = 1, topics = {HelloKafkaTest.consumingTopic, HelloKafkaTest.producingTopic}, ports = 29092, zkConnectionTimeout = 2000)
@DirtiesContext
class HelloKafkaTest {

    static final String consumingTopic = "helloCamel";
    static final String producingTopic = "helloKafka";





    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;


    @EndpointInject("mock:toCamelRoute")
    protected MockEndpoint mockEndpoint;




    @Test
    void camelWillConsumeFromKafka_andRouteToMockEndpoint() throws InterruptedException {

        String kafkaInputBody = "Hello Camel, i am kafka!", kafkaInputKey = "testKey";
        mockEndpoint.expectedBodiesReceived(kafkaInputBody);

        HashMap<String, Object> producerConfig = new HashMap<>(KafkaTestUtils.producerProps(embeddedKafkaBroker));
        Producer<String, String> producer = new DefaultKafkaProducerFactory<>(producerConfig, new StringSerializer(), new StringSerializer()).createProducer();
        producer.send(new ProducerRecord<>(consumingTopic, kafkaInputKey, kafkaInputBody ));
        producer.flush();
        producer.flush();


        mockEndpoint.assertIsSatisfied(5000);






    }


}
