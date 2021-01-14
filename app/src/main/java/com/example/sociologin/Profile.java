package com.example.sociologin;

public class Profile {
    public static  String username,useremail,userlocation;
    public static int id;

    public static int getId() {
        return id;
    }

    public static void setId(int id) {
        Profile.id = id;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        Profile.username = username;
    }

    public static String getUseremail() {
        return useremail;
    }

    public static void setUseremail(String useremail) {
        Profile.useremail = useremail;
    }

    public static String getUserlocation() {
        return userlocation;
    }

    public static void setUserlocation(String userlocation) {
        Profile.userlocation = userlocation;
    }
}
