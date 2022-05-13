package com.example.bookedroom;

public class DateFind {
    String roomName;
    int yesterday;
    int today;
    int nextday;

    public String getRoomName() {
        return roomName;
    }

    public int getYesterday() {
        return yesterday;
    }

    public int getToday() {
        return today;
    }

    public int getNextday() {
        return nextday;
    }

    public DateFind(String roomName, int yesterday, int today, int nextday) {
        this.roomName = roomName;
        this.yesterday = yesterday;
        this.today = today;
        this.nextday = nextday;
    }
}
