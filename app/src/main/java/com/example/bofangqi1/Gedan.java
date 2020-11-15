package com.example.bofangqi1;

public class Gedan {
    private String name;
    private int GedanId;
    private int GedanSong;

    public Gedan(String name, int GedanId,int GedanSong) {
        this.name = name;
        this.GedanId = GedanId;
        this.GedanSong=GedanSong;
    }

    public String getName() {
        return name;
    }

    public int getGedanId() {
        return GedanId;
    }
    public int getGedanSong() {
        return GedanSong;
    }

}
