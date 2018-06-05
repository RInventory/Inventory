package com.example.user.rinventory;

import android.content.Context;
import android.content.Intent;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;


public class loginActivity extends AppCompatActivity {
    Button LoginButton;
    EditText LoginEmail, LoginPass;
    private static String URL = "http://rinventory.online/Api/login";
    SharedPreferences sharedpref;
    SharedPreferences.Editor editor;
    private Snackbar snackbar;
    private ProgressDialog pd;
    private final static String TAG =  "coba";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        sharedpref=getSharedPreferences("shared", Context.MODE_PRIVATE);
        if (sharedpref.contains("email")){
            Intent iMenu = new Intent(getApplicationContext(),menuActivity.class);
            finish();
            startActivity(iMenu);
        }
        LoginEmail = (EditText) findViewById(R.id.editText);
        LoginPass = (EditText) findViewById(R.id.editText4);
        pd = new ProgressDialog(loginActivity.this);
        LoginButton = (Button) findViewById(R.id.btnLogin);

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginRequest();
            }
        });
    }

    private void loginRequest(){
        pd.setMessage("Signing In . . .");
        pd.show();
        RequestQueue queue = Volley.newRequestQueue(loginActivity.this);
        String response = null;
        final String finalResponse = response;
        editor =sharedpref.edit();


        StringRequest postRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        pd.hide();
                        showSnackbar(response);

                        if (response.equals("Login")) {
                            String email = LoginEmail.getText().toString();
                            editor.putString("email",email);
                            editor.commit();
                            Intent iMenu = new Intent(getApplicationContext(),menuActivity.class);
                            finish();
                            startActivity(iMenu);
                            Log.d("testing", "berhasil");
                        }


                    }

                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.hide();
                        Log.d("ErrorResponse", finalResponse);
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("email", LoginEmail.getText().toString());
                params.put("password", LoginPass.getText().toString());
                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);
    }
    public void showSnackbar(String stringSnackbar){
        snackbar.make(findViewById(android.R.id.content), stringSnackbar.toString(), Snackbar.LENGTH_SHORT)
                .setActionTextColor(getResources().getColor(R.color.colorPrimary))
                .show();

    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}