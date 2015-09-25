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
import de.fxdiagram.eclipse.xtext.ids.XtextEObjectID;
import de.fxdiagram.mapping.AbstractMapping;
import de.fxdiagram.mapping.IMappedElementDescriptor;
import de.fxdiagram.mapping.IMappedElementDescriptorProvider;
import de.fxdiagram.mapping.XDiagramConfig;
import java.util.List;
import java.util.Map;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.ResourceSet;
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
import org.eclipse.xtext.resource.ILocationInFileProvider;
import org.eclipse.xtext.resource.IResourceServiceProvider;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.IURIEditorOpener;
import org.eclipse.xtext.ui.editor.XtextEditor;
import org.eclipse.xtext.ui.editor.model.IXtextDocument;
import org.eclipse.xtext.ui.shared.Access;
import org.eclipse.xtext.util.ITextRegion;
import org.eclipse.xtext.util.concurrent.IUnitOfWork;
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
  
  private final XtextEObjectID.Factory idFactory = new XtextEObjectID.Factory();
  
  @Override
  public DomainObjectDescriptor createDescriptor(final Object handle) {
    return null;
  }
  
  @Override
  public <T extends Object> IMappedElementDescriptor<T> createMappedElementDescriptor(final T domainObject, final AbstractMapping<? extends T> mapping) {
    final T it = domainObject;
    boolean _matched = false;
    if (!_matched) {
      if (it instanceof EObject) {
        _matched=true;
        boolean _eIsProxy = ((EObject)it).eIsProxy();
        if (_eIsProxy) {
          return null;
        }
        XtextEObjectID _createXtextEObjectID = this.createXtextEObjectID(((EObject)it));
        XDiagramConfig _config = mapping.getConfig();
        String _iD = _config.getID();
        String _iD_1 = mapping.getID();
        XtextEObjectDescriptor<EObject> _xtextEObjectDescriptor = new XtextEObjectDescriptor<EObject>(_createXtextEObjectID, _iD, _iD_1, this);
        return ((IMappedElementDescriptor<T>) _xtextEObjectDescriptor);
      }
    }
    if (!_matched) {
      if (it instanceof ESetting) {
        _matched=true;
        boolean _or = false;
        boolean _or_1 = false;
        EObject _owner = ((ESetting<?>)it).getOwner();
        boolean _equals = Objects.equal(_owner, null);
        if (_equals) {
          _or_1 = true;
        } else {
          EObject _owner_1 = ((ESetting<?>)it).getOwner();
          boolean _eIsProxy = _owner_1.eIsProxy();
          _or_1 = _eIsProxy;
        }
        if (_or_1) {
          _or = true;
        } else {
          EObject _target = ((ESetting<?>)it).getTarget();
          boolean _equals_1 = Objects.equal(_target, null);
          _or = _equals_1;
        }
        if (_or) {
          return null;
        }
        EObject _owner_2 = ((ESetting<?>)it).getOwner();
        XtextEObjectID _createXtextEObjectID = this.createXtextEObjectID(_owner_2);
        EObject _target_1 = ((ESetting<?>)it).getTarget();
        XtextEObjectID _createXtextEObjectID_1 = this.createXtextEObjectID(_target_1);
        EReference _reference = ((ESetting<?>)it).getReference();
        int _index = ((ESetting<?>)it).getIndex();
        XDiagramConfig _config = mapping.getConfig();
        String _iD = _config.getID();
        String _iD_1 = mapping.getID();
        return new XtextESettingDescriptor(_createXtextEObjectID, _createXtextEObjectID_1, _reference, _index, _iD, _iD_1, this);
      }
    }
    return null;
  }
  
  public XtextEObjectID createXtextEObjectID(final EObject element) {
    return this.idFactory.createXtextEObjectID(element);
  }
  
  /**
   * Avoids expensive switching of active parts on subsequent withDomainObject operations.
   */
  public IEditorPart getCachedEditor(final XtextEObjectID elementID, final boolean isSelect, final boolean isActivate) {
    IWorkbench _workbench = PlatformUI.getWorkbench();
    IWorkbenchWindow _activeWorkbenchWindow = _workbench.getActiveWorkbenchWindow();
    final IWorkbenchPage activePage = _activeWorkbenchWindow.getActivePage();
    URI _uRI = elementID.getURI();
    final URI resourceURI = _uRI.trimFragment();
    XtextDomainObjectProvider.CachedEditor _get = this.editorCache.get(resourceURI);
    IEditorPart _findOn = null;
    if (_get!=null) {
      _findOn=_get.findOn(activePage);
    }
    final IEditorPart cachedEditor = _findOn;
    if ((cachedEditor instanceof XtextEditor)) {
      if (isActivate) {
        IWorkbenchPartSite _site = ((XtextEditor)cachedEditor).getSite();
        IWorkbenchPage _page = _site.getPage();
        _page.activate(cachedEditor);
        if (isSelect) {
          IXtextDocument _document = ((XtextEditor)cachedEditor).getDocument();
          final IUnitOfWork<Object, XtextResource> _function = (XtextResource it) -> {
            Object _xblockexpression = null;
            {
              ResourceSet _resourceSet = it.getResourceSet();
              final EObject eObject = elementID.resolve(_resourceSet);
              IResourceServiceProvider _resourceServiceProvider = it.getResourceServiceProvider();
              final ILocationInFileProvider locationInFileProvider = _resourceServiceProvider.<ILocationInFileProvider>get(ILocationInFileProvider.class);
              final ITextRegion textRegion = locationInFileProvider.getSignificantTextRegion(eObject);
              boolean _notEquals = (!Objects.equal(textRegion, null));
              if (_notEquals) {
                int _offset = textRegion.getOffset();
                int _length = textRegion.getLength();
                ((XtextEditor)cachedEditor).selectAndReveal(_offset, _length);
              }
              _xblockexpression = null;
            }
            return _xblockexpression;
          };
          _document.<Object>readOnly(_function);
        }
      }
      return cachedEditor;
    }
    final IWorkbenchPart activePart = activePage.getActivePart();
    Provider<IURIEditorOpener> _iURIEditorOpener = Access.getIURIEditorOpener();
    IURIEditorOpener _get_1 = _iURIEditorOpener.get();
    final IEditorPart editor = _get_1.open(resourceURI, isSelect);
    XtextDomainObjectProvider.CachedEditor _cachedEditor = new XtextDomainObjectProvider.CachedEditor(editor);
    this.editorCache.put(resourceURI, _cachedEditor);
    if ((!isActivate)) {
      activePage.activate(activePart);
    }
    return editor;
  }
  
  public void populate(final ModelElementImpl modelElement) {
    
  }
}
