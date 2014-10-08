package com.gdacarv.app.seratissample.activities;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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


public class MainListingActivity extends ActionBarActivity {
	
	private Tab mPatientsTab, mProvidersTab;
	private ListView mListView;
	private View mProgressView;
	private TextView mEmptyTextView;
	
	private UrlLoader mUrlLoader;
	private Parser<List<Person>> mPatientListParser;
	private Parser<List<Person>> mProviderListParser;
	
	private List<Person> mPatients, mProviders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing);
        
        mListView = (ListView) findViewById(R.id.list);
        mProgressView = findViewById(R.id.progress);
        mEmptyTextView = (TextView) findViewById(R.id.empty_text);
        
        mListView.setOnItemClickListener(onItemClickListener );
        
        // setup action bar for tabs
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        //actionBar.setDisplayShowTitleEnabled(false);

        mPatientsTab = actionBar.newTab()
                           .setText(R.string.patients)
                           .setTabListener(mTabListener);
        actionBar.addTab(mPatientsTab);

        mProvidersTab = actionBar.newTab()
                       .setText(R.string.providers)
                       .setTabListener(mTabListener);
        actionBar.addTab(mProvidersTab);
        
        mUrlLoader = new UrlLoader();
        mPatientListParser = new PersonJsonParser(Patient.class);
        mProviderListParser = new PersonJsonParser(Provider.class);
        mUrlLoader.load(getString(R.string.url_patients), mPatientListParser, patientReceiver);
        mUrlLoader.load(getString(R.string.url_providers), mProviderListParser, providerReceiver);
    }
    
    private TabListener mTabListener = new TabListener() {
		
		@Override
		public void onTabUnselected(Tab arg0, FragmentTransaction arg1) { }
		
		@Override
		public void onTabSelected(Tab tab, FragmentTransaction arg1) {
			reloadList(tab);
		}
		
		@Override
		public void onTabReselected(Tab arg0, FragmentTransaction arg1) { }
	};
	
	private Receiver<List<Person>> patientReceiver = new Receiver<List<Person>>() {
		
		@Override
		public void onReceive(List<Person> patients) {
			mPatients = patients;
			reloadList(mPatientsTab);
		}
	};
	
	private Receiver<List<Person>> providerReceiver = new Receiver<List<Person>>() {
		
		@Override
		public void onReceive(List<Person> providers) {
			mProviders = providers;
			reloadList(mProvidersTab);
		}
	};

	private void reloadList(Tab tab) {
		Tab selectedTab = getSupportActionBar().getSelectedTab();
		if(selectedTab != tab)
			return;
		List<Person> list;
		String msg;
		if(selectedTab == mPatientsTab) {
			list = mPatients;
			msg = getString(R.string.patients);
		} else{
			list = mProviders;
			msg = getString(R.string.providers);
		}
		msg = String.format(getString(R.string.empty_msg), msg);

		if(list != null) {
			mListView.setAdapter(new ArrayAdapter<Person>(this, android.R.layout.simple_list_item_1, list));
			mListView.setVisibility(View.VISIBLE);
			mProgressView.setVisibility(View.INVISIBLE);
			mEmptyTextView.setVisibility(list.isEmpty() ? View.VISIBLE : View.INVISIBLE);
			mEmptyTextView.setText(msg);
		}else{
			mListView.setVisibility(View.INVISIBLE);
			mProgressView.setVisibility(View.VISIBLE);
			mEmptyTextView.setVisibility(View.INVISIBLE);
		}
	}

	private OnItemClickListener onItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Person person = (Person) parent.getItemAtPosition(position);
			String firstName = person.getFirstName();
			String kind = getString(person instanceof Patient ? R.string.providers : R.string.patients);
			String url = getString(person instanceof Patient ? R.string.url_patients : R.string.url_providers) + person.getId();
			Intent i = new Intent(MainListingActivity.this, AssociatedListingActivity.class);
			i.putExtra(AssociatedListingActivity.EXTRA_TITLE, String.format(getString(R.string.title_activity_associated_listing), firstName, kind));
			i.putExtra(AssociatedListingActivity.EXTRA_URL, url);
			startActivity(i);
		}
	};
	
}
