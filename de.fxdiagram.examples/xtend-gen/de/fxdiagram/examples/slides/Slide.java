package de.fxdiagram.examples.slides;

import de.fxdiagram.core.XNode;
import de.fxdiagram.core.services.ImageCache;
import de.fxdiagram.examples.slides.Styles;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class Slide extends XNode {
  public Slide(final String name) {
    super(name);
    StackPane _stackPane = new StackPane();
    final Procedure1<StackPane> _function = new Procedure1<StackPane>() {
      public void apply(final StackPane it) {
        ObservableList<Node> _children = it.getChildren();
        ImageView _imageView = new ImageView();
        final Procedure1<ImageView> _function = new Procedure1<ImageView>() {
          public void apply(final ImageView it) {
            ImageCache _get = ImageCache.get();
            Image _image = _get.getImage(Slide.this, "images/jungle.jpg");
            it.setImage(_image);
          }
        };
        ImageView _doubleArrow = ObjectExtensions.<ImageView>operator_doubleArrow(_imageView, _function);
        _children.add(_doubleArrow);
      }
    };
    StackPane _doubleArrow = ObjectExtensions.<StackPane>operator_doubleArrow(_stackPane, _function);
    this.setNode(_doubleArrow);
  }
  
  public Slide(final String text, final int fontSize) {
    this(text);
    StackPane _stackPane = this.getStackPane();
    final Procedure1<StackPane> _function = new Procedure1<StackPane>() {
      public void apply(final StackPane it) {
        ObservableList<Node> _children = it.getChildren();
        Text _createText = Styles.createText(text, fontSize);
        _children.add(_createText);
      }
    };
    ObjectExtensions.<StackPane>operator_doubleArrow(_stackPane, _function);
  }
  
  public StackPane getStackPane() {
    Node _node = this.getNode();
    return ((StackPane) _node);
  }
  
  public void selectionFeedback(final boolean isSelected) {
  }
}
