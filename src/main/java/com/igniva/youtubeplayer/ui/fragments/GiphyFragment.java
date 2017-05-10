package com.igniva.youtubeplayer.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.igniva.youtubeplayer.R;

/**
 * Created by igniva-android-17 on 30/3/17.
 */

public class GiphyFragment extends BaseFragment implements TabLayout.OnTabSelectedListener {

    private View view;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (view == null) {
            view = inflater.inflate(R.layout.fragment_giphy, container, false);
            setUpLayouts();
        }
        return view;
    }

    @Override
    public void setUpLayouts() {
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);

        //Adding the tabs using addTab() method
        tabLayout.addTab(tabLayout.newTab().setText("Tab1"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab2"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager) view.findViewById(R.id.pager);

        Pager adapter = new Pager(getChildFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        tabLayout.setOnTabSelectedListener(this);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void setDataInViewLayouts() {

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    public class Pager extends FragmentStatePagerAdapter {

        int tabCount;
        private String[] tabTitles = new String[]{"Tab1", "Tab2"};

        public Pager(FragmentManager fm, int tabCount) {
            super(fm);
            this.tabCount = tabCount;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    GiphyTabOneFragment tab1 = new GiphyTabOneFragment();
                    return tab1;
                case 1:
                    GallaryFragment tab2 = new GallaryFragment();
                    return tab2;
                default:
                    return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        @Override
        public int getCount() {
            return tabCount;
        }
    }
}
