package main.domain;


import java.io.IOException;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.io.compress.Compression.Algorithm;
import org.apache.hadoop.hbase.mapred.TableOutputFormat;
import org.apache.hadoop.hbase.mapreduce.TableInputFormat;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.mapreduce.Job;

import scala.Tuple2;


public class HbaseTableUtil 
{

	private static final String TABLE_NAME = "twitter";
	private static final String CF_DEFAULT = "tweet";

	static Configuration config;
	JavaSparkContext jsc;
	String mode;
	Job jobConfig;
	
	public HbaseTableUtil (JavaSparkContext jsc, String mode) {
		this.jsc = jsc;
		this.mode = mode;
		config = HBaseConfiguration.create();
		config.addResource(new Path("file:///etc/hbase/conf.dist/hbase-site.xml"));
		config.addResource(new Path("file:///etc/hbase/conf.cloudera.hbase/hbase-site.xml"));
		config.set(TableInputFormat.INPUT_TABLE, TABLE_NAME);
		
		try {
			jobConfig = Job.getInstance(config);
			jobConfig.getConfiguration().set(TableOutputFormat.OUTPUT_TABLE, TABLE_NAME);
			jobConfig.setOutputFormatClass(org.apache.hadoop.hbase.mapreduce.TableOutputFormat.class);
			
			this.initialize(config);
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void initialize(Configuration config)
    {
        System.out.println( "init spark hbase!" );

        try (Connection conn = ConnectionFactory.createConnection(config);
        	Admin admin  = conn.getAdmin();) {
        	
        	

			System.out.print("Creating table.... ");
        	HTableDescriptor table = new HTableDescriptor(TableName.valueOf(TABLE_NAME));
			table.addFamily(new HColumnDescriptor(CF_DEFAULT).setCompressionType(Algorithm.NONE));
			
			if (admin.tableExists(table.getTableName()))
			{

	
			}else 
				admin.createTable(table);

			System.out.println(" Done!");
        } catch (IOException e) {
			e.printStackTrace();
		}
    }   
	
    public void writeRowNewHadoopAPI(JavaRDD<Twitte> records) {
    	JavaPairRDD<ImmutableBytesWritable, Put> hbasePuts = 
    			records.mapToPair(t -> {
					Twitte twitte= t;
    				Put put = new Put(Bytes.toBytes(t.getId())); //rowKey
					put.addColumn(Bytes.toBytes(CF_DEFAULT), Bytes.toBytes("id"), Bytes.toBytes(t.id)); 

					put.addColumn(Bytes.toBytes(CF_DEFAULT), Bytes.toBytes("author_id"), Bytes.toBytes(t.author_id)); 
					put.addColumn(Bytes.toBytes(CF_DEFAULT), Bytes.toBytes("text"), Bytes.toBytes(t.getText()));
					put.addColumn(Bytes.toBytes(CF_DEFAULT), Bytes.toBytes("created_at"), Bytes.toBytes(t.created_at));
					put.addColumn(Bytes.toBytes(CF_DEFAULT), Bytes.toBytes("like_count"), Bytes.toBytes(t.public_metrics.like_count)); 
					put.addColumn(Bytes.toBytes(CF_DEFAULT), Bytes.toBytes("retweet_count"), Bytes.toBytes(t.public_metrics.retweet_count));
					put.addColumn(Bytes.toBytes(CF_DEFAULT), Bytes.toBytes("reply_count"), Bytes.toBytes(t.public_metrics.reply_count));
					put.addColumn(Bytes.toBytes(CF_DEFAULT), Bytes.toBytes("quote_count"), Bytes.toBytes(t.public_metrics.quote_count));
					put.addColumn(Bytes.toBytes(CF_DEFAULT), Bytes.toBytes("keyword"), Bytes.toBytes(t.getKeyword()));

    				return new Tuple2<ImmutableBytesWritable, Put>(
						new ImmutableBytesWritable(), put);});
 		hbasePuts.saveAsNewAPIHadoopDataset(jobConfig.getConfiguration());
    }
}
