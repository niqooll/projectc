package com.example.projectfix;


public class ItemRiwayat {
    public String id;
    public String namaKonser;
    public String tanggal;
    public String jam;
    public String harga;
    public String image;


    public ItemRiwayat() {

    }
    public ItemRiwayat(String namaKonser, String tanggal, String jam, String harga, String image) {
        this.namaKonser = namaKonser;
        this.tanggal = tanggal;
        this.jam = jam;
        this.harga = harga;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNamaKonser() {
        return namaKonser;
    }

    public void setNamaKonser(String namaKonser) {
        this.namaKonser = namaKonser;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getJam() {
        return jam;
    }

    public void setJam(String jam) {
        this.jam = jam;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

