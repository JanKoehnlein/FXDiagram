package de.fxdiagram.xtext.glue.shapes;

import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.lib.nodes.RectangleBorderPane;
import de.fxdiagram.lib.simple.OpenableDiagramNode;
import de.fxdiagram.xtext.glue.XtextDomainObjectDescriptor;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.paint.Color;

@ModelNode
@SuppressWarnings("all")
public class BaseDiagramNode<T extends Object> extends OpenableDiagramNode {
  public BaseDiagramNode() {
    ReadOnlyObjectProperty<DomainObjectDescriptor> _domainObjectProperty = this.domainObjectProperty();
    final ChangeListener<DomainObjectDescriptor> _function = new ChangeListener<DomainObjectDescriptor>() {
      public void changed(final ObservableValue<? extends DomainObjectDescriptor> prop, final DomainObjectDescriptor oldVal, final DomainObjectDescriptor newVal) {
        if ((newVal instanceof XtextDomainObjectDescriptor<?>)) {
          ((XtextDomainObjectDescriptor<?>)newVal).injectMembers(BaseDiagramNode.this);
        }
      }
    };
    _domainObjectProperty.addListener(_function);
  }
  
  public BaseDiagramNode(final XtextDomainObjectDescriptor<T> descriptor) {
    super(descriptor);
    descriptor.injectMembers(this);
  }
  
  public void initializeGraphics() {
    super.initializeGraphics();
    RectangleBorderPane _pane = this.getPane();
    _pane.setBackgroundPaint(Color.BLANCHEDALMOND);
    RectangleBorderPane _pane_1 = this.getPane();
    _pane_1.setBorderRadius(0);
    RectangleBorderPane _pane_2 = this.getPane();
    _pane_2.setBackgroundRadius(0);
  }
  
  protected XtextDomainObjectDescriptor<T> getDescriptor() {
    DomainObjectDescriptor _domainObject = this.getDomainObject();
    return ((XtextDomainObjectDescriptor<T>) _domainObject);
  }
  
  public void doActivate() {
    throw new Error("Unresolved compilation problems:"
      + "\nThe field provider is not visible"
      + "\nThe field provider is not visible");
  }
}
