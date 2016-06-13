package de.fxdiagram.mapping.behavior;

import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.behavior.AbstractHostBehavior;
import de.fxdiagram.core.behavior.Behavior;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.tools.actions.DiagramActionRegistry;
import de.fxdiagram.mapping.behavior.ConnectAllAction;

@SuppressWarnings("all")
public class ConnectAllBehavior<T extends Object> extends AbstractHostBehavior<XDiagram> {
  public ConnectAllBehavior(final XDiagram host) {
    super(host);
  }
  
  @Override
  protected void doActivate() {
    XDiagram _host = this.getHost();
    XRoot _root = CoreExtensions.getRoot(_host);
    DiagramActionRegistry _diagramActionRegistry = _root.getDiagramActionRegistry();
    ConnectAllAction<Object> _connectAllAction = new ConnectAllAction<Object>();
    _diagramActionRegistry.operator_add(_connectAllAction);
  }
  
  @Override
  public Class<? extends Behavior> getBehaviorKey() {
    return ConnectAllBehavior.class;
  }
}
