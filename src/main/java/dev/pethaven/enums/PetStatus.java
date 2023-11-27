package dev.pethaven.enums;

public enum PetStatus {
    ACTIVE, FREEZE, ADOPTED;

    public boolean canTransitionTo(PetStatus newStatus) {
        switch (this) {
            case ACTIVE:
                return newStatus == FREEZE;
            case FREEZE:
                return newStatus == ACTIVE || newStatus == ADOPTED;
            default:
                return false;
        }
    }
}
