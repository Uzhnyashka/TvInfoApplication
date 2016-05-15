package com.example.andras.tvinfoapplication;

/**
 * Created by andras on 14.05.16.
 */

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by andras on 12.05.16.
 */
public class TvInfoContract {

    public static final String CONTENT_AUTHORITY = "com.example.andras.tvinfoapplication";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_FAVOURITE = "favourite";
    public static final String PATH_CHANNEL = "channel";
    public static final String PATH_CATEGORY = "category";

    public static final class ChannelEntry implements BaseColumns {
        // Content URI represents the base location for the table
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_CHANNEL).build();

        // These are special type prefixes that specify if a URI returns a list or a specific item
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_URI  + "/" + PATH_CHANNEL;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_URI + "/" + PATH_CHANNEL;

        // Define the table schema
        public static final String CHANNEL_TABLE_NAME = "channel";
        public static final String COLUMN_IDNAME = "idname";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_LOGO_URL = "logoURL";
        public static final String COLUMN_SITE_URL = "siteURL";
        public static final String COLUMN_STREAM_URL = "streamURL";
        public static final String COLUMN_TV_URL = "tvURL";
        public static final String COLUMN_YOUTUBE_URL = "youtubeURL";
        public static final String COLUMN_CATEGORY = "category";

        // Define a function to build a URI to find a specific movie by it's identifier
        public static Uri buildChannelUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class CategoryEntry implements BaseColumns{
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_CATEGORY).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_URI + "/" + PATH_CATEGORY;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_URI + "/" + PATH_CATEGORY;

        public static final String CATEGORY_TABLE_NAME = "category";
        public static final String COLUMN_NAME = "category";

        public static Uri buildCategoryUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class FavouritesEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVOURITE).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_URI + "/" + PATH_FAVOURITE;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_URI + "/" + PATH_FAVOURITE;

        public static final String FAVOURITE_TABLE_NAME = "favourite";
        public static final String COLUMN_FAVOURITE_CHANNEL_ID = "channel_id";

        public static Uri buildCategoryUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}