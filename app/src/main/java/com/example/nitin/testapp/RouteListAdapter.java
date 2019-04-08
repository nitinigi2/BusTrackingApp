package com.example.nitin.testapp;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by nitin on 4/3/2019.
 */

public class RouteListAdapter extends RecyclerView.Adapter<RouteListAdapter.RouteViewHolder>  {

    private String[] routeList;
    int index;
    int time;
    public RouteListAdapter(String[] routeList, int index, int time){
        this.routeList = routeList;
        this.index = index;
        this.time = time;
    }

    @Override
    public RouteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_bus_item, parent, false);
        return new RouteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RouteViewHolder holder, int position) {
        final String busRoute = routeList[position];
        holder.busstop.setText(busRoute);

        if(position == index)
        {
            holder.stopicon.setBackgroundColor(Color.parseColor("#0033cc"));
          //  holder.lay.setBackgroundColor(Color.parseColor("#0033cc"));
            if(time >1)
            holder.busime.setText(""+time+" min");
            else holder.busime.setText("Arriving Now");
        }
    }

    @Override
    public int getItemCount() {
        return routeList.length;
    }

    public class RouteViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout lay;
        TextView busstop;
        ImageView stopicon;
        TextView busime;
        public RouteViewHolder(View itemView) {
            super(itemView);

            busstop = (TextView) itemView.findViewById(R.id.bus_stop);
            busime = (TextView) itemView.findViewById(R.id.bustime);
            stopicon = (ImageView) itemView.findViewById(R.id.bus_stop_icon);
            lay = itemView.findViewById(R.id.parentlistlayout);
        }
    }
}
