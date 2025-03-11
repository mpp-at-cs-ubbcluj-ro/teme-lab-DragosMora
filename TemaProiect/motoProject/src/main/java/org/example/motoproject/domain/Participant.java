package org.example.motoproject.domain;

public class Participant extends Entity<Long>{
    private String name;
    private String idNumber;
    private Long engineSize;

    public Participant(Long id, String name, String idNumber, Long engineSize) {
        super(id);
        this.name = name;
        this.idNumber = idNumber;
        this.engineSize = engineSize;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public Long getEngineSize() {
        return engineSize;
    }

    public void setEngineSize(Long engineSize) {
        this.engineSize = engineSize;
    }
}
