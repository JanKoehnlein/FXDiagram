package de.fxdiagram.core.layout;

import com.google.common.base.Objects;
import com.google.inject.Guice;
import com.google.inject.Injector;
import de.cau.cs.kieler.kiml.graphviz.dot.GraphvizDotRuntimeModule;
import de.cau.cs.kieler.kiml.graphviz.dot.GraphvizDotStandaloneSetup;
import de.cau.cs.kieler.kiml.graphviz.dot.transform.DotHandler;
import de.cau.cs.kieler.kiml.service.TransformationService;
import de.cau.cs.kieler.kiml.service.formats.GraphFormatData;
import de.fxdiagram.annotations.logging.Logging;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

/**
 * KIELER's Graphviz layouts rely on the TransformationService to be initialized.
 */
@Logging
@SuppressWarnings("all")
public class LoggingTransformationService extends TransformationService {
  private GraphFormatData dotFormatData;
  
  public GraphFormatData getFormatData(final String id) {
    GraphFormatData _xifexpression = null;
    boolean _equals = Objects.equal(id, DotHandler.ID);
    if (_equals) {
      GraphFormatData _xblockexpression = null;
      {
        boolean _equals_1 = Objects.equal(this.dotFormatData, null);
        if (_equals_1) {
          GraphvizDotStandaloneSetup.doSetup();
          GraphvizDotRuntimeModule _graphvizDotRuntimeModule = new GraphvizDotRuntimeModule();
          final GraphvizDotRuntimeModule graphvizDotRuntimeModule = _graphvizDotRuntimeModule;
          final Injector injector = Guice.createInjector(graphvizDotRuntimeModule);
          GraphFormatData _graphFormatData = new GraphFormatData();
          final Procedure1<GraphFormatData> _function = new Procedure1<GraphFormatData>() {
              public void apply(final GraphFormatData it) {
                DotHandler _instance = injector.<DotHandler>getInstance(DotHandler.class);
                it.setHandler(_instance);
              }
            };
          GraphFormatData _doubleArrow = ObjectExtensions.<GraphFormatData>operator_doubleArrow(_graphFormatData, _function);
          this.dotFormatData = _doubleArrow;
        }
        _xblockexpression = (this.dotFormatData);
      }
      _xifexpression = _xblockexpression;
    } else {
      GraphFormatData _formatData = super.getFormatData(id);
      _xifexpression = _formatData;
    }
    return _xifexpression;
  }
  
  protected void reportError(final String extensionPoint, final IConfigurationElement element, final String attribute, final Throwable exception) {
    LoggingTransformationService.LOG.log(Level.SEVERE, "Error in LoggingTransformationService", exception);
  }
  
  protected void reportError(final CoreException exception) {
    LoggingTransformationService.LOG.log(Level.SEVERE, "Error in LoggingTransformationService", exception);
  }
  
  private static Logger LOG = Logger.getLogger("de.fxdiagram.core.layout.LoggingTransformationService");
    ;
}
