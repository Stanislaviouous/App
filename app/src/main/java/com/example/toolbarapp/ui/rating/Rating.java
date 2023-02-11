package com.example.toolbarapp.ui.rating;

public class Rating implements Comparable<Rating>{
    public void setNumber(String number) {
        this.number = number;
    }

    private String number;
    private String
            login,
            count,
            imageUri;

    public Rating(String number, String login, String count, String imageUri) {
        this.number = number;
        this.login = login;
        this.count = count;
        this.imageUri = imageUri;
    }

    public String getNumber() {
        return number;
    }

    public String getLogin() {
        return login;
    }

    public String getCount() {
        return count;
    }

    public String getImageUri() {
        return imageUri;
    }

    @Override
    public int compareTo(Rating o) {
        return Long.compare(Long.parseLong(this.count), Long.parseLong(o.count));
    }
}
