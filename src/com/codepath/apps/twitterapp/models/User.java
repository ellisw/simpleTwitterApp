package com.codepath.apps.twitterapp.models;

import org.json.JSONException;
import org.json.JSONObject;

import com.activeandroid.Model;

public class User extends Model{

	private long userId;
	private String name;
	private String screenName;
	private String profileBackgroundImageUrl;
	private int numTweets;
	private int followersCount;
	private int friendCount;
	private String tagline;
	
	public User(){
		
	}
	
	public String getTagline(){
		return tagline;
	}
	
	public long getUserId() {
		return userId;
	}

	public String getName() {
		return name;
	}

	public String getScreenName() {
		return screenName;
	}

	public String getProfileBackgroundImageUrl() {
		return profileBackgroundImageUrl;
	}

	public int getNumTweets() {
		return numTweets;
	}

	public int getFollowersCount() {
		return followersCount;
	}

	public int getFriendCount() {
		return friendCount;
	}
	
	public String toString(){
		return screenName;
	}
	

	public static User fromJson(JSONObject json){
		User u = new User();
		try {
			u.userId = json.getLong("id");
			u.name = json.getString("name");
			u.screenName = json.getString("screen_name");
			u.profileBackgroundImageUrl = json.getString("profile_image_url_https");
			u.followersCount = json.getInt("followers_count");
			u.friendCount = json.getInt("friends_count");
			u.numTweets = json.getInt("statuses_count");
			u.tagline = json.getString("description");
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return u;
	}
}
