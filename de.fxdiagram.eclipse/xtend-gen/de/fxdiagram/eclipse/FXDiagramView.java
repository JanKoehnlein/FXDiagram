package de.fxdiagram.eclipse;

import com.google.common.base.Objects;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.eclipse.FXDiagramTab;
import de.fxdiagram.eclipse.mapping.AbstractMapping;
import de.fxdiagram.eclipse.mapping.MappingCall;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import javafx.embed.swt.FXCanvas;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Pair;

/**
 * Embeds an {@link FXCanvas} with an {@link XRoot} in an eclipse {@link ViewPart}.
 * 
 * Uses {@link AbstractMapping} API to map domain objects to diagram elements.
 */
@SuppressWarnings("all")
public class FXDiagramView extends ViewPart {
  private CTabFolder tabFolder;
  
  private Map<CTabItem, FXDiagramTab> tab2content = CollectionLiterals.<CTabItem, FXDiagramTab>newHashMap();
  
  private List<Pair<EventType<?>, EventHandler<?>>> globalEventHandlers = CollectionLiterals.<Pair<EventType<?>, EventHandler<?>>>newArrayList();
  
  @Override
  public void createPartControl(final Composite parent) {
    CTabFolder _cTabFolder = new CTabFolder(parent, (SWT.BORDER + SWT.BOTTOM));
    this.tabFolder = _cTabFolder;
    Display _display = parent.getDisplay();
    Color _systemColor = _display.getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND);
    this.tabFolder.setBackground(_systemColor);
  }
  
  public FXDiagramTab createNewTab() {
    FXDiagramTab _xblockexpression = null;
    {
      final FXDiagramTab diagramTab = new FXDiagramTab(this, this.tabFolder);
      CTabItem _cTabItem = diagramTab.getCTabItem();
      this.tab2content.put(_cTabItem, diagramTab);
      CTabItem _cTabItem_1 = diagramTab.getCTabItem();
      this.tabFolder.setSelection(_cTabItem_1);
      final Consumer<Pair<EventType<?>, EventHandler<?>>> _function = new Consumer<Pair<EventType<?>, EventHandler<?>>>() {
        @Override
        public void accept(final Pair<EventType<?>, EventHandler<?>> it) {
          XRoot _root = diagramTab.getRoot();
          EventType<?> _key = it.getKey();
          EventHandler<?> _value = it.getValue();
          FXDiagramView.this.addEventHandlerWrapper(_root, ((EventType<? extends Event>) _key), _value);
        }
      };
      this.globalEventHandlers.forEach(_function);
      _xblockexpression = diagramTab;
    }
    return _xblockexpression;
  }
  
  private <T extends Event> void addEventHandlerWrapper(final XRoot root, final EventType<T> eventType, final EventHandler<?> handler) {
    root.<T>addEventHandler(eventType, ((EventHandler<? super T>) handler));
  }
  
  public <T extends Event> void addGlobalEventHandler(final EventType<T> eventType, final EventHandler<? super T> eventHandler) {
    Pair<EventType<?>, EventHandler<?>> _mappedTo = Pair.<EventType<?>, EventHandler<?>>of(eventType, eventHandler);
    this.globalEventHandlers.add(_mappedTo);
    Collection<FXDiagramTab> _values = this.tab2content.values();
    final Consumer<FXDiagramTab> _function = new Consumer<FXDiagramTab>() {
      @Override
      public void accept(final FXDiagramTab it) {
        XRoot _root = it.getRoot();
        _root.<T>addEventHandler(eventType, eventHandler);
      }
    };
    _values.forEach(_function);
  }
  
  public <T extends Event> void removeGlobalEventHandler(final EventType<T> eventType, final EventHandler<? super T> eventHandler) {
    Pair<EventType<T>, EventHandler<? super T>> _mappedTo = Pair.<EventType<T>, EventHandler<? super T>>of(eventType, eventHandler);
    this.globalEventHandlers.remove(_mappedTo);
    Collection<FXDiagramTab> _values = this.tab2content.values();
    final Consumer<FXDiagramTab> _function = new Consumer<FXDiagramTab>() {
      @Override
      public void accept(final FXDiagramTab it) {
        XRoot _root = it.getRoot();
        _root.<T>removeEventHandler(eventType, eventHandler);
      }
    };
    _values.forEach(_function);
  }
  
  protected FXDiagramTab getCurrentDiagramTab() {
    FXDiagramTab _xblockexpression = null;
    {
      final CTabItem currentTab = this.tabFolder.getSelection();
      FXDiagramTab _xifexpression = null;
      boolean _notEquals = (!Objects.equal(currentTab, null));
      if (_notEquals) {
        _xifexpression = this.tab2content.get(currentTab);
      } else {
        _xifexpression = null;
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  public XRoot getCurrentRoot() {
    FXDiagramTab _elvis = null;
    FXDiagramTab _currentDiagramTab = this.getCurrentDiagramTab();
    if (_currentDiagramTab != null) {
      _elvis = _currentDiagramTab;
    } else {
      FXDiagramTab _createNewTab = this.createNewTab();
      _elvis = _createNewTab;
    }
    return _elvis.getRoot();
  }
  
  @Override
  public void setFocus() {
    FXDiagramTab _currentDiagramTab = this.getCurrentDiagramTab();
    if (_currentDiagramTab!=null) {
      _currentDiagramTab.setFocus();
    }
  }
  
  public void clear() {
    FXDiagramTab _currentDiagramTab = this.getCurrentDiagramTab();
    if (_currentDiagramTab!=null) {
      _currentDiagramTab.clear();
    }
  }
  
  public <T extends Object> void revealElement(final T element, final MappingCall<?, ? super T> mappingCall, final IEditorPart editor) {
    FXDiagramTab _elvis = null;
    FXDiagramTab _currentDiagramTab = this.getCurrentDiagramTab();
    if (_currentDiagramTab != null) {
      _elvis = _currentDiagramTab;
    } else {
      FXDiagramTab _createNewTab = this.createNewTab();
      _elvis = _createNewTab;
    }
    _elvis.<T>revealElement(element, mappingCall, editor);
  }
}
