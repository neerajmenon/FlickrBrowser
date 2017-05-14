package com.example.neerajmenon.flickrbrowser;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by neeraj on 10/5/17.
 */

class FlickrRecyclerViewAdapter extends RecyclerView.Adapter<FlickrRecyclerViewAdapter.FlickrImageViewHolder> {
    private static final String TAG = "FlickrRecyclerViewAdapt";
    private List<Photo> mPhotosList;
    private Context mContext;

    public FlickrRecyclerViewAdapter(List<Photo> photosList, Context context) {
        mPhotosList = photosList;
        mContext = context;
    }

    @Override
    public FlickrImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: starts");

        //called when the layout manager requests a new view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.browse,parent,false);
        //get the inflater from correct context and parent,false ensures correct theme choosing and root non-attachment
        return new FlickrImageViewHolder(view); //constructs a viewholder with the current view and passes it back to LManager
    }

    @Override
    public void onBindViewHolder(FlickrImageViewHolder holder, int position) {
        //?autopass holder by LM or returned from onCreateViewHolder
        //called by recycler view when it wants new data to be stored in a viewHolder
        //code to load the title and load image into the viewholder (use picasso to get img from url)

        Photo photoItem = mPhotosList.get(position);
        Log.d(TAG, "onBindViewHolder: " +photoItem.getTitle() + "-->" +position);
        //singleton -> thus static method
        Picasso.with(mContext).load(photoItem.getImage())
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(holder.thumbnail);
        holder.title.setText(photoItem.getTitle());


    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: ");
        return (mPhotosList !=null && mPhotosList.size()!=0 ? mPhotosList.size():0);
    }

    void loadNewData(List<Photo> newPhotos){
        //IF PHOTOS CHANGE, LOAD THE NEW LIST
        mPhotosList = newPhotos;
        notifyDataSetChanged();  //tells registered observer (RCYView)
    }
    public Photo getPhoto(int position){
        //on tapping a particular photo
        return (mPhotosList!=null && mPhotosList.size()!=0 ? mPhotosList.get(position): null);
        //sends back a photo object at the given position else null
    }

    static class FlickrImageViewHolder extends RecyclerView.ViewHolder{
        private static final String TAG = "FlickrImageViewHolder";
        ImageView thumbnail = null;
        TextView title = null;

        public FlickrImageViewHolder(View itemView) {
            super(itemView); //
            Log.d(TAG, "FlickrImageViewHolder: starts");
            this.thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            this.title = (TextView) itemView.findViewById(R.id.title);
        }
    }
}
