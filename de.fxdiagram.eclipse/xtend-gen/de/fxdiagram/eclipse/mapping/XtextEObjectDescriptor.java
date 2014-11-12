package de.fxdiagram.eclipse.mapping;

import com.google.common.base.Objects;
import de.fxdiagram.eclipse.mapping.AbstractXtextDescriptor;
import de.fxdiagram.eclipse.mapping.XtextDomainObjectProvider;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.ui.IEditorPart;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.XtextEditor;
import org.eclipse.xtext.ui.editor.model.IXtextDocument;
import org.eclipse.xtext.util.concurrent.IUnitOfWork;
import org.eclipse.xtext.xbase.lib.Functions.Function1;

@SuppressWarnings("all")
public class XtextEObjectDescriptor<ECLASS extends EObject> extends AbstractXtextDescriptor<ECLASS> {
  public XtextEObjectDescriptor() {
  }
  
  public XtextEObjectDescriptor(final String uri, final String fqn, final String mappingConfigID, final String mappingID, final XtextDomainObjectProvider provider) {
    super(uri, fqn, mappingConfigID, mappingID, provider);
  }
  
  public <T extends Object> T withDomainObject(final Function1<? super ECLASS, ? extends T> lambda) {
    T _xblockexpression = null;
    {
      String _uri = this.getUri();
      final URI uriAsURI = URI.createURI(_uri);
      final IEditorPart editor = this.openInEditor(false);
      T _xifexpression = null;
      if ((editor instanceof XtextEditor)) {
        IXtextDocument _document = ((XtextEditor)editor).getDocument();
        final IUnitOfWork<T, XtextResource> _function = new IUnitOfWork<T, XtextResource>() {
          public T exec(final XtextResource it) throws Exception {
            ResourceSet _resourceSet = it.getResourceSet();
            EObject _eObject = _resourceSet.getEObject(uriAsURI, true);
            return lambda.apply(((ECLASS) _eObject));
          }
        };
        _xifexpression = _document.<T>readOnly(_function);
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  public boolean equals(final Object obj) {
    boolean _xifexpression = false;
    if ((obj instanceof XtextEObjectDescriptor<?>)) {
      String _uri = ((XtextEObjectDescriptor<?>)obj).getUri();
      String _uri_1 = this.getUri();
      _xifexpression = Objects.equal(_uri, _uri_1);
    } else {
      _xifexpression = false;
    }
    return _xifexpression;
  }
  
  public int hashCode() {
    String _uri = this.getUri();
    int _hashCode = _uri.hashCode();
    return (103 * _hashCode);
  }
}
