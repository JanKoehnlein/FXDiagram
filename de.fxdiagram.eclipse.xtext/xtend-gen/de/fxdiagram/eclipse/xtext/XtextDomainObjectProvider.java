package de.fxdiagram.eclipse.xtext;

import com.google.common.base.Objects;
import com.google.inject.Provider;
import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.core.model.DomainObjectProvider;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.eclipse.xtext.ESetting;
import de.fxdiagram.eclipse.xtext.XtextEObjectDescriptor;
import de.fxdiagram.eclipse.xtext.XtextESettingDescriptor;
import de.fxdiagram.mapping.AbstractMapping;
import de.fxdiagram.mapping.IMappedElementDescriptor;
import de.fxdiagram.mapping.IMappedElementDescriptorProvider;
import de.fxdiagram.mapping.XDiagramConfig;
import java.util.List;
import java.util.Map;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.xtext.naming.IQualifiedNameConverter;
import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.resource.IResourceServiceProvider;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.IURIEditorOpener;
import org.eclipse.xtext.ui.shared.Access;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;

/**
 * A {@link DomainObjectProvider} for Xtext based domain objects.
 */
@ModelNode
@SuppressWarnings("all")
public class XtextDomainObjectProvider implements IMappedElementDescriptorProvider {
  public static class CachedEditor {
    private IEditorInput editorInput;
    
    private String editorID;
    
    public CachedEditor(final IEditorPart editor) {
      IEditorInput _editorInput = editor.getEditorInput();
      this.editorInput = _editorInput;
      IEditorSite _editorSite = editor.getEditorSite();
      String _id = _editorSite.getId();
      this.editorID = _id;
    }
    
    public IEditorPart findOn(final IWorkbenchPage page) {
      IEditorReference[] _findEditors = page.findEditors(this.editorInput, this.editorID, (IWorkbenchPage.MATCH_ID + IWorkbenchPage.MATCH_INPUT));
      List<IEditorPart> _map = null;
      if (((List<IEditorReference>)Conversions.doWrapArray(_findEditors))!=null) {
        final Function1<IEditorReference, IEditorPart> _function = (IEditorReference it) -> {
          return it.getEditor(false);
        };
        _map=ListExtensions.<IEditorReference, IEditorPart>map(((List<IEditorReference>)Conversions.doWrapArray(_findEditors)), _function);
      }
      Iterable<IEditorPart> _filterNull = null;
      if (_map!=null) {
        _filterNull=IterableExtensions.<IEditorPart>filterNull(_map);
      }
      IEditorPart _head = null;
      if (_filterNull!=null) {
        _head=IterableExtensions.<IEditorPart>head(_filterNull);
      }
      return _head;
    }
  }
  
  private Map<URI, XtextDomainObjectProvider.CachedEditor> editorCache = CollectionLiterals.<URI, XtextDomainObjectProvider.CachedEditor>newHashMap();
  
  @Override
  public DomainObjectDescriptor createDescriptor(final Object handle) {
    return null;
  }
  
  public String getFullyQualifiedName(final EObject domainObject) {
    final Resource resource = domainObject.eResource();
    IResourceServiceProvider _xifexpression = null;
    if ((resource instanceof XtextResource)) {
      _xifexpression = ((XtextResource)resource).getResourceServiceProvider();
    } else {
      URI _uRI = resource.getURI();
      _xifexpression = IResourceServiceProvider.Registry.INSTANCE.getResourceServiceProvider(_uRI);
    }
    final IResourceServiceProvider resourceServiceProvider = _xifexpression;
    IQualifiedNameProvider _get = null;
    if (resourceServiceProvider!=null) {
      _get=resourceServiceProvider.<IQualifiedNameProvider>get(IQualifiedNameProvider.class);
    }
    QualifiedName _fullyQualifiedName = null;
    if (_get!=null) {
      _fullyQualifiedName=_get.getFullyQualifiedName(domainObject);
    }
    final QualifiedName qualifiedName = _fullyQualifiedName;
    boolean _notEquals = (!Objects.equal(qualifiedName, null));
    if (_notEquals) {
      IQualifiedNameConverter _get_1 = resourceServiceProvider.<IQualifiedNameConverter>get(IQualifiedNameConverter.class);
      String _string = null;
      if (_get_1!=null) {
        _string=_get_1.toString(qualifiedName);
      }
      return _string;
    } else {
      return "unnamed";
    }
  }
  
