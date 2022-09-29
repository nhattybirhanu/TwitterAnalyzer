package main;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import main.domain.Twitte;
import main.domain.TwitteResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class TwitterProducerApp {
	int i = -1;
	 String next_token=null;
	 String query=null;
	 String queryList[]={"USA","Programming","Ethiopia","Big data technology","Reggea"};
	 HashMap<String,String> queryNextTokenMap=new HashMap<String, String>();
	public static void  main(String args[] ){
		new TwitterProducerApp();
	}
	
	public TwitterProducerApp(){
		TwitterClient client=new TwitterClient();
		 ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		 TwitterKafakProducer kafakProducer=new TwitterKafakProducer();
	
		
	new Timer().schedule(new TimerTask() {
		
		@Override
		public void run() {

			loadNextQueryAndNext_token();
			 TwitteResponse twitteResponse= client.fetchTwiiter(query,next_token);
			 if(twitteResponse!=null&&twitteResponse.getData()!=null){
				 
				queryNextTokenMap.put(query, twitteResponse.meta.next_token);
				 for(Twitte twitte:twitteResponse.getData()){
			
					 try {
						
						 twitte.setKeyword(query);
							String json = ow.writeValueAsString(twitte);
							kafakProducer.send(json,twitte.id);		
													
					 } catch (JsonProcessingException e) {
							e.printStackTrace();
						}
				 }
	
			 }
		
		}
	}, 1,3000);
	}
	
	public void loadNextQueryAndNext_token(){
		if(i+1<queryList.length){
			i++;
			query=queryList[i];
		} else {
			i=0;
		}
		query=queryList[i];
		next_token=queryNextTokenMap.get(query);
	}
}
