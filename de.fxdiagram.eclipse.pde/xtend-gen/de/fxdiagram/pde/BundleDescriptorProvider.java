package de.fxdiagram.pde;

import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.mapping.AbstractMapping;
import de.fxdiagram.mapping.IMappedElementDescriptor;
import de.fxdiagram.mapping.IMappedElementDescriptorProvider;
import de.fxdiagram.pde.BundleDependency;
import de.fxdiagram.pde.BundleDependencyDescriptor;
import de.fxdiagram.pde.BundleDescriptor;
import org.eclipse.osgi.service.resolver.BundleDescription;

@SuppressWarnings("all")
public class BundleDescriptorProvider implements IMappedElementDescriptorProvider {
  @Override
  public <T extends Object> DomainObjectDescriptor createDescriptor(final T domainObject) {
    return null;
  }
  
  @Override
  public <T extends Object> IMappedElementDescriptor<T> createMappedElementDescriptor(final T domainObject, final AbstractMapping<? extends T> mapping) {
    IMappedElementDescriptor<T> _switchResult = null;
    boolean _matched = false;
    if (domainObject instanceof BundleDescription) {
      _matched=true;
      String _symbolicName = ((BundleDescription)domainObject).getSymbolicName();
      String _string = ((BundleDescription)domainObject).getVersion().toString();
      String _iD = mapping.getConfig().getID();
      String _iD_1 = mapping.getID();
      BundleDescriptor _bundleDescriptor = new BundleDescriptor(_symbolicName, _string, _iD, _iD_1);
      _switchResult = ((IMappedElementDescriptor<T>) _bundleDescriptor);
    }
    if (!_matched) {
      if (domainObject instanceof BundleDependency) {
        _matched=true;
        BundleDependency.Kind _kind = ((BundleDependency)domainObject).getKind();
        String _symbolicName = ((BundleDependency)domainObject).getOwner().getSymbolicName();
        String _string = ((BundleDependency)domainObject).getOwner().getVersion().toString();
        String _symbolicName_1 = ((BundleDependency)domainObject).getDependency().getSymbolicName();
        String _string_1 = ((BundleDependency)domainObject).getVersionRange().toString();
        String _iD = mapping.getConfig().getID();
        String _iD_1 = mapping.getID();
        BundleDependencyDescriptor _bundleDependencyDescriptor = new BundleDependencyDescriptor(_kind, _symbolicName, _string, _symbolicName_1, _string_1, _iD, _iD_1);
        _switchResult = ((IMappedElementDescriptor<T>) _bundleDependencyDescriptor);
      }
    }
    if (!_matched) {
      _switchResult = null;
    }
    return _switchResult;
  }
  
  @Override
  public void postLoad() {
  }
}
