package com.codepath.apps.twitterapp;

import org.json.JSONArray;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

public class ComposeActivity extends Activity {

	private EditText body;
	private TextView counter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compose);
		body = (EditText) findViewById(R.id.et_tweet_text);
		counter = (TextView) findViewById(R.id.tv_counter);

		body.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				String text = ((EditText) v).getText().toString();
				counter.setText(text.length() + "/140");
				return false;
			}
		});
	}

	public void tweet(View v) {
		if(body.getText().toString().length() > 140){
			Toast.makeText(this, "Tweets can not be greater than 140 characters", Toast.LENGTH_LONG).show();
			return;
		}
		TwitterApp.getRestClient().postUpdate(body.getText().toString(),
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONArray jsonTweets) {
					}

					@Override
					public void onFinish() {
					}
				});
		Intent data = new Intent();
		data.putExtra("body", body.getText().toString());
		setResult(RESULT_OK, data);
		finish();
	}
	
	public void cancel(View v){
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.compose, menu);
		return true;
	}

}
