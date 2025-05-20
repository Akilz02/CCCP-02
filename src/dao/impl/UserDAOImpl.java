package dao.impl;

import dao.UserDAO;

import models.User;

import java.sql.*;

public class UserDAOImpl implements UserDAO {

    private Connection connection;

    public UserDAOImpl(Connection connection) {

        this.connection = connection;

    }

    @Override

    public void addUser(User user) throws SQLException {

        String query = "INSERT INTO user (username, password) VALUES (?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, user.getUsername());

            stmt.setString(2, user.getPassword());

            stmt.executeUpdate();

        }

    }

    @Override

    public User getUserByUsername(String username) throws SQLException {

        String query = "SELECT * FROM user WHERE username = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {

                if(rs.next()) {

                    return new User(rs.getString("user_id"), rs.getString("username"), rs.getString("password"));

                }

            }

        }

        return null;

    }

    @Override

    public boolean validateUser(String username, String password) throws SQLException {

        String query = "SELECT COUNT(*) FROM user WHERE username = ? AND password = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, username);

            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {

                if(rs.next()) {

                    return rs.getInt(1) > 0;

                }

            }

        }

        return false;

    }


}
