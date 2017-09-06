package de.fxdiagram.eclipse;

import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.command.AbstractCommand;
import de.fxdiagram.core.command.CommandContext;

@SuppressWarnings("all")
public class ClearDiagramCommand extends AbstractCommand {
  private XDiagram newDiagram;
  
  private XDiagram oldRootDiagram;
  
  private XDiagram oldDiagram;
  
  @Override
  public void execute(final CommandContext context) {
    this.oldDiagram = context.getRoot().getDiagram();
    this.oldRootDiagram = context.getRoot().getRootDiagram();
    XRoot _root = context.getRoot();
    XDiagram _xDiagram = new XDiagram();
    XDiagram _newDiagram = (this.newDiagram = _xDiagram);
    _root.setRootDiagram(_newDiagram);
  }
  
  @Override
  public void undo(final CommandContext context) {
    XRoot _root = context.getRoot();
    _root.setRootDiagram(this.oldRootDiagram);
    XRoot _root_1 = context.getRoot();
    _root_1.setDiagram(this.oldDiagram);
  }
  
  @Override
  public void redo(final CommandContext context) {
    XRoot _root = context.getRoot();
    _root.setRootDiagram(this.newDiagram);
    XRoot _root_1 = context.getRoot();
    _root_1.setDiagram(this.newDiagram);
  }
}
