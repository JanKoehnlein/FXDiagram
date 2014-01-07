package de.fxdiagram.core.auxlines;

import com.google.common.collect.Iterables;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.auxlines.AuxiliaryLine;
import de.fxdiagram.core.auxlines.AuxiliaryLineMap;
import de.fxdiagram.core.auxlines.NodeLine;
import de.fxdiagram.core.extensions.CoreExtensions;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Orientation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class AuxiliaryLinesCache {
  private ListChangeListener<XNode> nodesListener;
  
  private Map<XNode,ChangeListener<Number>> node2scalarListener = CollectionLiterals.<XNode, ChangeListener<Number>>newHashMap();
  
  private Map<XNode,ChangeListener<Bounds>> node2boundsListener = CollectionLiterals.<XNode, ChangeListener<Bounds>>newHashMap();
  
  private AuxiliaryLineMap<Bounds> leftMap = new Function0<AuxiliaryLineMap<Bounds>>() {
    public AuxiliaryLineMap<Bounds> apply() {
      AuxiliaryLineMap<Bounds> _auxiliaryLineMap = new AuxiliaryLineMap<Bounds>();
      return _auxiliaryLineMap;
    }
  }.apply();
  
  private AuxiliaryLineMap<Bounds> centerXMap = new Function0<AuxiliaryLineMap<Bounds>>() {
    public AuxiliaryLineMap<Bounds> apply() {
      AuxiliaryLineMap<Bounds> _auxiliaryLineMap = new AuxiliaryLineMap<Bounds>();
      return _auxiliaryLineMap;
    }
  }.apply();
  
  private AuxiliaryLineMap<Bounds> rightMap = new Function0<AuxiliaryLineMap<Bounds>>() {
    public AuxiliaryLineMap<Bounds> apply() {
      AuxiliaryLineMap<Bounds> _auxiliaryLineMap = new AuxiliaryLineMap<Bounds>();
      return _auxiliaryLineMap;
    }
  }.apply();
  
  private AuxiliaryLineMap<Bounds> topMap = new Function0<AuxiliaryLineMap<Bounds>>() {
    public AuxiliaryLineMap<Bounds> apply() {
      AuxiliaryLineMap<Bounds> _auxiliaryLineMap = new AuxiliaryLineMap<Bounds>();
      return _auxiliaryLineMap;
    }
  }.apply();
  
  private AuxiliaryLineMap<Bounds> centerYMap = new Function0<AuxiliaryLineMap<Bounds>>() {
    public AuxiliaryLineMap<Bounds> apply() {
      AuxiliaryLineMap<Bounds> _auxiliaryLineMap = new AuxiliaryLineMap<Bounds>();
      return _auxiliaryLineMap;
    }
  }.apply();
  
  private AuxiliaryLineMap<Bounds> bottomMap = new Function0<AuxiliaryLineMap<Bounds>>() {
    public AuxiliaryLineMap<Bounds> apply() {
      AuxiliaryLineMap<Bounds> _auxiliaryLineMap = new AuxiliaryLineMap<Bounds>();
      return _auxiliaryLineMap;
    }
  }.apply();
  
  public AuxiliaryLinesCache(final XDiagram diagram) {
    final ListChangeListener<XNode> _function = new ListChangeListener<XNode>() {
      public void onChanged(final ListChangeListener.Change<? extends XNode> it) {
        boolean _next = it.next();
        boolean _while = _next;
        while (_while) {
          {
            boolean _wasAdded = it.wasAdded();
            if (_wasAdded) {
              List<? extends XNode> _addedSubList = it.getAddedSubList();
              final Procedure1<XNode> _function = new Procedure1<XNode>() {
                public void apply(final XNode it) {
                  AuxiliaryLinesCache.this.watchNode(it);
                }
              };
              IterableExtensions.forEach(_addedSubList, _function);
            }
            boolean _wasRemoved = it.wasRemoved();
            if (_wasRemoved) {
              List<? extends XNode> _removed = it.getRemoved();
              final Procedure1<XNode> _function_1 = new Procedure1<XNode>() {
                public void apply(final XNode it) {
                  AuxiliaryLinesCache.this.unwatchNode(it);
                }
              };
              IterableExtensions.forEach(_removed, _function_1);
            }
          }
          boolean _next_1 = it.next();
          _while = _next_1;
        }
      }
    };
    this.nodesListener = _function;
    ObservableList<XNode> _nodes = diagram.getNodes();
    _nodes.addListener(this.nodesListener);
    ObservableList<XNode> _nodes_1 = diagram.getNodes();
    final Procedure1<XNode> _function_1 = new Procedure1<XNode>() {
      public void apply(final XNode it) {
        AuxiliaryLinesCache.this.watchNode(it);
      }
    };
    IterableExtensions.<XNode>forEach(_nodes_1, _function_1);
  }
  
  public Iterable<AuxiliaryLine> getAuxiliaryLines(final XNode node) {
    Iterable<AuxiliaryLine> _xblockexpression = null;
    {
      Bounds _snapBounds = node.getSnapBounds();
      final Bounds boundsInDiagram = CoreExtensions.localToDiagram(node, _snapBounds);
      double _minX = boundsInDiagram.getMinX();
      Collection<AuxiliaryLine> _byPosition = this.leftMap.getByPosition(_minX);
      Iterable<AuxiliaryLine> _atLeastTwo = this.atLeastTwo(_byPosition);
      double _minX_1 = boundsInDiagram.getMinX();
      double _maxX = boundsInDiagram.getMaxX();
      double _plus = (_minX_1 + _maxX);
      double _multiply = (0.5 * _plus);
      Collection<AuxiliaryLine> _byPosition_1 = this.centerXMap.getByPosition(_multiply);
      Iterable<AuxiliaryLine> _atLeastTwo_1 = this.atLeastTwo(_byPosition_1);
      Iterable<AuxiliaryLine> _plus_1 = Iterables.<AuxiliaryLine>concat(_atLeastTwo, _atLeastTwo_1);
      double _maxX_1 = boundsInDiagram.getMaxX();
      Collection<AuxiliaryLine> _byPosition_2 = this.rightMap.getByPosition(_maxX_1);
      Iterable<AuxiliaryLine> _atLeastTwo_2 = this.atLeastTwo(_byPosition_2);
      Iterable<AuxiliaryLine> _plus_2 = Iterables.<AuxiliaryLine>concat(_plus_1, _atLeastTwo_2);
      double _minY = boundsInDiagram.getMinY();
      Collection<AuxiliaryLine> _byPosition_3 = this.topMap.getByPosition(_minY);
      Iterable<AuxiliaryLine> _atLeastTwo_3 = this.atLeastTwo(_byPosition_3);
      Iterable<AuxiliaryLine> _plus_3 = Iterables.<AuxiliaryLine>concat(_plus_2, _atLeastTwo_3);
      double _minY_1 = boundsInDiagram.getMinY();
      double _maxY = boundsInDiagram.getMaxY();
      double _plus_4 = (_minY_1 + _maxY);
      double _multiply_1 = (0.5 * _plus_4);
      Collection<AuxiliaryLine> _byPosition_4 = this.centerYMap.getByPosition(_multiply_1);
      Iterable<AuxiliaryLine> _atLeastTwo_4 = this.atLeastTwo(_byPosition_4);
      Iterable<AuxiliaryLine> _plus_5 = Iterables.<AuxiliaryLine>concat(_plus_3, _atLeastTwo_4);
      double _maxY_1 = boundsInDiagram.getMaxY();
      Collection<AuxiliaryLine> _byPosition_5 = this.bottomMap.getByPosition(_maxY_1);
      Iterable<AuxiliaryLine> _atLeastTwo_5 = this.atLeastTwo(_byPosition_5);
      Iterable<AuxiliaryLine> _plus_6 = Iterables.<AuxiliaryLine>concat(_plus_5, _atLeastTwo_5);
      _xblockexpression = (_plus_6);
    }
    return _xblockexpression;
  }
  
  protected Iterable<AuxiliaryLine> atLeastTwo(final Iterable<AuxiliaryLine> lines) {
    Iterable<AuxiliaryLine> _xifexpression = null;
    int _size = IterableExtensions.size(lines);
    boolean _lessThan = (_size < 2);
    if (_lessThan) {
      List<AuxiliaryLine> _emptyList = CollectionLiterals.<AuxiliaryLine>emptyList();
      _xifexpression = _emptyList;
    } else {
      _xifexpression = lines;
    }
    return _xifexpression;
  }
  
  public void watchNode(final XNode node) {
    final ChangeListener<Number> _function = new ChangeListener<Number>() {
      public void changed(final ObservableValue<? extends Number> scalar, final Number oldValue, final Number newValue) {
        AuxiliaryLinesCache.this.updateNode(node);
      }
    };
    final ChangeListener<Number> scalarListener = _function;
    final ChangeListener<Bounds> _function_1 = new ChangeListener<Bounds>() {
      public void changed(final ObservableValue<? extends Bounds> scalar, final Bounds oldValue, final Bounds newValue) {
        AuxiliaryLinesCache.this.updateNode(node);
      }
    };
    final ChangeListener<Bounds> boundsListener = _function_1;
    DoubleProperty _layoutXProperty = node.layoutXProperty();
    _layoutXProperty.addListener(scalarListener);
    DoubleProperty _layoutYProperty = node.layoutYProperty();
    _layoutYProperty.addListener(scalarListener);
    ReadOnlyObjectProperty<Bounds> _boundsInLocalProperty = node.boundsInLocalProperty();
    _boundsInLocalProperty.addListener(boundsListener);
    this.node2scalarListener.put(node, scalarListener);
    this.node2boundsListener.put(node, boundsListener);
    this.updateNode(node);
  }
  
  public void updateNode(final XNode node) {
    Bounds _snapBounds = node.getSnapBounds();
    final Bounds boundsInDiagram = CoreExtensions.localToDiagram(node, _snapBounds);
    double _minX = boundsInDiagram.getMinX();
    NodeLine _nodeLine = new NodeLine(_minX, 
      Orientation.VERTICAL, node, boundsInDiagram);
    this.leftMap.add(_nodeLine);
    double _minX_1 = boundsInDiagram.getMinX();
    double _maxX = boundsInDiagram.getMaxX();
    double _plus = (_minX_1 + _maxX);
    double _multiply = (0.5 * _plus);
    NodeLine _nodeLine_1 = new NodeLine(_multiply, 
      Orientation.VERTICAL, node, boundsInDiagram);
    this.centerXMap.add(_nodeLine_1);
    double _maxX_1 = boundsInDiagram.getMaxX();
    NodeLine _nodeLine_2 = new NodeLine(_maxX_1, 
      Orientation.VERTICAL, node, boundsInDiagram);
    this.rightMap.add(_nodeLine_2);
    double _minY = boundsInDiagram.getMinY();
    NodeLine _nodeLine_3 = new NodeLine(_minY, 
      Orientation.HORIZONTAL, node, boundsInDiagram);
    this.topMap.add(_nodeLine_3);
    double _minY_1 = boundsInDiagram.getMinY();
    double _maxY = boundsInDiagram.getMaxY();
    double _plus_1 = (_minY_1 + _maxY);
    double _multiply_1 = (0.5 * _plus_1);
    NodeLine _nodeLine_4 = new NodeLine(_multiply_1, 
      Orientation.HORIZONTAL, node, boundsInDiagram);
    this.centerYMap.add(_nodeLine_4);
    double _maxY_1 = boundsInDiagram.getMaxY();
    NodeLine _nodeLine_5 = new NodeLine(_maxY_1, 
      Orientation.HORIZONTAL, node, boundsInDiagram);
    this.bottomMap.add(_nodeLine_5);
  }
  
  public void unwatchNode(final XNode node) {
    this.leftMap.removeByNode(node);
    this.centerXMap.removeByNode(node);
    this.rightMap.removeByNode(node);
    this.topMap.removeByNode(node);
    this.centerYMap.removeByNode(node);
    this.bottomMap.removeByNode(node);
    final ChangeListener<Bounds> boundsListener = this.node2boundsListener.remove(node);
    ReadOnlyObjectProperty<Bounds> _boundsInLocalProperty = node.boundsInLocalProperty();
    _boundsInLocalProperty.removeListener(boundsListener);
    final ChangeListener<Number> scalarListener = this.node2scalarListener.remove(node);
    DoubleProperty _layoutXProperty = node.layoutXProperty();
    _layoutXProperty.removeListener(scalarListener);
    DoubleProperty _layoutYProperty = node.layoutYProperty();
    _layoutYProperty.removeListener(scalarListener);
  }
}
