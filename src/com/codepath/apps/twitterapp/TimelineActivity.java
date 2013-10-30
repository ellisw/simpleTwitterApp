package com.codepath.apps.twitterapp;

import java.util.Date;

import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.twitterapp.fragments.HomeTimelineFragment;
import com.codepath.apps.twitterapp.fragments.MentionsFragment;
import com.codepath.apps.twitterapp.fragments.TweetsListFragment.OnUserSelectedListener;
import com.codepath.apps.twitterapp.models.Tweet;
import com.codepath.apps.twitterapp.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

public class TimelineActivity extends FragmentActivity implements TabListener, OnUserSelectedListener{

	private static final int REQUEST_CODE = 1;
	private User sessionUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		
		setupNavigationTabs();
		setupUser();
	}

	private void setupNavigationTabs() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);
		Tab tabHome = actionBar.newTab().setText("Home").setTag("HomeTimelineFragment").setTabListener(this);
		Tab tabMentions = actionBar.newTab().setText("Mentions").setTag("MentionsFragment").setTabListener(this);
		actionBar.addTab(tabHome);
		actionBar.addTab(tabMentions);
		actionBar.selectTab(tabHome);
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
				HomeTimelineFragment frag = new HomeTimelineFragment();
				FragmentManager manager = getSupportFragmentManager();
				android.support.v4.app.FragmentTransaction fts = manager
						.beginTransaction();
				fts.replace(R.id.frame_container, frag);
				fts.commit();

				Tweet t = new Tweet();
				t.setTweetId(0);
				t.setText(data.getStringExtra("body"));
				t.setUser(sessionUser);
				t.setCreatedAt(new Date());
				frag.getAdapter().insert(t, 0);
			}
		}
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		FragmentManager manager = getSupportFragmentManager();
		android.support.v4.app.FragmentTransaction fts = manager
				.beginTransaction();
		if (tab.getTag().equals("HomeTimelineFragment")) {
			fts.replace(R.id.frame_container, new HomeTimelineFragment());
		} else if (tab.getTag().equals("MentionsFragment")) {
			fts.replace(R.id.frame_container, new MentionsFragment());
		}
		fts.commit();
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}
	
	public void onProfileView(MenuItem mi) {
		Intent i = new Intent(this, ProfileActivity.class);
		startActivity(i);
	}

	@Override
	public void onUserSelected(String userName) {
		Intent i = new Intent(this, ProfileActivity.class);
		i.putExtra("username", userName);
		startActivity(i);		
	}
}
