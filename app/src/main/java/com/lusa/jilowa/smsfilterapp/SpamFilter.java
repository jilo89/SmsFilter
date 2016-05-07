package com.lusa.jilowa.smsfilterapp;

/**
 * Created by JILOWA on 17-Jan-16.
 */

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SpamFilter extends AppCompatActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks,OnSpamLoaded,SwipeRefreshLayout.OnRefreshListener {


    protected Toolbar toolbar;
    private static final String INBOX_URI = "content://sms/inbox";
    private ArrayList<String> smsList = new ArrayList<String>();
    private ListView mListView;
    private ArrayAdapter<String> adapter;
    private String[] viewList;
    private static SpamFilter activity;
    private SwipeRefreshLayout mSwipeRefreshLayout;


    public static SpamFilter instance() {
        return activity;
    }
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private  SpamContact SpamContact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        mListView = (ListView) findViewById(R.id.list);
        setSupportActionBar(toolbar);
        /****************************************Tool bar setup **********************************/


        //mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);
        // getSupportActionBar().setHomeButtonEnabled(true);// enable or disable the "home" button in the corner of the action

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //show an arrow to the left indicating the action that will be taken


        mTitle = getTitle();
        SpamContact = new SpamContact(this, this);
        SpamContact.execute();
//        mSwipeRefreshLayout.setOnRefreshListener(this);

//        mSwipeRefreshLayout.post(new Runnable() {
//            @Override
//            public void run() {
//
//
//                mSwipeRefreshLayout.setRefreshing(true);
//            }
//        });
//        SpamContact=new SpamContact(this,this);
//        SpamContact.execute();

        //readSMS();

    }






    public void updateList(final String newSms) {
        adapter.insert(newSms, 0);
        adapter.notifyDataSetChanged();
    }
    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.app_bar, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sub, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            return true;
        }
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(SpamFilter.this);
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onSpamLoadedSuccess(List<String> smsList) {
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, smsList);
        mListView.setAdapter(adapter);
        viewList =getResources().getStringArray(R.array.list);
        Arrays.sort(viewList);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
    }

    @Override
    public void onRefresh() {

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

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}

