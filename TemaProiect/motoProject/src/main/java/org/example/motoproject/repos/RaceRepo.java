package org.example.motoproject.repos;

import org.example.motoproject.domain.Participant;
import org.example.motoproject.domain.Race;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RaceRepo implements RaceRepoInterface {
    private final String url;
    private static final Logger logger = LogManager.getLogger(RaceRepo.class);

    RaceRepo(String url) {
        this.url = url;
    }

    @Override
    public void save(Race race) {
        String query = "INSERT INTO races (name, engine_size, date) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            logger.info("Saving race: {}", race);
            stmt.setString(1, race.getName());
            stmt.setLong(2, race.getEngineSize());
            stmt.setString(3, race.getDate());
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error connecting: {}", e.getMessage(), e);
        }
    }

    @Override
    public Optional<Race> findById(Long id) {
        String query1 = "SELECT * FROM races WHERE id = ?";
        String query2 = "SELECT * FROM races_participants WHERE race_id = ?";

        Long raceId = null, engineSize = null;
        String name = null, date = null;
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(query1)) {
            logger.info("Finding race by id: {}", id);
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                raceId = rs.getLong("id");
                name = rs.getString("name");
                engineSize = rs.getLong("engine_size");
                date = rs.getString("date");
            }
        } catch (SQLException e) {
            logger.error("Error connecting: {}", e.getMessage(), e);
        }

        if (raceId == null) {
            logger.info("Race not found: {}", id);
            return Optional.empty();
        }

        List<Long> participantIds = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(query2)) {
            logger.info("Finding participants by race id: {}", id);
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Long participantId = rs.getLong("participant_id");
                participantIds.add(participantId);
            }
        } catch (SQLException e) {
            logger.error("Error connecting: {}", e.getMessage(), e);
        }
        List<Participant> participants = new ArrayList<>();
        String placeholders = String.join(",", Collections.nCopies(participantIds.size(), "?"));
        String query3 = "SELECT * FROM participants WHERE id IN (" + placeholders + ")";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(query3)) {
            logger.info("Finding participants in list!");
            for (int i = 0; i < participantIds.size(); i++) {
                stmt.setLong(i + 1, participantIds.get(i));
            }
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Long participantId = rs.getLong("id");
                String participantName = rs.getString("name");
                String participantIdNumber = rs.getString("id_number");
                Long participantEngineSize = rs.getLong("engine_size");
                participants.add(new Participant(participantId, participantName, participantIdNumber, participantEngineSize));
            }
        } catch (SQLException e) {
            logger.error("Error connecting: {}", e.getMessage(), e);
        }
        Race race = new Race(raceId, name, engineSize, date, participants);
        return Optional.of(race);
    }

    @Override
    public List<Race> findAll() {
        String query = "SELECT * FROM races";
        List<Race> races = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            logger.info("Finding all races!");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Long raceId = rs.getLong("id");
                findById(raceId).ifPresent(races::add);
            }
        } catch (SQLException e) {
            logger.error("Error connecting: {}", e.getMessage(), e);
        }
        return races;
    }

    @Override
    public void signUpParticipant(Participant participant, Race race) {
        String query = "INSERT INTO races_participants (race_id, participant_id) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            logger.info("Signing up participant: {}", participant);
            stmt.setLong(1, race.getId());
            stmt.setLong(2, participant.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error connecting: {}", e.getMessage(), e);
        }
    }
}
