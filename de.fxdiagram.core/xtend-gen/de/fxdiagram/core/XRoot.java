package de.fxdiagram.core;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.fxdiagram.annotations.logging.Logging;
import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.HeadsUpDisplay;
import de.fxdiagram.core.XActivatable;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XShape;
import de.fxdiagram.core.command.CommandStack;
import de.fxdiagram.core.css.JavaToCss;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.model.DomainObjectProvider;
import de.fxdiagram.core.model.DomainObjectProviderWithState;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.core.model.ToString;
import de.fxdiagram.core.model.XModelProvider;
import de.fxdiagram.core.tools.CompositeTool;
import de.fxdiagram.core.tools.DiagramActionTool;
import de.fxdiagram.core.tools.DiagramGestureTool;
import de.fxdiagram.core.tools.DiagramMouseTool;
import de.fxdiagram.core.tools.SelectionTool;
import de.fxdiagram.core.tools.XDiagramTool;
import de.fxdiagram.core.tools.actions.DiagramAction;
import de.fxdiagram.core.tools.actions.DiagramActionRegistry;
import de.fxdiagram.core.viewport.ViewportTransform;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.logging.Logger;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

/**
 * The root object in the scenegraph of FXDiagram. Embed this in our applications scenegraph.
 * 
 * The {@link XRoot} shows a single active {@link #diagram} and aa {@link HeadsUpDisplay} on
 * top.
 * 
 * The {@link XRoot} gives access to the common services, like {@link DiagramAction}s,
 * {@link DomainObjectProvider}s and the {@link CommandStack}. It also stores the current
 * {@link XDiagramTool}
 * 
 * To find the {@link XRoot} of an {@link XShape} or {@link XDiagram}
 * use the {@link CoreExtensions}.
 * 
 * The {@link XRoot} is also the root element for serialization. This is why it keeps track of the
 * {@link #rootDiagram} as well.
 */
@Logging
@ModelNode({ "domainObjectProviders", "rootDiagram", "diagram" })
@SuppressWarnings("all")
public class XRoot extends Parent implements XActivatable, XModelProvider {
  private DiagramActionRegistry diagramActionRegistry = new DiagramActionRegistry();
  
  private HeadsUpDisplay headsUpDisplay = new HeadsUpDisplay();
  
  private Pane diagramCanvas = new Pane();
  
  private List<XDiagramTool> tools = CollectionLiterals.<XDiagramTool>newArrayList();
  
  private CompositeTool defaultTool;
  
  private XDiagramTool currentTool;
  
  private Map<Class<? extends DomainObjectProvider>, DomainObjectProvider> domainObjectProviderCache;
  
  private CommandStack commandStack = new CommandStack(this);
  
  public XRoot() {
    ObservableList<Node> _children = this.getChildren();
    _children.add(this.diagramCanvas);
    ObservableList<Node> _children_1 = this.getChildren();
    _children_1.add(this.headsUpDisplay);
    final InvalidationListener _function = (Observable o) -> {
      this.domainObjectProviderCache = null;
    };
    this.getDomainObjectProviders().addListener(_function);
  }
  
  public void setRootDiagram(final XDiagram rootDiagram) {
    this.rootDiagramProperty.set(rootDiagram);
    this.setDiagram(rootDiagram);
  }
  
  public void setDiagram(final XDiagram newDiagram) {
    XDiagram _diagram = this.getDiagram();
    boolean _notEquals = (!Objects.equal(_diagram, null));
    if (_notEquals) {
      ObservableList<Node> _children = this.diagramCanvas.getChildren();
      XDiagram _diagram_1 = this.getDiagram();
      _children.remove(_diagram_1);
      ObservableList<Node> _children_1 = this.headsUpDisplay.getChildren();
      Set<Node> _keySet = this.getDiagram().getFixedButtons().keySet();
      Iterables.removeAll(_children_1, _keySet);
    }
    this.diagramProperty.set(newDiagram);
    ObservableList<Node> _children_2 = this.diagramCanvas.getChildren();
    _children_2.add(newDiagram);
    boolean _isActive = this.getIsActive();
    if (_isActive) {
      newDiagram.activate();
    }
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("-fx-background-color: ");
    CharSequence _css = JavaToCss.toCss(newDiagram.getBackgroundPaint());
    _builder.append(_css);
    _builder.append(";");
    _builder.newLineIfNotEmpty();
    _builder.append("-fx-text-fill: ");
    CharSequence _css_1 = JavaToCss.toCss(newDiagram.getForegroundPaint());
    _builder.append(_css_1);
    _builder.append(";");
    _builder.newLineIfNotEmpty();
    this.diagramCanvas.setStyle(_builder.toString());
    final Consumer<Map.Entry<Node, Pos>> _function = (Map.Entry<Node, Pos> it) -> {
      this.headsUpDisplay.add(it.getKey(), it.getValue());
    };
    this.getDiagram().getFixedButtons().entrySet().forEach(_function);
    newDiagram.centerDiagram(false);
  }
  
