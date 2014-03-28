package com.allysonpower.ui;

import android.app.Activity;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import android.app.ListActivity;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.allysonpower.parse.AllysonNewsInfo;
import com.markupartist.android.widget.PullToRefreshListView;
import com.markupartist.android.widget.PullToRefreshListView.OnRefreshListener;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

public class partb extends ListActivity {    
    private LinkedList<String> mListItems;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.partb_home);
        
        // Set a listener to be invoked when the list should be refreshed.
        ((PullToRefreshListView) getListView()).setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Do work to refresh the list here.
                new GetDataTask().execute();
            }
        });
        
        //regist the subclass of ParseObject first and then call Parse.initialize
        ParseObject.registerSubclass(AllysonNewsInfo.class);
        Parse.initialize(this, "bDExeWi2vct7yqm52r5WPnEiuNyorLu9B2tSFREW", "MvzGGKqOt5Q56inQCPNpbYLAaiJmtykCjgh93C1K");
        
		ParseQuery<AllysonNewsInfo> query = ParseQuery.getQuery(AllysonNewsInfo.class);
		
		query.orderByDescending("updatedAt");
		query.setLimit(10); 
		//query.setSkip(10);   //You can skip the first results with setSkip. This can be useful for pagination:
		
		query.findInBackground(new FindCallback<AllysonNewsInfo>() {
			@Override
			public void done(List<AllysonNewsInfo> newsList, ParseException e) {
				// TODO Auto-generated method stub
				if (e == null) {
					Log.e("score", "Retrieved " + newsList.size() + "scores~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
					
				} else {
					Log.d("score", "Error: " + e.getMessage());
				}
			}
		});
       
		mListItems = new LinkedList<String>();
		mListItems.addAll(Arrays.asList(mStrings));
		
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, mListItems);

        setListAdapter(adapter);
    }

    private class GetDataTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
            // Simulates a background job.
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                ;
            }
            return mStrings;
        }

        @Override
        protected void onPostExecute(String[] result) {
            mListItems.addFirst("Added after refresh...");

            // Call onRefreshComplete when the list has been refreshed.
            ((PullToRefreshListView) getListView()).onRefreshComplete();

            super.onPostExecute(result);
        }
    }

    private String[] mStrings = {
            "Abbaye de Belloc", "Abbaye du Mont des Cats", "Abertam",
            "Abondance", "Ackawi", "Acorn", "Adelost", "Affidelice au Chablis",
            "Afuega'l Pitu", "Airag", "Airedale", "Aisy Cendre",
            "Allgauer Emmentaler"};
}

