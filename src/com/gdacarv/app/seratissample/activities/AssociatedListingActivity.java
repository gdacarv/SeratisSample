package com.gdacarv.app.seratissample.activities;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.gdacarv.app.seratissample.R;
import com.gdacarv.app.seratissample.model.Patient;
import com.gdacarv.app.seratissample.model.Person;
import com.gdacarv.app.seratissample.model.Provider;
import com.gdacarv.app.seratissample.network.Parser;
import com.gdacarv.app.seratissample.network.PersonJsonParser;
import com.gdacarv.app.seratissample.network.Receiver;
import com.gdacarv.app.seratissample.network.UrlLoader;

public class AssociatedListingActivity extends ActionBarActivity {
	
	protected static final String EXTRA_TITLE = "extra_title";
	protected static final String EXTRA_URL = "extra_url";
	
	private ListView mListView;
	private View mProgressView;
	private TextView mEmptyTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listing);
        
        mListView = (ListView) findViewById(R.id.list);
        mProgressView = findViewById(R.id.progress);
        mEmptyTextView = (TextView) findViewById(R.id.empty_text);
        
        Intent intent = getIntent();
		setTitle(intent.getStringExtra(EXTRA_TITLE));
        
        UrlLoader urlLoader = new UrlLoader();
        Parser<List<Person>> parser = new PersonJsonParser();
        urlLoader.load(intent.getStringExtra(EXTRA_URL), parser, receiver);
	}

	private Receiver<List<Person>> receiver = new Receiver<List<Person>>() {
		
		@Override
		public void onReceive(List<Person> list) {
			mListView.setAdapter(new ArrayAdapter<Person>(AssociatedListingActivity.this, android.R.layout.simple_list_item_1, list));
			mListView.setVisibility(View.VISIBLE);
			mProgressView.setVisibility(View.INVISIBLE);
			mEmptyTextView.setVisibility(list == null || list.isEmpty() ? View.VISIBLE : View.INVISIBLE);
			mEmptyTextView.setText(String.format(getString(R.string.empty_msg), getTitle()));
		}
	};
}
