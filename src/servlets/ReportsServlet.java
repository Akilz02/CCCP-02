package servlets;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import singleton.DatabaseConnection;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/reports")
public class ReportsServlet extends HttpServlet {

    public void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.getRequestDispatcher("/WEB-INF/views/reports.jsp").forward(request, response);

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String reportType = request.getParameter("reportType");

        if (("reorder").equals(reportType)) {
            try {
                generateReorderReport(request);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else if ("stock".equals(reportType)) {
            try {
                generateStockReport(request);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else if ("bill".equals(reportType)) {
            try {
                generateNormalStoreTransactionsReport(request);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                generateOnlineStoreTransactionsReport(request);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        // Handle the report generation logic here based on the selected report type
        // For example, you can call a method to generate the report and set it as a request attribute

        request.setAttribute("success", "Report generated successfully.");
        doGet(request, response);
    }

    public void generateStockReport(HttpServletRequest request) throws SQLException {
        List<Map<String, Object>> stockItems = new ArrayList<>();
        Connection connection = DatabaseConnection.getInstance().getConnection();
        String query = "SELECT i.item_id, i.name, s.batch_id, s.quantity, s.unit_price, s.date_of_purchase, s.expiry_date " +
                "FROM stock s JOIN item i ON s.item_id = i.item_id";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Map<String, Object> item = new HashMap<>();
                item.put("itemId", resultSet.getString("item_id"));
                item.put("itemName", resultSet.getString("name"));
                item.put("batchId", resultSet.getString("batch_id"));
                item.put("quantity", resultSet.getInt("quantity"));
                item.put("unitPrice", resultSet.getDouble("unit_price"));
                item.put("dateOfPurchase", resultSet.getDate("date_of_purchase"));
                item.put("expiryDate", resultSet.getDate("expiry_date"));
                stockItems.add(item);
            }
        }
        request.setAttribute("stockItems", stockItems);
    }

    public void generateNormalStoreTransactionsReport(HttpServletRequest request) throws SQLException {
        List<Map<String, Object>> normalBills = new ArrayList<>();
        Connection connection = DatabaseConnection.getInstance().getConnection();
        String query = "SELECT bill_id, total_bill, cash_tendered, change_amount, bill_date FROM bill";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Map<String, Object> bill = new HashMap<>();
                bill.put("billId", resultSet.getInt("bill_id"));
                bill.put("totalBill", resultSet.getDouble("total_bill"));
                bill.put("cashTendered", resultSet.getDouble("cash_tendered"));
                bill.put("changeAmount", resultSet.getDouble("change_amount"));
                bill.put("billDate", resultSet.getDate("bill_date"));
                normalBills.add(bill);
            }
        }
        request.setAttribute("normalBills", normalBills);
    }

    public void generateOnlineStoreTransactionsReport(HttpServletRequest request) throws SQLException {
        List<Map<String, Object>> onlineBills = new ArrayList<>();
        Connection connection = DatabaseConnection.getInstance().getConnection();
        String query = "SELECT ob.online_bill_id, ob.total_bill, ob.bill_date, u.username " +
                "FROM online_bill ob JOIN user u ON ob.user_id = u.user_id";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Map<String, Object> bill = new HashMap<>();
                bill.put("onlineBillId", resultSet.getInt("online_bill_id"));
                bill.put("totalBill", resultSet.getDouble("total_bill"));
                bill.put("billDate", resultSet.getDate("bill_date"));
                bill.put("username", resultSet.getString("username"));
                onlineBills.add(bill);
            }
        }
        request.setAttribute("onlineBills", onlineBills);
    }

    public void generateReorderReport(HttpServletRequest request) throws SQLException {
        List<Map<String, Object>> reorderItems = new ArrayList<>();
        Connection connection = DatabaseConnection.getInstance().getConnection();
        String query = "SELECT i.item_id, i.name, s.quantity " +
                "FROM stock s JOIN item i ON s.item_id = i.item_id WHERE s.quantity < 50";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Map<String, Object> item = new HashMap<>();
                item.put("itemId", resultSet.getString("item_id"));
                item.put("itemName", resultSet.getString("name"));
                item.put("quantity", resultSet.getInt("quantity"));
                reorderItems.add(item);
            }
        }
        request.setAttribute("reorderItems", reorderItems);
    }

