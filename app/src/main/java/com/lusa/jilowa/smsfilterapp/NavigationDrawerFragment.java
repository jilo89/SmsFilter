package com.lusa.jilowa.smsfilterapp;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
//import android.support.v7.widget.LinearLayoutManager;

import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationDrawerFragment extends Fragment implements DrawerAdapter.ClickListner{
    private RecyclerView recyclerView;
    public static final String PREF_FILE_NAME = "testpref";
    public static final String KEY_USER_LEARNED_DRAWER = "user_learned_drawer";
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private boolean mUserLearnedDrawer;
    private boolean mFromSavedInstanceState;
    private View containerView;
    private DrawerAdapter adapter;
    private NavigationDrawerCallbacks mCallbacks;
    public NavigationDrawerFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserLearnedDrawer = Boolean.valueOf(readFromPreferences(getActivity(),KEY_USER_LEARNED_DRAWER,"false"));
        if(savedInstanceState != null){
            mFromSavedInstanceState = true;
        }
    }
    public List<NavItems> getData(){
        List<NavItems> data =  new ArrayList<>();
     int[] icons = {R.drawable.ic_number1,R.drawable.ic_number2,R.drawable.ic_number3,R.drawable.ic_number4};
        String[]titles = {
                getString(R.string.title_section1),
                getString(R.string.title_section2),
                getString(R.string.title_section3),
                getString(R.string.title_section4),
        };

        for ( int i=0; i<titles.length && i<icons.length;i++){
            NavItems current = new NavItems();
           current.iconId= icons[i];
            current.title=titles[i];
            data.add(current);

        }
        return data;
    }
      @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
          View layout =inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
           recyclerView = (RecyclerView) layout.findViewById(R.id.drawerlist);
          adapter = new DrawerAdapter(getActivity(),getData());
          adapter.setClickListner(this);
          recyclerView.setAdapter(adapter);
          recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return layout;
    }


    public void setUp(int fragmentId ,DrawerLayout drawerlayout, final Toolbar toolbar){
        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerlayout;//being passed
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(),drawerlayout,toolbar,R.string.drawer_open,R.string.drawer_close){

            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if(!mUserLearnedDrawer) {

                    mUserLearnedDrawer = true;
                    saveToPreferences(getActivity(), KEY_USER_LEARNED_DRAWER, mUserLearnedDrawer + "");
                }
                getActivity().invalidateOptionsMenu();
            }

            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                if(slideOffset<0.6) {
                    toolbar.setAlpha(1-slideOffset);
                }
            }
        };

        if(!mUserLearnedDrawer && !mFromSavedInstanceState){
            mDrawerLayout.openDrawer(containerView );

        }
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
    }

    public static void saveToPreferences(Context context, String preferenceName, String preferenceValue){

        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(preferenceName, preferenceValue);
        editor.apply();
    }

    public static String readFromPreferences(Context context, String preferenceName, String defaultValue){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(preferenceName, defaultValue);
    }

    @Override
    public void itemClicked(View view, int position) {
        switch(position){
            case 0:
                startActivity(new Intent(getActivity(),MainActivity.class));
                break;
            case 1:
                startActivity(new Intent(getActivity(),SpamFilter.class));
                break;
            case 2:
                startActivity(new Intent(getActivity(),SettingsActivity.class));
                break;
            case 3:
                startActivity(new Intent(getActivity(),About.class));
                break;
            default:
                startActivity(new Intent(getActivity(),MainActivity.class));
                break;
        }

    }

    /**
     * Callbacks interface that all activities using this fragment must implement.
     */
    public interface NavigationDrawerCallbacks {
        /**
         * Called when an item in the navigation drawer is selected.
         */
        void onNavigationDrawerItemSelected(int position);
    }
}
