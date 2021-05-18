package com.example.agriventure.data.models;

public class Offer {
    public String offer_id;
    public String product_id;
    public String buyer_name;
    public String offer_status;
    public String offer_amount;
    public String offer_date;

    public Offer() {
    }

    public Offer(String product_id, String buyer_name, String offer_status, String offer_amount, String offer_date) {
        this.product_id = product_id;
        this.buyer_name = buyer_name;
        this.offer_status = offer_status;
        this.offer_amount = offer_amount;
        this.offer_date = offer_date;
    }

    public String getOffer_id() {
        return offer_id;
    }

    public void setOffer_id(String offer_id) {
        this.offer_id = offer_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getOffer_status() {
        return offer_status;
    }

    public void setOffer_status(String offer_status) {
        this.offer_status = offer_status;
    }

    public String getBuyer_name() {
        return buyer_name;
    }

    public void setBuyer_name(String buyer_name) {
        this.buyer_name = buyer_name;
    }

    public String getOffer_amount() {
        return offer_amount;
    }

    public void setOffer_amount(String offer_amount) {
        this.offer_amount = offer_amount;
    }

    public String getOffer_date() {
        return offer_date;
    }

    public void setOffer_date(String offer_date) {
        this.offer_date = offer_date;
    }
}
