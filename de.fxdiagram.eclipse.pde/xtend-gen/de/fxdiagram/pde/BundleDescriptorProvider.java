package de.fxdiagram.pde;

import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.core.model.DomainObjectProvider;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.mapping.AbstractMapping;
import de.fxdiagram.mapping.IMappedElementDescriptor;
import de.fxdiagram.mapping.IMappedElementDescriptorProvider;
import de.fxdiagram.mapping.execution.XDiagramConfig;
import de.fxdiagram.pde.BundleDependency;
import de.fxdiagram.pde.BundleDependencyDescriptor;
import de.fxdiagram.pde.BundleDescriptor;
import org.eclipse.osgi.service.resolver.BundleDescription;
import org.eclipse.osgi.service.resolver.VersionRange;
import org.osgi.framework.Version;

@ModelNode
@SuppressWarnings("all")
public class BundleDescriptorProvider implements DomainObjectProvider, IMappedElementDescriptorProvider {
  @Override
  public <T extends Object> DomainObjectDescriptor createDescriptor(final T domainObject) {
    return null;
  }
  
  @Override
  public <T extends Object> IMappedElementDescriptor<T> createMappedElementDescriptor(final T domainObject, final AbstractMapping<T> mapping) {
    IMappedElementDescriptor<T> _switchResult = null;
    boolean _matched = false;
    if (!_matched) {
      if (domainObject instanceof BundleDescription) {
        _matched=true;
        String _symbolicName = ((BundleDescription)domainObject).getSymbolicName();
        Version _version = ((BundleDescription)domainObject).getVersion();
        String _string = _version.toString();
        XDiagramConfig _config = mapping.getConfig();
        String _iD = _config.getID();
        String _iD_1 = mapping.getID();
        BundleDescriptor _bundleDescriptor = new BundleDescriptor(_symbolicName, _string, _iD, _iD_1, this);
        _switchResult = ((IMappedElementDescriptor<T>) _bundleDescriptor);
      }
    }
    if (!_matched) {
      if (domainObject instanceof BundleDependency) {
        _matched=true;
        BundleDependency.Kind _kind = ((BundleDependency)domainObject).getKind();
        BundleDescription _owner = ((BundleDependency)domainObject).getOwner();
        String _symbolicName = _owner.getSymbolicName();
        BundleDescription _owner_1 = ((BundleDependency)domainObject).getOwner();
        Version _version = _owner_1.getVersion();
        String _string = _version.toString();
        BundleDescription _dependency = ((BundleDependency)domainObject).getDependency();
        String _symbolicName_1 = _dependency.getSymbolicName();
        VersionRange _versionRange = ((BundleDependency)domainObject).getVersionRange();
        String _string_1 = _versionRange.toString();
        XDiagramConfig _config = mapping.getConfig();
        String _iD = _config.getID();
        String _iD_1 = mapping.getID();
        BundleDependencyDescriptor _bundleDependencyDescriptor = new BundleDependencyDescriptor(_kind, _symbolicName, _string, _symbolicName_1, _string_1, _iD, _iD_1, this);
        _switchResult = ((IMappedElementDescriptor<T>) _bundleDependencyDescriptor);
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
