package com.soft.android.ozonepark;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ConfirmActivity extends AppCompatActivity {


    static final String SLOTNAME = "slot";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);


        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.confirm_fragment);
        Intent intent = getIntent();

        String slot = intent.getStringExtra(SLOTNAME);
        if(fragment==null){
            fragment = ConfirmFragment.newInstance(slot);

            fm.beginTransaction().add(R.id.confirm_fragment,fragment).commit();
        }

    }

    public static Intent newIntent(Context activity, String slotName) {

        Intent intent = new Intent(activity,ConfirmActivity.class);

        intent.putExtra(SLOTNAME,slotName);
        return intent;
    }
}
