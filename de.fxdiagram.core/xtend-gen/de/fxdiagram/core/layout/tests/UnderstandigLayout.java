package de.fxdiagram.core.layout.tests;

import de.fxdiagram.core.debug.Debug;
import de.fxdiagram.core.layout.tests.GroupWithFixedSize;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
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
    final Procedure1<Group> _function = new Procedure1<Group>() {
      public void apply(final Group it) {
        ObservableList<Node> _children = it.getChildren();
        Rectangle _rectangle = new Rectangle();
        final Procedure1<Rectangle> _function = new Procedure1<Rectangle>() {
          public void apply(final Rectangle it) {
            it.setWidth(1);
            it.setHeight(2);
            it.relocate((-3), (-4));
          }
        };
        Rectangle _doubleArrow = ObjectExtensions.<Rectangle>operator_doubleArrow(_rectangle, _function);
        _children.add(_doubleArrow);
      }
    };
    final Group group = ObjectExtensions.<Group>operator_doubleArrow(_group, _function);
    double _minWidth = group.minWidth((-1));
    Assert.assertEquals(1, _minWidth, UnderstandigLayout.EPS);
    double _minHeight = group.minHeight((-1));
    Assert.assertEquals(2, _minHeight, UnderstandigLayout.EPS);
    double _prefWidth = group.prefWidth((-1));
    Assert.assertEquals(1, _prefWidth, UnderstandigLayout.EPS);
    double _prefHeight = group.prefHeight((-1));
    Assert.assertEquals(2, _prefHeight, UnderstandigLayout.EPS);
    double _maxWidth = group.maxWidth((-1));
    Assert.assertEquals(1, _maxWidth, UnderstandigLayout.EPS);
    double _maxHeight = group.maxHeight((-1));
    Assert.assertEquals(2, _maxHeight, UnderstandigLayout.EPS);
    double _minWidth_1 = group.minWidth(10);
    Assert.assertEquals(1, _minWidth_1, UnderstandigLayout.EPS);
    double _minHeight_1 = group.minHeight(10);
    Assert.assertEquals(2, _minHeight_1, UnderstandigLayout.EPS);
    double _prefWidth_1 = group.prefWidth(10);
    Assert.assertEquals(1, _prefWidth_1, UnderstandigLayout.EPS);
    double _prefHeight_1 = group.prefHeight(10);
    Assert.assertEquals(2, _prefHeight_1, UnderstandigLayout.EPS);
    double _minWidth_2 = group.minWidth(10);
    Assert.assertEquals(1, _minWidth_2, UnderstandigLayout.EPS);
    double _minHeight_2 = group.minHeight(10);
    Assert.assertEquals(2, _minHeight_2, UnderstandigLayout.EPS);
  }
  
  @Test
  public void stackPaneSizes() {
    StackPane _stackPane = new StackPane();
    final Procedure1<StackPane> _function = new Procedure1<StackPane>() {
      public void apply(final StackPane it) {
        ObservableList<Node> _children = it.getChildren();
        Rectangle _rectangle = new Rectangle();
        final Procedure1<Rectangle> _function = new Procedure1<Rectangle>() {
          public void apply(final Rectangle it) {
            it.setWidth(1);
            it.setHeight(2);
            it.relocate((-3), (-4));
          }
        };
        Rectangle _doubleArrow = ObjectExtensions.<Rectangle>operator_doubleArrow(_rectangle, _function);
        _children.add(_doubleArrow);
      }
    };
    final StackPane stackPane = ObjectExtensions.<StackPane>operator_doubleArrow(_stackPane, _function);
    double _minWidth = stackPane.minWidth((-1));
    Assert.assertEquals(1, _minWidth, UnderstandigLayout.EPS);
    double _minHeight = stackPane.minHeight((-1));
    Assert.assertEquals(2, _minHeight, UnderstandigLayout.EPS);
    double _prefWidth = stackPane.prefWidth((-1));
    Assert.assertEquals(1, _prefWidth, UnderstandigLayout.EPS);
    double _prefHeight = stackPane.prefHeight((-1));
    Assert.assertEquals(2, _prefHeight, UnderstandigLayout.EPS);
    double _maxWidth = stackPane.maxWidth((-1));
    Assert.assertEquals(Double.MAX_VALUE, _maxWidth, UnderstandigLayout.EPS);
    double _maxHeight = stackPane.maxHeight((-1));
    Assert.assertEquals(Double.MAX_VALUE, _maxHeight, UnderstandigLayout.EPS);
    double _minWidth_1 = stackPane.minWidth(10);
    Assert.assertEquals(1, _minWidth_1, UnderstandigLayout.EPS);
    double _minHeight_1 = stackPane.minHeight(10);
    Assert.assertEquals(2, _minHeight_1, UnderstandigLayout.EPS);
    double _prefWidth_1 = stackPane.prefWidth(10);
    Assert.assertEquals(1, _prefWidth_1, UnderstandigLayout.EPS);
    double _prefHeight_1 = stackPane.prefHeight(10);
    Assert.assertEquals(2, _prefHeight_1, UnderstandigLayout.EPS);
    double _maxWidth_1 = stackPane.maxWidth(10);
    Assert.assertEquals(Double.MAX_VALUE, _maxWidth_1, UnderstandigLayout.EPS);
    double _maxHeight_1 = stackPane.maxHeight(10);
    Assert.assertEquals(Double.MAX_VALUE, _maxHeight_1, UnderstandigLayout.EPS);
    stackPane.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
    double _maxWidth_2 = stackPane.maxWidth((-1));
    Assert.assertEquals(1, _maxWidth_2, UnderstandigLayout.EPS);
    double _maxHeight_2 = stackPane.maxHeight((-1));
    Assert.assertEquals(2, _maxHeight_2, UnderstandigLayout.EPS);
    double _maxWidth_3 = stackPane.maxWidth(10);
    Assert.assertEquals(1, _maxWidth_3, UnderstandigLayout.EPS);
    double _maxHeight_3 = stackPane.maxHeight(10);
    Assert.assertEquals(2, _maxHeight_3, UnderstandigLayout.EPS);
  }
  
  @Test
  public void groupLayout() {
    Group _group = new Group();
    final Procedure1<Group> _function = new Procedure1<Group>() {
      public void apply(final Group it) {
        ObservableList<Node> _children = it.getChildren();
        Rectangle _rectangle = new Rectangle();
        final Procedure1<Rectangle> _function = new Procedure1<Rectangle>() {
          public void apply(final Rectangle it) {
            it.setWidth(2);
            it.setHeight(1);
          }
        };
        Rectangle _doubleArrow = ObjectExtensions.<Rectangle>operator_doubleArrow(_rectangle, _function);
        _children.add(_doubleArrow);
        ObservableList<Node> _children_1 = it.getChildren();
        Rectangle _rectangle_1 = new Rectangle();
        final Procedure1<Rectangle> _function_1 = new Procedure1<Rectangle>() {
          public void apply(final Rectangle it) {
            it.setWidth(1);
            it.setHeight(2);
            it.relocate((-3), (-4));
          }
        };
        Rectangle _doubleArrow_1 = ObjectExtensions.<Rectangle>operator_doubleArrow(_rectangle_1, _function_1);
        _children_1.add(_doubleArrow_1);
      }
    };
    final Group group = ObjectExtensions.<Group>operator_doubleArrow(_group, _function);
    ObservableList<Node> _children = group.getChildren();
    Node _get = _children.get(0);
    double _layoutX = _get.getLayoutX();
    Assert.assertEquals(0, _layoutX, UnderstandigLayout.EPS);
    ObservableList<Node> _children_1 = group.getChildren();
    Node _get_1 = _children_1.get(1);
    double _layoutX_1 = _get_1.getLayoutX();
    Assert.assertEquals((-3), _layoutX_1, UnderstandigLayout.EPS);
    double _minWidth = group.minWidth((-1));
    Assert.assertEquals(5, _minWidth, UnderstandigLayout.EPS);
    double _minHeight = group.minHeight((-1));
    Assert.assertEquals(5, _minHeight, UnderstandigLayout.EPS);
    double _prefWidth = group.prefWidth((-1));
    Assert.assertEquals(5, _prefWidth, UnderstandigLayout.EPS);
    double _prefHeight = group.prefHeight((-1));
    Assert.assertEquals(5, _prefHeight, UnderstandigLayout.EPS);
    double _maxWidth = group.maxWidth((-1));
    Assert.assertEquals(5, _maxWidth, UnderstandigLayout.EPS);
    double _maxHeight = group.maxHeight((-1));
    Assert.assertEquals(5, _maxHeight, UnderstandigLayout.EPS);
    group.layout();
    ObservableList<Node> _children_2 = group.getChildren();
    Node _get_2 = _children_2.get(0);
    double _layoutX_2 = _get_2.getLayoutX();
    Assert.assertEquals(0, _layoutX_2, UnderstandigLayout.EPS);
    ObservableList<Node> _children_3 = group.getChildren();
    Node _get_3 = _children_3.get(1);
    double _layoutX_3 = _get_3.getLayoutX();
    Assert.assertEquals((-3), _layoutX_3, UnderstandigLayout.EPS);
    double _minWidth_1 = group.minWidth((-1));
    Assert.assertEquals(5, _minWidth_1, UnderstandigLayout.EPS);
    double _minHeight_1 = group.minHeight((-1));
    Assert.assertEquals(5, _minHeight_1, UnderstandigLayout.EPS);
    double _prefWidth_1 = group.prefWidth((-1));
    Assert.assertEquals(5, _prefWidth_1, UnderstandigLayout.EPS);
    double _prefHeight_1 = group.prefHeight((-1));
    Assert.assertEquals(5, _prefHeight_1, UnderstandigLayout.EPS);
    double _maxWidth_1 = group.maxWidth((-1));
    Assert.assertEquals(5, _maxWidth_1, UnderstandigLayout.EPS);
    double _maxHeight_1 = group.maxHeight((-1));
    Assert.assertEquals(5, _maxHeight_1, UnderstandigLayout.EPS);
  }
  
  @Test
  public void stackPaneLayout() {
    StackPane _stackPane = new StackPane();
    final Procedure1<StackPane> _function = new Procedure1<StackPane>() {
      public void apply(final StackPane it) {
        ObservableList<Node> _children = it.getChildren();
        Rectangle _rectangle = new Rectangle();
        final Procedure1<Rectangle> _function = new Procedure1<Rectangle>() {
          public void apply(final Rectangle it) {
            it.setWidth(2);
            it.setHeight(1);
          }
        };
        Rectangle _doubleArrow = ObjectExtensions.<Rectangle>operator_doubleArrow(_rectangle, _function);
        _children.add(_doubleArrow);
        ObservableList<Node> _children_1 = it.getChildren();
        Rectangle _rectangle_1 = new Rectangle();
        final Procedure1<Rectangle> _function_1 = new Procedure1<Rectangle>() {
          public void apply(final Rectangle it) {
            it.setWidth(1);
            it.setHeight(2);
            it.relocate((-3), (-4));
          }
        };
        Rectangle _doubleArrow_1 = ObjectExtensions.<Rectangle>operator_doubleArrow(_rectangle_1, _function_1);
        _children_1.add(_doubleArrow_1);
      }
    };
    final StackPane stackPane = ObjectExtensions.<StackPane>operator_doubleArrow(_stackPane, _function);
    ObservableList<Node> _children = stackPane.getChildren();
    Node _get = _children.get(0);
    double _layoutX = _get.getLayoutX();
    Assert.assertEquals(0, _layoutX, UnderstandigLayout.EPS);
    ObservableList<Node> _children_1 = stackPane.getChildren();
    Node _get_1 = _children_1.get(1);
    double _layoutX_1 = _get_1.getLayoutX();
    Assert.assertEquals((-3), _layoutX_1, UnderstandigLayout.EPS);
    double _minWidth = stackPane.minWidth((-1));
    Assert.assertEquals(2, _minWidth, UnderstandigLayout.EPS);
    double _minHeight = stackPane.minHeight((-1));
    Assert.assertEquals(2, _minHeight, UnderstandigLayout.EPS);
    double _prefWidth = stackPane.prefWidth((-1));
    Assert.assertEquals(2, _prefWidth, UnderstandigLayout.EPS);
    double _prefHeight = stackPane.prefHeight((-1));
    Assert.assertEquals(2, _prefHeight, UnderstandigLayout.EPS);
    double _maxWidth = stackPane.maxWidth((-1));
    Assert.assertEquals(Double.MAX_VALUE, _maxWidth, UnderstandigLayout.EPS);
    double _maxHeight = stackPane.maxHeight((-1));
    Assert.assertEquals(Double.MAX_VALUE, _maxHeight, UnderstandigLayout.EPS);
    stackPane.layout();
    ObservableList<Node> _children_2 = stackPane.getChildren();
    Node _get_2 = _children_2.get(0);
    double _layoutX_2 = _get_2.getLayoutX();
    Assert.assertEquals((-1), _layoutX_2, UnderstandigLayout.EPS);
    ObservableList<Node> _children_3 = stackPane.getChildren();
    Node _get_3 = _children_3.get(1);
    double _layoutX_3 = _get_3.getLayoutX();
    Assert.assertEquals((-0.5), _layoutX_3, UnderstandigLayout.EPS);
    double _minWidth_1 = stackPane.minWidth((-1));
    Assert.assertEquals(2, _minWidth_1, UnderstandigLayout.EPS);
    double _minHeight_1 = stackPane.minHeight((-1));
    Assert.assertEquals(2, _minHeight_1, UnderstandigLayout.EPS);
    double _prefWidth_1 = stackPane.prefWidth((-1));
    Assert.assertEquals(2, _prefWidth_1, UnderstandigLayout.EPS);
    double _prefHeight_1 = stackPane.prefHeight((-1));
    Assert.assertEquals(2, _prefHeight_1, UnderstandigLayout.EPS);
    double _maxWidth_1 = stackPane.maxWidth((-1));
    Assert.assertEquals(Double.MAX_VALUE, _maxWidth_1, UnderstandigLayout.EPS);
    double _maxHeight_1 = stackPane.maxHeight((-1));
    Assert.assertEquals(Double.MAX_VALUE, _maxHeight_1, UnderstandigLayout.EPS);
  }
  
  @Test
  public void groupLyingAboutItsSizes() {
    StackPane _stackPane = new StackPane();
    final Procedure1<StackPane> _function = new Procedure1<StackPane>() {
      public void apply(final StackPane it) {
        ObservableList<Node> _children = it.getChildren();
        GroupWithFixedSize _groupWithFixedSize = new GroupWithFixedSize();
        final Procedure1<GroupWithFixedSize> _function = new Procedure1<GroupWithFixedSize>() {
          public void apply(final GroupWithFixedSize it) {
            ObservableList<Node> _children = it.getChildren();
            Rectangle _rectangle = new Rectangle();
            final Procedure1<Rectangle> _function = new Procedure1<Rectangle>() {
              public void apply(final Rectangle it) {
                it.setWidth(3);
                it.setHeight(3);
                it.relocate((-1), (-1));
              }
            };
            Rectangle _doubleArrow = ObjectExtensions.<Rectangle>operator_doubleArrow(_rectangle, _function);
            _children.add(_doubleArrow);
          }
        };
        GroupWithFixedSize _doubleArrow = ObjectExtensions.<GroupWithFixedSize>operator_doubleArrow(_groupWithFixedSize, _function);
        _children.add(_doubleArrow);
      }
    };
    final StackPane stackPane = ObjectExtensions.<StackPane>operator_doubleArrow(_stackPane, _function);
    double _minWidth = stackPane.minWidth((-1));
    Assert.assertEquals(100, _minWidth, UnderstandigLayout.EPS);
    double _minHeight = stackPane.minHeight((-1));
    Assert.assertEquals(100, _minHeight, UnderstandigLayout.EPS);
    double _prefWidth = stackPane.prefWidth((-1));
    Assert.assertEquals(100, _prefWidth, UnderstandigLayout.EPS);
    double _prefHeight = stackPane.prefHeight((-1));
    Assert.assertEquals(100, _prefHeight, UnderstandigLayout.EPS);
    double _maxWidth = stackPane.maxWidth((-1));
    Assert.assertEquals(Double.MAX_VALUE, _maxWidth, UnderstandigLayout.EPS);
    double _maxHeight = stackPane.maxHeight((-1));
    Assert.assertEquals(Double.MAX_VALUE, _maxHeight, UnderstandigLayout.EPS);
    stackPane.layout();
    double _minWidth_1 = stackPane.minWidth((-1));
    Assert.assertEquals(100, _minWidth_1, UnderstandigLayout.EPS);
    double _minHeight_1 = stackPane.minHeight((-1));
    Assert.assertEquals(100, _minHeight_1, UnderstandigLayout.EPS);
    double _prefWidth_1 = stackPane.prefWidth((-1));
    Assert.assertEquals(100, _prefWidth_1, UnderstandigLayout.EPS);
    double _prefHeight_1 = stackPane.prefHeight((-1));
    Assert.assertEquals(100, _prefHeight_1, UnderstandigLayout.EPS);
    double _maxWidth_1 = stackPane.maxWidth((-1));
    Assert.assertEquals(Double.MAX_VALUE, _maxWidth_1, UnderstandigLayout.EPS);
    double _maxHeight_1 = stackPane.maxHeight((-1));
    Assert.assertEquals(Double.MAX_VALUE, _maxHeight_1, UnderstandigLayout.EPS);
  }
  
  @Test
  public void testRelocateMovesFiguresIntoPositive() {
    Group _group = new Group();
    final Procedure1<Group> _function = new Procedure1<Group>() {
      public void apply(final Group it) {
        ObservableList<Node> _children = it.getChildren();
        Circle _circle = new Circle();
        final Procedure1<Circle> _function = new Procedure1<Circle>() {
          public void apply(final Circle it) {
            it.setRadius(2);
          }
        };
        Circle _doubleArrow = ObjectExtensions.<Circle>operator_doubleArrow(_circle, _function);
        _children.add(_doubleArrow);
      }
    };
    final Group group = ObjectExtensions.<Group>operator_doubleArrow(_group, _function);
    double _layoutX = group.getLayoutX();
    Assert.assertEquals(0, _layoutX, UnderstandigLayout.EPS);
    double _layoutY = group.getLayoutY();
    Assert.assertEquals(0, _layoutY, UnderstandigLayout.EPS);
    Bounds _layoutBounds = group.getLayoutBounds();
    double _minX = _layoutBounds.getMinX();
    Assert.assertEquals((-2), _minX, UnderstandigLayout.EPS);
    Bounds _layoutBounds_1 = group.getLayoutBounds();
    double _minY = _layoutBounds_1.getMinY();
    Assert.assertEquals((-2), _minY, UnderstandigLayout.EPS);
    group.relocate(2, 2);
    double _layoutX_1 = group.getLayoutX();
    Assert.assertEquals(4, _layoutX_1, UnderstandigLayout.EPS);
    double _layoutY_1 = group.getLayoutY();
    Assert.assertEquals(4, _layoutY_1, UnderstandigLayout.EPS);
    Bounds _layoutBounds_2 = group.getLayoutBounds();
    double _minX_1 = _layoutBounds_2.getMinX();
    Assert.assertEquals((-2), _minX_1, UnderstandigLayout.EPS);
    Bounds _layoutBounds_3 = group.getLayoutBounds();
    double _minY_1 = _layoutBounds_3.getMinY();
    Assert.assertEquals((-2), _minY_1, UnderstandigLayout.EPS);
  }
  
  public static void main(final String... args) {
    Application.launch();
  }
  
  public void start(final Stage stage) throws Exception {
    Rectangle _rectangle = new Rectangle();
    final Rectangle innerRectangle = _rectangle;
    StackPane _stackPane = new StackPane();
    final Procedure1<StackPane> _function = new Procedure1<StackPane>() {
      public void apply(final StackPane it) {
        ObservableList<Node> _children = it.getChildren();
        GroupWithFixedSize _groupWithFixedSize = new GroupWithFixedSize();
        final Procedure1<GroupWithFixedSize> _function = new Procedure1<GroupWithFixedSize>() {
          public void apply(final GroupWithFixedSize it) {
            ObservableList<Node> _children = it.getChildren();
            final Procedure1<Rectangle> _function = new Procedure1<Rectangle>() {
              public void apply(final Rectangle it) {
                it.setWidth(300);
                it.setHeight(300);
              }
            };
            Rectangle _doubleArrow = ObjectExtensions.<Rectangle>operator_doubleArrow(innerRectangle, _function);
            _children.add(_doubleArrow);
          }
        };
        GroupWithFixedSize _doubleArrow = ObjectExtensions.<GroupWithFixedSize>operator_doubleArrow(_groupWithFixedSize, _function);
        _children.add(_doubleArrow);
        ObservableList<Node> _children_1 = it.getChildren();
        Rectangle _rectangle = new Rectangle();
        final Procedure1<Rectangle> _function_1 = new Procedure1<Rectangle>() {
          public void apply(final Rectangle it) {
            it.setWidth(100);
            it.setHeight(100);
            it.relocate(250, 250);
            it.setFill(Color.GREEN);
          }
        };
        Rectangle _doubleArrow_1 = ObjectExtensions.<Rectangle>operator_doubleArrow(_rectangle, _function_1);
        _children_1.add(_doubleArrow_1);
      }
    };
    final StackPane stackPane = ObjectExtensions.<StackPane>operator_doubleArrow(_stackPane, _function);
    Scene _scene = new Scene(stackPane, 640, 480);
    stage.setScene(_scene);
    stage.show();
    Debug.dumpLayout(stackPane);
    ObservableList<Node> _children = stackPane.getChildren();
    Node _head = IterableExtensions.<Node>head(_children);
    Debug.dumpLayout(_head);
    ObservableList<Node> _children_1 = stackPane.getChildren();
    Node _last = IterableExtensions.<Node>last(_children_1);
    Debug.dumpLayout(_last);
    Debug.dumpBounds(innerRectangle);
  }
}
