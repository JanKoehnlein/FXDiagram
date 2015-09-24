package de.fxdiagram.core.layout.tests;

import de.fxdiagram.core.layout.tests.TestPane;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.junit.Assert;
import org.junit.Test;

@SuppressWarnings("all")
public class LocalCoodinateTest {
  private final double EPSILON = 1e-9;
  
  @Test
  public void paneWidthIsLocal() {
    final TestPane pane = new TestPane();
    Bounds _boundsInLocal = pane.getBoundsInLocal();
    double _width = _boundsInLocal.getWidth();
    Assert.assertEquals(100.0, _width, this.EPSILON);
    Bounds _layoutBounds = pane.getLayoutBounds();
    double _width_1 = _layoutBounds.getWidth();
    Assert.assertEquals(100.0, _width_1, this.EPSILON);
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
    Assert.assertEquals(100.0, _width, this.EPSILON);
    Bounds _boundsInParent = pane.getBoundsInParent();
    double _width_1 = _boundsInParent.getWidth();
    Assert.assertEquals(50.0, _width_1, this.EPSILON);
  }
  
  @Test
  public void layoutXIsInNotLayoutBounds() {
    final TestPane pane = new TestPane();
    pane.setLayoutX(100);
    Bounds _boundsInLocal = pane.getBoundsInLocal();
    double _minX = _boundsInLocal.getMinX();
    Assert.assertEquals(0.0, _minX, this.EPSILON);
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
  public void stackPaneToRoot() {
    final Rectangle rect = new Rectangle((-100), (-100), 1, 1);
    final Group group = new Group();
    final StackPane pane = new StackPane();
    Group _group = new Group();
    final Procedure1<Group> _function = (Group it) -> {
      ObservableList<Node> _children = it.getChildren();
      final Procedure1<StackPane> _function_1 = (StackPane it_1) -> {
        it_1.setLayoutX(10);
        it_1.setLayoutY(10);
        ObservableList<Node> _children_1 = it_1.getChildren();
        final Procedure1<Group> _function_2 = (Group it_2) -> {
          it_2.setManaged(false);
          ObservableList<Node> _children_2 = it_2.getChildren();
          _children_2.add(rect);
        };
        Group _doubleArrow = ObjectExtensions.<Group>operator_doubleArrow(group, _function_2);
        _children_1.add(_doubleArrow);
      };
      StackPane _doubleArrow = ObjectExtensions.<StackPane>operator_doubleArrow(pane, _function_1);
      _children.add(_doubleArrow);
    };
    ObjectExtensions.<Group>operator_doubleArrow(_group, _function);
    Bounds _boundsInLocal = rect.getBoundsInLocal();
    InputOutput.<Bounds>println(_boundsInLocal);
    Bounds _boundsInLocal_1 = rect.getBoundsInLocal();
    Bounds _localToParent = rect.localToParent(_boundsInLocal_1);
    InputOutput.<Bounds>println(_localToParent);
    Bounds _boundsInLocal_2 = rect.getBoundsInLocal();
    Bounds _localToParent_1 = rect.localToParent(_boundsInLocal_2);
    InputOutput.<Bounds>println(_localToParent_1);
    Bounds _boundsInLocal_3 = rect.getBoundsInLocal();
    Bounds _localToParent_2 = rect.localToParent(_boundsInLocal_3);
    Bounds _localToParent_3 = group.localToParent(_localToParent_2);
    InputOutput.<Bounds>println(_localToParent_3);
    Bounds _boundsInLocal_4 = rect.getBoundsInLocal();
    Bounds _localToParent_4 = rect.localToParent(_boundsInLocal_4);
    Bounds _localToParent_5 = group.localToParent(_localToParent_4);
    Bounds _localToParent_6 = pane.localToParent(_localToParent_5);
    InputOutput.<Bounds>println(_localToParent_6);
  }
}
