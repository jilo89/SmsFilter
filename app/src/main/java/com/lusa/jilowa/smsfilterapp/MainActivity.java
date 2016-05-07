package com.lusa.jilowa.smsfilterapp;
/**************************************** Imports **********************************/

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

/****************************************
 * End of  Imports
 **********************************/

public class MainActivity extends AppCompatActivity implements OnSMSLoaded, SwipeRefreshLayout.OnRefreshListener {

    private NavigationDrawerFragment mDrawerFragment;
    private CharSequence mTitle;
    private final static String  SMS_LIST_TAG="SMS_LIST_TAG";
    private Toolbar toolbar;
    private static final String INBOX_URI = "content://sms/inbox";
    private static MainActivity activity;
    private ArrayList<String> smsList = new ArrayList<String>();
    private ListView mListView;
    private ArrayAdapter<String> adapter;
    private String[] viewList;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private  SmsContact smsContact;
    final String TAG = "States";
    /****************************************
     * Instance of mainActivity
     **********************************/
    public static MainActivity instance() {
        return activity;
    }
    /**************************************** End of Instance **********************************/

    /****************************************
     * On Create Method
     **********************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_appbar);

        /****************************************Tool bar setup **********************************/
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mListView = (ListView) findViewById(R.id.list);
        /**************************************** End of toolbar setup **********************************/

        /**************************************** Swipe to refresh**********************************/
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);
        /**************************************** End of Swipe**********************************/

        /****************************************Navigation Drawer **********************************/
        mDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        mDrawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);

        mTitle = getTitle();
        /**************************************** End of Navigation**********************************/
        if(savedInstanceState!=null){
            ArrayList<String> arrayList= new ArrayList();
            arrayList=savedInstanceState.getStringArrayList(SMS_LIST_TAG);
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
            mListView.setAdapter(adapter);
            viewList = getResources().getStringArray(R.array.list);
            Arrays.sort(viewList);

        } else{
            smsContact = new SmsContact(this, this);
            mSwipeRefreshLayout.setOnRefreshListener(this);

            mSwipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {

                    smsContact.execute();
                    mSwipeRefreshLayout.setRefreshing(true);
                }
            });


        }






        Log.d(TAG, "MainActivity: onCreate()");


    }


    /****************************************
     * End of On create Method
     **********************************/
    private void setupAdapter() {
        final ArrayList<String> listaelemek = new ArrayList<String>();
        mListView = (ListView) findViewById(R.id.list);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, smsList);
        mListView.setAdapter(adapter);
        viewList = getResources().getStringArray(R.array.list);
        Arrays.sort(viewList);
        registerForContextMenu(mListView);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
//
//        final Button hozzaad = (Button) findViewById(R.id.bHozzaad);
//        hozzaad.setOnClickListener(new View.OnClickListener() {
//                                       public void onClick(View v) {
//                                           EditText entry = (EditText) findViewById(R.id.entry);
//                                           listaelemek.add(entry.getText().toString());
//                                           adapter.notifyDataSetChanged();
//                                       }
//                                   }
//        );
    }

    @Override
    public void onCreateContextMenu(android.view.ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.contextmenu, menu);

    }

    public String getContactName(Context context, String phoneNumber) {
        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,Uri.encode(phoneNumber));
        Cursor cursor = cr.query(uri,new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
//        if (cursor == null) {
//            return null;
//        }
        String contactName = null;
        if (cursor.moveToFirst()) {
            contactName = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
        }
//        if (cursor != null && !cursor.isClosed()) {
//            cursor.close();
//        }
        cursor.close();
        return contactName;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
//            case R.id.editItem:
//
//                return true;
//            case R.id.markItem:
////do something
//                return true;
            case R.id.deleteItem:
                delete_thread(item.getItemId());
                adapter.remove(adapter.getItem(info.position));
                ;
                adapter.notifyDataSetChanged();


                return true;
//            case R.id.permItem:
////do something
//                return true;
//            case R.id.copyItem:
////do something
//                return true;
//            case R.id.moveItem:
//do something

            default:
                return super.onContextItemSelected(item);
        }


    }


    @Override
    public void onStart() {
        super.onStart();
        activity = this;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//       String stateToSave = smsContact.execute().toString();
//        outState.putString("saved_state", stateToSave);
        outState.putStringArrayList(SMS_LIST_TAG, getStringArrayList());
        
    }

       public void updateList(final String newSms) {
        adapter.insert(newSms, 0);
        adapter.notifyDataSetChanged();
    }
    private AdapterView.OnItemClickListener MyItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
            try {
                Toast.makeText(getApplicationContext(), adapter.getItem(pos), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        if ( id == R.id.navigate){
            startActivity(new Intent(this, SettingsActivity.class));
        }



        return super.onOptionsItemSelected(item);
    }


    public void onSectionAttached(int number) {
    }
    public void delete_thread(int thread) {
        Cursor c = getApplicationContext().getContentResolver().query(
                Uri.parse("content://sms/"),new String[] {
                        "_id", "thread_id", "address", "person", "date","body" }, null, null, null);

        try {
            while (c.moveToNext())
            {
                int id = c.getInt(0);
                String address = c.getString(2);
                if (address.equals(thread))
                {
                    getApplicationContext().getContentResolver().delete(
                            Uri.parse("content://sms/" + id), null, null);
                }

            }
        } catch (Exception e) {

        }
    }

    @Override
    public void onSMSLoadedSuccess(final ArrayList<String> smsList) {
        mSwipeRefreshLayout.setRefreshing(false);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, smsList);
        setSMSList(smsList);


        mListView.setAdapter(adapter);
        viewList = getResources().getStringArray(R.array.list);
        Arrays.sort(viewList);
        registerForContextMenu(mListView);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s = smsList.get(position);
                toast(s);

            }
        });


    }

    private  void toast(String s){
        Toast.makeText(this, s,Toast.LENGTH_LONG).show();

    }

    @Override
    public void onRefresh() {

    }
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "MainActivity: onRestart()");
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "MainActivity: onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "MainActivity: onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "MainActivity: onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "MainActivity: onDestroy()");
    }

    @Override
    public void finish() {
        super.finish();
        Log.d(TAG,"MainActivity: finish()");
    }



    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
       savedInstanceState.getStringArray(SMS_LIST_TAG);




}
    private ArrayList<String> getStringArrayList(){
        return this.smsList;
    }

    private void  setSMSList(ArrayList<String> smsList){
        this.smsList=smsList;
    }

    }