package com.stsoftwaresolution.shahajalal.bottomnavigationdrawer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.rey.material.widget.ProgressView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SignUpUpload extends AppCompatActivity implements View.OnClickListener {

    private  static  final int IMG_REQUEST=1;
    private Button uploadbtn,choosebtn,download;
    public ImageView imageView,imageView1;
    private Bitmap bitmap;
    EditText details,fburl;
    String name,email,mobile,instute,pass,section;
    private ProgressView progressBar;
    private String URL="http://shahajalal.com/dev/EasyTuitionBD/api.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up_upload);
        uploadbtn=findViewById(R.id.uploadbtn);
        choosebtn=findViewById(R.id.choosebtn);
        imageView=findViewById(R.id.igageviewid);
        details=findViewById(R.id.details);
        fburl=findViewById(R.id.fburl);
        progressBar=findViewById(R.id.signupprogressbarid);
        Bundle bundle = getIntent().getExtras();
        name=bundle.getString("name");
        email=bundle.getString("email");
        mobile=bundle.getString("mobile");
        instute=bundle.getString("instute");
        pass=bundle.getString("pass");
        section=bundle.getString("section");
        choosebtn.setOnClickListener(this);
        uploadbtn.setOnClickListener(this);

    }

    public void selectimg(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMG_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==IMG_REQUEST && resultCode==RESULT_OK && data!=null){
            Uri path=data.getData();
            try {
                bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(),path);
                imageView.setImageBitmap(bitmap);
                imageView.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void uploadimg(){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String Response=jsonObject.getString("response");
                    Toast.makeText(SignUpUpload.this,Response,Toast.LENGTH_SHORT).show();
                    imageView.setImageResource(0);
                    imageView.setVisibility(View.GONE);
                    Intent intent=new Intent(SignUpUpload.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SignUpUpload.this,"Problem With Uploading file",Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> map=new HashMap<>();
                map.put("session","signup");
                map.put("name",name);
                map.put("image",imagetostring(bitmap));
                map.put("email",email);
                map.put("mobile",mobile);
                map.put("institue",instute);
                map.put("password",pass);
                map.put("details",details.getText().toString());
                map.put("section",section);
                map.put("fburl",fburl.getText().toString());
                return map;
            }
        };

        MySingleTon.getInstance(SignUpUpload.this).addToRequestQue(stringRequest);
    }

    private String imagetostring(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,40,byteArrayOutputStream);
        byte[] imgbyte=byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgbyte,Base64.DEFAULT);
    }

    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.choosebtn){
            selectimg();
        }else{
            if(imageView.getDrawable()!=null) {
                progressBar.setVisibility(View.VISIBLE);
                uploadimg();
            }else{
                Toast.makeText(SignUpUpload.this,"Please Select An Image",Toast.LENGTH_SHORT).show();
            }
        }
    }

}
