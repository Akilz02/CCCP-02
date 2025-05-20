package builder;

import models.Bill;

import models.Item;

import java.util.ArrayList;

import java.util.List;

public class BillBuilder {

    private Bill bill;

    public BillBuilder() {

        bill = new Bill();

        bill.setItems(new ArrayList<>());

    }

    public BillBuilder setBillId(int billId) {

        bill.setBillId(billId);

        return this;

    }

    public BillBuilder addItem(Item item) {

        bill.getItems().add(item);

        return this;

    }

    public BillBuilder setDate(java.sql.Date date) {

        bill.setDate(date);

        return this;

    }

    public Bill build() {

        return bill;

    }

}
