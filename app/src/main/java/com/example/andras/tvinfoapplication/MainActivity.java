package com.example.andras.tvinfoapplication;


import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.andras.tvinfoapplication.Adapter.CategoryCursorAdapter;
import com.example.andras.tvinfoapplication.Fragment.CategoriesFragment;
import com.example.andras.tvinfoapplication.Fragment.ChannelsFragment;

import java.io.FileDescriptor;
import java.io.PrintWriter;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final int ID_CHANNELS = 228;
    public static final int ID_CATEGORIES = 229;
    public static final int DELETE_CHANNELS = 666;
    public static final int DELETE_CATEGORIES = 667;

    private DatabaseHelper dbh;
    private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ListView mDrawerList;
    private CategoryCursorAdapter mDrawerListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        dbh = DatabaseHelper.getInstance(this);
        loadCategoriesFragment();

        mDrawerListAdapter = new CategoryCursorAdapter(this, null, 0);
        mDrawerList.setAdapter(mDrawerListAdapter);
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CategoryCursorAdapter.ViewHolder holder = (CategoryCursorAdapter.ViewHolder) view.getTag();
                if(holder != null) {
                    Uri uri = TvInfoContract.CategoryEntry.buildCategoryUri(holder.categoryID);
                    Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        String categoryName = cursor.getString(cursor.getColumnIndex(TvInfoContract.CategoryEntry.COLUMN_NAME));
                        Toast.makeText(getApplicationContext(), categoryName, Toast.LENGTH_SHORT).show();
                        loadChannelsFragment(categoryName);
                        mDrawerLayout.closeDrawers();
                    }
                } else {
                    return;
                }
            }
        });
        getSupportLoaderManager().initLoader(ID_CATEGORIES, null, this);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_sync:
                syncClicked();
                Toast.makeText(this, "Sync started", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_clear:
                clearDB();
                Toast.makeText(MainActivity.this, "db cleared", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void syncClicked() {
        clearDB();
        Intent i = new Intent(this, SyncService.class);
        startService(i);
    }
    public void clearDB() {
        //dbh.clearChannelsAndCategories();
        getSupportLoaderManager().restartLoader(DELETE_CATEGORIES, null, this);
        getSupportLoaderManager().restartLoader(DELETE_CHANNELS, null, this);
    }

    public void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.drawer_list);
        drawerToggle = setupDrawerToggle();
        mDrawerLayout.addDrawerListener(drawerToggle);
    }

    public ActionBarDrawerToggle setupDrawerToggle() {
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
                R.string.drawer_open, R.string.drawer_close);
        drawerToggle.setDrawerIndicatorEnabled(true);
        return drawerToggle;
    }

    public void loadChannelsFragment(String categoryName) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, ChannelsFragment.newInstance(categoryName));
        ft.addToBackStack(null);
        ft.commit();
    }
    public void loadCategoriesFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, CategoriesFragment.newInstance());
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (dbh != null) {
            dbh.close();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (dbh == null) {
            dbh = DatabaseHelper.getInstance(this);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case MainActivity.ID_CATEGORIES:
                return new CursorLoader(this, TvInfoContract.CategoryEntry.CONTENT_URI, null, null, null, null);
            //return new TvInfoCursorLoader(this, dbh, ID_CATEGORIES);
            case DELETE_CATEGORIES:
                Log.d("myTAG", "delte categories loader");
                getContentResolver().delete(TvInfoContract.CategoryEntry.CONTENT_URI, null, null);
                return new CursorLoader(this, TvInfoContract.CategoryEntry.CONTENT_URI, null, null, null, null);
            case DELETE_CHANNELS:
                Log.d("myTAG", "delte channels loader");
                getContentResolver().delete(TvInfoContract.ChannelEntry.CONTENT_URI, null, null);
                return new CursorLoader(this, TvInfoContract.ChannelEntry.CONTENT_URI, null, null, null, null);
            default:
                // An invalid id was passed in
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId())
        {
            case ID_CATEGORIES:
                mDrawerListAdapter.swapCursor(data);
                Toast.makeText(this, "onLoadFinished swapCursor", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch (loader.getId())
        {
            case ID_CATEGORIES:
                mDrawerListAdapter.swapCursor(null);
                Toast.makeText(this, "onLoadReset swapCursor", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }
}
