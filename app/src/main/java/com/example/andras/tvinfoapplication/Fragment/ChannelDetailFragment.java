package com.example.andras.tvinfoapplication.Fragment;


import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.andras.tvinfoapplication.Model.Channel;
import com.example.andras.tvinfoapplication.R;
import com.example.andras.tvinfoapplication.TvInfoContract;


/**
 * Created by andras on 11.05.16.
 */
public class ChannelDetailFragment extends Fragment {
    public static final String CHANNEL_KEY = "CHANNEL_KEY";
    public static final String CHANNEL_ID = "CHANNEL_ID";
    private TextView tvName;
    private TextView tvTvURL;
    private TextView tvID;
    private TextView tvCategory;
    private Button add;
    private Button delete;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final Channel channel = (Channel) getArguments().getSerializable(CHANNEL_KEY);
        if (channel != null) {
            tvName.setText(channel.getName());
            tvID.setText(channel.getId());
            tvTvURL.setText(channel.getTvURL());
            tvCategory.setText("category: " + channel.getCategory());
        }

        final long id = getArguments().getLong(CHANNEL_ID);
        initButtons(id);

        if (add.getVisibility() == View.VISIBLE) {
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ContentValues cv = new ContentValues();
                    cv.put(TvInfoContract.FavouritesEntry.COLUMN_FAVOURITE_CHANNEL_ID, channel.getId());
                    getActivity().getContentResolver().insert(TvInfoContract.FavouritesEntry.CONTENT_URI,
                            cv);
                    add.setVisibility(View.GONE);
                    delete.setVisibility(View.VISIBLE);
                }
            });
        } else {
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri uri = TvInfoContract.FavouritesEntry.buildCategoryUri(id);
                    getActivity().getContentResolver().delete(uri, null, null);
                    add.setVisibility(View.VISIBLE);
                    delete.setVisibility(View.GONE);
                }
            });
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_channel_details, null);
        tvName = (TextView) v.findViewById(R.id.tv_channel_name);
        tvTvURL = (TextView) v.findViewById(R.id.tv_channel_tvurl);
        tvID = (TextView) v.findViewById(R.id.tv_channel_id);
        tvCategory = (TextView) v.findViewById(R.id.tv_channel_category);
        add = (Button) v.findViewById(R.id.btn_add_to_favourites);
        delete = (Button) v.findViewById(R.id.btn_delete_from_favourites);
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static ChannelDetailFragment newInstance(Channel channel, long id) {
        Bundle args = new Bundle();
        args.putSerializable(CHANNEL_KEY, channel);
        args.putLong(CHANNEL_ID, id);
        ChannelDetailFragment fragment = new ChannelDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void initButtons(long id) {
        Cursor cursor = getActivity().getContentResolver().query(TvInfoContract.FavouritesEntry.CONTENT_URI,
                null, TvInfoContract.FavouritesEntry.COLUMN_FAVOURITE_CHANNEL_ID + " = ?",
                new String[] {String.valueOf(id)}, null);
        if (cursor != null && cursor.moveToFirst()) {
            delete.setVisibility(View.VISIBLE);
        } else {
            add.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}