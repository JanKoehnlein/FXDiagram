package de.fxdiagram.core.layout.tests;

import de.fxdiagram.core.debug.Debug;
import de.fxdiagram.core.layout.tests.GroupWithFixedSize;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.junit.Assert;
import org.junit.Test;

@SuppressWarnings("all")
public class UnderstandigLayout extends Application {
  private final static double EPS = 1e-6;
  
  @Test
  public void groupSizes() {
    Group _group = new Group();
    final Procedure1<Group> _function = (Group it) -> {
      ObservableList<Node> _children = it.getChildren();
      Rectangle _rectangle = new Rectangle();
      final Procedure1<Rectangle> _function_1 = (Rectangle it_1) -> {
        it_1.setWidth(1);
        it_1.setHeight(2);
        it_1.relocate((-3), (-4));
      };
      Rectangle _doubleArrow = ObjectExtensions.<Rectangle>operator_doubleArrow(_rectangle, _function_1);
      _children.add(_doubleArrow);
    };
    final Group group = ObjectExtensions.<Group>operator_doubleArrow(_group, _function);
    Assert.assertEquals(1, group.minWidth((-1)), UnderstandigLayout.EPS);
    Assert.assertEquals(2, group.minHeight((-1)), UnderstandigLayout.EPS);
    Assert.assertEquals(1, group.prefWidth((-1)), UnderstandigLayout.EPS);
    Assert.assertEquals(2, group.prefHeight((-1)), UnderstandigLayout.EPS);
    Assert.assertEquals(1, group.maxWidth((-1)), UnderstandigLayout.EPS);
    Assert.assertEquals(2, group.maxHeight((-1)), UnderstandigLayout.EPS);
    Assert.assertEquals(1, group.minWidth(10), UnderstandigLayout.EPS);
    Assert.assertEquals(2, group.minHeight(10), UnderstandigLayout.EPS);
    Assert.assertEquals(1, group.prefWidth(10), UnderstandigLayout.EPS);
    Assert.assertEquals(2, group.prefHeight(10), UnderstandigLayout.EPS);
    Assert.assertEquals(1, group.minWidth(10), UnderstandigLayout.EPS);
    Assert.assertEquals(2, group.minHeight(10), UnderstandigLayout.EPS);
  }
  
  @Test
  public void stackPaneSizes() {
    StackPane _stackPane = new StackPane();
    final Procedure1<StackPane> _function = (StackPane it) -> {
      ObservableList<Node> _children = it.getChildren();
      Rectangle _rectangle = new Rectangle();
      final Procedure1<Rectangle> _function_1 = (Rectangle it_1) -> {
        it_1.setWidth(1);
        it_1.setHeight(2);
        it_1.relocate((-3), (-4));
      };
      Rectangle _doubleArrow = ObjectExtensions.<Rectangle>operator_doubleArrow(_rectangle, _function_1);
      _children.add(_doubleArrow);
    };
    final StackPane stackPane = ObjectExtensions.<StackPane>operator_doubleArrow(_stackPane, _function);
    Assert.assertEquals(1, stackPane.minWidth((-1)), UnderstandigLayout.EPS);
    Assert.assertEquals(2, stackPane.minHeight((-1)), UnderstandigLayout.EPS);
    Assert.assertEquals(1, stackPane.prefWidth((-1)), UnderstandigLayout.EPS);
    Assert.assertEquals(2, stackPane.prefHeight((-1)), UnderstandigLayout.EPS);
    Assert.assertEquals(Double.MAX_VALUE, stackPane.maxWidth((-1)), UnderstandigLayout.EPS);
    Assert.assertEquals(Double.MAX_VALUE, stackPane.maxHeight((-1)), UnderstandigLayout.EPS);
    Assert.assertEquals(1, stackPane.minWidth(10), UnderstandigLayout.EPS);
    Assert.assertEquals(2, stackPane.minHeight(10), UnderstandigLayout.EPS);
    Assert.assertEquals(1, stackPane.prefWidth(10), UnderstandigLayout.EPS);
    Assert.assertEquals(2, stackPane.prefHeight(10), UnderstandigLayout.EPS);
    Assert.assertEquals(Double.MAX_VALUE, stackPane.maxWidth(10), UnderstandigLayout.EPS);
    Assert.assertEquals(Double.MAX_VALUE, stackPane.maxHeight(10), UnderstandigLayout.EPS);
    stackPane.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
    Assert.assertEquals(1, stackPane.maxWidth((-1)), UnderstandigLayout.EPS);
    Assert.assertEquals(2, stackPane.maxHeight((-1)), UnderstandigLayout.EPS);
    Assert.assertEquals(1, stackPane.maxWidth(10), UnderstandigLayout.EPS);
    Assert.assertEquals(2, stackPane.maxHeight(10), UnderstandigLayout.EPS);
  }
  
