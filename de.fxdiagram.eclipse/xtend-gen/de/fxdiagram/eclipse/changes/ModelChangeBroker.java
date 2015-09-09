package de.fxdiagram.eclipse.changes;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.fxdiagram.eclipse.changes.IChangeListener;
import de.fxdiagram.eclipse.changes.IChangeSource;
import de.fxdiagram.eclipse.selection.ISelectionExtractor;
import java.util.List;
import java.util.function.Consumer;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IPageListener;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;

@SuppressWarnings("all")
public class ModelChangeBroker {
  private final IPartListener2 partListener = new IPartListener2() {
    @Override
    public void partOpened(final IWorkbenchPartReference partRef) {
      final IWorkbenchPart part = partRef.getPart(false);
      boolean _notEquals = (!Objects.equal(part, null));
      if (_notEquals) {
        ISelectionExtractor.Registry _instance = ISelectionExtractor.Registry.getInstance();
        Iterable<ISelectionExtractor> _selectionExtractors = _instance.getSelectionExtractors();
        Iterable<IChangeSource> _filter = Iterables.<IChangeSource>filter(_selectionExtractors, IChangeSource.class);
        final Consumer<IChangeSource> _function = (IChangeSource it) -> {
          it.addChangeListener(part, ModelChangeBroker.this);
        };
        _filter.forEach(_function);
      }
    }
    
    @Override
    public void partClosed(final IWorkbenchPartReference partRef) {
      final IWorkbenchPart part = partRef.getPart(false);
      boolean _notEquals = (!Objects.equal(part, null));
      if (_notEquals) {
        ISelectionExtractor.Registry _instance = ISelectionExtractor.Registry.getInstance();
        Iterable<ISelectionExtractor> _selectionExtractors = _instance.getSelectionExtractors();
        Iterable<IChangeSource> _filter = Iterables.<IChangeSource>filter(_selectionExtractors, IChangeSource.class);
        final Consumer<IChangeSource> _function = (IChangeSource it) -> {
          it.removeChangeListener(part, ModelChangeBroker.this);
        };
        _filter.forEach(_function);
      }
    }
    
    @Override
    public void partActivated(final IWorkbenchPartReference partRef) {
    }
    
    @Override
    public void partBroughtToTop(final IWorkbenchPartReference partRef) {
    }
    
    @Override
    public void partDeactivated(final IWorkbenchPartReference partRef) {
    }
    
    @Override
    public void partHidden(final IWorkbenchPartReference partRef) {
    }
    
    @Override
    public void partInputChanged(final IWorkbenchPartReference partRef) {
    }
    
    @Override
    public void partVisible(final IWorkbenchPartReference partRef) {
    }
  };
  
  private final IPageListener pageListener = new IPageListener() {
    @Override
    public void pageActivated(final IWorkbenchPage page) {
    }
    
    @Override
    public void pageClosed(final IWorkbenchPage page) {
      page.removePartListener(ModelChangeBroker.this.partListener);
    }
    
    @Override
    public void pageOpened(final IWorkbenchPage page) {
      page.addPartListener(ModelChangeBroker.this.partListener);
    }
  };
  
  private List<IChangeListener> listeners = CollectionLiterals.<IChangeListener>newArrayList();
  
  public boolean addListener(final IChangeListener listener) {
    return this.listeners.add(listener);
  }
  
  public boolean removeListener(final IChangeListener listener) {
    return this.listeners.remove(listener);
  }
  
  public ModelChangeBroker(final IWorkbench workbench) {
    IWorkbenchWindow[] _workbenchWindows = workbench.getWorkbenchWindows();
    final Consumer<IWorkbenchWindow> _function = (IWorkbenchWindow it) -> {
      IWorkbenchPage[] _pages = it.getPages();
      final Consumer<IWorkbenchPage> _function_1 = (IWorkbenchPage it_1) -> {
        IEditorReference[] _editorReferences = it_1.getEditorReferences();
        final Consumer<IEditorReference> _function_2 = (IEditorReference it_2) -> {
          this.partListener.partOpened(it_2);
        };
        ((List<IEditorReference>)Conversions.doWrapArray(_editorReferences)).forEach(_function_2);
        it_1.addPartListener(this.partListener);
      };
      ((List<IWorkbenchPage>)Conversions.doWrapArray(_pages)).forEach(_function_1);
      it.addPageListener(this.pageListener);
    };
    ((List<IWorkbenchWindow>)Conversions.doWrapArray(_workbenchWindows)).forEach(_function);
  }
  
  public void partChanged(final IWorkbenchPart part) {
    final Consumer<IChangeListener> _function = (IChangeListener listener) -> {
      listener.partChanged(part);
    };
    this.listeners.forEach(_function);
  }
}
