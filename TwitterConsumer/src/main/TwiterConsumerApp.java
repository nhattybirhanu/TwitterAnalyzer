package main;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import kafka.serializer.StringDecoder;
import main.domain.HbaseTableUtil;
import main.domain.Twitte;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaPairInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;
import com.google.gson.Gson;


public class TwiterConsumerApp {

	public static void main(String args[]) {


	        SparkConf sconf = new SparkConf()
	        	.setAppName("KafkaStreamConsumer")
	        	.setMaster("local[*]");
			sconf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer");
			sconf.registerKryoClasses(new Class[]{org.apache.hadoop.hbase.io.ImmutableBytesWritable.class});
			
	        JavaSparkContext jsc = new JavaSparkContext(sconf);
	        JavaStreamingContext ssc = new JavaStreamingContext(jsc, Durations.seconds(3));
	    	
	    	HbaseTableUtil hbaseUtil  = new HbaseTableUtil(jsc, "local");
	    	
	    	try {

	    	    Set<String> topics = new HashSet<>(Arrays.asList(KafkaConfig.TOPIC));
	    		Map<String, String> kafkaParams = new HashMap<>();
	    	    kafkaParams.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
	    	    kafkaParams.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "StringDeserializer");
	    		kafkaParams.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "StringDeserializer");
	    		kafkaParams.put(ConsumerConfig.GROUP_ID_CONFIG, "twiite");
	    	    
	    		JavaPairInputDStream<String, String> stream = 
						KafkaUtils.createDirectStream(ssc, String.class, String.class, StringDecoder.class, StringDecoder.class, kafkaParams, topics);
	    		
	    		stream.foreachRDD(rdd -> {
				
					JavaRDD<Twitte> twitterRDD = rdd.map(record -> new Gson().fromJson(record._2, Twitte.class));
					hbaseUtil.writeRowNewHadoopAPI(twitterRDD);
	    		});

	    		ssc.start();
	    		ssc.awaitTermination();
//	    		jsc.sc().stop();
	    	
	        } catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        System.out.println( "complete spark stream!" );
	    }
	
}
