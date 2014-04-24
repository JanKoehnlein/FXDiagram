package de.fxdiagram.xtext.glue.mapping;

import com.google.common.collect.Iterables;
import de.fxdiagram.xtext.glue.mapping.XDiagramConfig;
import java.util.List;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;

@SuppressWarnings("all")
public class XDiagramConfigRegistry {
  private static XDiagramConfigRegistry instance;
  
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
    ObservableList<XDiagramConfig> _configs = this.getConfigs();
    List<XDiagramConfig> _staticConfigurations = this.getStaticConfigurations();
    Iterables.<XDiagramConfig>addAll(_configs, _staticConfigurations);
  }
  
  public List<XDiagramConfig> getConfigurations() {
    return this.getConfigs();
  }
  
  protected List<XDiagramConfig> getStaticConfigurations() {
    IExtensionRegistry _extensionRegistry = Platform.getExtensionRegistry();
    IConfigurationElement[] _configurationElementsFor = _extensionRegistry.getConfigurationElementsFor("de.fxdiagram.xtext.glue.fxDiagramConfig");
    final Function1<IConfigurationElement,XDiagramConfig> _function = new Function1<IConfigurationElement,XDiagramConfig>() {
      public XDiagramConfig apply(final IConfigurationElement it) {
        try {
          Object _createExecutableExtension = it.createExecutableExtension("class");
          return ((XDiagramConfig) _createExecutableExtension);
        } catch (Throwable _e) {
          throw Exceptions.sneakyThrow(_e);
        }
      }
    };
    List<XDiagramConfig> _map = ListExtensions.<IConfigurationElement, XDiagramConfig>map(((List<IConfigurationElement>)Conversions.doWrapArray(_configurationElementsFor)), _function);
    return IterableExtensions.<XDiagramConfig>toList(_map);
  }
  
  private SimpleListProperty<XDiagramConfig> configsProperty = new SimpleListProperty<XDiagramConfig>(this, "configs",_initConfigs());
  
  private static final ObservableList<XDiagramConfig> _initConfigs() {
    ObservableList<XDiagramConfig> _observableArrayList = FXCollections.<XDiagramConfig>observableArrayList();
    return _observableArrayList;
  }
  
  public ObservableList<XDiagramConfig> getConfigs() {
    return this.configsProperty.get();
  }
  
  public ListProperty<XDiagramConfig> configsProperty() {
    return this.configsProperty;
  }
}
