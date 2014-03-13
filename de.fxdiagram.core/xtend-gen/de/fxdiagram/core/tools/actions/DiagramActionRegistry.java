package de.fxdiagram.core.tools.actions;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import de.fxdiagram.core.tools.actions.DiagramAction;
import eu.hansolo.enzo.radialmenu.Symbol;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class DiagramActionRegistry {
  private List<DiagramAction> actions = CollectionLiterals.<DiagramAction>newArrayList();
  
  private Map<Symbol.Type,DiagramAction> symbol2action = CollectionLiterals.<Symbol.Type, DiagramAction>newHashMap();
  
  public void operator_add(final Iterable<? extends DiagramAction> diagramActions) {
    final Procedure1<DiagramAction> _function = new Procedure1<DiagramAction>() {
      public void apply(final DiagramAction it) {
        DiagramActionRegistry.this.operator_add(it);
      }
    };
    IterableExtensions.forEach(diagramActions, _function);
  }
  
  public void operator_add(final DiagramAction diagramAction) {
    this.actions.add(diagramAction);
    Symbol.Type _symbol = diagramAction.getSymbol();
    boolean _notEquals = (!Objects.equal(_symbol, null));
    if (_notEquals) {
      Symbol.Type _symbol_1 = diagramAction.getSymbol();
      this.symbol2action.put(_symbol_1, diagramAction);
    }
  }
  
  public void operator_remove(final DiagramAction diagramAction) {
    this.actions.remove(diagramAction);
    Symbol.Type _symbol = diagramAction.getSymbol();
    boolean _notEquals = (!Objects.equal(_symbol, null));
    if (_notEquals) {
      Symbol.Type _symbol_1 = diagramAction.getSymbol();
      this.symbol2action.remove(_symbol_1);
    }
  }
  
  public DiagramAction getBySymbol(final Symbol.Type symbol) {
    return this.symbol2action.get(symbol);
  }
  
  public ArrayList<DiagramAction> getActions() {
    return Lists.<DiagramAction>newArrayList(this.actions);
  }
}
