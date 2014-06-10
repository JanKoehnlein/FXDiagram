package de.fxdiagram.xtext.glue;

import com.google.common.base.Objects;
import com.google.inject.Injector;
import com.google.inject.Provider;
import de.fxdiagram.annotations.logging.Logging;
import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.xtext.glue.XtextDomainObjectProvider;
import de.fxdiagram.xtext.glue.mapping.AbstractMapping;
import de.fxdiagram.xtext.glue.mapping.XDiagramConfig;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.ui.IEditorPart;
import org.eclipse.xtext.resource.IResourceServiceProvider;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.IURIEditorOpener;
import org.eclipse.xtext.ui.editor.XtextEditor;
import org.eclipse.xtext.ui.editor.model.IXtextDocument;
import org.eclipse.xtext.ui.shared.Access;
import org.eclipse.xtext.util.concurrent.IUnitOfWork;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@ModelNode({ "provider", "uri", "fqn", "mappingConfigID", "mappingID" })
@Logging
@SuppressWarnings("all")
public class XtextDomainObjectDescriptor<ECLASS extends Object> implements DomainObjectDescriptor {
  private AbstractMapping<ECLASS> mapping;
  
  public XtextDomainObjectDescriptor(final String uri, final String fqn, final String mappingConfigID, final String mappingID, final XtextDomainObjectProvider provider) {
    this.uriProperty.set(uri);
    this.fqnProperty.set(fqn);
    this.providerProperty.set(provider);
    this.mappingIDProperty.set(mappingID);
    this.mappingConfigIDProperty.set(mappingConfigID);
  }
  
  public String getName() {
    String _fqn = this.getFqn();
    String[] _split = null;
    if (_fqn!=null) {
      _split=_fqn.split("\\.");
    }
    String _last = null;
    if (((Iterable<String>)Conversions.doWrapArray(_split))!=null) {
      _last=IterableExtensions.<String>last(((Iterable<String>)Conversions.doWrapArray(_split)));
    }
    return _last;
  }
  
  public String getId() {
    return this.getFqn();
  }
  
  public AbstractMapping<ECLASS> getMapping() {
    AbstractMapping<ECLASS> _xblockexpression = null;
    {
      boolean _equals = Objects.equal(this.mapping, null);
      if (_equals) {
        XDiagramConfig.Registry _instance = XDiagramConfig.Registry.getInstance();
        String _mappingConfigID = this.getMappingConfigID();
        final XDiagramConfig config = _instance.getConfigByID(_mappingConfigID);
        String _mappingID = this.getMappingID();
        AbstractMapping<?> _mappingByID = config.getMappingByID(_mappingID);
        this.mapping = ((AbstractMapping<ECLASS>) _mappingByID);
      }
      _xblockexpression = this.mapping;
    }
    return _xblockexpression;
  }
  
  public <T extends Object> T withDomainObject(final Function1<? super ECLASS, ? extends T> lambda) {
    T _xblockexpression = null;
    {
      String _uri = this.getUri();
      final URI uriAsURI = URI.createURI(_uri);
      final IEditorPart editor = this.revealInEditor();
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
      String _uri = ((XtextDomainObjectDescriptor<?>)obj).getUri();
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
  
  public IEditorPart revealInEditor() {
    Provider<IURIEditorOpener> _iURIEditorOpener = Access.getIURIEditorOpener();
    IURIEditorOpener _get = _iURIEditorOpener.get();
    String _uri = this.getUri();
    URI _createURI = URI.createURI(_uri);
    return _get.open(_createURI, true);
  }
  
  public void injectMembers(final Object it) {
    String _uri = this.getUri();
    URI _createURI = URI.createURI(_uri);
    final IResourceServiceProvider resourceServiceProvider = IResourceServiceProvider.Registry.INSTANCE.getResourceServiceProvider(_createURI);
    boolean _equals = Objects.equal(resourceServiceProvider, null);
    if (_equals) {
      String _uri_1 = this.getUri();
      String _plus = ("Cannot find IResourceServiceProvider for " + _uri_1);
      XtextDomainObjectDescriptor.LOG.severe(_plus);
    } else {
      Injector _get = resourceServiceProvider.<Injector>get(Injector.class);
      _get.injectMembers(it);
    }
  }
  
  /**
   * Automatically generated by @ModelNode. Needed for deserialization.
   */
  public XtextDomainObjectDescriptor() {
  }
  
  public void populate(final ModelElementImpl modelElement) {
    modelElement.addProperty(providerProperty, XtextDomainObjectProvider.class);
    modelElement.addProperty(uriProperty, String.class);
    modelElement.addProperty(fqnProperty, String.class);
    modelElement.addProperty(mappingConfigIDProperty, String.class);
    modelElement.addProperty(mappingIDProperty, String.class);
  }
  
  private static Logger LOG = Logger.getLogger("de.fxdiagram.xtext.glue.XtextDomainObjectDescriptor");
    ;
  
  private ReadOnlyObjectWrapper<XtextDomainObjectProvider> providerProperty = new ReadOnlyObjectWrapper<XtextDomainObjectProvider>(this, "provider");
  
  public XtextDomainObjectProvider getProvider() {
    return this.providerProperty.get();
  }
  
  public ReadOnlyObjectProperty<XtextDomainObjectProvider> providerProperty() {
    return this.providerProperty.getReadOnlyProperty();
  }
  
  private ReadOnlyStringWrapper fqnProperty = new ReadOnlyStringWrapper(this, "fqn");
  
  public String getFqn() {
    return this.fqnProperty.get();
  }
  
  public ReadOnlyStringProperty fqnProperty() {
    return this.fqnProperty.getReadOnlyProperty();
  }
  
  private ReadOnlyStringWrapper uriProperty = new ReadOnlyStringWrapper(this, "uri");
  
  public String getUri() {
    return this.uriProperty.get();
  }
  
  public ReadOnlyStringProperty uriProperty() {
    return this.uriProperty.getReadOnlyProperty();
  }
  
  private ReadOnlyStringWrapper mappingConfigIDProperty = new ReadOnlyStringWrapper(this, "mappingConfigID");
  
  public String getMappingConfigID() {
    return this.mappingConfigIDProperty.get();
  }
  
  public ReadOnlyStringProperty mappingConfigIDProperty() {
    return this.mappingConfigIDProperty.getReadOnlyProperty();
  }
  
  private ReadOnlyStringWrapper mappingIDProperty = new ReadOnlyStringWrapper(this, "mappingID");
  
  public String getMappingID() {
    return this.mappingIDProperty.get();
  }
  
  public ReadOnlyStringProperty mappingIDProperty() {
    return this.mappingIDProperty.getReadOnlyProperty();
  }
}
