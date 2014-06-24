package de.fxdiagram.xtext.glue;

import com.google.common.base.Objects;
import com.google.inject.Provider;
import de.fxdiagram.annotations.logging.Logging;
import de.fxdiagram.annotations.properties.FxProperty;
import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.xtext.glue.XtextDomainObjectProvider;
import de.fxdiagram.xtext.glue.mapping.AbstractMapping;
import de.fxdiagram.xtext.glue.mapping.XDiagramConfig;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.ui.IEditorPart;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.IURIEditorOpener;
import org.eclipse.xtext.ui.editor.XtextEditor;
import org.eclipse.xtext.ui.editor.model.IXtextDocument;
import org.eclipse.xtext.ui.shared.Access;
import org.eclipse.xtext.util.concurrent.IUnitOfWork;
import org.eclipse.xtext.xbase.lib.Functions.Function1;

@ModelNode({ "provider", "uri", "fqn", "mappingConfigID", "mappingID" })
@Logging
@SuppressWarnings("all")
public class XtextDomainObjectDescriptor<ECLASS extends Object> implements DomainObjectDescriptor {
  /* @FxProperty(/* name is null */)
   */private XtextDomainObjectProvider provider;
  
  /* @FxProperty(/* name is null */)
   */private String fqn;
  
  /* @FxProperty(/* name is null */)
   */private String uri;
  
  /* @FxProperty(/* name is null */)
   */private String mappingConfigID;
  
  /* @FxProperty(/* name is null */)
   */private String mappingID;
  
  private AbstractMapping<ECLASS> mapping;
  
  public XtextDomainObjectDescriptor(final String uri, final String fqn, final String mappingConfigID, final String mappingID, final XtextDomainObjectProvider provider) {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method or field uriProperty is undefined for the type XtextDomainObjectDescriptor"
      + "\nThe method or field fqnProperty is undefined for the type XtextDomainObjectDescriptor"
      + "\nThe method or field providerProperty is undefined for the type XtextDomainObjectDescriptor"
      + "\nThe method or field mappingIDProperty is undefined for the type XtextDomainObjectDescriptor"
      + "\nThe method or field mappingConfigIDProperty is undefined for the type XtextDomainObjectDescriptor"
      + "\nset cannot be resolved"
      + "\nset cannot be resolved"
      + "\nset cannot be resolved"
      + "\nset cannot be resolved"
      + "\nset cannot be resolved");
  }
  
  public String getName() {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method or field getFqn is undefined for the type XtextDomainObjectDescriptor"
      + "\nsplit cannot be resolved"
      + "\nlast cannot be resolved");
  }
  
  public String getId() {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method or field getFqn is undefined for the type XtextDomainObjectDescriptor");
  }
  
  public AbstractMapping<ECLASS> getMapping() {
    AbstractMapping<ECLASS> _xblockexpression = null;
    {
      boolean _equals = Objects.equal(this.mapping, null);
      if (_equals) {
        XDiagramConfig.Registry _instance = XDiagramConfig.Registry.getInstance();
        final XDiagramConfig config = _instance.getConfigByID(this.mappingConfigID);
        AbstractMapping<?> _mappingByID = config.getMappingByID(this.mappingID);
        this.mapping = ((AbstractMapping<ECLASS>) _mappingByID);
      }
      _xblockexpression = this.mapping;
    }
    return _xblockexpression;
  }
  
  public <T extends Object> T withDomainObject(final Function1<? super ECLASS, ? extends T> lambda) {
    T _xblockexpression = null;
    {
      final URI uriAsURI = URI.createURI(this.uri);
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
    if ((obj instanceof XtextDomainObjectDescriptor<?>)) {
      _xifexpression = Objects.equal(((XtextDomainObjectDescriptor<?>)obj).uri, this.uri);
    } else {
      _xifexpression = false;
    }
    return _xifexpression;
  }
  
  public int hashCode() {
    int _hashCode = this.uri.hashCode();
    return (103 * _hashCode);
  }
  
  public IEditorPart openInEditor(final boolean isSelect) {
    Provider<IURIEditorOpener> _iURIEditorOpener = Access.getIURIEditorOpener();
    IURIEditorOpener _get = _iURIEditorOpener.get();
    URI _createURI = URI.createURI(this.uri);
    return _get.open(_createURI, isSelect);
  }
  
  public Object injectMembers(final Object it) {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method or field LOG is undefined for the type XtextDomainObjectDescriptor"
      + "\nsevere cannot be resolved");
  }
}
