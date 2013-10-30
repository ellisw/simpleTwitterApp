package com.codepath.apps.twitterapp;

import java.io.UnsupportedEncodingException;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;
import android.util.Log;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
    public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class;
    public static final String REST_URL = "https://api.twitter.com/1.1";
    public static final String REST_CONSUMER_KEY = "hesaTcah4lPyGPpLvIHOMw";
    public static final String REST_CONSUMER_SECRET = "z4RyhLLDKL8wDBvokIsufvSJsAY9tWyDav68tZ72bI";
    public static final String REST_CALLBACK_URL = "oauth://mytwitterapp"; 
    
    public TwitterClient(Context context) {
        super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
    }
    
    
    public void getHomeTimeline(AsyncHttpResponseHandler handler) {
    	String url = getApiUrl("statuses/home_timeline.json?count=5");
    	Log.v("DEBUG", url);
    	client.get(url, null, handler);
	}
    
    public void getOlderHomeTimeline(long maxId, AsyncHttpResponseHandler handler) {
    	String url = getApiUrl("statuses/home_timeline.json?count=1&max_id=" + (maxId-1));
    	Log.v("DEBUG", url);
    	client.get(url, null, handler);
	}
    
    public void getNewerHomeTimeline(long since_id, AsyncHttpResponseHandler handler) {
    	String url = getApiUrl("statuses/home_timeline.json?count=5&since_id="+since_id);
    	Log.v("DEBUG", url);
    	client.get(url, null, handler);
	}
    
    public void getMentions(AsyncHttpResponseHandler handler) {
    	String url = getApiUrl("statuses/mentions_timeline.json?count=5");
    	Log.v("DEBUG", url);
    	client.get(url, null, handler);
	}
    
    public void getOlderMentions(long maxId, AsyncHttpResponseHandler handler) {
    	String url = getApiUrl("statuses/mentions_timeline.json?count=1&max_id=" + (maxId-1));
    	Log.v("DEBUG", url);
    	client.get(url, null, handler);
	}
    
    public void getNewerMentions(long since_id, AsyncHttpResponseHandler handler) {
    	String url = getApiUrl("statuses/mentions_timeline.json?count=5&since_id="+since_id);
    	Log.v("DEBUG", url);
    	client.get(url, null, handler);
	}
    
    public void getVerifyCredentials(AsyncHttpResponseHandler handler) {
    	String url = getApiUrl("account/verify_credentials.json");
    	Log.v("USERNAME", url);
    	client.get(url, null, handler);
	}
    
    public void getUserTimeline(AsyncHttpResponseHandler handler) {
    	String url = getApiUrl("statuses/user_timeline.json?count=5");
    	Log.v("DEBUG", url);
    	client.get(url, null, handler);
	}
    
    public void getOlderUserTimeline(long maxId, AsyncHttpResponseHandler handler) {
    	String url = getApiUrl("statuses/user_timeline.json?count=1&max_id=" + (maxId-1));
    	Log.v("DEBUG", url);
    	client.get(url, null, handler);
	}
    
    public void getNewerUserTimeline(long since_id, AsyncHttpResponseHandler handler) {
    	String url = getApiUrl("statuses/user_timeline.json?count=5&since_id="+since_id);
    	Log.v("DEBUG", url);
    	client.get(url, null, handler);
	}
    
    public void getUsers(String userName, AsyncHttpResponseHandler handler) {
    	String url = getApiUrl("users/show.json?screen_name="+userName);
    	Log.v("DEBUG", url);
    	client.get(url, null, handler);
	}
    
    public void postUpdate(String status, AsyncHttpResponseHandler handler) {
    	String url = "";
		try {
			url = getApiUrl("statuses/update.json?status=" + java.net.URLEncoder.encode(status, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return;
		}
    	Log.v("DEBUG", url);
    	client.post(url, null, handler);
	}
}