package com.example.user;

import com.example.db.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/** All SQL for the users table. Every query uses parameters (no SQL injection). */
public class UserRepository {

    private static final String UNIQUE_VIOLATION = "23505";

    /** Saves a new user and returns it. */
    public User create(String fullName, String mobile, String email, String passwordHash)
            throws SQLException, DuplicateEmailException {
        String sql = "INSERT INTO users (full_name, mobile, email, password_hash) VALUES (?, ?, ?, ?)";
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, fullName);
            ps.setString(2, mobile);
            ps.setString(3, email);
            ps.setString(4, passwordHash);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                keys.next();
                long id = keys.getLong(1);
                return findById(id);
            }
        } catch (SQLException e) {
            if (UNIQUE_VIOLATION.equals(e.getSQLState())) {
                throw new DuplicateEmailException();
            }
            throw e;
        }
    }

    public User findById(long id) throws SQLException {
        String sql = "SELECT id, full_name, mobile, email, created_at FROM users WHERE id = ?";
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    /** Returns the stored password hash for an email, or null if there is no such user. */
    public Credentials findCredentials(String email) throws SQLException {
        String sql = "SELECT id, password_hash FROM users WHERE email = ?";
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? new Credentials(rs.getLong(1), rs.getString(2)) : null;
            }
        }
    }

    public String findPasswordHash(long id) throws SQLException {
        String sql = "SELECT password_hash FROM users WHERE id = ?";
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getString(1) : null;
            }
        }
    }

    public void delete(long id) throws SQLException {
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM users WHERE id = ?")) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    public long count() throws SQLException {
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT COUNT(*) FROM users");
             ResultSet rs = ps.executeQuery()) {
            rs.next();
            return rs.getLong(1);
        }
    }

    /** The most recent sign-ups, newest first. */
    public List<User> recent(int limit) throws SQLException {
        String sql = "SELECT id, full_name, mobile, email, created_at FROM users ORDER BY created_at DESC, id DESC LIMIT ?";
        List<User> users = new ArrayList<>();
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    users.add(map(rs));
                }
            }
        }
        return users;
    }

    /** Simple health check: can we reach the database? */
    public void ping() throws SQLException {
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT 1");
             ResultSet rs = ps.executeQuery()) {
            rs.next();
        }
    }

    private static User map(ResultSet rs) throws SQLException {
        return new User(rs.getLong("id"), rs.getString("full_name"), rs.getString("mobile"),
            rs.getString("email"), rs.getTimestamp("created_at").toInstant());
    }

    /** User id plus stored hash, used only during login. */
    public static final class Credentials {
        private final long id;
        private final String passwordHash;

        public Credentials(long id, String passwordHash) {
            this.id = id;
            this.passwordHash = passwordHash;
        }

        public long getId() { return id; }
        public String getPasswordHash() { return passwordHash; }
    }
}
