package de.fxdiagram.core.binding;

import javafx.beans.binding.DoubleBinding;

@SuppressWarnings("all")
public class DoubleConstant extends DoubleBinding {
  private double value;
  
  public DoubleConstant(final double value) {
    this.value = value;
  }
  
  protected double computeValue() {
    return this.value;
  }
}
