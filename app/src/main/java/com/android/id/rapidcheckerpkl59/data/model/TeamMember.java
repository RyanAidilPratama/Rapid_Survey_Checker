package com.android.id.rapidcheckerpkl59.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class TeamMember implements Parcelable {
    private int jumlah_bangunan;
    private String kodeDesa;
    private String nim;
    private String namaDesa;
    private ArrayList<Buildings> buildings_terakhir;
    private String namaKabupaten;
    private String namaKecamatan;
    private String status;

    public TeamMember(int jumlah_bangunan, String kodeDesa, String nim, String namaDesa, ArrayList<Buildings> buildings_terakhir, String namaKabupaten, String namaKecamatan, String status) {
        this.jumlah_bangunan = jumlah_bangunan;
        this.kodeDesa = kodeDesa;
        this.nim = nim;
        this.namaDesa = namaDesa;
        this.buildings_terakhir = buildings_terakhir;
        this.namaKabupaten = namaKabupaten;
        this.namaKecamatan = namaKecamatan;
        this.status = status;
    }

    protected TeamMember(Parcel in) {
        jumlah_bangunan = in.readInt();
        kodeDesa = in.readString();
        nim = in.readString();
        namaDesa = in.readString();
        buildings_terakhir = in.readArrayList(Buildings.class.getClassLoader());
        namaKabupaten = in.readString();
        namaKecamatan = in.readString();
        status = in.readString();
    }

    public static final Creator<TeamMember> CREATOR = new Creator<TeamMember>() {
        @Override
        public TeamMember createFromParcel(Parcel in) {
            return new TeamMember(in);
        }

        @Override
        public TeamMember[] newArray(int size) {
            return new TeamMember[size];
        }
    };

    public int getJumlah_bangunan() {
        return jumlah_bangunan;
    }

    public String getKodeDesa() {
        return kodeDesa;
    }

    public String getNim() {
        return nim;
    }

    public String getNamaDesa() {
        return namaDesa;
    }

    public ArrayList<Buildings> getBuildings_terakhir() {
        return buildings_terakhir;
    }

    public String getNamaKabupaten() {
        return namaKabupaten;
    }

    public String getNamaKecamatan() {
        return namaKecamatan;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(jumlah_bangunan);
        parcel.writeString(kodeDesa);
        parcel.writeString(nim);
        parcel.writeString(namaDesa);
        parcel.writeList(buildings_terakhir);
        parcel.writeString(namaKabupaten);
        parcel.writeString(namaKecamatan);
        parcel.writeString(status);
    }
}
