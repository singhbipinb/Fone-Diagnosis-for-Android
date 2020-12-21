package com.kashisol.fonediagnosis;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class CPUAdapter extends RecyclerView.Adapter<CPUAdapter.MyViewHolder> {
    private ArrayList<cpuinput> list;


    public CPUAdapter(ArrayList<cpuinput> cpulist) {

        list = cpulist;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cpu_cards, parent, false);
        MyViewHolder mvh = new MyViewHolder(v);
        return mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        cpuinput curitem = list.get(position);
        holder.tv1.setText(curitem.getName());
        holder.tv2.setText(curitem.getMvalue());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv1, tv2;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv1 = itemView.findViewById(R.id.tvcpu1);
            tv2 = itemView.findViewById(R.id.tvcpu12);
        }
    }
}
