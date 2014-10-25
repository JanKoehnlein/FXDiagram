package de.fxdiagram.xtext.xbase;

import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.xtext.glue.mapping.XtextDomainObjectProvider;
import de.fxdiagram.xtext.xbase.JvmDomainObjectProvider;
import de.fxdiagram.xtext.xbase.JvmDomainUtil;
import de.fxdiagram.xtext.xbase.JvmEObjectDescriptor;
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
  
  public JavaElementDescriptor(final String uri, final String fqn, final String javaElementHandle, final String mappingConfigID, final String mappingID, final JvmDomainObjectProvider provider) {
    super(uri, fqn, mappingConfigID, mappingID, provider);
    this.handleIdentifierProperty.set(javaElementHandle);
  }
  
  protected IResourceServiceProvider getResourceServiceProvider() {
    URI _createURI = URI.createURI("dummy.___xbase");
    return IResourceServiceProvider.Registry.INSTANCE.getResourceServiceProvider(_createURI);
  }
  
  public <T extends Object> T withDomainObject(final Function1<? super JvmIdentifiableElement, ? extends T> lambda) {
    T _xblockexpression = null;
    {
      String _handleIdentifier = this.getHandleIdentifier();
      final IJavaElement javaElement = JavaCore.create(_handleIdentifier);
      XtextDomainObjectProvider _provider = this.getProvider();
      String _uri = this.getUri();
      URI _createURI = URI.createURI(_uri);
      final JvmDomainUtil domainUtil = ((JvmDomainObjectProvider) _provider).getJvmDomainUtil(_createURI);
      final JvmIdentifiableElement jvmElement = domainUtil.getJvmElement(javaElement);
      _xblockexpression = lambda.apply(jvmElement);
    }
    return _xblockexpression;
  }
  
  public IEditorPart openInEditor(final boolean isSelect) {
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
