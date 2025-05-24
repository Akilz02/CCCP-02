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

//
    public static class BillItem {
        private String itemId;
        private String name;
        private int quantity;
        private double unitPrice;
        private String batchId;

        public BillItem(String itemId, String name, int quantity, double unitPrice, String batchId) {
            this.itemId = itemId;
            this.name = name;
            this.quantity = quantity;
            this.unitPrice = unitPrice;
            this.batchId = batchId;
        }

        public String getItemId() {
            return itemId;
        }

        public String getName() {
            return name;
        }

        public int getQuantity() {
            return quantity;
        }

        public double getUnitPrice() {
            return unitPrice;
        }

        public String getBatchId() {
            return batchId;
        }

        public double getSubtotal() {
            return quantity * unitPrice;
        }

        // You'll also need setter methods if quantity gets updated
        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Load items from temp_bill to ensure consistency
        loadTempBillItems(request);
//        getBill(request);

        List<BillItem> bill = getBill(request);
        request.setAttribute("bill", bill);
        request.setAttribute("total", bill.stream().mapToDouble(BillItem::getSubtotal).sum());

        // Get available items for dropdown
        try {
            List<Map<String, Object>> items = getAvailableItems();
            request.setAttribute("availableItems", items);
        } catch (SQLException e) {
            request.setAttribute("error", "Error loading items: " + e.getMessage());
        }

        request.getRequestDispatcher("/WEB-INF/views/customerBilling.jsp").forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String itemId = request.getParameter("itemId");
            String batchId = request.getParameter("batchId");
            int quantity = Integer.parseInt(request.getParameter("quantity"));

            // Validate inputs
            if (itemId == null || batchId == null || quantity <= 0) {
                request.setAttribute("error", "Invalid item or quantity");
                doGet(request, response);
                return;
            }

            // Get item name and unit price from stock table instead of shelf_item
            String name = "";
            double unitPrice = 0;

            Connection connection = DatabaseConnection.getInstance().getConnection();
            String query = "SELECT i.name, s.unit_price FROM item i JOIN stock s ON i.item_id = s.item_id " +
                          "WHERE i.item_id = ? AND s.batch_id = ?";

            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setString(1, itemId);
                ps.setString(2, batchId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        name = rs.getString("name");
                        unitPrice = rs.getDouble("unit_price");
                    } else {
                        request.setAttribute("error", "Item not found");
                        doGet(request, response);
                        return;
                    }
                }
            }

            // Create BillItem and add to session
            BillItem item = new BillItem(itemId, name, quantity, unitPrice, batchId);
            updateBillItem(request, item);

            // Save to temp_bill table
            double totalPrice = quantity * unitPrice;
            saveToTempBill(itemId, batchId, quantity, unitPrice, totalPrice);

            request.setAttribute("success", "Item added to bill");
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid quantity");
        } catch (SQLException e) {
            request.setAttribute("error", "Database error: " + e.getMessage());
        }

        doGet(request, response);
    }

    private List<Map<String, Object>> getAvailableItems() throws SQLException {
        List<Map<String, Object>> items = new ArrayList<>();

        Connection connection = DatabaseConnection.getInstance().getConnection();
        String query = "SELECT i.item_id, i.name, s.batch_id, s.unit_price " +
                      "FROM item i JOIN stock s ON i.item_id = s.item_id " +
                      "WHERE s.quantity > 0";

        try (PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> item = new HashMap<>();
                item.put("itemId", rs.getString("item_id"));
                item.put("name", rs.getString("name"));
                item.put("batchId", rs.getString("batch_id"));
                item.put("unitPrice", rs.getDouble("unit_price"));
                items.add(item);
            }
        }

        return items;
    }

    public static double getUnitPrice(String itemId, String batchId) {
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String query = "SELECT unit_price FROM stock WHERE item_id = ? AND batch_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, itemId);
                preparedStatement.setString(2, batchId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getDouble("unit_price");
                    } else {
                        return 0;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static void saveToTempBill(String itemId, String batchId, int quantity, double unitPrice, double totalPrice) {
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String selectQuery = "SELECT quantity, total_price FROM temp_bill WHERE item_id = ? AND batch_id = ?";
            try (PreparedStatement selectStatement = connection.prepareStatement(selectQuery)) {
                selectStatement.setString(1, itemId);
                selectStatement.setString(2, batchId);
                try (ResultSet resultSet = selectStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int existingQuantity = resultSet.getInt("quantity");
                        double existingTotalPrice = resultSet.getDouble("total_price");
                        int newQuantity = existingQuantity + quantity;
                        double newTotalPrice = existingTotalPrice + totalPrice;

                        String updateQuery = "UPDATE temp_bill SET quantity = ?, total_price = ? WHERE item_id = ? AND batch_id = ?";
                        try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                            updateStatement.setInt(1, newQuantity);
                            updateStatement.setDouble(2, newTotalPrice);
                            updateStatement.setString(3, itemId);
                            updateStatement.setString(4, batchId);
                            updateStatement.executeUpdate();
                        }
                    } else {
                        String insertQuery = "INSERT INTO temp_bill (item_id, batch_id, quantity, unit_price, total_price) VALUES (?, ?, ?, ?, ?)";
                        try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                            insertStatement.setString(1, itemId);
                            insertStatement.setString(2, batchId);
                            insertStatement.setInt(3, quantity);
                            insertStatement.setDouble(4, unitPrice);
                            insertStatement.setDouble(5, totalPrice);
                            insertStatement.executeUpdate();
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadTempBillItems(HttpServletRequest request) {
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String query = "SELECT t.item_id, i.name, t.batch_id, t.quantity, t.unit_price " +
                          "FROM temp_bill t JOIN item i ON t.item_id = i.item_id";

            List<BillItem> bill = new ArrayList<>();

            try (PreparedStatement ps = connection.prepareStatement(query);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String itemId = rs.getString("item_id");
                    String name = rs.getString("name");
                    String batchId = rs.getString("batch_id");
                    int quantity = rs.getInt("quantity");
                    double unitPrice = rs.getDouble("unit_price");

                    bill.add(new BillItem(itemId, name, quantity, unitPrice, batchId));
                }
            }

            HttpSession session = request.getSession();
            session.setAttribute("bill", bill);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private List<BillItem> getBill(HttpServletRequest request) {
        HttpSession session = request.getSession();
        List<BillItem> bill = (List<BillItem>) session.getAttribute("bill");
        if (bill == null) {
            bill = new ArrayList<>();
            session.setAttribute("bill", bill);
        }
        return bill;
    }

    private void updateBillItem(HttpServletRequest request, BillItem newItem) {
        List<BillItem> bill = getBill(request);

        // Check if the item is already in the bill
        for (BillItem item : bill) {
            if (item.itemId.equals(newItem.itemId) && item.batchId.equals(newItem.batchId)) {
                item.quantity += newItem.quantity;
                return;
            }
        }

        // If not found, add as new item
        bill.add(newItem);
    }
}