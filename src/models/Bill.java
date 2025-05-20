package models;

import java.sql.Date;

import java.util.List;

public class Bill {
    private int billId;
    private List<Item> items;
    private Date date;
    private double cashTendered;
    private double totalAmount;
    private String change;

// Getters and Setters

    public int getBillId() { return billId; }
    public void setBillId(int billId) { this.billId = billId; }
    public List<Item> getItems() { return items; }
    public void setItems(List<Item> items) { this.items = items; }
    public Date getDate() { return date; }
    public void setDate(Date date) {
        this.date = date;
    }
}
