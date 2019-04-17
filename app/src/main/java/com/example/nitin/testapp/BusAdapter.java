package com.example.nitin.testapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import static android.support.v4.content.ContextCompat.startActivity;

/**
 * Created by nitin on 3/16/2019.
 */

public class BusAdapter extends RecyclerView.Adapter<BusAdapter.BusviewHolder> {

    private String[] data;
    Context context;

    public BusAdapter(Context c, String[]data){
        context = c;
        this.data = data;
    }

    @Override
    public BusviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_layout, parent, false);
        return new BusviewHolder(view);
    }

  //  SearchBus searchBus = new SearchBus();

    @Override
    public void onBindViewHolder(BusviewHolder holder, int position) {
        final String busNumb = data[position];
        holder.busNumber.setText(busNumb);
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  searchBus.setBuseNumber(busNumb);
                Toast.makeText(v.getContext(), busNumb, Toast.LENGTH_SHORT).show();
             //   Intent myIntent = new Intent(v.getContext(), BusRouteList.class);
             //   myIntent.putExtra("busNumber", busNumb);
              //  context.startActivity(myIntent);

                Intent myIntent = new Intent(v.getContext(), MapsActivity.class);
                myIntent.putExtra("busNumber", busNumb);
                context.startActivity(myIntent);
            }
        });
    }



    @Override
    public int getItemCount() {
        return data.length;
    }

    public class BusviewHolder extends RecyclerView.ViewHolder {

        TextView busNumber;
        LinearLayout parentLayout;
        public BusviewHolder(View itemView) {
            super(itemView);
            busNumber = (TextView) itemView.findViewById(R.id.bus_no);
            parentLayout = (LinearLayout) itemView.findViewById(R.id.parent_layout);
        }
    }
}
