package com.chochoChat.Adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.chochoChat.Fragment.YourChat;
import com.chochoChat.Fragment.YourMatches;

public class ViewPagerAdapter  extends FragmentPagerAdapter {

    private Context myContext;
    int totalTabs;

    public ViewPagerAdapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }

    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                YourMatches matches = new YourMatches();
                return matches;
            case 1:
                YourChat chats = new YourChat();
                return chats;
            default:
                return null;
        }
    }
    // this counts total number of tabs
    @Override
    public int getCount() {
        return totalTabs;
    }
}