package com.example.neerajmenon.flickrbrowser;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neeraj on 9/5/17.
 */

class GetJsonFlickrData extends AsyncTask<String,Void,List<Photo>>implements GetRawData.OnDownloadComplete {
    private static final String TAG = "GetJsonFlickrData";

    private List<Photo> mPhotoList = null;
    private String mBaseUrl;
    private String mLanguage;
    private boolean matchAll = true;
    private OnDataAvailable mCallback;
    private boolean runningOnSameThread = false;

    interface OnDataAvailable{
        void OnDataAvailable(List<Photo> data, DownloadStatus status);
    }




    public GetJsonFlickrData(OnDataAvailable callback,String baseUrl, String language, boolean matchAll) {
        mCallback = callback;
        mBaseUrl = baseUrl;
        mLanguage = language;
        this.matchAll = matchAll;
    }
    void executeOnSameThread(String searchCriteria){
        // same thread execution
        runningOnSameThread = true;
        String destinationUri = createUri(searchCriteria,mLanguage,matchAll);
        GetRawData getRawData = new GetRawData(this);
        getRawData.execute(destinationUri);

    }

    @Override
    protected void onPostExecute(List<Photo> photos) {
        Log.d(TAG, "onPostExecute: starts");
        if(mCallback != null)
        {
            mCallback.OnDataAvailable(mPhotoList,DownloadStatus.OK);
        }
        Log.d(TAG, "onPostExecute: ends");
    }

    @Override
    protected List<Photo> doInBackground(String... params) {
        //background thread execution
        String destinationUri = createUri(params[0],mLanguage,matchAll);
        GetRawData getRawData = new GetRawData(this);
       // getRawData.execute(destinationUri); cannot call BG thread from BG thread
        getRawData.runOnSameThread(destinationUri);
        return mPhotoList; // once the onDownloadComplete below is done and completed (GRD in sync) list is returned to onPostExcecute
    }

    String createUri(String searchCriteria, String mLanguage, boolean matchAll)
    {
        return Uri.parse(mBaseUrl).buildUpon()
                .appendQueryParameter("tags",searchCriteria)
                .appendQueryParameter("format","json")
                .appendQueryParameter("lang",mLanguage)
                .appendQueryParameter("tagmode",matchAll ? "ALL":"ANY")
                .appendQueryParameter("nojsoncallback","1")
                .build().toString();
    }

    @Override
    public void OnDownloadComplete(String data, DownloadStatus status) {
        if(status == DownloadStatus.OK){
            Log.d(TAG, "OnDownloadComplete: Download Status: "+status);
            Log.d(TAG, "OnDownloadComplete: GOING TO START JSON PARSING NOW..");
            //JSON DATA RETRIEVED, PARSING
            mPhotoList = new ArrayList<Photo>();
            try
            {
                JSONObject jsonData = new JSONObject(data);
                JSONArray itemsArray = jsonData.getJSONArray("items");
                for(int i = 0;i<itemsArray.length();i++){
                    JSONObject jsonPhoto = itemsArray.getJSONObject(i);
                    String title = jsonPhoto.getString("title");
                    String author = jsonPhoto.getString("author");
                    String author_id = jsonPhoto.getString("author_id");
                    String tags = jsonPhoto.getString("tags");
                    JSONObject jsonMedia = jsonPhoto.getJSONObject("media");
                    String photoUrl = jsonMedia.getString("m");
                    String link = photoUrl.replaceFirst("_m.","_b.");
                    Photo photo = new Photo(title,author,author_id,tags,photoUrl,link);
                    mPhotoList.add(photo);
                    Log.d(TAG, "OnDownloadComplete: ADDED PHOTO: " +photo.toString());
                }
                if(runningOnSameThread && mCallback != null)
                {
                    mCallback.OnDataAvailable(mPhotoList,status);
                }


            }catch (JSONException jsone)
            {
                Log.e(TAG, "OnDownloadComplete: JSONException, problem in the downloaded json?: " + jsone.getMessage());
                status = DownloadStatus.FAILED_OR_EMPTY; //something wrong with the recieved json :(
            }


            }
            else{
            Log.d(TAG, "OnDownloadComplete: Download Status: " +status);
        }

    }
}
