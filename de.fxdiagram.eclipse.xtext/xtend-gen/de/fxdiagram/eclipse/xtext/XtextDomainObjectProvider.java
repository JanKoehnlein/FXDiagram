package de.fxdiagram.eclipse.xtext;

import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.core.model.DomainObjectProvider;
import de.fxdiagram.eclipse.xtext.EObjectDescriptionDescriptor;
import de.fxdiagram.eclipse.xtext.ESetting;
import de.fxdiagram.eclipse.xtext.XtextEObjectDescriptor;
import de.fxdiagram.eclipse.xtext.XtextESettingDescriptor;
import de.fxdiagram.eclipse.xtext.ids.XtextEObjectID;
import de.fxdiagram.mapping.AbstractMapping;
import de.fxdiagram.mapping.IMappedElementDescriptor;
import de.fxdiagram.mapping.IMappedElementDescriptorProvider;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import org.eclipse.core.resources.IProject;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.resource.ILocationInFileProvider;
import org.eclipse.xtext.resource.IResourceDescriptions;
import org.eclipse.xtext.resource.IResourceServiceProvider;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.impl.ResourceDescriptionsProvider;
import org.eclipse.xtext.ui.editor.XtextEditor;
import org.eclipse.xtext.ui.refactoring.impl.ProjectUtil;
import org.eclipse.xtext.ui.resource.IResourceSetProvider;
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
@SuppressWarnings("all")
public class XtextDomainObjectProvider implements IMappedElementDescriptorProvider {
  public static class CachedEditor {
    private IEditorInput editorInput;
    
    private String editorID;
    
