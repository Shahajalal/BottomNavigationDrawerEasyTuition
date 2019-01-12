package com.stsoftwaresolution.shahajalal.bottomnavigationdrawer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.rey.material.widget.ProgressView;

import java.util.HashMap;
import java.util.Map;

public class RecoverPasswordActivity extends AppCompatActivity {

    EditText email;
    Button send;
    private String URL="http://shahajalal.com/dev/EasyTuitionBD/api.php";
    private ProgressView progressView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_recover_password);
        email=findViewById(R.id.recover_emailid);
        send=findViewById(R.id.sendbtn);
        progressView=findViewById(R.id.recoverprogressbarid);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email.getText().toString().equals("")){
                    Toast.makeText(RecoverPasswordActivity.this,"Please Give Your Email Address",Toast.LENGTH_SHORT).show();
                }else {
                    progressView.setVisibility(View.VISIBLE);
                    sendfunction(email.getText().toString());
                }
            }
        });

    }


    public void  sendfunction(final String email){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("sent")) {
                    Toast.makeText(RecoverPasswordActivity.this, "Please Check Email To Your Spam", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(RecoverPasswordActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    progressView.setVisibility(View.GONE);
                    Toast.makeText(RecoverPasswordActivity.this,"Don't Sent",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(RecoverPasswordActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                Log.d("volley", error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String > map=new HashMap<>();
                map.put("session","sendemail");
                map.put("email",email);
                return map;
            }
        };
        MySingleTon.getInstance(this).addToRequestQue(stringRequest);
    }


}
