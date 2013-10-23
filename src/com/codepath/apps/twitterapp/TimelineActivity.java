package com.codepath.apps.twitterapp;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.twitterapp.models.Tweet;
import com.codepath.apps.twitterapp.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class TimelineActivity extends Activity {

	private static final int REQUEST_CODE = 1;
	private TweetAdapter tweetAdapter;
	private ArrayList<Tweet> tweets;
	private PullToRefreshListView lvTweets;
	private User sessionUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);

		lvTweets = (PullToRefreshListView) findViewById(R.id.lvTweets);
		tweets = new ArrayList<Tweet>();
		tweetAdapter = new TweetAdapter(getBaseContext(), tweets);
		lvTweets.setOnScrollListener(new EndlessScrollListener() {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				fetchOlderTweets();
			}
		});

		lvTweets.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				fetchNewerTweets();
			}
		});

		setupUser();
		fetchTweets();
	}

	private void setupUser() {
		TwitterApp.getRestClient().getVerifyCredentials(
				new JsonHttpResponseHandler() {
					@Override
					public void onFailure(Throwable arg0, String arg1) {
						Log.d("USERNAME", arg1.toString());
					}

					@Override
					public void onSuccess(JSONObject jsonUser) {
						Log.d("USERNAME", jsonUser.toString());
						sessionUser = User.fromJson(jsonUser);

						Log.d("USERNAME", sessionUser.getScreenName());
						Log.d("USER", sessionUser.toString());
					}
				});
	}

	private void fetchOlderTweets() {
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

	private void fetchNewerTweets() {
		long firstTweetId = tweets.get(0).getTweetId();
		// The first element of the array could be a fake tweet inserted
		// in this app, which will have an id of 0. We want to get the first
		// real tweet id, so we can pass it in to the call to get new tweets
		// since the real tweet id
		while(firstTweetId == 0){
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

	private void fetchTweets() {
		TwitterApp.getRestClient().getHomeTimeline(
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.timeline, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.actionCompose) {
			Intent i = new Intent(this, ComposeActivity.class);
			startActivityForResult(i, REQUEST_CODE);
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
			if (data.hasExtra("body")) {
				Tweet t = new Tweet();
				t.setTweetId(0);
				t.setText(data.getStringExtra("body"));
				t.setUser(sessionUser);
				t.setCreatedAt(new Date());
				tweets.add(0, t);
				tweetAdapter.notifyDataSetChanged();
			}
		}
	}
}
