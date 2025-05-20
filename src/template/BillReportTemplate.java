package template;


import singleton.DatabaseConnection;

import java.sql.*;

public class BillReportTemplate extends AbstractReport {

    @Override
    public void generateReport() {
        generateNormalStoreTransactionsReport();
        generateOnlineStoreTransactionsReport();
    }

    public void generateNormalStoreTransactionsReport() {
        System.out.println("Generating in store transactions report");
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String query = "SELECT bill_id, total_bill, cash_tendered, change_amount, bill_date " + "FROM bill ";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                System.out.printf("%-10s%-20s%-20s%-20s%-20s%n", "Bill ID", "Total Bill", "Cash Tendered", "Change Amount", "Bill Date");
                System.out.println("------------------------------------------------------------------------------------------------------------");
                while (resultSet.next()) {
                    int billId = resultSet.getInt("bill_id");
                    double totalBill = resultSet.getDouble("total_bill");
                    double cashTendered = resultSet.getDouble("cash_tendered");
                    double changeAmount = resultSet.getDouble("change_amount");
                    Date billDate = resultSet.getDate("bill_date");
                    System.out.printf("%-10d%-20.2f%-20.2f%-20.2f%-20s%n", billId, totalBill, cashTendered, changeAmount, billDate);
                    System.out.println("------------------------------------------------------------------------------------------------------------");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void generateOnlineStoreTransactionsReport() {
        System.out.println();
        System.out.println("Generating online store transactions report");
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String query = "SELECT ob.online_bill_id, ob.total_bill, ob.bill_date, u.username " +
                    "FROM online_bill ob " +
                    "JOIN user u ON ob.user_id = u.user_id";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                System.out.printf("%-15s%-20s%-20s%-20s%n", "Online Bill ID", "Total Bill", "Bill Date", "Username");
                System.out.println("--------------------------------------------------------------------------------");
                while (resultSet.next()) {
                    int onlineBillId = resultSet.getInt("online_bill_id");
                    double totalBill = resultSet.getDouble("total_bill");
                    Date billDate = resultSet.getDate("bill_date");
                    String username = resultSet.getString("username");
                    System.out.printf("%-15d%-20.2f%-20s%-20s%n", onlineBillId, totalBill, billDate, username);
                    System.out.println("--------------------------------------------------------------------------------");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}