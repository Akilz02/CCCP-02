package servlets;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import singleton.DatabaseConnection;

import java.io.IOException;
import java.sql.*;

@WebServlet("/shelfCapacity")
public class ShelfCapacityServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/shelfCapacity.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String shelfId = request.getParameter("shelfId");
        String itemId = request.getParameter("itemId");
        String capacityLimitStr = request.getParameter("capacityLimit");

        // Validate inputs
        if (shelfId == null || itemId == null || capacityLimitStr == null ||
            shelfId.isEmpty() || itemId.isEmpty() || capacityLimitStr.isEmpty()) {
            request.setAttribute("error", "All fields are required");
            request.getRequestDispatcher("/WEB-INF/views/shelfCapacity.jsp").forward(request, response);
            return;
        }

        int capacityLimit;
        try {
            capacityLimit = Integer.parseInt(capacityLimitStr);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Capacity limit must be a number");
            request.getRequestDispatcher("/WEB-INF/views/shelfCapacity.jsp").forward(request, response);
            return;
        }

        // Check shelf availability
        if (!checkShelfAvailability(shelfId)) {
            request.setAttribute("error", "Shelf is not available");
            request.getRequestDispatcher("/WEB-INF/views/shelfCapacity.jsp").forward(request, response);
            return;
        }

        // Check item availability
        if (!checkItemAvailabilityInStock(itemId)) {
            request.setAttribute("error", "Item is not available in stock");
            request.getRequestDispatcher("/WEB-INF/views/shelfCapacity.jsp").forward(request, response);
            return;
        }

        // Check capacity limit
        if (isCapacityLimitExceeded(shelfId, capacityLimit)) {
            request.setAttribute("error", "Capacity limit exceeds the shelf's capacity");
            request.getRequestDispatcher("/WEB-INF/views/shelfCapacity.jsp").forward(request, response);
            return;
        }

        // Save to database
        if (saveToShelfItemCapacity(shelfId, itemId, capacityLimit)) {
            request.setAttribute("success", "Shelf item capacity added successfully");
        } else {
            request.setAttribute("error", "Failed to add shelf item capacity");
        }

        request.getRequestDispatcher("/WEB-INF/views/shelfCapacity.jsp").forward(request, response);
    }

    private boolean checkShelfAvailability(String shelfId) {
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String query = "SELECT COUNT(*) FROM shelf WHERE shelf_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, shelfId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    return resultSet.next() && resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean checkItemAvailabilityInStock(String itemId) {
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

    private boolean isCapacityLimitExceeded(String shelfId, int capacityLimit) {
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            // Get shelf capacity
            String shelfQuery = "SELECT capacity FROM shelf WHERE shelf_id = ?";
            int shelfCapacity = 0;
            try (PreparedStatement stmt = connection.prepareStatement(shelfQuery)) {
                stmt.setString(1, shelfId);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        shelfCapacity = rs.getInt("capacity");
                    } else {
                        return true; // Shelf not found
                    }
                }
            }

            // Sum existing capacity allocations
            String capacityQuery = "SELECT SUM(capacity) AS total_capacity FROM shelf_item_capacity WHERE shelf_id = ?";
            int totalCapacity = 0;
            try (PreparedStatement stmt = connection.prepareStatement(capacityQuery)) {
                stmt.setString(1, shelfId);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        totalCapacity = rs.getInt("total_capacity");
                    }
                }
            }

            return (totalCapacity + capacityLimit) > shelfCapacity;
        } catch (SQLException e) {
            e.printStackTrace();
            return true; // Assume exceeded on error
        }
    }

    private boolean saveToShelfItemCapacity(String shelfId, String itemId, int capacityLimit) {
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String query = "INSERT INTO shelf_item_capacity (shelf_id, item_id, capacity) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, shelfId);
                preparedStatement.setString(2, itemId);
                preparedStatement.setInt(3, capacityLimit);
                return preparedStatement.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}