package servlets;

import dao.UserDAO;
import dao.impl.UserDAOImpl;
import models.User;
import singleton.DatabaseConnection;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/onlineStoreLogin")
public class OnlineStoreLoginServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/onlineStoreLogin.jsp").forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("register".equals(action)) {
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            try {
                register(username, password);
                doGet(request, response);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        } else {
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            try {
                if (login(username, password)) {
                    HttpSession session = request.getSession();
                    session.setAttribute("currentUser", new User(username));
                    response.sendRedirect(request.getContextPath() + "/onlineStore");
                } else {
                    request.getRequestDispatcher("/WEB-INF/views/onlineStoreLogin.jsp").forward(request, response);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public static boolean login(String Username, String Password) throws SQLException {

         String username = Username;
         String password = Password;
        boolean LoginFlag = false;

        if (checkLogin(username, password)) {
            LoginFlag = true;
        }
        return LoginFlag;
    }

    public static void register(String Username, String Password) throws SQLException {

        String username =  Username;
        String password = Password;

//        saveUser(username, password);
        Connection connection;

            try {
                connection = DatabaseConnection.getInstance().getConnection();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        UserDAO userDAO = new UserDAOImpl(connection);
        User user = new User(username, password);
        userDAO.addUser(user);

    }

    public static boolean checkLogin(String username, String password) {
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String query = "SELECT COUNT(*) FROM user WHERE username = ? AND password = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next() && resultSet.getInt(1) > 0) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

