package de.fxdiagram.xtext.glue.shapes;

import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.lib.nodes.RectangleBorderPane;
import de.fxdiagram.lib.simple.SimpleNode;
import de.fxdiagram.xtext.glue.behavior.LazyConnectionMappingBehavior;
import de.fxdiagram.xtext.glue.behavior.OpenElementInEditorBehavior;
import de.fxdiagram.xtext.glue.mapping.AbstractXtextDescriptor;
import de.fxdiagram.xtext.glue.mapping.IMappedElementDescriptor;
import java.util.Collections;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;

@ModelNode
@SuppressWarnings("all")
public class BaseNode<T extends Object> extends SimpleNode {
  public BaseNode() {
    ReadOnlyObjectProperty<DomainObjectDescriptor> _domainObjectProperty = this.domainObjectProperty();
    final ChangeListener<DomainObjectDescriptor> _function = new ChangeListener<DomainObjectDescriptor>() {
      public void changed(final ObservableValue<? extends DomainObjectDescriptor> prop, final DomainObjectDescriptor oldVal, final DomainObjectDescriptor newVal) {
        BaseNode.this.injectMembers();
      }
    };
    _domainObjectProperty.addListener(_function);
  }
  
  public BaseNode(final IMappedElementDescriptor<T> descriptor) {
    super(descriptor);
    this.injectMembers();
  }
  
  protected void injectMembers() {
    final IMappedElementDescriptor<T> domainObject = this.getDomainObject();
    if ((domainObject instanceof AbstractXtextDescriptor<?>)) {
      ((AbstractXtextDescriptor<?>)domainObject).injectMembers(this);
    }
  }
  
  public IMappedElementDescriptor<T> getDomainObject() {
    DomainObjectDescriptor _domainObject = super.getDomainObject();
    return ((IMappedElementDescriptor<T>) _domainObject);
  }
  
  protected Node createNode() {
    RectangleBorderPane _xblockexpression = null;
    {
      Node _createNode = super.createNode();
      final RectangleBorderPane pane = ((RectangleBorderPane) _createNode);
      Color _rgb = Color.rgb(158, 188, 227);
      Stop _stop = new Stop(0, _rgb);
      Color _rgb_1 = Color.rgb(220, 230, 255);
      Stop _stop_1 = new Stop(1, _rgb_1);
      LinearGradient _linearGradient = new LinearGradient(
        0, 0, 1, 1, 
        true, CycleMethod.NO_CYCLE, 
        Collections.<Stop>unmodifiableList(CollectionLiterals.<Stop>newArrayList(_stop, _stop_1)));
      pane.setBackgroundPaint(_linearGradient);
      _xblockexpression = pane;
    }
    return _xblockexpression;
  }
  
  public void doActivate() {
    super.doActivate();
    IMappedElementDescriptor<T> _domainObject = this.getDomainObject();
    LazyConnectionMappingBehavior.<Object>addLazyBehavior(this, _domainObject);
    OpenElementInEditorBehavior _openElementInEditorBehavior = new OpenElementInEditorBehavior(this);
    this.addBehavior(_openElementInEditorBehavior);
  }
  
  public void populate(final ModelElementImpl modelElement) {
    super.populate(modelElement);
  }
}
