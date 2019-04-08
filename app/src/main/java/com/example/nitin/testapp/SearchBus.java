package com.example.nitin.testapp;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchBus extends Fragment {
    private AutoCompleteTextView startingLocation;
    private AutoCompleteTextView destinationLocation;
    private Button searchBus;
    RecyclerView recyclerView;
    final String[] buseNumbers = {"10", "11", "12", "13", "14", "15", "16", "17", "18", "19"};
    public SearchBus() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_bus, container, false);

        String busroutes[] = {"Vijay Nagar", "Bapat Choraha", "MR 10", "Aurbindo Hospital", "SVVV Campus",
                "Sayaji", "LIG" ,"Bombay Hospital", "Railway Station", "CAT Choraha","Reti Mandir Square",
                "Datt Mandir","Rajendra Nagar","Navneet Garden (Dhadichi Square)","Mandir","Vaishali Square","Prikanko Colony",
                "Annapurna Nagar","Apna Sweets","Monica Galaxy","Bank Colony","Revenue Nagar/Usha Nagar","Mahow Naka",
                "Sai Mandir (VIP Purasapar)","Navneet Gaurden Ring Road","Stop b/w Navneet Gaurden and Gopur Choki",
                "Gopur Choki","Jaroliya Market 1","Jaroliya Market 2"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (getContext(), android.R.layout.select_dialog_item, busroutes);

        startingLocation = (AutoCompleteTextView) view.findViewById(R.id.starting_location);
        destinationLocation = (AutoCompleteTextView) view.findViewById(R.id.destination_location);
        searchBus = (Button) view.findViewById(R.id.search_btn);


        startingLocation.setThreshold(1);//will start working from first character
        startingLocation.setAdapter(adapter);

        destinationLocation.setThreshold(1);
        destinationLocation.setAdapter(adapter);

        getData(view);

        return view;
    }

    public void getData(View view){


        recyclerView = (RecyclerView) view.findViewById(R.id.bus_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        searchBus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // startingloc and destination will be used to find buses
                String startingLoc = startingLocation.getText().toString().trim();
                String destinationLoc = destinationLocation.getText().toString().trim();

                String[] buseNumbers = selectbus(startingLoc, destinationLoc);

                if(TextUtils.isEmpty(startingLoc) || TextUtils.isEmpty(destinationLoc)){
                    Toast.makeText(getActivity().getApplicationContext(), "Please enter all fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(startingLocation.getText().toString().equals(destinationLocation.getText().toString())){
                    Toast.makeText(getContext(), "Stating location and destination can't be same", Toast.LENGTH_SHORT).show();
                }
                else

                // showing all available buses
                recyclerView.setAdapter(new BusAdapter(getContext() ,buseNumbers));

            }
        });

    }

    public String[] selectbus(String startingLoc, String destinationLoc){

        BusRoutes busRoutes = new BusRoutes();

        ArrayList<String[]> routeAll = busRoutes.Routes();


        ArrayList<String[]> actualRoutes = new ArrayList<>();
        for(int i=0;i<routeAll.size();i++) {
            String[] check = routeAll.get(i);
            if (Arrays.asList(check).contains(startingLoc) && Arrays.asList(check).contains(destinationLoc)) {
                if (Arrays.asList(check).indexOf(destinationLoc) > Arrays.asList(check).indexOf(startingLoc)) {
                    actualRoutes.add(routeAll.get(i));
                }
                else if(startingLoc.equals("SVVV Campus")){
                    actualRoutes.add(routeAll.get(i));
                }

            }

        }

        /*HashMap<String[], String> mapp = new HashMap<>();
        String[] resultBusNumbers = new String[actualRoutes.size()];

        for(int i=0;i<routeAll.size();i++){
            mapp.put(routeAll.get(i), buseNumbers[i]);
        }
        */

        HashMap<String[], String> mapp = hashMapping(routeAll, buseNumbers);

        String[] resultBusNumbers = new String[actualRoutes.size()];

        int k=0;
        for (Map.Entry<String[], String> entry : mapp.entrySet()){
            if(actualRoutes.contains(entry.getKey())){
                resultBusNumbers[k] = entry.getValue();
                k++;
            }
        }

        return resultBusNumbers;
    }

    // return mapping content
   public HashMap<String[], String> hashMapping(ArrayList<String[]> list, String[] buseNumbers){
       HashMap<String[], String> mapping = new HashMap<>();
       for(int i=0;i<list.size();i++){
           mapping.put(list.get(i), buseNumbers[i]);
       }

       return mapping;
   }
    // reutrns bus number list
    public String[] busNumbers(){
        return buseNumbers;
    }
}
