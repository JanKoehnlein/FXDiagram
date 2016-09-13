package de.fxdiagram.eclipse.ecore;

import com.google.common.base.Objects;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.eclipse.ecore.EReferenceWithOpposite;
import de.fxdiagram.eclipse.ecore.EReferenceWithOppositeDescriptor;
import de.fxdiagram.eclipse.ecore.ESuperType;
import de.fxdiagram.eclipse.ecore.ESuperTypeDescriptor;
import de.fxdiagram.eclipse.ecore.EcoreDomainObjectDescriptor;
import de.fxdiagram.mapping.AbstractMapping;
import de.fxdiagram.mapping.IMappedElementDescriptor;
import de.fxdiagram.mapping.IMappedElementDescriptorProvider;
import de.fxdiagram.mapping.XDiagramConfig;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.eclipse.emf.common.ui.URIEditorInput;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorRegistry;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.xtext.xbase.lib.Exceptions;

@SuppressWarnings("all")
public class EcoreDomainObjectProvider implements IMappedElementDescriptorProvider {
  @Override
  public <T extends Object> IMappedElementDescriptor<T> createMappedElementDescriptor(final T domainObject, final AbstractMapping<? extends T> mapping) {
    Object _switchResult = null;
    boolean _matched = false;
    if (domainObject instanceof ENamedElement) {
      _matched=true;
      URI _uRI = EcoreUtil.getURI(((ENamedElement)domainObject));
      String _string = _uRI.toString();
      String _name = ((ENamedElement)domainObject).getName();
      XDiagramConfig _config = mapping.getConfig();
      String _iD = _config.getID();
      String _iD_1 = mapping.getID();
      EcoreDomainObjectDescriptor _ecoreDomainObjectDescriptor = new EcoreDomainObjectDescriptor(_string, _name, _iD, _iD_1);
      return ((IMappedElementDescriptor<T>) _ecoreDomainObjectDescriptor);
    }
    if (!_matched) {
      if (domainObject instanceof EReferenceWithOpposite) {
        _matched=true;
        EReference _to = ((EReferenceWithOpposite)domainObject).getTo();
        URI _uRI = EcoreUtil.getURI(_to);
        String _string = _uRI.toString();
        EReference _to_1 = ((EReferenceWithOpposite)domainObject).getTo();
        String _name = _to_1.getName();
        EReference _fro = ((EReferenceWithOpposite)domainObject).getFro();
        URI _uRI_1 = null;
        if (_fro!=null) {
          _uRI_1=EcoreUtil.getURI(_fro);
        }
        String _string_1 = null;
        if (_uRI_1!=null) {
          _string_1=_uRI_1.toString();
        }
        EReference _fro_1 = ((EReferenceWithOpposite)domainObject).getFro();
        String _name_1 = null;
        if (_fro_1!=null) {
          _name_1=_fro_1.getName();
        }
        XDiagramConfig _config = mapping.getConfig();
        String _iD = _config.getID();
        String _iD_1 = mapping.getID();
        EReferenceWithOppositeDescriptor _eReferenceWithOppositeDescriptor = new EReferenceWithOppositeDescriptor(_string, _name, _string_1, _name_1, _iD, _iD_1);
        return ((IMappedElementDescriptor<T>) _eReferenceWithOppositeDescriptor);
      }
    }
    if (!_matched) {
      if (domainObject instanceof ESuperType) {
        _matched=true;
        EClass _subType = ((ESuperType)domainObject).getSubType();
        URI _uRI = EcoreUtil.getURI(_subType);
        String _string = _uRI.toString();
        EClass _subType_1 = ((ESuperType)domainObject).getSubType();
        String _name = _subType_1.getName();
        EClass _superType = ((ESuperType)domainObject).getSuperType();
        URI _uRI_1 = EcoreUtil.getURI(_superType);
        String _string_1 = _uRI_1.toString();
        EClass _superType_1 = ((ESuperType)domainObject).getSuperType();
        String _name_1 = _superType_1.getName();
        XDiagramConfig _config = mapping.getConfig();
        String _iD = _config.getID();
        String _iD_1 = mapping.getID();
        ESuperTypeDescriptor _eSuperTypeDescriptor = new ESuperTypeDescriptor(_string, _name, _string_1, _name_1, _iD, _iD_1);
        return ((IMappedElementDescriptor<T>) _eSuperTypeDescriptor);
      }
    }
    if (!_matched) {
      _switchResult = null;
    }
    return ((IMappedElementDescriptor<T>)_switchResult);
  }
  
  @Override
  public <T extends Object> DomainObjectDescriptor createDescriptor(final T domainObject) {
    return null;
  }
  
  public EObject resolveEObject(final String uri) {
    EObject _xblockexpression = null;
    {
      final URI theURI = URI.createURI(uri);
      final IEditingDomainProvider editor = this.openEditor(theURI, false);
      EObject _xifexpression = null;
      boolean _notEquals = (!Objects.equal(editor, null));
      if (_notEquals) {
        EditingDomain _editingDomain = editor.getEditingDomain();
        ResourceSet _resourceSet = _editingDomain.getResourceSet();
        _xifexpression = _resourceSet.getEObject(theURI, true);
      } else {
        _xifexpression = null;
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  public IEditingDomainProvider openEditor(final URI uri, final boolean select) {
    try {
      IWorkbench _workbench = PlatformUI.getWorkbench();
      IEditorRegistry _editorRegistry = _workbench.getEditorRegistry();
      String _lastSegment = uri.lastSegment();
      IEditorDescriptor _defaultEditor = _editorRegistry.getDefaultEditor(_lastSegment);
      final String editorID = _defaultEditor.getId();
      IWorkbench _workbench_1 = PlatformUI.getWorkbench();
      IWorkbenchWindow _activeWorkbenchWindow = _workbench_1.getActiveWorkbenchWindow();
      IWorkbenchPage _activePage = _activeWorkbenchWindow.getActivePage();
      URI _trimFragment = uri.trimFragment();
      URIEditorInput _uRIEditorInput = new URIEditorInput(_trimFragment);
      final IEditorPart editor = _activePage.openEditor(_uRIEditorInput, editorID);
      if ((editor instanceof IEditingDomainProvider)) {
        if (select) {
          EditingDomain _editingDomain = ((IEditingDomainProvider)editor).getEditingDomain();
          ResourceSet _resourceSet = _editingDomain.getResourceSet();
          final EObject element = _resourceSet.getEObject(uri, true);
          this.setSelection(editor, element);
        }
        return ((IEditingDomainProvider)editor);
      } else {
        return null;
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  protected Object setSelection(final IEditorPart editor, final EObject selectedElement) {
    Object _xtrycatchfinallyexpression = null;
    try {
      Object _xblockexpression = null;
      {
        Class<? extends IEditorPart> _class = editor.getClass();
        final Method method = _class.getMethod("setSelectionToViewer", Collection.class);
        List<EObject> _singletonList = Collections.<EObject>singletonList(selectedElement);
        _xblockexpression = method.invoke(editor, _singletonList);
      }
      _xtrycatchfinallyexpression = _xblockexpression;
    } catch (final Throwable _t) {
      if (_t instanceof Exception) {
        final Exception exc = (Exception)_t;
        exc.printStackTrace();
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    }
    return _xtrycatchfinallyexpression;
  }
}
