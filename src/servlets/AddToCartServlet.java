package servlets;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import models.User;
import singleton.DatabaseConnection;

import java.io.IOException;
import java.sql.*;

@WebServlet("/addToCart")
public class AddToCartServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/addToCart.jsp").forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");
        String username = currentUser.getUsername(); // Assuming User object has a getUsername() method

        String itemId = request.getParameter("itemId");
        String quantityStr = request.getParameter("quantity");

        if (username == null) {
//            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
            response.sendRedirect(request.getContextPath() + "/onineStoreLogin");
            return;
        }

        int quantity = 0;
        try {
            quantity = Integer.parseInt(quantityStr);
        } catch (Exception e) {
            request.setAttribute("error", "Invalid quantity.");
            doGet(request, response);
            return;
        }

        if (!checkItemAvailabilityInStock(itemId)) {
            request.setAttribute("error", "Item is not available in the stock.");
            doGet(request, response);
            return;
        }

        String userId = getUserID(username);
        saveToCart(itemId, quantity, userId);

        request.setAttribute("success", "Item added to cart.");
        doGet(request, response);
    }

    public static boolean checkItemAvailabilityInStock(String itemId) {
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String query = "SELECT COUNT(*) FROM stock WHERE item_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, itemId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    return resultSet.next() && resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void saveToCart(String itemId, int quantity, String userId) {
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String query = "INSERT INTO cart (user_id, item_id, quantity) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, userId);
                preparedStatement.setString(2, itemId);
                preparedStatement.setInt(3, quantity);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String getUserID(String username) {
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String query = "SELECT user_id FROM user WHERE username = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getString("user_id");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}