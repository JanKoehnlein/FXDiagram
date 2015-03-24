package de.fxdiagram.lib.media;

import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.core.services.ResourceDescriptor;
import javafx.beans.property.DoubleProperty;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@ModelNode
@SuppressWarnings("all")
public class ImageNode extends XNode {
  public ImageNode() {
    InputOutput.println();
  }
  
  public ImageNode(final ResourceDescriptor imageDescriptor) {
    super(imageDescriptor);
  }
  
  @Override
  protected Node createNode() {
    ImageView _imageView = new ImageView();
    final Procedure1<ImageView> _function = (ImageView it) -> {
      it.setPreserveRatio(true);
      DomainObjectDescriptor _domainObject = this.getDomainObject();
      String _uRI = ((ResourceDescriptor) _domainObject).toURI();
      Image _image = new Image(_uRI);
      it.setImage(_image);
      DoubleProperty _fitWidthProperty = it.fitWidthProperty();
      DoubleProperty _widthProperty = this.widthProperty();
      _fitWidthProperty.bind(_widthProperty);
      DoubleProperty _fitHeightProperty = it.fitHeightProperty();
      DoubleProperty _heightProperty = this.heightProperty();
      _fitHeightProperty.bind(_heightProperty);
    };
    return ObjectExtensions.<ImageView>operator_doubleArrow(_imageView, _function);
  }
  
  public void populate(final ModelElementImpl modelElement) {
    super.populate(modelElement);
  }
}
