package dev.animals.enums;

/**
 * Статусы животного
 */
public enum AnimalStatus {

  ACTIVE, FREEZE, ADOPTED;

  public boolean canTransitionTo(AnimalStatus newStatus) {
    return switch (this) {
      case ACTIVE -> newStatus == FREEZE;
      case FREEZE ->  newStatus == ACTIVE || newStatus == ADOPTED;
      default -> false;
    };
  }
}
