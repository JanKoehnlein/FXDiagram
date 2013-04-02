package de.itemis.javafx.diagram;

import de.itemis.javafx.diagram.XActivatable;
import de.itemis.javafx.diagram.XConnection;
import de.itemis.javafx.diagram.transform.TransformExtensions;
import java.util.List;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.scene.control.Label;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;
import org.eclipse.xtext.xbase.lib.DoubleExtensions;

@SuppressWarnings("all")
public class XConnectionLabel extends Label implements XActivatable {
  private XConnection connection;
  
  private boolean isActive;
  
  public XConnectionLabel(final XConnection connection) {
    this.connection = connection;
    connection.setLabel(this);
  }
  
  public void activate() {
    boolean _not = (!this.isActive);
    if (_not) {
      this.doActivate();
    }
    this.isActive = true;
  }
  
  public void doActivate() {
    ObservableList<Double> _points = this.connection.getPoints();
    this.place(_points);
    ObservableList<Double> _points_1 = this.connection.getPoints();
    final ListChangeListener<Double> _function = new ListChangeListener<Double>() {
        public void onChanged(final Change<? extends Double> it) {
          ObservableList<? extends Double> _list = it.getList();
          XConnectionLabel.this.place(_list);
        }
      };
    _points_1.addListener(_function);
    ReadOnlyObjectProperty<Bounds> _boundsInLocalProperty = this.boundsInLocalProperty();
    final ChangeListener<Bounds> _function_1 = new ChangeListener<Bounds>() {
        public void changed(final ObservableValue<? extends Bounds> element, final Bounds oldVlaue, final Bounds newValue) {
          ObservableList<Double> _points = XConnectionLabel.this.connection.getPoints();
          XConnectionLabel.this.place(_points);
        }
      };
    _boundsInLocalProperty.addListener(_function_1);
  }
  
  protected Boolean place(final List<? extends Double> list) {
    Boolean _xifexpression = null;
    int _size = list.size();
    boolean _greaterEqualsThan = (_size >= 4);
    if (_greaterEqualsThan) {
      boolean _xblockexpression = false;
      {
        Affine _affine = new Affine();
        final Affine transform = _affine;
        Bounds _boundsInLocal = this.getBoundsInLocal();
        double _width = _boundsInLocal.getWidth();
        double _minus = (-_width);
        double _divide = (_minus / 2);
        Bounds _boundsInLocal_1 = this.getBoundsInLocal();
        double _height = _boundsInLocal_1.getHeight();
        double _minus_1 = (-_height);
        double _minus_2 = (_minus_1 - 2);
        TransformExtensions.translate(transform, _divide, _minus_2);
        int _size_1 = list.size();
        int _minus_3 = (_size_1 - 2);
        Double _get = list.get(_minus_3);
        Double _get_1 = list.get(0);
        final double dx = DoubleExtensions.operator_minus(_get, _get_1);
        int _size_2 = list.size();
        int _minus_4 = (_size_2 - 1);
        Double _get_2 = list.get(_minus_4);
        Double _get_3 = list.get(1);
        final double dy = DoubleExtensions.operator_minus(_get_2, _get_3);
        final double angle = Math.atan2(dy, dx);
        double _multiply = (angle * 180);
        double _divide_1 = (_multiply / Math.PI);
        TransformExtensions.rotate(transform, _divide_1);
        Double _get_4 = list.get(0);
        int _size_3 = list.size();
        int _minus_5 = (_size_3 - 2);
        Double _get_5 = list.get(_minus_5);
        double _plus = DoubleExtensions.operator_plus(_get_4, _get_5);
        final double xPos = (_plus / 2);
        Double _get_6 = list.get(1);
        int _size_4 = list.size();
        int _minus_6 = (_size_4 - 1);
        Double _get_7 = list.get(_minus_6);
        double _plus_1 = DoubleExtensions.operator_plus(_get_6, _get_7);
        final double yPos = (_plus_1 / 2);
        TransformExtensions.translate(transform, xPos, yPos);
        ObservableList<Transform> _transforms = this.getTransforms();
        _transforms.clear();
        ObservableList<Transform> _transforms_1 = this.getTransforms();
        boolean _add = _transforms_1.add(transform);
        _xblockexpression = (_add);
      }
      _xifexpression = Boolean.valueOf(_xblockexpression);
    }
    return _xifexpression;
  }
}
