package main;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

import main.domain.PublicMetrics;
import main.domain.Twitte;
import main.domain.Twitte2;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableInputFormat;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.SQLContext;

public class SparkSqlHbase {
	private static final String TABLE_NAME = "twitter";
	private static final String CF_DEFAULT = "tweet";
	static Configuration config;
	static JavaSparkContext jsc;
	public static void main(String[] args) {

		SparkConf sconf = new SparkConf().setAppName("KafkaStreamConsumer")
				.setMaster("local[3]");
		sconf.set("spark.serializer",
				"org.apache.spark.serializer.KryoSerializer");
		sconf.registerKryoClasses(new Class[] { org.apache.hadoop.hbase.io.ImmutableBytesWritable.class });
		
		config = HBaseConfiguration.create();
		config.addResource(new Path("file:///etc/hbase/conf.dist/hbase-site.xml"));
		config.addResource(new Path("file:///etc/hbase/conf.cloudera.hbase/hbase-site.xml"));
		config.set(TableInputFormat.INPUT_TABLE, TABLE_NAME);

		jsc = new JavaSparkContext(sconf);
		SQLContext sqlContext = new SQLContext(jsc.sc());
		
		JavaPairRDD<ImmutableBytesWritable, Result> hBaseRDD = readTableByJavaPairRDD();
		System.out.println("Number of register in hbase table: " + hBaseRDD.count());
		

		

		//build hbase table schema
		JavaRDD<Twitte> rows = hBaseRDD.map(x -> {
			System.out.println(x);
			Twitte twitte=new Twitte();
			twitte.setId(Bytes.toString(x._1.get())); //row-key
			twitte.setAuthor_id(Bytes.toString(x._2.getValue(Bytes.toBytes(CF_DEFAULT), Bytes.toBytes("author_id")))); 
			twitte.setText(Bytes.toString(x._2.getValue(Bytes.toBytes(CF_DEFAULT), Bytes.toBytes("text"))));
			twitte.setCreated_at(Bytes.toString(x._2.getValue(Bytes.toBytes(CF_DEFAULT), Bytes.toBytes("created_at"))));
			
			twitte.setReply_count(Bytes.toLong(x._2.getValue(Bytes.toBytes(CF_DEFAULT), Bytes.toBytes("reply_count"))));
			twitte.setLike_count(Bytes.toLong(x._2.getValue(Bytes.toBytes(CF_DEFAULT), Bytes.toBytes("like_count"))));
			twitte.setQuote_count(Bytes.toLong(x._2.getValue(Bytes.toBytes(CF_DEFAULT), Bytes.toBytes("quote_count"))));
			twitte.setRetweet_count(Bytes.toLong(x._2.getValue(Bytes.toBytes(CF_DEFAULT), Bytes.toBytes("retweet_count"))));
			twitte.setKeyword(Bytes.toString(x._2.getValue(Bytes.toBytes(CF_DEFAULT), Bytes.toBytes("keyword"))));

		
			return twitte;
		});

		DataFrame tabledata = sqlContext
				.createDataFrame(rows, Twitte.class);
		tabledata.registerTempTable(TABLE_NAME);
		tabledata.printSchema();

		// Query 1 
		DataFrame query1 = sqlContext
				.table(TABLE_NAME)
				.select("*")
				.orderBy("like_count")
				.limit(10);
		query1.show();

		// Query 2 SELECT avg(reply_count) as average_replay FROM twitter
		DataFrame query2 = sqlContext
				.sql("SELECT avg(reply_count) as average_replay FROM twitter");
		query2.show();

		// Query 3  "SELECT name, author_id, "id, created_at, key  FROM twiiter LIMIT 10"
		 DataFrame query3 = sqlContext
				 .table(TABLE_NAME)
				 .select( "author_id", "id", "created_at","keyword")
				 .limit(10);
		 query3.show();
		 
		// Query 4   twittes more that 100 retweet_count 
			DataFrame query4 = sqlContext
					.table(TABLE_NAME)
					.select("*")
					.filter("retweet_count > 100")
					.orderBy()
					.limit(10);
			query4.show();
		



		jsc.stop();

	}
	
	//get the Result of query from the Table of Hbase
    public static JavaPairRDD<ImmutableBytesWritable, Result> readTableByJavaPairRDD() {
		
    	JavaPairRDD<ImmutableBytesWritable, Result> hBaseRDD = jsc
				.newAPIHadoopRDD(
						config,
						TableInputFormat.class,
						org.apache.hadoop.hbase.io.ImmutableBytesWritable.class,
						org.apache.hadoop.hbase.client.Result.class);
		return hBaseRDD;
    }
	

}
