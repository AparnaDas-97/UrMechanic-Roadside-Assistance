package com.example.mechfinderapp.serviceRecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mechfinderapp.R;

import java.util.ArrayList;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceViewHolders> {
    private ArrayList<ServiceObject> itemList;
    private Context context;

    public ServiceAdapter(ArrayList<ServiceObject> itemList,Context context){
        this.itemList = itemList;
        this.context = context;
    }

    @NonNull
    @Override
    public ServiceViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_service,null,false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        ServiceViewHolders rcv = new ServiceViewHolders(layoutView);
        return rcv;
    }

    public void onBindViewHolder(ServiceViewHolders holder, final int position) {
        holder.registrationNumber.setText(itemList.get(position).getUser());
      //  if(itemList.get(position).getTime()!=null){
          //  holder.time.setText(itemList.get(position).getTime());
       // }
    }
    @Override
    public int getItemCount() {
        return this.itemList.size();
    }

}
