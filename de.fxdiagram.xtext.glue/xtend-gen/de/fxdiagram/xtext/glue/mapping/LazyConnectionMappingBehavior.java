package de.fxdiagram.xtext.glue.mapping;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRapidButton;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.behavior.AbstractHostBehavior;
import de.fxdiagram.core.behavior.Behavior;
import de.fxdiagram.core.extensions.ButtonExtensions;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.core.tools.AbstractChooser;
import de.fxdiagram.core.tools.ChooserConnectionProvider;
import de.fxdiagram.lib.tools.CarusselChooser;
import de.fxdiagram.lib.tools.CoverFlowChooser;
import de.fxdiagram.xtext.glue.XtextDomainObjectDescriptor;
import de.fxdiagram.xtext.glue.mapping.AbstractConnectionMappingCall;
import de.fxdiagram.xtext.glue.mapping.ConnectionMapping;
import de.fxdiagram.xtext.glue.mapping.NodeMapping;
import de.fxdiagram.xtext.glue.mapping.NodeMappingCall;
import de.fxdiagram.xtext.glue.mapping.XDiagramProvider;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.geometry.VPos;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.SVGPath;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class LazyConnectionMappingBehavior<MODEL extends Object, ARG extends Object> extends AbstractHostBehavior<XNode> {
  private XDiagramProvider diagramProvider;
  
  private AbstractConnectionMappingCall<MODEL,ARG> mappingCall;
  
  private List<XRapidButton> buttons = CollectionLiterals.<XRapidButton>newArrayList();
  
  private String tooltip;
  
  private boolean hostIsSource;
  
  public LazyConnectionMappingBehavior(final XNode host, final AbstractConnectionMappingCall<MODEL,ARG> mappingCall, final XDiagramProvider diagramProvider, final String tooltip, final boolean hostIsSource) {
    super(host);
    this.mappingCall = mappingCall;
    this.diagramProvider = diagramProvider;
    this.tooltip = tooltip;
    this.hostIsSource = hostIsSource;
  }
  
  public LazyConnectionMappingBehavior(final XNode host) {
    super(host);
  }
  
  public Class<? extends Behavior> getBehaviorKey() {
    return this.getClass();
  }
  
  protected void doActivate() {
    final Procedure1<XRapidButton> _function = new Procedure1<XRapidButton>() {
      public void apply(final XRapidButton button) {
        final AbstractChooser chooser = LazyConnectionMappingBehavior.this.createChooser(button);
        LazyConnectionMappingBehavior.this.populateChooser(chooser);
        XNode _host = LazyConnectionMappingBehavior.this.getHost();
        XRoot _root = CoreExtensions.getRoot(_host);
        _root.setCurrentTool(chooser);
      }
    };
    final Procedure1<XRapidButton> activateChooserAction = _function;
    Iterable<XRapidButton> _createButtons = this.createButtons(activateChooserAction);
    Iterables.<XRapidButton>addAll(this.buttons, _createButtons);
    XNode _host = this.getHost();
    XDiagram _diagram = CoreExtensions.getDiagram(_host);
    ObservableList<XRapidButton> _buttons = _diagram.getButtons();
    Iterables.<XRapidButton>addAll(_buttons, this.buttons);
  }
  
  protected Iterable<XRapidButton> createButtons(final Procedure1<? super XRapidButton> activateChooserAction) {
    XNode _host = this.getHost();
    SVGPath _triangleButton = ButtonExtensions.getTriangleButton(Side.LEFT, this.tooltip);
    XRapidButton _xRapidButton = new XRapidButton(_host, 0, 0.5, _triangleButton, activateChooserAction);
    XNode _host_1 = this.getHost();
    SVGPath _triangleButton_1 = ButtonExtensions.getTriangleButton(Side.RIGHT, this.tooltip);
    XRapidButton _xRapidButton_1 = new XRapidButton(_host_1, 1, 0.5, _triangleButton_1, activateChooserAction);
    XNode _host_2 = this.getHost();
    SVGPath _triangleButton_2 = ButtonExtensions.getTriangleButton(Side.TOP, this.tooltip);
    XRapidButton _xRapidButton_2 = new XRapidButton(_host_2, 0.5, 0, _triangleButton_2, activateChooserAction);
    XNode _host_3 = this.getHost();
    SVGPath _triangleButton_3 = ButtonExtensions.getTriangleButton(Side.BOTTOM, this.tooltip);
    XRapidButton _xRapidButton_3 = new XRapidButton(_host_3, 0.5, 1, _triangleButton_3, activateChooserAction);
    return Collections.<XRapidButton>unmodifiableList(Lists.<XRapidButton>newArrayList(_xRapidButton, _xRapidButton_1, _xRapidButton_2, _xRapidButton_3));
  }
  
  protected AbstractChooser createChooser(final XRapidButton button) {
    AbstractChooser _xblockexpression = null;
    {
      final Pos position = button.getChooserPosition();
      AbstractChooser _xifexpression = null;
      VPos _vpos = position.getVpos();
      boolean _equals = Objects.equal(_vpos, VPos.CENTER);
      if (_equals) {
        XNode _host = this.getHost();
        _xifexpression = new CarusselChooser(_host, position);
      } else {
        XNode _host_1 = this.getHost();
        _xifexpression = new CoverFlowChooser(_host_1, position);
      }
      final AbstractChooser chooser = _xifexpression;
      _xblockexpression = chooser;
    }
    return _xblockexpression;
  }
  
  protected Object populateChooser(final AbstractChooser chooser) {
    Object _xblockexpression = null;
    {
      XNode _host = this.getHost();
      DomainObjectDescriptor _domainObject = _host.getDomainObject();
      final XtextDomainObjectDescriptor<ARG> hostDescriptor = ((XtextDomainObjectDescriptor<ARG>) _domainObject);
      XNode _host_1 = this.getHost();
      XDiagram _diagram = CoreExtensions.getDiagram(_host_1);
      ObservableList<XConnection> _connections = _diagram.getConnections();
      final Function1<XConnection,DomainObjectDescriptor> _function = new Function1<XConnection,DomainObjectDescriptor>() {
        public DomainObjectDescriptor apply(final XConnection it) {
          return it.getDomainObject();
        }
      };
      List<DomainObjectDescriptor> _map = ListExtensions.<XConnection, DomainObjectDescriptor>map(_connections, _function);
      final Set<DomainObjectDescriptor> existingConnectionDescriptors = IterableExtensions.<DomainObjectDescriptor>toSet(_map);
      final Function1<ARG,Object> _function_1 = new Function1<ARG,Object>() {
        public Object apply(final ARG domainArgument) {
          Object _xblockexpression = null;
          {
            final List<MODEL> connectionDomainObjects = LazyConnectionMappingBehavior.this.diagramProvider.<MODEL, ARG>select(LazyConnectionMappingBehavior.this.mappingCall, domainArgument);
            final Procedure1<MODEL> _function = new Procedure1<MODEL>() {
              public void apply(final MODEL connectionDomainObject) {
                ConnectionMapping<MODEL> _connectionMapping = LazyConnectionMappingBehavior.this.mappingCall.getConnectionMapping();
                final XtextDomainObjectDescriptor<MODEL> connectionDescriptor = LazyConnectionMappingBehavior.this.diagramProvider.<MODEL>getDescriptor(connectionDomainObject, _connectionMapping);
                boolean _add = existingConnectionDescriptors.add(connectionDescriptor);
                if (_add) {
                  NodeMappingCall<?,MODEL> _elvis = null;
                  ConnectionMapping<MODEL> _connectionMapping_1 = LazyConnectionMappingBehavior.this.mappingCall.getConnectionMapping();
                  NodeMappingCall<?,MODEL> _source = _connectionMapping_1.getSource();
                  if (_source != null) {
                    _elvis = _source;
                  } else {
                    ConnectionMapping<MODEL> _connectionMapping_2 = LazyConnectionMappingBehavior.this.mappingCall.getConnectionMapping();
                    NodeMappingCall<?,MODEL> _target = _connectionMapping_2.getTarget();
                    _elvis = _target;
                  }
                  final NodeMappingCall<?,MODEL> nodeMappingCall = _elvis;
                  final List<?> nodeDomainObjects = LazyConnectionMappingBehavior.this.diagramProvider.select(nodeMappingCall, connectionDomainObject);
                  final Procedure1<Object> _function = new Procedure1<Object>() {
                    public void apply(final Object it) {
                      NodeMapping<?> _nodeMapping = nodeMappingCall.getNodeMapping();
                      XNode _createNode = LazyConnectionMappingBehavior.this.<Object>createNode(it, _nodeMapping);
                      chooser.addChoice(_createNode, connectionDescriptor);
                    }
                  };
                  IterableExtensions.forEach(nodeDomainObjects, _function);
                }
              }
            };
            IterableExtensions.<MODEL>forEach(connectionDomainObjects, _function);
            final ChooserConnectionProvider _function_1 = new ChooserConnectionProvider() {
              public XConnection getConnection(final XNode host, final XNode other, final DomainObjectDescriptor connectionDesc) {
                XConnection _xblockexpression = null;
                {
                  final XtextDomainObjectDescriptor<MODEL> descriptor = ((XtextDomainObjectDescriptor<MODEL>) connectionDesc);
                  ConnectionMapping<MODEL> _connectionMapping = LazyConnectionMappingBehavior.this.mappingCall.getConnectionMapping();
                  Function1<? super XtextDomainObjectDescriptor<MODEL>,? extends XConnection> _createConnection = _connectionMapping.getCreateConnection();
                  XConnection _apply = _createConnection.apply(descriptor);
                  final Procedure1<XConnection> _function = new Procedure1<XConnection>() {
                    public void apply(final XConnection it) {
                      final EventHandler<MouseEvent> _function = new EventHandler<MouseEvent>() {
                        public void handle(final MouseEvent it) {
                          int _clickCount = it.getClickCount();
                          boolean _equals = (_clickCount == 2);
                          if (_equals) {
                            descriptor.revealInEditor();
                          }
                        }
                      };
                      it.setOnMouseClicked(_function);
                      if (LazyConnectionMappingBehavior.this.hostIsSource) {
                        it.setSource(host);
                        it.setTarget(other);
                      } else {
                        it.setSource(other);
                        it.setTarget(host);
                      }
                    }
                  };
                  _xblockexpression = ObjectExtensions.<XConnection>operator_doubleArrow(_apply, _function);
                }
                return _xblockexpression;
              }
            };
            chooser.setConnectionProvider(_function_1);
            _xblockexpression = null;
          }
          return _xblockexpression;
        }
      };
      _xblockexpression = hostDescriptor.<Object>withDomainObject(_function_1);
    }
    return _xblockexpression;
  }
  
  protected <NODE extends Object> XNode createNode(final Object nodeDomainObject, final NodeMapping<?> nodeMapping) {
    XNode _xifexpression = null;
    boolean _isApplicable = nodeMapping.isApplicable(nodeDomainObject);
    if (_isApplicable) {
      XNode _xblockexpression = null;
      {
        final NodeMapping<NODE> nodeMappingCasted = ((NodeMapping<NODE>) nodeMapping);
        final XtextDomainObjectDescriptor<NODE> descriptor = this.diagramProvider.<NODE>getDescriptor(((NODE) nodeDomainObject), nodeMappingCasted);
        Function1<? super XtextDomainObjectDescriptor<NODE>,? extends XNode> _createNode = nodeMappingCasted.getCreateNode();
        XNode _apply = _createNode.apply(descriptor);
        final Procedure1<XNode> _function = new Procedure1<XNode>() {
          public void apply(final XNode it) {
            final EventHandler<MouseEvent> _function = new EventHandler<MouseEvent>() {
              public void handle(final MouseEvent it) {
                int _clickCount = it.getClickCount();
                boolean _equals = (_clickCount == 2);
                if (_equals) {
                  descriptor.revealInEditor();
                }
              }
            };
            it.setOnMouseClicked(_function);
          }
        };
        final XNode node = ObjectExtensions.<XNode>operator_doubleArrow(_apply, _function);
        List<AbstractConnectionMappingCall<?,NODE>> _outgoing = nodeMappingCasted.getOutgoing();
        final Function1<AbstractConnectionMappingCall<?,NODE>,Boolean> _function_1 = new Function1<AbstractConnectionMappingCall<?,NODE>,Boolean>() {
          public Boolean apply(final AbstractConnectionMappingCall<?,NODE> it) {
            return Boolean.valueOf(it.isLazy());
          }
        };
        Iterable<AbstractConnectionMappingCall<?,NODE>> _filter = IterableExtensions.<AbstractConnectionMappingCall<?,NODE>>filter(_outgoing, _function_1);
        final Procedure1<AbstractConnectionMappingCall<?,NODE>> _function_2 = new Procedure1<AbstractConnectionMappingCall<?,NODE>>() {
          public void apply(final AbstractConnectionMappingCall<?,NODE> it) {
            LazyConnectionMappingBehavior<?,NODE> _lazyConnectionMappingBehavior = new LazyConnectionMappingBehavior(node, it, LazyConnectionMappingBehavior.this.diagramProvider, "TODO", true);
            node.addBehavior(_lazyConnectionMappingBehavior);
          }
        };
        IterableExtensions.<AbstractConnectionMappingCall<?,NODE>>forEach(_filter, _function_2);
        List<AbstractConnectionMappingCall<?,NODE>> _incoming = nodeMappingCasted.getIncoming();
        final Function1<AbstractConnectionMappingCall<?,NODE>,Boolean> _function_3 = new Function1<AbstractConnectionMappingCall<?,NODE>,Boolean>() {
          public Boolean apply(final AbstractConnectionMappingCall<?,NODE> it) {
            return Boolean.valueOf(it.isLazy());
          }
        };
        Iterable<AbstractConnectionMappingCall<?,NODE>> _filter_1 = IterableExtensions.<AbstractConnectionMappingCall<?,NODE>>filter(_incoming, _function_3);
        final Procedure1<AbstractConnectionMappingCall<?,NODE>> _function_4 = new Procedure1<AbstractConnectionMappingCall<?,NODE>>() {
          public void apply(final AbstractConnectionMappingCall<?,NODE> it) {
            LazyConnectionMappingBehavior<?,NODE> _lazyConnectionMappingBehavior = new LazyConnectionMappingBehavior(node, it, LazyConnectionMappingBehavior.this.diagramProvider, "TODO", false);
            node.addBehavior(_lazyConnectionMappingBehavior);
          }
        };
        IterableExtensions.<AbstractConnectionMappingCall<?,NODE>>forEach(_filter_1, _function_4);
        _xblockexpression = node;
      }
      _xifexpression = _xblockexpression;
    } else {
      _xifexpression = null;
    }
    return _xifexpression;
  }
}
