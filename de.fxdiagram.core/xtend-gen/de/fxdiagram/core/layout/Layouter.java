package de.fxdiagram.core.layout;

import com.google.common.collect.Iterables;
import de.cau.cs.kieler.core.alg.BasicProgressMonitor;
import de.cau.cs.kieler.core.kgraph.KEdge;
import de.cau.cs.kieler.core.kgraph.KGraphData;
import de.cau.cs.kieler.core.kgraph.KGraphElement;
import de.cau.cs.kieler.core.kgraph.KGraphFactory;
import de.cau.cs.kieler.core.kgraph.KLabel;
import de.cau.cs.kieler.core.kgraph.KNode;
import de.cau.cs.kieler.kiml.graphviz.layouter.GraphvizLayoutProvider;
import de.cau.cs.kieler.kiml.klayoutdata.KEdgeLayout;
import de.cau.cs.kieler.kiml.klayoutdata.KInsets;
import de.cau.cs.kieler.kiml.klayoutdata.KLayoutDataFactory;
import de.cau.cs.kieler.kiml.klayoutdata.KPoint;
import de.cau.cs.kieler.kiml.klayoutdata.KShapeLayout;
import de.cau.cs.kieler.kiml.options.LayoutOptions;
import de.fxdiagram.core.XAbstractDiagram;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XConnectionLabel;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.layout.LayoutTransitionFactory;
import de.fxdiagram.core.layout.LoggingTransformationService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javafx.animation.Animation;
import javafx.animation.PathTransition;
import javafx.geometry.Bounds;
import javafx.util.Duration;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
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
  
  public void layout(final XAbstractDiagram diagram, final Duration duration) {
    final HashMap<Object,KGraphElement> cache = CollectionLiterals.<Object, KGraphElement>newHashMap();
    final KNode kRoot = this.toKRootNode(diagram, cache);
    final GraphvizLayoutProvider provider = this.getLayoutProvider();
    try {
      BasicProgressMonitor _basicProgressMonitor = new BasicProgressMonitor();
      provider.doLayout(kRoot, _basicProgressMonitor);
      this.apply(cache, duration);
    } finally {
      provider.dispose();
    }
  }
  
  public GraphvizLayoutProvider getLayoutProvider() {
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
          if (xElement instanceof XNode) {
            final XNode _xNode = (XNode)xElement;
            _matched=true;
            EList<KGraphData> _data = kElement.getData();
            Iterable<KShapeLayout> _filter = Iterables.<KShapeLayout>filter(_data, KShapeLayout.class);
            final KShapeLayout shapeLayout = IterableExtensions.<KShapeLayout>head(_filter);
            float _xpos = shapeLayout.getXpos();
            float _ypos = shapeLayout.getYpos();
            PathTransition _createTransition = this._layoutTransitionFactory.createTransition(_xNode, _xpos, _ypos, duration);
            animations.add(_createTransition);
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
      List<XNode> _nodes = it.getNodes();
      final Procedure1<XNode> _function = new Procedure1<XNode>() {
          public void apply(final XNode it) {
            EList<KNode> _children = kRoot.getChildren();
            KNode _kNode = Layouter.this.toKNode(it, cache);
            _children.add(_kNode);
          }
        };
      IterableExtensions.<XNode>forEach(_nodes, _function);
      List<XConnection> _connections = it.getConnections();
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
      String _text = null;
      if (it!=null) {
        _text=it.getText();
      }
      if (_text != null) {
        _elvis = _text;
      } else {
        _elvis = ObjectExtensions.<String>operator_elvis(_text, "");
      }
      kLabel.setText(_elvis);
      final KShapeLayout shapeLayout = this._kLayoutDataFactory.createKShapeLayout();
      EList<KGraphData> _data = kLabel.getData();
      _data.add(shapeLayout);
      cache.put(it, kLabel);
      _xblockexpression = (kLabel);
    }
    return _xblockexpression;
  }
}
