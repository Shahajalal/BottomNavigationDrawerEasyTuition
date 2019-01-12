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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rey.material.widget.ProgressView;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private TextView singnuptxt;
    private Button loginbtn,forgotbtn;
    private EditText email,pass;
    private ProgressView progressBar;
    private String URL="http://shahajalal.com/dev/EasyTuitionBD/api.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.login_layout);
        singnuptxt=findViewById(R.id.createAccount);
        loginbtn=findViewById(R.id.loginBtn);
        forgotbtn=findViewById(R.id.forgotpassbtn);
        email=findViewById(R.id.login_emailid);
        pass=findViewById(R.id.login_password);
        progressBar=findViewById(R.id.loginprogressbarid);
        singnuptxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,SignUpctivity.class);
                startActivity(intent);
                //customType(getApplication().getApplicationContext(),"fadein-to-fadeout");
                finish();
            }
        });
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email.getText().toString().equals("") && pass.getText().toString().equals("")){
                    Toast.makeText(LoginActivity.this,"Please Give Email And Password",Toast.LENGTH_SHORT).show();
                }else{
                    progressBar.setVisibility(View.VISIBLE);
                    gotoAdminPage(email.getText().toString(),pass.getText().toString());
                }

            }
        });

        forgotbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,RecoverPasswordActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void gotoAdminPage(final String user, final String pass){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String s=response;
                        Log.d("gotoadmin", response);
                        String arr[];
                        arr=s.split("\n");
                        if(user.equals(arr[0])&& pass.equals(arr[1])){
                            Intent intent=new Intent(LoginActivity.this,AfterLogIn.class);
                            Bundle bundle=new Bundle();
                            bundle.putString("email",user);
                            bundle.putString("pass",pass);
                            bundle.putString("name",arr[2]);
                            bundle.putString("details",arr[3]);
                            bundle.putString("mobile",arr[4]);
                            bundle.putString("university",arr[5]);
                            bundle.putString("fburl",arr[6]);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            finish();
                        }else{
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(LoginActivity.this,"Incorrect Email Or Password",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this,"Wrong",Toast.LENGTH_LONG).show();
                Log.d("Volley", "onErrorResponse: "+error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String > map=new HashMap<>();
                map.put("session","login");
                map.put("email",user);
                map.put("pass",pass);
                return map;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
