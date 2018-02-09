package com.example.suriya.firebasepushnotification.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.suriya.firebasepushnotification.fragment.NotificationFragment;
import com.example.suriya.firebasepushnotification.fragment.ProfileFragment;
import com.example.suriya.firebasepushnotification.fragment.UserFragment;

/**
 * Created by Suriya on 4/1/2561.
 */

public class PagerViewAdapter extends FragmentPagerAdapter {
    public PagerViewAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                ProfileFragment profileFragment = new ProfileFragment();
                return profileFragment;

            case 1:
                UserFragment userFragment = new UserFragment();
                return userFragment;

            case 2:
                NotificationFragment notificationFragment = new NotificationFragment();
                return notificationFragment;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
