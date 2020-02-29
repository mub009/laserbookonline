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
import com.squareup.picasso.Picasso;

import java.util.List;

public class List_Menu_Adapter extends RecyclerView.Adapter<List_Menu_Adapter.MyViewHolder> {
    private List<MenuModel> List_Menu_Adapter;

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
    public List_Menu_Adapter(List<MenuModel> List_Menu_Adapter) {
        this.List_Menu_Adapter = List_Menu_Adapter;
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
        List_Menu_Adapter.get(position);
//        myViewHolder.Icon.
//                Picasso.get().load(img_cat1_string).fit().into(Icon);
          myViewHolder.MenuName.setText("");
    }


    @Override
    public int getItemCount() {
        return List_Menu_Adapter.size();
    }



}
