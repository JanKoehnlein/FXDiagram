package de.fxdiagram.xtext.glue.mapping;

import com.google.common.base.Objects;
import com.google.inject.Injector;
import com.google.inject.Provider;
import de.fxdiagram.annotations.logging.Logging;
import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.xtext.glue.mapping.AbstractMapping;
import de.fxdiagram.xtext.glue.mapping.XDiagramConfig;
import de.fxdiagram.xtext.glue.mapping.XtextDomainObjectProvider;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import org.eclipse.emf.common.util.URI;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.xtext.resource.IResourceServiceProvider;
import org.eclipse.xtext.ui.editor.IURIEditorOpener;
import org.eclipse.xtext.ui.shared.Access;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@ModelNode({ "provider", "uri", "fqn", "mappingConfigID", "mappingID" })
@Logging
@SuppressWarnings("all")
public abstract class AbstractXtextDescriptor<ECLASS_OR_ESETTING extends Object> implements DomainObjectDescriptor {
  private AbstractMapping<ECLASS_OR_ESETTING> mapping;
  
  public AbstractXtextDescriptor(final String uri, final String fqn, final String mappingConfigID, final String mappingID, final XtextDomainObjectProvider provider) {
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
  
  public AbstractMapping<ECLASS_OR_ESETTING> getMapping() {
    AbstractMapping<ECLASS_OR_ESETTING> _xblockexpression = null;
    {
      boolean _equals = Objects.equal(this.mapping, null);
      if (_equals) {
        XDiagramConfig.Registry _instance = XDiagramConfig.Registry.getInstance();
        String _mappingConfigID = this.getMappingConfigID();
        final XDiagramConfig config = _instance.getConfigByID(_mappingConfigID);
        String _mappingID = this.getMappingID();
        AbstractMapping<?> _mappingByID = config.getMappingByID(_mappingID);
        this.mapping = ((AbstractMapping<ECLASS_OR_ESETTING>) _mappingByID);
      }
      _xblockexpression = this.mapping;
    }
    return _xblockexpression;
  }
  
  public abstract <T extends Object> T withDomainObject(final Function1<? super ECLASS_OR_ESETTING, ? extends T> lambda);
  
  public IEditorPart openInEditor(final boolean isSelect) {
    IEditorPart _xifexpression = null;
    if (isSelect) {
      Provider<IURIEditorOpener> _iURIEditorOpener = Access.getIURIEditorOpener();
      IURIEditorOpener _get = _iURIEditorOpener.get();
      String _uri = this.getUri();
      URI _createURI = URI.createURI(_uri);
      _xifexpression = _get.open(_createURI, isSelect);
    } else {
      IWorkbench _workbench = PlatformUI.getWorkbench();
      IWorkbenchWindow _activeWorkbenchWindow = _workbench.getActiveWorkbenchWindow();
      final IWorkbenchPage activePage = _activeWorkbenchWindow.getActivePage();
      final IWorkbenchPart activePart = activePage.getActivePart();
      Provider<IURIEditorOpener> _iURIEditorOpener_1 = Access.getIURIEditorOpener();
      IURIEditorOpener _get_1 = _iURIEditorOpener_1.get();
      String _uri_1 = this.getUri();
      URI _createURI_1 = URI.createURI(_uri_1);
      final IEditorPart editor = _get_1.open(_createURI_1, isSelect);
      activePage.activate(activePart);
      return editor;
    }
    return _xifexpression;
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
  
  /**
   * Automatically generated by @ModelNode. Needed for deserialization.
   */
  public AbstractXtextDescriptor() {
  }
  
  public void populate(final ModelElementImpl modelElement) {
    modelElement.addProperty(providerProperty, XtextDomainObjectProvider.class);
    modelElement.addProperty(uriProperty, String.class);
    modelElement.addProperty(fqnProperty, String.class);
    modelElement.addProperty(mappingConfigIDProperty, String.class);
    modelElement.addProperty(mappingIDProperty, String.class);
  }
  
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
  
  private static Logger LOG = Logger.getLogger("de.fxdiagram.xtext.glue.mapping.AbstractXtextDescriptor");
    ;
}
