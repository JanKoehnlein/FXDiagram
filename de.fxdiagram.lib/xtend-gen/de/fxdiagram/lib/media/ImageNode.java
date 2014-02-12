package de.fxdiagram.lib.media;

import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.model.DomainObjectHandle;
import de.fxdiagram.core.model.ModelElementImpl;
import javafx.beans.property.DoubleProperty;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@ModelNode({ "layoutX", "layoutY", "domainObject", "width", "height" })
@SuppressWarnings("all")
public class ImageNode extends XNode {
  public ImageNode() {
    super("Image");
  }
  
  protected Node createNode() {
    ImageView _imageView = new ImageView();
    final Procedure1<ImageView> _function = new Procedure1<ImageView>() {
      public void apply(final ImageView it) {
        it.setPreserveRatio(true);
        DoubleProperty _fitWidthProperty = it.fitWidthProperty();
        DoubleProperty _widthProperty = ImageNode.this.widthProperty();
        _fitWidthProperty.bind(_widthProperty);
        DoubleProperty _fitHeightProperty = it.fitHeightProperty();
        DoubleProperty _heightProperty = ImageNode.this.heightProperty();
        _fitHeightProperty.bind(_heightProperty);
      }
    };
    return ObjectExtensions.<ImageView>operator_doubleArrow(_imageView, _function);
  }
  
  protected ImageView getImageView() {
    Node _node = this.getNode();
    return ((ImageView) _node);
  }
  
  public void setImage(final Image image) {
    ImageView _imageView = this.getImageView();
    _imageView.setImage(image);
  }
  
  public Image getImage() {
    ImageView _imageView = this.getImageView();
    return _imageView.getImage();
  }
  
  public void populate(final ModelElementImpl modelElement) {
    modelElement.addProperty(layoutXProperty(), Double.class);
    modelElement.addProperty(layoutYProperty(), Double.class);
    modelElement.addProperty(domainObjectProperty(), DomainObjectHandle.class);
    modelElement.addProperty(widthProperty(), Double.class);
    modelElement.addProperty(heightProperty(), Double.class);
  }
}
