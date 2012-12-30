package de.itemis.javafx.diagram.behavior;

import de.itemis.javafx.diagram.Extensions;
import de.itemis.javafx.diagram.XActivatable;
import de.itemis.javafx.diagram.XNode;
import de.itemis.javafx.diagram.XRootDiagram;
import de.itemis.javafx.diagram.behavior.AbstractBehavior;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure3;

@SuppressWarnings("all")
public class LevelOfDetailBehavior extends AbstractBehavior {
  private List<Double> thresholds = new Function0<List<Double>>() {
    public List<Double> apply() {
      ArrayList<Double> _newArrayList = CollectionLiterals.<Double>newArrayList();
      return _newArrayList;
    }
  }.apply();
  
  private List<Node> children = new Function0<List<Node>>() {
    public List<Node> apply() {
      ArrayList<Node> _newArrayList = CollectionLiterals.<Node>newArrayList();
      return _newArrayList;
    }
  }.apply();
  
  public LevelOfDetailBehavior(final XNode host, final Node defaultChild) {
    super(host);
    this.children.add(defaultChild);
    if ((defaultChild instanceof XActivatable)) {
      ((XActivatable) defaultChild).activate();
    }
    defaultChild.setVisible(true);
  }
  
  public void doActivate() {
    final Procedure3<ObservableValue<? extends Bounds>,Bounds,Bounds> _function = new Procedure3<ObservableValue<? extends Bounds>,Bounds,Bounds>() {
        public void apply(final ObservableValue<? extends Bounds> bounds, final Bounds oldBounds, final Bounds newBounds) {
          XNode _host = LevelOfDetailBehavior.this.getHost();
          XNode _host_1 = LevelOfDetailBehavior.this.getHost();
          Bounds _boundsInLocal = _host_1.getBoundsInLocal();
          Bounds _localToScene = _host.localToScene(_boundsInLocal);
          double _value = LevelOfDetailBehavior.this.getValue(_localToScene);
          final int newIndex = LevelOfDetailBehavior.this.getChildIndexForValue(_value);
          final Node child = LevelOfDetailBehavior.this.children.get(newIndex);
          if ((child instanceof XActivatable)) {
            ((XActivatable) child).activate();
          }
          final Procedure1<Node> _function = new Procedure1<Node>() {
              public void apply(final Node it) {
                boolean _equals = ObjectExtensions.operator_equals(it, child);
                it.setVisible(_equals);
              }
            };
          IterableExtensions.<Node>forEach(LevelOfDetailBehavior.this.children, _function);
        }
      };
    final ChangeListener<Bounds> boundsListener = new ChangeListener<Bounds>() {
        public void changed(ObservableValue<? extends Bounds> observable,Bounds oldValue,Bounds newValue) {
          _function.apply(observable,oldValue,newValue);
        }
    };
    XNode _host = this.getHost();
    XRootDiagram _rootDiagram = Extensions.getRootDiagram(_host);
    ReadOnlyObjectProperty<Bounds> _boundsInParentProperty = _rootDiagram.boundsInParentProperty();
    _boundsInParentProperty.addListener(boundsListener);
  }
  
  public void addChildForThreshold(final double threshold, final Node child) {
    final int childIndex = this.getChildIndexForValue(threshold);
    this.thresholds.add(childIndex, Double.valueOf(threshold));
    int _plus = (childIndex + 1);
    this.children.add(_plus, child);
    child.setVisible(false);
  }
  
  protected double getValue(final Bounds bounds) {
    double _xifexpression = (double) 0;
    boolean _notEquals = ObjectExtensions.operator_notEquals(bounds, null);
    if (_notEquals) {
      double _width = bounds.getWidth();
      double _height = bounds.getHeight();
      double _multiply = (_width * _height);
      _xifexpression = _multiply;
    } else {
      _xifexpression = 0.0;
    }
    return _xifexpression;
  }
  
  protected int getChildIndexForValue(final double value) {
    int _xblockexpression = (int) 0;
    {
      int index = 0;
      for (final Double threshold : this.thresholds) {
        boolean _greaterThan = ((threshold).doubleValue() > value);
        if (_greaterThan) {
          return index;
        } else {
          int _plus = (index + 1);
          index = _plus;
        }
      }
      _xblockexpression = (index);
    }
    return Integer.valueOf(_xblockexpression);
  }
}
