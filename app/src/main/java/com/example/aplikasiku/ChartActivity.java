package com.example.aplikasiku;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

public class ChartActivity extends AppCompatActivity {
//    FragmentPagerAdapter fragmentPagerAdapter;
//    public static int NUM_TIMES = 3;
//    public pagerAdapter(FragmentManager fragmentManager){
//        super(fragmentManager);
//    }
//    @Override
//    public int getCount(){
//        return NUM_TIMES;
//
//    }
//    @Override
//    public Fragment getItem(int position){
//        switch (position){
//            case 0:
//                return ChartRateAFragment.newInstance(0, "Rate A");
//            case 1:
//                return ChartRateAFragment.newInstance(1,"Rate B");
//            case 2:
//                return ChartRateAFragment.newInstance(3, "Rate C");
//            case 3:
//                return ChartRateAFragment.newInstance(1,"Rate D");
//            case 4:
//                return ChartRateAFragment.newInstance(3, "Rate P");
//            default:
//                return null;
//        }
//    }
//    @Override
//    public CharSequence getPageTitle(int position){
//        return "Page " +position;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

//        ViewPager viewPager=findViewById(R.id.vplist);
//        fragmentPagerAdapter = new PagerAdapter(getSupportFragmentManager());
//        viewPager.setAdapter(fragmentPagerAdapter);
    }
}