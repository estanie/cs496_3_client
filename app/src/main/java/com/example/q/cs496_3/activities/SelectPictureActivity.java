package com.example.q.cs496_3.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.example.q.cs496_3.R;
import com.example.q.cs496_3.adapters.SelectPictureAdapter;
import com.example.q.cs496_3.models.Image;

import java.util.ArrayList;

public class SelectPictureActivity extends AppCompatActivity {
    private final String TAG = "SELECT_PICTURE_ACTIVITY";
    private RecyclerView mRecyclerView;
    private FloatingActionButton mFab;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Image> mImageData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //맨위 TITEL_BAR 제거
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide();
        setContentView(R.layout.activity_select_picture);
        mImageData = new ArrayList<>();
        mRecyclerView = (RecyclerView) findViewById(R.id.imageGrid);
        mFab = (FloatingActionButton) findViewById(R.id.selectConfirm);
        mLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        // TODO(estanie): mImageData 테스트용임. Random 한 이미지 서버로부터 받기..
        mImageData.add(new Image("https://i.pinimg.com/originals/5d/72/6d/5d726db2ead085c1ab35e41e8390df2b.jpg"));
        mImageData.add(new Image("http://www.topstarnews.net/news/photo/201811/530683_197566_198_org.jpg"));
        mImageData.add(new Image("http://image.chosun.com/sitedata/image/201605/16/2016051602048_0.jpg"));
        mImageData.add(new Image("http://image.kmib.co.kr/online_image/2017/1018/201710180000_13200923832185_1.jpg"));
        mAdapter = new SelectPictureAdapter(getApplicationContext(), mImageData);
        mRecyclerView.setAdapter(mAdapter);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO(estanie): 10장 이상 골랐는지 확인하고 넘어가고, 데이터 서버에 보내서 계산하게 시키기.
                Intent intent = new Intent(SelectPictureActivity.this, FragmentActivity.class);
                startActivity(intent);
                setResult(RESULT_OK);
                finish();
            }
        });
    }
}
