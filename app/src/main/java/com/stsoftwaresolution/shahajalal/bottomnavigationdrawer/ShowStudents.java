package com.stsoftwaresolution.shahajalal.bottomnavigationdrawer;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
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
import com.android.volley.toolbox.Volley;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.rey.material.widget.ProgressView;

import java.util.HashMap;
import java.util.Map;

import static maes.tech.intentanim.CustomIntent.customType;

public class ShowStudents extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 0;
    String name, email, phone,fburl;
    SimpleDraweeView imageView;
    private TextView nametxt, emailtxt, phonetxt, alltext, university;
    private String URL = "http://shahajalal.com/dev/EasyTuitionBD/api.php";
    private ProgressView progressView;
    private Button facebookbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_show_students);
        if (ActivityCompat.checkSelfPermission(ShowStudents.this,Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(ShowStudents.this,
                    Manifest.permission.CALL_PHONE)) {
            } else {
                ActivityCompat.requestPermissions(ShowStudents.this,
                        new String[]{Manifest.permission.CALL_PHONE},
                        MY_PERMISSIONS_REQUEST_CALL_PHONE);
            }
        }
        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        imageView = findViewById(R.id.loginImageViewid1);
        nametxt = findViewById(R.id.nameTextViewId1);
        emailtxt = findViewById(R.id.emailtextviewid1);
        phonetxt = findViewById(R.id.mobiletextid1);
        alltext = findViewById(R.id.alltextid1);
        university = findViewById(R.id.universitytextid);
        progressView=findViewById(R.id.showstudentprogressbarid);
        facebookbtn=findViewById(R.id.facebookbtnid);

        Fresco.initialize(this);
        Uri imageUri = Uri.parse("http://shahajalal.com/dev/EasyTuitionBD/photo/"+email+".jpeg");
        imageView.setImageURI(imageUri);
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        imagePipeline.evictFromMemoryCache(imageUri);
        imagePipeline.evictFromDiskCache(imageUri);
        imagePipeline.evictFromCache(imageUri);



        /*
                .with(getApplicationContext()) // replace with 'this' if it's in activity
                .load("http://shahajalal.com/dev/EasyTuitionBD/photo/" + email + ".jpeg")
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(imageView);*/
        gotoAdminPage(name, email);
        facebookbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fburl.equals("no")){
                    Toast.makeText(ShowStudents.this,"No Facebook ID Assigned",Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent;
                    intent = getOpenFacebookIntent(getApplicationContext());
                    startActivity(intent);
                }
            }
        });

        phonetxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ShowStudents.this,"calling",Toast.LENGTH_SHORT).show();
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+phone));
                startActivity(callIntent);
            }
        });
    }

    public Intent getOpenFacebookIntent(Context context) {

        try {
            context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
            return new Intent(Intent.ACTION_VIEW, Uri.parse("fb://facewebmodal/f?href=" +fburl));
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW, Uri.parse(fburl));
        }
    }


    public void gotoAdminPage(final String user, final String email){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressView.setVisibility(View.GONE);
                        String s=response;
                        Log.d("gotoadmin", response);
                        String arr[];
                        arr=s.split("\n");
                        nametxt.setText(arr[1]);
                        emailtxt.setText(arr[0]);
                        phonetxt.setText(arr[3]);
                        phone=arr[3];
                        alltext.setText(arr[2]);
                        university.setText(arr[4]);
                        fburl=arr[5];
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ShowStudents.this,"Wrong",Toast.LENGTH_LONG).show();
                Log.d("Volley", "onErrorResponse: "+error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String > map=new HashMap<>();
                map.put("session","showstudents");
                map.put("name",user);
                map.put("email",email);
                return map;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ShowStudents.this,MainActivity.class));
        customType(this,"fadein-to-fadeout");
    }
}
