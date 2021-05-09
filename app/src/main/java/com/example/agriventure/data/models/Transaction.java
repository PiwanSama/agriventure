package com.example.agriventure.data.models;

public class Transaction {
    public int tran_id;
    public String tran_amount;
    public String order_id;
    public boolean is_completed;
    public boolean is_reversed;

    public int getTran_id() {
        return tran_id;
    }

    public void setTran_id(int tran_id) {
        this.tran_id = tran_id;
    }

    public String getTran_amount() {
        return tran_amount;
    }

    public void setTran_amount(String tran_amount) {
        this.tran_amount = tran_amount;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public boolean isIs_completed() {
        return is_completed;
    }

    public void setIs_completed(boolean is_completed) {
        this.is_completed = is_completed;
    }

    public boolean isIs_reversed() {
        return is_reversed;
    }

    public void setIs_reversed(boolean is_reversed) {
        this.is_reversed = is_reversed;
    }
}
