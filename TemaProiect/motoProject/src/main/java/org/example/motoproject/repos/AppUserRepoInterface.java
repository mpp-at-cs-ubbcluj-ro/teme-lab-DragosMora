package org.example.motoproject.repos;

import java.util.Optional;

public interface AppUserRepoInterface {
    Optional<String> findPasswordByUsername(String username);
}
