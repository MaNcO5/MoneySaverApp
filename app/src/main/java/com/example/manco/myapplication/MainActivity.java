package com.example.manco.myapplication;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.manco.myapplication.controller.Controller;
import com.example.manco.myapplication.controller.DBController;
import com.example.manco.myapplication.fragments.ExpenseFragment;
import com.example.manco.myapplication.fragments.IncomeFragment;
import com.example.manco.myapplication.fragments.SummaryFragment;
import com.example.manco.myapplication.sharedpreferences.SharedPrefs;

import java.util.Locale;

/**
 * Created by Marjan Shredenkovski
 */

public class MainActivity extends Activity {
    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;
    DBController dbController;
    Controller controller;
    SummaryFragment sumFrag;
    ExpenseFragment expFrag;
    IncomeFragment incFrag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sumFrag = new SummaryFragment();
        expFrag = new ExpenseFragment();
        incFrag = new IncomeFragment();
        dbController = new DBController(this);

        initializeSystem();
    }


    /**
     * This method is initialize the system
     */
    public void initializeSystem(){
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.mainActivity);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        controller = new Controller(this, sumFrag, expFrag, incFrag, dbController);
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sp = getSharedPreferences( "MySharedString", MODE_PRIVATE );
        if (sp != null)
            setTitle( sp.getString( "sharedString", null ) );
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
//            alertDialog.show();
            startActivity( new Intent( this, SharedPrefs.class ) );
            return true;
        }
        return super.onOptionsItemSelected( item );
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position){

                case 0:
                    return sumFrag;
                case 1:
                    return incFrag;
                case 2:
                    return expFrag;

            }
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {

            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }
}