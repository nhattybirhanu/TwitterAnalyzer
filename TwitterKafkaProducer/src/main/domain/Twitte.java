package main.domain;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Twitte implements Serializable {
public String id;
public String text;
public String author_id;
public PublicMetrics public_metrics;
public String keyword;

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

public PublicMetrics getPublic_metrics() {
	return public_metrics;
}

public void setPublic_metrics(PublicMetrics public_metrics) {
	this.public_metrics = public_metrics;
}

public String getCreated_at() {
	return created_at;
}

public void setCreated_at(String created_at) {
	this.created_at = created_at;
}

public String getKeyword() {
	return keyword;
}

public void setKeyword(String keyword) {
	this.keyword = keyword;
}




}
