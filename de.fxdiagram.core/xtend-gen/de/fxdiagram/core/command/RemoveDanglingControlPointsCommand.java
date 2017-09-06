package de.fxdiagram.core.command;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XConnectionLabel;
import de.fxdiagram.core.XControlPoint;
import de.fxdiagram.core.command.AbstractCommand;
import de.fxdiagram.core.command.CommandContext;
import de.fxdiagram.core.extensions.ConnectionExtensions;
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
    this.controlPointsCopy = CollectionLiterals.<XControlPoint>newArrayList();
    ObservableList<XControlPoint> _controlPoints = this.connection.getControlPoints();
    Iterables.<XControlPoint>addAll(this.controlPointsCopy, _controlPoints);
    final Function1<XConnectionLabel, Pair<XConnectionLabel, Point2D>> _function = (XConnectionLabel it) -> {
      Point2D _at = this.connection.at(it.getPosition());
      return Pair.<XConnectionLabel, Point2D>of(it, _at);
    };
    final HashMap<XConnectionLabel, Point2D> label2position = CollectionLiterals.<XConnectionLabel, Point2D>newHashMap(((Pair<? extends XConnectionLabel, ? extends Point2D>[])Conversions.unwrapArray(ListExtensions.<XConnectionLabel, Pair<XConnectionLabel, Point2D>>map(this.connection.getLabels(), _function), Pair.class)));
    final Function1<XControlPoint, Boolean> _function_1 = (XControlPoint it) -> {
      XControlPoint.Type _type = it.getType();
      return Boolean.valueOf((!Objects.equal(_type, XControlPoint.Type.DANGLING)));
    };
    this.connection.getControlPoints().setAll(((XControlPoint[])Conversions.unwrapArray(IterableExtensions.<XControlPoint>filter(this.controlPointsCopy, _function_1), XControlPoint.class)));
    final Consumer<XConnectionLabel> _function_2 = (XConnectionLabel it) -> {
      final Function1<XControlPoint, Point2D> _function_3 = (XControlPoint it_1) -> {
        return ConnectionExtensions.toPoint2D(it_1);
      };
      it.setPosition(ConnectionExtensions.getNearestPointOnConnection(label2position.get(it), ListExtensions.<XControlPoint, Point2D>map(this.connection.getControlPoints(), _function_3), XConnection.Kind.POLYLINE).getParameter());
    };
    this.connection.getLabels().forEach(_function_2);
  }
  
  @Override
  public void undo(final CommandContext context) {
    final Function1<XConnectionLabel, Pair<XConnectionLabel, Point2D>> _function = (XConnectionLabel it) -> {
      Point2D _at = this.connection.at(it.getPosition());
      return Pair.<XConnectionLabel, Point2D>of(it, _at);
    };
    final HashMap<XConnectionLabel, Point2D> label2position = CollectionLiterals.<XConnectionLabel, Point2D>newHashMap(((Pair<? extends XConnectionLabel, ? extends Point2D>[])Conversions.unwrapArray(ListExtensions.<XConnectionLabel, Pair<XConnectionLabel, Point2D>>map(this.connection.getLabels(), _function), Pair.class)));
    this.connection.getControlPoints().setAll(this.controlPointsCopy);
    final Consumer<XConnectionLabel> _function_1 = (XConnectionLabel it) -> {
      final Function1<XControlPoint, Point2D> _function_2 = (XControlPoint it_1) -> {
        return ConnectionExtensions.toPoint2D(it_1);
      };
      it.setPosition(ConnectionExtensions.getNearestPointOnConnection(label2position.get(it), ListExtensions.<XControlPoint, Point2D>map(this.connection.getControlPoints(), _function_2), XConnection.Kind.POLYLINE).getParameter());
    };
    this.connection.getLabels().forEach(_function_1);
  }
  
  @Override
  public void redo(final CommandContext context) {
    final Function1<XConnectionLabel, Pair<XConnectionLabel, Point2D>> _function = (XConnectionLabel it) -> {
      Point2D _at = this.connection.at(it.getPosition());
      return Pair.<XConnectionLabel, Point2D>of(it, _at);
    };
    final HashMap<XConnectionLabel, Point2D> label2position = CollectionLiterals.<XConnectionLabel, Point2D>newHashMap(((Pair<? extends XConnectionLabel, ? extends Point2D>[])Conversions.unwrapArray(ListExtensions.<XConnectionLabel, Pair<XConnectionLabel, Point2D>>map(this.connection.getLabels(), _function), Pair.class)));
    final Function1<XControlPoint, Boolean> _function_1 = (XControlPoint it) -> {
      XControlPoint.Type _type = it.getType();
      return Boolean.valueOf((!Objects.equal(_type, XControlPoint.Type.DANGLING)));
    };
    this.connection.getControlPoints().setAll(((XControlPoint[])Conversions.unwrapArray(IterableExtensions.<XControlPoint>filter(this.controlPointsCopy, _function_1), XControlPoint.class)));
    final Consumer<XConnectionLabel> _function_2 = (XConnectionLabel it) -> {
      final Function1<XControlPoint, Point2D> _function_3 = (XControlPoint it_1) -> {
        return ConnectionExtensions.toPoint2D(it_1);
      };
      it.setPosition(ConnectionExtensions.getNearestPointOnConnection(label2position.get(it), ListExtensions.<XControlPoint, Point2D>map(this.connection.getControlPoints(), _function_3), XConnection.Kind.POLYLINE).getParameter());
    };
    this.connection.getLabels().forEach(_function_2);
  }
  
  public RemoveDanglingControlPointsCommand(final XConnection connection) {
    super();
    this.connection = connection;
  }
}
