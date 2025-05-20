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

@WebServlet("/viewCart")
public class ViewCartServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Map<String, Object>> cartItems = getCartItems();
        request.setAttribute("cartItems", cartItems);
        request.getRequestDispatcher("/WEB-INF/views/viewCart.jsp").forward(request, response);
    }

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
                    cartItems.add(item);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cartItems;
    }
}