  @Test
  public void groupLayout() {
    Group _group = new Group();
    final Procedure1<Group> _function = (Group it) -> {
      ObservableList<Node> _children = it.getChildren();
      Rectangle _rectangle = new Rectangle();
      final Procedure1<Rectangle> _function_1 = (Rectangle it_1) -> {
        it_1.setWidth(2);
        it_1.setHeight(1);
      };
      Rectangle _doubleArrow = ObjectExtensions.<Rectangle>operator_doubleArrow(_rectangle, _function_1);
      _children.add(_doubleArrow);
      ObservableList<Node> _children_1 = it.getChildren();
      Rectangle _rectangle_1 = new Rectangle();
      final Procedure1<Rectangle> _function_2 = (Rectangle it_1) -> {
        it_1.setWidth(1);
        it_1.setHeight(2);
        it_1.relocate((-3), (-4));
      };
      Rectangle _doubleArrow_1 = ObjectExtensions.<Rectangle>operator_doubleArrow(_rectangle_1, _function_2);
      _children_1.add(_doubleArrow_1);
    };
    final Group group = ObjectExtensions.<Group>operator_doubleArrow(_group, _function);
    Assert.assertEquals(0, group.getChildren().get(0).getLayoutX(), UnderstandigLayout.EPS);
    Assert.assertEquals((-3), group.getChildren().get(1).getLayoutX(), UnderstandigLayout.EPS);
    Assert.assertEquals(5, group.minWidth((-1)), UnderstandigLayout.EPS);
    Assert.assertEquals(5, group.minHeight((-1)), UnderstandigLayout.EPS);
    Assert.assertEquals(5, group.prefWidth((-1)), UnderstandigLayout.EPS);
    Assert.assertEquals(5, group.prefHeight((-1)), UnderstandigLayout.EPS);
    Assert.assertEquals(5, group.maxWidth((-1)), UnderstandigLayout.EPS);
    Assert.assertEquals(5, group.maxHeight((-1)), UnderstandigLayout.EPS);
    group.layout();
    Assert.assertEquals(0, group.getChildren().get(0).getLayoutX(), UnderstandigLayout.EPS);
    Assert.assertEquals((-3), group.getChildren().get(1).getLayoutX(), UnderstandigLayout.EPS);
    Assert.assertEquals(5, group.minWidth((-1)), UnderstandigLayout.EPS);
    Assert.assertEquals(5, group.minHeight((-1)), UnderstandigLayout.EPS);
    Assert.assertEquals(5, group.prefWidth((-1)), UnderstandigLayout.EPS);
    Assert.assertEquals(5, group.prefHeight((-1)), UnderstandigLayout.EPS);
    Assert.assertEquals(5, group.maxWidth((-1)), UnderstandigLayout.EPS);
    Assert.assertEquals(5, group.maxHeight((-1)), UnderstandigLayout.EPS);
  }
  