  public HeadsUpDisplay getHeadsUpDisplay() {
    return this.headsUpDisplay;
  }
  
  public Pane getDiagramCanvas() {
    return this.diagramCanvas;
  }
  
  public ViewportTransform getViewportTransform() {
    return this.getDiagram().getViewportTransform();
  }
  
  @Override
  public void activate() {
    boolean _isActive = this.getIsActive();
    boolean _not = (!_isActive);
    if (_not) {
      try {
        this.doActivate();
        this.isActiveProperty.set(true);
      } catch (final Throwable _t) {
        if (_t instanceof Exception) {
          final Exception exc = (Exception)_t;
          XRoot.LOG.severe(exc.getMessage());
          exc.printStackTrace();
        } else {
          throw Exceptions.sneakyThrow(_t);
        }
      }
    }
  }
  
  public void doActivate() {
    this.commandStack.activate();
    XDiagram _diagram = this.getDiagram();
    if (_diagram!=null) {
      _diagram.activate();
    }
    final Procedure1<Pane> _function = (Pane it) -> {
      it.prefWidthProperty().bind(it.getScene().widthProperty());
      it.prefHeightProperty().bind(it.getScene().heightProperty());
    };
    ObjectExtensions.<Pane>operator_doubleArrow(
      this.diagramCanvas, _function);
    boolean _isInteractive = this.getIsInteractive();
    if (_isInteractive) {
      CompositeTool _compositeTool = new CompositeTool();
      this.defaultTool = _compositeTool;
      SelectionTool _selectionTool = new SelectionTool(this);
      this.defaultTool.operator_add(_selectionTool);
      DiagramGestureTool _diagramGestureTool = new DiagramGestureTool(this);
      this.defaultTool.operator_add(_diagramGestureTool);
      DiagramMouseTool _diagramMouseTool = new DiagramMouseTool(this);
      this.defaultTool.operator_add(_diagramMouseTool);
      DiagramActionTool _diagramActionTool = new DiagramActionTool(this);
      this.defaultTool.operator_add(_diagramActionTool);
      this.tools.add(this.defaultTool);
      this.setCurrentTool(this.defaultTool);
    }
  }
  
  public void setCurrentTool(final XDiagramTool tool) {
    XDiagramTool previousTool = this.currentTool;
    boolean _notEquals = (!Objects.equal(previousTool, null));
    if (_notEquals) {
      boolean _deactivate = previousTool.deactivate();
      boolean _not = (!_deactivate);
      if (_not) {
        XRoot.LOG.severe("Could not deactivate active tool");
      }
    }
    this.currentTool = tool;
    boolean _notEquals_1 = (!Objects.equal(tool, null));
    if (_notEquals_1) {
      boolean _activate = tool.activate();
      boolean _not_1 = (!_activate);
      if (_not_1) {
        this.currentTool = previousTool;
        boolean _activate_1 = false;
        if (previousTool!=null) {
          _activate_1=previousTool.activate();
        }
        boolean _not_2 = (!_activate_1);
        if (_not_2) {
          XRoot.LOG.severe("Could not reactivate tool");
        }
      }
    }
  }
  
  public void restoreDefaultTool() {
    this.setCurrentTool(this.defaultTool);
  }
  
  public DiagramActionRegistry getDiagramActionRegistry() {
    return this.diagramActionRegistry;
  }
  
  public Iterable<XShape> getCurrentSelection() {
    final Function1<XShape, Boolean> _function = (XShape it) -> {
      return Boolean.valueOf((it.isSelectable() && it.getSelected()));
    };
    return IterableExtensions.<XShape>filter(this.getDiagram().getAllShapes(), _function);
  }
  
  public <T extends DomainObjectProvider> T getDomainObjectProvider(final Class<T> providerClazz) {
    T _xblockexpression = null;
    {
      boolean _equals = Objects.equal(this.domainObjectProviderCache, null);
      if (_equals) {
        this.domainObjectProviderCache = CollectionLiterals.<Class<? extends DomainObjectProvider>, DomainObjectProvider>newHashMap();
        final Consumer<DomainObjectProvider> _function = (DomainObjectProvider it) -> {
          this.domainObjectProviderCache.put(it.getClass(), it);
        };
        this.getDomainObjectProviders().forEach(_function);
      }
      DomainObjectProvider _get = this.domainObjectProviderCache.get(providerClazz);
      _xblockexpression = ((T) _get);
    }
    return _xblockexpression;
  }
  
