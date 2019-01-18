package com.soft.android.ozonepark;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by chukwudi on 4/22/2018.
 */

public class LoginFragment extends Fragment {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    public static LoginFragment newInstance() {//method to call this fragment
        
        Bundle args = new Bundle();
        
        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;


    }
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //Button button = new Button(getContext());
        //button.setText(R.string.testvalue);

       // View headerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
         //       .inflate(R.layout.custom_tab, null, false);

        View view = inflater.inflate(R.layout.login_register_frag,container,false);

        mViewPager = view.findViewById(R.id.viewpager);
        setupWithViewPager(mViewPager);//declare viewpager and set it up

        mTabLayout = view.findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);//declare tablayout and set it up

        View headerView = inflater.inflate(R.layout.home_layout, null, false);//inflate a fresh view

        //inflate the headers of the tabs
        LinearLayout linearLayoutOne = (LinearLayout) headerView.findViewById(R.id.login_button);
        LinearLayout linearLayout2 = (LinearLayout) headerView.findViewById(R.id.register_button);

        //set the header for the tab
        mTabLayout.getTabAt(0).setCustomView(linearLayoutOne);

        mTabLayout.getTabAt(1).setCustomView(linearLayout2);



        return view;
    }

    private void setupWithViewPager(ViewPager viewPager){//sync tab and viewpager
        TabsFragAdapter fragAdapter = new TabsFragAdapter(getFragmentManager());
        fragAdapter.addFragment(new LoginTab(),"ONE");
        fragAdapter.addFragment(new RegisterTab(),"TWO");

        viewPager.setAdapter(fragAdapter);
    }



}
