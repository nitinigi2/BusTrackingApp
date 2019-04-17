package com.example.nitin.testapp;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    FirebaseDatabase database;
    DatabaseReference myRef;
    String busNumber = "";
    int time;
    private Marker currentBusMarker;

    private static final int Request_User_Location_Code = 99;
    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Location lastLocation;
    private Marker currentUserLocationMarker;
    private LatLng latLng;

    private Polyline currentPolyline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            checkUserLocationPermission();
        }



        Intent intent = getIntent();

        busNumber = intent.getStringExtra("busNumber");
        Log.d("Inside maps Act busNo", busNumber);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showdata(dataSnapshot, busNumber, time);
                Log.d("showdata is called", " ");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(22.7196, 75.8577);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            buildApiClient();
            mMap.setMyLocationEnabled(true);
        }
    }


    UserInfo userInfo = new UserInfo();
    double latitude, longitude;

    private void showdata(DataSnapshot dataSnapshot, final String busNumber, int time) {
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            UserInfo userinfo = new UserInfo();
            userinfo.setLatitude(ds.child(busNumber).getValue(UserInfo.class).getLatitude());
            userinfo.setLongitude(ds.child(busNumber).getValue(UserInfo.class).getLongitude());

            latitude = userinfo.getLatitude();
            longitude = userinfo.getLongitude();
        }

        userInfo.setLatitude(latitude);
        userInfo.setLongitude(longitude);
        Log.d("UserInfo getLat", ""+userInfo.getLatitude());

        if(currentBusMarker != null) currentBusMarker.remove();
        LatLng latLng = new LatLng(latitude, longitude);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(busNumber);
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_busliveloc));
        currentBusMarker = mMap.addMarker(markerOptions);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)
                .zoom(13)
                .bearing(0)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        String currentAddress="";
        if(Geocoder.isPresent()){
            List<Address> addresses;
            try {

                Geocoder gc = new Geocoder(this);
                Log.d("Inside Geocoder", ""+gc);
                //    for(int i=0;i<routeList.length;i++) {
                Log.d("Inside loop", "inside loop");

                // addresses = gc.getFromLocationName("vijay nagar"+", indore", 1);
                addresses = gc.getFromLocation(latitude, longitude, 1);
                Log.d("current Address", latitude+" "+longitude);
                Log.d("Below loop", "below loop");
                Address address = addresses.get(0);


                Log.d("Area 1", ""+addresses.get(0).getFeatureName()); // giving area of your location
                Log.d("Area 2", ""+addresses.get(0).getSubAdminArea());
                currentAddress = addresses.get(0).getSubLocality();
                Log.d("getSubLocality()", "" + addresses.get(0).getSubLocality());
                Log.d("getSubThoroughfare()", "" + addresses.get(0).getSubThoroughfare());
                Log.d("getThoroughfare()", "" + addresses.get(0).getAddressLine(0));

                //   allCoordinates.add(new LatLng(address.getLatitude(), address.getLongitude()));
                // }
            } catch (IOException e) {
                // handle the exception
            }
        }
        if(currentAddress != null) {
            Log.d("current Address", currentAddress);
            Toast.makeText(this, "Bus Location: " + currentAddress, Toast.LENGTH_SHORT).show();
        }

    }


    public int calculateTime(double loc1Lat1, double locLong, double Loc2Lat1, double Loc2Long2) {

        float result[] = new float[1];
        Log.d("Loc1Lat1, LocLong", ""+loc1Lat1+" "+locLong +" " + Loc2Lat1+" "+Loc2Long2);
        int speedin1kmmin = 150;
        Location.distanceBetween(loc1Lat1, locLong, Loc2Lat1, Loc2Long2, result);
        int distance=  (int) result[0];
        Log.d("Dist b/w user and bus", ""+distance);
        int time = distance/speedin1kmmin;
        Log.d("Estimated time", ""+time);
        return time;
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public boolean checkUserLocationPermission(){

        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION))
            {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, Request_User_Location_Code);
            }
            else{
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, Request_User_Location_Code);
            }
            return false;
        }
        else return true;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case Request_User_Location_Code:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                        if(googleApiClient == null){
                            buildApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                }
                else{
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }

    protected synchronized void buildApiClient(){
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        lastLocation = location;

        if(currentUserLocationMarker != null){
            currentUserLocationMarker.remove();
        }
        double lat = userInfo.getLatitude();
        double lng = userInfo.getLongitude();
        int time=0;
        String message="";
        if(lat !=0 && lng !=0 && location.getLatitude() != 0 && location.getLongitude() != 0) {
            time = calculateTime(lat, lng, location.getLatitude(), location.getLongitude());
            if(time<=1)  message = "Arriving Now";
            else message = String.valueOf(time)+" min";
        }
        else message = "Some Error Occurrred...Please try again";
        Log.d("Location in Maps" , lat+" "+ lng);

        latLng = new LatLng(location.getLatitude(), location.getLongitude());
        LatLng latlng1 = new LatLng(userInfo.getLatitude(), userInfo.getLongitude());
      //  LatLng latlng1 = new LatLng(22.7416242, 75.8872407);
     //   String url = getUrl(latlng1, latLng, "driving");
      //  new FetchURL(MapsActivity.this).execute(url, "driving");

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("user current location ");
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_loc));
        currentUserLocationMarker = mMap.addMarker(markerOptions);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)
                .zoom(13)
                .bearing(0)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        Toast.makeText(this, "Estimated arrival time: "+message, Toast.LENGTH_SHORT).show();

        if(googleApiClient != null){
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
        }


    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1100);
        locationRequest.setFastestInterval(1100);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);

        }
    }

  /*  private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        Log.d("google api ", ""+R.string.google_maps_key);
        return url;
    }
    */

  /*  @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }
    */

}
