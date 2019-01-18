package com.soft.android.ozonepark;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.soft.android.ozonepark.util.User;

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

import static android.R.attr.data;

/**
 * Created by chukwudi on 4/23/2018.
 */

public class LoginTab extends Fragment {
    private static final String SAVED_USERNAME = "username";//the layout for the login body

    private EditText mUsername;
    private EditText mPassword;
    ProgressDialog pdLoading ;
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    private int xcode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.login_page, container, false);
        Button loginButton = view.findViewById(R.id.login_button);
        mUsername =  view.findViewById(R.id.username);
        mPassword =  view.findViewById(R.id.password);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username=mUsername.getText().toString();
                String password= mPassword.getText().toString();
                if(username.length()==0 && password.length()==0){
                    mUsername.setError("Empty");
                    mPassword.setError("Empty");
                }else{

                    new AsyncLogin(getActivity()).execute(username,password);

                }


            }
        });

        return view;
    }


    class AsyncLogin extends AsyncTask<String, String, String>
    {

        HttpURLConnection conn;
        URL url = null;
        String response;
        private String username;
        private String password;
        private String resp;


        public AsyncLogin(Context context) {
            pdLoading = new ProgressDialog(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLogging in...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }
        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides
                url = new URL("http://"+ getString(R.string.ip_address)+ "//carparkingsystem/login.php");

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


                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("username", params[0])
                        .appendQueryParameter("password", params[1]);

                username = params[0];
                password = params[1];
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
                xcode = response_code;
                resp =conn.getResponseMessage();
                // Check if successful connection made
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



            if(result.equalsIgnoreCase("true"))
            {

                new AsyncSave(getActivity()).execute(username);
                Toast.makeText(getActivity(), "Successful", Toast.LENGTH_LONG).show();
                /* Here launching another activity when login successful. If you persist login state
                Intent intent;
                    intent = new Intent(getActivity(),SlotListActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                use sharedPreferences of Android. and logout button to clear sharedPreferences.
                 */
            }else if (result.equalsIgnoreCase("false")){

                // If username and password does not match display a error message
                Toast.makeText(getActivity(), "Invalid email or password", Toast.LENGTH_LONG).show();

            } else if (result.equalsIgnoreCase("exception")||result.equalsIgnoreCase("unsuccessful")) {

                Toast.makeText(getActivity(),  resp, Toast.LENGTH_LONG).show();

            }
        }

    }


    //ASYNCSAVE CLASS

    class AsyncSave extends AsyncTask<String, String, String>
    {

        static final String SAVED_NAME = "savedname";
        HttpURLConnection conn;
        URL url = null;
        String response;
        private String username;



        public AsyncSave(Context context) {
            pdLoading = new ProgressDialog(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLogging in...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }
        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides
                url = new URL("http://"+ getString(R.string.ip_address)+ "//carparkingsystem/getuername.php");

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

            mUsername.setText(result);

            try {

                String amps = "[" + result + "]";
                // result is a variable which holds fetched json data.
                JSONArray jArray = new JSONArray(amps);

                    JSONObject json_data = jArray.getJSONObject(0);

                    String fullname = json_data.getString("firstname") + " "+ json_data.getString("lastname");
                Toast.makeText(getActivity(),fullname,Toast.LENGTH_LONG);
                    User user = new User();
                    user.setName(fullname);

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(SAVED_USERNAME,username);
                editor.putString(SAVED_NAME, fullname);
                editor.commit();

              Intent intent = SlotListActivity.newIntent(getActivity(),username,fullname);
                startActivity(intent);
                getActivity().finish();



            } catch (JSONException e) {
                mUsername.setText(e.getMessage());
            }

        }
    }}
