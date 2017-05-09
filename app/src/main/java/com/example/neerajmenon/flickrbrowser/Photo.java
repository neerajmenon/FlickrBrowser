package com.example.neerajmenon.flickrbrowser;

/**
 * Created by neeraj on 9/5/17.
 */

class Photo {
    String mTitle;
    String mAuthor;
    String mAuthorID;
    String mTags;
    String mImage;
    String mLink;

    public Photo(String title,String author, String authorID,String tags, String image,String link) {
        mTitle = title;
        mAuthor = author;
        mAuthorID = authorID;
        mTags = tags;
        mImage = image;
        mLink = link;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getAuthorID() {
        return mAuthorID;
    }

    public String getTags() {
        return mTags;
    }

    public String getImage() {
        return mImage;
    }

    public String getLink() {
        return mLink;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "mTitle='" + mTitle + '\'' +
                ", mAuthor='" + mAuthor + '\'' +
                ", mAuthorID='" + mAuthorID + '\'' +
                ", mTags='" + mTags + '\'' +
                ", mImage='" + mImage + '\'' +
                ", mLink='" + mLink + '\'' +
                '}';
    }
}
