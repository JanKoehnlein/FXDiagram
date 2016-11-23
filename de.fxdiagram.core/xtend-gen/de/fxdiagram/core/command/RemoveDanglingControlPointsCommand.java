package de.fxdiagram.core.command;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XConnectionLabel;
import de.fxdiagram.core.XControlPoint;
import de.fxdiagram.core.command.AbstractCommand;
import de.fxdiagram.core.command.CommandContext;
import de.fxdiagram.core.extensions.ConnectionExtensions;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import org.eclipse.xtend.lib.annotations.FinalFieldsConstructor;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.Pair;

@FinalFieldsConstructor
@SuppressWarnings("all")
public class RemoveDanglingControlPointsCommand extends AbstractCommand {
  private final XConnection connection;
  
  private List<XControlPoint> controlPointsCopy;
  
  @Override
  public void execute(final CommandContext context) {
    ArrayList<XControlPoint> _newArrayList = CollectionLiterals.<XControlPoint>newArrayList();
    this.controlPointsCopy = _newArrayList;
    ObservableList<XControlPoint> _controlPoints = this.connection.getControlPoints();
    Iterables.<XControlPoint>addAll(this.controlPointsCopy, _controlPoints);
    ObservableList<XConnectionLabel> _labels = this.connection.getLabels();
    final Function1<XConnectionLabel, Pair<XConnectionLabel, Point2D>> _function = (XConnectionLabel it) -> {
      double _position = it.getPosition();
      Point2D _at = this.connection.at(_position);
      return Pair.<XConnectionLabel, Point2D>of(it, _at);
    };
    List<Pair<XConnectionLabel, Point2D>> _map = ListExtensions.<XConnectionLabel, Pair<XConnectionLabel, Point2D>>map(_labels, _function);
    final HashMap<XConnectionLabel, Point2D> label2position = CollectionLiterals.<XConnectionLabel, Point2D>newHashMap(((Pair<? extends XConnectionLabel, ? extends Point2D>[])Conversions.unwrapArray(_map, Pair.class)));
    ObservableList<XControlPoint> _controlPoints_1 = this.connection.getControlPoints();
    final Function1<XControlPoint, Boolean> _function_1 = (XControlPoint it) -> {
      XControlPoint.Type _type = it.getType();
      return Boolean.valueOf((!Objects.equal(_type, XControlPoint.Type.DANGLING)));
    };
    Iterable<XControlPoint> _filter = IterableExtensions.<XControlPoint>filter(this.controlPointsCopy, _function_1);
    _controlPoints_1.setAll(((XControlPoint[])Conversions.unwrapArray(_filter, XControlPoint.class)));
    ObservableList<XConnectionLabel> _labels_1 = this.connection.getLabels();
    final Consumer<XConnectionLabel> _function_2 = (XConnectionLabel it) -> {
      Point2D _get = label2position.get(it);
      ObservableList<XControlPoint> _controlPoints_2 = this.connection.getControlPoints();
      final Function1<XControlPoint, Point2D> _function_3 = (XControlPoint it_1) -> {
        return ConnectionExtensions.toPoint2D(it_1);
      };
      List<Point2D> _map_1 = ListExtensions.<XControlPoint, Point2D>map(_controlPoints_2, _function_3);
      ConnectionExtensions.PointOnCurve _nearestPointOnConnection = ConnectionExtensions.getNearestPointOnConnection(_get, _map_1, XConnection.Kind.POLYLINE);
      double _parameter = _nearestPointOnConnection.getParameter();
      it.setPosition(_parameter);
    };
    _labels_1.forEach(_function_2);
  }
  
