package com.mohtaref.clinics.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mohtaref.clinics.R;
import com.mohtaref.clinics.model.MenuModel;

import java.util.List;

public class ListMenuAdapter extends RecyclerView.Adapter<ListMenuAdapter.MyViewHolder> {
    private List<MenuModel> ListMenuAdapter;

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
        ListMenuAdapter.get(position);
//        myViewHolder.Icon.
//                Picasso.get().load(img_cat1_string).fit().into(Icon);
          myViewHolder.MenuName.setText("");
    }


    @Override
    public int getItemCount() {
        return ListMenuAdapter.size();
    }



}
