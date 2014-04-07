package de.fxdiagram.xtext.glue;

import com.google.common.base.Objects;
import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.core.model.DomainObjectProvider;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.xtext.glue.XtextDomainObjectDescriptor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
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
  public DomainObjectDescriptor createDescriptor(final Object domainObject) {
    if ((domainObject instanceof EObject)) {
      final Resource resource = ((EObject)domainObject).eResource();
      if ((resource instanceof XtextResource)) {
        final IResourceServiceProvider resourceServiceProvider = ((XtextResource)resource).getResourceServiceProvider();
        boolean _notEquals = (!Objects.equal(resourceServiceProvider, null));
        if (_notEquals) {
          IQualifiedNameProvider _get = resourceServiceProvider.<IQualifiedNameProvider>get(IQualifiedNameProvider.class);
          QualifiedName _fullyQualifiedName = null;
          if (_get!=null) {
            _fullyQualifiedName=_get.getFullyQualifiedName(((EObject)domainObject));
          }
          final QualifiedName fqn = _fullyQualifiedName;
          String _xifexpression = null;
          boolean _notEquals_1 = (!Objects.equal(fqn, null));
          if (_notEquals_1) {
            IQualifiedNameConverter _get_1 = resourceServiceProvider.<IQualifiedNameConverter>get(IQualifiedNameConverter.class);
            String _string = null;
            if (_get_1!=null) {
              _string=_get_1.toString(fqn);
            }
            _xifexpression = _string;
          } else {
            _xifexpression = "unnamed";
          }
          final String fqnString = _xifexpression;
          URI _uRI = EcoreUtil.getURI(((EObject)domainObject));
          String _string_1 = null;
          if (_uRI!=null) {
            _string_1=_uRI.toString();
          }
          final String uri = _string_1;
          boolean _and = false;
          boolean _notEquals_2 = (!Objects.equal(fqnString, null));
          if (!_notEquals_2) {
            _and = false;
          } else {
            boolean _notEquals_3 = (!Objects.equal(uri, null));
            _and = _notEquals_3;
          }
          if (_and) {
            return new XtextDomainObjectDescriptor<Object>(uri, fqnString, this);
          }
        }
      }
    }
    return null;
  }
  
  public Object resolveDomainObject(final DomainObjectDescriptor descriptor) {
    throw new UnsupportedOperationException("Need a transaction to access EObjects");
  }
  
  public void populate(final ModelElementImpl modelElement) {
    
  }
}
