package com.stsoftwaresolution.shahajalal.bottomnavigationdrawer;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipeline;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapterForStudents extends RecyclerView.Adapter<RecyclerAdapterForStudents.myviewhoder> {
    private List<ContactForStudents> list;

    Context ctx;

    RecyclerAdapterForStudents(List<ContactForStudents> list, Context ctx) {
        this.ctx = ctx;
        this.list = list;

    }

    @NonNull
    @Override
    public myviewhoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemviewforstudents, parent, false);
        return new myviewhoder(view, ctx, list);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewhoder holder, int position) {

        try {
            ContactForStudents item =  list.get(position);
            holder.bind(item);

            /*ContactForStudents con=list.get(position);
            holder.username.setText(con.getName());
            holder.father.setText(con.getEmail());
            holder.village.setText(con.getInstution());
            Glide
                    .with(ctx) // replace with 'this' if it's in activity
                    .load("http://shahajalal.com/dev/EasyTuitionBD/photo/" + con.getEmail() + ".jpeg")
                    .into(holder.imageView);*/
        }catch (Exception e){

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class myviewhoder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView username, father, village;
        ImageView imageView;
        List<ContactForStudents> contacts;
        Context ctx;
        private ContactForStudents currentItem;
        SimpleDraweeView draweeView;
        //Main1Activity mainActivity=new Main1Activity();


        public myviewhoder(View itemView, Context ctx, List<ContactForStudents> contacts) {
            super(itemView);
            this.contacts = contacts;
            this.ctx = ctx;
            itemView.setOnClickListener(this);
            username = itemView.findViewById(R.id.usernametextview);
            father = itemView.findViewById(R.id.fathernametxtviewid);
            village = itemView.findViewById(R.id.villagetxtviewid);
            draweeView=  itemView.findViewById(R.id.imagesmallid);
            int color = itemView.getResources().getColor(R.color.colorAccent);
            RoundingParams roundingParams = RoundingParams.fromCornersRadius(5f);
            roundingParams.setBorder(color, 1.0f);
            roundingParams.setRoundAsCircle(true);
            draweeView.getHierarchy().setRoundingParams(roundingParams);
        }


        void bind (ContactForStudents item) {
            //<--bind method allows the ViewHolder to bind to the data it is displaying
            username.setText(item.getName());
            father.setText(item.getEmail());
            village.setText(item.getInstution());
            Uri imageUri = Uri.parse("http://shahajalal.com/dev/EasyTuitionBD/photo/" + item.getEmail() + ".jpeg");
            draweeView.setImageURI(imageUri);
            ImagePipeline imagePipeline = Fresco.getImagePipeline();
            imagePipeline.evictFromMemoryCache(imageUri);
            imagePipeline.evictFromDiskCache(imageUri);
            imagePipeline.evictFromCache(imageUri);
            currentItem = item; //<-- keep a reference to the current item
        }

        @Override
        public void onClick(View v) {
            ContactForStudents contact=currentItem;
            Log.d(String.valueOf(this), contact.getName()+" "+contact.getEmail());
            Intent intent=new Intent(this.ctx,ShowStudents.class);
            intent.putExtra("name",contact.getName());
            intent.putExtra("email",contact.getEmail());
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            this.ctx.startActivity(intent);
        }
    }

    public void setFilter(ArrayList<ContactForStudents> newList){
        list=new ArrayList<>();
        list.addAll(newList);
        notifyDataSetChanged();
    }

    }

