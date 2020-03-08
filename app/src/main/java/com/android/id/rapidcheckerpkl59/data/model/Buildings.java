package com.android.id.rapidcheckerpkl59.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Buildings implements Parcelable {
    private String keterangan;
    private String nama;
    private String noUrutBgn;
    private String kode_desa;
    private String latitude;
    private String jenis;
    private String time;
    private String longitude;

    public Buildings() {

    }

    protected Buildings(Parcel in) {
        keterangan = in.readString();
        nama = in.readString();
        noUrutBgn = in.readString();
        kode_desa = in.readString();
        latitude = in.readString();
        jenis = in.readString();
        time = in.readString();
        longitude = in.readString();
    }

    public static final Creator<Buildings> CREATOR = new Creator<Buildings>() {
        @Override
        public Buildings createFromParcel(Parcel in) {
            return new Buildings(in);
        }

        @Override
        public Buildings[] newArray(int size) {
            return new Buildings[size];
        }
    };

    public String getKeterangan() {
        return this.keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getNama() {
        return this.nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNoUrutBgn() {
        return this.noUrutBgn;
    }

    public void setNoUrutBgn(String noUrutBgn) {
        this.noUrutBgn = noUrutBgn;
    }

    public String getKode_desa() {
        return this.kode_desa;
    }

    public void setKode_desa(String kode_desa) {
        this.kode_desa = kode_desa;
    }

    public String getLatitude() {
        return this.latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getJenis() {
        return this.jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLongitude() {
        return this.longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(keterangan);
        parcel.writeString(nama);
        parcel.writeString(noUrutBgn);
        parcel.writeString(kode_desa);
        parcel.writeString(latitude);
        parcel.writeString(jenis);
        parcel.writeString(time);
        parcel.writeString(longitude);
    }
}
