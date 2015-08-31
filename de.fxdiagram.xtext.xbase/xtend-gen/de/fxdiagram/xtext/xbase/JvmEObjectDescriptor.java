package de.fxdiagram.xtext.xbase;

import de.fxdiagram.eclipse.xtext.XtextEObjectDescriptor;
import de.fxdiagram.eclipse.xtext.ids.XtextEObjectID;
import de.fxdiagram.xtext.xbase.JvmDomainObjectProvider;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.resource.IResourceServiceProvider;

@SuppressWarnings("all")
public class JvmEObjectDescriptor<ECLASS extends EObject> extends XtextEObjectDescriptor<ECLASS> {
  public JvmEObjectDescriptor() {
  }
  
  public JvmEObjectDescriptor(final XtextEObjectID elementID, final String mappingConfigID, final String mappingID, final JvmDomainObjectProvider provider) {
    super(elementID, mappingConfigID, mappingID, provider);
  }
  
  @Override
  protected IResourceServiceProvider getResourceServiceProvider() {
    URI _createURI = URI.createURI("dummy.___xbase");
    return IResourceServiceProvider.Registry.INSTANCE.getResourceServiceProvider(_createURI);
  }
}
