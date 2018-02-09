package com.example.suriya.firebasepushnotification.activity;

import android.content.Intent;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.suriya.firebasepushnotification.R;
import com.example.suriya.firebasepushnotification.adapter.PagerViewAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private TextView tvProfile;
    private TextView tvUser;
    private TextView tvNoti;

    private ViewPager mMainPager;
    private PagerViewAdapter mPagerViewAdapter;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        initinstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void initinstance() {
        tvProfile = (TextView) findViewById(R.id.tvProfile);
        tvUser = (TextView) findViewById(R.id.tvUser);
        tvNoti = (TextView) findViewById(R.id.tvNoti);

        mMainPager = (ViewPager) findViewById(R.id.mainPage);
        mMainPager.setOffscreenPageLimit(2);
        mPagerViewAdapter = new PagerViewAdapter(getSupportFragmentManager());
        mMainPager.setAdapter(mPagerViewAdapter);
        tvProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMainPager.setCurrentItem(0);
            }
        });
        tvUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMainPager.setCurrentItem(1);
            }
        });
        tvNoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMainPager.setCurrentItem(2);
            }
        });
        mMainPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changeTabs(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void changeTabs(int position) {
        if (position == 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                tvProfile.setTextColor(getColor(R.color.mainTextFC));
                tvProfile.setTextSize(22);

                tvUser.setTextColor(getColor(R.color.mainTextNoFC));
                tvUser.setTextSize(16);

                tvNoti.setTextColor(getColor(R.color.mainTextNoFC));
                tvNoti.setTextSize(16);
            }
        }
        if (position == 1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                tvProfile.setTextColor(getColor(R.color.mainTextNoFC));
                tvProfile.setTextSize(16);

                tvUser.setTextColor(getColor(R.color.mainTextFC));
                tvUser.setTextSize(22);

                tvNoti.setTextColor(getColor(R.color.mainTextNoFC));
                tvNoti.setTextSize(16);
            }
        }
        if (position == 2) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                tvProfile.setTextColor(getColor(R.color.mainTextNoFC));
                tvProfile.setTextSize(16);

                tvUser.setTextColor(getColor(R.color.mainTextNoFC));
                tvUser.setTextSize(16);

                tvNoti.setTextColor(getColor(R.color.mainTextFC));
                tvNoti.setTextSize(22);
            }
        }
    }
}
