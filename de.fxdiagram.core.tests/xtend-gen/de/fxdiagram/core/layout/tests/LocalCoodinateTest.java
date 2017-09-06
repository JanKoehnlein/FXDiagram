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
    Assert.assertEquals(100.0, pane.getBoundsInLocal().getWidth(), this.EPSILON);
    Assert.assertEquals(100.0, pane.getLayoutBounds().getWidth(), this.EPSILON);
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
    Assert.assertEquals(100.0, pane.getBoundsInLocal().getWidth(), this.EPSILON);
    Assert.assertEquals(50.0, pane.getBoundsInParent().getWidth(), this.EPSILON);
  }
  
  @Test
  public void layoutXIsInNotLayoutBounds() {
    final TestPane pane = new TestPane();
    pane.setLayoutX(100);
    Assert.assertEquals(0.0, pane.getBoundsInLocal().getMinX(), this.EPSILON);
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
    InputOutput.<Bounds>println(r0.getLayoutBounds());
    InputOutput.<Double>println(Double.valueOf(r0.getLayoutX()));
    InputOutput.<Double>println(Double.valueOf(r0.getLayoutY()));
    InputOutput.<Bounds>println(r1.getLayoutBounds());
    InputOutput.<Double>println(Double.valueOf(r1.getLayoutX()));
    InputOutput.<Double>println(Double.valueOf(r1.getLayoutY()));
    StackPane _stackPane = new StackPane();
    final Procedure1<StackPane> _function_2 = (StackPane it) -> {
      ObservableList<Node> _children = it.getChildren();
      _children.add(r0);
      ObservableList<Node> _children_1 = it.getChildren();
      _children_1.add(r1);
      it.layout();
    };
    final StackPane pane = ObjectExtensions.<StackPane>operator_doubleArrow(_stackPane, _function_2);
    InputOutput.<Bounds>println(r0.getLayoutBounds());
    InputOutput.<Double>println(Double.valueOf(r0.getLayoutX()));
    InputOutput.<Double>println(Double.valueOf(r0.getLayoutY()));
    InputOutput.<Bounds>println(r1.getLayoutBounds());
    InputOutput.<Double>println(Double.valueOf(r1.getLayoutX()));
    InputOutput.<Double>println(Double.valueOf(r1.getLayoutY()));
    InputOutput.<Bounds>println(pane.getLayoutBounds());
    InputOutput.<Double>println(Double.valueOf(pane.getLayoutX()));
    InputOutput.<Double>println(Double.valueOf(pane.getLayoutY()));
    InputOutput.<Bounds>println(pane.getBoundsInLocal());
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
    InputOutput.<Bounds>println(rect.getBoundsInLocal());
    InputOutput.<Bounds>println(rect.localToParent(rect.getBoundsInLocal()));
    InputOutput.<Bounds>println(rect.localToParent(rect.getBoundsInLocal()));
    InputOutput.<Bounds>println(group.localToParent(rect.localToParent(rect.getBoundsInLocal())));
    InputOutput.<Bounds>println(pane.localToParent(group.localToParent(rect.localToParent(rect.getBoundsInLocal()))));
  }
}
