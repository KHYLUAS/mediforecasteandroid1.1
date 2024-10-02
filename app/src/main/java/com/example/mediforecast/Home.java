package com.example.mediforecast;

public class Home {
    String rhu, postMessage, postImg, created_by, fileType;

    public Home(String rhu, String created_by, String postMessage, String postImg, String fileType) {
        this.rhu = rhu;
        this.created_by = created_by;
        this.postMessage = postMessage;
        this.postImg = postImg;
        this.fileType = fileType;

    }

    public String getRhu() {
        return rhu;
    }
    public void setRhu(String rhu){
        this.rhu = rhu;
    }
    public String getCreated_by() {
        return created_by;
    }
    public void setCreated_by(String created_by){
        this.created_by = created_by;
    }
    public String getPostMessage() {
        return postMessage;
    }
    public void setPostMessage(String postMessage){
        this.postMessage = postMessage;
    }
    public String getPostImg() {
        return postImg;
    }
    public void setPostImg(String postImg){
        this.postImg = postImg;
    }
    public String getFileType(){
        return fileType;
    }
}
