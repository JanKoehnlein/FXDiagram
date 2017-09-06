package de.fxdiagram.lib.simple;

import de.fxdiagram.core.command.AnimationCommand;
import de.fxdiagram.core.command.CommandContext;
import de.fxdiagram.lib.simple.OpenableDiagramNode;
import javafx.animation.Animation;

@SuppressWarnings("all")
public class CloseDiagramCommand implements AnimationCommand {
  private OpenableDiagramNode host;
  
  public CloseDiagramCommand(final OpenableDiagramNode host) {
    this.host = host;
  }
  
  @Override
  public Animation getExecuteAnimation(final CommandContext context) {
    return this.host.closeDiagram(context.getDefaultExecuteDuration());
  }
  
  @Override
  public Animation getUndoAnimation(final CommandContext context) {
    return this.host.openDiagram(context.getDefaultUndoDuration());
  }
  
  @Override
  public Animation getRedoAnimation(final CommandContext context) {
    return this.host.closeDiagram(context.getDefaultUndoDuration());
  }
  
  @Override
  public boolean clearRedoStackOnExecute() {
    return true;
  }
  
  @Override
  public void skipViewportRestore() {
  }
}
