package com.example.projectfix;

public class Event {
    public String judul, lokasi,harga, deskripsi,imageUrl;
    public boolean isSoldOut;

    public Event(String g, String j, String l, String h, String d, boolean isSoldOut) {
        this.judul = j;
        this.imageUrl = g;
        this.lokasi = l;
        this.harga = h;
        this.deskripsi = d;
        this.isSoldOut = isSoldOut;
    }
}
