package com.google.sample.cloudvision;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    String username;
    String salt;
    String inputPw;
    String calculatedHash;
    String getHash;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void login(View view) throws JSONException {
        Button loginBtn = findViewById(R.id.loginBtn);
        loginBtn.setEnabled(false);
        loginBtn.setBackgroundColor(Color.BLACK);
        EditText usernameInput = findViewById(R.id.input_id);
        username = usernameInput.getEditableText().toString().toUpperCase();
        EditText passwordInput = findViewById(R.id.input_pw);
        inputPw = passwordInput.getEditableText().toString();
        if(!username.isEmpty() && !inputPw.isEmpty())
            checkLogin();
        else
        {
            loginBtn.setEnabled(true);
            loginBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
            Toast.makeText(getBaseContext(), "username or password is empty!", Toast.LENGTH_LONG).show();
        }
    }

    public void checkLogin() throws JSONException {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://identiphoto.azurewebsites.net/api/AuthenticatePhotographer";

        String Data = preProcessData().toString();
        Button loginBtn = findViewById(R.id.loginBtn);

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("STATUS:GRANTED")) {
                    String[] split = response.split(":");
                    Toast toast = Toast.makeText(getApplicationContext(), "Login Successful!", Toast.LENGTH_SHORT);
                    toast.show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    split[2] = split[2].substring(0, split[2].length() - 1);
                    intent.putExtra("photographer_ID", split[2]);
                    startActivity(intent);
                    finish();
                } else {
                    Log.e("Something has gone wrong!", "could not be saved... " + response);
                    Toast toast = Toast.makeText(getApplicationContext(), "Login Failed! Please try again.", Toast.LENGTH_SHORT);
                    toast.show();
                    loginBtn.setEnabled(true);
                    loginBtn.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.colorPrimary));
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error is ", "" + error);
            }
        }) {

            //This is for Headers
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json; charset=UTF-8");
                params.put("API_KEY", getResources().getString(R.string.API_KEY));
                return params;
            }

            //Pass Params
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {

                byte[] body = new byte[0];
                try {
                    body = Data.getBytes("UTF-8");
                } catch (UnsupportedEncodingException e) {
                }
                return body;
            }
        };
        queue.add(request);
    }

    private JSONObject preProcessData() throws JSONException {
        JSONObject JSONData = new JSONObject();
        JSONData.put("user_ID", username);
        JSONData.put("pwd", inputPw);
        return JSONData;
    }
}
