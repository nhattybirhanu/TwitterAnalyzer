package main;

import main.domain.TwitteResponse;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.api.Response;
import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.util.ssl.SslContextFactory;

import com.fasterxml.jackson.databind.ObjectMapper;






public class TwitterClient {
	String headerName="Authorization";
	String value="Bearer "+TwiiterAuthConfig.bearerToken;
	public TwitteResponse fetchTwiiter(String query,String next_token){
		
		SslContextFactory sslContextFactory = new SslContextFactory();

		HttpClient client=new HttpClient(sslContextFactory);
		try {
			client.start();
			ContentResponse response =
					next_token==null?
						client.newRequest("https://api.twitter.com/2/tweets/search/recent").
						method(HttpMethod.GET).
						param("tweet.fields", "public_metrics,text,author_id,created_at").
						param("query", query).
						header(headerName, value).	
					send()
				:
				requestWithNextToken(client, query, next_token).send();
	        client.stop();
	        ObjectMapper mapper=new ObjectMapper();
	        TwitteResponse twitteResponse=mapper.readValue( response.getContentAsString(), TwitteResponse.class);
	
	   			return twitteResponse;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	
	}
	
	public Request requestWithNextToken(HttpClient client,String query,String nextToken){
		return client.newRequest("https://api.twitter.com/2/tweets/search/recent").
		method(HttpMethod.GET).
		param("tweet.fields", "public_metrics,text,author_id,created_at").
		param("query", query).
		param("next_token",nextToken).
		header(headerName, value);
			}

}
