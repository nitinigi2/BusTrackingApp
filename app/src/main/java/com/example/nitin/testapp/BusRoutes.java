package com.example.nitin.testapp;

import java.util.ArrayList;

/**
 * Created by nitin on 3/29/2019.
 */

public class BusRoutes {

    public ArrayList<String[]> Routes(){
        String[] buseNumbers = {"10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20"};

        String[] route1= {"CAT Choraha","Reti Mandir Square","Datt Mandir","Rajendra Nagar","Navneet Garden","Mandir","Vaishali Square","Prikanko Colony","Annapurna Nagar","Apna Sweets","Monica Galaxy","Bank Colony","Revenue Nagar/Usha Nagar","Mahow Naka","SVVV Campus"};

        String[] route2={"Sai Mandir (VIP Purasapar)","Navneet Gaurden Ring Road","Stop b/w Navneet Gaurden and Gopur Choki","Gopur Choki","Jaroliya Market 1","Jaroliya Market 2","SVVV Campus"};

        String[] route3={"Footi Kothi","Sethi Gate(Sudama Nagar)","Bank Colony Chouraha","Ranjeet Hanuman Temple","Dravid Nagar","Rajmohalla Chouraha","VIP Ramchandra Nagar","SVVV Campus"};

        String[] route4={"Ankit Hotel","Shikshak Nagar","Kalani Nagar","Vidhya Dham","Airport Thana","Gandhi Nagar","SVVV Campus"};

        String[] route5={"Shiv Mandir","Shankar Kirana","Boys Hostel","SVVV Campus"};

        String[] route6={"Chandan Nagar Chouraha","Raj Nagar","Ganga Nagar","Bada Ganpati","Jinsi","Kila Maidan Post Office","Khada Ganpati","Sch No. 51 PNB ATM","Sangam Nagar","Bangarda Stop1","Bangarda Stop2","Bangarda Stop3","Bangarda Stop4","SVVV Campus"};

        String[] route7={"Vaishnav Girls Hostel","SVVV Campus"};

        String[] route8={"Ghunghat Garden(RTO)","Jain Colony","Lokmanya Nagar","Mahow Naka","Malganj Choraha","Lohar Patti","Malhar Ganj Thana","Badwali Choki","Emli Bazar Choraha","Sadar Bazar(Ram Mandir)","SVVV Campus"};

        String[] route9={"Khajrana Chouraha","Royal Care Hospital","Shri Nagar Extension","Anand Bazar","Greater Kailash Hospital","Guitar Choraha","Industry House","Anoop Nagar","Apolo Hospital","Dainik Press","Vijay Nagar", "Bapat Choraha", "MR 10","SVVV Campus"};

        String[] route10={"Tower Chouraha","Khatiwala Tank","Saifi Nagar","Manikbag Bridge Starting","Manikbag Bridge Ending","Manikbag Square Signal","Collectorat","Harsiddhi","Rajwada","Nagar Nigam Square","Lokhande Bridge","Aganiban Press","Power House","Marimata","Akhada","Banganga","Tiwari Complex","Banganga Bridge Starting","Banganga Bridge Ending","SVVV Campus"};

        ArrayList<String[]> routeAll = new ArrayList<>();

        routeAll.add(route1);
        routeAll.add(route2);
        routeAll.add(route3);
        routeAll.add(route4);
        routeAll.add(route5);
        routeAll.add(route6);
        routeAll.add(route7);
        routeAll.add(route8);
        routeAll.add(route9);
        routeAll.add(route10);

        return routeAll;
    }
}
