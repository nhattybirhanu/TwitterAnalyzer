package main;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TwitterKafakProducer {
	
	public KafkaProducer<String , String> producer;
	public TwitterKafakProducer(){

		
		KafkaProperties properties=new KafkaProperties();
		producer=properties.createKafkaProducer();
	}
	public void send(String tweet,String key){
		producer.send(new ProducerRecord<String, String>(KafkaConfig.TOPIC, key,tweet),new Callback() {
			
			@Override
			public void onCompletion(RecordMetadata arg0, Exception arg1) {
				// TODO Auto-generated method stub
				System.out.println("Sent ");
			}
			
		});
	}
}
