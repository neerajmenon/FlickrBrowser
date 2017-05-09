package com.example.neerajmenon.flickrbrowser;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by neeraj on 9/5/17.
 */
enum DownloadStatus {IDLE, PROCESSING, NOT_INITIALIZED, FAILED_OR_EMPTY, OK}



class GetRawData extends AsyncTask<String, Void, String> {

    private static final String TAG = "GetRawData";
    private DownloadStatus mDownloadStatus;
    OnDownloadComplete mCallback;

    interface OnDownloadComplete{
        void OnDownloadComplete(String data,DownloadStatus status);

    }

    public GetRawData(OnDownloadComplete callback) {
        mDownloadStatus = DownloadStatus.IDLE;
        mCallback = callback;
    }

    @Override
    protected void onPostExecute(String s) {
        Log.d(TAG, "onPostExecute: " +s);
        if(mCallback !=null)
        {
            mCallback.OnDownloadComplete(s,mDownloadStatus);
        }
    }

    @Override
    protected String doInBackground(String... params) {

        HttpURLConnection connection = null;
        BufferedReader reader = null;
        if(params == null){
            Log.d(TAG, "doInBackground: NO PARAMS!");
            mDownloadStatus = DownloadStatus.NOT_INITIALIZED;
            return  null;
        }
        try{

            mDownloadStatus = DownloadStatus.PROCESSING;
            URL url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int reponse = connection.getResponseCode();
            Log.d(TAG, "doInBackground: Repsonse code: " +reponse);
            StringBuilder result = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            //READING PER LINE FROM INPUT STREAM
            while(null != (line = reader.readLine())){
                result.append(line).append("\n");
            }
            
            mDownloadStatus = DownloadStatus.OK;
            Log.d(TAG, "doInBackground: Download Completed");
            return result.toString();

        }catch(MalformedURLException e)
        {
            Log.e(TAG, "doInBackground: Error in URL" + e.getMessage() );
        }
        catch (IOException e){
            Log.e(TAG, "doInBackground: IOExeception " +e.getMessage() );
        }
        catch (SecurityException e){
            Log.e(TAG, "doInBackground: Security Exception, Permissions? " +e.getMessage() );
        }
        finally {
            if(connection != null)
            connection.disconnect();
            if(reader!=null)
            {
                try{
                    reader.close();
                }catch(IOException e)
                {
                    Log.e(TAG, "doInBackground: Error closing the stream" + e.getMessage() );
                }
            }

        }
        mDownloadStatus = DownloadStatus.FAILED_OR_EMPTY;
        return null;

    }
}
