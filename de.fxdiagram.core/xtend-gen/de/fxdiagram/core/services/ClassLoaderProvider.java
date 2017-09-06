package de.fxdiagram.core.services;

import com.google.common.base.Objects;
import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.extensions.ClassLoaderExtensions;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.core.model.DomainObjectProviderWithState;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.core.model.ToString;
import de.fxdiagram.core.services.ClassLoaderDescriptor;
import de.fxdiagram.core.services.ResourceDescriptor;
import de.fxdiagram.core.services.ResourceHandle;
import java.net.URL;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Pure;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleReference;

@ModelNode
@SuppressWarnings("all")
public class ClassLoaderProvider implements DomainObjectProviderWithState {
  @Accessors
  private ClassLoader rootClassLoader;
  
  @Override
  public <T extends Object> DomainObjectDescriptor createDescriptor(final T domainObject) {
    boolean _matched = false;
    if (domainObject instanceof Class) {
      _matched=true;
      String _classLoaderID = this.getClassLoaderID(((Class<?>)domainObject));
      return new ClassLoaderDescriptor(_classLoaderID, this);
    }
    if (!_matched) {
      if (domainObject instanceof ResourceHandle) {
        _matched=true;
        return this.createResourceDescriptor(((ResourceHandle)domainObject).getName(), ((ResourceHandle)domainObject).getContext(), ((ResourceHandle)domainObject).getRelativePath());
      }
    }
    return null;
  }
  
  public ResourceDescriptor createResourceDescriptor(final String name, final Class<?> context, final String relativePath) {
    ResourceDescriptor _xblockexpression = null;
    {
      String _replace = context.getPackage().getName().replace(".", "/");
      String _plus = (_replace + "/");
      final String absoulutePath = (_plus + relativePath);
      String _classLoaderID = this.getClassLoaderID(context);
      _xblockexpression = new ResourceDescriptor(_classLoaderID, absoulutePath, name, this);
    }
    return _xblockexpression;
  }
  
  protected String getClassLoaderID(final Class<?> clazz) {
    if ((ClassLoaderExtensions.isEquinox() && (!Objects.equal(this.rootClassLoader, null)))) {
      final ClassLoader classLoader = clazz.getClassLoader();
      if ((classLoader instanceof BundleReference)) {
        return ((BundleReference)classLoader).getBundle().getSymbolicName();
      }
    }
    return "root";
  }
  
  public Class<?> loadClass(final String className, final ClassLoaderDescriptor descriptor) {
    try {
      Class<?> _xifexpression = null;
      if ((Objects.equal(descriptor.getClassLoaderID(), "root") || (!ClassLoaderExtensions.isEquinox()))) {
        _xifexpression = this.rootClassLoader.loadClass(className);
      } else {
        Bundle _bundle = Platform.getBundle(descriptor.getClassLoaderID());
        Class<?> _loadClass = null;
        if (_bundle!=null) {
          _loadClass=_bundle.loadClass(className);
        }
        _xifexpression = _loadClass;
      }
      return _xifexpression;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public String toURI(final String resourcePath, final ClassLoaderDescriptor descriptor) {
    try {
      URL _xifexpression = null;
      if ((Objects.equal(descriptor.getClassLoaderID(), "root") || (!ClassLoaderExtensions.isEquinox()))) {
        _xifexpression = this.rootClassLoader.getResource(resourcePath);
      } else {
        Bundle _bundle = Platform.getBundle(descriptor.getClassLoaderID());
        URL _resource = null;
        if (_bundle!=null) {
          _resource=_bundle.getResource(resourcePath);
        }
        _xifexpression = FileLocator.toFileURL(_resource);
      }
      final URL url = _xifexpression;
      return url.toExternalForm();
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  @Override
  public void copyState(final DomainObjectProviderWithState from) {
    this.rootClassLoader = ((ClassLoaderProvider) from).rootClassLoader;
  }
  
  public void populate(final ModelElementImpl modelElement) {
    
  }
  
  public void postLoad() {
    
  }
  
  public String toString() {
    return ToString.toString(this);
  }
  
  @Pure
  public ClassLoader getRootClassLoader() {
    return this.rootClassLoader;
  }
  
  public void setRootClassLoader(final ClassLoader rootClassLoader) {
    this.rootClassLoader = rootClassLoader;
  }
}
