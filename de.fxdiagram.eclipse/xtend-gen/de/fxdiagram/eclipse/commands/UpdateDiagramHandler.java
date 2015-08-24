package de.fxdiagram.eclipse.commands;

import com.google.common.collect.Iterables;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.XShape;
import de.fxdiagram.core.behavior.DirtyStateBehavior;
import de.fxdiagram.eclipse.FXDiagramView;
import java.util.ArrayList;
import java.util.function.Consumer;
import javafx.collections.ObservableList;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;

@SuppressWarnings("all")
public class UpdateDiagramHandler extends AbstractHandler {
  @Override
  public Object execute(final ExecutionEvent event) throws ExecutionException {
    Object _xblockexpression = null;
    {
      final IWorkbenchPart view = HandlerUtil.getActivePart(event);
      if ((view instanceof FXDiagramView)) {
        XRoot _currentRoot = ((FXDiagramView)view).getCurrentRoot();
        final XDiagram diagram = _currentRoot.getDiagram();
        final ArrayList<XShape> allShapes = CollectionLiterals.<XShape>newArrayList();
        ObservableList<XNode> _nodes = diagram.getNodes();
        Iterables.<XShape>addAll(allShapes, _nodes);
        ObservableList<XConnection> _connections = diagram.getConnections();
        Iterables.<XShape>addAll(allShapes, _connections);
        final Consumer<XShape> _function = (XShape it) -> {
          DirtyStateBehavior _behavior = it.<DirtyStateBehavior>getBehavior(DirtyStateBehavior.class);
          if (_behavior!=null) {
            _behavior.getDirtyState();
          }
        };
        allShapes.forEach(_function);
      }
      _xblockexpression = null;
    }
    return _xblockexpression;
  }
}
