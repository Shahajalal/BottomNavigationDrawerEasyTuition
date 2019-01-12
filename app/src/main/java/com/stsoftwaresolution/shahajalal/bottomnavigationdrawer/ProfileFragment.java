package com.stsoftwaresolution.shahajalal.bottomnavigationdrawer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rey.material.widget.ProgressView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.support.v4.view.MenuItemCompat.getActionView;
import static maes.tech.intentanim.CustomIntent.customType;

/**
 * Created by Belal on 1/23/2018.
 */

public class ProfileFragment extends Fragment implements SearchView.OnQueryTextListener{
    List<ContactForStudents> list;
    private RecyclerView recyclerView;
    RecyclerAdapterForStudents adapter;
    RecyclerView.LayoutManager layoutManager;
    android.support.v7.widget.Toolbar toolbar;
    private ProgressView progressView;
    private String URL="http://shahajalal.com/dev/EasyTuitionBD/api.php";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //just change the fragment_dashboard
        //with the fragment you want to inflate
        //like if the class is HomeFragment it should have R.layout.home_fragment
        //if it is DashboardFragment it should have R.layout.fragment_dashboard
        View view= inflater.inflate(R.layout.fragment_profile, null);
        toolbar=view.findViewById(R.id.toolbarid);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        progressView=view.findViewById(R.id.mainactivityprogressid111);
        recyclerView=view.findViewById(R.id.recyclerviewid1111);
        layoutManager= new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        getIngormation();
        setHasOptionsMenu(true);
        return view;
    }


    private void getIngormation(){

        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressView.setVisibility(View.GONE);

                GsonBuilder builder=new GsonBuilder();
                Gson gson=builder.create();
                Log.d("Response", response);

                list= Arrays.asList(gson.fromJson(response,ContactForStudents[].class));

                adapter=new RecyclerAdapterForStudents(list,getContext());
                recyclerView.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"Error",Toast.LENGTH_LONG).show();
                Log.d("volley", error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String > map=new HashMap<>();
                map.put("session","seecommercestudents");
                return map;
            }
        };
        MySingleTon.getInstance(getContext()).addToRequestQue(stringRequest);
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }*/

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.login) {
            ///login button work
            Intent intent=new Intent(getContext(),LoginActivity.class);
            startActivity(intent);
            customType(getContext(),"fadein-to-fadeout");
            //finish();
            return true;
        }
        if(id==R.id.searchactionid){

            SearchView searchView=(SearchView) getActionView(item);
            searchView.setQueryHint("University Or College Name");
            item.expandActionView();
            MenuItemCompat.setOnActionExpandListener(item, new MenuItemCompat.OnActionExpandListener() {
                @Override
                public boolean onMenuItemActionExpand(MenuItem item) {
                    return true;
                }

                @Override
                public boolean onMenuItemActionCollapse(MenuItem item) {
                    //recreate();
                    getFragmentManager()
                            .beginTransaction()
                            .detach(ProfileFragment.this)
                            .attach(ProfileFragment.this)
                            .commit();

                    return true;
                }
            });

            searchView.setOnQueryTextListener(this);
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        try {
            s = s.toLowerCase();
            ArrayList<ContactForStudents> newList = new ArrayList<>();
            for (ContactForStudents contact : list) {
                String name = contact.getInstution().toLowerCase();
                if (name.contains(s)) {
                    newList.add(contact);
                }

            }
            adapter.setFilter(newList);
        }catch (Exception e){
            Toast.makeText(getContext(),"Loading",Toast.LENGTH_LONG).show();
        }
        return true;
    }
}