  @Test
  public void stackPaneLayout() {
    StackPane _stackPane = new StackPane();
    final Procedure1<StackPane> _function = (StackPane it) -> {
      ObservableList<Node> _children = it.getChildren();
      Rectangle _rectangle = new Rectangle();
      final Procedure1<Rectangle> _function_1 = (Rectangle it_1) -> {
        it_1.setWidth(2);
        it_1.setHeight(1);
      };
      Rectangle _doubleArrow = ObjectExtensions.<Rectangle>operator_doubleArrow(_rectangle, _function_1);
      _children.add(_doubleArrow);
      ObservableList<Node> _children_1 = it.getChildren();
      Rectangle _rectangle_1 = new Rectangle();
      final Procedure1<Rectangle> _function_2 = (Rectangle it_1) -> {
        it_1.setWidth(1);
        it_1.setHeight(2);
        it_1.relocate((-3), (-4));
      };
      Rectangle _doubleArrow_1 = ObjectExtensions.<Rectangle>operator_doubleArrow(_rectangle_1, _function_2);
      _children_1.add(_doubleArrow_1);
    };
    final StackPane stackPane = ObjectExtensions.<StackPane>operator_doubleArrow(_stackPane, _function);
    Assert.assertEquals(0, stackPane.getChildren().get(0).getLayoutX(), UnderstandigLayout.EPS);
    Assert.assertEquals((-3), stackPane.getChildren().get(1).getLayoutX(), UnderstandigLayout.EPS);
    Assert.assertEquals(2, stackPane.minWidth((-1)), UnderstandigLayout.EPS);
    Assert.assertEquals(2, stackPane.minHeight((-1)), UnderstandigLayout.EPS);
    Assert.assertEquals(2, stackPane.prefWidth((-1)), UnderstandigLayout.EPS);
    Assert.assertEquals(2, stackPane.prefHeight((-1)), UnderstandigLayout.EPS);
    Assert.assertEquals(Double.MAX_VALUE, stackPane.maxWidth((-1)), UnderstandigLayout.EPS);
    Assert.assertEquals(Double.MAX_VALUE, stackPane.maxHeight((-1)), UnderstandigLayout.EPS);
    stackPane.layout();
    Assert.assertEquals((-1), stackPane.getChildren().get(0).getLayoutX(), UnderstandigLayout.EPS);
    Assert.assertEquals(0, stackPane.getChildren().get(1).getLayoutX(), UnderstandigLayout.EPS);
    Assert.assertEquals(2, stackPane.minWidth((-1)), UnderstandigLayout.EPS);
    Assert.assertEquals(2, stackPane.minHeight((-1)), UnderstandigLayout.EPS);
    Assert.assertEquals(2, stackPane.prefWidth((-1)), UnderstandigLayout.EPS);
    Assert.assertEquals(2, stackPane.prefHeight((-1)), UnderstandigLayout.EPS);
    Assert.assertEquals(Double.MAX_VALUE, stackPane.maxWidth((-1)), UnderstandigLayout.EPS);
    Assert.assertEquals(Double.MAX_VALUE, stackPane.maxHeight((-1)), UnderstandigLayout.EPS);
  }
  
  @Test
  public void groupLyingAboutItsSizes() {
    StackPane _stackPane = new StackPane();
    final Procedure1<StackPane> _function = (StackPane it) -> {
      ObservableList<Node> _children = it.getChildren();
      GroupWithFixedSize _groupWithFixedSize = new GroupWithFixedSize();
      final Procedure1<GroupWithFixedSize> _function_1 = (GroupWithFixedSize it_1) -> {
        ObservableList<Node> _children_1 = it_1.getChildren();
        Rectangle _rectangle = new Rectangle();
        final Procedure1<Rectangle> _function_2 = (Rectangle it_2) -> {
          it_2.setWidth(3);
          it_2.setHeight(3);
          it_2.relocate((-1), (-1));
        };
        Rectangle _doubleArrow = ObjectExtensions.<Rectangle>operator_doubleArrow(_rectangle, _function_2);
        _children_1.add(_doubleArrow);
      };
      GroupWithFixedSize _doubleArrow = ObjectExtensions.<GroupWithFixedSize>operator_doubleArrow(_groupWithFixedSize, _function_1);
      _children.add(_doubleArrow);
    };
    final StackPane stackPane = ObjectExtensions.<StackPane>operator_doubleArrow(_stackPane, _function);
    Assert.assertEquals(100, stackPane.minWidth((-1)), UnderstandigLayout.EPS);
    Assert.assertEquals(100, stackPane.minHeight((-1)), UnderstandigLayout.EPS);
    Assert.assertEquals(100, stackPane.prefWidth((-1)), UnderstandigLayout.EPS);
    Assert.assertEquals(100, stackPane.prefHeight((-1)), UnderstandigLayout.EPS);
    Assert.assertEquals(Double.MAX_VALUE, stackPane.maxWidth((-1)), UnderstandigLayout.EPS);
    Assert.assertEquals(Double.MAX_VALUE, stackPane.maxHeight((-1)), UnderstandigLayout.EPS);
    stackPane.layout();
    Assert.assertEquals(100, stackPane.minWidth((-1)), UnderstandigLayout.EPS);
    Assert.assertEquals(100, stackPane.minHeight((-1)), UnderstandigLayout.EPS);
    Assert.assertEquals(100, stackPane.prefWidth((-1)), UnderstandigLayout.EPS);
    Assert.assertEquals(100, stackPane.prefHeight((-1)), UnderstandigLayout.EPS);
    Assert.assertEquals(Double.MAX_VALUE, stackPane.maxWidth((-1)), UnderstandigLayout.EPS);
    Assert.assertEquals(Double.MAX_VALUE, stackPane.maxHeight((-1)), UnderstandigLayout.EPS);
  }
  