  @Override
  public <T extends Object> IMappedElementDescriptor<T> createMappedElementDescriptor(final T domainObject, final AbstractMapping<T> mapping) {
    final T it = domainObject;
    boolean _matched = false;
    if (!_matched) {
      if (it instanceof EObject) {
        _matched=true;
        URI _uRI = EcoreUtil.getURI(((EObject)it));
        String _string = _uRI.toString();
        String _fullyQualifiedName = this.getFullyQualifiedName(((EObject)it));
        XDiagramConfig _config = mapping.getConfig();
        String _iD = _config.getID();
        String _iD_1 = mapping.getID();
        XtextEObjectDescriptor<EObject> _xtextEObjectDescriptor = new XtextEObjectDescriptor<EObject>(_string, _fullyQualifiedName, _iD, _iD_1, this);
        return ((IMappedElementDescriptor<T>) _xtextEObjectDescriptor);
      }
    }
    if (!_matched) {
      if (it instanceof ESetting) {
        _matched=true;
        EObject _owner = ((ESetting<?>)it).getOwner();
        URI _uRI = EcoreUtil.getURI(_owner);
        String _string = _uRI.toString();
        EObject _owner_1 = ((ESetting<?>)it).getOwner();
        String _fullyQualifiedName = this.getFullyQualifiedName(_owner_1);
        EReference _reference = ((ESetting<?>)it).getReference();
        int _index = ((ESetting<?>)it).getIndex();
        XDiagramConfig _config = mapping.getConfig();
        String _iD = _config.getID();
        String _iD_1 = mapping.getID();
        return new XtextESettingDescriptor(_string, _fullyQualifiedName, _reference, _index, _iD, _iD_1, this);
      }
    }
    return null;
  }
  
  /**
   * Avoids expensive switching of active parts on subsequent withDomainObject operations.
   */
  public IEditorPart getCachedEditor(final URI elementURI, final boolean isSelect, final boolean isActivate) {
    final URI uri = elementURI.trimFragment();
    IWorkbench _workbench = PlatformUI.getWorkbench();
    IWorkbenchWindow _activeWorkbenchWindow = _workbench.getActiveWorkbenchWindow();
    final IWorkbenchPage activePage = _activeWorkbenchWindow.getActivePage();
    XtextDomainObjectProvider.CachedEditor _get = this.editorCache.get(uri);
    IEditorPart _findOn = null;
    if (_get!=null) {
      _findOn=_get.findOn(activePage);
    }
    final IEditorPart cachedEditor = _findOn;
    boolean _notEquals = (!Objects.equal(cachedEditor, null));
    if (_notEquals) {
      if (isActivate) {
        IWorkbenchPartSite _site = cachedEditor.getSite();
        IWorkbenchPage _page = _site.getPage();
        _page.activate(cachedEditor);
      }
      return cachedEditor;
    }
    final IWorkbenchPart activePart = activePage.getActivePart();
    Provider<IURIEditorOpener> _iURIEditorOpener = Access.getIURIEditorOpener();
    IURIEditorOpener _get_1 = _iURIEditorOpener.get();
    final IEditorPart editor = _get_1.open(elementURI, isSelect);
    XtextDomainObjectProvider.CachedEditor _cachedEditor = new XtextDomainObjectProvider.CachedEditor(editor);
    this.editorCache.put(uri, _cachedEditor);
    if ((!isActivate)) {
      activePage.activate(activePart);
    }
    return editor;
  }
  
  public void populate(final ModelElementImpl modelElement) {
    
  }
}
