package dao;

import models.User;

import java.sql.SQLException;

public interface UserDAO {

    void addUser(User user) throws SQLException;

    User getUserByUsername(String username) throws SQLException;

    boolean validateUser(String username, String password) throws SQLException;

}