  public void replaceDomainObjectProviders(final List<DomainObjectProvider> newDomainObjectProviders) {
    final Consumer<DomainObjectProvider> _function = (DomainObjectProvider newProvider) -> {
      final DomainObjectProvider oldProvider = this.getDomainObjectProvider(newProvider.getClass());
      boolean _notEquals = (!Objects.equal(oldProvider, null));
      if (_notEquals) {
        this.getDomainObjectProviders().set(this.getDomainObjectProviders().indexOf(oldProvider), newProvider);
        if ((newProvider instanceof DomainObjectProviderWithState)) {
          ((DomainObjectProviderWithState)newProvider).copyState(((DomainObjectProviderWithState) oldProvider));
        }
      } else {
        this.getDomainObjectProviders().add(newProvider);
      }
    };
    newDomainObjectProviders.forEach(_function);
  }
  
  public CommandStack getCommandStack() {
    return this.commandStack;
  }
  
  private static Logger LOG = Logger.getLogger("de.fxdiagram.core.XRoot");
    ;
  
  public void populate(final ModelElementImpl modelElement) {
    modelElement.addProperty(domainObjectProvidersProperty, DomainObjectProvider.class);
    modelElement.addProperty(rootDiagramProperty, XDiagram.class);
    modelElement.addProperty(diagramProperty, XDiagram.class);
  }
  
  public void postLoad() {
    
  }
  
  public String toString() {
    return ToString.toString(this);
  }
  
  private ReadOnlyBooleanWrapper isActiveProperty = new ReadOnlyBooleanWrapper(this, "isActive");
  
  public boolean getIsActive() {
    return this.isActiveProperty.get();
  }
  
  public ReadOnlyBooleanProperty isActiveProperty() {
    return this.isActiveProperty.getReadOnlyProperty();
  }
  
  private SimpleBooleanProperty isInteractiveProperty = new SimpleBooleanProperty(this, "isInteractive",_initIsInteractive());
  
  private static final boolean _initIsInteractive() {
    return true;
  }
  
  public boolean getIsInteractive() {
    return this.isInteractiveProperty.get();
  }
  
  public void setIsInteractive(final boolean isInteractive) {
    this.isInteractiveProperty.set(isInteractive);
  }
  
  public BooleanProperty isInteractiveProperty() {
    return this.isInteractiveProperty;
  }
  
  private ReadOnlyObjectWrapper<XDiagram> rootDiagramProperty = new ReadOnlyObjectWrapper<XDiagram>(this, "rootDiagram");
  
  public XDiagram getRootDiagram() {
    return this.rootDiagramProperty.get();
  }
  
  public ReadOnlyObjectProperty<XDiagram> rootDiagramProperty() {
    return this.rootDiagramProperty.getReadOnlyProperty();
  }
  
  private ReadOnlyObjectWrapper<XDiagram> diagramProperty = new ReadOnlyObjectWrapper<XDiagram>(this, "diagram");
  
  public XDiagram getDiagram() {
    return this.diagramProperty.get();
  }
  
  public ReadOnlyObjectProperty<XDiagram> diagramProperty() {
    return this.diagramProperty.getReadOnlyProperty();
  }
  
  private SimpleListProperty<DomainObjectProvider> domainObjectProvidersProperty = new SimpleListProperty<DomainObjectProvider>(this, "domainObjectProviders",_initDomainObjectProviders());
  
  private static final ObservableList<DomainObjectProvider> _initDomainObjectProviders() {
    ObservableList<DomainObjectProvider> _observableArrayList = FXCollections.<DomainObjectProvider>observableArrayList();
    return _observableArrayList;
  }
  
  public ObservableList<DomainObjectProvider> getDomainObjectProviders() {
    return this.domainObjectProvidersProperty.get();
  }
  
  public ListProperty<DomainObjectProvider> domainObjectProvidersProperty() {
    return this.domainObjectProvidersProperty;
  }
  
  private SimpleStringProperty fileNameProperty = new SimpleStringProperty(this, "fileName",_initFileName());
  
  private static final String _initFileName() {
    return null;
  }
  
  public String getFileName() {
    return this.fileNameProperty.get();
  }
  
  public void setFileName(final String fileName) {
    this.fileNameProperty.set(fileName);
  }
  
  public StringProperty fileNameProperty() {
    return this.fileNameProperty;
  }
  
  private SimpleBooleanProperty needsSaveProperty = new SimpleBooleanProperty(this, "needsSave");
  
  public boolean getNeedsSave() {
    return this.needsSaveProperty.get();
  }
  
  public void setNeedsSave(final boolean needsSave) {
    this.needsSaveProperty.set(needsSave);
  }
  
  public BooleanProperty needsSaveProperty() {
    return this.needsSaveProperty;
  }
}
