package de.fxdiagram.core.services;

import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.extensions.ClassLoaderExtensions;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.core.model.DomainObjectProvider;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.core.services.ResourceDescriptor;
import de.fxdiagram.core.services.ResourceHandle;

@ModelNode
@SuppressWarnings("all")
public class ResourceProvider implements DomainObjectProvider {
  protected ResourceHandle resolveResourceHandle(final ResourceDescriptor description) {
    String _name = description.getName();
    String _classLoaderClassName = description.getClassLoaderClassName();
    Class<?> _deserialize = ClassLoaderExtensions.deserialize(_classLoaderClassName);
    String _relativePath = description.getRelativePath();
    return new ResourceHandle(_name, _deserialize, _relativePath);
  }
  
  public DomainObjectDescriptor createDescriptor(final Object domainObject) {
    ResourceDescriptor _switchResult = null;
    boolean _matched = false;
    if (!_matched) {
      if (domainObject instanceof ResourceHandle) {
        _matched=true;
        _switchResult = this.createResourceDescriptor(((ResourceHandle)domainObject));
      }
    }
    if (!_matched) {
      throw new IllegalArgumentException(("Cannot handle " + domainObject));
    }
    return _switchResult;
  }
  
  public ResourceDescriptor createResourceDescriptor(final ResourceHandle object) {
    Class<?> _context = object.getContext();
    Class<? extends Class> _class = _context.getClass();
    String _serialize = ClassLoaderExtensions.serialize(_class);
    String _relativePath = object.getRelativePath();
    String _name = object.getName();
    return new ResourceDescriptor(_serialize, _relativePath, _name, this);
  }
  
  public void populate(final ModelElementImpl modelElement) {
    
  }
}
