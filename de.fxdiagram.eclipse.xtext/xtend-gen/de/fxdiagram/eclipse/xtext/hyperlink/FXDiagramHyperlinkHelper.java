package de.fxdiagram.eclipse.xtext.hyperlink;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import de.fxdiagram.eclipse.FXDiagramView;
import de.fxdiagram.eclipse.xtext.XtextDomainObjectProvider;
import de.fxdiagram.mapping.AbstractMapping;
import de.fxdiagram.mapping.IMappedElementDescriptor;
import de.fxdiagram.mapping.IMappedElementDescriptorProvider;
import de.fxdiagram.mapping.MappingCall;
import de.fxdiagram.mapping.XDiagramConfig;
import java.util.List;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.xtend.lib.annotations.Data;
import org.eclipse.xtext.resource.EObjectAtOffsetHelper;
import org.eclipse.xtext.resource.ILocationInFileProvider;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.hyperlinking.HyperlinkHelper;
import org.eclipse.xtext.ui.editor.hyperlinking.IHyperlinkAcceptor;
import org.eclipse.xtext.ui.editor.hyperlinking.IHyperlinkHelper;
import org.eclipse.xtext.util.ITextRegion;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.util.ToStringBuilder;

@SuppressWarnings("all")
public class FXDiagramHyperlinkHelper extends HyperlinkHelper {
  @Data
  public static class FXDiagramHyperlink implements IHyperlink {
    private final IMappedElementDescriptor<EObject> descriptor;
    
    private final MappingCall<?, ? super EObject> mappingCall;
    
    private final ITextRegion region;
    
    private final IEditorPart editor;
    
    @Override
    public IRegion getHyperlinkRegion() {
      int _offset = this.region.getOffset();
      int _length = this.region.getLength();
      return new Region(_offset, _length);
    }
    
    @Override
    public String getHyperlinkText() {
      AbstractMapping<?> _mapping = this.mappingCall.getMapping();
      String _iD = _mapping.getID();
      return ("Show in FXDiagram as " + _iD);
    }
    
    @Override
    public String getTypeLabel() {
      return "FXDiagram";
    }
    
    @Override
    public void open() {
      try {
        IWorkbenchPartSite _site = null;
        if (this.editor!=null) {
          _site=this.editor.getSite();
        }
        IWorkbenchWindow _workbenchWindow = null;
        if (_site!=null) {
          _workbenchWindow=_site.getWorkbenchWindow();
        }
        IWorkbench _workbench = null;
        if (_workbenchWindow!=null) {
          _workbench=_workbenchWindow.getWorkbench();
        }
        IWorkbenchWindow _activeWorkbenchWindow = null;
        if (_workbench!=null) {
          _activeWorkbenchWindow=_workbench.getActiveWorkbenchWindow();
        }
        IWorkbenchPage _activePage = null;
        if (_activeWorkbenchWindow!=null) {
          _activePage=_activeWorkbenchWindow.getActivePage();
        }
        IViewPart _showView = null;
        if (_activePage!=null) {
          _showView=_activePage.showView("de.fxdiagram.eclipse.FXDiagramView");
        }
        final IViewPart view = _showView;
        if ((view instanceof FXDiagramView)) {
          final Function1<EObject, Object> _function = (EObject it) -> {
            Object _xblockexpression = null;
            {
              ((FXDiagramView)view).<EObject>revealElement(it, this.mappingCall, this.editor);
              _xblockexpression = null;
            }
            return _xblockexpression;
          };
          this.descriptor.<Object>withDomainObject(_function);
        }
      } catch (Throwable _e) {
        throw Exceptions.sneakyThrow(_e);
      }
    }
    
    public FXDiagramHyperlink(final IMappedElementDescriptor<EObject> descriptor, final MappingCall<?, ? super EObject> mappingCall, final ITextRegion region, final IEditorPart editor) {
      super();
      this.descriptor = descriptor;
      this.mappingCall = mappingCall;
      this.region = region;
      this.editor = editor;
    }
    
    @Override
    @Pure
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((this.descriptor== null) ? 0 : this.descriptor.hashCode());
      result = prime * result + ((this.mappingCall== null) ? 0 : this.mappingCall.hashCode());
      result = prime * result + ((this.region== null) ? 0 : this.region.hashCode());
      result = prime * result + ((this.editor== null) ? 0 : this.editor.hashCode());
      return result;
    }
    
    @Override
    @Pure
    public boolean equals(final Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      FXDiagramHyperlinkHelper.FXDiagramHyperlink other = (FXDiagramHyperlinkHelper.FXDiagramHyperlink) obj;
      if (this.descriptor == null) {
        if (other.descriptor != null)
          return false;
      } else if (!this.descriptor.equals(other.descriptor))
        return false;
      if (this.mappingCall == null) {
        if (other.mappingCall != null)
          return false;
      } else if (!this.mappingCall.equals(other.mappingCall))
        return false;
      if (this.region == null) {
        if (other.region != null)
          return false;
      } else if (!this.region.equals(other.region))
        return false;
      if (this.editor == null) {
        if (other.editor != null)
          return false;
      } else if (!this.editor.equals(other.editor))
        return false;
      return true;
    }
    
    @Override
    @Pure
    public String toString() {
      ToStringBuilder b = new ToStringBuilder(this);
      b.add("descriptor", this.descriptor);
      b.add("mappingCall", this.mappingCall);
      b.add("region", this.region);
      b.add("editor", this.editor);
      return b.toString();
    }
    
