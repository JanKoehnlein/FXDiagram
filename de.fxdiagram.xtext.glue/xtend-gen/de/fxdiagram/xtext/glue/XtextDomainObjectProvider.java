package de.fxdiagram.xtext.glue;

import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.core.model.DomainObjectProvider;
import de.fxdiagram.xtext.glue.MappedEObjectHandle;
import de.fxdiagram.xtext.glue.XtextDomainObjectDescriptor;
import de.fxdiagram.xtext.glue.mapping.AbstractMapping;
import de.fxdiagram.xtext.glue.mapping.XDiagramConfig;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;

@ModelNode
@SuppressWarnings("all")
public class XtextDomainObjectProvider implements DomainObjectProvider {
  public DomainObjectDescriptor createDescriptor(final Object it) {
    if ((it instanceof MappedEObjectHandle<?>)) {
      URI _uRI = ((MappedEObjectHandle<?>)it).getURI();
      String _string = _uRI.toString();
      String _fullyQualifiedName = ((MappedEObjectHandle<?>)it).getFullyQualifiedName();
      AbstractMapping<? extends EObject> _mapping = ((MappedEObjectHandle<?>)it).getMapping();
      XDiagramConfig _config = _mapping.getConfig();
      String _iD = _config.getID();
      AbstractMapping<? extends EObject> _mapping_1 = ((MappedEObjectHandle<?>)it).getMapping();
      String _iD_1 = _mapping_1.getID();
      return new XtextDomainObjectDescriptor<Object>(_string, _fullyQualifiedName, _iD, _iD_1, this);
    }
    return null;
  }
  
  public <T extends Object, U extends EObject> XtextDomainObjectDescriptor<T> createDescriptor(final T domainObject, final AbstractMapping<?> mapping) {
    MappedEObjectHandle<U> _mappedEObjectHandle = new MappedEObjectHandle<U>(((U) domainObject), ((AbstractMapping<U>) mapping));
    DomainObjectDescriptor _createDescriptor = this.createDescriptor(_mappedEObjectHandle);
    return ((XtextDomainObjectDescriptor<T>) _createDescriptor);
  }
}