  @Test
  public void testRelocateMovesFiguresIntoPositive() {
    Group _group = new Group();
    final Procedure1<Group> _function = (Group it) -> {
      ObservableList<Node> _children = it.getChildren();
      Circle _circle = new Circle();
      final Procedure1<Circle> _function_1 = (Circle it_1) -> {
        it_1.setRadius(2);
      };
      Circle _doubleArrow = ObjectExtensions.<Circle>operator_doubleArrow(_circle, _function_1);
      _children.add(_doubleArrow);
    };
    final Group group = ObjectExtensions.<Group>operator_doubleArrow(_group, _function);
    Assert.assertEquals(0, group.getLayoutX(), UnderstandigLayout.EPS);
    Assert.assertEquals(0, group.getLayoutY(), UnderstandigLayout.EPS);
    Assert.assertEquals((-2), group.getLayoutBounds().getMinX(), UnderstandigLayout.EPS);
    Assert.assertEquals((-2), group.getLayoutBounds().getMinY(), UnderstandigLayout.EPS);
    group.relocate(2, 2);
    Assert.assertEquals(4, group.getLayoutX(), UnderstandigLayout.EPS);
    Assert.assertEquals(4, group.getLayoutY(), UnderstandigLayout.EPS);
    Assert.assertEquals((-2), group.getLayoutBounds().getMinX(), UnderstandigLayout.EPS);
    Assert.assertEquals((-2), group.getLayoutBounds().getMinY(), UnderstandigLayout.EPS);
  }
  
  public static void main(final String... args) {
    Application.launch();
  }
  
  @Override
  public void start(final Stage stage) throws Exception {
    final Rectangle innerRectangle = new Rectangle();
    StackPane _stackPane = new StackPane();
    final Procedure1<StackPane> _function = (StackPane it) -> {
      ObservableList<Node> _children = it.getChildren();
      GroupWithFixedSize _groupWithFixedSize = new GroupWithFixedSize();
      final Procedure1<GroupWithFixedSize> _function_1 = (GroupWithFixedSize it_1) -> {
        ObservableList<Node> _children_1 = it_1.getChildren();
        final Procedure1<Rectangle> _function_2 = (Rectangle it_2) -> {
          it_2.setWidth(300);
          it_2.setHeight(300);
        };
        Rectangle _doubleArrow = ObjectExtensions.<Rectangle>operator_doubleArrow(innerRectangle, _function_2);
        _children_1.add(_doubleArrow);
      };
      GroupWithFixedSize _doubleArrow = ObjectExtensions.<GroupWithFixedSize>operator_doubleArrow(_groupWithFixedSize, _function_1);
      _children.add(_doubleArrow);
      ObservableList<Node> _children_1 = it.getChildren();
      Rectangle _rectangle = new Rectangle();
      final Procedure1<Rectangle> _function_2 = (Rectangle it_1) -> {
        it_1.setWidth(100);
        it_1.setHeight(100);
        it_1.relocate(250, 250);
        it_1.setFill(Color.GREEN);
      };
      Rectangle _doubleArrow_1 = ObjectExtensions.<Rectangle>operator_doubleArrow(_rectangle, _function_2);
      _children_1.add(_doubleArrow_1);
    };
    final StackPane stackPane = ObjectExtensions.<StackPane>operator_doubleArrow(_stackPane, _function);
    Scene _scene = new Scene(stackPane, 640, 480);
    stage.setScene(_scene);
    stage.show();
    Debug.dumpLayout(stackPane);
    Debug.dumpLayout(IterableExtensions.<Node>head(stackPane.getChildren()));
    Debug.dumpLayout(IterableExtensions.<Node>last(stackPane.getChildren()));
    Debug.dumpBounds(innerRectangle);
  }
}
