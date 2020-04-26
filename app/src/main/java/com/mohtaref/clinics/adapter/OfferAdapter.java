package com.mohtaref.clinics.adapter;

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

import com.mohtaref.clinics.R;
import com.mohtaref.clinics.helper.BaseViewHolder;
import com.mohtaref.clinics.model.OfferModel;
import com.squareup.picasso.Picasso;

import java.util.List;


public class OfferAdapter extends RecyclerView.Adapter<BaseViewHolder> implements Adapter {
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private boolean isLoaderVisible = false;

    private List<OfferModel> offerModel;

    public OfferAdapter(List<OfferModel> offerModel)
    {
       this.offerModel=offerModel;
    }

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

    public static  class MyViewHolder extends BaseViewHolder
    {
        public ImageView OfferImage;
        public LinearLayout LLOffer;
        public TextView service,persentage,distance,address,previos_price,price;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            OfferImage= (ImageView) itemView.findViewById(R.id.imageView2);
            service= (TextView) itemView.findViewById(R.id.service);
            persentage= (TextView) itemView.findViewById(R.id.persentage);
            distance= (TextView) itemView.findViewById(R.id.distance);
            address= (TextView) itemView.findViewById(R.id.address);
            previos_price= (TextView) itemView.findViewById(R.id.previos_price);
            price= (TextView) itemView.findViewById(R.id.price);
            LLOffer=itemView.findViewById(R.id.LLOffer);

        }

        @Override
        protected void clear() {

        }

        @Override
        public void onBind(int position) {
            super.onBind(position);
//            offerModel item = offerModel.get(position);
//            Picasso.get().load(offerModel.get(position).getOfferImage()).fit().into(OfferImage);
//            service.setText(offerModel.get(position).getService());
//            persentage.setText(offerModel.get(position).getPersentage());
//            distance.setText(offerModel.get(position).getDistance());
//            address.setText(offerModel.get(position).getAddress());
//            previos_price.setText(offerModel.get(position).getPrevios_price());
//            price.setText(offerModel.get(position).getPrice());
        }
    }

    public class ProgressHolder extends BaseViewHolder {
        ProgressHolder(View itemView) {
            super(itemView);

        }
        @Override
        protected void clear() {
        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        switch (viewType) {
            case VIEW_TYPE_NORMAL:
            View itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.list_item, viewGroup, false);
            return new MyViewHolder(itemView);
            case VIEW_TYPE_LOADING:
//                return new ProgressHolder(
//                        LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_loading, viewGroup, false));
            default:
                return null;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder myViewHolder, int position) {
        myViewHolder.onBind(position);

    }





    @Override
    public int getItemCount() {
        return offerModel.size();
    }
}
