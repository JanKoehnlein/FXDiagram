package de.fxdiagram.xtext.glue.mapping;

import de.fxdiagram.annotations.logging.Logging;
import de.fxdiagram.xtext.glue.mapping.AbstractDiagramConfig;
import de.fxdiagram.xtext.glue.mapping.XDiagramConfig;
import java.util.Map;
import java.util.logging.Logger;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@Logging
@SuppressWarnings("all")
public class XDiagramConfigRegistry {
  private static XDiagramConfigRegistry instance;
  
  private Map<String, XDiagramConfig> configs = CollectionLiterals.<String, XDiagramConfig>newHashMap();
  
  public static XDiagramConfigRegistry getInstance() {
    XDiagramConfigRegistry _elvis = null;
    if (XDiagramConfigRegistry.instance != null) {
      _elvis = XDiagramConfigRegistry.instance;
    } else {
      XDiagramConfigRegistry _xDiagramConfigRegistry = new XDiagramConfigRegistry();
      XDiagramConfigRegistry _instance = XDiagramConfigRegistry.instance = _xDiagramConfigRegistry;
      _elvis = _instance;
    }
    return _elvis;
  }
  
  private XDiagramConfigRegistry() {
    this.addStaticConfigurations();
  }
  
  public Iterable<? extends XDiagramConfig> getConfigurations() {
    return this.configs.values();
  }
  
  protected void addStaticConfigurations() {
    IExtensionRegistry _extensionRegistry = Platform.getExtensionRegistry();
    IConfigurationElement[] _configurationElementsFor = _extensionRegistry.getConfigurationElementsFor("de.fxdiagram.xtext.glue.fxDiagramConfig");
    final Procedure1<IConfigurationElement> _function = new Procedure1<IConfigurationElement>() {
      public void apply(final IConfigurationElement it) {
        try {
          Object _createExecutableExtension = it.createExecutableExtension("class");
          final AbstractDiagramConfig config = ((AbstractDiagramConfig) _createExecutableExtension);
          final String id = it.getAttribute("id");
          config.setID(id);
          boolean _containsKey = XDiagramConfigRegistry.this.configs.containsKey(id);
          if (_containsKey) {
            XDiagramConfigRegistry.LOG.severe(("Duplicate fxDiagramConfig id=" + id));
          } else {
            XDiagramConfigRegistry.this.configs.put(id, config);
          }
        } catch (Throwable _e) {
          throw Exceptions.sneakyThrow(_e);
        }
      }
    };
    IterableExtensions.<IConfigurationElement>forEach(((Iterable<IConfigurationElement>)Conversions.doWrapArray(_configurationElementsFor)), _function);
  }
  
  public XDiagramConfig getConfigByID(final String configID) {
    return this.configs.get(configID);
  }
  
  private static Logger LOG = Logger.getLogger("de.fxdiagram.xtext.glue.mapping.XDiagramConfigRegistry");
    ;
}
