package com.soft.android.ozonepark;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


public class LoginActivity extends AppCompatActivity {

    private static final String SAVED_USERNAME = "username";//the layout for the login body

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        //SharedPreferences.Editor editor = preferences.edit();
        String username = sharedPreferences.getString(SAVED_USERNAME, "hahaha");

        if(!username.equals("hahaha")){
            Intent intent =SlotListActivity.newIntent(LoginActivity.this,username,"tabans");
            startActivity(intent);
        }

        setContentView(R.layout.activity_login);


        FragmentManager fm = getSupportFragmentManager();//get fragmentmanager

        Fragment fragment = fm.findFragmentById(R.id.login_fragment); //create fragment

        if(fragment == null){ //check if fragment is null and assigns a class to the fragment
            fragment =  new LoginFragment();
            fm.beginTransaction().add(R.id.login_fragment,fragment).commit();
        }
    }



    public static Intent newIntent(Context packageContext){

        Intent intent = new Intent(packageContext,LoginActivity.class);

        return intent;
    }





}
