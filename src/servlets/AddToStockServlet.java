package servlets;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import singleton.DatabaseConnection;

import java.io.IOException;
import java.sql.*;

@WebServlet("/addToStock")
public class AddToStockServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/addToStock.jsp").forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String itemId = request.getParameter("itemId");
        String batchId = request.getParameter("batchId");
        String quantityStr = request.getParameter("quantity");
        String unitPriceStr = request.getParameter("unitPrice");
        String dateOfPurchaseStr = request.getParameter("dateOfPurchase");
        String expiryDateStr = request.getParameter("expiryDate");

        int quantity = 0;
        double unitPrice = 0.0;
        Date dateOfPurchase = null;
        Date expiryDate = null;

        try {
            quantity = Integer.parseInt(quantityStr);
            unitPrice = Double.parseDouble(unitPriceStr);
            dateOfPurchase = Date.valueOf(dateOfPurchaseStr);
            expiryDate = Date.valueOf(expiryDateStr);
        } catch (Exception e) {
            request.setAttribute("error", "Invalid input values.");
            doGet(request, response);
            return;
        }

        if (checkBatchAvailabilityInStock(itemId, batchId)) {
            request.setAttribute("error", "Batch is already available in the stock.");
            doGet(request, response);
            return;
        }

        saveBatchToStock(itemId, batchId, quantity, unitPrice, dateOfPurchase, expiryDate);

        doGet(request, response);
    }

    public static boolean checkBatchAvailabilityInStock(String itemId, String batchId) {
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String query = "SELECT COUNT(*) FROM stock WHERE item_id = ? AND batch_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, itemId);
                preparedStatement.setString(2, batchId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    return resultSet.next() && resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void saveBatchToStock(String itemId, String batchId, int quantity, double unitPrice, Date dateOfPurchase, Date expiryDate) {
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String query = "INSERT INTO stock (item_id, batch_id, quantity, unit_price, date_of_purchase, expiry_date) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, itemId);
                preparedStatement.setString(2, batchId);
                preparedStatement.setInt(3, quantity);
                preparedStatement.setDouble(4, unitPrice);
                preparedStatement.setDate(5, dateOfPurchase);
                preparedStatement.setDate(6, expiryDate);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}