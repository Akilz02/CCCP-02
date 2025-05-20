package template;

import singleton.DatabaseConnection;

import java.sql.*;
import java.util.Scanner;

public class DailySalesReportTemplate extends AbstractReport {

    @Override

    public void generateReport() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Generating daily sales report...");
        System.out.print("Enter the date (YYYY-MM-DD): ");
        String dateString = scanner.nextLine();
        Date date = Date.valueOf(dateString);
        System.out.println();
        System.out.println("Generating In-Store Daily Sales Report");

        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String query = "SELECT i.item_id, i.name, SUM(bi.quantity) AS total_quantity, SUM(bi.total_price) AS total_revenue " +
                    "FROM bill_item bi " +
                    "JOIN item i ON bi.item_id = i.item_id " +
                    "JOIN bill b ON bi.bill_id = b.bill_id " +
                    "WHERE DATE(b.bill_date) = ? " +
                    "GROUP BY i.item_id, i.name";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setDate(1, date);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    System.out.printf("%-20s%-30s%-20s%-20s%n", "Item ID", "Item Name", "Total Quantity", "Total Revenue");
                    System.out.println("-------------------------------------------------------------------------------");
                    while (resultSet.next()) {
                        String itemId = resultSet.getString("item_id");
                        String itemName = resultSet.getString("name");
                        int totalQuantity = resultSet.getInt("total_quantity");
                        double totalRevenue = resultSet.getDouble("total_revenue");
                        System.out.printf("%-20s%-30s%-20d%-20.2f%n", itemId, itemName, totalQuantity, totalRevenue);
                        System.out.println("-------------------------------------------------------------------------------");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //generate online sales report
        System.out.println();
        System.out.println("Generating Online Daily Sales Report");
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String query = "SELECT i.item_id, i.name, SUM(oi.quantity) AS total_quantity, SUM(oi.quantity * oi.unit_price) AS total_revenue " +
                    "FROM online_bill_item oi " +
                    "JOIN item i ON oi.item_id = i.item_id " +
                    "JOIN online_bill o ON oi.online_bill_id = o.online_bill_id " +
                    "WHERE DATE(o.bill_date) = ? " +
                    "GROUP BY i.item_id, i.name";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setDate(1, date);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    System.out.printf("%-20s%-30s%-20s%-20s%n", "Item ID", "Item Name", "Total Quantity", "Total Revenue");
                    System.out.println("-------------------------------------------------------------------------------");
                    while (resultSet.next()) {
                        String itemId = resultSet.getString("item_id");
                        String itemName = resultSet.getString("name");
                        int totalQuantity = resultSet.getInt("total_quantity");
                        double totalRevenue = resultSet.getDouble("total_revenue");
                        System.out.printf("%-20s%-30s%-20d%-20.2f%n", itemId, itemName, totalQuantity, totalRevenue);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
