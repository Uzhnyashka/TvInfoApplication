package com.example.andras.tvinfoapplication.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.andras.tvinfoapplication.R;
import com.example.andras.tvinfoapplication.TvInfoContract;

/**
 * Created by andras on 11.05.16.
 */
public class ChannelsCursorAdapter extends CursorAdapter {
    private LayoutInflater mInflater;
    private Cursor cursor;

    public ChannelsCursorAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.cursor = cursor;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View root = mInflater.inflate(R.layout.channel_row, viewGroup, false);
        ViewHolder holder = new ViewHolder();
        TextView tvName = (TextView) root.findViewById(R.id.tv_channel_name);
        TextView tvTvURL = (TextView) root.findViewById(R.id.tv_channel_tvurl);
        holder.tvName = tvName;
        holder.tvTvURL = tvTvURL;
        root.setTag(holder);
        return root;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndex(TvInfoContract.ChannelEntry._ID));
        String channelName = cursor.getString(cursor.getColumnIndex(TvInfoContract.ChannelEntry.COLUMN_NAME));
        String channelTvURL = cursor.getString(cursor.getColumnIndex(TvInfoContract.ChannelEntry.COLUMN_TV_URL));
        ViewHolder holder = (ViewHolder) view.getTag();
        if (holder != null) {
            holder.tvName.setText(channelName);
            holder.tvTvURL.setText(channelTvURL);
            holder.channelID = id;
        }
    }

    public static class ViewHolder {
        public TextView tvName;
        public TextView tvTvURL;
        public long channelID;
    }
}