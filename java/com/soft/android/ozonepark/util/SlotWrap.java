package com.soft.android.ozonepark.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.soft.android.ozonepark.ConfirmActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import java.util.ArrayList;
import java.util.List;
import static com.soft.android.ozonepark.LoginTab.CONNECTION_TIMEOUT;
import static com.soft.android.ozonepark.LoginTab.READ_TIMEOUT;

/**
 * Created by chukwudi on 4/24/2018.
 */

public class SlotWrap {

    private List<Slot> mSlotList ;
    private Context mContext;
    private static SlotWrap sSlotWrap;
    private ProgressDialog pdLoading;
    private static final String SAVED_USERNAME = "username";//the layout for the login body

    public List<Slot> getSlotList() {
        new AsyncList().execute();
        return mSlotList;
    }

    public static SlotWrap get(Context context) {

        if (sSlotWrap== null){

            sSlotWrap = new SlotWrap(context);


        }
        return sSlotWrap;
    }

    private SlotWrap(Context context) {
        mContext = context.getApplicationContext();

        mSlotList = getDefaultSlotList();
        new AsyncList().execute();

    }



    class AsyncList extends AsyncTask<String, String, String>
    {

        static final String SAVED_NAME = "";
        HttpURLConnection conn;
        URL url = null;
        String response;



        public AsyncList() {
            pdLoading = new ProgressDialog(mContext);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
        //    pdLoading.setMessage("\tLoading List...");
          //  pdLoading.setCancelable(false);
            //pdLoading.show();

        }
        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides
                url = new URL("http://192.168.43.229//carparkingsystem/slotlist.php");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                mSlotList = getDefaultSlotList();
                e.printStackTrace();
                return "exception";
            }
            /*try {
                // Setup HttpURLConnection class to send and receive data from php and mysql


                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("username", params[0]);

                username = params[0];

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
                mSlotList = getDefaultSlotList();
                return "exception";
            }*/

            try {
                conn = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                //conn.setDoOutput(true);
                response = "";

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line );
                    }

                    // Pass data to onPostExecute method
                    return(result.toString());

                }else{
                    mSlotList = getDefaultSlotList();
                    return("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                response = e.getMessage();
                mSlotList = getDefaultSlotList();


                return "exception";
            } finally {
                conn.disconnect();
            }


        }

        @Override
        protected void onPostExecute(String result) {

            //this method will be running on UI thread

//            pdLoading.dismiss();



            try {

                List <Slot> slotList = new ArrayList<>();


                //result = result.replace(result.substring(result.length()-1), "");
                result = result.substring(0,result.length()-1);
                String amps = "[" + result + "]";
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
                String username= sharedPreferences.getString(SAVED_USERNAME, "hahaha");

                // result is a variable which holds fetched json data.
                JSONArray jArray = new JSONArray(amps);

                for (int i=0;i< jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i);

                    int slotid = json_data.getInt("slotid");
                    boolean occupied= true;
                    if (!json_data.getString("occupied").equals("true")) {
                        occupied = false;
                    }
                    String userid = json_data.getString("userid");
                    if(userid.equals(username)){
                        Intent intent =ConfirmActivity.newIntent(mContext,"Space" + slotid);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                    }
                    Slot slot = new Slot(slotid,occupied,userid);
                    slotList.add(slot);
                }
                Toast.makeText(mContext,amps,Toast.LENGTH_LONG).show();
                mSlotList = slotList;


            } catch (JSONException e) {

                    Toast.makeText(mContext,e.getMessage(),Toast.LENGTH_LONG).show();
            }

        }
    }

    private List <Slot> getDefaultSlotList(){

        List<Slot> defaultSlotList = new ArrayList<>();
        String[] names = {"alanturry", "empty", "alanturry", "jakebrown","empty","empty","statefarm", "dagwarm","empty","dagwarmm"};
        boolean [] occupied = {true,false,true,true,false,false,true,true,false,true};



        for(int i = 0 ; i<names.length;i++){
            Slot slot = new Slot(i+1,occupied[i],names[i]);


            defaultSlotList.add(slot);

        }

        return defaultSlotList;
    }

    }
