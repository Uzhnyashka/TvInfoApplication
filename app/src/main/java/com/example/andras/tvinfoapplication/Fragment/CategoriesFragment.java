package com.example.andras.tvinfoapplication.Fragment;


import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.andras.tvinfoapplication.Adapter.CategoryCursorAdapter;
import com.example.andras.tvinfoapplication.DatabaseHelper;
import com.example.andras.tvinfoapplication.MainActivity;
import com.example.andras.tvinfoapplication.R;
import com.example.andras.tvinfoapplication.TvInfoContract;


/**
 * Created by andras on 13.05.16.
 */
public class CategoriesFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private String categoryName;
    private DatabaseHelper dbh;
    private CategoryCursorAdapter mCategoryCursorAdapter;
    private SwipeRefreshLayout swipeContainer;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dbh = DatabaseHelper.getInstance(getActivity());

        mCategoryCursorAdapter = new CategoryCursorAdapter(getActivity(), null, 0);
        setListAdapter(mCategoryCursorAdapter);

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CategoryCursorAdapter.ViewHolder holder = (CategoryCursorAdapter.ViewHolder) view.getTag();
                if(holder != null) {
                    Uri uri = TvInfoContract.CategoryEntry.buildCategoryUri(holder.categoryID);
                    Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        String categoryName = cursor.getString(cursor.getColumnIndex(TvInfoContract.CategoryEntry.COLUMN_NAME));
                        Log.d("myTAG", categoryName + " clicked");
                        Toast.makeText(getActivity(), categoryName, Toast.LENGTH_SHORT).show();
                        loadChannelsFragment(categoryName);
                    }
                } else {
                    return;
                }
            }
        });
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getLoaderManager().restartLoader(MainActivity.ID_CATEGORIES, null, CategoriesFragment.this);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_categories, null);
        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
        return v;
    }
    public static CategoriesFragment newInstance() {
        Bundle args = new Bundle();
        CategoriesFragment fragment = new CategoriesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void loadChannelsFragment(String categoryName) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, ChannelsFragment.newInstance(categoryName));
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case MainActivity.ID_CATEGORIES:
                return new CursorLoader(getActivity(), TvInfoContract.CategoryEntry.CONTENT_URI, null, null, null, null);
            default:
                // An invalid id was passed in
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId())
        {
            case MainActivity.ID_CATEGORIES:
                mCategoryCursorAdapter.swapCursor(data);
                swipeContainer.setRefreshing(false);
                break;
            default:
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch (loader.getId())
        {
            case MainActivity.ID_CATEGORIES:
                mCategoryCursorAdapter.swapCursor(null);
                break;
            default:
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (dbh != null) {
            dbh.close();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (dbh == null) {
            dbh = DatabaseHelper.getInstance(getActivity());
        }
        getLoaderManager().initLoader(MainActivity.ID_CATEGORIES, null, this);
    }
}