package com.example.user.rinventory.data;

/**
 * Created by ASUS on 6/4/2018.
 */

public class Data {
    private String id_kategori, nama_kategori;

    public Data() {
    }

    public Data(String id_kategori, String nama_kategori) {
        this.id_kategori = id_kategori;
        this.nama_kategori = nama_kategori;
    }

    public String getId() {
        return id_kategori;
    }

    public void setId(String id_kategori) {
        this.id_kategori = id_kategori;
    }

    public String getPendidikan() {
        return nama_kategori;
    }

    public void setPendidikan(String nama_kategori) {
        this.nama_kategori = nama_kategori;
    }

}