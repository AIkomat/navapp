package com.studentproject.navigationapp;

import static android.app.PendingIntent.getActivity;
import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.internal.PolylineEncoding;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mGoogleMap;
    private Intent intent;
    private GeoApiContext mGeoApiContext = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        intent = getIntent();

        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.mGoogleMap = googleMap;

        LatLng location = new LatLng(intent.getDoubleExtra("lat", 52.231724), intent.getDoubleExtra("lon", 21.006011));
        googleMap.addMarker(new MarkerOptions().position(location).title(intent.getStringExtra("locationName")).icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 13));

        LatLng destination = new LatLng(intent.getDoubleExtra("destinationLat", 52.231724), intent.getDoubleExtra("destinationLon", 21.006011));
        googleMap.addMarker(new MarkerOptions().position(destination).title(intent.getStringExtra("destinationName")).icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

//        LatLng hospital = new LatLng(intent.getDoubleExtra("hospitalLat",0), intent.getDoubleExtra("hospitalLon",0));
//        googleMap.addMarker(new MarkerOptions().position(hospital).title("Szpital").icon(BitmapDescriptorFactory
//                .defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));

        LatLng hospital1 = new LatLng(52.2286946,21.0282758);
        googleMap.addMarker(new MarkerOptions().position(hospital1).title("Szpital Orłowskiego").icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));

        LatLng hospital2 = new LatLng(52.2268815,21.0045954);
        googleMap.addMarker(new MarkerOptions().position(hospital2).title("Szpitalny oddział ratunkowy").icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));

        LatLng hospital3 = new LatLng(52.2395649,20.9685209);
        googleMap.addMarker(new MarkerOptions().position(hospital3).title("Szpital KOUZNZ").icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));

        LatLng hospital4 = new LatLng(52.2389398,20.962805);
        googleMap.addMarker(new MarkerOptions().position(hospital4).title("Hospital emergency ward").icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));

        LatLng hospital5 = new LatLng(52.2268748,20.9559663);
        googleMap.addMarker(new MarkerOptions().position(hospital5).title("Szpitalny Oddział Ratunkowy").icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));

        LatLng hospital6 = new LatLng(2.2533352,20.9365264);
        googleMap.addMarker(new MarkerOptions().position(hospital6).title("Izba Przyjęć WIML").icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));

        LatLng hospital7 = new LatLng(52.2528922,21.0201231);
        googleMap.addMarker(new MarkerOptions().position(hospital7).title("Szpitalny Oddzial Ratunkowy").icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));

        LatLng hospital8 = new LatLng(52.2410494,21.0439408);
        googleMap.addMarker(new MarkerOptions().position(hospital8).title("Szpitalny oddział ratunkowy").icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));

        LatLng hospital10 = new LatLng(52.2037232,20.971785);
        googleMap.addMarker(new MarkerOptions().position(hospital10).title("Szpital Specjalistyczny im. Świętej Rodziny").icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));


        LatLng riot = new LatLng(intent.getDoubleExtra("riotLat", 52.231724) , intent.getDoubleExtra("riotLon", 21.006011));
        googleMap.addMarker(new MarkerOptions().position(riot).title("ZAMIESZKI"));

        LatLng carCrash = new LatLng(intent.getDoubleExtra("carCrashLat", 52.231724), intent.getDoubleExtra("carCrashLon", 21.006011));
        googleMap.addMarker(new MarkerOptions().position(carCrash).title("WYPADEK"));



        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);

        mGeoApiContext = new GeoApiContext.Builder().apiKey("AIzaSyCsKjGK0BrhEJt9_jbzNxt8dTmfKOeY0rk").build();
        calculateDirectionsToDestination();
//        calculateDirectionsToHospital();
    }

    private void calculateDirectionsToDestination() {
        com.google.maps.model.LatLng destination = new com.google.maps.model.LatLng(
                intent.getDoubleExtra("destinationLat", 52.231724),
                intent.getDoubleExtra("destinationLon", 21.006011)
        );
        DirectionsApiRequest directions = new DirectionsApiRequest(mGeoApiContext);
        directions.alternatives(false);
        directions.origin(
                new com.google.maps.model.LatLng(
                        intent.getDoubleExtra("lat", 52.231724),
                        intent.getDoubleExtra("lon", 21.006011)
                )
        );
        Log.d(TAG, "calculateDirections: destination: " + destination.toString());
        directions.destination(destination).setCallback(new PendingResult.Callback<DirectionsResult>() {
            @Override
            public void onResult(DirectionsResult result) {
                addPolylinesToMap(result);
            }

            @Override
            public void onFailure(Throwable e) {
                Log.e(TAG, "calculateDirections: Failed to get directions to destination: " + e.getMessage());

            }
        });

    }

    private void calculateDirectionsToHospital() {
        com.google.maps.model.LatLng destination = new com.google.maps.model.LatLng(
                intent.getDoubleExtra("hospitalLat", 52.231724),
                intent.getDoubleExtra("hospitalLon", 21.006011)
        );
        DirectionsApiRequest directions = new DirectionsApiRequest(mGeoApiContext);
        directions.alternatives(false);
        directions.origin(
                new com.google.maps.model.LatLng(
                        intent.getDoubleExtra("destinationLat", 52.231724),
                        intent.getDoubleExtra("destinationLon", 21.006011)
                )
        );
        directions.destination(destination).setCallback(new PendingResult.Callback<DirectionsResult>() {
            @Override
            public void onResult(DirectionsResult result) {
                addPolylinesToMap(result);
            }

            @Override
            public void onFailure(Throwable e) {
                Log.e(TAG, "calculateDirections: Failed to get directions to hospital:: " + e.getMessage());

            }
        });
    }

    private void addPolylinesToMap(final DirectionsResult result){
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: result routes: " + result.routes.length);

                for(DirectionsRoute route: result.routes){
                    Log.d(TAG, "run: leg: " + route.legs[0].toString());
                    List<com.google.maps.model.LatLng> decodedPath = PolylineEncoding.decode(route.overviewPolyline.getEncodedPath());
                    List<LatLng> newDecodedPath = new ArrayList<>();

                    for(com.google.maps.model.LatLng latLng: decodedPath){
                        newDecodedPath.add(new LatLng(
                                latLng.lat,
                                latLng.lng
                        ));
                    }
                    Polyline polyline = mGoogleMap.addPolyline(new PolylineOptions().addAll(newDecodedPath));
                    polyline.setColor(ContextCompat.getColor(MapActivity.this, R.color.blue));
                    polyline.setClickable(true);

                }
            }
        });
    }
}