package com.stsoftwaresolution.shahajalal.bottomnavigationdrawer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.rey.material.widget.ProgressView;

import java.util.HashMap;
import java.util.Map;

public class EditProfilePage extends AppCompatActivity {

    private EditText name,email,mobile,password,university,details,fburl;
    private Button next;
    private String URL="http://shahajalal.com/dev/EasyTuitionBD/api.php";
    private ProgressView progressView;
    private Spinner spinner;
    private String spineer1;
    private static final String[] paths ={"Science","Arts","Commerce"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_edit_profile_page);
        progressView=findViewById(R.id.editprofileprogressbarid);
        spinner=findViewById(R.id.spinner1);
        name=findViewById(R.id.fullName1);
        fburl=findViewById(R.id.fburl1);
        name.setText(getIntent().getStringExtra("name"));
        email=findViewById(R.id.userEmailId1);
        email.setText(getIntent().getStringExtra("email"));
        mobile=findViewById(R.id.mobileNumber1);
        mobile.setText(getIntent().getStringExtra("mobile"));
        password=findViewById(R.id.password1);
        password.setText(getIntent().getStringExtra("pass"));
        next=findViewById(R.id.signUpBtn1);
        university=findViewById(R.id.universityid1);
        university.setText(getIntent().getStringExtra("university"));
        details=findViewById(R.id.details1);
        details.setText(getIntent().getStringExtra("details"));
        fburl.setText(getIntent().getStringExtra("fburl"));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditProfilePage.this,
                android.R.layout.simple_spinner_item, paths);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        spineer1=paths[0];
                        break;
                    case 1:
                        spineer1=paths[1];
                        break;
                    case 2:
                        spineer1=paths[2];
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressView.setVisibility(View.VISIBLE);
                updatefunction(name.getText().toString(),email.getText().toString(),password.getText().toString(),mobile.getText().toString(),university.getText().toString(),details.getText().toString(),spineer1,fburl.getText().toString());
            }
        });

    }

    private void updatefunction(final String name, final String email, final String pass, final String mobile, final String university, final String details,final String section,final String fburlstr){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("updated")) {
                    Toast.makeText(EditProfilePage.this, "Updated", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EditProfilePage.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    progressView.setVisibility(View.GONE);
                    Toast.makeText(EditProfilePage.this,"Not Updated",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<>();
                map.put("session","update");
                map.put("name",name);
                map.put("email",email);
                map.put("mobile",mobile);
                map.put("institue",university);
                map.put("password",pass);
                map.put("details",details);
                map.put("section",section);
                map.put("fburl",fburlstr);
                return map;
            }
        };

        MySingleTon.getInstance(EditProfilePage.this).addToRequestQue(stringRequest);
    }
    }

