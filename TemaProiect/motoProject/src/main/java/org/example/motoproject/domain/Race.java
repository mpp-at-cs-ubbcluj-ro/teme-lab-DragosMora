package org.example.motoproject.domain;

import java.util.ArrayList;
import java.util.List;

public class Race extends Entity<Long>{
    private String name;
    private Long engineSize;
    private List<Participant> participants = new ArrayList<>();

    public Race(Long id, Long engineSize, Long participantNumber) {
        super(id);
        this.engineSize = engineSize;
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
}
