package com.example.andras.tvinfoapplication.Model;


import android.database.Cursor;

import com.example.andras.tvinfoapplication.TvInfoContract;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by andras on 10.05.16.
 */
public class Channel implements Serializable {
    private String description;
    private String id;
    private String logoURL;
    private String siteURL;
    private String streamURL;
    private String tvURL;
    private String youtubeURL;
    private String name;
    private String category;

    public Channel () {}

    public Channel(String description, String id, String logoURL, String siteURL, String streamURL, String tvURL, String youtubeURL, String name) {
        this.description = description;
        this.id = id;
        this.logoURL = logoURL;
        this.siteURL = siteURL;
        this.streamURL = streamURL;
        this.tvURL = tvURL;
        this.youtubeURL = youtubeURL;
        this.name = name;
    }

    public static Channel fromJson(JSONObject jsonObject) {
        Channel c = new Channel();
        try {
            c.id = jsonObject.getString("id");
            c.name = jsonObject.getString("name");
            c.description = jsonObject.getString("description");
            c.logoURL = jsonObject.getString("logoURL");
            c.siteURL = jsonObject.getString("siteURL");
            c.streamURL = jsonObject.getString("streamURL");
            c.tvURL = jsonObject.getString("tvURL");
            c.youtubeURL = jsonObject.getString("youtubeURL");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return c;
    }

    public static Channel fromCursor(Cursor cursor) {
        Channel channel = new Channel();
        if (cursor.moveToFirst()) {
            channel = new Channel();
            channel.setDescription(cursor.getString(cursor.getColumnIndex(TvInfoContract.ChannelEntry.COLUMN_DESCRIPTION)));
            channel.setName(cursor.getString(cursor.getColumnIndex(TvInfoContract.ChannelEntry.COLUMN_NAME)));
            channel.setId(cursor.getString(cursor.getColumnIndex(TvInfoContract.ChannelEntry.COLUMN_IDNAME)));
            channel.setLogoURL(cursor.getString(cursor.getColumnIndex(TvInfoContract.ChannelEntry.COLUMN_LOGO_URL)));
            channel.setSiteURL(cursor.getString(cursor.getColumnIndex(TvInfoContract.ChannelEntry.COLUMN_SITE_URL)));
            channel.setStreamURL(cursor.getString(cursor.getColumnIndex(TvInfoContract.ChannelEntry.COLUMN_STREAM_URL)));
            channel.setTvURL(cursor.getString(cursor.getColumnIndex(TvInfoContract.ChannelEntry.COLUMN_TV_URL)));
            channel.setYoutubeURL(cursor.getString(cursor.getColumnIndex(TvInfoContract.ChannelEntry.COLUMN_YOUTUBE_URL)));
            channel.setCategory(cursor.getString(cursor.getColumnIndex(TvInfoContract.ChannelEntry.COLUMN_CATEGORY)));
        }
        return channel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogoURL() {
        return logoURL;
    }

    public void setLogoURL(String logoURL) {
        this.logoURL = logoURL;
    }

    public String getSiteURL() {
        return siteURL;
    }

    public void setSiteURL(String siteURL) {
        this.siteURL = siteURL;
    }

    public String getStreamURL() {
        return streamURL;
    }

    public void setStreamURL(String streamURL) {
        this.streamURL = streamURL;
    }

    public String getTvURL() {
        return tvURL;
    }

    public void setTvURL(String tvURL) {
        this.tvURL = tvURL;
    }

    public String getYoutubeURL() {
        return youtubeURL;
    }

    public void setYoutubeURL(String youtubeURL) {
        this.youtubeURL = youtubeURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}