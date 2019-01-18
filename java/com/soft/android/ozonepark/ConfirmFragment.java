package com.soft.android.ozonepark;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.TextView;

import static com.soft.android.ozonepark.ConfirmActivity.SLOTNAME;

import static com.soft.android.ozonepark.LoginTab.AsyncSave.SAVED_NAME;

/**
 * Created by chukwudi on 5/11/2018.
 */

public class ConfirmFragment extends android.support.v4.app.Fragment {

    private Chronometer mChronometer;    static final String SAVED_USERNAME = "username";//the layout for the login body

    private TextView mUsername , mSlotName, mFullName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Bundle args = getArguments();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        //SharedPreferences.Editor editor = preferences.edit();
        String username  = sharedPreferences.getString(SAVED_USERNAME, "hahaha");
        String slotname = args.getString(SLOTNAME,"network error");
        String fullname = sharedPreferences.getString(SAVED_NAME, "hahaha");
        View view = inflater.inflate(R.layout.fragment_confirm,container,false);
        mFullName = view.findViewById(R.id.fullname);
        mFullName.setText(fullname);
        mUsername = view.findViewById(R.id.username);
        mSlotName = view.findViewById(R.id.slot_name);
        mUsername.setText(username);
        mSlotName.setText(slotname);

        mChronometer = (Chronometer) view.findViewById(R.id.chronometer);
        mChronometer.setBase(SystemClock.elapsedRealtime());
        mChronometer.start();
        mChronometer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mChronometer.stop();
            }
        });
        return view;
    }


    public static ConfirmFragment newInstance( String slot) {

        Bundle args = new Bundle();

        args.putString(SLOTNAME,slot);
        ConfirmFragment fragment = new ConfirmFragment();
        fragment.setArguments(args);
        return fragment;
    }


}
