//package servlets;
//
//import jakarta.servlet.*;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.*;
//
//import java.io.IOException;
//
//@WebServlet("/checkout")
//public class CheckoutServlet extends HttpServlet {
//
//    public void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//
//        request.getRequestDispatcher("/WEB-INF/views/checkout.jsp").forward(request, response);
//
//    }
//}

package servlets;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import models.User;
import singleton.DatabaseConnection;

import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get logged in username from session
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");
        String username = currentUser.getUsername();

        if (username == null) {
            response.sendRedirect(request.getContextPath() + "/onlineStoreLogin");
            return;
        }

        // Calculate total bill and get cart items
        // Get cart items for display
        List<Map<String, Object>> cartItems = getCartItems();
        request.setAttribute("cartItems", cartItems);

        // Calculate total

        // create a for loop that goes through the cart items and add the subtotal of each item to the total bill
        double totalBill = 0;
        for (Map<String, Object> item : cartItems) {
            double subtotal = (double) item.get("subtotal");
            totalBill += subtotal;
        }
        // Set total bill in request attribute
        request.setAttribute("totalBill", totalBill);

        request.getRequestDispatcher("/WEB-INF/views/checkout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");
        String username = currentUser.getUsername();

        if (username == null) {
            response.sendRedirect(request.getContextPath() + "/onlineStoreLogin");
            return;
        }

        try {
            // Calculate the total bill
            double totalBill = calculateTotalBill(username);

            // Get user ID
            String userId = getUserID(username);
            if (userId == null) {
                request.setAttribute("error", "User not found");
                doGet(request, response);
                return;
            }

            // Generate new bill number
            int onlineBillNo = getOnlineBillNo();

            // Save bill and bill items
            Date billDate = new Date(System.currentTimeMillis());
            saveOnlineBill(onlineBillNo, userId, totalBill, billDate);
            saveOnlineBillItems(onlineBillNo, userId);

            // Clear the user's cart after successful checkout
            clearCart(userId);

            // Set success message
            session.setAttribute("success", "Order placed successfully! Your order number is " + onlineBillNo);

            // Redirect to confirmation page or back to store
            response.sendRedirect(request.getContextPath() + "/onlineStore");

        } catch (SQLException e) {
            request.setAttribute("error", "Checkout failed: " + e.getMessage());
            doGet(request, response);
        }
    }

//    private List<Map<String, Object>> getCartItems(String username) throws SQLException {
//        List<Map<String, Object>> items = new ArrayList<>();
//        String userId = getUserID(username);
//
//        Connection connection = DatabaseConnection.getInstance().getConnection();
//        String query = "SELECT c.item_id, i.name, c.quantity, s.unit_price " +
//                       "FROM cart c JOIN item i ON c.item_id = i.item_id " +
//                       "JOIN stock s ON c.item_id = s.item_id " +
//                       "WHERE c.user_id = ?";
//
//        try (PreparedStatement stmt = connection.prepareStatement(query)) {
//            stmt.setString(1, userId);
//            try (ResultSet rs = stmt.executeQuery()) {
//                while (rs.next()) {
//                    Map<String, Object> item = new HashMap<>();
//                    item.put("itemId", rs.getString("item_id"));
//                    item.put("itemName", rs.getString("name"));
//                    item.put("quantity", rs.getInt("quantity"));
//                    item.put("unitPrice", rs.getDouble("unit_price"));
//                    item.put("subtotal", rs.getInt("quantity") * rs.getDouble("unit_price"));
//                    items.add(item);
//                }
//            }
//        }
//        return items;
//    }

    public static List<Map<String, Object>> getCartItems() {
        List<Map<String, Object>> cartItems = new ArrayList<>();
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String query = "SELECT c.item_id, i.name, c.quantity, s.unit_price FROM cart c JOIN item i ON c.item_id = i.item_id JOIN stock s ON c.item_id = s.item_id";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("itemId", resultSet.getString("item_id"));
                    item.put("itemName", resultSet.getString("name"));
                    item.put("quantity", resultSet.getInt("quantity"));
                    item.put("unitPrice", resultSet.getDouble("unit_price"));
                    item.put("subtotal", resultSet.getInt("quantity") * resultSet.getDouble("unit_price"));
                    cartItems.add(item);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cartItems;
    }

    private double calculateTotalBill(String username) throws SQLException {
        String userId = getUserID(username);
        double totalBill = 0;

        Connection connection = DatabaseConnection.getInstance().getConnection();
        String query = "SELECT SUM(c.quantity * s.unit_price) AS total " +
                       "FROM cart c JOIN stock s ON c.item_id = s.item_id " +
                       "WHERE c.user_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    totalBill = rs.getDouble("total");
                }
            }
        }
        return totalBill;
    }

    private String getUserID(String username) throws SQLException {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        String query = "SELECT user_id FROM user WHERE username = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("user_id");
                }
            }
        }
        return null;
    }

    private int getOnlineBillNo() throws SQLException {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        String query = "SELECT MAX(online_bill_id) AS max_id FROM online_bill";

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("max_id") + 1;
            }
        }
        return 1;
    }

    private void saveOnlineBill(int onlineBillId, String userId, double totalBill, Date billDate) throws SQLException {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        String query = "INSERT INTO online_bill (online_bill_id, user_id, total_bill, bill_date) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, onlineBillId);
            stmt.setString(2, userId);
            stmt.setDouble(3, totalBill);
            stmt.setDate(4, billDate);
            stmt.executeUpdate();
        }
    }

    private void saveOnlineBillItems(int onlineBillId, String userId) throws SQLException {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        String query = "SELECT c.item_id, c.quantity, s.unit_price FROM cart c " +
                      "JOIN stock s ON c.item_id = s.item_id WHERE c.user_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String itemId = rs.getString("item_id");
                    int quantity = rs.getInt("quantity");
                    double unitPrice = rs.getDouble("unit_price");

                    String insertQuery = "INSERT INTO online_bill_item (online_bill_id, item_id, quantity, unit_price) " +
                                         "VALUES (?, ?, ?, ?)";
                    try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
                        insertStmt.setInt(1, onlineBillId);
                        insertStmt.setString(2, itemId);
                        insertStmt.setInt(3, quantity);
                        insertStmt.setDouble(4, unitPrice);
                        insertStmt.executeUpdate();
                    }
                }
            }
        }
    }

    private void clearCart(String userId) throws SQLException {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        String query = "DELETE FROM cart WHERE user_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, userId);
            stmt.executeUpdate();
        }
    }
}