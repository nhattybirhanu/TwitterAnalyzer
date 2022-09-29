package main.domain;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

public class Twitte2 implements Serializable {
public String id;
public String text;
public String author_id;
public long retweet_count;
public long reply_count;
public long like_count;
public long quote_count;

public String created_at;

public String getId() {
	return id;
}

public void setId(String id) {
	this.id = id;
}

public String getText() {
	return text;
}

public void setText(String text) {
	this.text = text;
}

public String getAuthor_id() {
	return author_id;
}

public void setAuthor_id(String author_id) {
	this.author_id = author_id;
}




public String getCreated_at() {
	return created_at;
}

public void setCreated_at(String created_at) {
	this.created_at = created_at;
}

public long getReply_count() {
	return reply_count;
}
public void setReply_count(long reply_count) {
	this.reply_count = reply_count;
}
public long getLike_count() {
	return like_count;
}
public void setLike_count(long like_count) {
	this.like_count = like_count;
}
public long getQuote_count() {
	return quote_count;
}
public void setQuote_count(long quote_count) {
	this.quote_count = quote_count;
}

public void setRetweet_count(long retweet_count) {
this.retweet_count=retweet_count;
	
}

public long getRetweet_count() {
	return retweet_count;
}







}
