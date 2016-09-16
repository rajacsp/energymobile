package org.qwan.energy.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.qwan.energy.R;
import org.qwan.energy.utils.AppGlobals;
import org.qwan.energy.utils.Helpers;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class LoginActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "LoginActivity";

    private Button mLogin;
    private EditText mEmail;
    private EditText mPassword;
    private String getEmail;
    private String getPassword;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        mLogin = (Button) findViewById(R.id.login_button);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mLogin.setOnClickListener(this);
        mEmail = (EditText) findViewById(R.id.email_login);
        mPassword = (EditText) findViewById(R.id.password_login);
        getEmail = mEmail.getText().toString();
        getPassword = mPassword.getText().toString();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_button:
                if (mEmail.getText().toString().trim().isEmpty() &&
                        mPassword.getText().toString().trim().isEmpty()) {
                    Toast.makeText(LoginActivity.this, "All fields must be filled",
                            Toast.LENGTH_SHORT).show();

                }
                if (!mEmail.getText().toString().trim().isEmpty() &&
                        !mPassword.getText().toString().trim().isEmpty()) {
                    String[] data = {mEmail.getText().toString(), mPassword.getText().toString()};
                    new LoginTask().execute(data);
                }
                break;
        }
    }

    class LoginTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
            mLogin.setClickable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            String data = "";
            if (Helpers.isNetworkAvailable() && Helpers.isInternetWorking()) {

                try {
                  data =   Helpers.connectionRequest
                          (String.format(
                                  AppGlobals.LOGIN_URL +"userid="+"%s"+"&password="+"%s",
                                  params[0], params[1]), "POST");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    JSONObject jsonObject = new JSONObject(data);
                    Log.i(TAG, jsonObject + " : "+jsonObject);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                Log.i(TAG, "{onPostExecute} jsonObject : "+jsonObject);

                if (jsonObject.getInt("apiresult") == 0) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    mProgressBar.setVisibility(View.GONE);


                    JSONObject apiValueJsonObject = new JSONObject(jsonObject.getString("apivalue"));
                    String sessionid = apiValueJsonObject.getString("sessionid");

                    Helpers.saveDataToSharedPreferences(AppGlobals.KEY_ER_SESSIONID, sessionid);
                    Helpers.saveDataToSharedPreferences(AppGlobals.KEY_ER_MEMBERID, apiValueJsonObject.getString("MEMBERID"));
                    Helpers.saveDataToSharedPreferences(AppGlobals.KEY_ER_SESSIONID, apiValueJsonObject.getString("USERID"));
                    Helpers.userLogin(sessionid);

                    return;
                }

                Toast.makeText(getApplicationContext(), "invalid credentials", Toast.LENGTH_SHORT).show();

                mEmail.setText("");
                mPassword.setText("");
                mProgressBar.setVisibility(View.GONE);
                mLogin.setClickable(true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (AppGlobals.sRegisterStatus) {
        MainActivity.getInstance().closeApplication();
        }
    }
}
