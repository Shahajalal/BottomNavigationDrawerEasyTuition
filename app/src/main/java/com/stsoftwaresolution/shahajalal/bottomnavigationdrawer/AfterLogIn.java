package com.stsoftwaresolution.shahajalal.bottomnavigationdrawer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.rey.material.widget.ProgressView;

import java.util.HashMap;
import java.util.Map;

import static maes.tech.intentanim.CustomIntent.customType;

public class AfterLogIn extends AppCompatActivity {
    private String email,pass,name,details,mobile,uni,fburl;
    private TextView nametxt,emailtxt,phonetxt,alltext,university;
    private SimpleDraweeView image;
    private Button edit,delete;
    private String URL="http://shahajalal.com/dev/EasyTuitionBD/api.php";
    private ProgressView progressView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_after_log_in);
        Bundle bundle = getIntent().getExtras();
        edit=findViewById(R.id.editprofileid);
        delete=findViewById(R.id.deleteprofile);
        progressView=findViewById(R.id.afterloginprogressbarid);
        email=bundle.getString("email");
        pass=bundle.getString("pass");
        name=bundle.getString("name");
        details=bundle.getString("details");
        mobile=bundle.getString("mobile");
        uni=bundle.getString("university");
        fburl=bundle.getString("fburl");
        nametxt=findViewById(R.id.nameTextViewId);
        nametxt.setText(name);
        emailtxt=findViewById(R.id.emailtextviewid);
        emailtxt.setText(email);
        phonetxt=findViewById(R.id.mobiletextid);
        phonetxt.setText(mobile);
        alltext=findViewById(R.id.alltextid);
        alltext.setText(details);
        university=findViewById(R.id.universitytextid1);
        university.setText(uni);
        image=findViewById(R.id.loginImageViewid);
        Fresco.initialize(this);
        Uri imageUri = Uri.parse("http://shahajalal.com/dev/EasyTuitionBD/photo/"+email+".jpeg");
        image.setImageURI(imageUri);
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        imagePipeline.evictFromMemoryCache(imageUri);
        imagePipeline.evictFromDiskCache(imageUri);
        imagePipeline.evictFromCache(imageUri);
       /* Glide
                .with(getApplicationContext()) // replace with 'this' if it's in activity
                .load("http://shahajalal.com/dev/EasyTuitionBD/photo/"+email+".jpeg")
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(image);*/


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AfterLogIn.this,EditProfilePage.class);
                Bundle bundle=new Bundle();
                bundle.putString("name",name);
                bundle.putString("email",email);
                bundle.putString("mobile",mobile);
                bundle.putString("university",uni);
                bundle.putString("details",details);
                bundle.putString("pass",pass);
                bundle.putString("fburl",fburl);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressView.setVisibility(View.VISIBLE);
                deletefunction();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(AfterLogIn.this,MainActivity.class);
        startActivity(intent);
        customType(this,"fadein-to-fadeout");
        finish();
    }

    public void  deletefunction(){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("deleted")) {
                    Toast.makeText(AfterLogIn.this, "Deleted", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AfterLogIn.this, LoginActivity.class);
                    startActivity(intent);
                }else{
                    progressView.setVisibility(View.GONE);
                    Toast.makeText(AfterLogIn.this,"Not Deleted",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(AfterLogIn.this,error.toString(),Toast.LENGTH_LONG).show();
                Log.d("volley", error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String > map=new HashMap<>();
                map.put("session","delete");
                map.put("name",name);
                map.put("email",email);
                return map;
            }
        };
        MySingleTon.getInstance(this).addToRequestQue(stringRequest);
    }
}
