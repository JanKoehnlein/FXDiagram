package de.fxdiagram.eclipse.ecore;

import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.eclipse.ecore.EReferenceWithOpposite;
import de.fxdiagram.eclipse.ecore.EReferenceWithOppositeDescriptor;
import de.fxdiagram.eclipse.ecore.ESuperType;
import de.fxdiagram.eclipse.ecore.ESuperTypeDescriptor;
import de.fxdiagram.eclipse.ecore.EcoreDomainObjectDescriptor;
import de.fxdiagram.mapping.AbstractMapping;
import de.fxdiagram.mapping.IMappedElementDescriptor;
import de.fxdiagram.mapping.IMappedElementDescriptorProvider;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import org.eclipse.emf.common.ui.URIEditorInput;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
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
      String _string = EcoreUtil.getURI(((ENamedElement)domainObject)).toString();
      String _name = ((ENamedElement)domainObject).getName();
      String _iD = mapping.getConfig().getID();
      String _iD_1 = mapping.getID();
      EcoreDomainObjectDescriptor _ecoreDomainObjectDescriptor = new EcoreDomainObjectDescriptor(_string, _name, _iD, _iD_1);
      return ((IMappedElementDescriptor<T>) _ecoreDomainObjectDescriptor);
    }
    if (!_matched) {
      if (domainObject instanceof EReferenceWithOpposite) {
        _matched=true;
        String _string = EcoreUtil.getURI(((EReferenceWithOpposite)domainObject).getTo()).toString();
        String _name = ((EReferenceWithOpposite)domainObject).getTo().getName();
        EReference _fro = ((EReferenceWithOpposite)domainObject).getFro();
        URI _uRI = null;
        if (_fro!=null) {
          _uRI=EcoreUtil.getURI(_fro);
        }
        String _string_1 = null;
        if (_uRI!=null) {
          _string_1=_uRI.toString();
        }
        EReference _fro_1 = ((EReferenceWithOpposite)domainObject).getFro();
        String _name_1 = null;
        if (_fro_1!=null) {
          _name_1=_fro_1.getName();
        }
        String _iD = mapping.getConfig().getID();
        String _iD_1 = mapping.getID();
        EReferenceWithOppositeDescriptor _eReferenceWithOppositeDescriptor = new EReferenceWithOppositeDescriptor(_string, _name, _string_1, _name_1, _iD, _iD_1);
        return ((IMappedElementDescriptor<T>) _eReferenceWithOppositeDescriptor);
      }
    }
    if (!_matched) {
      if (domainObject instanceof ESuperType) {
        _matched=true;
        String _string = EcoreUtil.getURI(((ESuperType)domainObject).getSubType()).toString();
        String _name = ((ESuperType)domainObject).getSubType().getName();
        String _string_1 = EcoreUtil.getURI(((ESuperType)domainObject).getSuperType()).toString();
        String _name_1 = ((ESuperType)domainObject).getSuperType().getName();
        String _iD = mapping.getConfig().getID();
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
      if ((editor != null)) {
        _xifexpression = editor.getEditingDomain().getResourceSet().getEObject(theURI, true);
      } else {
        _xifexpression = null;
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  public IEditingDomainProvider openEditor(final URI uri, final boolean select) {
    try {
      final String editorID = PlatformUI.getWorkbench().getEditorRegistry().getDefaultEditor(uri.lastSegment()).getId();
      IWorkbenchPage _activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
      URI _trimFragment = uri.trimFragment();
      URIEditorInput _uRIEditorInput = new URIEditorInput(_trimFragment);
      final IEditorPart editor = _activePage.openEditor(_uRIEditorInput, editorID);
      if ((editor instanceof IEditingDomainProvider)) {
        if (select) {
          final EObject element = ((IEditingDomainProvider)editor).getEditingDomain().getResourceSet().getEObject(uri, true);
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
        final Method method = editor.getClass().getMethod("setSelectionToViewer", Collection.class);
        _xblockexpression = method.invoke(editor, Collections.<EObject>singletonList(selectedElement));
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
  
  @Override
  public void postLoad() {
  }
}
