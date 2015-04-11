package de.fxdiagram.eclipse.xtext;

import com.google.common.base.Objects;
import com.google.inject.Injector;
import de.fxdiagram.annotations.logging.Logging;
import de.fxdiagram.core.model.DomainObjectProvider;
import de.fxdiagram.eclipse.xtext.XtextDomainObjectProvider;
import de.fxdiagram.mapping.AbstractMappedElementDescriptor;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyStringProperty;
import org.eclipse.emf.common.util.URI;
import org.eclipse.xtext.resource.IResourceServiceProvider;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@Logging
@SuppressWarnings("all")
public abstract class AbstractXtextDescriptor<ECLASS_OR_ESETTING extends Object> extends AbstractMappedElementDescriptor<ECLASS_OR_ESETTING> {
  public AbstractXtextDescriptor() {
  }
  
  public AbstractXtextDescriptor(final String uri, final String fqn, final String mappingConfigID, final String mappingID, final XtextDomainObjectProvider provider) {
    super(uri, fqn, mappingConfigID, mappingID, provider);
  }
  
  @Override
  public String getName() {
    ReadOnlyStringProperty _nameProperty = this.nameProperty();
    String _get = _nameProperty.get();
    String[] _split = null;
    if (_get!=null) {
      _split=_get.split("\\.");
    }
    String _last = null;
    if (((Iterable<String>)Conversions.doWrapArray(_split))!=null) {
      _last=IterableExtensions.<String>last(((Iterable<String>)Conversions.doWrapArray(_split)));
    }
    return _last;
  }
  
  public String getFqn() {
    ReadOnlyStringProperty _nameProperty = this.nameProperty();
    return _nameProperty.get();
  }
  
  public String getUri() {
    return this.getId();
  }
  
  @Override
  public Object openInEditor(final boolean isSelect) {
    DomainObjectProvider _provider = this.getProvider();
    String _uri = this.getUri();
    URI _createURI = URI.createURI(_uri);
    return ((XtextDomainObjectProvider) _provider).getCachedEditor(_createURI, isSelect, isSelect);
  }
  
  public void injectMembers(final Object it) {
    final IResourceServiceProvider resourceServiceProvider = this.getResourceServiceProvider();
    boolean _equals = Objects.equal(resourceServiceProvider, null);
    if (_equals) {
      String _uri = this.getUri();
      String _plus = ("Cannot find IResourceServiceProvider for " + _uri);
      AbstractXtextDescriptor.LOG.severe(_plus);
    } else {
      Injector _get = resourceServiceProvider.<Injector>get(Injector.class);
      _get.injectMembers(it);
    }
  }
  
  protected IResourceServiceProvider getResourceServiceProvider() {
    String _uri = this.getUri();
    URI _createURI = URI.createURI(_uri);
    return IResourceServiceProvider.Registry.INSTANCE.getResourceServiceProvider(_createURI);
  }
  
  private static Logger LOG = Logger.getLogger("de.fxdiagram.eclipse.xtext.AbstractXtextDescriptor");
    ;
}
