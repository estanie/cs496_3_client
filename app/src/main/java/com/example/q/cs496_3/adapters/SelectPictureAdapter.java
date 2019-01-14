package com.example.q.cs496_3.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.q.cs496_3.R;
import com.example.q.cs496_3.models.Image;

import java.util.ArrayList;

public class SelectPictureAdapter extends RecyclerView.Adapter<SelectPictureAdapter.ViewHolder> {
    private final String TAG = "SelectPictureAdapter";
    // TODO(estanie): Image 어떤식으로 받을지 고민...
    private ArrayList<Image> imageData;
    private Context mContext;

    public SelectPictureAdapter(Context context, ArrayList<Image> imageData) {
        this.imageData = imageData;
        this.mContext = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView selectImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            selectImage = itemView.findViewById(R.id.selectImage);
        }
    }

    @NonNull @Override
    public SelectPictureAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.entry_image, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectPictureAdapter.ViewHolder holder, final int i) {
        changeHeight(holder);
        Glide.with(mContext)
                .load(imageData.get(i).getPath())
                .into(holder.selectImage);
        holder.selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageData.get(i).toggle();
                Log.e(TAG, "Clicked " + i);
            }
        });
        // TODO(estanie): 사진 다중 선택기능 추가.
    }

    @Override
    public int getItemCount() {
        return imageData.size();
    }

    private void changeHeight(ViewHolder holder) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        ImageView image = holder.itemView.findViewById(R.id.selectImage);
        image.setLayoutParams(new ConstraintLayout.LayoutParams(
                displayMetrics.widthPixels/2, displayMetrics.widthPixels/2));
    }
}
