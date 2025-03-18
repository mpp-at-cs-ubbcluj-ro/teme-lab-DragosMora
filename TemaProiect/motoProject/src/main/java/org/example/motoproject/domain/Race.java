package org.example.motoproject.domain;

import java.util.ArrayList;
import java.util.List;

public class Race extends Entity<Long>{
    private String name;
    private Long engineSize;
    private String date;
    private List<Participant> participants = new ArrayList<>();

    public Race(Long id, String name, Long engineSize, String date, List<Participant> participants) {
        super(id);
        this.name = name;
        this.engineSize = engineSize;
        this.date = date;
        this.participants = participants;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getEngineSize() {
        return engineSize;
    }

    public void setEngineSize(Long engineSize) {
        this.engineSize = engineSize;
    }

    public String getDate() { return date; }

    public void setDate(String date) { this.date = date; }

    public List<Participant> getParticipants() { return participants; }

    public void setParticipants(List<Participant> participants) { this.participants = participants; }
}