    @Pure
    public IMappedElementDescriptor<EObject> getDescriptor() {
      return this.descriptor;
    }
    
    @Pure
    public MappingCall<?, ? super EObject> getMappingCall() {
      return this.mappingCall;
    }
    
    @Pure
    public ITextRegion getRegion() {
      return this.region;
    }
    
    @Pure
    public IEditorPart getEditor() {
      return this.editor;
    }
  }
  
  public final static String DELEGATE = "de.fxdiagram.eclipse.xtext.FXDiagramHyperlinkHelper.Delegate";
  
  @Inject
  @Named(FXDiagramHyperlinkHelper.DELEGATE)
  private IHyperlinkHelper delegate;
  
  @Inject
  private IWorkbench workbench;
  
  @Inject
  private ILocationInFileProvider locationInFileProvider;
  
  @Override
  public IHyperlink[] createHyperlinksByOffset(final XtextResource resource, final int offset, final boolean createMultipleHyperlinks) {
    Iterable<IHyperlink> _xblockexpression = null;
    {
      IHyperlink[] _createHyperlinksByOffset = this.delegate.createHyperlinksByOffset(resource, offset, createMultipleHyperlinks);
      List<IHyperlink> _emptyListIfNull = this.<IHyperlink>emptyListIfNull(_createHyperlinksByOffset);
      IHyperlink[] _createHyperlinksByOffset_1 = super.createHyperlinksByOffset(resource, offset, createMultipleHyperlinks);
      List<IHyperlink> _emptyListIfNull_1 = this.<IHyperlink>emptyListIfNull(_createHyperlinksByOffset_1);
      final Iterable<IHyperlink> hyperlinks = Iterables.<IHyperlink>concat(_emptyListIfNull, _emptyListIfNull_1);
      Iterable<IHyperlink> _xifexpression = null;
      boolean _isEmpty = IterableExtensions.isEmpty(hyperlinks);
      if (_isEmpty) {
        _xifexpression = null;
      } else {
        _xifexpression = hyperlinks;
      }
      _xblockexpression = _xifexpression;
    }
    return ((IHyperlink[])Conversions.unwrapArray(_xblockexpression, IHyperlink.class));
  }
  
  protected <T extends Object> List<T> emptyListIfNull(final T[] array) {
    List<T> _elvis = null;
    if (((List<T>) Conversions.doWrapArray(array)) != null) {
      _elvis = ((List<T>) Conversions.doWrapArray(array));
    } else {
      List<T> _emptyList = CollectionLiterals.<T>emptyList();
      _elvis = _emptyList;
    }
    return _elvis;
  }
  
  @Override
  public void createHyperlinksByOffset(final XtextResource resource, final int offset, final IHyperlinkAcceptor acceptor) {
    EObjectAtOffsetHelper _eObjectAtOffsetHelper = this.getEObjectAtOffsetHelper();
    final EObject selectedElement = _eObjectAtOffsetHelper.resolveElementAt(resource, offset);
    IWorkbenchWindow _activeWorkbenchWindow = null;
    if (this.workbench!=null) {
      _activeWorkbenchWindow=this.workbench.getActiveWorkbenchWindow();
    }
    IWorkbenchPage _activePage = null;
    if (_activeWorkbenchWindow!=null) {
      _activePage=_activeWorkbenchWindow.getActivePage();
    }
    IEditorPart _activeEditor = null;
    if (_activePage!=null) {
      _activeEditor=_activePage.getActiveEditor();
    }
    final IEditorPart editor = _activeEditor;
    boolean _notEquals = (!Objects.equal(selectedElement, null));
    if (_notEquals) {
      XDiagramConfig.Registry _instance = XDiagramConfig.Registry.getInstance();
      Iterable<? extends XDiagramConfig> _configurations = _instance.getConfigurations();
      final Function1<XDiagramConfig, Iterable<? extends MappingCall<?, EObject>>> _function = (XDiagramConfig it) -> {
        return it.<EObject>getEntryCalls(selectedElement);
      };
      Iterable<Iterable<? extends MappingCall<?, EObject>>> _map = IterableExtensions.map(_configurations, _function);
      final Iterable<MappingCall<?, EObject>> mappingCalls = Iterables.<MappingCall<?, EObject>>concat(_map);
      boolean _isEmpty = IterableExtensions.isEmpty(mappingCalls);
      boolean _not = (!_isEmpty);
      if (_not) {
        final ITextRegion region = this.locationInFileProvider.getSignificantTextRegion(selectedElement);
        for (final MappingCall<?, EObject> mappingCall : mappingCalls) {
          {
            AbstractMapping<?> _mapping = mappingCall.getMapping();
            XDiagramConfig _config = _mapping.getConfig();
            final IMappedElementDescriptorProvider domainObjectProvider = _config.getDomainObjectProvider();
            if ((domainObjectProvider instanceof XtextDomainObjectProvider)) {
              AbstractMapping<?> _mapping_1 = mappingCall.getMapping();
              final IMappedElementDescriptor<EObject> descriptor = ((XtextDomainObjectProvider)domainObjectProvider).<EObject>createMappedElementDescriptor(selectedElement, ((AbstractMapping<EObject>) _mapping_1));
              FXDiagramHyperlinkHelper.FXDiagramHyperlink _fXDiagramHyperlink = new FXDiagramHyperlinkHelper.FXDiagramHyperlink(descriptor, mappingCall, region, editor);
              acceptor.accept(_fXDiagramHyperlink);
            }
          }
        }
      }
    }
  }
}
