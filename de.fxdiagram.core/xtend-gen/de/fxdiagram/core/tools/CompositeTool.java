package de.fxdiagram.core.tools;

import de.fxdiagram.core.tools.XDiagramTool;
import java.util.List;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.Functions.Function2;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;

@SuppressWarnings("all")
public class CompositeTool implements XDiagramTool {
  private List<XDiagramTool> children = CollectionLiterals.<XDiagramTool>newArrayList();
  
  public boolean operator_add(final XDiagramTool child) {
    return this.children.add(child);
  }
  
  public boolean activate() {
    final Function1<XDiagramTool,Boolean> _function = new Function1<XDiagramTool,Boolean>() {
      public Boolean apply(final XDiagramTool it) {
        return Boolean.valueOf(it.activate());
      }
    };
    List<Boolean> _map = ListExtensions.<XDiagramTool, Boolean>map(this.children, _function);
    final Function2<Boolean,Boolean,Boolean> _function_1 = new Function2<Boolean,Boolean,Boolean>() {
      public Boolean apply(final Boolean a, final Boolean b) {
        boolean _or = false;
        if ((a).booleanValue()) {
          _or = true;
        } else {
          _or = (b).booleanValue();
        }
        return Boolean.valueOf(_or);
      }
    };
    return (IterableExtensions.<Boolean>reduce(_map, _function_1)).booleanValue();
  }
  
  public boolean deactivate() {
    final Function1<XDiagramTool,Boolean> _function = new Function1<XDiagramTool,Boolean>() {
      public Boolean apply(final XDiagramTool it) {
        return Boolean.valueOf(it.deactivate());
      }
    };
    List<Boolean> _map = ListExtensions.<XDiagramTool, Boolean>map(this.children, _function);
    final Function2<Boolean,Boolean,Boolean> _function_1 = new Function2<Boolean,Boolean,Boolean>() {
      public Boolean apply(final Boolean a, final Boolean b) {
        boolean _or = false;
        if ((a).booleanValue()) {
          _or = true;
        } else {
          _or = (b).booleanValue();
        }
        return Boolean.valueOf(_or);
      }
    };
    return (IterableExtensions.<Boolean>reduce(_map, _function_1)).booleanValue();
  }
}
