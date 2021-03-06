package de.fxdiagram.xtext.xbase;

import com.google.common.base.Objects;
import de.fxdiagram.eclipse.xtext.XtextEObjectDescriptor;
import de.fxdiagram.eclipse.xtext.ids.XtextEObjectID;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.resource.IResourceServiceProvider;

@SuppressWarnings("all")
public class JvmEObjectDescriptor<ECLASS extends EObject> extends XtextEObjectDescriptor<ECLASS> {
  public JvmEObjectDescriptor() {
  }
  
  public JvmEObjectDescriptor(final XtextEObjectID elementID, final String mappingConfigID, final String mappingID) {
    super(elementID, mappingConfigID, mappingID);
  }
  
  @Override
  protected IResourceServiceProvider getResourceServiceProvider() {
    return IResourceServiceProvider.Registry.INSTANCE.getResourceServiceProvider(URI.createURI("dummy.___xbase"));
  }
  
  @Override
  public boolean equals(final Object obj) {
    boolean _xifexpression = false;
    if ((obj instanceof JvmEObjectDescriptor<?>)) {
      _xifexpression = (super.equals(obj) && Objects.equal(this.getElementID().getURI(), ((JvmEObjectDescriptor<?>)obj).getElementID().getURI()));
    } else {
      _xifexpression = false;
    }
    return _xifexpression;
  }
  
  @Override
  public int hashCode() {
    int _hashCode = super.hashCode();
    int _hashCode_1 = this.getElementID().getURI().hashCode();
    int _multiply = (137 * _hashCode_1);
    return (_hashCode + _multiply);
  }
}
