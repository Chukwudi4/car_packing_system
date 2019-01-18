package com.soft.android.ozonepark;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import static com.soft.android.ozonepark.LoginTab.AsyncSave.SAVED_NAME;

/**
 * Created by chukwudi on 4/25/2018.
 */

public class SlotListActivity extends AppCompatActivity {


    private static final String USERNAME_KEY = "";

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.login_fragment); //create fragment

        if(fragment == null){ //check if fragment is null and assigns a class to the fragment
            fragment =  SlotWrapFragment.newInstance(getIntent().getStringExtra(SAVED_NAME));
            fm.beginTransaction().add(R.id.login_fragment,fragment).commit();
        }
    }


    @Override
    public void finish() {
        super.finish();
        finishAffinity();
    }

    public static Intent newIntent(FragmentActivity activity, String username, String fullname) {

        Intent intent = new Intent(activity,SlotListActivity.class);
        intent.putExtra(USERNAME_KEY,username);
        intent.putExtra(SAVED_NAME,fullname);

        return intent ;
    }
}
