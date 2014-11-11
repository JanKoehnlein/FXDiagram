package de.fxdiagram.pde;

import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.core.model.DomainObjectProvider;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.pde.PluginDescriptor;
import de.fxdiagram.pde.PluginImportDescriptor;
import de.fxdiagram.xtext.glue.mapping.AbstractMapping;
import de.fxdiagram.xtext.glue.mapping.IMappedElementDescriptor;
import de.fxdiagram.xtext.glue.mapping.IMappedElementDescriptorProvider;
import de.fxdiagram.xtext.glue.mapping.XDiagramConfig;
import org.eclipse.pde.core.plugin.IPluginBase;
import org.eclipse.pde.core.plugin.IPluginImport;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.pde.core.plugin.IPluginObject;

@ModelNode
@SuppressWarnings("all")
public class PluginDescriptorProvider implements DomainObjectProvider, IMappedElementDescriptorProvider {
  public <T extends Object> DomainObjectDescriptor createDescriptor(final T domainObject) {
    return null;
  }
  
  public <T extends Object> IMappedElementDescriptor<T> createMappedElementDescriptor(final T domainObject, final AbstractMapping<T> mapping) {
    IMappedElementDescriptor<T> _switchResult = null;
    boolean _matched = false;
    if (!_matched) {
      if (domainObject instanceof IPluginModelBase) {
        _matched=true;
        IPluginBase _pluginBase = ((IPluginModelBase)domainObject).getPluginBase();
        String _id = _pluginBase.getId();
        IPluginBase _pluginBase_1 = ((IPluginModelBase)domainObject).getPluginBase();
        String _version = _pluginBase_1.getVersion();
        IPluginBase _pluginBase_2 = ((IPluginModelBase)domainObject).getPluginBase();
        String _name = _pluginBase_2.getName();
        String _resourceString = ((IPluginModelBase)domainObject).getResourceString(_name);
        XDiagramConfig _config = mapping.getConfig();
        String _iD = _config.getID();
        String _iD_1 = mapping.getID();
        PluginDescriptor _pluginDescriptor = new PluginDescriptor(_id, _version, _resourceString, _iD, _iD_1, this);
        _switchResult = ((IMappedElementDescriptor<T>) _pluginDescriptor);
      }
    }
    if (!_matched) {
      if (domainObject instanceof IPluginImport) {
        _matched=true;
        IPluginObject _parent = ((IPluginImport)domainObject).getParent();
        IPluginBase _pluginBase = _parent.getPluginBase();
        String _id = _pluginBase.getId();
        IPluginObject _parent_1 = ((IPluginImport)domainObject).getParent();
        IPluginBase _pluginBase_1 = _parent_1.getPluginBase();
        String _version = _pluginBase_1.getVersion();
        String _id_1 = ((IPluginImport)domainObject).getId();
        String _version_1 = ((IPluginImport)domainObject).getVersion();
        XDiagramConfig _config = mapping.getConfig();
        String _iD = _config.getID();
        String _iD_1 = mapping.getID();
        PluginImportDescriptor _pluginImportDescriptor = new PluginImportDescriptor(_id, _version, _id_1, _version_1, _iD, _iD_1, this);
        _switchResult = ((IMappedElementDescriptor<T>) _pluginImportDescriptor);
      }
    }
    if (!_matched) {
      _switchResult = null;
    }
    return _switchResult;
  }
  
  public void populate(final ModelElementImpl modelElement) {
    
  }
}
