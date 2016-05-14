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
 * Created by andras on 12.05.16.
 */
public class CategoryCursorAdapter extends CursorAdapter {
    private LayoutInflater mInflater;
    private Cursor cursor;

    public CategoryCursorAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.cursor = cursor;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View root = mInflater.inflate(R.layout.category_row, viewGroup, false);
        ViewHolder holder = new ViewHolder();
        TextView tvName = (TextView) root.findViewById(R.id.tv_category_name);
        holder.tvName = tvName;
        root.setTag(holder);
        return root;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndex("_id"));
        String category = cursor.getString(cursor.getColumnIndex(TvInfoContract.CategoryEntry.COLUMN_NAME));
        ViewHolder holder = (ViewHolder) view.getTag();
        if(holder != null) {
            holder.tvName.setText(category);
            holder.categoryID = id;
        }
    }

    public static class ViewHolder {
        public TextView tvName;
        public long categoryID;
    }
}