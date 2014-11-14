package de.fxdiagram.pde;

import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.core.model.DomainObjectProvider;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.eclipse.mapping.AbstractMapping;
import de.fxdiagram.eclipse.mapping.IMappedElementDescriptor;
import de.fxdiagram.eclipse.mapping.IMappedElementDescriptorProvider;
import de.fxdiagram.eclipse.mapping.XDiagramConfig;
import de.fxdiagram.pde.PluginDependency;
import de.fxdiagram.pde.PluginDependencyDescriptor;
import de.fxdiagram.pde.PluginDescriptor;
import org.eclipse.osgi.service.resolver.VersionRange;
import org.eclipse.pde.core.plugin.IPluginBase;
import org.eclipse.pde.core.plugin.IPluginModelBase;

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
        XDiagramConfig _config = mapping.getConfig();
        String _iD = _config.getID();
        String _iD_1 = mapping.getID();
        PluginDescriptor _pluginDescriptor = new PluginDescriptor(_id, _version, _iD, _iD_1, this);
        _switchResult = ((IMappedElementDescriptor<T>) _pluginDescriptor);
      }
    }
    if (!_matched) {
      if (domainObject instanceof PluginDependency) {
        _matched=true;
        IPluginModelBase _owner = ((PluginDependency)domainObject).getOwner();
        IPluginBase _pluginBase = _owner.getPluginBase();
        String _id = _pluginBase.getId();
        IPluginModelBase _owner_1 = ((PluginDependency)domainObject).getOwner();
        IPluginBase _pluginBase_1 = _owner_1.getPluginBase();
        String _version = _pluginBase_1.getVersion();
        IPluginModelBase _dependency = ((PluginDependency)domainObject).getDependency();
        IPluginBase _pluginBase_2 = _dependency.getPluginBase();
        String _id_1 = _pluginBase_2.getId();
        VersionRange _versionRange = ((PluginDependency)domainObject).getVersionRange();
        String _string = _versionRange.toString();
        XDiagramConfig _config = mapping.getConfig();
        String _iD = _config.getID();
        String _iD_1 = mapping.getID();
        PluginDependencyDescriptor _pluginDependencyDescriptor = new PluginDependencyDescriptor(_id, _version, _id_1, _string, _iD, _iD_1, this);
        _switchResult = ((IMappedElementDescriptor<T>) _pluginDependencyDescriptor);
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
