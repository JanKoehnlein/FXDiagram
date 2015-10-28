package de.fxdiagram.eclipse.ecore;

import com.google.common.base.Objects;
import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.core.model.ToString;
import de.fxdiagram.mapping.AbstractMappedElementDescriptor;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import org.eclipse.emf.common.ui.URIEditorInput;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorRegistry;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;

@ModelNode("uri")
@SuppressWarnings("all")
public class EcoreDomainObjectDescriptor extends AbstractMappedElementDescriptor<EObject> {
  public EcoreDomainObjectDescriptor(final String uri, final String name, final String mappingConfigID, final String mappingID) {
    super(mappingConfigID, mappingID);
    this.uriProperty.set(uri);
    this.nameProperty.set(name);
  }
  
  @Override
  public <U extends Object> U withDomainObject(final Function1<? super EObject, ? extends U> lambda) {
    try {
      String _uri = this.getUri();
      final URI theURI = URI.createURI(_uri);
      IWorkbench _workbench = PlatformUI.getWorkbench();
      IEditorRegistry _editorRegistry = _workbench.getEditorRegistry();
      String _lastSegment = theURI.lastSegment();
      IEditorDescriptor _defaultEditor = _editorRegistry.getDefaultEditor(_lastSegment);
      final String editorID = _defaultEditor.getId();
      IWorkbench _workbench_1 = PlatformUI.getWorkbench();
      IWorkbenchWindow _activeWorkbenchWindow = _workbench_1.getActiveWorkbenchWindow();
      IWorkbenchPage _activePage = _activeWorkbenchWindow.getActivePage();
      URI _trimFragment = theURI.trimFragment();
      URIEditorInput _uRIEditorInput = new URIEditorInput(_trimFragment);
      final IEditorPart editor = _activePage.openEditor(_uRIEditorInput, editorID);
      if ((editor instanceof IEditingDomainProvider)) {
        EditingDomain _editingDomain = ((IEditingDomainProvider)editor).getEditingDomain();
        ResourceSet _resourceSet = _editingDomain.getResourceSet();
        final EObject element = _resourceSet.getEObject(theURI, true);
        if ((element instanceof EObject)) {
          return lambda.apply(element);
        }
      }
      String _uri_1 = this.getUri();
      String _plus = ("Cannot resolve EObject " + _uri_1);
      throw new NoSuchElementException(_plus);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  @Override
  public Object openInEditor(final boolean select) {
    try {
      String _uri = this.getUri();
      final URI theURI = URI.createURI(_uri);
      IWorkbench _workbench = PlatformUI.getWorkbench();
      IEditorRegistry _editorRegistry = _workbench.getEditorRegistry();
      String _lastSegment = theURI.lastSegment();
      IEditorDescriptor _defaultEditor = _editorRegistry.getDefaultEditor(_lastSegment);
      final String editorID = _defaultEditor.getId();
      IWorkbench _workbench_1 = PlatformUI.getWorkbench();
      IWorkbenchWindow _activeWorkbenchWindow = _workbench_1.getActiveWorkbenchWindow();
      IWorkbenchPage _activePage = _activeWorkbenchWindow.getActivePage();
      URI _trimFragment = theURI.trimFragment();
      URIEditorInput _uRIEditorInput = new URIEditorInput(_trimFragment);
      final IEditorPart editor = _activePage.openEditor(_uRIEditorInput, editorID);
      if ((editor instanceof IEditingDomainProvider)) {
        EditingDomain _editingDomain = ((IEditingDomainProvider)editor).getEditingDomain();
        ResourceSet _resourceSet = _editingDomain.getResourceSet();
        final EObject element = _resourceSet.getEObject(theURI, true);
        this.setSelection(editor, element);
        return editor;
      }
      return null;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  protected Object setSelection(final IEditorPart editor, final EObject selectedElement) {
    Object _xtrycatchfinallyexpression = null;
    try {
      Object _xblockexpression = null;
      {
        Class<? extends IEditorPart> _class = editor.getClass();
        final Method method = _class.getMethod("setSelectionToViewer", Collection.class);
        List<EObject> _singletonList = Collections.<EObject>singletonList(selectedElement);
        _xblockexpression = method.invoke(editor, _singletonList);
      }
      _xtrycatchfinallyexpression = _xblockexpression;
    } catch (final Throwable _t) {
      if (_t instanceof Exception) {
        final Exception exc = (Exception)_t;
        exc.printStackTrace();
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    }
    return _xtrycatchfinallyexpression;
  }
  
  @Override
  public boolean equals(final Object obj) {
    if ((obj instanceof EcoreDomainObjectDescriptor)) {
      boolean _and = false;
      String _uri = ((EcoreDomainObjectDescriptor)obj).getUri();
      String _uri_1 = this.getUri();
      boolean _equals = Objects.equal(_uri, _uri_1);
      if (!_equals) {
        _and = false;
      } else {
        String _name = ((EcoreDomainObjectDescriptor)obj).getName();
        String _name_1 = this.getName();
        boolean _equals_1 = Objects.equal(_name, _name_1);
        _and = _equals_1;
      }
      return _and;
    } else {
      return false;
    }
  }
  
  @Override
  public int hashCode() {
    int _hashCode = super.hashCode();
    String _uri = this.getUri();
    int _hashCode_1 = _uri.hashCode();
    int _multiply = (563 * _hashCode_1);
    int _plus = (_hashCode + _multiply);
    String _name = this.getName();
    int _hashCode_2 = _name.hashCode();
    int _multiply_1 = (547 * _hashCode_2);
    return (_plus + _multiply_1);
  }
  
  /**
   * Automatically generated by @ModelNode. Needed for deserialization.
   */
  public EcoreDomainObjectDescriptor() {
  }
  
  public void populate(final ModelElementImpl modelElement) {
    super.populate(modelElement);
    modelElement.addProperty(uriProperty, String.class);
  }
  
  public String toString() {
    return ToString.toString(this);
  }
  
  private ReadOnlyStringWrapper uriProperty = new ReadOnlyStringWrapper(this, "uri");
  
  public String getUri() {
    return this.uriProperty.get();
  }
  
  public ReadOnlyStringProperty uriProperty() {
    return this.uriProperty.getReadOnlyProperty();
  }
  
  private ReadOnlyStringWrapper nameProperty = new ReadOnlyStringWrapper(this, "name");
  
  public String getName() {
    return this.nameProperty.get();
  }
  
  public ReadOnlyStringProperty nameProperty() {
    return this.nameProperty.getReadOnlyProperty();
  }
}
