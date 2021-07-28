package tech.nermindedovic.camelkafkademo;


import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}")
@ContextConfiguration
@EmbeddedKafka(partitions = 1, topics = {
        HelloKafkaTest.INBOUND_TOPIC,
        HelloKafkaTest.OUTBOUND_TOPIC,
        HelloKafkaTest.INBOUND_ONLY_TOPIC
}, ports = 29092, zkConnectionTimeout = 2000)
@DirtiesContext
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HelloKafkaTest {

    static final String INBOUND_ONLY_TOPIC = "toCamelTopic";
    static final String INBOUND_TOPIC = "helloCamel";
    static final String OUTBOUND_TOPIC = "helloKafka";


    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @EndpointInject("mock:toCamelRoute")
    protected MockEndpoint mockEndpoint;

    private Producer<String, String> producer;

    //test 2
    @Produce("direct:start")
    protected ProducerTemplate template;

    private BlockingQueue<ConsumerRecord<String, String>> sentFromCamelRecords;


    @BeforeAll
    void setup() {
        //set up producer
        HashMap<String, Object> producerConfig = new HashMap<>(KafkaTestUtils.producerProps(embeddedKafkaBroker));
        producer = new DefaultKafkaProducerFactory<>(producerConfig, new StringSerializer(), new StringSerializer()).createProducer();

        //set up consumer
        Map<String, Object> consumerProps = new HashMap<>(KafkaTestUtils.consumerProps("camel-produce-kafka", "false", embeddedKafkaBroker));
        DefaultKafkaConsumerFactory<String, String> factory = new DefaultKafkaConsumerFactory<>(consumerProps, new StringDeserializer(), new StringDeserializer());
        ContainerProperties containerProperties = new ContainerProperties(OUTBOUND_TOPIC);

        KafkaMessageListenerContainer<String, String> container = new KafkaMessageListenerContainer<>(factory, containerProperties);
        sentFromCamelRecords = new LinkedBlockingQueue<>();
        container.setupMessageListener((MessageListener<String, String>) sentFromCamelRecords::add);

        container.start();
        ContainerTestUtils.waitForAssignment(container, embeddedKafkaBroker.getPartitionsPerTopic());
    }





    @Test
    void camelWillConsumeFromKafka_andRouteToMockEndpoint() throws InterruptedException {
        String kafkaInputBody = "Hello Camel, i am kafka!", kafkaInputKey = "testKey";
        mockEndpoint.expectedBodiesReceived(kafkaInputBody);

        producer.send(new ProducerRecord<>(INBOUND_ONLY_TOPIC, kafkaInputKey, kafkaInputBody ));
        producer.flush();

        mockEndpoint.assertIsSatisfied(5000);
    }




    @Test
    void camelWillProduceToKafka() throws InterruptedException {
        String input = "Hello Kafka! - From Camel";
        template.sendBody(input);

        ConsumerRecord<String, String> potentialRecord = sentFromCamelRecords.poll(1000, TimeUnit.MILLISECONDS);
        assertThat(potentialRecord).isNotNull();
        assertThat(potentialRecord.value()).isEqualTo(input);
    }


    @Test
    void camelWillConsume_thenRouteToBean_toProduceBackToKafka() throws InterruptedException {
        sentFromCamelRecords.clear();

        String input = "Hi Camel! - From Kafka";
        String expected = "Hi Kafka! - From Camel";
        producer.send(new ProducerRecord<>(INBOUND_TOPIC, input));
        producer.flush();

        ConsumerRecord<String, String> potentialRecord = sentFromCamelRecords.poll(2000, TimeUnit.MILLISECONDS);
        assertThat(potentialRecord).isNotNull();
        assertThat(potentialRecord.value()).isEqualTo(expected);

    }


}
