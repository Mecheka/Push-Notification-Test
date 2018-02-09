package com.example.suriya.firebasepushnotification.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.suriya.firebasepushnotification.R;

public class NotificationActivity extends AppCompatActivity {

    private TextView tvNotiData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        initInstance();

    }

    private void initInstance() {

        tvNotiData = (TextView) findViewById(R.id.tvNotiData);

    }
}
