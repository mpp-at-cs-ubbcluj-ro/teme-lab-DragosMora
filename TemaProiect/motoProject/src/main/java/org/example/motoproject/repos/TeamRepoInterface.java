package org.example.motoproject.repos;

import org.example.motoproject.domain.Team;

import java.util.List;
import java.util.Optional;

public interface TeamRepoInterface extends RepoInterface<Team, Long> {
    @Override
    void save(Team entity);

    @Override
    Optional<Team> findById(Long id);

    @Override
    List<Team> findAll();
}
