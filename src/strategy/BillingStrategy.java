package strategy;

import models.Bill;

public interface BillingStrategy {

    double calculateTotal(Bill bill);

}
