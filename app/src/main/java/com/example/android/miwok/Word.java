package com.example.android.miwok;

/**
 * Created by adeen-s on 3/2/17.
 */

public class Word {
    private String mDefaultTranslation;
    private String mMiwokTranslation;
    private int mImageResourceId=-1;
    private int mAudioResourceId=-1;

    public Word(String DefaultTranslation, String MiwokTranslation, int AudioResourceId) {
        mDefaultTranslation = DefaultTranslation;
        mMiwokTranslation = MiwokTranslation;
        mAudioResourceId = AudioResourceId;
    }

    public Word(String DefaultTranslation, String Miwoktranslation, int ImageResourceId, int AudioResourceId) {
        mDefaultTranslation = DefaultTranslation;
        mMiwokTranslation = Miwoktranslation;
        mImageResourceId = ImageResourceId;
        mAudioResourceId = AudioResourceId;
    }

    public String getDefaultTranslation() {
        return mDefaultTranslation;
    }

    public String getMiwokTranslation() {
        return mMiwokTranslation;
    }

    public int getImageResourceId() { return mImageResourceId; }

    public int getAudioResourceId() { return mAudioResourceId; }

    public boolean hasImage() {
        if(mImageResourceId==-1)
            return false;
        return true;
    }
}
