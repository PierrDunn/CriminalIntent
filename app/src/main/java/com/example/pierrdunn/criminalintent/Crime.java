package com.example.pierrdunn.criminalintent;

import java.util.Date;
import java.util.UUID;
import java.util.Random;

/**
 * Created by pierrdunn on 24.02.18.
 */

public class Crime {
    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mSolved;
    private String mSuspect;

    public Crime(){
        this(UUID.randomUUID());
    }

    /*
    public Crime(String mTitle, boolean mSolved){
        mId = UUID.randomUUID();
        this.mTitle = mTitle;
        mDate = new Date();
        this.mSolved = mSolved;
    }
    */
    public Crime(UUID id){
        mId = id;
        mDate = new Date();
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    public String getSuspect() {
        return mSuspect;
    }

    public void setSuspect(String suspect) {
        mSuspect = suspect;
    }

}
