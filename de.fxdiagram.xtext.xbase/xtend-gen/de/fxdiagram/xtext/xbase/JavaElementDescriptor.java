package de.fxdiagram.xtext.xbase;

import com.google.common.base.Objects;
import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.eclipse.xtext.XtextDomainObjectProvider;
import de.fxdiagram.eclipse.xtext.ids.XtextEObjectID;
import de.fxdiagram.xtext.xbase.JvmDomainObjectProvider;
import de.fxdiagram.xtext.xbase.JvmDomainUtil;
import de.fxdiagram.xtext.xbase.JvmEObjectDescriptor;
import java.util.NoSuchElementException;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import org.eclipse.emf.common.util.URI;
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
public class JavaElementDescriptor extends JvmEObjectDescriptor<JvmIdentifiableElement> {
  public JavaElementDescriptor() {
  }
  
  public JavaElementDescriptor(final XtextEObjectID elementID, final String javaElementHandle, final String mappingConfigID, final String mappingID, final JvmDomainObjectProvider provider) {
    super(elementID, mappingConfigID, mappingID, provider);
    this.handleIdentifierProperty.set(javaElementHandle);
  }
  
  @Override
  protected IResourceServiceProvider getResourceServiceProvider() {
    URI _createURI = URI.createURI("dummy.___xbase");
    return IResourceServiceProvider.Registry.INSTANCE.getResourceServiceProvider(_createURI);
  }
  
  @Override
  public <T extends Object> T withDomainObject(final Function1<? super JvmIdentifiableElement, ? extends T> lambda) {
    T _xblockexpression = null;
    {
      String _handleIdentifier = this.getHandleIdentifier();
      final IJavaElement javaElement = JavaCore.create(_handleIdentifier);
      boolean _equals = Objects.equal(javaElement, null);
      if (_equals) {
        String _handleIdentifier_1 = this.getHandleIdentifier();
        String _plus = ("Java element " + _handleIdentifier_1);
        String _plus_1 = (_plus + " not found");
        throw new NoSuchElementException(_plus_1);
      }
      XtextDomainObjectProvider _provider = this.getProvider();
      XtextEObjectID _elementID = this.getElementID();
      URI _uRI = _elementID.getURI();
      final JvmDomainUtil domainUtil = ((JvmDomainObjectProvider) _provider).getJvmDomainUtil(_uRI);
      final JvmIdentifiableElement jvmElement = domainUtil.getJvmElement(javaElement);
      boolean _equals_1 = Objects.equal(jvmElement, null);
      if (_equals_1) {
        String _elementName = javaElement.getElementName();
        String _plus_2 = ("JVM element for " + _elementName);
        String _plus_3 = (_plus_2 + " not found");
        throw new NoSuchElementException(_plus_3);
      }
      _xblockexpression = lambda.apply(jvmElement);
    }
    return _xblockexpression;
  }
  
  @Override
  public Object openInEditor(final boolean isSelect) {
    try {
      IEditorPart _xblockexpression = null;
      {
        String _handleIdentifier = this.getHandleIdentifier();
        final IJavaElement javaElement = JavaCore.create(_handleIdentifier);
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
  
  private ReadOnlyStringWrapper handleIdentifierProperty = new ReadOnlyStringWrapper(this, "handleIdentifier");
  
  public String getHandleIdentifier() {
    return this.handleIdentifierProperty.get();
  }
  
  public ReadOnlyStringProperty handleIdentifierProperty() {
    return this.handleIdentifierProperty.getReadOnlyProperty();
  }
}
