package com.tishinanton.mad2016assignment3.Activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.github.atishin.helpers.Helpers;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mikepenz.iconics.typeface.FontAwesome;
import com.tishinanton.mad2016assignment3.DAL.Place;
import com.tishinanton.mad2016assignment3.DAL.PlacesRepository;
import com.tishinanton.mad2016assignment3.Fragments.MarkerDetailsFragment;
import com.tishinanton.mad2016assignment3.Listeners.OnPlaceAddedListener;
import com.tishinanton.mad2016assignment3.R;

public class MapActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        OnPlaceAddedListener
{

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Marker locationMarker;
    private Location currentLocation;

    FloatingActionButton addPlaceButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.tishinanton.mad2016assignment3.R.layout.activity_map);

        addPlaceButton = (FloatingActionButton)findViewById(R.id.addPlaceFloatingButton);
        addPlaceButton.setImageDrawable(Helpers.getFontAwesomeIcon(this, FontAwesome.Icon.faw_map_marker, Color.WHITE));

        addPlaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddPlaceButtonClicked();
            }
        });

        SupportMapFragment mapFragment = new SupportMapFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.map_frame,mapFragment, "map_fragment");
        fragmentTransaction.commit();
        mapFragment.getMapAsync(this);
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

    }

    private void onAddPlaceButtonClicked() {
        if (currentLocation == null) {
            Toast.makeText(this, "Unable to determine current location", Toast.LENGTH_SHORT).show();
            return;
        }
        BottomSheetDialogFragment bottomSheetDialogFragment = new MarkerDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putDouble("Lat", currentLocation.getLatitude());
        bundle.putDouble("Lng", currentLocation.getLongitude());
        bottomSheetDialogFragment.setArguments(bundle);
        bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng almaty = new LatLng(43.251010, 76.895106);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(almaty, 12.0f));

        PlacesRepository repository = new PlacesRepository(this);
        for (Place place : repository.getAll()) {
            mMap.addMarker(new MarkerOptions().position(new LatLng(place.Lat, place.Lng)).title(place.Title));
        }

        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        onConnected(null);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
            return;
        }

        LocationRequest mLocationRequest = new LocationRequest();
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
        LatLng locationCoordinates = new LatLng(location.getLatitude(), location.getLongitude());
//        if (locationMarker == null) {
//            MarkerOptions options = new MarkerOptions()
//                    .position(locationCoordinates)
//                    .title("Current location")
//                    .snippet("This is a marker pointing to current location");
//
//            locationMarker = mMap.addMarker(options);
//        } else {
//            locationMarker.setPosition(locationCoordinates);
//        }

    }

    @Override
    public void onPlaceAdded(Place place) {
        mMap.addMarker(new MarkerOptions().position(place.getLatLng()).title(place.Title));
    }
}
