package de.fxdiagram.xtext.glue.mapping;

import com.google.common.base.Objects;
import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.core.model.DomainObjectProvider;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.xtext.glue.mapping.AbstractMapping;
import de.fxdiagram.xtext.glue.mapping.AbstractXtextDescriptor;
import de.fxdiagram.xtext.glue.mapping.ESetting;
import de.fxdiagram.xtext.glue.mapping.MappedElement;
import de.fxdiagram.xtext.glue.mapping.XDiagramConfig;
import de.fxdiagram.xtext.glue.mapping.XtextEObjectDescriptor;
import de.fxdiagram.xtext.glue.mapping.XtextESettingDescriptor;
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

@ModelNode
@SuppressWarnings("all")
public class XtextDomainObjectProvider implements DomainObjectProvider {
  public DomainObjectDescriptor createDescriptor(final Object handle) {
    if ((handle instanceof MappedElement<?>)) {
      Object _element = ((MappedElement<?>)handle).getElement();
      final Object it = _element;
      boolean _matched = false;
      if (!_matched) {
        if (it instanceof EObject) {
          _matched=true;
          URI _uRI = EcoreUtil.getURI(((EObject)it));
          String _string = _uRI.toString();
          String _fullyQualifiedName = this.getFullyQualifiedName(((EObject)it));
          AbstractMapping<?> _mapping = ((MappedElement<?>)handle).getMapping();
          XDiagramConfig _config = _mapping.getConfig();
          String _iD = _config.getID();
          AbstractMapping<?> _mapping_1 = ((MappedElement<?>)handle).getMapping();
          String _iD_1 = _mapping_1.getID();
          return new XtextEObjectDescriptor<EObject>(_string, _fullyQualifiedName, _iD, _iD_1, this);
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
          AbstractMapping<?> _mapping = ((MappedElement<?>)handle).getMapping();
          XDiagramConfig _config = _mapping.getConfig();
          String _iD = _config.getID();
          AbstractMapping<?> _mapping_1 = ((MappedElement<?>)handle).getMapping();
          String _iD_1 = _mapping_1.getID();
          return new XtextESettingDescriptor<EObject>(_string, _fullyQualifiedName, _reference, _index, _iD, _iD_1, this);
        }
      }
      return null;
    }
    return null;
  }
  
  public <T extends Object> AbstractXtextDescriptor<T> createMappedDescriptor(final T domainObject, final AbstractMapping<T> mapping) {
    MappedElement<T> _mappedElement = new MappedElement<T>(domainObject, mapping);
    DomainObjectDescriptor _createDescriptor = this.createDescriptor(_mappedElement);
    return ((AbstractXtextDescriptor<T>) _createDescriptor);
  }
  
  public <T extends Object, U extends Object> AbstractXtextDescriptor<T> createMappedDescriptor2(final T domainObject, final AbstractMapping<?> mapping) {
    MappedElement<?> _mappedElement = new MappedElement(domainObject, mapping);
    DomainObjectDescriptor _createDescriptor = this.createDescriptor(_mappedElement);
    return ((AbstractXtextDescriptor<T>) _createDescriptor);
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
  
  public void populate(final ModelElementImpl modelElement) {
    
  }
}
