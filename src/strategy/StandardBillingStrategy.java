package strategy;

import models.Bill;
import models.Item;
import java.util.Scanner;

public class StandardBillingStrategy implements BillingStrategy {

    private double total;
    private double cashTendered;
    private double change;

    @Override

    public double calculateTotal(Bill bill) {
        Scanner scanner = new Scanner(System.in);

// Standard billing calculation
        total = 0;

        for(Item item : bill.getItems()) {
            total += item.getQuantity() * item.getUnitPrice();
        }

        System.out.println("Enter Cash Tendered: ");
        cashTendered = scanner.nextDouble();
        change = cashTendered - total;
        return total;
    }

    public double getChange() {
        return change;
    }

    public double getCashTendered() {
        return cashTendered;
    }


}
