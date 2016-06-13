package de.fxdiagram.mapping.behavior;

import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.command.CommandStack;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.core.tools.actions.DiagramAction;
import de.fxdiagram.mapping.AbstractMapping;
import de.fxdiagram.mapping.AbstractNodeMappingCall;
import de.fxdiagram.mapping.ConnectionMapping;
import de.fxdiagram.mapping.DiagramMapping;
import de.fxdiagram.mapping.IMappedElementDescriptor;
import de.fxdiagram.mapping.XDiagramConfig;
import de.fxdiagram.mapping.execution.InterpreterContext;
import de.fxdiagram.mapping.execution.XDiagramConfigInterpreter;
import eu.hansolo.enzo.radialmenu.SymbolType;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import javafx.scene.input.KeyEvent;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class ConnectAllAction<T extends Object> implements DiagramAction {
  private final XDiagramConfigInterpreter configInterpreter = new XDiagramConfigInterpreter();
  
  @Override
  public boolean matches(final KeyEvent event) {
    return false;
  }
  
  @Override
  public SymbolType getSymbol() {
    return SymbolType.REFRESH;
  }
  
  @Override
  public String getTooltip() {
    return "Connect all nodes";
  }
  
  @Override
  public void perform(final XRoot root) {
    XDiagram _diagram = root.getDiagram();
    final DomainObjectDescriptor diagramDO = _diagram.getDomainObjectDescriptor();
    if ((diagramDO instanceof IMappedElementDescriptor<?>)) {
      final Function1<T, Object> _function = (T diagramObject) -> {
        Object _xblockexpression = null;
        {
          AbstractMapping<?> _mapping = ((IMappedElementDescriptor<?>)diagramDO).getMapping();
          final DiagramMapping<T> diagramMapping = ((DiagramMapping<T>) _mapping);
          XDiagramConfig _config = diagramMapping.getConfig();
          Iterable<? extends AbstractMapping<?>> _mappings = _config.getMappings();
          final Function1<AbstractMapping<?>, Boolean> _function_1 = (AbstractMapping<?> it) -> {
            return Boolean.valueOf((it instanceof ConnectionMapping<?>));
          };
          Iterable<? extends AbstractMapping<?>> _filter = IterableExtensions.filter(_mappings, _function_1);
          final Set<ConnectionMapping<?>> allConnectionMappings = IterableExtensions.<ConnectionMapping<?>>toSet(((Iterable<ConnectionMapping<?>>) _filter));
          XDiagram _diagram_1 = root.getDiagram();
          final InterpreterContext context = new InterpreterContext(_diagram_1);
          List<AbstractNodeMappingCall<?, T>> _nodes = diagramMapping.getNodes();
          final Consumer<AbstractNodeMappingCall<?, T>> _function_2 = (AbstractNodeMappingCall<?, T> it) -> {
            this.configInterpreter.<T>connectNodesEagerly(it, diagramObject, allConnectionMappings, context);
          };
          _nodes.forEach(_function_2);
          CommandStack _commandStack = root.getCommandStack();
          context.executeCommands(_commandStack);
          _xblockexpression = null;
        }
        return _xblockexpression;
      };
      ((IMappedElementDescriptor<T>) diagramDO).<Object>withDomainObject(_function);
    }
  }
}
