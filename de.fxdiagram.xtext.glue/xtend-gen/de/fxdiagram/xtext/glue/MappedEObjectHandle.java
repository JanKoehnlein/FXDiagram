package de.fxdiagram.xtext.glue;

import com.google.common.base.Objects;
import de.fxdiagram.xtext.glue.mapping.AbstractMapping;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtext.naming.IQualifiedNameConverter;
import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.resource.IResourceServiceProvider;
import org.eclipse.xtext.resource.XtextResource;

@SuppressWarnings("all")
public class MappedEObjectHandle<MODEL extends EObject> {
  private URI uri;
  
  private String fqn;
  
  private AbstractMapping<MODEL> mapping;
  
  public MappedEObjectHandle(final MODEL domainObject, final AbstractMapping<MODEL> mapping) {
    final Resource resource = domainObject.eResource();
    URI _uRI = EcoreUtil.getURI(domainObject);
    this.uri = _uRI;
    this.mapping = mapping;
    this.fqn = "unnamed";
    if ((resource instanceof XtextResource)) {
      final IResourceServiceProvider resourceServiceProvider = ((XtextResource)resource).getResourceServiceProvider();
      IQualifiedNameProvider _get = resourceServiceProvider.<IQualifiedNameProvider>get(IQualifiedNameProvider.class);
      QualifiedName _fullyQualifiedName = null;
      if (_get!=null) {
        _fullyQualifiedName=_get.getFullyQualifiedName(domainObject);
      }
      final QualifiedName qualifiedName = _fullyQualifiedName;
      String _xifexpression = null;
      boolean _notEquals = (!Objects.equal(qualifiedName, null));
      if (_notEquals) {
        IQualifiedNameConverter _get_1 = resourceServiceProvider.<IQualifiedNameConverter>get(IQualifiedNameConverter.class);
        String _string = null;
        if (_get_1!=null) {
          _string=_get_1.toString(qualifiedName);
        }
        _xifexpression = _string;
      }
      this.fqn = _xifexpression;
    }
  }
  
  public URI getURI() {
    return this.uri;
  }
  
  public String getFullyQualifiedName() {
    return this.fqn;
  }
  
  public AbstractMapping<MODEL> getMapping() {
    return this.mapping;
  }
}
