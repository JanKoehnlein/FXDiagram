package de.fxdiagram.xtext.glue;

import com.google.inject.Provider;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.ui.IEditorPart;
import org.eclipse.xtend.lib.Data;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.IURIEditorOpener;
import org.eclipse.xtext.ui.editor.XtextEditor;
import org.eclipse.xtext.ui.editor.model.IXtextDocument;
import org.eclipse.xtext.ui.shared.Access;
import org.eclipse.xtext.util.concurrent.IUnitOfWork;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.util.ToStringHelper;

@Data
@SuppressWarnings("all")
public class XtextEObjectHandle<ECLASS extends Object> {
  private final URI _uri;
  
  public URI getUri() {
    return this._uri;
  }
  
  public <T extends Object> T withEObject(final Function1<? super ECLASS,? extends T> lambda) {
    T _xblockexpression = null;
    {
      Provider<IURIEditorOpener> _iURIEditorOpener = Access.getIURIEditorOpener();
      IURIEditorOpener _get = _iURIEditorOpener.get();
      URI _uri = this.getUri();
      final IEditorPart editor = _get.open(_uri, true);
      T _xifexpression = null;
      if ((editor instanceof XtextEditor)) {
        IXtextDocument _document = ((XtextEditor)editor).getDocument();
        final IUnitOfWork<T,XtextResource> _function = new IUnitOfWork<T,XtextResource>() {
          public T exec(final XtextResource it) throws Exception {
            ResourceSet _resourceSet = it.getResourceSet();
            URI _uri = XtextEObjectHandle.this.getUri();
            EObject _eObject = _resourceSet.getEObject(_uri, true);
            return lambda.apply(((ECLASS) _eObject));
          }
        };
        _xifexpression = _document.<T>readOnly(_function);
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  public XtextEObjectHandle(final URI uri) {
    super();
    this._uri = uri;
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((_uri== null) ? 0 : _uri.hashCode());
    return result;
  }
  
  @Override
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    XtextEObjectHandle other = (XtextEObjectHandle) obj;
    if (_uri == null) {
      if (other._uri != null)
        return false;
    } else if (!_uri.equals(other._uri))
      return false;
    return true;
  }
  
  @Override
  public String toString() {
    String result = new ToStringHelper().toString(this);
    return result;
  }
}
