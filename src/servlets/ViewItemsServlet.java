package servlets;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import singleton.DatabaseConnection;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/viewItems")
public class ViewItemsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Map<String, Object>> items = getItems();
        request.setAttribute("items", items);
        request.getRequestDispatcher("/WEB-INF/views/viewItems.jsp").forward(request, response);
    }

    public static List<Map<String, Object>> getItems() {
        List<Map<String, Object>> items = new ArrayList<>();
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String query = "SELECT DISTINCT s.item_id, i.name, s.unit_price FROM stock s JOIN item i ON s.item_id = i.item_id";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("itemId", resultSet.getString("item_id"));
                    item.put("itemName", resultSet.getString("name"));
                    item.put("unitPrice", resultSet.getDouble("unit_price"));
                    items.add(item);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }
}