//    public void generateStockReport(HttpServletRequest request) {
//        System.out.println("Generating stock report...");
//        try {
//            Connection connection = DatabaseConnection.getInstance().getConnection();
//            String query = "SELECT i.item_id, i.name, s.batch_id, s.quantity, s.unit_price, s.date_of_purchase, s.expiry_date " +
//                    "FROM stock s " +
//                    "JOIN item i ON s.item_id = i.item_id";
//            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
//                 ResultSet resultSet = preparedStatement.executeQuery()) {
//                System.out.printf("%-20s%-30s%-20s%-20s%-20s%-20s%-20s%n", "Item ID", "Item Name", "Batch ID", "Quantity", "Unit Price", "Date of Purchase", "Expiry Date");
//                System.out.println("----------------------------------------------------------------------------------------------------------------------------");
//                while (resultSet.next()) {
//                    String itemId = resultSet.getString("item_id");
//                    String itemName = resultSet.getString("name");
//                    String batchId = resultSet.getString("batch_id");
//                    int quantity = resultSet.getInt("quantity");
//                    double unitPrice = resultSet.getDouble("unit_price");
//                    Date dateOfPurchase = resultSet.getDate("date_of_purchase");
//                    Date expiryDate = resultSet.getDate("expiry_date");
//                    System.out.printf("%-20s%-30s%-20s%-20d%-20.2f%-20s%-20s%n", itemId, itemName, batchId, quantity, unitPrice, dateOfPurchase, expiryDate);
//                    System.out.println("----------------------------------------------------------------------------------------------------------------------------");
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    public void generateNormalStoreTransactionsReport(HttpServletRequest request) {
//        System.out.println("Generating in store transactions report");
//        try {
//            Connection connection = DatabaseConnection.getInstance().getConnection();
//            String query = "SELECT bill_id, total_bill, cash_tendered, change_amount, bill_date " + "FROM bill ";
//            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
//                 ResultSet resultSet = preparedStatement.executeQuery()) {
//                System.out.printf("%-10s%-20s%-20s%-20s%-20s%n", "Bill ID", "Total Bill", "Cash Tendered", "Change Amount", "Bill Date");
//                System.out.println("------------------------------------------------------------------------------------------------------------");
//                while (resultSet.next()) {
//                    int billId = resultSet.getInt("bill_id");
//                    double totalBill = resultSet.getDouble("total_bill");
//                    double cashTendered = resultSet.getDouble("cash_tendered");
//                    double changeAmount = resultSet.getDouble("change_amount");
//                    Date billDate = resultSet.getDate("bill_date");
//                    System.out.printf("%-10d%-20.2f%-20.2f%-20.2f%-20s%n", billId, totalBill, cashTendered, changeAmount, billDate);
//                    System.out.println("------------------------------------------------------------------------------------------------------------");
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void generateOnlineStoreTransactionsReport(HttpServletRequest request) {
//        System.out.println();
//        System.out.println("Generating online store transactions report");
//        try {
//            Connection connection = DatabaseConnection.getInstance().getConnection();
//            String query = "SELECT ob.online_bill_id, ob.total_bill, ob.bill_date, u.username " +
//                    "FROM online_bill ob " +
//                    "JOIN user u ON ob.user_id = u.user_id";
//            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
//                 ResultSet resultSet = preparedStatement.executeQuery()) {
//                System.out.printf("%-15s%-20s%-20s%-20s%n", "Online Bill ID", "Total Bill", "Bill Date", "Username");
//                System.out.println("--------------------------------------------------------------------------------");
//                while (resultSet.next()) {
//                    int onlineBillId = resultSet.getInt("online_bill_id");
//                    double totalBill = resultSet.getDouble("total_bill");
//                    Date billDate = resultSet.getDate("bill_date");
//                    String username = resultSet.getString("username");
//                    System.out.printf("%-15d%-20.2f%-20s%-20s%n", onlineBillId, totalBill, billDate, username);
//                    System.out.println("--------------------------------------------------------------------------------");
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void generateReorderReport(HttpServletRequest request) {
//        System.out.println("Generating reorder report...");
//        try {
//            Connection connection = DatabaseConnection.getInstance().getConnection();
//            String query = "SELECT i.item_id, i.name, s.quantity " +
//                    "FROM stock s " +
//                    "JOIN item i ON s.item_id = i.item_id " +
//                    "WHERE s.quantity < 50";
//            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
//                 ResultSet resultSet = preparedStatement.executeQuery()) {
//                System.out.printf("%-20s%-30s%-20s%n", "Item ID", "Item Name", "Quantity");
//                System.out.println("-------------------------------------------------------------");
//                while (resultSet.next()) {
//                    String itemId = resultSet.getString("item_id");
//                    String itemName = resultSet.getString("name");
//                    int quantity = resultSet.getInt("quantity");
//                    System.out.printf("%-20s%-30s%-20d%n", itemId, itemName, quantity);
//                    System.out.println("-------------------------------------------------------------");
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//
//
//    }
}