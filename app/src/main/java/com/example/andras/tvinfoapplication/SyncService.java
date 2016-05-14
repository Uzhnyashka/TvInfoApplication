package com.example.andras.tvinfoapplication;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import cz.msebera.android.httpclient.Header;

import com.example.andras.tvinfoapplication.Model.Channel;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by andras on 10.05.16.
 */
public class SyncService extends Service {
    public static final String URL_CHANNELS = "https://t2dev.firebaseio.com/CHANNEL.json";
    public static final String URL_CATEGORIES = "https://t2dev.firebaseio.com/CATEGORY.json";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        getChannels();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void getChannels() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        client.get(URL_CHANNELS, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                saveChannelsToDB(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }
    public void getCategories() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        client.get(URL_CATEGORIES, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("TAG", "onSuccess jsonobject");
                saveCategories(response);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("TAG", "onSUccess jsonarray");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    private void saveCategories(JSONObject response) {
        ContentValues values;
        Iterator x = response.keys();
        while (x.hasNext()) {
            String categoryKey = (String) x.next();
            values = new ContentValues();
            values.put(TvInfoContract.CategoryEntry.COLUMN_NAME, categoryKey);
            getContentResolver().insert(TvInfoContract.CategoryEntry.CONTENT_URI, values);
            updateChannelsCategories(categoryKey, response);
        }
    }
    private void updateChannelsCategories(String categoryKey, JSONObject response) {
        try {
            JSONObject jsonObject = response.getJSONObject(categoryKey);
            Iterator i = jsonObject.keys();
            while (i.hasNext()) {
                String channelKey = (String) i.next();
                //Log.d("myTAG", channelKey + " channel key in " + categoryKey);
                ContentValues cv = new ContentValues();
                cv.put(TvInfoContract.ChannelEntry.COLUMN_CATEGORY, categoryKey);
                int a = getContentResolver().update(TvInfoContract.ChannelEntry.CONTENT_URI, cv,
                        TvInfoContract.ChannelEntry.COLUMN_IDNAME + " = ?",
                        new String[]{channelKey});
                //Log.d("myTAG", "updated " + a + " rows, category key - " + categoryKey);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveChannelsToDB(JSONObject response) {
        try {
            Iterator x = response.keys();
            while (x.hasNext()) {
                String key = (String) x.next();
                JSONObject o = response.getJSONObject(key);
                Channel channel = Channel.fromJson(o);
                saveChannelToDB(channel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            getCategories();
        }
    }
    //insert 1 channel to db
    public void saveChannelToDB(Channel c) {
        ContentValues values = new ContentValues();
        values.put(TvInfoContract.ChannelEntry.COLUMN_IDNAME, c.getId());
        values.put(TvInfoContract.ChannelEntry.COLUMN_NAME, c.getName());
        values.put(TvInfoContract.ChannelEntry.COLUMN_DESCRIPTION, c.getDescription());
        values.put(TvInfoContract.ChannelEntry.COLUMN_LOGO_URL, c.getLogoURL());
        values.put(TvInfoContract.ChannelEntry.COLUMN_SITE_URL, c.getSiteURL());
        values.put(TvInfoContract.ChannelEntry.COLUMN_STREAM_URL, c.getStreamURL());
        values.put(TvInfoContract.ChannelEntry.COLUMN_TV_URL, c.getTvURL());
        values.put(TvInfoContract.ChannelEntry.COLUMN_YOUTUBE_URL, c.getYoutubeURL());
        getContentResolver().insert(TvInfoContract.ChannelEntry.CONTENT_URI, values);
    }
}