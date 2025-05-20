package servlets;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import singleton.DatabaseConnection;

import java.io.IOException;
import java.sql.*;
import java.util.*;

@WebServlet("/customerBilling")
public class CustomerBillingServlet extends HttpServlet {

    public static class BillItem {
        public String itemId;
        public String name;
        public int quantity;
        public double unitPrice;

        public BillItem(String itemId, String name, int quantity, double unitPrice) {
            this.itemId = itemId;
            this.name = name;
            this.quantity = quantity;
            this.unitPrice = unitPrice;
        }

        public double getSubtotal() {
            return quantity * unitPrice;
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<BillItem> bill = getBill(request);
        request.setAttribute("bill", bill);
        request.setAttribute("total", bill.stream().mapToDouble(BillItem::getSubtotal).sum());
        request.getRequestDispatcher("/WEB-INF/views/customerBilling.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String itemId = request.getParameter("itemId");
        int quantity = Integer.parseInt(request.getParameter("quantity"));

        // Fetch item details from DB
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            String sql = "SELECT i.name, s.unit_price FROM item i JOIN stock s ON i.item_id = s.item_id WHERE i.item_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, itemId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String name = rs.getString("name");
                        double unitPrice = rs.getDouble("unit_price");
                        BillItem item = new BillItem(itemId, name, quantity, unitPrice);
                        addToBill(request, item);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        doGet(request, response);
    }

    private List<BillItem> getBill(HttpServletRequest request) {
        HttpSession session = request.getSession();
        List<BillItem> bill = (List<BillItem>) session.getAttribute("bill");
        if (bill == null) {
            bill = new ArrayList<>();
            session.setAttribute("bill", bill);
        }
        return bill;
    }

    private void addToBill(HttpServletRequest request, BillItem item) {
        List<BillItem> bill = getBill(request);
        bill.add(item);
    }
}

