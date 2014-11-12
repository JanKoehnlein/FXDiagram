package de.fxdiagram.xtext.xbase;

import de.fxdiagram.eclipse.mapping.XtextEObjectDescriptor;
import de.fxdiagram.xtext.xbase.JvmDomainObjectProvider;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.resource.IResourceServiceProvider;

@SuppressWarnings("all")
public class JvmEObjectDescriptor<ECLASS extends EObject> extends XtextEObjectDescriptor<ECLASS> {
  public JvmEObjectDescriptor() {
  }
  
  public JvmEObjectDescriptor(final String uri, final String fqn, final String mappingConfigID, final String mappingID, final JvmDomainObjectProvider provider) {
    super(uri, fqn, mappingConfigID, mappingID, provider);
  }
  
  protected IResourceServiceProvider getResourceServiceProvider() {
    URI _createURI = URI.createURI("dummy.___xbase");
    return IResourceServiceProvider.Registry.INSTANCE.getResourceServiceProvider(_createURI);
  }
}
