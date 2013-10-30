package com.codepath.apps.twitterapp.fragments;

import java.util.ArrayList;

import org.json.JSONArray;

import com.codepath.apps.twitterapp.TwitterApp;
import com.codepath.apps.twitterapp.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.os.Bundle;
import android.util.Log;

public class MentionsFragment extends TweetsListFragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
	}

	protected void fetchNewerTweets() {
		if(tweets.get(0) == null)
			return;
		
		long firstTweetId = tweets.get(0).getTweetId();
		TwitterApp.getRestClient().getNewerMentions(firstTweetId,
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
		TwitterApp.getRestClient().getMentions(
				new JsonHttpResponseHandler() {
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

	protected void fetchOlderTweets() {
		Log.v("DEBUG", Long.valueOf(tweets.get(tweets.size() - 1).getTweetId())
				.toString());
		TwitterApp.getRestClient().getOlderMentions(
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
	
}
