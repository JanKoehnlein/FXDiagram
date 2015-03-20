package de.fxdiagram.eclipse.commands;

import com.google.common.base.Objects;
import de.fxdiagram.eclipse.commands.ISelectionExtractor;
import de.fxdiagram.eclipse.mapping.MappingCall;
import de.fxdiagram.eclipse.mapping.XDiagramConfig;
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
          XDiagramConfig.Registry _instance = XDiagramConfig.Registry.getInstance();
          Iterable<? extends XDiagramConfig> _configurations = _instance.getConfigurations();
          final Function1<XDiagramConfig, Boolean> _function = new Function1<XDiagramConfig, Boolean>() {
            @Override
            public Boolean apply(final XDiagramConfig it) {
              Iterable<? extends MappingCall<?, Object>> _entryCalls = it.<Object>getEntryCalls(selectedElement);
              boolean _isEmpty = IterableExtensions.isEmpty(_entryCalls);
              return Boolean.valueOf((!_isEmpty));
            }
          };
          final boolean hasMapping = IterableExtensions.exists(_configurations, _function);
          return hasMapping;
        }
        return false;
      }
    };
    ISelectionExtractor.Registry _instance = ISelectionExtractor.Registry.getInstance();
    Iterable<ISelectionExtractor> _selectionExtractors = _instance.getSelectionExtractors();
    final Function1<ISelectionExtractor, Boolean> _function = new Function1<ISelectionExtractor, Boolean>() {
      @Override
      public Boolean apply(final ISelectionExtractor it) {
        return Boolean.valueOf(it.addSelectedElement(activePart, acceptor));
      }
    };
    return IterableExtensions.<ISelectionExtractor>exists(_selectionExtractors, _function);
  }
}
