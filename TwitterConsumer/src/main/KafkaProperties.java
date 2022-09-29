package main;

import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

public class KafkaProperties {

	public  KafkaProducer<String, String> createKafkaProducer() {
	    // Create producer properties
	    Properties prop = new Properties();
	    prop.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConfig.BOOTSTRAPSERVERS);
	    prop.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
	    prop.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

	    // create safe Producer
//	    prop.setProperty(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
//	    prop.setProperty(ProducerConfig.ACKS_CONFIG, KafkaConfig.ACKS_CONFIG);
//	    prop.setProperty(ProducerConfig.RETRIES_CONFIG, KafkaConfig.RETRIES_CONFIG);
//	    prop.setProperty(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, KafkaConfig.MAX_IN_FLIGHT_CONN);

	    // Additional settings for high throughput producer
//	    prop.setProperty(ProducerConfig.COMPRESSION_TYPE_CONFIG, KafkaConfig.COMPRESSION_TYPE);
//	    prop.setProperty(ProducerConfig.LINGER_MS_CONFIG, KafkaConfig.LINGER_CONFIG);
//	    prop.setProperty(ProducerConfig.BATCH_SIZE_CONFIG, KafkaConfig.BATCH_SIZE);

	    // Create producer
	    return new KafkaProducer<String, String>(prop);
	}
	public  KafkaConsumer<String, String> createKafkaConsumer() {
	    // Create producer properties
	    Properties prop = new Properties();
	    prop.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConfig.BOOTSTRAPSERVERS);
	    prop.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
	    prop.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
	    prop.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "Twitter");
	    prop.setProperty(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
	     prop.setProperty(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG,"30000");
	     prop.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
	     prop.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

	    // create safe Producer
//	    prop.setProperty(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
//	    prop.setProperty(ProducerConfig.ACKS_CONFIG, KafkaConfig.ACKS_CONFIG);
//	    prop.setProperty(ProducerConfig.RETRIES_CONFIG, KafkaConfig.RETRIES_CONFIG);
//	    prop.setProperty(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, KafkaConfig.MAX_IN_FLIGHT_CONN);

	    // Additional settings for high throughput producer
//	    prop.setProperty(ProducerConfig.COMPRESSION_TYPE_CONFIG, KafkaConfig.COMPRESSION_TYPE);
//	    prop.setProperty(ProducerConfig.LINGER_MS_CONFIG, KafkaConfig.LINGER_CONFIG);
//	    prop.setProperty(ProducerConfig.BATCH_SIZE_CONFIG, KafkaConfig.BATCH_SIZE);

	    // Create producer
	    return new KafkaConsumer<String, String>(prop);
	}
}
