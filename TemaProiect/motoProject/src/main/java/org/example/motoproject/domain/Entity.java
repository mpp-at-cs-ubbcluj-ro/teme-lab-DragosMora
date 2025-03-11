package org.example.motoproject.domain;

public class Entity<ID> {
    private final ID id;

    public Entity(ID entityId) {
        this.id = entityId;
    }

    public ID getId() {
        return id;
    }
}