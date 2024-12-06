package com.example.mediforecast;

public class History {
    private String date;
    private String painLocation;
    private String documentId;

    public History(String date, String painLocation, String documentId){
        this.date = date;
        this.painLocation = painLocation;
        this.documentId = documentId;
    }
    public String getDate() {
        return date;
    }

    public String getPainLocation() {
        return painLocation;
    }

    public String getDocumentId() {
        return documentId;
    }
}
