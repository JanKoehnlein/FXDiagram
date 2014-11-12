package de.fxdiagram.eclipse.mapping;

import com.google.common.base.Objects;
import com.google.inject.Injector;
import com.google.inject.Provider;
import de.fxdiagram.annotations.logging.Logging;
import de.fxdiagram.eclipse.mapping.AbstractMappedElementDescriptor;
import de.fxdiagram.eclipse.mapping.XtextDomainObjectProvider;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyStringProperty;
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
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@Logging
@SuppressWarnings("all")
public abstract class AbstractXtextDescriptor<ECLASS_OR_ESETTING extends Object> extends AbstractMappedElementDescriptor<ECLASS_OR_ESETTING> {
  public AbstractXtextDescriptor() {
  }
  
  public AbstractXtextDescriptor(final String uri, final String fqn, final String mappingConfigID, final String mappingID, final XtextDomainObjectProvider provider) {
    super(uri, fqn, mappingConfigID, mappingID, provider);
  }
  
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
  
  private static Logger LOG = Logger.getLogger("de.fxdiagram.eclipse.mapping.AbstractXtextDescriptor");
    ;
}