  @Override
  public void undo(final CommandContext context) {
    ObservableList<XConnectionLabel> _labels = this.connection.getLabels();
    final Function1<XConnectionLabel, Pair<XConnectionLabel, Point2D>> _function = (XConnectionLabel it) -> {
      double _position = it.getPosition();
      Point2D _at = this.connection.at(_position);
      return Pair.<XConnectionLabel, Point2D>of(it, _at);
    };
    List<Pair<XConnectionLabel, Point2D>> _map = ListExtensions.<XConnectionLabel, Pair<XConnectionLabel, Point2D>>map(_labels, _function);
    final HashMap<XConnectionLabel, Point2D> label2position = CollectionLiterals.<XConnectionLabel, Point2D>newHashMap(((Pair<? extends XConnectionLabel, ? extends Point2D>[])Conversions.unwrapArray(_map, Pair.class)));
    ObservableList<XControlPoint> _controlPoints = this.connection.getControlPoints();
    _controlPoints.setAll(this.controlPointsCopy);
    ObservableList<XConnectionLabel> _labels_1 = this.connection.getLabels();
    final Consumer<XConnectionLabel> _function_1 = (XConnectionLabel it) -> {
      Point2D _get = label2position.get(it);
      ObservableList<XControlPoint> _controlPoints_1 = this.connection.getControlPoints();
      final Function1<XControlPoint, Point2D> _function_2 = (XControlPoint it_1) -> {
        return ConnectionExtensions.toPoint2D(it_1);
      };
      List<Point2D> _map_1 = ListExtensions.<XControlPoint, Point2D>map(_controlPoints_1, _function_2);
      ConnectionExtensions.PointOnCurve _nearestPointOnConnection = ConnectionExtensions.getNearestPointOnConnection(_get, _map_1, XConnection.Kind.POLYLINE);
      double _parameter = _nearestPointOnConnection.getParameter();
      it.setPosition(_parameter);
    };
    _labels_1.forEach(_function_1);
  }
  
  @Override
  public void redo(final CommandContext context) {
    ObservableList<XConnectionLabel> _labels = this.connection.getLabels();
    final Function1<XConnectionLabel, Pair<XConnectionLabel, Point2D>> _function = (XConnectionLabel it) -> {
      double _position = it.getPosition();
      Point2D _at = this.connection.at(_position);
      return Pair.<XConnectionLabel, Point2D>of(it, _at);
    };
    List<Pair<XConnectionLabel, Point2D>> _map = ListExtensions.<XConnectionLabel, Pair<XConnectionLabel, Point2D>>map(_labels, _function);
    final HashMap<XConnectionLabel, Point2D> label2position = CollectionLiterals.<XConnectionLabel, Point2D>newHashMap(((Pair<? extends XConnectionLabel, ? extends Point2D>[])Conversions.unwrapArray(_map, Pair.class)));
    ObservableList<XControlPoint> _controlPoints = this.connection.getControlPoints();
    final Function1<XControlPoint, Boolean> _function_1 = (XControlPoint it) -> {
      XControlPoint.Type _type = it.getType();
      return Boolean.valueOf((!Objects.equal(_type, XControlPoint.Type.DANGLING)));
    };
    Iterable<XControlPoint> _filter = IterableExtensions.<XControlPoint>filter(this.controlPointsCopy, _function_1);
    _controlPoints.setAll(((XControlPoint[])Conversions.unwrapArray(_filter, XControlPoint.class)));
    ObservableList<XConnectionLabel> _labels_1 = this.connection.getLabels();
    final Consumer<XConnectionLabel> _function_2 = (XConnectionLabel it) -> {
      Point2D _get = label2position.get(it);
      ObservableList<XControlPoint> _controlPoints_1 = this.connection.getControlPoints();
      final Function1<XControlPoint, Point2D> _function_3 = (XControlPoint it_1) -> {
        return ConnectionExtensions.toPoint2D(it_1);
      };
      List<Point2D> _map_1 = ListExtensions.<XControlPoint, Point2D>map(_controlPoints_1, _function_3);
      ConnectionExtensions.PointOnCurve _nearestPointOnConnection = ConnectionExtensions.getNearestPointOnConnection(_get, _map_1, XConnection.Kind.POLYLINE);
      double _parameter = _nearestPointOnConnection.getParameter();
      it.setPosition(_parameter);
    };
    _labels_1.forEach(_function_2);
  }
  
  public RemoveDanglingControlPointsCommand(final XConnection connection) {
    super();
    this.connection = connection;
  }
}
