package dao.impl;

import dao.ItemDAO;

import models.Item;

import java.sql.*;

import java.util.ArrayList;

import java.util.List;

public class ItemDAOImpl implements ItemDAO {

    private Connection connection;

    public ItemDAOImpl(Connection connection) {

        this.connection = connection;

    }

    @Override

    public void addItem(Item item) throws SQLException {

        String query = "INSERT INTO item (item_id, name) VALUES (?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, item.getItemId());

            stmt.setString(2, item.getName());

            stmt.executeUpdate();

        }

    }

    @Override

    public Item getItemById(String itemId) throws SQLException {

        String query = "SELECT * FROM item WHERE item_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, itemId);

            try (ResultSet rs = stmt.executeQuery()) {

                if(rs.next()) {

                    return new Item(rs.getString("item_id"), rs.getString("name"));

                }

            }

        }

        return null;

    }

    @Override

    public List<Item> getAllItems() throws SQLException {

        String query = "SELECT * FROM item";

        List<Item> items = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(query);

             ResultSet rs = stmt.executeQuery()) {

            while(rs.next()) {

                items.add(new Item(rs.getString("item_id"), rs.getString("name")));

            }

        }

        return items;

    }

    @Override

    public boolean checkItemAvailability(String itemId) throws SQLException {

        String query = "SELECT COUNT(*) FROM item WHERE item_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, itemId);

            try (ResultSet rs = stmt.executeQuery()) {

                if(rs.next()) {

                    return rs.getInt(1) > 0;

                }

            }

        }

        return false;

    }

}
