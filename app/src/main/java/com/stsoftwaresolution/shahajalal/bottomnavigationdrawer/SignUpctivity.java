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
import android.widget.TextView;
import android.widget.Toast;

import static maes.tech.intentanim.CustomIntent.customType;

public class SignUpctivity extends AppCompatActivity {
    private EditText name,email,mobile,password,university;
    private Button next;
    private TextView alreadyuser;
    private Spinner spinner;
    private static final String[] paths ={"Science","Arts","Commerce"};
    private String spineer1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.signup_layout);
        spinner=findViewById(R.id.spinner);
        name=findViewById(R.id.fullName);
        email=findViewById(R.id.userEmailId);
        mobile=findViewById(R.id.mobileNumber);
        password=findViewById(R.id.password);
        next=findViewById(R.id.signUpBtn);
        university=findViewById(R.id.universityid);
        alreadyuser=findViewById(R.id.already_user);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(SignUpctivity.this,
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
                spineer1=paths[0];
            }
        });



        alreadyuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignUpctivity.this,LoginActivity.class);
                startActivity(intent);
                //customType(getApplicationContext(),"fadein-to-fadeout");
                finish();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.getText().toString().equals("") && email.getText().toString().equals("")&&university.getText().toString().equals("")&&password.getText().toString().equals("")){
                    Toast.makeText(SignUpctivity.this,"Please Fill the All Text Area",Toast.LENGTH_SHORT).show();
                }else {
                Intent intent=new Intent(SignUpctivity.this,SignUpUpload.class);
                Bundle bundle=new Bundle();
                bundle.putString("name",name.getText().toString());
                bundle.putString("email",email.getText().toString());
                bundle.putString("mobile",mobile.getText().toString());
                bundle.putString("instute",university.getText().toString());
                bundle.putString("pass",password.getText().toString());
                bundle.putString("section",spineer1);
                intent.putExtras(bundle);
                startActivity(intent);
                    //customType(getApplicationContext(),"fadein-to-fadeout");
                }

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(SignUpctivity.this,MainActivity.class);
        startActivity(intent);
        customType(this,"fadein-to-fadeout");
        finish();
    }
}
