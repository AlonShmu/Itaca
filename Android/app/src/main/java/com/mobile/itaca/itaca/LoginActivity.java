package com.mobile.itaca.itaca;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.util.Log;
import android.os.AsyncTask;
import android.content.SharedPreferences;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import android.widget.EditText;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.DataOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class LoginActivity extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ((EditText)findViewById(R.id.editText2)).setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(((EditText) findViewById(R.id.editText2)).getWindowToken(), 0);
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void loginPressed(View v) {
//        Log.i("LoginActivity", "loginPressed");
        String username = ((EditText)findViewById(R.id.editText)).getText().toString();
        String password = ((EditText)findViewById(R.id.editText2)).getText().toString();
        if ((username.length() > 0) && (password.length() > 0)) {
            new LoginUser().execute("http://52.1.104.153/returnssetionid.php", username, password);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Login Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.mobile.itaca.itaca/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Login Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.mobile.itaca.itaca/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    private class LoginUser extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            return postData(urls[0],urls[1],urls[2]);
        }

        protected void onPostExecute(String data) {
            Log.i("LoginActivity", "onPostExecute");
            LoginData loginData = new Gson().fromJson(data, LoginData.class);
            if (loginData.getSucess().equals("true")) {
                //--SAVE Data
                SharedPreferences preferences = getApplicationContext().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("username", loginData.getUsername());
                editor.putString("password", loginData.getPassword());
                editor.putString("companyname", loginData.getCompanName());
                editor.putString("id", loginData.getId());
                editor.commit();

                //--READ data
                //Log.i("loginData", preferences.getString("companyname", "") );
                if (!preferences.getString("username", "").equals("") && !preferences.getString("password", "").equals("")) {
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Unable to verify user", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(LoginActivity.this, loginData.getError(), Toast.LENGTH_LONG).show();
            }
        }

        public String postData(String urlStr, String username, String password) {
            try {
                URL url = new URL(urlStr);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                String urlParameters = "username=" + username + "&password=" + password;
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
                dStream.writeBytes(urlParameters);
                dStream.flush();
                dStream.close();
                int responseCode = connection.getResponseCode();
                Log.i("LoginActivity", "postData code" + responseCode);

                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = "";
                StringBuilder responseOutput = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    responseOutput.append(line);
                }
                br.close();

                Log.i("LoginActivity", "postData output" + responseOutput.toString());
                return responseOutput.toString();
            } catch (IOException e) {
                Toast.makeText(LoginActivity.this, "Communication Error ", Toast.LENGTH_LONG).show();
            }

            return null;
        }
    }
}