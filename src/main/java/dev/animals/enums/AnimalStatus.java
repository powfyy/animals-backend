package dev.animals.enums;

/**
 * Статусы животного
 */
public enum AnimalStatus {

  ACTIVE, FREEZE, ADOPTED;

  public boolean canTransitionTo(AnimalStatus newStatus) {
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
