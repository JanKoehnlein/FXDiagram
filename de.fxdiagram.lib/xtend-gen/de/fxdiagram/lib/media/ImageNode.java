package de.fxdiagram.lib.media;

import de.fxdiagram.core.XNode;
import javafx.beans.property.DoubleProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class ImageNode extends XNode {
  private ImageView imageView;
  
  public ImageNode() {
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
    ImageView _doubleArrow = ObjectExtensions.<ImageView>operator_doubleArrow(_imageView, _function);
    ImageView _imageView_1 = this.imageView = _doubleArrow;
    this.setNode(_imageView_1);
  }
  
  public void setImage(final Image image) {
    this.imageView.setImage(image);
  }
  
  public Image getImage() {
    Image _image = this.imageView.getImage();
    return _image;
  }
}
