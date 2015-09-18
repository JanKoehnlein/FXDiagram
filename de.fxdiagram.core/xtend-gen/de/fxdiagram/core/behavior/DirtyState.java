package de.fxdiagram.core.behavior;

/**
 * Describes whether a shape is in sync with its domain model.
 */
@SuppressWarnings("all")
public enum DirtyState {
  CLEAN,
  
  DIRTY,
  
  DANGLING;
}
