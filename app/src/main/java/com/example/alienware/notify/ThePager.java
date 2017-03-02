package com.example.alienware.notify;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alienware on 01-02-2017.
 */

public class ThePager extends FragmentPagerAdapter {
    private final List<Fragment> fragments = new ArrayList<>();
    private final List<String> fragNames = new ArrayList<>();

   public ThePager(FragmentManager manager) {
       super(manager);
   }

    public void addFragment(Fragment fragment, String string){
        fragments.add(fragment);
        fragNames.add(string);
    }


    @Override
    public Fragment getItem(int position) {
       return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragNames.get(position);
    }
}
