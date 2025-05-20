package template;

import singleton.DatabaseConnection;

import java.sql.*;
import java.util.Scanner;

public class ReorderReportTemplate extends AbstractReport {

    @Override

    public void generateReport() {
        System.out.println("Generating reorder report...");
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String query = "SELECT i.item_id, i.name, s.quantity " +
                    "FROM stock s " +
                    "JOIN item i ON s.item_id = i.item_id " +
                    "WHERE s.quantity < 50";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                System.out.printf("%-20s%-30s%-20s%n", "Item ID", "Item Name", "Quantity");
                System.out.println("-------------------------------------------------------------");
                while (resultSet.next()) {
                    String itemId = resultSet.getString("item_id");
                    String itemName = resultSet.getString("name");
                    int quantity = resultSet.getInt("quantity");
                    System.out.printf("%-20s%-30s%-20d%n", itemId, itemName, quantity);
                    System.out.println("-------------------------------------------------------------");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }



    }

}
