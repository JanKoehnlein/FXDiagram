package de.fxdiagram.eclipse.xtext.mapping;

import com.google.inject.Inject;
import de.fxdiagram.core.XDomainObjectShape;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.extensions.InitializingListener;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.eclipse.mapping.AbstractEclipseDiagramConfig;
import de.fxdiagram.eclipse.xtext.AbstractXtextDescriptor;
import de.fxdiagram.eclipse.xtext.XtextDomainObjectProvider;
import de.fxdiagram.mapping.IMappedElementDescriptorProvider;
import javafx.beans.property.ReadOnlyObjectProperty;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.ui.resource.IStorage2UriMapper;
import org.eclipse.xtext.util.Pair;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public abstract class AbstractXtextDiagramConfig extends AbstractEclipseDiagramConfig {
  @Inject(optional = true)
  @Extension
  private IStorage2UriMapper _iStorage2UriMapper;
  
  @Override
  public void initialize(final XDomainObjectShape shape) {
    ReadOnlyObjectProperty<DomainObjectDescriptor> _domainObjectDescriptorProperty = shape.domainObjectDescriptorProperty();
    if (_domainObjectDescriptorProperty!=null) {
      InitializingListener<DomainObjectDescriptor> _initializingListener = new InitializingListener<DomainObjectDescriptor>();
      final Procedure1<InitializingListener<DomainObjectDescriptor>> _function = (InitializingListener<DomainObjectDescriptor> it) -> {
        final Procedure1<Object> _function_1 = (Object it_1) -> {
          if ((it_1 instanceof AbstractXtextDescriptor<?>)) {
            ((AbstractXtextDescriptor<?>)it_1).injectMembers(shape);
          }
        };
        it.setSet(_function_1);
      };
      InitializingListener<DomainObjectDescriptor> _doubleArrow = ObjectExtensions.<InitializingListener<DomainObjectDescriptor>>operator_doubleArrow(_initializingListener, _function);
      CoreExtensions.<DomainObjectDescriptor>addInitializingListener(_domainObjectDescriptorProperty, _doubleArrow);
    }
    super.initialize(shape);
  }
  
  @Override
  protected IMappedElementDescriptorProvider createDomainObjectProvider() {
    return new XtextDomainObjectProvider();
  }
  
  public String getFilePath(final EObject element) {
    Resource _eResource = element.eResource();
    URI _uRI = _eResource.getURI();
    Iterable<Pair<IStorage, IProject>> _storages = this._iStorage2UriMapper.getStorages(_uRI);
    Pair<IStorage, IProject> _head = IterableExtensions.<Pair<IStorage, IProject>>head(_storages);
    IStorage _first = _head.getFirst();
    IPath _fullPath = ((IFile) _first).getFullPath();
    IPath _removeFileExtension = _fullPath.removeFileExtension();
    String _string = _removeFileExtension.toString();
    return (_string + ".fxd");
  }
}
