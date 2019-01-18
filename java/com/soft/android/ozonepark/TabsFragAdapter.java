package com.soft.android.ozonepark;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chukwudi on 4/23/2018.
 */

public class TabsFragAdapter extends FragmentPagerAdapter {// Adapter for viewpager

    //declare data sets for the fragments for the view pager
    List<Fragment> mFragmentList = new ArrayList<>();
    List<String> mFragmentListTitle = new ArrayList<>();

    public TabsFragAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) { //getposition of viewpager
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {//get number of fragmenrts in viewpager
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment, String title){

        mFragmentList.add(fragment);
        mFragmentListTitle.add(title);
    }//add neew fragment to viewpager

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentListTitle.get(position);
    } //get title of page
}
