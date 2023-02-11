package com.example.toolbarapp.ui.home;

import java.util.ArrayList;

public class RecyclerNewsItem implements Comparable<RecyclerNewsItem> {
    private String id,
            ownerId,
            groupPhotoUrl,
            groupName,
            text;
    private long date;
    private ArrayList<String> mediaUrl;
    private ArrayList<String> videos;
    private String link;

    public RecyclerNewsItem() {
    }

    public RecyclerNewsItem(String id, String ownerId, String groupPhotoUrl, String groupName, long date, String text, ArrayList<String> mediaUrl, ArrayList<String> videos, String link) {
        this.id = id;
        this.ownerId = ownerId;
        this.groupPhotoUrl = groupPhotoUrl;
        this.groupName = groupName;
        this.date = date;
        this.text = text;
        this.mediaUrl = mediaUrl;
        this.videos = videos;
        this.link = link;
    }
    public RecyclerNewsItem(String id, String ownerId, String groupPhotoUrl, String groupName, long date, String text, ArrayList<String> mediaUrl, ArrayList<String> videos) {
        this.id = id;
        this.ownerId = ownerId;
        this.groupPhotoUrl = groupPhotoUrl;
        this.groupName = groupName;
        this.date = date;
        this.text = text;
        this.mediaUrl = mediaUrl;
        this.videos = videos;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public void setGroupPhotoUrl(String groupPhotoUrl) {
        this.groupPhotoUrl = groupPhotoUrl;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setMediaUrl(ArrayList<String> mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public void setVideos(ArrayList<String> videos) {
        this.videos = videos;
    }

    public void setLink(String link) {
        this.link = link;
    }

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

    public long getDate() {
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

    @Override
    public int compareTo(RecyclerNewsItem o) {
        return Long.compare(Long.parseLong(String.valueOf(this.getDate())), Long.parseLong(String.valueOf(o.getDate())));
    }
}
