package de.fxdiagram.core.layout;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.cau.cs.kieler.core.alg.BasicProgressMonitor;
import de.cau.cs.kieler.core.kgraph.KEdge;
import de.cau.cs.kieler.core.kgraph.KGraphData;
import de.cau.cs.kieler.core.kgraph.KGraphElement;
import de.cau.cs.kieler.core.kgraph.KGraphFactory;
import de.cau.cs.kieler.core.kgraph.KLabel;
import de.cau.cs.kieler.core.kgraph.KNode;
import de.cau.cs.kieler.core.math.KVector;
import de.cau.cs.kieler.core.math.KVectorChain;
import de.cau.cs.kieler.kiml.AbstractLayoutProvider;
import de.cau.cs.kieler.kiml.graphviz.layouter.GraphvizLayoutProvider;
import de.cau.cs.kieler.kiml.klayoutdata.KEdgeLayout;
import de.cau.cs.kieler.kiml.klayoutdata.KInsets;
import de.cau.cs.kieler.kiml.klayoutdata.KLayoutDataFactory;
import de.cau.cs.kieler.kiml.klayoutdata.KPoint;
import de.cau.cs.kieler.kiml.klayoutdata.KShapeLayout;
import de.cau.cs.kieler.kiml.options.EdgeLabelPlacement;
import de.cau.cs.kieler.kiml.options.EdgeRouting;
import de.cau.cs.kieler.kiml.options.LayoutOptions;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XConnectionLabel;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XShape;
import de.fxdiagram.core.anchors.ArrowHead;
import de.fxdiagram.core.behavior.MoveBehavior;
import de.fxdiagram.core.command.AbstractAnimationCommand;
import de.fxdiagram.core.command.LazyCommand;
import de.fxdiagram.core.command.MoveCommand;
import de.fxdiagram.core.command.ParallelAnimationCommand;
import de.fxdiagram.core.layout.ConnectionMorphCommand;
import de.fxdiagram.core.layout.LayoutType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class Layouter {
  private final static int NODE_PADDING = 8;
  
  @Extension
  private KLayoutDataFactory _kLayoutDataFactory = KLayoutDataFactory.eINSTANCE;
  
  @Extension
  private KGraphFactory _kGraphFactory = KGraphFactory.eINSTANCE;
  
  public Layouter() {
    AbstractLayoutProvider _layoutProvider = this.getLayoutProvider(LayoutType.DOT);
    _layoutProvider.dispose();
  }
  
  public LazyCommand createLayoutCommand(final LayoutType type, final XDiagram diagram, final Duration duration) {
    final LazyCommand _function = new LazyCommand() {
      @Override
      protected AbstractAnimationCommand createDelegate() {
        final HashMap<Object, KGraphElement> cache = CollectionLiterals.<Object, KGraphElement>newHashMap();
        diagram.layout();
        final KNode kRoot = Layouter.this.toKRootNode(diagram, cache);
        final AbstractLayoutProvider provider = Layouter.this.getLayoutProvider(type);
        try {
          BasicProgressMonitor _basicProgressMonitor = new BasicProgressMonitor();
          provider.doLayout(kRoot, _basicProgressMonitor);
          return Layouter.this.composeCommand(cache, duration);
        } finally {
          provider.dispose();
        }
      }
    };
    return _function;
  }
  
  protected AbstractLayoutProvider getLayoutProvider(final LayoutType type) {
    GraphvizLayoutProvider _graphvizLayoutProvider = new GraphvizLayoutProvider();
    final Procedure1<GraphvizLayoutProvider> _function = new Procedure1<GraphvizLayoutProvider>() {
      @Override
      public void apply(final GraphvizLayoutProvider it) {
        String _string = type.toString();
        it.initialize(_string);
      }
    };
    return ObjectExtensions.<GraphvizLayoutProvider>operator_doubleArrow(_graphvizLayoutProvider, _function);
  }
  
  protected ParallelAnimationCommand composeCommand(final Map<Object, KGraphElement> map, final Duration duration) {
    final ParallelAnimationCommand composite = new ParallelAnimationCommand();
    Set<Map.Entry<Object, KGraphElement>> _entrySet = map.entrySet();
    for (final Map.Entry<Object, KGraphElement> entry : _entrySet) {
      {
        final Object xElement = entry.getKey();
        final KGraphElement kElement = entry.getValue();
        boolean _matched = false;
        if (!_matched) {
          if (xElement instanceof XNode) {
            _matched=true;
            MoveBehavior _behavior = ((XNode)xElement).<MoveBehavior>getBehavior(MoveBehavior.class);
            _behavior.setIsManuallyPlaced(false);
            EList<KGraphData> _data = kElement.getData();
            Iterable<KShapeLayout> _filter = Iterables.<KShapeLayout>filter(_data, KShapeLayout.class);
            final KShapeLayout shapeLayout = IterableExtensions.<KShapeLayout>head(_filter);
            float _xpos = shapeLayout.getXpos();
            Bounds _layoutBounds = ((XNode)xElement).getLayoutBounds();
            double _minX = _layoutBounds.getMinX();
            double _minus = (_xpos - _minX);
            double _plus = (_minus + (Layouter.NODE_PADDING / 2));
            float _ypos = shapeLayout.getYpos();
            Bounds _layoutBounds_1 = ((XNode)xElement).getLayoutBounds();
            double _minY = _layoutBounds_1.getMinY();
            double _minus_1 = (_ypos - _minY);
            double _plus_1 = (_minus_1 + (Layouter.NODE_PADDING / 2));
            MoveCommand _moveCommand = new MoveCommand(((XShape)xElement), _plus, _plus_1);
            final Procedure1<MoveCommand> _function = new Procedure1<MoveCommand>() {
              @Override
              public void apply(final MoveCommand it) {
                it.setExecuteDuration(duration);
              }
            };
            MoveCommand _doubleArrow = ObjectExtensions.<MoveCommand>operator_doubleArrow(_moveCommand, _function);
            composite.operator_add(_doubleArrow);
          }
        }
        if (!_matched) {
          if (xElement instanceof XConnection) {
            _matched=true;
            ObservableList<XConnectionLabel> _labels = ((XConnection)xElement).getLabels();
            final Consumer<XConnectionLabel> _function = new Consumer<XConnectionLabel>() {
              @Override
              public void accept(final XConnectionLabel it) {
                it.place(true);
              }
            };
            _labels.forEach(_function);
            EList<KGraphData> _data = kElement.getData();
            Iterable<KEdgeLayout> _filter = Iterables.<KEdgeLayout>filter(_data, KEdgeLayout.class);
            final KEdgeLayout edgeLayout = IterableExtensions.<KEdgeLayout>head(_filter);
            final KVectorChain layoutPoints = edgeLayout.createVectorChain();
            XConnection.Kind _switchResult_1 = null;
            EdgeRouting _property = edgeLayout.<EdgeRouting>getProperty(LayoutOptions.EDGE_ROUTING);
            if (_property != null) {
              switch (_property) {
                case SPLINES:
                  XConnection.Kind _xifexpression = null;
                  int _size = layoutPoints.size();
                  int _minus = (_size - 1);
                  int _modulo = (_minus % 3);
                  boolean _equals = (_modulo == 0);
                  if (_equals) {
                    _xifexpression = XConnection.Kind.CUBIC_CURVE;
                  } else {
                    XConnection.Kind _xifexpression_1 = null;
                    int _size_1 = layoutPoints.size();
                    int _minus_1 = (_size_1 - 1);
                    int _modulo_1 = (_minus_1 % 2);
                    boolean _equals_1 = (_modulo_1 == 0);
                    if (_equals_1) {
                      _xifexpression_1 = XConnection.Kind.QUAD_CURVE;
                    } else {
                      _xifexpression_1 = XConnection.Kind.POLYLINE;
                    }
                    _xifexpression = _xifexpression_1;
                  }
                  _switchResult_1 = _xifexpression;
                  break;
                default:
                  _switchResult_1 = XConnection.Kind.POLYLINE;
                  break;
              }
            } else {
              _switchResult_1 = XConnection.Kind.POLYLINE;
            }
            final XConnection.Kind newKind = _switchResult_1;
            final Function1<KVector, Point2D> _function_1 = new Function1<KVector, Point2D>() {
              @Override
              public Point2D apply(final KVector it) {
                return new Point2D(it.x, it.y);
              }
            };
            List<Point2D> _map = ListExtensions.<KVector, Point2D>map(layoutPoints, _function_1);
            ConnectionMorphCommand _connectionMorphCommand = new ConnectionMorphCommand(((XConnection)xElement), newKind, _map);
            final Procedure1<ConnectionMorphCommand> _function_2 = new Procedure1<ConnectionMorphCommand>() {
              @Override
              public void apply(final ConnectionMorphCommand it) {
                it.setExecuteDuration(duration);
              }
            };
            ConnectionMorphCommand _doubleArrow = ObjectExtensions.<ConnectionMorphCommand>operator_doubleArrow(_connectionMorphCommand, _function_2);
            composite.operator_add(_doubleArrow);
          }
        }
      }
    }
    return composite;
  }
  
  protected KNode toKRootNode(final XDiagram it, final Map<Object, KGraphElement> cache) {
    KNode _xblockexpression = null;
    {
      final KNode kRoot = this._kGraphFactory.createKNode();
      final KShapeLayout shapeLayout = this._kLayoutDataFactory.createKShapeLayout();
      KInsets _createKInsets = this._kLayoutDataFactory.createKInsets();
      shapeLayout.setInsets(_createKInsets);
      EList<KGraphData> _data = kRoot.getData();
      _data.add(shapeLayout);
      cache.put(it, kRoot);
      ObservableList<XNode> _nodes = it.getNodes();
      final Consumer<XNode> _function = new Consumer<XNode>() {
        @Override
        public void accept(final XNode it) {
          EList<KNode> _children = kRoot.getChildren();
          KNode _kNode = Layouter.this.toKNode(it, cache);
          _children.add(_kNode);
        }
      };
      _nodes.forEach(_function);
      double spacing = 60.0;
      ObservableList<XConnection> _connections = it.getConnections();
      for (final XConnection it_1 : _connections) {
        {
          this.toKEdge(it_1, cache);
          double minLength = ((double) Layouter.NODE_PADDING);
          ObservableList<XConnectionLabel> _labels = it_1.getLabels();
          for (final XConnectionLabel label : _labels) {
            double _minLength = minLength;
            Bounds _boundsInLocal = label.getBoundsInLocal();
            double _width = _boundsInLocal.getWidth();
            minLength = (_minLength + _width);
          }
          ArrowHead _sourceArrowHead = it_1.getSourceArrowHead();
          boolean _notEquals = (!Objects.equal(_sourceArrowHead, null));
          if (_notEquals) {
            double _minLength_1 = minLength;
            ArrowHead _sourceArrowHead_1 = it_1.getSourceArrowHead();
            double _width_1 = _sourceArrowHead_1.getWidth();
            minLength = (_minLength_1 + _width_1);
          }
          ArrowHead _targetArrowHead = it_1.getTargetArrowHead();
          boolean _notEquals_1 = (!Objects.equal(_targetArrowHead, null));
          if (_notEquals_1) {
            double _minLength_2 = minLength;
            ArrowHead _targetArrowHead_1 = it_1.getTargetArrowHead();
            double _width_2 = _targetArrowHead_1.getWidth();
            minLength = (_minLength_2 + _width_2);
          }
          double _max = Math.max(spacing, minLength);
          spacing = _max;
        }
      }
      shapeLayout.<Float>setProperty(LayoutOptions.SPACING, Float.valueOf(((float) spacing)));
      _xblockexpression = kRoot;
    }
    return _xblockexpression;
  }
  
  protected KNode toKNode(final XNode it, final Map<Object, KGraphElement> cache) {
    KNode _xblockexpression = null;
    {
      final KNode kNode = this._kGraphFactory.createKNode();
      final KShapeLayout shapeLayout = this._kLayoutDataFactory.createKShapeLayout();
      shapeLayout.setSize(
        (((float) it.getLayoutBounds().getWidth()) + Layouter.NODE_PADDING), 
        (((float) it.getLayoutBounds().getHeight()) + Layouter.NODE_PADDING));
      EList<KGraphData> _data = kNode.getData();
      _data.add(shapeLayout);
      cache.put(it, kNode);
      _xblockexpression = kNode;
    }
    return _xblockexpression;
  }
  
  protected KEdge toKEdge(final XConnection it, final Map<Object, KGraphElement> cache) {
    KEdge _xblockexpression = null;
    {
      XNode _source = it.getSource();
      final KGraphElement kSource = cache.get(_source);
      XNode _target = it.getTarget();
      final KGraphElement kTarget = cache.get(_target);
      KEdge _xifexpression = null;
      boolean _and = false;
      if (!(kSource instanceof KNode)) {
        _and = false;
      } else {
        _and = (kTarget instanceof KNode);
      }
      if (_and) {
        KEdge _xblockexpression_1 = null;
        {
          final KEdge kEdge = this._kGraphFactory.createKEdge();
          EList<KEdge> _outgoingEdges = ((KNode) kSource).getOutgoingEdges();
          _outgoingEdges.add(kEdge);
          EList<KEdge> _incomingEdges = ((KNode) kTarget).getIncomingEdges();
          _incomingEdges.add(kEdge);
          final KEdgeLayout edgeLayout = this._kLayoutDataFactory.createKEdgeLayout();
          KPoint _createKPoint = this._kLayoutDataFactory.createKPoint();
          edgeLayout.setSourcePoint(_createKPoint);
          KPoint _createKPoint_1 = this._kLayoutDataFactory.createKPoint();
          edgeLayout.setTargetPoint(_createKPoint_1);
          EList<KGraphData> _data = kEdge.getData();
          _data.add(edgeLayout);
          cache.put(it, kEdge);
          _xblockexpression_1 = kEdge;
        }
        _xifexpression = _xblockexpression_1;
      } else {
        _xifexpression = null;
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  protected KLabel toKLabel(final XConnectionLabel it, final Map<Object, KGraphElement> cache) {
    KLabel _xblockexpression = null;
    {
      final KLabel kLabel = this._kGraphFactory.createKLabel();
      String _elvis = null;
      Text _text = null;
      if (it!=null) {
        _text=it.getText();
      }
      String _text_1 = null;
      if (_text!=null) {
        _text_1=_text.getText();
      }
      if (_text_1 != null) {
        _elvis = _text_1;
      } else {
        _elvis = "";
      }
      kLabel.setText(_elvis);
      final KShapeLayout shapeLayout = this._kLayoutDataFactory.createKShapeLayout();
      Bounds _layoutBounds = it.getLayoutBounds();
      double _width = _layoutBounds.getWidth();
      Bounds _layoutBounds_1 = it.getLayoutBounds();
      double _height = _layoutBounds_1.getHeight();
      shapeLayout.setSize(
        ((float) _width), 
        ((float) _height));
      shapeLayout.<Integer>setProperty(LayoutOptions.FONT_SIZE, Integer.valueOf(12));
      shapeLayout.<EdgeLabelPlacement>setProperty(LayoutOptions.EDGE_LABEL_PLACEMENT, EdgeLabelPlacement.CENTER);
      EList<KGraphData> _data = kLabel.getData();
      _data.add(shapeLayout);
      cache.put(it, kLabel);
      _xblockexpression = kLabel;
    }
    return _xblockexpression;
  }
}