    public CachedEditor(final IEditorPart editor) {
      this.editorInput = editor.getEditorInput();
      this.editorID = editor.getEditorSite().getId();
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
  
  public XtextEObjectID.Factory getIdFactory() {
    return new XtextEObjectID.Factory();
  }
  
  @Override
  public DomainObjectDescriptor createDescriptor(final Object handle) {
    return null;
  }
  
  @Override
  public <T extends Object> IMappedElementDescriptor<T> createMappedElementDescriptor(final T domainObject, final AbstractMapping<? extends T> mapping) {
    final T it = domainObject;
    boolean _matched = false;
    if (it instanceof IEObjectDescription) {
      _matched=true;
      XtextEObjectID _createXtextEObjectID = this.createXtextEObjectID(((IEObjectDescription)it));
      String _iD = mapping.getConfig().getID();
      String _iD_1 = mapping.getID();
      EObjectDescriptionDescriptor _eObjectDescriptionDescriptor = new EObjectDescriptionDescriptor(_createXtextEObjectID, _iD, _iD_1);
      return ((IMappedElementDescriptor<T>) _eObjectDescriptionDescriptor);
    }
    if (!_matched) {
      if (it instanceof EObject) {
        _matched=true;
        boolean _eIsProxy = ((EObject)it).eIsProxy();
        if (_eIsProxy) {
          return null;
        }
        XtextEObjectID _createXtextEObjectID = this.createXtextEObjectID(((EObject)it));
        String _iD = mapping.getConfig().getID();
        String _iD_1 = mapping.getID();
        XtextEObjectDescriptor<EObject> _xtextEObjectDescriptor = new XtextEObjectDescriptor<EObject>(_createXtextEObjectID, _iD, _iD_1);
        return ((IMappedElementDescriptor<T>) _xtextEObjectDescriptor);
      }
    }
    if (!_matched) {
      if (it instanceof ESetting) {
        _matched=true;
        if ((((((ESetting<?>)it).getOwner() == null) || ((ESetting<?>)it).getOwner().eIsProxy()) || (((ESetting<?>)it).getTarget() == null))) {
          return null;
        }
        XtextEObjectID _createXtextEObjectID = this.createXtextEObjectID(((ESetting<?>)it).getOwner());
        XtextEObjectID _createXtextEObjectID_1 = this.createXtextEObjectID(((ESetting<?>)it).getTarget());
        EReference _reference = ((ESetting<?>)it).getReference();
        int _index = ((ESetting<?>)it).getIndex();
        String _iD = mapping.getConfig().getID();
        String _iD_1 = mapping.getID();
        return new XtextESettingDescriptor(_createXtextEObjectID, _createXtextEObjectID_1, _reference, _index, _iD, _iD_1);
      }
    }
    return null;
  }
  
  public XtextEObjectID createXtextEObjectID(final EObject element) {
    return this.getIdFactory().createXtextEObjectID(element);
  }
  
  public XtextEObjectID createXtextEObjectID(final IEObjectDescription element) {
    return this.getIdFactory().createXtextEObjectID(element);
  }
  
  public IResourceDescriptions getIndex(final XtextEObjectID context) {
    IResourceDescriptions _xblockexpression = null;
    {
      final IResourceServiceProvider rsp = context.getResourceServiceProvider();
      ProjectUtil _get = rsp.<ProjectUtil>get(ProjectUtil.class);
      IProject _project = null;
      if (_get!=null) {
        _project=_get.getProject(context.getURI());
      }
      final IProject project = _project;
      if ((project == null)) {
        URI _uRI = context.getURI();
        String _plus = ("Project " + _uRI);
        String _plus_1 = (_plus + " does not exist");
        throw new NoSuchElementException(_plus_1);
      }
      final ResourceSet resourceSet = rsp.<IResourceSetProvider>get(IResourceSetProvider.class).get(project);
      resourceSet.getLoadOptions().put(ResourceDescriptionsProvider.PERSISTED_DESCRIPTIONS, Boolean.valueOf(true));
      _xblockexpression = rsp.<ResourceDescriptionsProvider>get(ResourceDescriptionsProvider.class).getResourceDescriptions(resourceSet);
    }
    return _xblockexpression;
  }
  
  /**
   * Avoids expensive switching of active parts on subsequent withDomainObject operations.
   */
  public IEditorPart getCachedEditor(final XtextEObjectID elementID, final boolean isSelect, final boolean isActivate) {
    final IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
    final URI resourceURI = elementID.getURI().trimFragment();
    XtextDomainObjectProvider.CachedEditor _get = this.editorCache.get(resourceURI);
    IEditorPart _findOn = null;
    if (_get!=null) {
      _findOn=_get.findOn(activePage);
    }
    final IEditorPart cachedEditor = _findOn;
    if ((cachedEditor instanceof XtextEditor)) {
      if (isActivate) {
        ((XtextEditor)cachedEditor).getSite().getPage().activate(cachedEditor);
        if (isSelect) {
          final IUnitOfWork<Object, XtextResource> _function = (XtextResource it) -> {
            Object _xblockexpression = null;
            {
              final EObject eObject = elementID.resolve(it.getResourceSet());
              final ILocationInFileProvider locationInFileProvider = it.getResourceServiceProvider().<ILocationInFileProvider>get(ILocationInFileProvider.class);
              final ITextRegion textRegion = locationInFileProvider.getSignificantTextRegion(eObject);
              if ((textRegion != null)) {
                ((XtextEditor)cachedEditor).selectAndReveal(textRegion.getOffset(), textRegion.getLength());
              }
              _xblockexpression = null;
            }
            return _xblockexpression;
          };
          ((XtextEditor)cachedEditor).getDocument().<Object>readOnly(_function);
        }
      }
      return cachedEditor;
    }
    final IWorkbenchPart activePart = activePage.getActivePart();
    final IEditorPart editor = Access.getIURIEditorOpener().get().open(resourceURI, isSelect);
    if ((editor != null)) {
      XtextDomainObjectProvider.CachedEditor _cachedEditor = new XtextDomainObjectProvider.CachedEditor(editor);
      this.editorCache.put(resourceURI, _cachedEditor);
    }
    if ((!isActivate)) {
      activePage.activate(activePart);
    }
    return editor;
  }
  
  @Override
  public boolean isTransient() {
    return true;
  }
  
  @Override
  public void postLoad() {
  }
}
