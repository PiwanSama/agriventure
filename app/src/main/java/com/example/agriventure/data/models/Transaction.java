package com.example.agriventure.data.models;

public class Transaction {
    public String tran_id;
    public String tran_amount;
    public String recepient_name;
    public String sender_name;
    public String payment_reason;
    public String payment_date;
    public String status;
    public boolean is_completed;
    public boolean is_reversed;

    public Transaction() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Transaction(String tran_amount, String recepient_name, String sender_name, String payment_reason, String payment_date, String status, boolean is_completed, boolean is_reversed) {
        this.tran_amount = tran_amount;
        this.recepient_name = recepient_name;
        this.sender_name = sender_name;
        this.payment_reason = payment_reason;
        this.payment_date = payment_date;
        this.status = status;
        this.is_completed = is_completed;
        this.is_reversed = is_reversed;
    }

    public String getTran_id() {
        return tran_id;
    }

    public void setTran_id(String tran_id) {
        this.tran_id = tran_id;
    }

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public String getTran_amount() {
        return tran_amount;
    }

    public void setTran_amount(String tran_amount) {
        this.tran_amount = tran_amount;
    }

    public String getRecepient_name() {
        return recepient_name;
    }

    public void setRecepient_name(String recepient_name) {
        this.recepient_name = recepient_name;
    }

    public String getPayment_reason() {
        return payment_reason;
    }

    public void setPayment_reason(String payment_reason) {
        this.payment_reason = payment_reason;
    }

    public String getPayment_date() {
        return payment_date;
    }

    public void setPayment_date(String payment_date) {
        this.payment_date = payment_date;
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
