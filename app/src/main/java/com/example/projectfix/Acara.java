package com.example.projectfix;

public class Acara {
    private String kategori, poster, nama, lokasi;
    private int harga;

    public Acara(String kategori, String poster, String nama, String lokasi, int harga) {
        this.kategori = kategori;
        this.poster = poster;
        this.nama = nama;
        this.lokasi = lokasi;
        this.harga = harga;
    }

    public String getKategori() { return kategori; }
    public String getPoster() { return poster; }
    public String getNama() { return nama; }
    public String getLokasi() { return lokasi; }
    public int getHarga() { return harga; }
}