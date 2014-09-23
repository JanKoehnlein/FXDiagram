package de.fxdiagram.xtext.xbase;

import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.xtext.glue.mapping.XtextDomainObjectProvider;
import de.fxdiagram.xtext.glue.mapping.XtextESettingDescriptor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.xtext.resource.IResourceServiceProvider;

@ModelNode
@SuppressWarnings("all")
public class JvmESettingDescriptor<ECLASS extends EObject> extends XtextESettingDescriptor<ECLASS> {
  public JvmESettingDescriptor() {
  }
  
  public JvmESettingDescriptor(final String uri, final String fqn, final EReference reference, final int index, final String mappingConfigID, final String mappingID, final XtextDomainObjectProvider provider) {
    super(uri, fqn, reference, index, mappingConfigID, mappingID, provider);
  }
  
  protected IResourceServiceProvider getResourceServiceProvider() {
    URI _createURI = URI.createURI("dummy.___xbase");
    return IResourceServiceProvider.Registry.INSTANCE.getResourceServiceProvider(_createURI);
  }
  
  public void populate(final ModelElementImpl modelElement) {
    super.populate(modelElement);
  }
}
