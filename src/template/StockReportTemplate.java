package template;

import singleton.DatabaseConnection;

import java.sql.*;
import java.util.Scanner;

public class StockReportTemplate extends AbstractReport {

    @Override

    public void generateReport() {
        System.out.println("Generating stock report...");
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String query = "SELECT i.item_id, i.name, s.batch_id, s.quantity, s.unit_price, s.date_of_purchase, s.expiry_date " +
                    "FROM stock s " +
                    "JOIN item i ON s.item_id = i.item_id";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                System.out.printf("%-20s%-30s%-20s%-20s%-20s%-20s%-20s%n", "Item ID", "Item Name", "Batch ID", "Quantity", "Unit Price", "Date of Purchase", "Expiry Date");
                System.out.println("----------------------------------------------------------------------------------------------------------------------------");
                while (resultSet.next()) {
                    String itemId = resultSet.getString("item_id");
                    String itemName = resultSet.getString("name");
                    String batchId = resultSet.getString("batch_id");
                    int quantity = resultSet.getInt("quantity");
                    double unitPrice = resultSet.getDouble("unit_price");
                    Date dateOfPurchase = resultSet.getDate("date_of_purchase");
                    Date expiryDate = resultSet.getDate("expiry_date");
                    System.out.printf("%-20s%-30s%-20s%-20d%-20.2f%-20s%-20s%n", itemId, itemName, batchId, quantity, unitPrice, dateOfPurchase, expiryDate);
                    System.out.println("----------------------------------------------------------------------------------------------------------------------------");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
