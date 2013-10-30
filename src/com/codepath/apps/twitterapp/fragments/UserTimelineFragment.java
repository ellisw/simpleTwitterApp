package com.codepath.apps.twitterapp.fragments;

import java.util.ArrayList;

import org.json.JSONArray;

import android.util.Log;

import com.codepath.apps.twitterapp.TwitterApp;
import com.codepath.apps.twitterapp.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

public class UserTimelineFragment extends TweetsListFragment {


	protected void fetchOlderTweets() {
		Log.v("DEBUG", Long.valueOf(tweets.get(tweets.size() - 1).getTweetId())
				.toString());
		TwitterApp.getRestClient().getOlderUserTimeline(
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
		TwitterApp.getRestClient().getNewerUserTimeline(firstTweetId,
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
		TwitterApp.getRestClient().getUserTimeline(
				new JsonHttpResponseHandler() {
					protected void handleFailureMessage(Throwable arg0, String arg1) {
						Log.d("DEBUG", arg1.toString());
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
