package org.example.motoproject.domain;

import java.util.ArrayList;
import java.util.List;

public class Team extends Entity<Long> {
    private String name;
    private List<Participant> teamMembers = new ArrayList<>();

    public Team(Long id, String name, List<Participant> teamMembers) {
        super(id);
        this.name = name;
        this.teamMembers = teamMembers;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Participant> getTeamMembers() {
        return teamMembers;
    }

    public void setTeamMembers(List<Participant> teamMembers) {
        this.teamMembers = teamMembers;
    }
}
