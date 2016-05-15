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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.example.andras.tvinfoapplication.Adapter.ChannelsCursorAdapter;
import com.example.andras.tvinfoapplication.DatabaseHelper;
import com.example.andras.tvinfoapplication.MainActivity;
import com.example.andras.tvinfoapplication.Model.Channel;
import com.example.andras.tvinfoapplication.R;
import com.example.andras.tvinfoapplication.TvInfoContract;

/**
 * Created by andras on 11.05.16.
 */
public class ChannelsFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private DatabaseHelper dbh;
    private String categoryName;
    public static final String KEY_CATEGORY = "CATEGORY";

    private ChannelsCursorAdapter mChannelListAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_channels, null);
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dbh = DatabaseHelper.getInstance(getActivity());
        categoryName = getArguments().getString(KEY_CATEGORY);

        mChannelListAdapter = new ChannelsCursorAdapter(getActivity(), null, 0);
        setListAdapter(mChannelListAdapter);
        getLoaderManager().initLoader(MainActivity.ID_CHANNELS, null, this);

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ChannelsCursorAdapter.ViewHolder holder = (ChannelsCursorAdapter.ViewHolder) view.getTag();
                if (holder != null) {
                    Uri uri = TvInfoContract.ChannelEntry.buildChannelUri(holder.channelID);
                    Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        Channel channel = Channel.fromCursor(cursor);
                        long id = cursor.getLong(cursor.getColumnIndex(TvInfoContract.ChannelEntry._ID));
                        loadChannel(channel, id);
                    }
                } else {
                    return;
                }
            }
        });
    }

    public static ChannelsFragment newInstance(String categoryName) {
        Bundle args = new Bundle();
        args.putString(KEY_CATEGORY, categoryName);
        ChannelsFragment fragment = new ChannelsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void loadChannel(Channel channel, long id) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, ChannelDetailFragment.newInstance(channel, id));
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case MainActivity.ID_CHANNELS:
                /*return new CursorLoader(getActivity(), TvInfoContract.ChannelEntry.CONTENT_URI, null,
                        TvInfoContract.ChannelEntry.COLUMN_CATEGORY + " = ?", new String[] {categoryName}, null);*/
            //check this
            String projection[] = new String[] {TvInfoContract.ChannelEntry._ID, TvInfoContract.ChannelEntry.COLUMN_NAME, TvInfoContract.ChannelEntry.COLUMN_TV_URL};
            return new CursorLoader(getActivity(), TvInfoContract.ChannelEntry.CONTENT_URI,
                    projection, TvInfoContract.ChannelEntry.COLUMN_CATEGORY + " = ?",
                    new String[] {categoryName},
                    null);
            default:
                // An invalid id was passed in
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId())
        {
            case MainActivity.ID_CHANNELS:
                mChannelListAdapter.swapCursor(data);
                break;
            default:
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch (loader.getId())
        {
            case MainActivity.ID_CHANNELS:
                mChannelListAdapter.swapCursor(null);
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
    }
}