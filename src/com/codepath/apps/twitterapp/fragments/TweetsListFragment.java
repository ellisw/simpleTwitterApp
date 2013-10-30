package com.codepath.apps.twitterapp.fragments;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.codepath.apps.twitterapp.EndlessScrollListener;
import com.codepath.apps.twitterapp.R;
import com.codepath.apps.twitterapp.TweetAdapter;
import com.codepath.apps.twitterapp.models.Tweet;
import com.codepath.apps.twitterapp.models.User;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public abstract class TweetsListFragment extends Fragment {
	protected TweetAdapter tweetAdapter;
	protected ArrayList<Tweet> tweets;
	protected PullToRefreshListView lvTweets;
	
	private OnUserSelectedListener listener;
	
	public TweetAdapter getAdapter(){
		return tweetAdapter;
	}
	
	public interface OnUserSelectedListener {
		public void onUserSelected(String userName);
	}
	
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		if (activity instanceof OnUserSelectedListener){
			listener = (OnUserSelectedListener) activity;
		} else {
			throw new ClassCastException(activity.toString() + 
					"must impelement TweetsListFragment.OnUserSelectedListener");
		}
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_tweets_list, container, false);
		lvTweets = (PullToRefreshListView) view.findViewById(R.id.lvTweets);


		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		tweets = new ArrayList<Tweet>();
		tweetAdapter = new TweetAdapter(getActivity(), tweets);
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
		
		lvTweets.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position,
					long arg) {
				Tweet tweet = tweets.get(position);
				User u = tweet.getUser();
				listener.onUserSelected(u.getScreenName());
			}
		});
		fetchTweets();
	}
	
	protected abstract void fetchOlderTweets();
	protected abstract void fetchNewerTweets();
	protected abstract void fetchTweets();

}
