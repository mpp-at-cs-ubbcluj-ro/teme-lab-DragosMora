package org.example.motoproject.repos;

import java.sql.*;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AppUserRepo implements AppUserRepoInterface {
    private final String url;
    private static final Logger logger = LogManager.getLogger(AppUserRepo.class);

    AppUserRepo(String url) {
        this.url = url;
    }

    @Override
    public Optional<String> findPasswordByUsername(String username) {
        String query = "SELECT password FROM users WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            logger.info("Successfully connected to the database!");
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                logger.info("Password found!");
                return Optional.of(rs.getString("password"));
            }
        } catch (SQLException e) {
            logger.error("Error connecting: {}", e.getMessage(), e);
            return Optional.empty();
        }
        return Optional.empty();
    }
}
