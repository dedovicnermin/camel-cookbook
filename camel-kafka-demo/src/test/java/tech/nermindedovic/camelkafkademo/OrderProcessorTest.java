package tech.nermindedovic.camelkafkademo;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest(properties = "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}")
@ContextConfiguration
@EmbeddedKafka(
        partitions = 1,
        topics = {OrderProcessorTest.TOPIC_IN, OrderProcessorTest.TOPIC_OUT},
        ports = 29092
)
@DirtiesContext
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OrderProcessorTest {
    static final String TOPIC_IN = "orders.in";
    static final String TOPIC_OUT = "orders.out";

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    private Producer<String, Object> producer;
    private BlockingQueue<ConsumerRecord<String, Order>> records = new LinkedBlockingQueue<>();

    @BeforeAll
    void setup() {
        Map<String, Object> producerConfig = new HashMap<>(KafkaTestUtils.producerProps(embeddedKafkaBroker));
        producer = new DefaultKafkaProducerFactory<>(producerConfig, new StringSerializer(), new JsonSerializer<>()).createProducer();

        Map<String,Object> consumerConfig = new HashMap<>(KafkaTestUtils.consumerProps("order-producer", "false", embeddedKafkaBroker));
        ContainerProperties containerConfig = new ContainerProperties(TOPIC_OUT);
        DefaultKafkaConsumerFactory<String, Order> factory = new DefaultKafkaConsumerFactory<>(consumerConfig, new StringDeserializer(), new JsonDeserializer<>(Order.class));
        KafkaMessageListenerContainer<String, Order> listenerContainer = new KafkaMessageListenerContainer<>(factory, containerConfig);
        listenerContainer.setupMessageListener((MessageListener<String, Order>) records::add);
        listenerContainer.start();
        ContainerTestUtils.waitForAssignment(listenerContainer, embeddedKafkaBroker.getPartitionsPerTopic());
    }

    @Test
    void willConsumeOrder_routeToProcessor_andProduceToTopic() throws ExecutionException, InterruptedException {
        Order input = new Order(1L, "Test Product", 23.33, false);
        RecordMetadata recordMetadata = producer.send(new ProducerRecord<>(TOPIC_IN, input)).get();
        assertThat(recordMetadata.hasOffset()).isTrue();

        ConsumerRecord<String, Order> potentialRecord = records.poll(500, TimeUnit.MILLISECONDS);
        assertThat(potentialRecord).isNotNull();
        Order consumed = potentialRecord.value();
        assertThat(consumed.isProcessed()).isTrue();

    }







}