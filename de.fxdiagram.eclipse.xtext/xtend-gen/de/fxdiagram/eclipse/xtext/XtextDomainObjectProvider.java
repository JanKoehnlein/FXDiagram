package de.fxdiagram.eclipse.xtext;

import com.google.common.base.Objects;
import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.core.model.DomainObjectProvider;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.eclipse.xtext.ESetting;
import de.fxdiagram.eclipse.xtext.XtextEObjectDescriptor;
import de.fxdiagram.eclipse.xtext.XtextESettingDescriptor;
import de.fxdiagram.mapping.AbstractMapping;
import de.fxdiagram.mapping.IMappedElementDescriptor;
import de.fxdiagram.mapping.IMappedElementDescriptorProvider;
import de.fxdiagram.mapping.XDiagramConfig;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtext.naming.IQualifiedNameConverter;
import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.resource.IResourceServiceProvider;
import org.eclipse.xtext.resource.XtextResource;

/**
 * A {@link DomainObjectProvider} for Xtext based domain objects.
 */
@ModelNode
@SuppressWarnings("all")
public class XtextDomainObjectProvider implements IMappedElementDescriptorProvider {
  @Override
  public DomainObjectDescriptor createDescriptor(final Object handle) {
    return null;
  }
  
  public String getFullyQualifiedName(final EObject domainObject) {
    final Resource resource = domainObject.eResource();
    IResourceServiceProvider _xifexpression = null;
    if ((resource instanceof XtextResource)) {
      _xifexpression = ((XtextResource)resource).getResourceServiceProvider();
    } else {
      URI _uRI = resource.getURI();
      _xifexpression = IResourceServiceProvider.Registry.INSTANCE.getResourceServiceProvider(_uRI);
    }
    final IResourceServiceProvider resourceServiceProvider = _xifexpression;
    IQualifiedNameProvider _get = null;
    if (resourceServiceProvider!=null) {
      _get=resourceServiceProvider.<IQualifiedNameProvider>get(IQualifiedNameProvider.class);
    }
    QualifiedName _fullyQualifiedName = null;
    if (_get!=null) {
      _fullyQualifiedName=_get.getFullyQualifiedName(domainObject);
    }
    final QualifiedName qualifiedName = _fullyQualifiedName;
    boolean _notEquals = (!Objects.equal(qualifiedName, null));
    if (_notEquals) {
      IQualifiedNameConverter _get_1 = resourceServiceProvider.<IQualifiedNameConverter>get(IQualifiedNameConverter.class);
      String _string = null;
      if (_get_1!=null) {
        _string=_get_1.toString(qualifiedName);
      }
      return _string;
    } else {
      return "unnamed";
    }
  }
  
  @Override
  public <T extends Object> IMappedElementDescriptor<T> createMappedElementDescriptor(final T domainObject, final AbstractMapping<T> mapping) {
    final T it = domainObject;
    boolean _matched = false;
    if (!_matched) {
      if (it instanceof EObject) {
        _matched=true;
        URI _uRI = EcoreUtil.getURI(((EObject)it));
        String _string = _uRI.toString();
        String _fullyQualifiedName = this.getFullyQualifiedName(((EObject)it));
        XDiagramConfig _config = mapping.getConfig();
        String _iD = _config.getID();
        String _iD_1 = mapping.getID();
        XtextEObjectDescriptor<EObject> _xtextEObjectDescriptor = new XtextEObjectDescriptor<EObject>(_string, _fullyQualifiedName, _iD, _iD_1, this);
        return ((IMappedElementDescriptor<T>) _xtextEObjectDescriptor);
      }
    }
    if (!_matched) {
      if (it instanceof ESetting) {
        _matched=true;
        EObject _owner = ((ESetting<?>)it).getOwner();
        URI _uRI = EcoreUtil.getURI(_owner);
        String _string = _uRI.toString();
        EObject _owner_1 = ((ESetting<?>)it).getOwner();
        String _fullyQualifiedName = this.getFullyQualifiedName(_owner_1);
        EReference _reference = ((ESetting<?>)it).getReference();
        int _index = ((ESetting<?>)it).getIndex();
        XDiagramConfig _config = mapping.getConfig();
        String _iD = _config.getID();
        String _iD_1 = mapping.getID();
        return new XtextESettingDescriptor(_string, _fullyQualifiedName, _reference, _index, _iD, _iD_1, this);
      }
    }
    return null;
  }
  
  public void populate(final ModelElementImpl modelElement) {
    
  }
}
