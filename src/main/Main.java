package main;//import com.mysql.cj.protocol.a.LocalDateValueEncoder;
import dao.ItemDAO;
import dao.UserDAO;
import dao.impl.ItemDAOImpl;
import dao.impl.UserDAOImpl;
import models.Bill;
import models.Item;
import models.User;
import facade.StoreFacade;
import singleton.DatabaseConnection;
import strategy.BillingStrategy;

import java.sql.Date;
import java.sql.*;
import java.util.Scanner;

public class Main {

    static Scanner scanner = new Scanner(System.in);
    static StoreFacade storeFacade = new StoreFacade();
    static Connection connection;
    static {
        try {
            connection = DatabaseConnection.getInstance().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    static Bill bill = new Bill();
    static BillingStrategy strategy;

    public static void main(String[] args) throws SQLException {
        System.out.println("*************************************************************");
        System.out.println("         .d8888b. Y88b   d88P  .d88888b.   .d8888b.  ");
        System.out.println("        d88P  Y88b Y88b d88P  d88P\" \"Y88b d88P  Y88b ");
        System.out.println("        Y88b.       Y88o88P   888     888 Y88b.      ");
        System.out.println("         \"Y888b.     Y888P    888     888  \"Y888b.   ");
        System.out.println("            \"Y88b.    888     888     888     \"Y88b. ");
        System.out.println("              \"888    888     888     888       \"888 ");
        System.out.println("        Y88b  d88P    888     Y88b. .d88P Y88b  d88P ");
        System.out.println("         \"Y8888P\"     888      \"Y88888P\"   \"Y8888P\"  ");
        System.out.println("*************************************************************");
        System.out.println("          Welcome to SYOS - Your Shopping System!");
        System.out.println("*************************************************************");
        GeneralMenu();
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        while (choice != 12) {
            switch (choice) {
                case 1:
                    storeFacade.processCustomerBilling();
                    break;
                case 2:
                    AddItemToStock();
                    break;
                case 3:
                    AddToStock();
                    break;
                case 4:
                    ShelfItemCapacity();
                    break;
                case 5:
                    MoveStockToShelf();
                    break;
                case 6:
                    CreateShelf();
                    break;
                case 7:
                    OnlineStore();
                    break;
                case 8:
                    storeFacade.generateReport("dailysales");
                    break;
                case 9:
                    storeFacade.generateReport("reorder");
                    break;
                case 10:
                    storeFacade.generateReport("stock");
                    break;
                case 11:
                    storeFacade.generateReport("bill");
                    break;
                default:
                    System.out.println("Invalid choice");
                    break;
            }
            GeneralMenu();
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();
        }
    }

    public static void GeneralMenu() {
        System.out.println("1. Customer Billing");
        System.out.println("2. Create Item");
        System.out.println("3. Add to Stock");
        System.out.println("4. Shelf Item Capacity");
        System.out.println("5. Move Stock to Shelf");
        System.out.println("6. Create Shelf");
        System.out.println("7. Online Store");
        System.out.println("8. Sales Reports");
        System.out.println("9. Reorder Levels Report");
        System.out.println("10. Stock Report");
        System.out.println("11. Bill Report");
        System.out.println("12. Exit");
    }


    // Add Item to Stock
    public static void AddItemToStock() throws SQLException {
        ItemDAO itemDAO = new ItemDAOImpl(connection);
        System.out.println("Add Item to Stock");
        System.out.println("******************");
        System.out.print("Enter Item ID: ");
        String itemId = scanner.nextLine();

        while (checkItemAvailabilityInStock(itemId)) {
            System.out.println("Item is already available in the stock.");
            System.out.print("Enter Item ID: ");
            itemId = scanner.nextLine();
        }

        System.out.print("Enter Item Name: ");
        String name = scanner.nextLine();

        Item item = new Item(itemId, name);
//        saveItemToStock(itemId, name);
        itemDAO.addItem(item);
    }

//    public static void NewAddItemToStock(String itemID, String Name) throws SQLException {
//        ItemDAO itemDAO = new ItemDAOImpl(connection);
//
//        String itemId = itemID;
//        String name = Name;
//
//        Item item = new Item(itemId, name);
////        saveItemToStock(itemId, name);
//        itemDAO.addItem(item);
//    }

    public static boolean checkItemAvailabilityInStock(String itemId) {
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String query = "SELECT COUNT(*) FROM item WHERE item_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, itemId);
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

    // Add To Stock
    public static void AddToStock() {
        System.out.println("Add to Stock");
        System.out.println("************");
        System.out.print("Enter Item ID: ");
        String itemId = scanner.nextLine();

        while (!checkItemAvailabilityInStock(itemId)) {
            System.out.println("Item is not available in the stock.");
            System.out.print("Enter Item ID: ");
            itemId = scanner.nextLine();
        }

        System.out.print("Enter Batch ID: ");
        String batchId = scanner.nextLine();

        while (checkBatchAvailabilityInStock(itemId, batchId)) {
            System.out.println("Batch is already available in the stock.");
            System.out.print("Enter Batch ID: ");
            batchId = scanner.nextLine();
        }

        System.out.print("Enter Quantity: ");
        int quantity = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter Unit Price: ");
        double unitPrice = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Enter Date of Purchase (YYYY-MM-DD): ");
        Date dateOfPurchase = Date.valueOf(scanner.nextLine());

        System.out.print("Enter Expiry Date (YYYY-MM-DD): ");
        Date expiryDate = Date.valueOf(scanner.nextLine());

        saveBatchToStock(itemId, batchId, quantity, unitPrice, dateOfPurchase, expiryDate);

    }

    public static boolean checkBatchAvailabilityInStock(String itemId, String batchId) {
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String query = "SELECT COUNT(*) FROM stock WHERE item_id = ? AND batch_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, itemId);
                preparedStatement.setString(2, batchId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next() && resultSet.getInt(1) > 0) {
//                        System.out.println("Batch is available in the stock.");
                        return true;
                    } else {
//                        System.out.println("Batch is not available in the stock.");
                        return false;
                    }
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
                int rows = preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Shelf Item Capacity
    public static void ShelfItemCapacity() {
        System.out.println("Shelf Item Capacity");
        System.out.println("********************");
        System.out.println("Enter Shelf ID: ");
        String shelfId = scanner.nextLine();

        while (!checkShelfAvailability(shelfId)) {
            System.out.println("Shelf is not available.");
            System.out.println("Enter Shelf ID: ");
            shelfId = scanner.nextLine();
        }

        System.out.print("Enter Item ID: ");
        String itemId = scanner.nextLine();

        while (!checkItemAvailabilityInStock(itemId)) {
            System.out.println("Item is not available in the stock.");
            System.out.print("Enter Item ID: ");
            itemId = scanner.nextLine();
        }

        System.out.print("Enter Capacity Limit: ");
        int capacityLimit = scanner.nextInt();
        scanner.nextLine();

        // check if the entered capacity limit is greater than the capacity of the shelf
        while (isCapacityLimitExceeded(shelfId, capacityLimit)) {
            System.out.println("Capacity limit exceeds the shelf's capacity.");
            System.out.print("Enter Capacity Limit: ");
            capacityLimit = scanner.nextInt();
            scanner.nextLine();
        }
        saveToShelfItemCapacity(shelfId, itemId, capacityLimit);
    }

    public static boolean checkShelfAvailability(String shelfId) {
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String query = "SELECT COUNT(*) FROM shelf WHERE shelf_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, shelfId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next() && resultSet.getInt(1) > 0) {
                        System.out.println("Shelf is available.");
                        return true;
                    } else {
//                        System.out.println("Shelf is not available.");
                        return false;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isCapacityLimitExceeded(String shelfId, int capacityLimit) {
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            // Step 1: Retrieve the capacity of the shelf
            String shelfQuery = "SELECT capacity FROM shelf WHERE shelf_id = ?";
            int shelfCapacity = 0;
            try (PreparedStatement shelfStatement = connection.prepareStatement(shelfQuery)) {
                shelfStatement.setString(1, shelfId);
                try (ResultSet shelfResultSet = shelfStatement.executeQuery()) {
                    if (shelfResultSet.next()) {
                        shelfCapacity = shelfResultSet.getInt("capacity");
                    } else {
                        System.out.println("Shelf not found.");
                        return false;
                    }
                }
            }

            // Step 2: Sum the capacities from the shelf_item_capacity table
            String capacityQuery = "SELECT SUM(capacity) AS total_capacity FROM shelf_item_capacity WHERE shelf_id = ?";
            int totalCapacity = 0;
            try (PreparedStatement capacityStatement = connection.prepareStatement(capacityQuery)) {
                capacityStatement.setString(1, shelfId);
                try (ResultSet capacityResultSet = capacityStatement.executeQuery()) {
                    if (capacityResultSet.next()) {
                        totalCapacity = capacityResultSet.getInt("total_capacity");
                    }
                }
            }

            // Step 3: Check if the sum of the current capacities and the new capacityLimit exceeds the shelf's capacity
            return (totalCapacity + capacityLimit) > shelfCapacity;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void saveToShelfItemCapacity(String shelfId, String itemId, int capacityLimit) {
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String query = "INSERT INTO shelf_item_capacity (shelf_id, item_id, capacity) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, shelfId);
                preparedStatement.setString(2, itemId);
                preparedStatement.setInt(3, capacityLimit);
                int rows = preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Move Stock to Shelf
    public static void MoveStockToShelf() {
        System.out.println("Move Stock to Shelf");
        System.out.println("********************");

        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            // Step 1: Loop through each record in the shelf table
            String shelfQuery = "SELECT shelf_id FROM shelf";
            try (PreparedStatement shelfStatement = connection.prepareStatement(shelfQuery);
                 ResultSet shelfResultSet = shelfStatement.executeQuery()) {
                while (shelfResultSet.next()) {
                    String shelfId = shelfResultSet.getString("shelf_id");


                    // Step 2: Get the capacity limit for each item in the currently selected shelf
                    String capacityQuery = "SELECT item_id, capacity FROM shelf_item_capacity WHERE shelf_id = ?";
                    try (PreparedStatement capacityStatement = connection.prepareStatement(capacityQuery)) {
                        capacityStatement.setString(1, shelfId);
                        try (ResultSet capacityResultSet = capacityStatement.executeQuery()) {
                            while (capacityResultSet.next()) {
                                String itemId = capacityResultSet.getString("item_id");
                                int capacityLimit = capacityResultSet.getInt("capacity");

                                // Step 3: Check the shelf_item table for the current quantity
                                String shelfItemQuery = "SELECT batch_id, quantity FROM shelf_item WHERE item_id = ?";
                                int totalQuantity = 0;
                                try (PreparedStatement shelfItemStatement = connection.prepareStatement(shelfItemQuery)) {
                                    shelfItemStatement.setString(1, itemId);
                                    try (ResultSet shelfItemResultSet = shelfItemStatement.executeQuery()) {
                                        while (shelfItemResultSet.next()) {
                                            totalQuantity += shelfItemResultSet.getInt("quantity");
                                        }
                                    }
                                }

                                // Step 4: Calculate the ToBeMoved quantity
                                int toBeMoved = capacityLimit - totalQuantity;

                                // Step 5: Open the stock table, search for the item_id, sort by expiry_date ASC
                                String stockQuery = "SELECT batch_id, quantity, unit_price FROM stock WHERE item_id = ? ORDER BY expiry_date ASC";
                                try (PreparedStatement stockStatement = connection.prepareStatement(stockQuery)) {
                                    stockStatement.setString(1, itemId);
                                    try (ResultSet stockResultSet = stockStatement.executeQuery()) {

                                        while (stockResultSet.next() && toBeMoved > 0) {

                                            String batchId = stockResultSet.getString("batch_id");
                                            int stockQuantity = stockResultSet.getInt("quantity");
                                            double unitPrice = stockResultSet.getDouble("unit_price");

                                            int moveQuantity = Math.min(toBeMoved, stockQuantity);
                                            toBeMoved -= moveQuantity;

                                            // Step 6: Update the stock table
                                            String updateStockQuery = "UPDATE stock SET quantity = quantity - ? WHERE item_id = ? AND batch_id = ?";
                                            try (PreparedStatement updateStockStatement = connection.prepareStatement(updateStockQuery)) {
                                                updateStockStatement.setInt(1, moveQuantity);
                                                updateStockStatement.setString(2, itemId);
                                                updateStockStatement.setString(3, batchId);
                                                updateStockStatement.executeUpdate();
                                            }

                                            // Step 7: Add to the shelf_item table
                                            String checkShelfItemQuery = "SELECT quantity FROM shelf_item WHERE item_id = ? AND batch_id = ?";
                                            try (PreparedStatement checkShelfItemStatement = connection.prepareStatement(checkShelfItemQuery)) {
                                                checkShelfItemStatement.setString(1, itemId);
                                                checkShelfItemStatement.setString(2, batchId);
                                                try (ResultSet checkShelfItemResultSet = checkShelfItemStatement.executeQuery()) {
                                                    if (checkShelfItemResultSet.next()) {
                                                        // Update existing record
                                                        System.out.println("Updating existing record in shelf_item table");
                                                        String updateShelfItemQuery = "UPDATE shelf_item SET quantity = quantity + ? WHERE item_id = ? AND batch_id = ?";
                                                        try (PreparedStatement updateShelfItemStatement = connection.prepareStatement(updateShelfItemQuery)) {
                                                            updateShelfItemStatement.setInt(1, moveQuantity);
                                                            updateShelfItemStatement.setString(2, itemId);
                                                            updateShelfItemStatement.setString(3, batchId);
                                                            int rowsUpdated = updateShelfItemStatement.executeUpdate();
                                                            System.out.println("Rows updated: " + rowsUpdated);
                                                        }
                                                    } else {
                                                        // Insert new record
                                                        System.out.println("Inserting new record into shelf_item table");
                                                        String insertShelfItemQuery = "INSERT INTO shelf_item (item_id, batch_id, quantity, unit_price) VALUES (?, ?, ?, ?)";
                                                        try (PreparedStatement insertShelfItemStatement = connection.prepareStatement(insertShelfItemQuery)) {
                                                            insertShelfItemStatement.setString(1, itemId);
                                                            insertShelfItemStatement.setString(2, batchId);
                                                            insertShelfItemStatement.setInt(3, moveQuantity);
                                                            insertShelfItemStatement.setDouble(4, unitPrice);
                                                            int rowsInserted = insertShelfItemStatement.executeUpdate();
                                                            System.out.println("Rows inserted: " + rowsInserted);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        removeEmptyStock();
    }

    public static void removeEmptyStock() {
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();

            // Step 1: Delete referencing rows in the shelf_item table
            String deleteShelfItemQuery = "DELETE FROM shelf_item WHERE (item_id, batch_id) IN (SELECT item_id, batch_id FROM stock WHERE quantity = 0)";
            try (PreparedStatement deleteShelfItemStatement = connection.prepareStatement(deleteShelfItemQuery)) {
                deleteShelfItemStatement.executeUpdate();
            }

            // Step 2: Delete rows in the stock table where quantity is 0
            String deleteStockQuery = "DELETE FROM stock WHERE quantity = 0";
            try (PreparedStatement deleteStockStatement = connection.prepareStatement(deleteStockQuery)) {
                deleteStockStatement.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Create Shelf

    public static void CreateShelf() {
        System.out.println("Create Shelf");
        System.out.println("************");
        System.out.print("Enter Shelf ID: ");
        String shelfId = scanner.nextLine();

        while (checkShelfAvailability(shelfId)) {
            System.out.println("Shelf is already available.");
            System.out.print("Enter Shelf ID: ");
            shelfId = scanner.nextLine();
        }

        System.out.print("Enter Capacity: ");
        int capacity = scanner.nextInt();
        scanner.nextLine();
        saveShelf(shelfId, capacity);
    }

    public static void saveShelf(String shelfId, int capacity) {
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String query = "INSERT INTO shelf (shelf_id, capacity) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, shelfId);
                preparedStatement.setInt(2, capacity);
                int rows = preparedStatement.executeUpdate();
            }
            System.out.println("Shelf created successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Online Store
    public static void OnlineStore() throws SQLException {
        System.out.println("Online Store");
        System.out.println("************");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Exit");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        while (choice != 3) {
            switch (choice) {
                case 1:
                    login();
                    break;
                case 2:
                    register();
                    break;
                default:
                    System.out.println("Invalid choice");
                    break;
            }

            System.out.println("Online Store");
            System.out.println("************");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();
        }
    }

    public static void login() {
        System.out.println("Login");
        System.out.println("*****");

        System.out.print("Enter Username: ");
        String username = scanner.nextLine();

        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        if (checkLogin(username, password)) {
            System.out.println("Login successful.");
            OnlineMenu(username);
        } else {
            System.out.println("Login failed.");
        }
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

    public static void register() throws SQLException {
        System.out.println("Register");
        System.out.println("********");

        System.out.print("Enter Username: ");
        String username = scanner.nextLine();

        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

//        saveUser(username, password);
        UserDAO userDAO = new UserDAOImpl(connection);
        User user = new User(username, password);
        userDAO.addUser(user);

        System.out.println("User registered successfully.");
    }

    public static void saveUser(String username, String password) {
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String query = "INSERT INTO user (username, password) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                int rows = preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void OnlineMenu(String username) {
        System.out.println();
        System.out.println("Welcome To SYOS Online Store");
        System.out.println("******************************");
        System.out.println("1. View Items");
        System.out.println("2. Add to Cart");
        System.out.println("3. View Cart");
        System.out.println("4. Checkout");
        System.out.println("5. Logout");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        while (choice != 5) {
            switch (choice) {
                case 1:
                    viewItems();
                    break;
                case 2:
                    addToCart(username);
                    break;
                case 3:
                    viewCart();
                    break;
                case 4:
                    checkoutOnline(username);
                    clearCart(username);
                    break;
                default:
                    System.out.println("Invalid choice");
                    break;
            }

            System.out.println("Welcome To SYOS Online Store");
            System.out.println("******************************");
            System.out.println("1. View Items");
            System.out.println("2. Add to Cart");
            System.out.println("3. View Cart");
            System.out.println("4. Checkout");
            System.out.println("5. Logout");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();
        }

        // clear the cart for the user "username"
        clearCart(username);

    }

    public static void clearCart(String username) {
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String query = "DELETE FROM cart WHERE user_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, getUserID(username));
                int rows = preparedStatement.executeUpdate();
                if (rows > 0) {
                    System.out.println("Cart cleared.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void viewItems() {
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String query = "SELECT DISTINCT s.item_id, i.name, s.unit_price FROM stock s JOIN item i ON s.item_id = i.item_id";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                System.out.printf("%-20s%-30s%-20s%n", "Item ID", "Item Name", "Unit Price");
                System.out.println("-------------------------------------------------------------");
                while (resultSet.next()) {
                    String itemId = resultSet.getString("item_id");
                    String itemName = resultSet.getString("name");
                    double unitPrice = resultSet.getDouble("unit_price");
                    System.out.printf("%-20s%-30s%-20s%n", itemId, itemName, unitPrice);
                    System.out.println("-------------------------------------------------------------");
                    System.out.println();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addToCart(String username) {
        System.out.print("Enter Item ID: ");
        String itemId = scanner.nextLine();

        while (!checkItemAvailabilityInStock(itemId)) {
            System.out.println("Item is not available in the stock.");
            System.out.print("Enter Item ID: ");
            itemId = scanner.nextLine();
        }

        System.out.print("Enter Quantity: ");
        int quantity = scanner.nextInt();
        scanner.nextLine();
        String userID = getUserID(username);
        saveToCart(itemId, quantity, userID);
    }

    public static void saveToCart(String itemId, int quantity, String userId) {
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String query = "INSERT INTO cart (user_id, item_id, quantity) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, userId);
                preparedStatement.setString(2, itemId);
                preparedStatement.setInt(3, quantity);
                int rows = preparedStatement.executeUpdate();
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
                    } else {
                        return null;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void viewCart() {
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String query = "SELECT c.item_id, i.name, c.quantity, s.unit_price FROM cart c JOIN item i ON c.item_id = i.item_id JOIN stock s ON c.item_id = s.item_id";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                System.out.printf("%-20s%-30s%-20s%-20s%n", "Item ID", "Item Name", "Quantity", "Unit Price");
                System.out.println("-----------------------------------------------------------------------------");
                while (resultSet.next()) {
                    String itemId = resultSet.getString("item_id");
                    String itemName = resultSet.getString("name");
                    int quantity = resultSet.getInt("quantity");
                    double unitPrice = resultSet.getDouble("unit_price");
                    System.out.printf("%-20s%-30s%-20s%-20s%n", itemId, itemName, quantity, unitPrice);
                    System.out.println("-----------------------------------------------------------------------------");
                    System.out.println();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void checkoutOnline(String username) {
        System.out.println("Checking out...");

        double totalBill = 0;
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String query = "SELECT SUM(c.quantity * s.unit_price) FROM cart c JOIN stock s ON c.item_id = s.item_id";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        totalBill = resultSet.getDouble(1);
                        System.out.println("Total Bill: " + totalBill);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        int onlineBillNo = getOnlineBillNo();
        saveOnlineBill(onlineBillNo, getUserID(username), totalBill, new Date(System.currentTimeMillis()));
        saveOnlineBillItems(onlineBillNo, getUserID(username));
        System.out.println("Online bill saved successfully.");
    }

    // creating a record in the online_bill table with the fields: online_bill_id, user_id, total_bill, bill_date
    public static void saveOnlineBill(int onlineBillId, String userId, double totalBill, Date billDate) {
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String query = "INSERT INTO online_bill (online_bill_id, user_id, total_bill, bill_date) VALUES (?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, onlineBillId);
                preparedStatement.setString(2, userId);
                preparedStatement.setDouble(3, totalBill);
                preparedStatement.setDate(4, billDate);
                preparedStatement.executeUpdate();

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int getOnlineBillNo() {
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String query = "SELECT MAX(online_bill_id) FROM online_bill";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt(1) + 1;
                    } else {
                        return 1;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    // creating records in the online_bill_item table with the online_bill_id, item_id, quantity, unit_price
    public static void saveOnlineBillItems(int onlineBillId, String userId) {
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String query = "SELECT c.item_id, c.quantity, s.unit_price FROM cart c JOIN stock s ON c.item_id = s.item_id WHERE c.user_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, userId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        String itemId = resultSet.getString("item_id");
                        int quantity = resultSet.getInt("quantity");
                        double unitPrice = resultSet.getDouble("unit_price");

                        String insertQuery = "INSERT INTO online_bill_item (online_bill_id, item_id, quantity, unit_price) VALUES (?, ?, ?, ?)";
                        try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                            insertStatement.setInt(1, onlineBillId);
                            insertStatement.setString(2, itemId);
                            insertStatement.setInt(3, quantity);
                            insertStatement.setDouble(4, unitPrice);
                            int rows = insertStatement.executeUpdate();
                            if (rows > 0) {
                                System.out.println("Item added to the online_bill_item table.");
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}