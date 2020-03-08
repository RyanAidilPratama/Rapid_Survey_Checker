package com.android.id.rapidcheckerpkl59.data.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.id.rapidcheckerpkl59.data.model.Buildings;
import com.android.id.rapidcheckerpkl59.data.model.TeamMember;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class TeamMemberViewModel extends ViewModel {
    private MutableLiveData<ArrayList<TeamMember>> teamMemberList = new MutableLiveData<>();

    public void setTeamListMember(String teamCode) {
        AsyncHttpClient client = new AsyncHttpClient();
        String baseUrl = "";
        client.get(baseUrl, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    final String result = new String(responseBody);
                    Log.e("JSON ", result);
                    JSONObject objectResponse = new JSONObject(result);
                    JSONArray results = objectResponse.getJSONArray("result");
                    ArrayList<TeamMember> teamMember = new ArrayList<>();
                    for (int i = 0; i < results.length(); i++) {
                        ArrayList<Buildings> buildings = new ArrayList<>();
                        JSONArray buildingArray;
                        try {
                            if (results.getJSONObject(i).getJSONArray("bangunan_terakhir").length() <= 0) {
                                Buildings lastBuilding = new Buildings();
                                lastBuilding.setNama("Kosong");
                                lastBuilding.setKode_desa("00000000");
                                lastBuilding.setNoUrutBgn("00");
                                lastBuilding.setLongitude("0");
                                lastBuilding.setLatitude("0");
                                buildings.add(lastBuilding);
                            } else {
                                buildingArray = results.getJSONObject(i).getJSONArray("bangunan_terakhir");
                                for (int j = 0; j < buildingArray.length(); j++) {
                                    JSONObject buildingObj = buildingArray.getJSONObject(j);
                                    Buildings building = new Buildings();
                                    building.setJenis(buildingObj.getString("jenis"));
                                    building.setNoUrutBgn(buildingObj.getString("noUrutBgn"));
                                    building.setNama(buildingObj.getString("nama"));
                                    building.setLatitude(buildingObj.getString("latitude"));
                                    building.setLongitude(buildingObj.getString("longitude"));
                                    building.setKode_desa(buildingObj.getString("kode_desa"));
                                    building.setKeterangan(buildingObj.getString("keterangan"));
                                    building.setTime(buildingObj.getString("time"));
                                    buildings.add(building);
                                }
                            }
                        } catch (JSONException e) {
                            Buildings lastBuilding = new Buildings();
                            lastBuilding.setNama("Kosong");
                            lastBuilding.setKode_desa("00000000");
                            lastBuilding.setNoUrutBgn("00");
                            lastBuilding.setLongitude("0");
                            lastBuilding.setLatitude("0");
                            buildings.add(lastBuilding);
                        }
                        TeamMember member = new TeamMember(
                                Integer.parseInt(results.getJSONObject(i).
                                        getJSONObject("jumlah_bangunan").getString("jumlah")),
                                results.getJSONObject(i).getString("kodeDesa"),
                                results.getJSONObject(i).getString("nim"),
                                results.getJSONObject(i).getString("namaDesa"),
                                buildings,
                                results.getJSONObject(i).getString("namaKabupaten"),
                                results.getJSONObject(i).getString("namaKecamatan"),
                                results.getJSONObject(i).getString("status")
                        );
                        teamMember.add(member);
                    }
                    teamMemberList.postValue(teamMember);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

    }

    public LiveData<ArrayList<TeamMember>> getTeamMemberList() {
        return teamMemberList;
    }
}
