package servlets;

import dao.ItemDAO;
import dao.impl.ItemDAOImpl;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import main.Main;
import models.Item;
import singleton.DatabaseConnection;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;


@WebServlet("/createItem")
public class CreateItemServlet extends HttpServlet {

    public void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/createItem.jsp").forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String itemName = request.getParameter("itemName");
        String itemId = request.getParameter("itemId");

        try {
            NewAddItemToStock(itemId, itemName);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        doGet(request, response);
    }


    public static void NewAddItemToStock(String itemID, String Name) throws SQLException {
        Connection connection;
        try {
            connection = DatabaseConnection.getInstance().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ItemDAO itemDAO = new ItemDAOImpl(connection);


        String itemId = itemID;
        String name = Name;

        Item item = new Item(itemId, name);
//        saveItemToStock(itemId, name);
        itemDAO.addItem(item);
    }
}