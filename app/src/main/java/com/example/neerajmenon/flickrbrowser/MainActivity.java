package com.example.neerajmenon.flickrbrowser;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

public class MainActivity extends AppCompatActivity implements GetJsonFlickrData.OnDataAvailable{
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: starts");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        GetJsonFlickrData getJsonFlickrData = new GetJsonFlickrData(this,"https://api.flickr.com/services/feeds/photos_public.gne","en-us",true);
        getJsonFlickrData.executeOnSameThread("dexter,morgan");


        Log.d(TAG, "onCreate: ends");

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
        if(status == DownloadStatus.OK){
            Log.d(TAG, "OnDataAvailable: Download Status: "+status);
        }
        else{
            Log.d(TAG, "OOnDataAvailable: Download Status: " +status);
        }

    }
}
