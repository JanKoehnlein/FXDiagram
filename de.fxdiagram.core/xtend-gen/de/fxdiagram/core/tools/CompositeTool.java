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
  
  @Override
  public boolean activate() {
    final Function1<XDiagramTool, Boolean> _function = (XDiagramTool it) -> {
      return Boolean.valueOf(it.activate());
    };
    List<Boolean> _map = ListExtensions.<XDiagramTool, Boolean>map(this.children, _function);
    final Function2<Boolean, Boolean, Boolean> _function_1 = (Boolean a, Boolean b) -> {
      return Boolean.valueOf(((a).booleanValue() || (b).booleanValue()));
    };
    return (boolean) IterableExtensions.<Boolean>reduce(_map, _function_1);
  }
  
  @Override
  public boolean deactivate() {
    final Function1<XDiagramTool, Boolean> _function = (XDiagramTool it) -> {
      return Boolean.valueOf(it.deactivate());
    };
    List<Boolean> _map = ListExtensions.<XDiagramTool, Boolean>map(this.children, _function);
    final Function2<Boolean, Boolean, Boolean> _function_1 = (Boolean a, Boolean b) -> {
      return Boolean.valueOf(((a).booleanValue() || (b).booleanValue()));
    };
    return (boolean) IterableExtensions.<Boolean>reduce(_map, _function_1);
  }
}
