package com.example.andras.tvinfoapplication;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.widget.TextViewCompat;

/**
 * Created by andras on 10.05.16.
 */
public class TvInfoProvider extends ContentProvider {
    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static final int CHANNEL = 100;
    private static final int CHANNEL_ID = 101;
    private static final int CATEGORY = 200;
    private static final int CATEGORY_ID = 201;
    private static final int FAVOURITE = 300;
    public static final int FAVOURITE_ID = 301;

    /**
     * Builds a UriMatcher that is used to determine witch database request is being made.
     */
    public static UriMatcher buildUriMatcher(){
        String content = TvInfoContract.CONTENT_AUTHORITY;

        // All paths to the UriMatcher have a corresponding code to return
        // when a match is found (the ints above).
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(content, TvInfoContract.PATH_CHANNEL, CHANNEL);
        matcher.addURI(content, TvInfoContract.PATH_CHANNEL + "/#", CHANNEL_ID);
        matcher.addURI(content, TvInfoContract.PATH_CATEGORY, CATEGORY);
        matcher.addURI(content, TvInfoContract.PATH_CATEGORY + "/#", CATEGORY_ID);
        matcher.addURI(content, TvInfoContract.PATH_FAVOURITE, FAVOURITE);
        matcher.addURI(content, TvInfoContract.PATH_FAVOURITE + "/#", FAVOURITE_ID);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
        return (db == null) ? false:true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor;
        long id;
        switch(sUriMatcher.match(uri)){
            case CHANNEL:
                cursor = db.query(
                        TvInfoContract.ChannelEntry.CHANNEL_TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case CHANNEL_ID:
                id = ContentUris.parseId(uri);
                cursor = db.query(
                        TvInfoContract.ChannelEntry.CHANNEL_TABLE_NAME,
                        projection,
                        TvInfoContract.ChannelEntry._ID + " = ?",
                        new String[]{String.valueOf(id)},
                        null,
                        null,
                        sortOrder
                );
                break;
            case CATEGORY:
                cursor = db.query(
                        TvInfoContract.CategoryEntry.CATEGORY_TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case CATEGORY_ID:
                id = ContentUris.parseId(uri);
                cursor = db.query(
                        TvInfoContract.CategoryEntry.CATEGORY_TABLE_NAME,
                        projection,
                        TvInfoContract.CategoryEntry._ID + " = ?",
                        new String[]{String.valueOf(id)},
                        null,
                        null,
                        sortOrder
                );
                break;
            case FAVOURITE:
                cursor = db.query(TvInfoContract.FavouritesEntry.FAVOURITE_TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case FAVOURITE_ID:
                id = ContentUris.parseId(uri);
                cursor = db.query(TvInfoContract.FavouritesEntry.FAVOURITE_TABLE_NAME,
                        projection,
                        TvInfoContract.FavouritesEntry._ID + " = ?",
                        new String[] {String.valueOf(id)},
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch(sUriMatcher.match(uri)){
            case CHANNEL:
                return TvInfoContract.ChannelEntry.CONTENT_TYPE;
            case CHANNEL_ID:
                return TvInfoContract.ChannelEntry.CONTENT_ITEM_TYPE;
            case CATEGORY:
                return TvInfoContract.CategoryEntry.CONTENT_TYPE;
            case CATEGORY_ID:
                return TvInfoContract.CategoryEntry.CONTENT_ITEM_TYPE;
            case FAVOURITE:
                return TvInfoContract.FavouritesEntry.CONTENT_TYPE;
            case FAVOURITE_ID:
                return TvInfoContract.FavouritesEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        long _id;
        Uri returnUri;

        switch(sUriMatcher.match(uri)){
            case CHANNEL:
                _id = db.insert(TvInfoContract.ChannelEntry.CHANNEL_TABLE_NAME, null, contentValues);
                if(_id > 0){
                    returnUri =  TvInfoContract.ChannelEntry.buildChannelUri(_id);
                } else {
                    throw new UnsupportedOperationException("Unable to insert rows into: " + uri);
                }
                break;
            case CATEGORY:
                _id = db.insert(TvInfoContract.CategoryEntry.CATEGORY_TABLE_NAME, null, contentValues);
                if(_id > 0){
                    returnUri = TvInfoContract.CategoryEntry.buildCategoryUri(_id);
                } else{
                    throw new UnsupportedOperationException("Unable to insert rows into: " + uri);
                }
                break;
            case FAVOURITE:
                _id = db.insert(TvInfoContract.FavouritesEntry.FAVOURITE_TABLE_NAME,
                        null,
                        contentValues);
                if (_id > 0) {
                    returnUri = TvInfoContract.FavouritesEntry.buildCategoryUri(_id);
                } else {
                    throw new UnsupportedOperationException("Unable to insert rows into: " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int rows;
        switch(sUriMatcher.match(uri)) {
            case FAVOURITE_ID:
                rows = db.delete(TvInfoContract.FavouritesEntry.FAVOURITE_TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because null could delete all rows:
        if (selection == null || rows != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rows;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int rows;
        switch (sUriMatcher.match(uri)) {
            case CHANNEL_ID:
                rows = db.update(TvInfoContract.ChannelEntry.CHANNEL_TABLE_NAME, values, selection, selectionArgs);
                break;
            case CHANNEL:
                rows = db.update(TvInfoContract.ChannelEntry.CHANNEL_TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rows != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rows;
    }
}