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
import de.fxdiagram.core.XAbstractDiagram;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XConnectionKind;
import de.fxdiagram.core.XConnectionLabel;
import de.fxdiagram.core.XControlPoint;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.anchors.ConnectionRouter;
import de.fxdiagram.core.layout.LayoutTransitionFactory;
import de.fxdiagram.core.layout.LoggingTransformationService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javafx.animation.Animation;
import javafx.animation.PathTransition;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.ExclusiveRange;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class Layouter {
  @Extension
  private KLayoutDataFactory _kLayoutDataFactory = KLayoutDataFactory.eINSTANCE;
  
  @Extension
  private KGraphFactory _kGraphFactory = KGraphFactory.eINSTANCE;
  
  @Extension
  private LayoutTransitionFactory _layoutTransitionFactory = new Function0<LayoutTransitionFactory>() {
    public LayoutTransitionFactory apply() {
      LayoutTransitionFactory _layoutTransitionFactory = new LayoutTransitionFactory();
      return _layoutTransitionFactory;
    }
  }.apply();
  
  public Layouter() {
    AbstractLayoutProvider _layoutProvider = this.getLayoutProvider();
    _layoutProvider.dispose();
  }
  
  public void layout(final XAbstractDiagram diagram, final Duration duration) {
    final HashMap<Object,KGraphElement> cache = CollectionLiterals.<Object, KGraphElement>newHashMap();
    final KNode kRoot = this.toKRootNode(diagram, cache);
    final AbstractLayoutProvider provider = this.getLayoutProvider();
    try {
      BasicProgressMonitor _basicProgressMonitor = new BasicProgressMonitor();
      provider.doLayout(kRoot, _basicProgressMonitor);
      this.apply(cache, duration);
    } finally {
      provider.dispose();
    }
  }
  
  public AbstractLayoutProvider getLayoutProvider() {
    GraphvizLayoutProvider _xblockexpression = null;
    {
      new LoggingTransformationService();
      GraphvizLayoutProvider _graphvizLayoutProvider = new GraphvizLayoutProvider();
      final Procedure1<GraphvizLayoutProvider> _function = new Procedure1<GraphvizLayoutProvider>() {
        public void apply(final GraphvizLayoutProvider it) {
          it.initialize("DOT");
        }
      };
      GraphvizLayoutProvider _doubleArrow = ObjectExtensions.<GraphvizLayoutProvider>operator_doubleArrow(_graphvizLayoutProvider, _function);
      _xblockexpression = (_doubleArrow);
    }
    return _xblockexpression;
  }
  
  public void apply(final Map<Object,KGraphElement> map, final Duration duration) {
    final ArrayList<Animation> animations = CollectionLiterals.<Animation>newArrayList();
    Set<Entry<Object,KGraphElement>> _entrySet = map.entrySet();
    for (final Entry<Object,KGraphElement> entry : _entrySet) {
      {
        final Object xElement = entry.getKey();
        final KGraphElement kElement = entry.getValue();
        boolean _matched = false;
        if (!_matched) {
          if (xElement instanceof XConnectionLabel) {
            final XConnectionLabel _xConnectionLabel = (XConnectionLabel)xElement;
            _matched=true;
            EList<KGraphData> _data = kElement.getData();
            Iterable<KShapeLayout> _filter = Iterables.<KShapeLayout>filter(_data, KShapeLayout.class);
            final KShapeLayout shapeLayout = IterableExtensions.<KShapeLayout>head(_filter);
            float _xpos = shapeLayout.getXpos();
            float _ypos = shapeLayout.getYpos();
            PathTransition _createTransition = this._layoutTransitionFactory.createTransition(_xConnectionLabel, _xpos, _ypos, 
              true, duration);
            animations.add(_createTransition);
          }
        }
        if (!_matched) {
          if (xElement instanceof XNode) {
            final XNode _xNode = (XNode)xElement;
            _matched=true;
            EList<KGraphData> _data = kElement.getData();
            Iterable<KShapeLayout> _filter = Iterables.<KShapeLayout>filter(_data, KShapeLayout.class);
            final KShapeLayout shapeLayout = IterableExtensions.<KShapeLayout>head(_filter);
            float _xpos = shapeLayout.getXpos();
            float _ypos = shapeLayout.getYpos();
            PathTransition _createTransition = this._layoutTransitionFactory.createTransition(_xNode, _xpos, _ypos, true, duration);
            animations.add(_createTransition);
          }
        }
        if (!_matched) {
          if (xElement instanceof XConnection) {
            final XConnection _xConnection = (XConnection)xElement;
            _matched=true;
            EList<KGraphData> _data = kElement.getData();
            Iterable<KEdgeLayout> _filter = Iterables.<KEdgeLayout>filter(_data, KEdgeLayout.class);
            final KEdgeLayout edgeLayout = IterableExtensions.<KEdgeLayout>head(_filter);
            final KVectorChain layoutPoints = edgeLayout.createVectorChain();
            EdgeRouting _property = edgeLayout.<EdgeRouting>getProperty(LayoutOptions.EDGE_ROUTING);
            final EdgeRouting _switchValue = _property;
            boolean _matched_1 = false;
            if (!_matched_1) {
              if (Objects.equal(_switchValue,EdgeRouting.SPLINES)) {
                _matched_1=true;
                int _size = layoutPoints.size();
                int _minus = (_size - 1);
                int _modulo = (_minus % 3);
                boolean _equals = (_modulo == 0);
                if (_equals) {
                  _xConnection.setKind(XConnectionKind.CUBIC_CURVE);
                } else {
                  int _size_1 = layoutPoints.size();
                  int _minus_1 = (_size_1 - 1);
                  int _modulo_1 = (_minus_1 % 2);
                  boolean _equals_1 = (_modulo_1 == 0);
                  if (_equals_1) {
                    _xConnection.setKind(XConnectionKind.QUAD_CURVE);
                  } else {
                    _xConnection.setKind(XConnectionKind.POLYLINE);
                  }
                }
              }
            }
            if (!_matched_1) {
              _xConnection.setKind(XConnectionKind.POLYLINE);
            }
            final ObservableList<XControlPoint> controlPoints = _xConnection.getControlPoints();
            ConnectionRouter _connectionRouter = _xConnection.getConnectionRouter();
            int _size_2 = layoutPoints.size();
            _connectionRouter.growToSize(_size_2);
            int _size_3 = controlPoints.size();
            int _minus_2 = (_size_3 - 1);
            ExclusiveRange _doubleDotLessThan = new ExclusiveRange(1, _minus_2, true);
            for (final Integer i : _doubleDotLessThan) {
              {
                int _size_4 = layoutPoints.size();
                int _minus_3 = (_size_4 - 1);
                int _min = Math.min(_minus_3, (i).intValue());
                final KVector layoutPoint = layoutPoints.get(_min);
                final XControlPoint currentControlPoint = controlPoints.get((i).intValue());
                final PathTransition transition = this._layoutTransitionFactory.createTransition(currentControlPoint, layoutPoint.x, layoutPoint.y, false, duration);
                boolean _equals_2 = ((i).intValue() == 1);
                if (_equals_2) {
                  final EventHandler<ActionEvent> unbind = transition.getOnFinished();
                  final EventHandler<ActionEvent> _function = new EventHandler<ActionEvent>() {
                    public void handle(final ActionEvent it) {
                      unbind.handle(it);
                      ConnectionRouter _connectionRouter = _xConnection.getConnectionRouter();
                      int _size = layoutPoints.size();
                      _connectionRouter.shrinkToSize(_size);
                    }
                  };
                  transition.setOnFinished(_function);
                }
                animations.add(transition);
              }
            }
          }
        }
      }
    }
    final Procedure1<Animation> _function = new Procedure1<Animation>() {
      public void apply(final Animation it) {
        it.play();
      }
    };
    IterableExtensions.<Animation>forEach(animations, _function);
  }
  
  protected KNode toKRootNode(final XAbstractDiagram it, final Map<Object,KGraphElement> cache) {
    KNode _xblockexpression = null;
    {
      final KNode kRoot = this._kGraphFactory.createKNode();
      final KShapeLayout shapeLayout = this._kLayoutDataFactory.createKShapeLayout();
      KInsets _createKInsets = this._kLayoutDataFactory.createKInsets();
      shapeLayout.setInsets(_createKInsets);
      shapeLayout.setProperty(LayoutOptions.SPACING, Float.valueOf(60f));
      EList<KGraphData> _data = kRoot.getData();
      _data.add(shapeLayout);
      cache.put(it, kRoot);
      ObservableList<XNode> _nodes = it.getNodes();
      final Procedure1<XNode> _function = new Procedure1<XNode>() {
        public void apply(final XNode it) {
          EList<KNode> _children = kRoot.getChildren();
          KNode _kNode = Layouter.this.toKNode(it, cache);
          _children.add(_kNode);
        }
      };
      IterableExtensions.<XNode>forEach(_nodes, _function);
      ObservableList<XConnection> _connections = it.getConnections();
      final Procedure1<XConnection> _function_1 = new Procedure1<XConnection>() {
        public void apply(final XConnection it) {
          Layouter.this.toKEdge(it, cache);
        }
      };
      IterableExtensions.<XConnection>forEach(_connections, _function_1);
      _xblockexpression = (kRoot);
    }
    return _xblockexpression;
  }
  
  protected KNode toKNode(final XNode it, final Map<Object,KGraphElement> cache) {
    KNode _xblockexpression = null;
    {
      final KNode kNode = this._kGraphFactory.createKNode();
      final KShapeLayout shapeLayout = this._kLayoutDataFactory.createKShapeLayout();
      Bounds _layoutBounds = it.getLayoutBounds();
      double _width = _layoutBounds.getWidth();
      Bounds _layoutBounds_1 = it.getLayoutBounds();
      double _height = _layoutBounds_1.getHeight();
      shapeLayout.setSize(((float) _width), ((float) _height));
      EList<KGraphData> _data = kNode.getData();
      _data.add(shapeLayout);
      cache.put(it, kNode);
      _xblockexpression = (kNode);
    }
    return _xblockexpression;
  }
  
  protected KEdge toKEdge(final XConnection it, final Map<Object,KGraphElement> cache) {
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
        _and = ((kSource instanceof KNode) && (kTarget instanceof KNode));
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
          XConnectionLabel _label = it.getLabel();
          boolean _notEquals = (!Objects.equal(_label, null));
          if (_notEquals) {
            XConnectionLabel _label_1 = it.getLabel();
            KLabel _kLabel = this.toKLabel(_label_1, cache);
            _kLabel.setParent(kEdge);
          }
          _xblockexpression_1 = (kEdge);
        }
        _xifexpression = _xblockexpression_1;
      } else {
        _xifexpression = null;
      }
      _xblockexpression = (_xifexpression);
    }
    return _xblockexpression;
  }
  
  protected KLabel toKLabel(final XConnectionLabel it, final Map<Object,KGraphElement> cache) {
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
        _elvis = ObjectExtensions.<String>operator_elvis(_text_1, "");
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
      shapeLayout.setProperty(LayoutOptions.FONT_SIZE, Integer.valueOf(12));
      shapeLayout.setProperty(LayoutOptions.EDGE_LABEL_PLACEMENT, EdgeLabelPlacement.CENTER);
      EList<KGraphData> _data = kLabel.getData();
      _data.add(shapeLayout);
      cache.put(it, kLabel);
      _xblockexpression = (kLabel);
    }
    return _xblockexpression;
  }
}
