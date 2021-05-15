package com.example.agriventure.data.models;

public class Produce {
    public String product_id;
    public String product_image;
    public String product_name;
    public String product_category;
    public String product_state;//available now, available soon
    public String product_maturity_date;
    public String product_price;
    public String product_quantity;
    public String seller_name;
    public boolean is_sold;
    public int user_id;

    public Produce() {
    }

    public Produce(String product_image, String product_name, String product_category, String product_state, String product_maturity_date, String product_price, String product_quantity, boolean is_sold, int user_id, String seller_name) {
        this.product_image = product_image;
        this.product_name = product_name;
        this.product_category = product_category;
        this.product_state = product_state;
        this.product_maturity_date = product_maturity_date;
        this.product_price = product_price;
        this.product_quantity = product_quantity;
        this.is_sold = is_sold;
        this.user_id = user_id;
        this.seller_name = seller_name;
    }

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

    public String getProduct_state() {
        return product_state;
    }

    public void setProduct_state(String product_state) {
        this.product_state = product_state;
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

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }
}
