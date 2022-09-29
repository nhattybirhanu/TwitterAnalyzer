# Big Data Pipeline for Real-Time Twitter Dataset

### Project Members

* **AJEMA**, Natnael Birhanu
****
**Project Video**

  https://web.microsoftstream.com/video/ba5afb63-c794-4d2b-8655-e9e54a558514

### Prerequisites
* Cloudera VM image
* Linux machine

### Configuration
* Install and run kafka
    * kafka-server-start.sh $KAFKA_HOME/config/server.properties
    * **Note:** Zookeeper is already running
* Make sure HBase servers are running
    * **confirm servers are up:** service --status-all
    * **start master server:** sudo service hbase-master start;
    * **start region H-Region server** sudo service hbase-regionserver start;

  
### Data Repository => Kafka producer(topic:TwitterKafka)
* [Twitter API](https://developer.twitter.com/en/docs/twitter-api)
* Endpoint: **/search**
    * List all recent tweets related to query 
    * limited to 10 records for this use case    
    * curl -X 'GET' \
      'https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&order=market_cap_desc&per_page=20&page=1&sparkline=false' \
      -H 'accept: application/json'
      
## SparkStreaming <= Kafka consumer(topic:Twitter kafka)
* Use this to obtain all the twitte  data (text, author_id, key,public_metric)

### HBase => Hive Table
* Create an external table on Hive to query HBase Data
  * **Query:**
    *
CREATE EXTERNAL TABLE IF NOT EXISTS twitter (
  id STRING, text STRING, author_id STRING, keyword STRING, retweet_count BIGINT ,
  reply_count BIGINT, like_count BIGINT, quote_count BIGINT
)
STORED BY 'org.apache.hadoop.hive.hbase.HBaseStorageHandler' 
WITH SERDEPROPERTIES (
  'hbase.columns.mapping'=':key, tweet:text,tweet:keyword, tweet:author_id,tweet:retweet_count#b, 
  tweet:reply_count#b, tweet:like_count#b, tweet:quote_count#b'
) 
TBLPROPERTIES ('hbase.table.name' = 'twitter');
* Column mapping and serialization 
    * **Hive/HBase Serde:** 'org.apache.hadoop.hive.hbase.HBaseStorageHandler'
  


### Spark SQL <= HBase
* Plain queries
  * DataFrame query2 = sqlContext
				.sql("SELECT avg(reply_count) as average_replay FROM twitter");
		query2.show();
* Functional Queries
 DataFrame query3 = sqlContext
 .table(TABLE_NAME)
 .select( "author_id", "id", "created_at","keyword")
 .limit(10);
		 query3.show();
  * DataFrame query4 = sqlContext
    .table(TABLE_NAME)
    .select("*")
    .filter("current_price > 1000")
    .orderBy()
    .limit(10);
    query4.show();

### RUN Projects
1. kafkaproducer
  * Run **Runner.java** class for streaming data from API to kafka queue
2. kafkaconsumer
  * Run **TwitterConsumerApp.java** class for kafka consumer + spark streaming to HBase
  * Run **SparkSqlHbase.java** class to connect spark SQL to HBase and query table
3. Alternatively in pseudo-distributed mode
   * **package jar:** cd to project folder, run 'mvn clean package'
   * **spark-submit:** submit jars to spark on terminal