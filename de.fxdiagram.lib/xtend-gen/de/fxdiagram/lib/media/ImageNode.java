package de.fxdiagram.lib.media;

import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.core.model.ToString;
import de.fxdiagram.core.services.ResourceDescriptor;
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
      DomainObjectDescriptor _domainObjectDescriptor = this.getDomainObjectDescriptor();
      String _uRI = ((ResourceDescriptor) _domainObjectDescriptor).toURI();
      Image _image = new Image(_uRI);
      it.setImage(_image);
      it.fitWidthProperty().bind(this.widthProperty());
      it.fitHeightProperty().bind(this.heightProperty());
    };
    return ObjectExtensions.<ImageView>operator_doubleArrow(_imageView, _function);
  }
  
  public void populate(final ModelElementImpl modelElement) {
    super.populate(modelElement);
  }
  
  public void postLoad() {
    
  }
  
  public String toString() {
    return ToString.toString(this);
  }
}
