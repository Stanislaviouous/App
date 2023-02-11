package com.example.toolbarapp.ui.home.news;

import java.util.ArrayList;

public class NewsStencil {
    public NewsStencil(){

    }
    public NewsStencil(String id, String ownerId, String groupPhotoUrl, String groupName, String date, String text, ArrayList<String> mediaUrl, ArrayList<String> videos, String link){
        this.id = id;
        this.ownerId = ownerId;
        this.groupPhotoUrl = groupPhotoUrl;
        this.groupName = groupName;
        this.date = date;
        this.text = text;
        this.mediaUrl = mediaUrl;
        this.link = link;
        this.videos = videos;
    }
    public String id,
            ownerId,
            groupPhotoUrl,
            groupName,
            date,
            text;
    public ArrayList<String> mediaUrl;
    public ArrayList<String> videos;
    public String link;

    public String getId() {
        return id;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public String getGroupPhotoUrl() {
        return groupPhotoUrl;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getDate() {
        return date;
    }

    public String getText() {
        return text;
    }

    public ArrayList<String> getMediaUrl() {
        return mediaUrl;
    }

    public ArrayList<String> getVideos() {
        return videos;
    }

    public String getLink() {
        return link;
    }

}
