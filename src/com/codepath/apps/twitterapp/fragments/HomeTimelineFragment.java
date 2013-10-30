package com.codepath.apps.twitterapp.fragments;

import java.util.ArrayList;

import org.json.JSONArray;

import android.os.Bundle;
import android.util.Log;

import com.codepath.apps.twitterapp.TwitterApp;
import com.codepath.apps.twitterapp.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

public class HomeTimelineFragment extends TweetsListFragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	protected void fetchOlderTweets() {
		Log.v("DEBUG", Long.valueOf(tweets.get(tweets.size() - 1).getTweetId())
				.toString());
		TwitterApp.getRestClient().getOlderHomeTimeline(
				tweets.get(tweets.size() - 1).getTweetId(),
				new JsonHttpResponseHandler() {
					@Override
					public void onFailure(Throwable arg0, String arg1) {
						super.onFailure(arg0, arg1);
					}

					@Override
					public void onSuccess(JSONArray jsonTweets) {
						Log.d("DEBUG", jsonTweets.toString());
						ArrayList<Tweet> oldTweets = Tweet.fromJson(jsonTweets);

						tweets.addAll(tweets.size(), oldTweets);
						Log.d("DEBUG", tweets.toString());
						tweetAdapter.notifyDataSetChanged();

						super.onSuccess(jsonTweets);
					}
				});
	}
	
	protected void fetchNewerTweets() {
		if(tweets.get(0) == null)
			return;
		
		long firstTweetId = tweets.get(0).getTweetId();
		// The first element of the array could be a fake tweet inserted
		// in this app, which will have an id of 0. We want to get the first
		// real tweet id, so we can pass it in to the call to get new tweets
		// since the real tweet id
		while (firstTweetId == 0) {
			tweets.remove(0);
			firstTweetId = tweets.get(0).getTweetId();
		}
		TwitterApp.getRestClient().getNewerHomeTimeline(firstTweetId,
				new JsonHttpResponseHandler() {
					@Override
					public void onFailure(Throwable arg0, String arg1) {
						Log.d("DEBUG", arg1.toString());
						super.onFailure(arg0, arg1);
					}

					@Override
					public void onSuccess(JSONArray jsonTweets) {
						Log.d("DEBUG", jsonTweets.toString());
						ArrayList<Tweet> newTweets = Tweet.fromJson(jsonTweets);

						tweets.addAll(0, newTweets);
						Log.d("DEBUG", tweets.toString());
						tweetAdapter.notifyDataSetChanged();

						super.onSuccess(jsonTweets);
						lvTweets.onRefreshComplete();
					}
				});
	}

	protected void fetchTweets() {
		TwitterApp.getRestClient().getHomeTimeline(
				new JsonHttpResponseHandler() {
					@Override
					protected void handleFailureMessage(Throwable arg0, String arg1) {
						Log.d("DEBUG", "FAILED" + arg1.toString());
						arg0.printStackTrace();
					};
					
					@Override
					public void onFailure(Throwable arg0, String arg1) {
						Log.d("DEBUG", arg1.toString());
						super.onFailure(arg0, arg1);
					}

					@Override
					public void onSuccess(JSONArray jsonTweets) {
						ArrayList<Tweet> newTweets = Tweet.fromJson(jsonTweets);
						tweetAdapter.clear();
						tweetAdapter.addAll(newTweets);
						Log.d("DEBUG", tweets.toString());

						lvTweets.setAdapter(tweetAdapter);

						super.onSuccess(jsonTweets);
					}
				});
	}
}
