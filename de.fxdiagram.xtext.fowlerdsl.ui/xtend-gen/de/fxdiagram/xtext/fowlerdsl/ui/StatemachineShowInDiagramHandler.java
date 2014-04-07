package de.fxdiagram.xtext.fowlerdsl.ui;

import de.fxdiagram.xtext.fowlerdsl.ui.StatemachineDiagramConfig;
import de.fxdiagram.xtext.glue.ShowInDiagramHandler;
import de.fxdiagram.xtext.glue.mapping.XDiagramConfig;

@SuppressWarnings("all")
public class StatemachineShowInDiagramHandler extends ShowInDiagramHandler {
  protected XDiagramConfig createDiagramConfig() {
    return new StatemachineDiagramConfig();
  }
}
