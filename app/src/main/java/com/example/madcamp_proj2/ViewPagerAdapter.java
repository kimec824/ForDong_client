package com.example.madcamp_proj2;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private int pageCount;
    public int fragnum;

    public ViewPagerAdapter(FragmentManager mgr, int pageCount) {
        super(mgr, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.pageCount = pageCount;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                fragnum = 1;
                return new Fragment1 ();
            case 1:
                fragnum = 2;
                return new Fragment2 ();
            case 2:
                fragnum = 3;
                return new Fragment3 ();
            default:
                return null;
        }
    }



    @Override
    public int getCount() {
        return pageCount;
    }
}