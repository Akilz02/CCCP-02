package servlets;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import singleton.DatabaseConnection;

import java.io.IOException;
import java.sql.*;

@WebServlet("/moveToShelf")
public class MoveToShelfServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/moveToShelf.jsp").forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            moveStockToShelf();
            request.setAttribute("success", "Stock moved to shelf successfully.");
        } catch (Exception e) {
            request.setAttribute("error", "Error moving stock to shelf: " + e.getMessage());
        }
        doGet(request, response);
    }

    public static void moveStockToShelf() {
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String shelfQuery = "SELECT shelf_id FROM shelf";
            try (PreparedStatement shelfStatement = connection.prepareStatement(shelfQuery);
                 ResultSet shelfResultSet = shelfStatement.executeQuery()) {
                while (shelfResultSet.next()) {
                    String shelfId = shelfResultSet.getString("shelf_id");
                    String capacityQuery = "SELECT item_id, capacity FROM shelf_item_capacity WHERE shelf_id = ?";
                    try (PreparedStatement capacityStatement = connection.prepareStatement(capacityQuery)) {
                        capacityStatement.setString(1, shelfId);
                        try (ResultSet capacityResultSet = capacityStatement.executeQuery()) {
                            while (capacityResultSet.next()) {
                                String itemId = capacityResultSet.getString("item_id");
                                int capacityLimit = capacityResultSet.getInt("capacity");
                                int totalQuantity = 0;
                                String shelfItemQuery = "SELECT quantity FROM shelf_item WHERE item_id = ?";
                                try (PreparedStatement shelfItemStatement = connection.prepareStatement(shelfItemQuery)) {
                                    shelfItemStatement.setString(1, itemId);
                                    try (ResultSet shelfItemResultSet = shelfItemStatement.executeQuery()) {
                                        while (shelfItemResultSet.next()) {
                                            totalQuantity += shelfItemResultSet.getInt("quantity");
                                        }
                                    }
                                }
                                int toBeMoved = capacityLimit - totalQuantity;
                                String stockQuery = "SELECT batch_id, quantity, unit_price FROM stock WHERE item_id = ? ORDER BY expiry_date ASC";
                                try (PreparedStatement stockStatement = connection.prepareStatement(stockQuery)) {
                                    stockStatement.setString(1, itemId);
                                    try (ResultSet stockResultSet = stockStatement.executeQuery()) {
                                        while (stockResultSet.next() && toBeMoved > 0) {
                                            String batchId = stockResultSet.getString("batch_id");
                                            int stockQuantity = stockResultSet.getInt("quantity");
                                            double unitPrice = stockResultSet.getDouble("unit_price");
                                            int moveQuantity = Math.min(toBeMoved, stockQuantity);
                                            toBeMoved -= moveQuantity;
                                            String updateStockQuery = "UPDATE stock SET quantity = quantity - ? WHERE item_id = ? AND batch_id = ?";
                                            try (PreparedStatement updateStockStatement = connection.prepareStatement(updateStockQuery)) {
                                                updateStockStatement.setInt(1, moveQuantity);
                                                updateStockStatement.setString(2, itemId);
                                                updateStockStatement.setString(3, batchId);
                                                updateStockStatement.executeUpdate();
                                            }
                                            String checkShelfItemQuery = "SELECT quantity FROM shelf_item WHERE item_id = ? AND batch_id = ?";
                                            try (PreparedStatement checkShelfItemStatement = connection.prepareStatement(checkShelfItemQuery)) {
                                                checkShelfItemStatement.setString(1, itemId);
                                                checkShelfItemStatement.setString(2, batchId);
                                                try (ResultSet checkShelfItemResultSet = checkShelfItemStatement.executeQuery()) {
                                                    if (checkShelfItemResultSet.next()) {
                                                        String updateShelfItemQuery = "UPDATE shelf_item SET quantity = quantity + ? WHERE item_id = ? AND batch_id = ?";
                                                        try (PreparedStatement updateShelfItemStatement = connection.prepareStatement(updateShelfItemQuery)) {
                                                            updateShelfItemStatement.setInt(1, moveQuantity);
                                                            updateShelfItemStatement.setString(2, itemId);
                                                            updateShelfItemStatement.setString(3, batchId);
                                                            updateShelfItemStatement.executeUpdate();
                                                        }
                                                    } else {
                                                        String insertShelfItemQuery = "INSERT INTO shelf_item (item_id, batch_id, quantity, unit_price) VALUES (?, ?, ?, ?)";
                                                        try (PreparedStatement insertShelfItemStatement = connection.prepareStatement(insertShelfItemQuery)) {
                                                            insertShelfItemStatement.setString(1, itemId);
                                                            insertShelfItemStatement.setString(2, batchId);
                                                            insertShelfItemStatement.setInt(3, moveQuantity);
                                                            insertShelfItemStatement.setDouble(4, unitPrice);
                                                            insertShelfItemStatement.executeUpdate();
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            removeEmptyStock();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void removeEmptyStock() {
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String deleteShelfItemQuery = "DELETE FROM shelf_item WHERE (item_id, batch_id) IN (SELECT item_id, batch_id FROM stock WHERE quantity = 0)";
            try (PreparedStatement deleteShelfItemStatement = connection.prepareStatement(deleteShelfItemQuery)) {
                deleteShelfItemStatement.executeUpdate();
            }
            String deleteStockQuery = "DELETE FROM stock WHERE quantity = 0";
            try (PreparedStatement deleteStockStatement = connection.prepareStatement(deleteStockQuery)) {
                deleteStockStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}