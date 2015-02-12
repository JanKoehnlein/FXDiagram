package de.fxdiagram.eclipse.mapping;

import de.fxdiagram.annotations.logging.Logging;
import de.fxdiagram.eclipse.mapping.AbstractDiagramConfig;
import de.fxdiagram.eclipse.mapping.AbstractMapping;
import de.fxdiagram.eclipse.mapping.IMappedElementDescriptorProvider;
import de.fxdiagram.eclipse.mapping.MappingCall;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.logging.Logger;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;

/**
 * Stores a set of {@link AbstractMapping}s for a sepecific domain.
 * 
 * {@link XDiagramConfig}s must be registered to the {@link XDiagramConfig$Registry}
 * to be picked up by the runtime using the extension point
 * <code>de.fxdiagram.eclipse.fxDiagramConfig</code>.
 * 
 * Consider inheriting from {@link AbstractDiagramConfig} instead of directly
 * implementing this interface.
 */
@SuppressWarnings("all")
public interface XDiagramConfig {
  @Logging
  public static class Registry {
    private static XDiagramConfig.Registry instance;
    
    private Map<String, XDiagramConfig> configs = CollectionLiterals.<String, XDiagramConfig>newHashMap();
    
    public static XDiagramConfig.Registry getInstance() {
      XDiagramConfig.Registry _elvis = null;
      if (XDiagramConfig.Registry.instance != null) {
        _elvis = XDiagramConfig.Registry.instance;
      } else {
        XDiagramConfig.Registry _registry = new XDiagramConfig.Registry();
        _elvis = (XDiagramConfig.Registry.instance = _registry);
      }
      return _elvis;
    }
    
    private Registry() {
      this.addStaticConfigurations();
    }
    
    public Iterable<? extends XDiagramConfig> getConfigurations() {
      return this.configs.values();
    }
    
    protected void addStaticConfigurations() {
      IExtensionRegistry _extensionRegistry = Platform.getExtensionRegistry();
      IConfigurationElement[] _configurationElementsFor = _extensionRegistry.getConfigurationElementsFor("de.fxdiagram.eclipse.fxDiagramConfig");
      final Consumer<IConfigurationElement> _function = new Consumer<IConfigurationElement>() {
        @Override
        public void accept(final IConfigurationElement it) {
          try {
            Object _createExecutableExtension = it.createExecutableExtension("class");
            final AbstractDiagramConfig config = ((AbstractDiagramConfig) _createExecutableExtension);
            final String id = it.getAttribute("id");
            config.setID(id);
            boolean _containsKey = Registry.this.configs.containsKey(id);
            if (_containsKey) {
              XDiagramConfig.Registry.LOG.severe(("Duplicate fxDiagramConfig id=" + id));
            } else {
              Registry.this.configs.put(id, config);
            }
          } catch (Throwable _e) {
            throw Exceptions.sneakyThrow(_e);
          }
        }
      };
      ((List<IConfigurationElement>)Conversions.doWrapArray(_configurationElementsFor)).forEach(_function);
    }
    
    public XDiagramConfig getConfigByID(final String configID) {
      return this.configs.get(configID);
    }
    
    private static Logger LOG = Logger.getLogger("de.fxdiagram.eclipse.mapping.XDiagramConfig.Registry");
      ;
  }
  
  /**
   * @return all possible calls to add a diagram element for the given domain object
   */
  public abstract <ARG extends Object> Iterable<? extends MappingCall<?, ARG>> getEntryCalls(final ARG domainObject);
  
  public abstract AbstractMapping<?> getMappingByID(final String mappingID);
  
  public abstract String getID();
  
  public abstract <ARG extends Object> void addMapping(final AbstractMapping<ARG> mapping);
  
  public abstract IMappedElementDescriptorProvider getDomainObjectProvider();
}
