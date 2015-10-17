package de.fxdiagram.xtext.xbase;

import com.google.common.base.Objects;
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
  
  @Override
  public boolean equals(final Object obj) {
    boolean _xifexpression = false;
    if ((obj instanceof JvmEObjectDescriptor<?>)) {
      boolean _and = false;
      boolean _equals = super.equals(obj);
      if (!_equals) {
        _and = false;
      } else {
        XtextEObjectID _elementID = this.getElementID();
        URI _uRI = _elementID.getURI();
        XtextEObjectID _elementID_1 = ((JvmEObjectDescriptor<?>)obj).getElementID();
        URI _uRI_1 = _elementID_1.getURI();
        boolean _equals_1 = Objects.equal(_uRI, _uRI_1);
        _and = _equals_1;
      }
      _xifexpression = _and;
    } else {
      _xifexpression = false;
    }
    return _xifexpression;
  }
  
  @Override
  public int hashCode() {
    int _hashCode = super.hashCode();
    XtextEObjectID _elementID = this.getElementID();
    URI _uRI = _elementID.getURI();
    int _hashCode_1 = _uRI.hashCode();
    int _multiply = (137 * _hashCode_1);
    return (_hashCode + _multiply);
  }
}
