package de.itemis.javafx.diagram.tools;

import de.itemis.javafx.diagram.tools.XDiagramTool;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.Functions.Function2;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;

@SuppressWarnings("all")
public class CompositeTool implements XDiagramTool {
  private List<XDiagramTool> children = new Function0<List<XDiagramTool>>() {
    public List<XDiagramTool> apply() {
      ArrayList<XDiagramTool> _newArrayList = CollectionLiterals.<XDiagramTool>newArrayList();
      return _newArrayList;
    }
  }.apply();
  
  public boolean operator_add(final XDiagramTool child) {
    boolean _add = this.children.add(child);
    return _add;
  }
  
  public boolean activate() {
    final Function1<XDiagramTool,Boolean> _function = new Function1<XDiagramTool,Boolean>() {
        public Boolean apply(final XDiagramTool it) {
          boolean _activate = it.activate();
          return Boolean.valueOf(_activate);
        }
      };
    List<Boolean> _map = ListExtensions.<XDiagramTool, Boolean>map(this.children, _function);
    final Function2<Boolean,Boolean,Boolean> _function_1 = new Function2<Boolean,Boolean,Boolean>() {
        public Boolean apply(final Boolean a, final Boolean b) {
          boolean _or = false;
          if ((a).booleanValue()) {
            _or = true;
          } else {
            _or = ((a).booleanValue() || (b).booleanValue());
          }
          return Boolean.valueOf(_or);
        }
      };
    Boolean _reduce = IterableExtensions.<Boolean>reduce(_map, _function_1);
    return (_reduce).booleanValue();
  }
  
  public boolean deactivate() {
    final Function1<XDiagramTool,Boolean> _function = new Function1<XDiagramTool,Boolean>() {
        public Boolean apply(final XDiagramTool it) {
          boolean _deactivate = it.deactivate();
          return Boolean.valueOf(_deactivate);
        }
      };
    List<Boolean> _map = ListExtensions.<XDiagramTool, Boolean>map(this.children, _function);
    final Function2<Boolean,Boolean,Boolean> _function_1 = new Function2<Boolean,Boolean,Boolean>() {
        public Boolean apply(final Boolean a, final Boolean b) {
          boolean _or = false;
          if ((a).booleanValue()) {
            _or = true;
          } else {
            _or = ((a).booleanValue() || (b).booleanValue());
          }
          return Boolean.valueOf(_or);
        }
      };
    Boolean _reduce = IterableExtensions.<Boolean>reduce(_map, _function_1);
    return (_reduce).booleanValue();
  }
}
