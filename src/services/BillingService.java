package services;

import builder.BillBuilder;
import strategy.BillingStrategy;
import strategy.StandardBillingStrategy;
import models.Bill;
import models.Item;
import java.sql.Date;
import java.util.List;

public class BillingService {
    private BillingStrategy billingStrategy;

    public BillingService() {
        this.billingStrategy = new StandardBillingStrategy(); // Default strategy
    }

    public void setBillingStrategy(BillingStrategy billingStrategy) {
        this.billingStrategy = billingStrategy;
    }

    public double handleBilling(Bill bill) {
        return billingStrategy.calculateTotal(bill);
    }

    public Bill createBill(int billId, List<Item> items, Date date) {
        BillBuilder builder = new BillBuilder();
        return builder.setBillId(billId)
                .addItem(new Item("I001", "Item1"))
                .addItem(new Item("I002", "Item2"))
                .setDate(date)
                .build();

    }
}
