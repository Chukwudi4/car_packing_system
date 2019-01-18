package com.soft.android.ozonepark.util;

import android.support.v7.app.AppCompatActivity;

import com.soft.android.ozonepark.R;

/**
 * Created by chukwudi on 5/28/2018.
 */

public class IpAddress extends AppCompatActivity{
    private String IP_ADDRESS = getString(R.string.ip_address);

    public String getIP_ADDRESS() {
        return IP_ADDRESS;
    }
}
