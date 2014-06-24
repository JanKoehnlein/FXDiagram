package de.fxdiagram.xtext.glue.mapping;

import de.fxdiagram.annotations.logging.Logging;
import de.fxdiagram.xtext.glue.mapping.AbstractMapping;
import de.fxdiagram.xtext.glue.mapping.MappingCall;
import java.util.Map;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;

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
        XDiagramConfig.Registry _instance = XDiagramConfig.Registry.instance = _registry;
        _elvis = _instance;
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
      throw new Error("Unresolved compilation problems:"
        + "\nThe method setID is undefined for the type Registry"
        + "\nThe method or field LOG is undefined for the type Registry"
        + "\nsevere cannot be resolved");
    }
    
    public XDiagramConfig getConfigByID(final String configID) {
      return this.configs.get(configID);
    }
  }
  
  public abstract <ARG extends Object> Iterable<? extends MappingCall<?, ARG>> getEntryCalls(final ARG domainObject);
  
  public abstract AbstractMapping<?> getMappingByID(final String mappingID);
  
  public abstract String getID();
  
  public abstract <ARG extends Object> void addMapping(final AbstractMapping<ARG> mapping);
}
