package models;

public class Item {

    private String itemId;
    private String name;
    private String batchId;
    private int quantity;
    private double price;

    public Item(String itemId, String name) {
        this.itemId = itemId;
        this.name = name;

    }

    public Item(String itemId, String batchId, int quantity, double price) {
        this.itemId = itemId;
        this.batchId = batchId;
        this.quantity = quantity;
        this.price = price;
    }

// Getters and Setters

    public String getItemId() { return itemId; }
    public void setItemId(String itemId) { this.itemId = itemId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getUnitPrice() { return price; }
    public double getQuantity() { return quantity; }
    public void setUnitPrice(double v) {
        price = v;
    }

    public void setQuantity(int i) {
        quantity = i;
    }
}
