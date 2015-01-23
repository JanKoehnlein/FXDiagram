package de.fxdiagram.core.layout.tests;

import de.fxdiagram.core.layout.tests.TestPane;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import junit.framework.Assert;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.junit.Test;

@SuppressWarnings("all")
public class LocalCoodinateTest {
  @Test
  public void paneWidthIsLocal() {
    final TestPane pane = new TestPane();
    Bounds _boundsInLocal = pane.getBoundsInLocal();
    double _width = _boundsInLocal.getWidth();
    Assert.assertEquals(Double.valueOf(100.0), Double.valueOf(_width));
    Bounds _layoutBounds = pane.getLayoutBounds();
    double _width_1 = _layoutBounds.getWidth();
    Assert.assertEquals(Double.valueOf(100.0), Double.valueOf(_width_1));
  }
  
  @Test
  public void scaleIsLocalToParent() {
    final TestPane pane = new TestPane();
    Group _group = new Group();
    final Procedure1<Group> _function = (Group it) -> {
      ObservableList<Node> _children = it.getChildren();
      _children.add(pane);
    };
    ObjectExtensions.<Group>operator_doubleArrow(_group, _function);
    pane.setScaleX(0.5);
    Bounds _boundsInLocal = pane.getBoundsInLocal();
    double _width = _boundsInLocal.getWidth();
    Assert.assertEquals(Double.valueOf(100.0), Double.valueOf(_width));
    Bounds _boundsInParent = pane.getBoundsInParent();
    double _width_1 = _boundsInParent.getWidth();
    Assert.assertEquals(Double.valueOf(50.0), Double.valueOf(_width_1));
  }
  
  @Test
  public void layoutXIsInNotLayoutBounds() {
    final TestPane pane = new TestPane();
    pane.setLayoutX(100);
    Bounds _boundsInLocal = pane.getBoundsInLocal();
    double _minX = _boundsInLocal.getMinX();
    Assert.assertEquals(Double.valueOf(0.0), Double.valueOf(_minX));
  }
  
  @Test
  public void stackPane() {
    Rectangle _rectangle = new Rectangle();
    final Procedure1<Rectangle> _function = (Rectangle it) -> {
      it.setWidth(5);
      it.setHeight(1);
    };
    final Rectangle r0 = ObjectExtensions.<Rectangle>operator_doubleArrow(_rectangle, _function);
    Rectangle _rectangle_1 = new Rectangle();
    final Procedure1<Rectangle> _function_1 = (Rectangle it) -> {
      it.setWidth(10);
      it.setHeight(5);
    };
    final Rectangle r1 = ObjectExtensions.<Rectangle>operator_doubleArrow(_rectangle_1, _function_1);
    Bounds _layoutBounds = r0.getLayoutBounds();
    InputOutput.<Bounds>println(_layoutBounds);
    double _layoutX = r0.getLayoutX();
    InputOutput.<Double>println(Double.valueOf(_layoutX));
    double _layoutY = r0.getLayoutY();
    InputOutput.<Double>println(Double.valueOf(_layoutY));
    Bounds _layoutBounds_1 = r1.getLayoutBounds();
    InputOutput.<Bounds>println(_layoutBounds_1);
    double _layoutX_1 = r1.getLayoutX();
    InputOutput.<Double>println(Double.valueOf(_layoutX_1));
    double _layoutY_1 = r1.getLayoutY();
    InputOutput.<Double>println(Double.valueOf(_layoutY_1));
    StackPane _stackPane = new StackPane();
    final Procedure1<StackPane> _function_2 = (StackPane it) -> {
      ObservableList<Node> _children = it.getChildren();
      _children.add(r0);
      ObservableList<Node> _children_1 = it.getChildren();
      _children_1.add(r1);
      it.layout();
    };
    final StackPane pane = ObjectExtensions.<StackPane>operator_doubleArrow(_stackPane, _function_2);
    Bounds _layoutBounds_2 = r0.getLayoutBounds();
    InputOutput.<Bounds>println(_layoutBounds_2);
    double _layoutX_2 = r0.getLayoutX();
    InputOutput.<Double>println(Double.valueOf(_layoutX_2));
    double _layoutY_2 = r0.getLayoutY();
    InputOutput.<Double>println(Double.valueOf(_layoutY_2));
    Bounds _layoutBounds_3 = r1.getLayoutBounds();
    InputOutput.<Bounds>println(_layoutBounds_3);
    double _layoutX_3 = r1.getLayoutX();
    InputOutput.<Double>println(Double.valueOf(_layoutX_3));
    double _layoutY_3 = r1.getLayoutY();
    InputOutput.<Double>println(Double.valueOf(_layoutY_3));
    Bounds _layoutBounds_4 = pane.getLayoutBounds();
    InputOutput.<Bounds>println(_layoutBounds_4);
    double _layoutX_4 = pane.getLayoutX();
    InputOutput.<Double>println(Double.valueOf(_layoutX_4));
    double _layoutY_4 = pane.getLayoutY();
    InputOutput.<Double>println(Double.valueOf(_layoutY_4));
    Bounds _boundsInLocal = pane.getBoundsInLocal();
    InputOutput.<Bounds>println(_boundsInLocal);
  }
  
  @Test
  public Object testBoundInLocal() {
    return null;
  }
}
