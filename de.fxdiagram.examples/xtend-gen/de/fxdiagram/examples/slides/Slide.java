package de.fxdiagram.examples.slides;

import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.tools.actions.ZoomToFitAction;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class Slide extends XNode {
  public Slide(final String name, final Image backgroundImage) {
    super(name);
    StackPane _stackPane = new StackPane();
    final Procedure1<StackPane> _function = new Procedure1<StackPane>() {
      public void apply(final StackPane it) {
        ObservableList<Node> _children = it.getChildren();
        ImageView _imageView = new ImageView();
        final Procedure1<ImageView> _function = new Procedure1<ImageView>() {
          public void apply(final ImageView it) {
            it.setImage(backgroundImage);
            ColorAdjust _colorAdjust = new ColorAdjust();
            final Procedure1<ColorAdjust> _function = new Procedure1<ColorAdjust>() {
              public void apply(final ColorAdjust it) {
                it.setBrightness((-0.5));
                it.setSaturation(0);
                it.setContrast((-0.1));
              }
            };
            ColorAdjust _doubleArrow = ObjectExtensions.<ColorAdjust>operator_doubleArrow(_colorAdjust, _function);
            it.setEffect(_doubleArrow);
          }
        };
        ImageView _doubleArrow = ObjectExtensions.<ImageView>operator_doubleArrow(_imageView, _function);
        _children.add(_doubleArrow);
      }
    };
    StackPane _doubleArrow = ObjectExtensions.<StackPane>operator_doubleArrow(_stackPane, _function);
    this.setNode(_doubleArrow);
  }
  
  public void doActivate() {
    super.doActivate();
    ZoomToFitAction _zoomToFitAction = new ZoomToFitAction();
    XRoot _root = CoreExtensions.getRoot(this);
    _zoomToFitAction.perform(_root);
  }
  
  public StackPane getStackPane() {
    Node _node = this.getNode();
    return ((StackPane) _node);
  }
  
  public void selectionFeedback(final boolean isSelected) {
  }
}
