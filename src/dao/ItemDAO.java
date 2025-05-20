package dao;

import models.Item;

import java.sql.SQLException;

import java.util.List;

public interface ItemDAO {

    void addItem(Item item) throws SQLException;

    Item getItemById(String itemId) throws SQLException;

    List<Item> getAllItems() throws SQLException;

    boolean checkItemAvailability(String itemId) throws SQLException;

}
