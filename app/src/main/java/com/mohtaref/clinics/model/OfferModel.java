package com.mohtaref.clinics.model;

public class OfferModel{

    private String OfferImage,service,persentage,distance,address,previos_price,price;

    public OfferModel(String OfferImage, String service, String persentage, String distance, String address, String previos_price, String price)
    {
        this.address=address;
        this.distance=distance;
        this.OfferImage=OfferImage;
        this.persentage=persentage;
        this.previos_price=previos_price;
        this.price=price;
        this.service=service;
    }

    public String getOfferImage() {
        return OfferImage;
    }

    public void setOfferImage(String offerImage) {
        OfferImage = offerImage;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getPersentage() {
        return persentage;
    }

    public void setPersentage(String persentage) {
        this.persentage = persentage;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPrevios_price() {
        return previos_price;
    }

    public void setPrevios_price(String previos_price) {
        this.previos_price = previos_price;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }


}
