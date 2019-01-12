package com.example.q.cs496_3.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.example.q.cs496_3.R;
import com.example.q.cs496_3.adapters.PagerAdapter;
import com.example.q.cs496_3.fragments.InformationFragment;
import com.example.q.cs496_3.fragments.MatchFragment;
import com.example.q.cs496_3.fragments.OtherFragment;

public class FragmentActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;
    private TabLayout tablayout;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide();
        
        setContentView(R.layout.fragment_main);

        tablayout = (TabLayout) findViewById(R.id.tablayout);


        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mPagerAdapter = new PagerAdapter(getSupportFragmentManager(), 3);

        mPagerAdapter.AddFragment(new OtherFragment(), "Give");
        mPagerAdapter.AddFragment(new InformationFragment(),"Info");
        mPagerAdapter.AddFragment(new MatchFragment(), "Match");

        mViewPager.setAdapter(mPagerAdapter);
        tablayout.setupWithViewPager(mViewPager);
        mViewPager.setCurrentItem(1, true);
    }
}

