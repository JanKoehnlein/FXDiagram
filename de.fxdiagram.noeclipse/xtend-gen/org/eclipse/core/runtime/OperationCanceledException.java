package org.eclipse.core.runtime;

@SuppressWarnings("all")
public class OperationCanceledException extends RuntimeException {
  public OperationCanceledException(final String message) {
    super(message);
  }
  
  public OperationCanceledException() {
  }
}
