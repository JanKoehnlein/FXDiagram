package de.fxdiagram.xtext.xbase;

import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.core.model.ToString;
import de.fxdiagram.eclipse.xtext.XtextDomainObjectProvider;
import de.fxdiagram.eclipse.xtext.ids.XtextEObjectID;
import de.fxdiagram.xtext.xbase.JvmDomainObjectProvider;
import de.fxdiagram.xtext.xbase.JvmDomainUtil;
import de.fxdiagram.xtext.xbase.JvmEObjectDescriptor;
import java.util.NoSuchElementException;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.ui.IEditorPart;
import org.eclipse.xtext.common.types.JvmIdentifiableElement;
import org.eclipse.xtext.resource.IResourceServiceProvider;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;

@ModelNode("handleIdentifier")
@SuppressWarnings("all")
public class JavaElementDescriptor<ECLASS extends EObject> extends JvmEObjectDescriptor<ECLASS> {
  public JavaElementDescriptor() {
  }
  
  public JavaElementDescriptor(final XtextEObjectID elementID, final String javaElementHandle, final String mappingConfigID, final String mappingID) {
    super(elementID, mappingConfigID, mappingID);
    this.handleIdentifierProperty.set(javaElementHandle);
  }
  
  @Override
  protected IResourceServiceProvider getResourceServiceProvider() {
    return IResourceServiceProvider.Registry.INSTANCE.getResourceServiceProvider(URI.createURI("dummy.___xbase"));
  }
  
  @Override
  public <T extends Object> T withDomainObject(final Function1<? super ECLASS, ? extends T> lambda) {
    T _xblockexpression = null;
    {
      final IJavaElement javaElement = JavaCore.create(this.getHandleIdentifier());
      if ((javaElement == null)) {
        String _handleIdentifier = this.getHandleIdentifier();
        String _plus = ("Java element " + _handleIdentifier);
        String _plus_1 = (_plus + " not found");
        throw new NoSuchElementException(_plus_1);
      }
      XtextDomainObjectProvider _provider = this.getProvider();
      final JvmDomainUtil domainUtil = ((JvmDomainObjectProvider) _provider).getJvmDomainUtil(this.getElementID().getURI());
      final JvmIdentifiableElement jvmElement = domainUtil.getJvmElement(javaElement);
      if ((jvmElement == null)) {
        String _elementName = javaElement.getElementName();
        String _plus_2 = ("JVM element for " + _elementName);
        String _plus_3 = (_plus_2 + " not found");
        throw new NoSuchElementException(_plus_3);
      }
      final EObject realJvmElement = jvmElement.eResource().getEObject(this.getElementID().getURI().fragment());
      _xblockexpression = lambda.apply(((ECLASS) realJvmElement));
    }
    return _xblockexpression;
  }
  
  @Override
  public Object openInEditor(final boolean isSelect) {
    try {
      IEditorPart _xblockexpression = null;
      {
        final IJavaElement javaElement = JavaCore.create(this.getHandleIdentifier());
        _xblockexpression = JavaUI.openInEditor(javaElement, true, isSelect);
      }
      return _xblockexpression;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public void populate(final ModelElementImpl modelElement) {
    super.populate(modelElement);
    modelElement.addProperty(handleIdentifierProperty, String.class);
  }
  
  public void postLoad() {
    
  }
  
  public String toString() {
    return ToString.toString(this);
  }
  
  private ReadOnlyStringWrapper handleIdentifierProperty = new ReadOnlyStringWrapper(this, "handleIdentifier");
  
  public String getHandleIdentifier() {
    return this.handleIdentifierProperty.get();
  }
  
  public ReadOnlyStringProperty handleIdentifierProperty() {
    return this.handleIdentifierProperty.getReadOnlyProperty();
  }
}
