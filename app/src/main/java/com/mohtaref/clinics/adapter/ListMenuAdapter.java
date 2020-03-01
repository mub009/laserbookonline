package com.mohtaref.clinics.adapter;

import android.content.Intent;
import android.database.DataSetObserver;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mohtaref.clinics.Clinics_List_Laser;
import com.mohtaref.clinics.HomePage;
import com.mohtaref.clinics.R;
import com.mohtaref.clinics.model.MenuModel;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
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
        public LinearLayout LLCategory;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            Icon= (ImageView) itemView.findViewById(R.id.img_menu_icon);
            MenuName= (TextView) itemView.findViewById(R.id.menu_Name);
            LLCategory=itemView.findViewById(R.id.LLcategoryList);
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
         myViewHolder.LLCategory.setOnClickListener(new View.OnClickListener() {
                                                       @Override
                                                       public void onClick(View v) {
                                                           Intent i = new Intent(v.getContext(), Clinics_List_Laser.class);
                                                           HashMap<String, String> hashMap = new HashMap<String, String>();
                                                           hashMap.put("categoryId", ListMenuAdapter.get(position).getCategoryId());
                                                           hashMap.put("categoryName",ListMenuAdapter.get(position).getTitleName());
                                                           i.putExtra("category", hashMap);
                                                           v.getContext().startActivity(i);
                                                       }
                                                   }
        );
    }



    @Override
    public int getItemCount() {
        return ListMenuAdapter.size();
    }



}
