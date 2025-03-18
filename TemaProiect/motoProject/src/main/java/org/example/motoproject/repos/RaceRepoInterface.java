package org.example.motoproject.repos;

import org.example.motoproject.domain.Participant;
import org.example.motoproject.domain.Race;

import java.util.List;
import java.util.Optional;

public interface RaceRepoInterface extends RepoInterface<Race, Long> {
    @Override
    void save(Race entity);

    @Override
    Optional<Race> findById(Long id);

    @Override
    List<Race> findAll();

    void signUpParticipant(Participant participant, Race race);
}

