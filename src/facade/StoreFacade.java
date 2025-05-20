package facade;
import models.Bill;
import models.Item;
import services.BillingService;
import services.StockService;
import services.UserService;
import services.ReportService;
import strategy.BillingStrategy;
import strategy.DiscountBillingStrategy;
import strategy.StandardBillingStrategy;
import singleton.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StoreFacade {
    private static BillingService billingService;
    private StockService stockService;
    private UserService userService;
    private ReportService reportService;
    static Scanner scanner = new Scanner(System.in);

    private static List<Bill> bill;

    public StoreFacade() {
        this.billingService = new BillingService();
        this.stockService = new StockService();
        this.userService = new UserService();
        this.reportService = new ReportService();
        this.bill = new ArrayList<>();
    }

    public void processCustomerBilling() {
        CustomerBilling();
    }

    public void manageStock() {
        stockService.updateStock();
    }

    public void manageUser() {
        userService.handleUserOperations();
    }

    public void generateReport(String reportType) {
        reportService.generate(reportType);
    }

    public void addItemToStock() {

// Implementation for adding item to stock
        System.out.println("Adding item to stock via StoreFacade...");
    }

    public static double processBilling(Bill bill, BillingStrategy strategy) {
        billingService.setBillingStrategy(strategy);
        return billingService.handleBilling(bill);
    }

    private static void CustomerBilling() {
        System.out.println("Customer Billing");
        System.out.println("****************");

        System.out.print("Enter Item Code \"I001B100\" : "); // Item code :- I100#001
        String itemCode = scanner.nextLine();
        // itemid is from I to #
        String itemId = itemCode.substring(1, itemCode.indexOf("B"));
        String batchId = itemCode.substring(itemCode.indexOf("B") + 1);

        while (!checkItemAvailability(itemId, batchId)) {
//            System.out.println("Item is not available in the shelf.");
            System.out.print("Enter Item Code \"I001B100\" : ");
            itemCode = scanner.nextLine();
            itemId = itemCode.substring(1, itemCode.indexOf("B"));
            batchId = itemCode.substring(itemCode.indexOf("B") + 1);
        }

        System.out.print("Enter Quantity: ");
        int quantity = scanner.nextInt();
        scanner.nextLine();

        while (!checkQuantityAvailability(itemId, batchId, quantity)) {
            System.out.println("Please enter a smaller quantity.");
            System.out.print("Enter Quantity: ");
            quantity = scanner.nextInt();
            scanner.nextLine();
        }

        // pull the unit price from the database table shelf_items
        double unitPrice = getUnitPrice(itemId, batchId);
        double totalPrice = quantity * unitPrice;

//        save them to the database table temp_bill
        saveToTempBill(itemId, batchId, quantity, unitPrice, totalPrice);

        CustomerBillingOptions();
    }

    public static boolean checkItemAvailability(String itemId, String batchId) {
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String query = "SELECT COUNT(*) FROM shelf_item WHERE item_id = ? AND batch_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, itemId);
                preparedStatement.setString(2, batchId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next() && resultSet.getInt(1) > 0) {
//                        System.out.println("Item is available in the shelf.");
                        return true;
                    } else {
//                        System.out.println("Item is not available in the shelf.");
                        return false;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean checkQuantityAvailability(String itemId, String batchId, int quantity) {
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String query = "SELECT quantity FROM shelf_item WHERE item_id = ? AND batch_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, itemId);
                preparedStatement.setString(2, batchId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int availableQuantity = resultSet.getInt(1);
                        if (availableQuantity >= quantity) {
//                            System.out.println("Item is available in the required quantity.");
                            return true;
                        } else {
//                            System.out.println("Item is not available in the required quantity.");
                            return false;
                        }
                    } else {
//                        System.out.println("Item is not available in the shelf.");
                        return false;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static double getUnitPrice(String itemId, String batchId) {
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String query = "SELECT unit_price FROM shelf_item WHERE item_id = ? AND batch_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, itemId);
                preparedStatement.setString(2, batchId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        double unitPrice = resultSet.getDouble(1);
                        System.out.println("Unit Price: " + unitPrice);
                        return unitPrice;
                    } else {
//                        System.out.println("Item is not available in the shelf.");
                        return 0;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static void saveToTempBill(String itemId, String batchId, int quantity, double unitPrice, double totalPrice) {
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String selectQuery = "SELECT quantity, total_price FROM temp_bill WHERE item_id = ? AND batch_id = ?";
            try (PreparedStatement selectStatement = connection.prepareStatement(selectQuery)) {
                selectStatement.setString(1, itemId);
                selectStatement.setString(2, batchId);
                try (ResultSet resultSet = selectStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int existingQuantity = resultSet.getInt("quantity");
                        double existingTotalPrice = resultSet.getDouble("total_price");
                        int newQuantity = existingQuantity + quantity;
                        double newTotalPrice = existingTotalPrice + totalPrice;

                        String updateQuery = "UPDATE temp_bill SET quantity = ?, total_price = ? WHERE item_id = ? AND batch_id = ?";
                        try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                            updateStatement.setInt(1, newQuantity);
                            updateStatement.setDouble(2, newTotalPrice);
                            updateStatement.setString(3, itemId);
                            updateStatement.setString(4, batchId);
                            int rows = updateStatement.executeUpdate();
                            if (rows > 0) {
                                System.out.println("Item quantity updated in the bill.");
                            } else {
                                System.out.println("Failed to update item in the bill.");
                            }
                        }
                    } else {
                        String insertQuery = "INSERT INTO temp_bill (item_id, batch_id, quantity, unit_price, total_price) VALUES (?, ?, ?, ?, ?)";
                        try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                            insertStatement.setString(1, itemId);
                            insertStatement.setString(2, batchId);
                            insertStatement.setInt(3, quantity);
                            insertStatement.setDouble(4, unitPrice);
                            insertStatement.setDouble(5, totalPrice);
                            int rows = insertStatement.executeUpdate();
                            if (rows > 0) {
                                System.out.println("Item added to the bill.");
                            } else {
                                System.out.println("Failed to add item to the bill.");
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void CustomerBillingOptions() {
        System.out.println("1. Add more items to the bill");
        System.out.println("2. View bill");
        System.out.println("3. Remove item from the bill");
        System.out.println("4. Checkout");
        System.out.println("5. Cancel");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        while (choice != 5) {
            switch (choice) {
                case 1:
                    CustomerBilling();
                    break;
                case 2:
                    viewBill();
                    break;
                case 3:
                    removeItemFromBill();
                    break;
                case 4:
                    checkout();
                    choice = 5;
                    break;
                default:
                    System.out.println("Invalid choice");
                    break;
            }
            if (choice != 5) {
                System.out.print("Enter your choice: ");
                choice = scanner.nextInt();
                scanner.nextLine();
            }
        }
        clearTempBill();
    }

    public static void viewBill() {
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String query = "SELECT * FROM temp_bill";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    System.out.printf("%-10s%-10s%-10s%-15s%-15s%n", "Item ID", "Batch ID", "Quantity", "Unit Price", "Total Price");
                    System.out.println("-------------------------------------------------------------");
                    while (resultSet.next()) {
                        System.out.printf("%-10s%-10s%-10d%-15.2f%-15.2f%n",
                                resultSet.getString("item_id"),
                                resultSet.getString("batch_id"),
                                resultSet.getInt("quantity"),
                                resultSet.getDouble("unit_price"),
                                resultSet.getDouble("total_price"));
                        System.out.println("-------------------------------------------------------------");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void removeItemFromBill() {
        System.out.print("Enter Item Code \"I001B100\" : ");
        String itemCode = scanner.nextLine();
        String itemId = itemCode.substring(1, itemCode.indexOf("B"));
        String batchId = itemCode.substring(itemCode.indexOf("B") + 1);

        while (!checkItemAvailability(itemId, batchId)) {
            System.out.println("Item is not available in the shelf.");
            System.out.print("Enter Item Code \"I001B100\" : ");
            itemCode = scanner.nextLine();
            itemId = itemCode.substring(1, itemCode.indexOf("B"));
            batchId = itemCode.substring(itemCode.indexOf("B") + 1);
        }

        System.out.println("Removing item from the bill...");

        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String query = "DELETE FROM temp_bill WHERE item_id = ? AND batch_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, itemId);
                preparedStatement.setString(2, batchId);
                int rows = preparedStatement.executeUpdate();
                if (rows > 0) {
                    System.out.println("Item removed from the bill.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void checkout() {
        System.out.println("Checking out...");

        double totalBill = 0;
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String query = "SELECT SUM(total_price) FROM temp_bill";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        totalBill = resultSet.getDouble(1);
                        System.out.println("Total Bill: " + totalBill);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("Choose billing strategy:");
        System.out.println("1. Standard Billing");
        System.out.println("2. Discount Billing");
        System.out.print("Enter your choice: ");

        int strategyChoice = scanner.nextInt();
        scanner.nextLine();

        while (strategyChoice != 1 && strategyChoice != 2) {
            System.out.println("Invalid choice. Please try again.");
            System.out.println("Choose billing strategy:");
            System.out.println("1. Standard Billing");
            System.out.println("2. Discount Billing");
            System.out.print("Enter your choice: ");
            strategyChoice = scanner.nextInt();
            scanner.nextLine();
        }

        BillingStrategy strategy = null;
        double cashTendered = 0;
        double change = 0;

        switch (strategyChoice) {
            case 1:
                strategy = new StandardBillingStrategy();
                break;
            case 2:
                System.out.print("Enter discount rate (e.g., 0.1 for 10%): ");
                double discount = scanner.nextDouble();
                scanner.nextLine();

                while (discount < 0 || discount > 1) {
                    System.out.println("Invalid discount rate. Please try again.");
                    System.out.print("Enter discount rate (e.g., 0.1 for 10%): ");
                    discount = scanner.nextDouble();
                    scanner.nextLine();
                }
                strategy = new DiscountBillingStrategy(discount);
                break;
        }

        // access the temp_bill table and create of type bill and add to the list in the Bill class by iterating through the temp_bill table and using the set_items method
        List<Item> items = new ArrayList<>();
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String query = "SELECT item_id, batch_id, quantity, unit_price FROM temp_bill";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String itemId = resultSet.getString("item_id");
                    String batchId = resultSet.getString("batch_id");
                    int quantity = resultSet.getInt("quantity");
                    double unitPrice = resultSet.getDouble("unit_price");

                    Item item = new Item(itemId, batchId, quantity, unitPrice);
                    items.add(item);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Bill bill = new Bill();
        bill.setItems(items);
        StoreFacade.bill.add(bill);

        double total = processBilling(bill, strategy);
        double discount = 0;

        if (strategyChoice == 1) {
            assert strategy instanceof StandardBillingStrategy;
            cashTendered = ((StandardBillingStrategy) strategy).getCashTendered();
            change = ((StandardBillingStrategy) strategy).getChange();
        } else {
            assert strategy instanceof DiscountBillingStrategy;
            cashTendered = ((DiscountBillingStrategy) strategy).getCashTendered();
            change = ((DiscountBillingStrategy) strategy).getChange();
            discount = ((DiscountBillingStrategy) strategy).getDiscountAmount();
        }

        System.out.println("Cash Tendered: " + cashTendered);
        System.out.println("Change: " + change);

        //give the discount price
        System.out.println("Discount Price: " + discount);
        System.out.println("Total Bill: " + total);
        System.out.println("Thank you for shopping with us.");
        System.out.println();

//        access the bill table and get the no of records
        int billId = getBillId();
        Date billDate = new Date(System.currentTimeMillis());
        saveToBillTable(billId, totalBill, cashTendered, change, billDate);

        // save all the items in the temp_bill table to the bill_item table
        saveToBillItemTable(billId);
        removeItemsFromShelf();
        clearTempBill();
    }

    public static int getBillId() {
        int billId = 0;
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String query = "SELECT COUNT(*) FROM bill";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        billId = resultSet.getInt(1) + 1;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return billId;
    }

    public static void saveToBillTable(int billId, double totalBill, double cashTendered, double change, Date billDate) {
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String query = "INSERT INTO bill (bill_id, total_bill, cash_tendered, change_amount, bill_date) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, billId);
                preparedStatement.setDouble(2, totalBill);
                preparedStatement.setDouble(3, cashTendered);
                preparedStatement.setDouble(4, change);
                preparedStatement.setDate(5, billDate);
                int rows = preparedStatement.executeUpdate();
                if (rows > 0) {
                    System.out.println("Bill saved to table.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void saveToBillItemTable(int billId) {
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String query = "SELECT * FROM temp_bill";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        String itemId = resultSet.getString("item_id");
                        String batchId = resultSet.getString("batch_id");
                        int quantity = resultSet.getInt("quantity");
                        double unitPrice = resultSet.getDouble("unit_price");
                        double totalPrice = resultSet.getDouble("total_price");

                        String insertQuery = "INSERT INTO bill_item (bill_id, item_id, batch_id, quantity, unit_price, total_price) VALUES (?, ?, ?, ?, ?, ?)";
                        try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                            insertStatement.setInt(1, billId);
                            insertStatement.setString(2, itemId);
                            insertStatement.setString(3, batchId);
                            insertStatement.setInt(4, quantity);
                            insertStatement.setDouble(5, unitPrice);
                            insertStatement.setDouble(6, totalPrice);
                            int rows = insertStatement.executeUpdate();
                            if (rows > 0) {
                                System.out.println("Item added to the bill table.");
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void removeItemsFromShelf() {
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String selectQuery = "SELECT item_id, batch_id, quantity FROM temp_bill";
            try (PreparedStatement selectStatement = connection.prepareStatement(selectQuery)) {
                try (ResultSet resultSet = selectStatement.executeQuery()) {
                    while (resultSet.next()) {
                        String itemId = resultSet.getString("item_id");
                        String batchId = resultSet.getString("batch_id");
                        int quantity = resultSet.getInt("quantity");

                        String updateQuery = "UPDATE shelf_item SET quantity = quantity - ? WHERE item_id = ? AND batch_id = ?";
                        try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                            updateStatement.setInt(1, quantity);
                            updateStatement.setString(2, itemId);
                            updateStatement.setString(3, batchId);
                            int rows = updateStatement.executeUpdate();
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void clearTempBill() {
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String query = "DELETE FROM temp_bill";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                int rows = preparedStatement.executeUpdate();
                if (rows > 0) {
                    System.out.println("Bill cleared.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
