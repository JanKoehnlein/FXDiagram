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
import de.fxdiagram.core.XControlPoint;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XDiagramContainer;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XShape;
import de.fxdiagram.core.anchors.ArrowHead;
import de.fxdiagram.core.anchors.ConnectionRouter;
import de.fxdiagram.core.behavior.MoveBehavior;
import de.fxdiagram.core.command.AnimationCommand;
import de.fxdiagram.core.command.LazyCommand;
import de.fxdiagram.core.command.MoveCommand;
import de.fxdiagram.core.command.ParallelAnimationCommand;
import de.fxdiagram.core.extensions.BoundsExtensions;
import de.fxdiagram.core.extensions.Point2DExtensions;
import de.fxdiagram.core.layout.ConnectionRelayoutCommand;
import de.fxdiagram.core.layout.LayoutParameters;
import de.fxdiagram.core.layout.LayoutType;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import javafx.collections.ObservableList;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Dimension2D;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.ExclusiveRange;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.Functions.Function2;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class Layouter {
  @Extension
  private KLayoutDataFactory _kLayoutDataFactory = KLayoutDataFactory.eINSTANCE;
  
  @Extension
  private KGraphFactory _kGraphFactory = KGraphFactory.eINSTANCE;
  
  private Map<LayoutType, AbstractLayoutProvider> layoutProviders = CollectionLiterals.<LayoutType, AbstractLayoutProvider>newHashMap();
  
  public Layouter() {
    LayoutParameters _layoutParameters = new LayoutParameters();
    this.getLayoutProvider(_layoutParameters);
  }
  
  public LazyCommand createLayoutCommand(final LayoutParameters parameters, final XDiagram diagram, final Duration duration) {
    return this.createLayoutCommand(parameters, diagram, duration, null);
  }
  
  public LazyCommand createLayoutCommand(final XDiagram diagram, final Duration duration) {
    LayoutParameters _layoutParameters = diagram.getLayoutParameters();
    return this.createLayoutCommand(_layoutParameters, diagram, duration, null);
  }
  
  public LazyCommand createLayoutCommand(final LayoutParameters parameters, final XDiagram diagram, final Duration duration, final XShape fixed) {
    final LazyCommand _function = new LazyCommand() {
      @Override
      protected AnimationCommand createDelegate() {
        final HashMap<Object, KGraphElement> cache = Layouter.this.calculateLayout(parameters, diagram);
        return Layouter.this.composeCommand(cache, duration, fixed, diagram);
      }
    };
    return _function;
  }
  
  public void layout(final LayoutParameters parameters, final XDiagram diagram, final XShape fixed) {
    final HashMap<Object, KGraphElement> cache = this.calculateLayout(parameters, diagram);
    this.applyLayout(cache, fixed, diagram);
  }
  
  protected HashMap<Object, KGraphElement> calculateLayout(final LayoutParameters parameters, final XDiagram diagram) {
    LayoutParameters _elvis = null;
    if (parameters != null) {
      _elvis = parameters;
    } else {
      LayoutParameters _layoutParameters = new LayoutParameters();
      _elvis = _layoutParameters;
    }
    final LayoutParameters theParameters = _elvis;
    final AbstractLayoutProvider provider = this.getLayoutProvider(theParameters);
    final HashMap<Object, KGraphElement> cache = CollectionLiterals.<Object, KGraphElement>newHashMap();
    diagram.layout();
    final KNode kRoot = this.toKRootNode(diagram, theParameters, cache);
    BasicProgressMonitor _basicProgressMonitor = new BasicProgressMonitor();
    provider.doLayout(kRoot, _basicProgressMonitor);
    return cache;
  }
  
  protected AbstractLayoutProvider getLayoutProvider(final LayoutParameters parameters) {
    LayoutType _type = parameters.getType();
    AbstractLayoutProvider layoutProvider = this.layoutProviders.get(_type);
    boolean _equals = Objects.equal(layoutProvider, null);
    if (_equals) {
      GraphvizLayoutProvider _graphvizLayoutProvider = new GraphvizLayoutProvider();
      final Procedure1<GraphvizLayoutProvider> _function = (GraphvizLayoutProvider it) -> {
        LayoutType _type_1 = parameters.getType();
        String _string = _type_1.toString();
        it.initialize(_string);
      };
      GraphvizLayoutProvider _doubleArrow = ObjectExtensions.<GraphvizLayoutProvider>operator_doubleArrow(_graphvizLayoutProvider, _function);
      layoutProvider = _doubleArrow;
      LayoutType _type_1 = parameters.getType();
      this.layoutProviders.put(_type_1, layoutProvider);
    }
    return layoutProvider;
  }
  
  protected void applyLayout(final Map<Object, KGraphElement> map, final XShape fixed, final XDiagram diagram) {
    final Point2D delta = this.getDelta(map, fixed, diagram);
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
            if (_behavior!=null) {
              _behavior.setManuallyPlaced(false);
            }
            EList<KGraphData> _data = kElement.getData();
            Iterable<KShapeLayout> _filter = Iterables.<KShapeLayout>filter(_data, KShapeLayout.class);
            final KShapeLayout shapeLayout = IterableExtensions.<KShapeLayout>head(_filter);
            Point2D _xifexpression = null;
            boolean _isTopLevel = this.isTopLevel(kElement);
            if (_isTopLevel) {
              _xifexpression = delta;
            } else {
              Point2D _xblockexpression = null;
              {
                EObject _eContainer = kElement.eContainer();
                KShapeLayout _data_1 = ((KNode) _eContainer).<KShapeLayout>getData(KShapeLayout.class);
                final KInsets insets = _data_1.getInsets();
                float _left = 0f;
                if (insets!=null) {
                  _left=insets.getLeft();
                }
                float _minus = (-_left);
                float _top = 0f;
                if (insets!=null) {
                  _top=insets.getTop();
                }
                float _minus_1 = (-_top);
                _xblockexpression = new Point2D(_minus, _minus_1);
              }
              _xifexpression = _xblockexpression;
            }
            final Point2D correction = _xifexpression;
            float _xpos = shapeLayout.getXpos();
            double _x = correction.getX();
            double _minus = (_xpos - _x);
            ((XNode)xElement).setLayoutX(_minus);
            float _ypos = shapeLayout.getYpos();
            double _y = correction.getY();
            double _minus_1 = (_ypos - _y);
            ((XNode)xElement).setLayoutY(_minus_1);
          }
        }
        if (!_matched) {
          if (xElement instanceof XConnection) {
            _matched=true;
            ObservableList<XConnectionLabel> _labels = ((XConnection)xElement).getLabels();
            final Consumer<XConnectionLabel> _function = (XConnectionLabel it) -> {
              it.place(true);
            };
            _labels.forEach(_function);
            EList<KGraphData> _data = kElement.getData();
            Iterable<KEdgeLayout> _filter = Iterables.<KEdgeLayout>filter(_data, KEdgeLayout.class);
            final KEdgeLayout edgeLayout = IterableExtensions.<KEdgeLayout>head(_filter);
            KVectorChain _createVectorChain = edgeLayout.createVectorChain();
            final Function1<KVector, Point2D> _function_1 = (KVector it) -> {
              return new Point2D(it.x, it.y);
            };
            final List<Point2D> layoutPoints = ListExtensions.<KVector, Point2D>map(_createVectorChain, _function_1);
            XConnection.Kind _xifexpression = null;
            LayoutParameters _layoutParameters = diagram.getLayoutParameters();
            boolean _useSplines = _layoutParameters.getUseSplines();
            if (_useSplines) {
              XConnection.Kind _switchResult_1 = null;
              EdgeRouting _property = edgeLayout.<EdgeRouting>getProperty(LayoutOptions.EDGE_ROUTING);
              if (_property != null) {
                switch (_property) {
                  case SPLINES:
                    XConnection.Kind _xifexpression_1 = null;
                    int _size = layoutPoints.size();
                    int _minus = (_size - 1);
                    int _modulo = (_minus % 3);
                    boolean _equals = (_modulo == 0);
                    if (_equals) {
                      _xifexpression_1 = XConnection.Kind.CUBIC_CURVE;
                    } else {
                      XConnection.Kind _xifexpression_2 = null;
                      int _size_1 = layoutPoints.size();
                      int _minus_1 = (_size_1 - 1);
                      int _modulo_1 = (_minus_1 % 2);
                      boolean _equals_1 = (_modulo_1 == 0);
                      if (_equals_1) {
                        _xifexpression_2 = XConnection.Kind.QUAD_CURVE;
                      } else {
                        _xifexpression_2 = XConnection.Kind.POLYLINE;
                      }
                      _xifexpression_1 = _xifexpression_2;
                    }
                    _switchResult_1 = _xifexpression_1;
                    break;
                  default:
                    _switchResult_1 = XConnection.Kind.POLYLINE;
                    break;
                }
              } else {
                _switchResult_1 = XConnection.Kind.POLYLINE;
              }
              _xifexpression = _switchResult_1;
            } else {
              _xifexpression = XConnection.Kind.POLYLINE;
            }
            final XConnection.Kind newKind = _xifexpression;
            boolean _equals_2 = Objects.equal(newKind, XConnection.Kind.POLYLINE);
            if (_equals_2) {
              this.removeDuplicates(layoutPoints);
            }
            ((XConnection)xElement).setKind(newKind);
            ObservableList<XControlPoint> _controlPoints = ((XConnection)xElement).getControlPoints();
            int _size_2 = _controlPoints.size();
            int _size_3 = layoutPoints.size();
            boolean _lessThan = (_size_2 < _size_3);
            if (_lessThan) {
              ConnectionRouter _connectionRouter = ((XConnection)xElement).getConnectionRouter();
              int _size_4 = layoutPoints.size();
              _connectionRouter.growToSize(_size_4);
            } else {
              ConnectionRouter _connectionRouter_1 = ((XConnection)xElement).getConnectionRouter();
              int _size_5 = layoutPoints.size();
              _connectionRouter_1.shrinkToSize(_size_5);
            }
            ConnectionRouter _connectionRouter_2 = ((XConnection)xElement).getConnectionRouter();
            _connectionRouter_2.setSplineShapeKeeperEnabled(false);
            final KNode kSource = ((KEdge) kElement).getSource();
            Point2D _xifexpression_3 = null;
            boolean _isTopLevel = this.isTopLevel(kSource);
            if (_isTopLevel) {
              _xifexpression_3 = delta;
            } else {
              Point2D _xblockexpression = null;
              {
                EObject _eContainer = kSource.eContainer();
                KShapeLayout _data_1 = ((KNode) _eContainer).<KShapeLayout>getData(KShapeLayout.class);
                final KInsets insets = _data_1.getInsets();
                double _x = delta.getX();
                float _left = 0f;
                if (insets!=null) {
                  _left=insets.getLeft();
                }
                double _minus_2 = (_x - _left);
                double _y = delta.getY();
                float _top = 0f;
                if (insets!=null) {
                  _top=insets.getTop();
                }
                double _minus_3 = (_y - _top);
                _xblockexpression = new Point2D(_minus_2, _minus_3);
              }
              _xifexpression_3 = _xblockexpression;
            }
            final Point2D correction = _xifexpression_3;
            Iterable<Point2D> _layoutPointsInRoot = this.layoutPointsInRoot(layoutPoints, kSource);
            List<Point2D> _list = IterableExtensions.<Point2D>toList(_layoutPointsInRoot);
            final Function1<Point2D, Point2D> _function_2 = (Point2D it) -> {
              return Point2DExtensions.operator_minus(it, correction);
            };
            final List<Point2D> xLayoutPoints = ListExtensions.<Point2D, Point2D>map(_list, _function_2);
            int _size_6 = layoutPoints.size();
            int _minus_2 = (_size_6 - 1);
            ExclusiveRange _doubleDotLessThan = new ExclusiveRange(1, _minus_2, true);
            for (final Integer i : _doubleDotLessThan) {
              {
                ObservableList<XControlPoint> _controlPoints_1 = ((XConnection)xElement).getControlPoints();
                final XControlPoint controlPoint = _controlPoints_1.get((i).intValue());
                Point2D _get = xLayoutPoints.get((i).intValue());
                double _x = _get.getX();
                controlPoint.setLayoutX(_x);
                Point2D _get_1 = xLayoutPoints.get((i).intValue());
                double _y = _get_1.getY();
                controlPoint.setLayoutY(_y);
              }
            }
          }
        }
      }
    }
  }
  
  protected void removeDuplicates(final List<Point2D> layoutPoints) {
    for (int i = (layoutPoints.size() - 2); (i > 0); i--) {
      Point2D _get = layoutPoints.get((i - 1));
      Point2D _get_1 = layoutPoints.get(i);
      Point2D _get_2 = layoutPoints.get((i + 1));
      boolean _areOnSameLine = Point2DExtensions.areOnSameLine(_get, _get_1, _get_2);
      if (_areOnSameLine) {
        layoutPoints.remove(i);
      }
    }
  }
  
  protected ParallelAnimationCommand composeCommand(final Map<Object, KGraphElement> map, final Duration duration, final XShape fixed, final XDiagram diagram) {
    final ParallelAnimationCommand composite = new ParallelAnimationCommand();
    final Point2D delta = this.getDelta(map, fixed, diagram);
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
            _behavior.setManuallyPlaced(false);
            EList<KGraphData> _data = kElement.getData();
            Iterable<KShapeLayout> _filter = Iterables.<KShapeLayout>filter(_data, KShapeLayout.class);
            final KShapeLayout shapeLayout = IterableExtensions.<KShapeLayout>head(_filter);
            Point2D _xifexpression = null;
            boolean _isTopLevel = this.isTopLevel(kElement);
            if (_isTopLevel) {
              _xifexpression = delta;
            } else {
              Point2D _xblockexpression = null;
              {
                EObject _eContainer = kElement.eContainer();
                KShapeLayout _data_1 = ((KNode) _eContainer).<KShapeLayout>getData(KShapeLayout.class);
                final KInsets insets = _data_1.getInsets();
                float _left = 0f;
                if (insets!=null) {
                  _left=insets.getLeft();
                }
                float _minus = (-_left);
                float _top = 0f;
                if (insets!=null) {
                  _top=insets.getTop();
                }
                float _minus_1 = (-_top);
                _xblockexpression = new Point2D(_minus, _minus_1);
              }
              _xifexpression = _xblockexpression;
            }
            final Point2D correction = _xifexpression;
            float _xpos = shapeLayout.getXpos();
            double _x = correction.getX();
            double _minus = (_xpos - _x);
            float _ypos = shapeLayout.getYpos();
            double _y = correction.getY();
            double _minus_1 = (_ypos - _y);
            MoveCommand _moveCommand = new MoveCommand(((Node)xElement), _minus, _minus_1);
            final Procedure1<MoveCommand> _function = (MoveCommand it) -> {
              it.setExecuteDuration(duration);
            };
            MoveCommand _doubleArrow = ObjectExtensions.<MoveCommand>operator_doubleArrow(_moveCommand, _function);
            composite.operator_add(_doubleArrow);
          }
        }
        if (!_matched) {
          if (xElement instanceof XConnection) {
            _matched=true;
            ObservableList<XConnectionLabel> _labels = ((XConnection)xElement).getLabels();
            final Consumer<XConnectionLabel> _function = (XConnectionLabel it) -> {
              it.place(true);
            };
            _labels.forEach(_function);
            EList<KGraphData> _data = kElement.getData();
            Iterable<KEdgeLayout> _filter = Iterables.<KEdgeLayout>filter(_data, KEdgeLayout.class);
            final KEdgeLayout edgeLayout = IterableExtensions.<KEdgeLayout>head(_filter);
            KVectorChain _createVectorChain = edgeLayout.createVectorChain();
            final Function1<KVector, Point2D> _function_1 = (KVector it) -> {
              return new Point2D(it.x, it.y);
            };
            final List<Point2D> layoutPoints = ListExtensions.<KVector, Point2D>map(_createVectorChain, _function_1);
            XConnection.Kind _xifexpression = null;
            LayoutParameters _layoutParameters = diagram.getLayoutParameters();
            boolean _useSplines = _layoutParameters.getUseSplines();
            if (_useSplines) {
              XConnection.Kind _switchResult_1 = null;
              EdgeRouting _property = edgeLayout.<EdgeRouting>getProperty(LayoutOptions.EDGE_ROUTING);
              if (_property != null) {
                switch (_property) {
                  case SPLINES:
                    XConnection.Kind _xifexpression_1 = null;
                    int _size = layoutPoints.size();
                    int _minus = (_size - 1);
                    int _modulo = (_minus % 3);
                    boolean _equals = (_modulo == 0);
                    if (_equals) {
                      _xifexpression_1 = XConnection.Kind.CUBIC_CURVE;
                    } else {
                      XConnection.Kind _xifexpression_2 = null;
                      int _size_1 = layoutPoints.size();
                      int _minus_1 = (_size_1 - 1);
                      int _modulo_1 = (_minus_1 % 2);
                      boolean _equals_1 = (_modulo_1 == 0);
                      if (_equals_1) {
                        _xifexpression_2 = XConnection.Kind.QUAD_CURVE;
                      } else {
                        _xifexpression_2 = XConnection.Kind.POLYLINE;
                      }
                      _xifexpression_1 = _xifexpression_2;
                    }
                    _switchResult_1 = _xifexpression_1;
                    break;
                  default:
                    _switchResult_1 = XConnection.Kind.POLYLINE;
                    break;
                }
              } else {
                _switchResult_1 = XConnection.Kind.POLYLINE;
              }
              _xifexpression = _switchResult_1;
            } else {
              _xifexpression = XConnection.Kind.POLYLINE;
            }
            final XConnection.Kind newKind = _xifexpression;
            boolean _equals_2 = Objects.equal(newKind, XConnection.Kind.POLYLINE);
            if (_equals_2) {
              this.removeDuplicates(layoutPoints);
            }
            ConnectionRouter _connectionRouter = ((XConnection)xElement).getConnectionRouter();
            _connectionRouter.setSplineShapeKeeperEnabled(false);
            final KNode kSource = ((KEdge) kElement).getSource();
            Point2D _xifexpression_3 = null;
            boolean _isTopLevel = this.isTopLevel(kSource);
            if (_isTopLevel) {
              _xifexpression_3 = delta;
            } else {
              Point2D _xblockexpression = null;
              {
                EObject _eContainer = kSource.eContainer();
                KShapeLayout _data_1 = ((KNode) _eContainer).<KShapeLayout>getData(KShapeLayout.class);
                final KInsets insets = _data_1.getInsets();
                double _x = delta.getX();
                float _left = 0f;
                if (insets!=null) {
                  _left=insets.getLeft();
                }
                double _minus_2 = (_x - _left);
                double _y = delta.getY();
                float _top = 0f;
                if (insets!=null) {
                  _top=insets.getTop();
                }
                double _minus_3 = (_y - _top);
                _xblockexpression = new Point2D(_minus_2, _minus_3);
              }
              _xifexpression_3 = _xblockexpression;
            }
            final Point2D correction = _xifexpression_3;
            Iterable<Point2D> _layoutPointsInRoot = this.layoutPointsInRoot(layoutPoints, kSource);
            List<Point2D> _list = IterableExtensions.<Point2D>toList(_layoutPointsInRoot);
            final Function1<Point2D, Point2D> _function_2 = (Point2D it) -> {
              return Point2DExtensions.operator_minus(it, correction);
            };
            final List<Point2D> xLayoutPoints = ListExtensions.<Point2D, Point2D>map(_list, _function_2);
            ConnectionRelayoutCommand _connectionRelayoutCommand = new ConnectionRelayoutCommand(((XConnection)xElement), newKind, xLayoutPoints);
            final Procedure1<ConnectionRelayoutCommand> _function_3 = (ConnectionRelayoutCommand it) -> {
              it.setExecuteDuration(duration);
            };
            ConnectionRelayoutCommand _doubleArrow = ObjectExtensions.<ConnectionRelayoutCommand>operator_doubleArrow(_connectionRelayoutCommand, _function_3);
            composite.operator_add(_doubleArrow);
          }
        }
      }
    }
    return composite;
  }
  
  protected boolean isTopLevel(final KGraphElement kElement) {
    boolean _switchResult = false;
    boolean _matched = false;
    if (!_matched) {
      if (kElement instanceof KNode) {
        _matched=true;
        EObject _eContainer = null;
        if (((KNode)kElement)!=null) {
          _eContainer=((KNode)kElement).eContainer();
        }
        EObject _eContainer_1 = null;
        if (_eContainer!=null) {
          _eContainer_1=_eContainer.eContainer();
        }
        _switchResult = Objects.equal(_eContainer_1, null);
      }
    }
    if (!_matched) {
      if (kElement instanceof KEdge) {
        _matched=true;
        KNode _source = ((KEdge)kElement).getSource();
        _switchResult = this.isTopLevel(_source);
      }
    }
    if (!_matched) {
      _switchResult = false;
    }
    return _switchResult;
  }
  
  protected Iterable<Point2D> layoutPointsInRoot(final Iterable<Point2D> layoutPoints, final KNode refKNode) {
    Iterable<Point2D> _xblockexpression = null;
    {
      final EObject parentKNode = refKNode.eContainer();
      Iterable<Point2D> _xifexpression = null;
      if ((parentKNode instanceof KNode)) {
        Iterable<Point2D> _xblockexpression_1 = null;
        {
          EList<KGraphData> _data = ((KNode)parentKNode).getData();
          Iterable<KShapeLayout> _filter = Iterables.<KShapeLayout>filter(_data, KShapeLayout.class);
          final KShapeLayout pos = IterableExtensions.<KShapeLayout>head(_filter);
          final Function1<Point2D, Point2D> _function = (Point2D it) -> {
            float _xpos = pos.getXpos();
            double _x = it.getX();
            double _plus = (_xpos + _x);
            float _ypos = pos.getYpos();
            double _y = it.getY();
            double _plus_1 = (_ypos + _y);
            return new Point2D(_plus, _plus_1);
          };
          Iterable<Point2D> _map = IterableExtensions.<Point2D, Point2D>map(layoutPoints, _function);
          _xblockexpression_1 = this.layoutPointsInRoot(_map, ((KNode)parentKNode));
        }
        _xifexpression = _xblockexpression_1;
      } else {
        _xifexpression = layoutPoints;
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  protected Point2D getDelta(final Map<Object, KGraphElement> map, final XShape xFixed, final XDiagram diagram) {
    boolean _equals = Objects.equal(xFixed, null);
    if (_equals) {
      Collection<KGraphElement> _values = map.values();
      Iterable<KNode> _filter = Iterables.<KNode>filter(_values, KNode.class);
      final Function1<KNode, BoundingBox> _function = (KNode it) -> {
        BoundingBox _xblockexpression = null;
        {
          EList<KGraphData> _data = it.getData();
          Iterable<KShapeLayout> _filter_1 = Iterables.<KShapeLayout>filter(_data, KShapeLayout.class);
          final KShapeLayout layout = IterableExtensions.<KShapeLayout>head(_filter_1);
          float _xpos = layout.getXpos();
          float _ypos = layout.getYpos();
          float _width = layout.getWidth();
          float _height = layout.getHeight();
          _xblockexpression = new BoundingBox(_xpos, _ypos, _width, _height);
        }
        return _xblockexpression;
      };
      Iterable<BoundingBox> _map = IterableExtensions.<KNode, BoundingBox>map(_filter, _function);
      final Function2<BoundingBox, BoundingBox, BoundingBox> _function_1 = (BoundingBox $0, BoundingBox $1) -> {
        return BoundsExtensions.operator_plus($0, $1);
      };
      BoundingBox _reduce = IterableExtensions.<BoundingBox>reduce(_map, _function_1);
      Point2D _center = null;
      if (_reduce!=null) {
        _center=BoundsExtensions.center(_reduce);
      }
      final Point2D newCenter = _center;
      boolean _notEquals = (!Objects.equal(newCenter, null));
      if (_notEquals) {
        final Scene scene = diagram.getScene();
        boolean _equals_1 = Objects.equal(scene, null);
        if (_equals_1) {
          return newCenter;
        } else {
          double _width = scene.getWidth();
          double _multiply = (0.5 * _width);
          double _height = scene.getHeight();
          double _multiply_1 = (0.5 * _height);
          final Point2D currentCenter = diagram.sceneToLocal(_multiply, _multiply_1);
          return Point2DExtensions.operator_minus(newCenter, currentCenter);
        }
      }
    }
    final KGraphElement kFixed = map.get(xFixed);
    boolean _matched = false;
    if (!_matched) {
      if (kFixed instanceof KNode) {
        _matched=true;
        EList<KGraphData> _data = ((KNode)kFixed).getData();
        Iterable<KShapeLayout> _filter_1 = Iterables.<KShapeLayout>filter(_data, KShapeLayout.class);
        final KShapeLayout shapeLayout = IterableExtensions.<KShapeLayout>head(_filter_1);
        float _xpos = shapeLayout.getXpos();
        Bounds _layoutBounds = xFixed.getLayoutBounds();
        double _minX = _layoutBounds.getMinX();
        double _minus = (_xpos - _minX);
        double _layoutX = xFixed.getLayoutX();
        double _minus_1 = (_minus - _layoutX);
        float _ypos = shapeLayout.getYpos();
        Bounds _layoutBounds_1 = xFixed.getLayoutBounds();
        double _minY = _layoutBounds_1.getMinY();
        double _minus_2 = (_ypos - _minY);
        double _layoutY = xFixed.getLayoutY();
        double _minus_3 = (_minus_2 - _layoutY);
        return new Point2D(_minus_1, _minus_3);
      }
    }
    if (!_matched) {
      if (kFixed instanceof KEdge) {
        _matched=true;
        EList<KGraphData> _data = ((KEdge)kFixed).getData();
        Iterable<KEdgeLayout> _filter_1 = Iterables.<KEdgeLayout>filter(_data, KEdgeLayout.class);
        final KEdgeLayout edgeLayout = IterableExtensions.<KEdgeLayout>head(_filter_1);
        KVectorChain _createVectorChain = edgeLayout.createVectorChain();
        final Function1<KVector, BoundingBox> _function_2 = (KVector it) -> {
          return new BoundingBox(it.x, it.y, 0, 0);
        };
        List<BoundingBox> _map_1 = ListExtensions.<KVector, BoundingBox>map(_createVectorChain, _function_2);
        final Function2<BoundingBox, BoundingBox, BoundingBox> _function_3 = (BoundingBox $0, BoundingBox $1) -> {
          return BoundsExtensions.operator_plus($0, $1);
        };
        BoundingBox _reduce_1 = IterableExtensions.<BoundingBox>reduce(_map_1, _function_3);
        Point2D _center_1 = null;
        if (_reduce_1!=null) {
          _center_1=BoundsExtensions.center(_reduce_1);
        }
        final Point2D edgeCenter = _center_1;
        boolean _notEquals_1 = (!Objects.equal(edgeCenter, null));
        if (_notEquals_1) {
          return edgeCenter;
        }
      }
    }
    return new Point2D(0, 0);
  }
  
  protected KNode toKRootNode(final XDiagram it, final LayoutParameters parameters, final Map<Object, KGraphElement> cache) {
    KNode _xblockexpression = null;
    {
      final KNode kRoot = this._kGraphFactory.createKNode();
      final KShapeLayout shapeLayout = this._kLayoutDataFactory.createKShapeLayout();
      KInsets _createKInsets = this._kLayoutDataFactory.createKInsets();
      shapeLayout.setInsets(_createKInsets);
      shapeLayout.<Boolean>setProperty(LayoutOptions.LAYOUT_HIERARCHY, Boolean.valueOf(true));
      boolean _useSplines = parameters.getUseSplines();
      if (_useSplines) {
        shapeLayout.<EdgeRouting>setProperty(LayoutOptions.EDGE_ROUTING, EdgeRouting.SPLINES);
      } else {
        shapeLayout.<EdgeRouting>setProperty(LayoutOptions.EDGE_ROUTING, EdgeRouting.POLYLINE);
      }
      EList<KGraphData> _data = kRoot.getData();
      _data.add(shapeLayout);
      cache.put(it, kRoot);
      ObservableList<XNode> _nodes = it.getNodes();
      final Consumer<XNode> _function = (XNode it_1) -> {
        EList<KNode> _children = kRoot.getChildren();
        KNode _kNode = this.toKNode(it_1, parameters, cache);
        _children.add(_kNode);
      };
      _nodes.forEach(_function);
      ObservableList<XConnection> _connections = it.getConnections();
      double _transformConnections = this.transformConnections(_connections, parameters, cache);
      double _max = Math.max(60.0, _transformConnections);
      ObservableList<XNode> _nodes_1 = it.getNodes();
      double _transformNestedConnections = this.transformNestedConnections(_nodes_1, parameters, cache);
      double spacing = Math.max(_max, _transformNestedConnections);
      shapeLayout.<Float>setProperty(LayoutOptions.SPACING, Float.valueOf(((float) spacing)));
      _xblockexpression = kRoot;
    }
    return _xblockexpression;
  }
  
  protected double transformConnections(final Iterable<XConnection> connections, final LayoutParameters parameters, final Map<Object, KGraphElement> cache) {
    double _xblockexpression = (double) 0;
    {
      double spacing = 0.0;
      for (final XConnection it : connections) {
        {
          this.toKEdge(it, parameters, cache);
          double minLength = 0.0;
          ObservableList<XConnectionLabel> _labels = it.getLabels();
          for (final XConnectionLabel label : _labels) {
            double _minLength = minLength;
            Bounds _boundsInLocal = label.getBoundsInLocal();
            double _width = _boundsInLocal.getWidth();
            minLength = (_minLength + _width);
          }
          ArrowHead _sourceArrowHead = it.getSourceArrowHead();
          boolean _notEquals = (!Objects.equal(_sourceArrowHead, null));
          if (_notEquals) {
            double _minLength_1 = minLength;
            ArrowHead _sourceArrowHead_1 = it.getSourceArrowHead();
            double _width_1 = _sourceArrowHead_1.getWidth();
            minLength = (_minLength_1 + _width_1);
          }
          ArrowHead _targetArrowHead = it.getTargetArrowHead();
          boolean _notEquals_1 = (!Objects.equal(_targetArrowHead, null));
          if (_notEquals_1) {
            double _minLength_2 = minLength;
            ArrowHead _targetArrowHead_1 = it.getTargetArrowHead();
            double _width_2 = _targetArrowHead_1.getWidth();
            minLength = (_minLength_2 + _width_2);
          }
          double _max = Math.max(spacing, minLength);
          spacing = _max;
        }
      }
      _xblockexpression = spacing;
    }
    return _xblockexpression;
  }
  
  protected double transformNestedConnections(final Iterable<XNode> nodes, final LayoutParameters parameters, final Map<Object, KGraphElement> cache) {
    Iterable<XDiagramContainer> _filter = Iterables.<XDiagramContainer>filter(nodes, XDiagramContainer.class);
    final Function1<XDiagramContainer, Boolean> _function = (XDiagramContainer it) -> {
      return Boolean.valueOf(it.isInnerDiagramActive());
    };
    Iterable<XDiagramContainer> _filter_1 = IterableExtensions.<XDiagramContainer>filter(_filter, _function);
    final Function1<XDiagramContainer, Double> _function_1 = (XDiagramContainer it) -> {
      XDiagram _innerDiagram = it.getInnerDiagram();
      ObservableList<XConnection> _connections = _innerDiagram.getConnections();
      double _transformConnections = this.transformConnections(_connections, parameters, cache);
      XDiagram _innerDiagram_1 = it.getInnerDiagram();
      ObservableList<XNode> _nodes = _innerDiagram_1.getNodes();
      double _transformNestedConnections = this.transformNestedConnections(_nodes, parameters, cache);
      return Double.valueOf(Math.max(_transformConnections, _transformNestedConnections));
    };
    Iterable<Double> _map = IterableExtensions.<XDiagramContainer, Double>map(_filter_1, _function_1);
    final Function2<Double, Double, Double> _function_2 = (Double $0, Double $1) -> {
      return Double.valueOf(Math.max(($0).doubleValue(), ($1).doubleValue()));
    };
    return (double) IterableExtensions.<Double, Double>fold(_map, Double.valueOf(0.0), _function_2);
  }
  
  protected KNode toKNode(final XNode xNode, final LayoutParameters parameters, final Map<Object, KGraphElement> cache) {
    KNode _xblockexpression = null;
    {
      final KNode kNode = this._kGraphFactory.createKNode();
      final KShapeLayout shapeLayout = this._kLayoutDataFactory.createKShapeLayout();
      final Dimension2D autoLayoutDimension = xNode.getAutoLayoutDimension();
      double _width = autoLayoutDimension.getWidth();
      double _height = autoLayoutDimension.getHeight();
      shapeLayout.setSize(
        ((float) _width), 
        ((float) _height));
      EList<KGraphData> _data = kNode.getData();
      _data.add(shapeLayout);
      cache.put(xNode, kNode);
      if ((xNode instanceof XDiagramContainer)) {
        boolean _isInnerDiagramActive = ((XDiagramContainer)xNode).isInnerDiagramActive();
        if (_isInnerDiagramActive) {
          KInsets _createKInsets = this._kLayoutDataFactory.createKInsets();
          final Procedure1<KInsets> _function = (KInsets it) -> {
            Insets _insets = ((XDiagramContainer)xNode).getInsets();
            double _left = 0.0;
            if (_insets!=null) {
              _left=_insets.getLeft();
            }
            it.setLeft(((float) _left));
            it.setRight(0);
            Insets _insets_1 = ((XDiagramContainer)xNode).getInsets();
            double _top = 0.0;
            if (_insets_1!=null) {
              _top=_insets_1.getTop();
            }
            it.setTop(((float) _top));
            it.setBottom(0);
          };
          KInsets _doubleArrow = ObjectExtensions.<KInsets>operator_doubleArrow(_createKInsets, _function);
          shapeLayout.setInsets(_doubleArrow);
          XDiagram _innerDiagram = ((XDiagramContainer)xNode).getInnerDiagram();
          ObservableList<XNode> _nodes = _innerDiagram.getNodes();
          final Consumer<XNode> _function_1 = (XNode it) -> {
            EList<KNode> _children = kNode.getChildren();
            KNode _kNode = this.toKNode(it, parameters, cache);
            _children.add(_kNode);
          };
          _nodes.forEach(_function_1);
        }
      }
      _xblockexpression = kNode;
    }
    return _xblockexpression;
  }
  
  protected KEdge toKEdge(final XConnection it, final LayoutParameters parameters, final Map<Object, KGraphElement> cache) {
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
  
  protected KLabel toKLabel(final XConnectionLabel it, final LayoutParameters parameters, final Map<Object, KGraphElement> cache) {
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
