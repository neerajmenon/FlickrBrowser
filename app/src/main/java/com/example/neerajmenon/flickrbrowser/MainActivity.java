package com.example.neerajmenon.flickrbrowser;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements GetJsonFlickrData.OnDataAvailable{
    private static final String TAG = "MainActivity";
    private FlickrRecyclerViewAdapter mFlickrRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: starts");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //LM does the managing, thus delegating responsibility makes RV more flexible
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mFlickrRecyclerViewAdapter = new FlickrRecyclerViewAdapter(new ArrayList<Photo>(),this);
        recyclerView.setAdapter(mFlickrRecyclerViewAdapter);



        Log.d(TAG, "onCreate: ends");

    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: start");
        GetJsonFlickrData getJsonFlickrData = new GetJsonFlickrData(this,"https://api.flickr.com/services/feeds/photos_public.gne","en-us",true);
        //getJsonFlickrData.executeOnSameThread("dexter,morgan"); //  RUN SYNCHRONOUSLY
        getJsonFlickrData.execute("dexter,morgan");               //  RUN VIA ASYNCTASK
        super.onResume();

        Log.d(TAG, "onResume: ends");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        Log.d(TAG, "onCreateOptionsMenu: IN");
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        Log.d(TAG, "onCreateOptionsMenu: OUT");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void OnDataAvailable(List<Photo> data, DownloadStatus status) {
        //Put list contents into recycler view
        Log.d(TAG, "OnDataAvailable: starts");
        if(status == DownloadStatus.OK){
            mFlickrRecyclerViewAdapter.loadNewData(data);
        }
        else{ //download or processing failed
            Log.d(TAG, "OOnDataAvailable: failed with status : " +status);
        }

    }
}
