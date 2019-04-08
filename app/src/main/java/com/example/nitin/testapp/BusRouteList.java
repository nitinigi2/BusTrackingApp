package com.example.nitin.testapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class BusRouteList extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference myRef;

    String busNumber = "";

    TextView lat;
    TextView longt;
    TextView textBusNumber;
    RecyclerView recyclerView;
    RouteListAdapter adapter;

    int index = 0;

    private FusedLocationProviderClient client;

    private LatLng currentAddress;

    double latitude, longitude;
    int time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_route_list);

        Intent intent = getIntent();

        busNumber = intent.getStringExtra("busNumber");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        textBusNumber = (TextView) toolbar.findViewById(R.id.textbusNumber);
        textBusNumber.setText(busNumber);

        //   getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // shows the list of bus routes of a particular bus





        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();





        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showdata(dataSnapshot, busNumber, time);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        /*
        requestPermission();
        client = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {

            return;
        }
        client.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null){
                    currentAddress = new LatLng(location.getLatitude(), location.getLatitude());
                    Log.d("cuurentAddress", ""+location.getLatitude()+" " + location.getLongitude());
                    showlist(busNumber, location.getLatitude(), location.getLongitude());

                    int time = calculateTime(location.getLatitude(), location.getLongitude(), latitude, longitude);
                    Log.d("Time", ""+time);
                }
            }
        });
        */

    }



    private void showdata(DataSnapshot dataSnapshot, final String busNumber, int time) {
        for(DataSnapshot ds : dataSnapshot.getChildren()){
            UserInfo userinfo = new UserInfo();
            userinfo.setLatitude(ds.child(busNumber).getValue(UserInfo.class).getLatitude());
            userinfo.setLongitude(ds.child(busNumber).getValue(UserInfo.class).getLongitude());

           // lat.setText(String.valueOf(userinfo.getLatitude()));
           // longt.setText(String.valueOf(userinfo.getLongitude()));
            latitude = userinfo.getLatitude();
            longitude = userinfo.getLongitude();

            requestPermission();
            client = LocationServices.getFusedLocationProviderClient(this);
            if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {

                return;
            }
            client.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if(location != null){
                        currentAddress = new LatLng(location.getLatitude(), location.getLatitude());
                        Log.d("cuurentAddress", ""+location.getLatitude()+" " + location.getLongitude());


                        int time = calculateTime(location.getLatitude(), location.getLongitude(), latitude, longitude);
                        Log.d("Time", ""+time);
                        showlist(busNumber, location.getLatitude(), location.getLongitude(), time);
                    }
                }
            });
        }

    }



    public void showlist(String busNumber, double latitude, double longitude, int time){
        String[] routeList={"jkdshfk","jfhgkjf", "bsdhfdsajjya"};
        SearchBus searchBus = new SearchBus();
        BusRoutes busRoutes = new BusRoutes();
        String[] busNumbers = searchBus.busNumbers();
        HashMap<String[], String> routemap = searchBus.hashMapping(busRoutes.Routes(), busNumbers);

        for(Map.Entry<String[], String> map : routemap.entrySet()){
            if(map.getValue().equals(busNumber)){
                routeList = map.getKey();
            }

        }
        recyclerView = (RecyclerView) findViewById(R.id.routelist);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        String currentAreaLocation = "";
     //   List<LatLng> allCoordinates = new ArrayList<>(routeList.length); //list of all latlong for a bus route
        if(Geocoder.isPresent()){
            List<Address> addresses;
            try {

                Geocoder gc = new Geocoder(this);
                Log.d("Inside Geocoder", ""+gc);
                Log.d("Route List Length", ""+routeList.length);
            //    for(int i=0;i<routeList.length;i++) {
                    Log.d("Inside loop", "inside loop");

                   // addresses = gc.getFromLocationName("vijay nagar"+", indore", 1);
                    addresses = gc.getFromLocation(latitude, longitude, 1);
                    Log.d("current Address", latitude+" "+longitude);
                    Log.d("Below loop", "below loop");
                    Address address = addresses.get(0);

                    currentAreaLocation = addresses.get(0).getSubLocality();  // giving area of your location

                    Log.d("Area 1", ""+addresses.get(0).getFeatureName()); // giving area of your location
                    Log.d("Area 2", ""+addresses.get(0).getSubAdminArea());
                    Log.d("getSubLocality()", "" + addresses.get(0).getSubLocality());
                    Log.d("getSubThoroughfare()", "" + addresses.get(0).getSubThoroughfare());
                    Log.d("getThoroughfare()", "" + addresses.get(0).getAddressLine(0));

                 //   allCoordinates.add(new LatLng(address.getLatitude(), address.getLongitude()));
               // }
            } catch (IOException e) {
                // handle the exception
            }
        }


    /*    String loc = calculateLocation(allCoordinates, currentAddress, routeList);
        Log.d("Location", ""+loc); */
        index = Arrays.asList(routeList).indexOf(currentAreaLocation);
        Log.d("CuurentAreaLocation", currentAreaLocation);
        Log.d("Index", ""+index);

        adapter = new RouteListAdapter(routeList, index, time); // bus stops with respect to bus number
        recyclerView.setAdapter(adapter);
    }

   /* public static String calculateLocation(List<LatLng> latLon1, LatLng currentaddress, String[] routeList) {
        Log.d("List of all coordinats", ""+latLon1);
        if (latLon1 == null || currentaddress == null)
            return null;

        float[] result = new float[latLon1.size()];

        for (int i = 0; i < latLon1.size(); i++) {
            Location.distanceBetween(latLon1.get(i).latitude, latLon1.get(i).longitude,
                    currentaddress.latitude, currentaddress.longitude, result);
        }

        int min_dis = (int)result[0];
        int pos=0;
        for(int i=0;i<result.length;i++){
            if(result[i] < min_dis){ min_dis = (int)result[i]; pos = i;}
        }

        return routeList[pos];
    }
    */

    public int calculateTime(double loc1Lat1, double locLong, double Loc2Lat1, double Loc2Long2) {

        float result[] = new float[1];
        Log.d("Loc1Lat1, LocLong", ""+loc1Lat1+" "+locLong +" " + Loc2Lat1+" "+Loc2Long2);
        int speedin1kmMin = 100;
        Location.distanceBetween(loc1Lat1, locLong, Loc2Lat1, Loc2Long2, result);
        return (int) result[0]/speedin1kmMin;


    }

    public int returnIndex(){
        return index;
    }

    public void requestPermission(){
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
    }


}
