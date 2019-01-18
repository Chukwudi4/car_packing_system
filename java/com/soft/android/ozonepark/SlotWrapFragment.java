package com.soft.android.ozonepark;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.soft.android.ozonepark.util.Slot;
import com.soft.android.ozonepark.util.SlotWrap;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static com.soft.android.ozonepark.ConfirmFragment.SAVED_USERNAME;
import static com.soft.android.ozonepark.LoginTab.AsyncSave.SAVED_NAME;
import static com.soft.android.ozonepark.LoginTab.CONNECTION_TIMEOUT;
import static com.soft.android.ozonepark.LoginTab.READ_TIMEOUT;

/**
 * Created by chukwudi on 4/25/2018.
 */

public class SlotWrapFragment extends android.support.v4.app.Fragment {


    public static SlotWrapFragment newInstance(String fullname) {

        Bundle args = new Bundle();
        args.putString(SAVED_NAME,fullname);
        SlotWrapFragment fragment = new SlotWrapFragment();
        fragment.setArguments(args);
        return fragment;
    }
    private RecyclerView mRecyclerView ;
    private SlotAdapter mAdapter;
    private static int mNormalCrime=0;
    private TextView mFullName;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.slot_list,container,false);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        //SharedPreferences.Editor editor = preferences.edit();
        String full = sharedPreferences.getString(SAVED_NAME, "hahaha");


        mFullName = view.findViewById(R.id.fullname);
        mFullName.setText(full);
        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));



        refreshUI();refreshUI();
        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        refreshUI();
    }



    public void refreshUI(){
        SlotWrap slotwrap =  SlotWrap.get(getActivity());
        List<Slot> slots = slotwrap.getSlotList();

        if(mAdapter ==null){
            mAdapter = new SlotAdapter(slots);
            mRecyclerView.setAdapter(mAdapter);
        }else{
            mAdapter.setSlots(slots);
            mAdapter.notifyDataSetChanged();
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    class SlotHolder extends RecyclerView.ViewHolder{

            private TextView mSlotName ;
        private TextView mUserName;
        private Button mSecureSpace;
        private Slot mSlot;

        public SlotHolder(LayoutInflater inflater,
                           ViewGroup parent) {
            super(inflater.inflate(R.layout.button_item,
                    parent, false));

            mSlotName = (TextView) itemView.findViewById(R.id.slot_name);
            mUserName = (TextView) itemView.findViewById(R.id.user_id);
            mSecureSpace = (Button) itemView.findViewById(R.id.secure_button);

            mSecureSpace.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    //SharedPreferences.Editor editor = preferences.edit();
                    String username  = sharedPreferences.getString(SAVED_USERNAME, "hahaha");
                    new AsyncSecure().execute(username,mSlot.getNumber()+"");
                }
            });
        }
            public void bind(Slot slot){
                mSlot = slot;
                mSlotName.setText("Space " + mSlot.getNumber());
                if(mSlot.isOccupied()) {

                    mSecureSpace.setEnabled(false);
                    mSecureSpace.setText(R.string.occupied);
                    mUserName.setText(mSlot.getUser());
                }else{
                    mSecureSpace.setEnabled(true);
                    mSecureSpace.setText("SECURE");
                    mUserName.setText(mSlot.getUser());
                }
            }

        }

        class SlotAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

            private List<Slot> mSlotList ;

            public SlotAdapter(List<Slot> slotList) {
                mSlotList = slotList;
            }

            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                LayoutInflater inflater=LayoutInflater.from(getActivity());

                return new SlotHolder(inflater,parent);
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

                Slot slot = mSlotList.get(position);
                ((SlotHolder) holder).bind(slot);
            }

            @Override
            public int getItemCount() {
                return mSlotList.size();
            }


            public void setSlots(List<Slot> slots) {
                mSlotList = slots;
            }

            @Override
            public int getItemViewType(int position) {
                return position;
            }
        }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.logout_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.close_application:

                SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor edito = preference.edit();
                edito.clear().commit();
                getActivity().finishAffinity();
                return true;
            case R.id.logout:

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear().commit();

                Intent intent =LoginActivity.newIntent(getActivity());
                startActivity(intent);
                return true;

            case R.id.refresh:
                refreshUI();
            default:


                super.onOptionsItemSelected(item);
        }
        return true;
    }


    class AsyncSecure extends AsyncTask<String, String, String>
    {

        static final String SAVED_NAME = "savedname";
        static final String BOOKED = "booked";
        HttpURLConnection conn;
        URL url = null;
        String response;
        private String slotid;
        private ProgressDialog pdLoading = new ProgressDialog(getActivity());


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tSecuring Space...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }
        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides
                url = new URL("http://"+ getString(R.string.ip_address)+ "//carparkingsystem/updateslot.php");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "exception";
            }
            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);
                response = "";

                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("username", params[0])
                        .appendQueryParameter("slotid", params[1]);


                slotid = params[1];

                String query = builder.build().getEncodedQuery();

                // Open connection for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                response = e1.getMessage();
                return "exception";
            }

            try {

                int response_code = conn.getResponseCode();
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return(result.toString());

                }else{

                    return("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                response = e.getMessage();
                return "exception";
            } finally {
                conn.disconnect();
            }


        }

        @Override
        protected void onPostExecute(String result) {

            //this method will be running on UI thread

            pdLoading.dismiss();

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(BOOKED,"true");

                editor.commit();

                Intent intent = ConfirmActivity.newIntent(getActivity(),"Space "+ slotid);
                startActivity(intent);
                getActivity().finish();

        }
    }
    }

