package com.example.andras.tvinfoapplication;

/**
 * Created by andras on 14.05.16.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * Created by andras on 11.05.16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private SQLiteDatabase db;
    private static DatabaseHelper databaseHelper;
    static final String DATABASE_NAME = "TVINFO";
    static final int DATABASE_VERSION = 1;

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (databaseHelper == null) {
            databaseHelper = new DatabaseHelper(context);
        }
        return databaseHelper;
    }

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        addChannelTable(db);
        addCategoryTable(db);
        addFavouritesTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TvInfoContract.CategoryEntry.CATEGORY_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TvInfoContract.ChannelEntry.CHANNEL_TABLE_NAME);
        onCreate(db);
    }

    private void addChannelTable(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + TvInfoContract.ChannelEntry.CHANNEL_TABLE_NAME + " (" +
                        TvInfoContract.ChannelEntry._ID + " INTEGER PRIMARY KEY, " +
                        TvInfoContract.ChannelEntry.COLUMN_NAME + " TEXT, " +
                        TvInfoContract.ChannelEntry.COLUMN_DESCRIPTION + " TEXT, " +
                        TvInfoContract.ChannelEntry.COLUMN_IDNAME + " TEXT, " +
                        TvInfoContract.ChannelEntry.COLUMN_LOGO_URL + " TEXT, " +
                        TvInfoContract.ChannelEntry.COLUMN_SITE_URL + " TEXT, " +
                        TvInfoContract.ChannelEntry.COLUMN_STREAM_URL + " TEXT, " +
                        TvInfoContract.ChannelEntry.COLUMN_TV_URL + " TEXT, " +
                        TvInfoContract.ChannelEntry.COLUMN_CATEGORY + " TEXT, " +
                        TvInfoContract.ChannelEntry.COLUMN_YOUTUBE_URL + " TEXT);"
        );
    }

    private void addCategoryTable(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + TvInfoContract.CategoryEntry.CATEGORY_TABLE_NAME + " (" +
                        TvInfoContract.CategoryEntry._ID + " INTEGER PRIMARY KEY, " +
                        TvInfoContract.CategoryEntry.COLUMN_NAME + " TEXT);"
        );
    }

    private void addFavouritesTable(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + TvInfoContract.FavouritesEntry.FAVOURITE_TABLE_NAME + " (" +
                        TvInfoContract.FavouritesEntry._ID + " INTEGER PRIMARY KEY, " +
                        TvInfoContract.FavouritesEntry.COLUMN_FAVOURITE_CHANNEL_ID + " TEXT UNIQUE NOT NULL);"
        );
    }
}
