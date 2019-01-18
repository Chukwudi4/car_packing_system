package com.soft.android.ozonepark;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
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

import static com.soft.android.ozonepark.LoginTab.CONNECTION_TIMEOUT;
import static com.soft.android.ozonepark.LoginTab.READ_TIMEOUT;

/**
 * Created by chukwudi on 4/24/2018.
 */

public class RegisterTab extends Fragment{//the layout for the register body

    private EditText mLastName,mMiddleName, mPassword, mConfirmPassword, mUsername;
    private EditText mFirstName;
    private Button mRegBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.register_page, container, false);

        mLastName = (EditText) view.findViewById(R.id.lastname);
        mFirstName = (EditText) view.findViewById(R.id.firstname);
        mPassword =(EditText) view.findViewById(R.id.password);
        mUsername = (EditText) view.findViewById(R.id.username);
        mConfirmPassword = (EditText) view.findViewById(R.id.confirmpassword);
        mMiddleName = (EditText) view.findViewById(R.id.middlename);

        mRegBtn = (Button) view.findViewById(R.id.reg_btn);

        mRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isEmpty(mConfirmPassword)||isEmpty(mPassword)||isEmpty(mUsername)||isEmpty(mLastName)||isEmpty(mFirstName)) {

                }else if(!mPassword.getText().toString().equals(mConfirmPassword.getText().toString())){
                    mConfirmPassword.setError("This should be the same with password");
                }else{

                    String username = mUsername.getText().toString();
                    String password= mPassword.getText().toString();
                    String lastname= mLastName.getText().toString();
                    String firstname= mFirstName.getText().toString();
                    String middlename = mMiddleName.getText().toString();

                    new Register().execute(username,password,lastname,firstname,middlename);
                }



            }
        });
        return view;
    }


    private class Register extends AsyncTask<String, String, String> {

        ProgressDialog pdLoading = new ProgressDialog(getActivity());
        HttpURLConnection conn;
        URL url = null;
        String response ="null";
        private String username;
        private String password;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tRegistering you...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        @Override
        protected String doInBackground(String... params) {

            try {

                // Enter URL address where your php file resides
                url = new URL("http://"+ getString(R.string.ip_address)+ "//carparkingsystem/register.php");

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
                        .appendQueryParameter("password", params[1])
                        .appendQueryParameter("surname", params[2])
                        .appendQueryParameter("firstname", params[3])
                        .appendQueryParameter("middlename", params[4]);


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
            mMiddleName.setText(result);
            pdLoading.dismiss();



            if(result.equalsIgnoreCase("true"))
            {



                /* Here launching another activity when login successful. If you persist login state
                use sharedPreferences of Android. and logout button to clear sharedPreferences.
                 */
                clear(mUsername);
                clear(mPassword);
                clear(mConfirmPassword);
                clear(mLastName);
                clear(mFirstName);
                clear(mMiddleName);
                Toast.makeText(getActivity(), "You've successfully registered", Toast.LENGTH_LONG).show();

                //getActivity().finish();

            }else if (result.equalsIgnoreCase("false")){

                // If username and password does not match display a error message
                Toast.makeText(getActivity(), "Username already exits", Toast.LENGTH_LONG).show();

            } else if (result.equalsIgnoreCase("exception")||result.equalsIgnoreCase("unsuccessful")) {

                Toast.makeText(getActivity(), "Connection Not Successful", Toast.LENGTH_LONG).show();

            }
        }




    }

    private boolean isEmpty(EditText et){

        if(et.getText().toString().equals("")){
            et.setError("Empty");
            return true;
        }
        return false;
    }

    private void clear(EditText et){
        et.setText("");
    }

}
