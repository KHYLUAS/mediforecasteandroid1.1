package com.example.mediforecast;

public class Home {
    String rhu, postMessage, postImg, created_by;

    public Home(String rhu, String postMessage, String postImg, String created_by) {
        this.rhu = rhu;
        this.postMessage = postMessage;
        this.postImg = postImg;
        this.created_by = created_by;
    }

    public String getRhu() {
        return rhu;
    }

    public String getPostMessage() {
        return postMessage;
    }

    public String getPostImg() {
        return postImg;
    }

    public String getCreated_by() {
        return created_by;
    }
}
