package main.domain;

import lombok.Data;

@Data
public class PublicMetrics {

	public long retweet_count;
	public long reply_count;
	public long like_count;
	public long quote_count;
	public long getRetweet_count() {
		return retweet_count;
	}
	public void setRetweet_count(long retweet_count) {
		this.retweet_count = retweet_count;
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
	

}
