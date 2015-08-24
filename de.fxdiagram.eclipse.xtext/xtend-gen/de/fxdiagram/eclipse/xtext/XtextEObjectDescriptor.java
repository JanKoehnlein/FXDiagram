package de.fxdiagram.eclipse.xtext;

import com.google.common.base.Objects;
import com.google.inject.Injector;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.eclipse.xtext.AbstractXtextDescriptor;
import de.fxdiagram.eclipse.xtext.XtextDomainObjectProvider;
import java.util.NoSuchElementException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.XtextEditor;
import org.eclipse.xtext.ui.editor.model.IXtextDocument;
import org.eclipse.xtext.util.concurrent.IUnitOfWork;
import org.eclipse.xtext.xbase.lib.Functions.Function1;

/**
 * A {@link DomainObjectDescriptor} that points to an {@link EObject} from an Xtext document.
 * 
 * Xtext objects only exist in a read-only transaction on an Xtext editor's document.
 * They can be recovered from their URI.
 * The descriptor also allows to use the {@link Injector} of the langage the domain object
 * belongs to.
 */
@SuppressWarnings("all")
public class XtextEObjectDescriptor<ECLASS extends EObject> extends AbstractXtextDescriptor<ECLASS> {
  public XtextEObjectDescriptor() {
  }
  
  public XtextEObjectDescriptor(final String uri, final String fqn, final String mappingConfigID, final String mappingID, final XtextDomainObjectProvider provider) {
    super(uri, fqn, mappingConfigID, mappingID, provider);
  }
  
  @Override
  public <T extends Object> T withDomainObject(final Function1<? super ECLASS, ? extends T> lambda) {
    T _xblockexpression = null;
    {
      String _uri = this.getUri();
      final URI uriAsURI = URI.createURI(_uri);
      final Object editor = this.openInEditor(false);
      T _xifexpression = null;
      if ((editor instanceof XtextEditor)) {
        IXtextDocument _document = ((XtextEditor)editor).getDocument();
        final IUnitOfWork<T, XtextResource> _function = (XtextResource it) -> {
          T _xblockexpression_1 = null;
          {
            ResourceSet _resourceSet = it.getResourceSet();
            final EObject domainObject = _resourceSet.getEObject(uriAsURI, true);
            boolean _equals = Objects.equal(domainObject, null);
            if (_equals) {
              String _fqn = this.getFqn();
              String _plus = ("Xtext element " + _fqn);
              String _plus_1 = (_plus + " does not exist");
              throw new NoSuchElementException(_plus_1);
            }
            _xblockexpression_1 = lambda.apply(((ECLASS) domainObject));
          }
          return _xblockexpression_1;
        };
        _xifexpression = _document.<T>readOnly(_function);
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  @Override
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
  
  @Override
  public int hashCode() {
    String _uri = this.getUri();
    int _hashCode = _uri.hashCode();
    return (103 * _hashCode);
  }
}
