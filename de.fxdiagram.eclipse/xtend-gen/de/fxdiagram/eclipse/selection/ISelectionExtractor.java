package de.fxdiagram.eclipse.selection;

import java.util.List;
import java.util.function.Consumer;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;

@SuppressWarnings("all")
public interface ISelectionExtractor {
  public interface Acceptor {
    public abstract boolean accept(final Object selectedElement);
  }
  
  public static class Registry {
    private final static Logger LOG = Logger.getLogger(ISelectionExtractor.Registry.class);
    
    private static ISelectionExtractor.Registry INSTANCE;
    
    private List<ISelectionExtractor> extractors = CollectionLiterals.<ISelectionExtractor>newArrayList();
    
    private Registry() {
      final Consumer<IConfigurationElement> _function = (IConfigurationElement it) -> {
        try {
          Object _createExecutableExtension = it.createExecutableExtension("class");
          final ISelectionExtractor extractor = ((ISelectionExtractor) _createExecutableExtension);
          this.extractors.add(extractor);
        } catch (final Throwable _t) {
          if (_t instanceof Exception) {
            final Exception exc = (Exception)_t;
            ISelectionExtractor.Registry.LOG.error(exc);
          } else {
            throw Exceptions.sneakyThrow(_t);
          }
        }
      };
      ((List<IConfigurationElement>)Conversions.doWrapArray(Platform.getExtensionRegistry().getConfigurationElementsFor("de.fxdiagram.eclipse.selectionExtractor"))).forEach(_function);
    }
    
    public static ISelectionExtractor.Registry getInstance() {
      ISelectionExtractor.Registry _elvis = null;
      if (ISelectionExtractor.Registry.INSTANCE != null) {
        _elvis = ISelectionExtractor.Registry.INSTANCE;
      } else {
        ISelectionExtractor.Registry _registry = new ISelectionExtractor.Registry();
        ISelectionExtractor.Registry _INSTANCE = (ISelectionExtractor.Registry.INSTANCE = _registry);
        _elvis = _INSTANCE;
      }
      return _elvis;
    }
    
    public Iterable<ISelectionExtractor> getSelectionExtractors() {
      return this.extractors;
    }
  }
  
  public abstract boolean addSelectedElement(final IWorkbenchPart activePart, final ISelectionExtractor.Acceptor acceptor);
}
