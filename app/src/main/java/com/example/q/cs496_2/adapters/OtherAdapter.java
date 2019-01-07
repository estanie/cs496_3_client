package com.example.q.cs496_2.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.q.cs496_2.R;
import com.example.q.cs496_2.models.User;

import java.util.ArrayList;

public class OtherAdapter extends RecyclerView.Adapter<OtherAdapter.viewHolder> {
    private ArrayList<User> userData;

    public OtherAdapter(ArrayList<User> data){
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

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            viewPhoto = itemView.findViewById(R.id.oEntryPhoto);
            viewName = itemView.findViewById(R.id.oEntryName);
            viewAge = itemView.findViewById(R.id.oEntryAge);
            viewResidence = itemView.findViewById(R.id.oEntryResidence);
            viewJob = itemView.findViewById(R.id.oEntryJob);
            viewHobby =itemView.findViewById(R.id.oEntryHobby);
        }
    }
    
    @NonNull
    @Override
    public OtherAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.entry_others, viewGroup, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OtherAdapter.viewHolder holder, int i) {
        //holder.viewPhoto;
        holder.viewName.setText(userData.get(i).getName());
        holder.viewAge.setText(userData.get(i).getAge());
        holder.viewResidence.setText(userData.get(i).getResidence());
        holder.viewJob.setText(userData.get(i).getJob());
        holder.viewHobby.setText(userData.get(i).getHobby());
        /*holder.viewEntry.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //TODO heart +하는 방법 생각
            }
        });
        */
    }

    @Override
    public int getItemCount() {
        return userData.size();
    }
}