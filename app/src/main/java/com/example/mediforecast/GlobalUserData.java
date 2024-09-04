package com.example.mediforecast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class GlobalUserData {
    private static String name;
    private static String email;
    private static String contact;
    private static String location;
    private static String username;
    private static String birthday;
    private static String gender;


    // Getter and Setter for name
    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        GlobalUserData.name = name;
    }
    public static String getGender() {
        return gender;
    }

    public static void setGender(String gender) {
        GlobalUserData.gender = gender;
    }

    public static String getBirthday() {
        return formatBirthday(birthday);
    }

    public static void setBirthday(String birthday) {
        GlobalUserData.birthday = birthday;
    }

    private static String formatBirthday(String birthday) {
        SimpleDateFormat originalFormat = new SimpleDateFormat("d/M/yyyy", Locale.getDefault());
        SimpleDateFormat targetFormat = new SimpleDateFormat("MMMM d, yyyy", Locale.getDefault());
        try {
            Date date = originalFormat.parse(birthday);
            return targetFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return birthday; // Return the original birthday if parsing fails
        }
    }

    public static String getUsername() {
        return username;
    }
    public static void setUsername(String username) {
        GlobalUserData.username = username;
    }


    // Getter and Setter for email
    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        GlobalUserData.email = email;
    }

    // Getter and Setter for contact
    public static String getContact() {
        return contact;
    }

    public static void setContact(String contact) {
        GlobalUserData.contact = contact;
    }

    // Getter and Setter for location
    public static String getLocation() {
        return location;
    }

    public static void setLocation(String location) {
        GlobalUserData.location = location;
    }
}
