package org.example.motoproject.repos;

import org.example.motoproject.domain.Participant;
import org.example.motoproject.domain.Team;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TeamRepo implements TeamRepoInterface {
    private final String url;
    private static final Logger logger = LogManager.getLogger(TeamRepo.class);

    public TeamRepo(String url) {
        this.url = url;
    }

    @Override
    public void save(Team team) {
        String query = "INSERT INTO teams (name) VALUES (?)";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            logger.info("Saving team: {}", team);
            stmt.setString(1, team.getName());
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error connecting: {}", e.getMessage(), e);
        }
    }

    @Override
    public Optional<Team> findById(Long id) {
        String query1 = "SELECT * FROM teams WHERE id = ?";
        String query2 = "SELECT * form teams_participants WHERE team_id = ?";

        Long teamId = null;
        String teamName = null;
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(query1)) {
            logger.info("Finding team by id: {}", id);
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                teamId = rs.getLong("id");
                teamName = rs.getString("name");
            }
        } catch (SQLException e) {
            logger.error("Error connecting: {}", e.getMessage(), e);
        }

        if (teamId == null) {
            logger.info("Team not found: {}", id);
            return Optional.empty();
        }

        List<Long> memberIds = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(query2)) {
            logger.info("Finding members by id: {}", id);
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Long memberId = rs.getLong("participant_id");
                memberIds.add(memberId);
            }
        } catch (SQLException e) {
            logger.error("Error connecting: {}", e.getMessage(), e);
        }

        List<Participant> members = new ArrayList<>();
        String placeholders = String.join(",", Collections.nCopies(memberIds.size(), "?"));
        String query3 = "SELECT * FROM participants WHERE id IN (" + placeholders + ")";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(query3)) {
            logger.info("Finding members in list");
            for (int i = 0; i < memberIds.size(); i++) {
                stmt.setLong(i + 1, memberIds.get(i));
            }
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Long memberId = rs.getLong("id");
                String memberName = rs.getString("name");
                String memberIdNumber = rs.getString("id_number");
                Long memberEngineSize = rs.getLong("engine_size");
                members.add(new Participant(memberId, memberName, memberIdNumber, memberEngineSize));
            }
        } catch (SQLException e) {
            logger.error("Error connecting: {}", e.getMessage(), e);
        }
        Team team = new Team(teamId, teamName, members);
        return Optional.of(team);
    }

    @Override
    public List<Team> findAll() {
        String query = "SELECT * FROM teams";
        List<Team> teams = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            logger.info("Finding all teams");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Long teamId = rs.getLong("id");
                findById(teamId).ifPresent(teams::add);
            }
        } catch (SQLException e) {
            logger.error("Error connecting: {}", e.getMessage(), e);
        }
        return teams;
    }
}
