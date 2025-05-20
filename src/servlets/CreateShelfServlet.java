package servlets;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import singleton.DatabaseConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet("/createShelf")
public class CreateShelfServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/createShelf.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String shelfId = request.getParameter("shelfId");
        String shelfCapacityStr = request.getParameter("shelfCapacity");

        int shelfCapacity = 0;
        try {
            shelfCapacity = Integer.parseInt(shelfCapacityStr);
        } catch (NumberFormatException e) {
            // handle error, e.g., set error attribute and forward back to JSP
            request.setAttribute("error", "Invalid capacity value.");
            doGet(request, response);
            return;
        }

        try {
            saveShelf(shelfId, shelfCapacity);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        doGet(request, response);
    }

    public static void saveShelf(String shelfId, int capacity) throws SQLException {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        String query = "INSERT INTO shelf (shelf_id, capacity) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, shelfId);
            preparedStatement.setInt(2, capacity);
            preparedStatement.executeUpdate();
        }
    }
}