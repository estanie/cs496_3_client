package com.example.q.cs496_3.adapters;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.example.q.cs496_3.R;
import com.example.q.cs496_3.models.User;

import java.util.ArrayList;

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.viewHolder> {
    private ArrayList<User> userData;

    public MatchAdapter(ArrayList<User> data){
        userData = data;
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout viewEntry;
        private ImageView viewPhoto;
        private TextView viewName;
        private TextView viewAge;
        private TextView viewResidence;
        private TextView viewJob;
        private TextView viewHobby;
        private TextView viewContact;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            viewPhoto = itemView.findViewById(R.id.mEntryPhoto);
            viewName = itemView.findViewById(R.id.mEntryName);
            viewAge = itemView.findViewById(R.id.mEntryAge);
            viewResidence = itemView.findViewById(R.id.mEntryResidence);
            viewJob = itemView.findViewById(R.id.mEntryJob);
            viewHobby =itemView.findViewById(R.id.mEntryHobby);
            viewContact =itemView.findViewById(R.id.mEntryContacts);
        }
    }

    @NonNull
    @Override
    public MatchAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.entry_matches, viewGroup, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchAdapter.viewHolder holder, int i) {
        //View holder에 적절한 값을 bind하는 함수
        Uri uri = null;
        ImageAdapter imageAdapter = new ImageAdapter(holder.viewPhoto.getContext(), uri);
        //ImageView imageView = new ImageView(getContext());
        RequestManager requestManager = Glide.with(imageAdapter.getContext());
        // Create request builder and load image.
        RequestBuilder requestBuilder = requestManager.load("http://143.248.140.106:2580/uploads/"+userData.get(i).getPhoto());
        //requestBuilder = requestBuilder.apply(new RequestOptions().override(250, 250));
        // Show image into target imageview.
        requestBuilder.into(holder.viewPhoto);
        holder.viewName.setText(userData.get(i).getName());
        holder.viewAge.setText(userData.get(i).getAge());
        holder.viewResidence.setText(userData.get(i).getResidence());
        holder.viewJob.setText(userData.get(i).getJob());
        holder.viewHobby.setText(userData.get(i).getHobby());
        holder.viewContact.setText(userData.get(i).getContact());
    }

    @Override
    public int getItemCount() {
        return userData.size();
    }
}