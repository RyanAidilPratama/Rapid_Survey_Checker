package com.android.id.rapidcheckerpkl59.activities;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import com.android.id.rapidcheckerpkl59.R;
import com.android.id.rapidcheckerpkl59.data.helper.StaticFinal;
import com.android.id.rapidcheckerpkl59.data.model.Buildings;
import com.android.id.rapidcheckerpkl59.data.model.TeamMember;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;


public class DetailsActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final String EXTRA_MEMBER = "extra_anggota";

    GoogleMap gMap;
    Polygon polygons = null;
    Polyline mainPolyline = null, grayPolyline = null;
    List<LatLng> latLngs = new ArrayList<>();
    List<LatLng> listLatLng = new ArrayList<>();
    ArrayList<Buildings> buildings = new ArrayList<>();
    ArrayList<Marker> markers = new ArrayList<>();
    TeamMember teamMember;

    TextView nim, status, villageName, villageCode, districtName, cityName, sum, sumBTTBC;
    MaterialCheckBox checkPolygon, checkMarker, checkBC, checkBTT;
    ImageView imgProfile;
    AppCompatImageButton btnPrev, btnNext;
    ProgressBar loadingProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        teamMember = getIntent().getParcelableExtra(EXTRA_MEMBER);
        getAllViewComponents();
        bind(teamMember);
        checkMarker();
        checkPolyLine();
        setCheckBC();
        setCheckBTT();
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.detail_google_maps);
        supportMapFragment.getMapAsync(this);
    }

    private void bind(TeamMember teamMember) {
        String baseUrl = String.format(getString(R.string.base_url_image), teamMember.getNim());
        Glide.with(this)
                .load(baseUrl)
                .into(imgProfile);
        nim.setText(teamMember.getNim());
        if (teamMember.getStatus().equalsIgnoreCase("Listing")) {
            setStatus(teamMember.getStatus());
        } else if (teamMember.getStatus().equalsIgnoreCase("Ready")) {
            setStatus("Final");
        } else {
            setStatus("Sampel Diambil");
        }
        villageName.setText(teamMember.getNamaDesa());
        villageCode.setText(teamMember.getKodeDesa());
        cityName.setText(teamMember.getNamaKabupaten());
        districtName.setText(teamMember.getNamaKecamatan());
        sum.setText(String.valueOf(teamMember.getJumlah_bangunan()));
        buildings.addAll(teamMember.getBuildings_terakhir());
        int sumBTT = 0;
        int sumBC = 0;
        for (int i = 0; i < buildings.size(); i++) {
            double lat = Double.parseDouble(buildings.get(i).getLatitude());
            double lng = Double.parseDouble(buildings.get(i).getLongitude());
            if (buildings.get(i).getJenis().equalsIgnoreCase("BTT")) {
                sumBTT++;
            } else {
                sumBC++;
            }
            LatLng latLng = new LatLng(lat, lng);
            latLngs.add(latLng);
        }
        String sumBTBC = "(" + sumBC + " BC, " + sumBTT + " BTT)";
        sumBTTBC.setText(sumBTBC);
        setNext();
        setPrev();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        getPolygonMap(gMap);
        writeMarker();
    }

    void writeMarker() {
        if (!markers.isEmpty()) {
            removeMarker();
            markers.clear();
        }
        String title;
        title = buildings.get(0).getNoUrutBgn() + " - " + buildings.get(0).getNama();
        Marker marker1;
        if (buildings.get(0).getJenis().equalsIgnoreCase("BTT")) {
            marker1 = gMap.addMarker(new MarkerOptions()
                    .position(latLngs.get(0))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                    .title(title));
            marker1.showInfoWindow();
        } else {
            marker1 = gMap.addMarker(new MarkerOptions().position(latLngs.get(0)).title(title));
            marker1.showInfoWindow();
        }
        markers.add(marker1);
        title = buildings.get(latLngs.size() - 1).getNoUrutBgn() + " - " + buildings.get(latLngs.size() - 1).getNama();
        Marker marker2;
        if (buildings.get(latLngs.size() - 1).getJenis().equalsIgnoreCase("BTT")) {
            marker2 = gMap.addMarker(new MarkerOptions()
                    .position(latLngs.get(latLngs.size() - 1))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                    .title(title));
        } else {
            marker2 = gMap.addMarker(new MarkerOptions().position(latLngs.get(latLngs.size() - 1)).title(title));
        }
        markers.add(marker2);
        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngs.get(0), 18), 2000, null);
    }

    void writeAllMarker() {
        if (!markers.isEmpty()) {
            removeMarker();
            markers.clear();
        }
        String title;
        for (int i = 0; i < buildings.size(); i++) {
            title = buildings.get(i).getNoUrutBgn() + " - " + buildings.get(i).getNama();
            Marker marker;
            if (buildings.get(i).getJenis().equalsIgnoreCase("BTT")) {
                marker = gMap.addMarker(new MarkerOptions()
                        .position(latLngs.get(i))
                        .title(title)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                );
            } else {
                marker = gMap.addMarker(new MarkerOptions()
                        .position(latLngs.get(i))
                        .title(title)
                );
            }
            if (i == 0) {
                marker.showInfoWindow();
            }
            markers.add(marker);
        }
        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(markers.get(0).getPosition(), 18), 2000, null);
    }

    void writeAllBcMarkers() {
        if (!markers.isEmpty()) {
            removeMarker();
            markers.clear();
        }
        int first = 0;
        int firstIdx = 0;
        String title;
        for (int i = 0; i < buildings.size(); i++) {
            title = buildings.get(i).getNoUrutBgn() + " - " + buildings.get(i).getNama();
            Marker marker = gMap.addMarker(new MarkerOptions()
                    .position(latLngs.get(i))
                    .title(title));
            if (buildings.get(i).getJenis().equalsIgnoreCase("BC")) {
                if (first < 1) {
                    first++;
                    firstIdx = i;
                }
            } else {
                marker.setVisible(false);
            }
            markers.add(marker);
        }
        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(markers.get(firstIdx).getPosition(), 18), 2000, null);
        markers.get(firstIdx).showInfoWindow();
    }

    void writeAllBttMarkers() {
        if (!markers.isEmpty()) {
            removeMarker();
            markers.clear();
        }
        int first = 0;
        int firstIdx = 0;
        String title;
        for (int i = 0; i < buildings.size(); i++) {
            title = buildings.get(i).getNoUrutBgn() + " - " + buildings.get(i).getNama();
            Marker marker = gMap.addMarker(new MarkerOptions()
                    .position(latLngs.get(i))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                    .title(title));
            if (buildings.get(i).getJenis().equalsIgnoreCase("BTT")) {
                if (first < 1) {
                    first++;
                    firstIdx = i;
                }
            } else {
                marker.setVisible(false);
            }
            markers.add(marker);
        }
        markers.get(firstIdx).showInfoWindow();
        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(markers.get(firstIdx).getPosition(), 18), 2000, null);
    }

    void removeMarker() {
        for (int i = 0; i < markers.size(); i++) {
            markers.get(i).setVisible(false);
        }
        markers.get(0).showInfoWindow();
    }

    void getPolygonMap(GoogleMap gMap) {
        AsyncHttpClient client = new AsyncHttpClient();
        String BASE_URL_POLYGON = StaticFinal.BASE_URL_POLYGON;
        client.get(BASE_URL_POLYGON, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                try {
                    PolygonOptions polygonCluster = new PolygonOptions();
                    JSONObject object = new JSONObject(result);
                    JSONArray villages = object.getJSONArray("features");
                    for (int i = 0; i < villages.length(); i++) {
                        String villageName = villages.getJSONObject(i).getJSONObject("properties").getString("NAMOBJ");
                        String districtName = villages.getJSONObject(i).getJSONObject("properties").getString("Kecamatan");
                        if (villageName.equalsIgnoreCase(teamMember.getNamaDesa()) && districtName.equalsIgnoreCase(teamMember.getNamaKecamatan())) {
                            if (polygons != null) polygons.remove();
                            for (int j = 0; j < villages.getJSONObject(i).getJSONObject("geometry").getJSONArray("coordinates").getJSONArray(0).getJSONArray(0).length(); j++) {
                                double lat = (double) villages.getJSONObject(i).getJSONObject("geometry").getJSONArray("coordinates").getJSONArray(0).getJSONArray(0).getJSONArray(j).get(1);
                                double lng = (double) villages.getJSONObject(i).getJSONObject("geometry").getJSONArray("coordinates").getJSONArray(0).getJSONArray(0).getJSONArray(j).get(0);
                                LatLng latLng = new LatLng(lat, lng);
                                polygonCluster.add(latLng);
                            }
                        }
                    }
                    if (!polygonCluster.getPoints().isEmpty()) {
                        polygons = gMap.addPolygon(polygonCluster);
                        polygons.setStrokeColor(Color.rgb(255, 0, 0));
                    }
                    loadingProgressBar.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                super.onProgress(bytesWritten, totalSize);
                loadingProgressBar.setVisibility(View.VISIBLE);
                loadingProgressBar.setProgress((int) (bytesWritten / totalSize));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                loadingProgressBar.setVisibility(View.GONE);
            }
        });
    }

    void setStatus(String tvStatus) {
        String text = "(" + tvStatus.toUpperCase() + ")";
        status.setText(text);
    }

    void getAllViewComponents() {
        imgProfile = findViewById(R.id.detail_prof_pict);
        sumBTTBC = findViewById(R.id.detail_total_building_bttbc);
        btnNext = findViewById(R.id.detail_btn_next_buildings);
        btnPrev = findViewById(R.id.detail_btn_prev_buildings);
        nim = findViewById(R.id.detail_nim);
        status = findViewById(R.id.detail_village_data);
        villageName = findViewById(R.id.detail_village_name);
        villageCode = findViewById(R.id.detail_village_code);
        districtName = findViewById(R.id.detail_name_district);
        cityName = findViewById(R.id.detail_name_city);
        sum = findViewById(R.id.detail_total_building);
        checkMarker = findViewById(R.id.detail_check_show_marker);
        checkPolygon = findViewById(R.id.detail_check_show_polygon);
        checkBC = findViewById(R.id.detail_bc_check);
        checkBTT = findViewById(R.id.detail_btt_check);
        loadingProgressBar = findViewById(R.id.detail_content_progress);
    }

    void checkPolyLine() {
        checkPolygon.setOnCheckedChangeListener((compoundButton, b) -> {
            if (compoundButton.isChecked()) {
//                ArrayList<LatLng> points = null;
                PolylineOptions mainOptions = new PolylineOptions();
//                for (int i = 0; i < latLngs.size(); i++) {
//                    points = new ArrayList<>();
//                    mainOptions = new PolylineOptions();
//                    for (int j = 0; j < latLngs.size(); j++) {
//                        LatLng pos = new LatLng(latLngs.get(j).latitude, latLngs.get(j).longitude);
//                        points.add(pos);
//                    }
//                    this.listLatLng.addAll(points);
//                }

                mainOptions.color(getResources().getColor(R.color.colorPrimary));
                mainOptions.startCap(new SquareCap());
                mainOptions.endCap(new SquareCap());
                mainOptions.jointType(JointType.ROUND);
                mainOptions.addAll(latLngs);
                mainPolyline = gMap.addPolyline(mainOptions);

//                PolylineOptions grayOptions = new PolylineOptions();
//                grayOptions.width(10);
//                grayOptions.color(Color.GRAY);
//                grayOptions.startCap(new SquareCap());
//                grayOptions.endCap(new SquareCap());
//                grayOptions.jointType(JointType.ROUND);
//                grayOptions.addAll(latLngs);
//                grayPolyline = gMap.addPolyline(grayOptions);
//                animatePolyline();
//                if (mainPolyline != null) {
//                    mainPolyline.remove();
//                    grayPolyline.remove();
//                }
            } else {
                mainPolyline.remove();
//                grayPolyline.remove();
            }
        });
    }

    void checkMarker() {
        checkMarker.setOnCheckedChangeListener((compoundButton, b) -> {
            if (compoundButton.isChecked()) {
                if (checkBC.isChecked()) {
                    checkBC.setChecked(false);
                    removeMarker();
                } else if (checkBTT.isChecked()) {
                    checkBTT.setChecked(false);
                    removeMarker();
                }
                if (!markers.isEmpty()) {
                    removeMarker();
                }
                writeAllMarker();
            } else {
                removeMarker();
                if (checkBC.isChecked()) {
                    writeAllBcMarkers();
                } else if (checkBTT.isChecked()) {
                    writeAllBttMarkers();
                } else {
                    writeMarker();
                }
            }
        });
    }

    void setCheckBC() {
        checkBC.setOnCheckedChangeListener((compoundButton, b) -> {
            if (compoundButton.isChecked()) {
                if (checkBTT.isChecked()) {
                    checkBTT.setChecked(false);
                    removeMarker();
                } else if (checkMarker.isChecked()) {
                    checkMarker.setChecked(false);
                    removeMarker();
                }

                if (!markers.isEmpty()) {
                    removeMarker();
                }
                writeAllBcMarkers();
            } else {
                removeMarker();
                if (checkMarker.isChecked()) {
                    writeAllMarker();
                } else if (checkBTT.isChecked()) {
                    writeAllBttMarkers();
                } else {
                    writeMarker();
                }

            }
        });
    }

    void setCheckBTT() {
        checkBTT.setOnCheckedChangeListener((compoundButton, b) -> {
            if (compoundButton.isChecked()) {
                if (checkBC.isChecked()) {
                    checkBC.setChecked(false);
                    removeMarker();
                } else if (checkMarker.isChecked()) {
                    checkMarker.setChecked(false);
                    removeMarker();
                }
                if (!markers.isEmpty()) {
                    markers.clear();
                }
                writeAllBttMarkers();
            } else {
                removeMarker();
                if (checkMarker.isChecked()) {
                    writeAllMarker();
                } else if (checkBC.isChecked()) {
                    writeAllBcMarkers();
                } else {
                    writeMarker();
                }
            }
        });
    }

    void setNext() {
        btnNext.setOnClickListener(view -> {
            boolean skip = false;
            for (int i = 0; i < markers.size(); i++) {
                Log.d("Marker ke-" + (i + 1), "Visibility - " + markers.get(i).isVisible());
                if (markers.get(i).isVisible()) {
                    if (markers.get(i).isInfoWindowShown()) {
                        int idx = i + 1;
                        if (idx == markers.size()) {
                            idx = 0;
                        }
                        if (markers.get(idx).isVisible()) {
                            markers.get(idx).showInfoWindow();
                            gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(markers.get(idx).getPosition(), 18), 2000, null);
                        } else {
                            skip = true;
                            continue;
                        }
                        break;
                    } else if (skip) {
                        markers.get(i).showInfoWindow();
                        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(markers.get(i).getPosition(), 18), 2000, null);
                        break;
                    }
                }
            }
        });
    }

    void setPrev() {
        btnPrev.setOnClickListener(view -> {
            boolean skip = false;
            for (int i = markers.size() - 1; i >= 0; i--) {
                if (markers.get(i).isVisible()) {
                    if (markers.get(i).isInfoWindowShown()) {
                        int idx = i - 1;
                        if (idx == -1) {
                            idx = markers.size() - 1;
                        }
                        if (markers.get(idx).isVisible()) {
                            markers.get(idx).showInfoWindow();
                            gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(markers.get(idx).getPosition(), 18), 2000, null);
                        } else {
                            skip = true;
                            continue;
                        }
                        break;
                    } else if (skip) {
                        markers.get(i).showInfoWindow();
                        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(markers.get(i).getPosition(), 18), 2000, null);
                        break;
                    }
                }
            }
        });
    }

    void animatePolyline() {
        ValueAnimator animator = ValueAnimator.ofInt(0, 100);
        animator.setDuration(1000);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(valueAnimator -> {
            List<LatLng> latLngList = mainPolyline.getPoints();
            int initialPointSize = latLngList.size();
            int animatedValue = (int) animator.getAnimatedValue();
            int newPoints = (animatedValue * latLngList.size()) / 100;
            if (initialPointSize < newPoints) {
                latLngList.addAll(listLatLng.subList(initialPointSize, newPoints));
                mainPolyline.setPoints(latLngList);
            }
        });
        animator.addListener(polyLineAnimatorListener);
        animator.start();
    }

    Animator.AnimatorListener polyLineAnimatorListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animator) {

        }

        @Override
        public void onAnimationEnd(Animator animator) {
            List<LatLng> mainLatng = mainPolyline.getPoints();
            List<LatLng> grayLatLngs = grayPolyline.getPoints();

            grayLatLngs.clear();
            grayLatLngs.addAll(mainLatng);
            mainLatng.clear();

            mainPolyline.setPoints(mainLatng);
            grayPolyline.setPoints(grayLatLngs);

            mainPolyline.setZIndex(2);
            animator.start();
        }

        @Override
        public void onAnimationCancel(Animator animator) {

        }

        @Override
        public void onAnimationRepeat(Animator animator) {

        }
    };
}
