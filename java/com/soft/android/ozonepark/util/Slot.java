package com.soft.android.ozonepark.util;

/**
 * Created by chukwudi on 4/24/2018.
 */

public class Slot {

    private boolean mOccupied;
    private int mNumber;
    private String mUser;



    public String getUser() {
        return mUser;
    }

    public void setUser(String user) {
        mUser = user;
    }

    public boolean isOccupied() {
        return mOccupied;
    }

    public void setOccupied(boolean occupied) {
        this.mOccupied = occupied;
    }

    public int getNumber() {
        return mNumber;
    }

    public Slot() {

    }


    public Slot( int number,boolean occupied, String user){
        this.mNumber= number; this.mUser=user ; mOccupied = occupied;
    }
}
