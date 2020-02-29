package com.mohtaref.clinics.adapter;

import android.database.DataSetObserver;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mohtaref.clinics.R;
import com.mohtaref.clinics.model.MenuModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ListMenuAdapter extends RecyclerView.Adapter<ListMenuAdapter.MyViewHolder> implements Adapter {
    private List<MenuModel> ListMenuAdapter;

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    public static  class MyViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView Icon;
        public TextView MenuName;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            Icon= (ImageView) itemView.findViewById(R.id.img_menu_icon);
            MenuName= (TextView) itemView.findViewById(R.id.menu_Name);

        }
    }
    public ListMenuAdapter(List<MenuModel> ListMenuAdapter) {
        this.ListMenuAdapter = ListMenuAdapter;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycle_list_menu, viewGroup, false);

        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
         Picasso.get().load(ListMenuAdapter.get(position).getImage()).fit().into(myViewHolder.Icon);
         myViewHolder.MenuName.setText(ListMenuAdapter.get(position).getTitleName());



    }


    @Override
    public int getItemCount() {
        return ListMenuAdapter.size();
    }



}
