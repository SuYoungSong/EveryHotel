package com.example.bookedroom;

public class LoginData {
    private static String memberType;
    private static String id;

    public LoginData() {
    }

    public LoginData(String memberType, String id) {
        this.memberType = memberType;
        this.id = id;
    }

    public String getMemberType() {
        return memberType;
    }

    public String getId() {
        return id;
    }
}
