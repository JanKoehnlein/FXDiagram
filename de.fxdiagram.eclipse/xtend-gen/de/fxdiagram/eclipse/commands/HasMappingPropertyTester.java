package de.fxdiagram.eclipse.commands;

import com.google.common.base.Objects;
import de.fxdiagram.eclipse.selection.ISelectionExtractor;
import de.fxdiagram.mapping.XDiagramConfig;
import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class HasMappingPropertyTester extends PropertyTester {
  @Override
  public boolean test(final Object receiver, final String property, final Object[] args, final Object expectedValue) {
    final IWorkbenchPart activePart = ((IWorkbenchPart) receiver);
    final ISelectionExtractor.Acceptor acceptor = new ISelectionExtractor.Acceptor() {
      @Override
      public boolean accept(final Object selectedElement) {
        boolean _notEquals = (!Objects.equal(selectedElement, null));
        if (_notEquals) {
          final Function1<XDiagramConfig, Boolean> _function = (XDiagramConfig it) -> {
            boolean _isEmpty = IterableExtensions.isEmpty(it.<Object>getEntryCalls(selectedElement));
            return Boolean.valueOf((!_isEmpty));
          };
          final boolean hasMapping = IterableExtensions.exists(XDiagramConfig.Registry.getInstance().getConfigurations(), _function);
          return hasMapping;
        }
        return false;
      }
    };
    final Function1<ISelectionExtractor, Boolean> _function = (ISelectionExtractor it) -> {
      return Boolean.valueOf(it.addSelectedElement(activePart, acceptor));
    };
    return IterableExtensions.<ISelectionExtractor>exists(ISelectionExtractor.Registry.getInstance().getSelectionExtractors(), _function);
  }
}
