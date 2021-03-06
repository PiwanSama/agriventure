package com.example.agriventure.data.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Produce implements Parcelable {
    public String product_id;
    public String product_image;
    public String product_name;
    public String product_category;
    public String product_maturity_date;
    public String product_price;
    public String product_quantity;
    public String seller_name;
    public boolean is_sold;

    public Produce() {}

    public Produce(String product_image, String product_name, String product_category, String product_state, String product_maturity_date, String product_price, String product_quantity, boolean is_sold, String user_id, String seller_name) {
        this.product_image = product_image;
        this.product_name = product_name;
        this.product_category = product_category;
        this.product_maturity_date = product_maturity_date;
        this.product_price = product_price;
        this.product_quantity = product_quantity;
        this.is_sold = is_sold;
        this.seller_name = seller_name;
    }

    protected Produce(Parcel in) {
        product_id = in.readString();
        product_image = in.readString();
        product_name = in.readString();
        product_category = in.readString();
        product_maturity_date = in.readString();
        product_price = in.readString();
        product_quantity = in.readString();
        seller_name = in.readString();
        is_sold = in.readByte() != 0;
    }

    public static final Creator<Produce> CREATOR = new Creator<Produce>() {
        @Override
        public Produce createFromParcel(Parcel in) {
            return new Produce(in);
        }

        @Override
        public Produce[] newArray(int size) {
            return new Produce[size];
        }
    };

    public String getSeller_name() {
        return seller_name;
    }

    public void setSeller_name(String seller_name) {
        this.seller_name = seller_name;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_category() {
        return product_category;
    }

    public void setProduct_category(String product_category) {
        this.product_category = product_category;
    }

    public String getProduct_maturity_date() {
        return product_maturity_date;
    }

    public void setProduct_maturity_date(String product_maturity_date) {
        this.product_maturity_date = product_maturity_date;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public String getProduct_quantity() {
        return product_quantity;
    }

    public void setProduct_quantity(String product_quantity) {
        this.product_quantity = product_quantity;
    }

    public boolean isIs_sold() {
        return is_sold;
    }

    public void setIs_sold(boolean is_sold) {
        this.is_sold = is_sold;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(product_id);
        dest.writeString(product_image);
        dest.writeString(product_name);
        dest.writeString(product_category);
        dest.writeString(product_maturity_date);
        dest.writeString(product_price);
        dest.writeString(product_quantity);
        dest.writeString(seller_name);
        dest.writeByte((byte) (is_sold ? 1 : 0));
    }
}