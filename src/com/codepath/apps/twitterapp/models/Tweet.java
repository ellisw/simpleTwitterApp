package com.codepath.apps.twitterapp.models;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.activeandroid.Model;

public class Tweet extends Model{

	private long tweetId;
	private String text;
	private User user;
	private boolean isFavorited;
	private boolean isRetweeted;
	private Date createdAt;
	
	public Tweet(){}
	

	public void setTweetId(long tweetId) {
		this.tweetId = tweetId;
	}


	public void setText(String text) {
		this.text = text;
	}


	public void setUser(User user) {
		this.user = user;
	}


	public void setFavorited(boolean isFavorited) {
		this.isFavorited = isFavorited;
	}


	public void setRetweeted(boolean isRetweeted) {
		this.isRetweeted = isRetweeted;
	}


	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public long getTweetId() {
		return tweetId;
	}

	public String getText() {
		return text;
	}

	public User getUser() {
		return user;
	}

	public boolean isFavorited() {
		return isFavorited;
	}

	public boolean isRetweeted() {
		return isRetweeted;
	}
	
	public Date getCreatedAt() {
		return createdAt;
	}
	

	public String toString(){
		return tweetId + ""+createdAt;
	}
	
	@SuppressWarnings("deprecation")
	public static Tweet fromJson(JSONObject jsonObject){
		Tweet tweet = new Tweet();
		try {
			tweet.tweetId = jsonObject.getLong("id");
			tweet.text = jsonObject.getString("text");
			tweet.isRetweeted = jsonObject.getBoolean("retweeted");
			tweet.isFavorited = jsonObject.getBoolean("favorited");
			tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
			tweet.createdAt = new Date(jsonObject.getString("created_at"));
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return tweet;
	}
	
	public static ArrayList<Tweet> fromJson(JSONArray jsonArray){
		ArrayList<Tweet> tweets = new ArrayList<Tweet>(jsonArray.length());
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject tweetJson = null;
			try {
				tweetJson = jsonArray.getJSONObject(i);
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
			
			Tweet tweet = Tweet.fromJson(tweetJson);
			if (tweet != null){
				tweets.add(tweet);
			}
		}
		return tweets;
	}
	
}
