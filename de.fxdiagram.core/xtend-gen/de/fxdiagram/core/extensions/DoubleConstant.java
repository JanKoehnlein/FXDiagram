package de.fxdiagram.core.extensions;

import javafx.beans.binding.DoubleBinding;

@SuppressWarnings("all")
public class DoubleConstant extends DoubleBinding {
  private double value;
  
  public DoubleConstant(final double value) {
    this.value = value;
  }
  
  @Override
  protected double computeValue() {
    return this.value;
  }
}
