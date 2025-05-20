package strategy;

import models.Bill;
import models.Item;

import java.util.Scanner;

public class DiscountBillingStrategy implements BillingStrategy {

    private double total;
    private double cashTendered;
    private double change;
    private double discountRate;
    private double discountAmount;

    public DiscountBillingStrategy(double discountRate) {

        this.discountRate = discountRate;

    }

    @Override

    public double calculateTotal(Bill bill) {
        Scanner scanner = new Scanner(System.in);

// Billing calculation with discount
        double total = 0;
        for(Item item : bill.getItems()) {
            total += item.getQuantity() * item.getUnitPrice();
        }

        System.out.println("Enter Cash Tendered: ");
        cashTendered = scanner.nextDouble();
        change = cashTendered - total;
        discountAmount = total * discountRate;
        return total - (total * discountRate);

    }


    public double getChange() {
        return change;
    }

    public double getCashTendered() {
        return cashTendered;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }
}
