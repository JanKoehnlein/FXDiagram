package de.fxdiagram.eclipse.shapes;

import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.eclipse.behavior.LazyConnectionMappingBehavior;
import de.fxdiagram.eclipse.behavior.OpenElementInEditorBehavior;
import de.fxdiagram.eclipse.mapping.AbstractXtextDescriptor;
import de.fxdiagram.eclipse.mapping.IMappedElementDescriptor;
import de.fxdiagram.lib.nodes.RectangleBorderPane;
import de.fxdiagram.lib.simple.OpenableDiagramNode;
import java.util.Collections;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;

/**
 * Base implementation for a {@link XNode} with a nested {@link XDiagram} that belongs to an
 * {@link IMappedElementDescriptor}.
 * 
 * If the descriptor is an {@link AbstractXtextDescriptor}, members are automatically injected using
 * the Xtext language's injector.
 */
@ModelNode
@SuppressWarnings("all")
public class BaseDiagramNode<T extends Object> extends OpenableDiagramNode {
  public BaseDiagramNode() {
    ReadOnlyObjectProperty<DomainObjectDescriptor> _domainObjectProperty = this.domainObjectProperty();
    final ChangeListener<DomainObjectDescriptor> _function = new ChangeListener<DomainObjectDescriptor>() {
      @Override
      public void changed(final ObservableValue<? extends DomainObjectDescriptor> prop, final DomainObjectDescriptor oldVal, final DomainObjectDescriptor newVal) {
        BaseDiagramNode.this.injectMembers();
      }
    };
    _domainObjectProperty.addListener(_function);
  }
  
  public BaseDiagramNode(final IMappedElementDescriptor<T> descriptor) {
    super(descriptor);
    this.injectMembers();
  }
  
  protected void injectMembers() {
    final DomainObjectDescriptor descriptor = this.getDomainObject();
    if ((descriptor instanceof AbstractXtextDescriptor<?>)) {
      ((AbstractXtextDescriptor<?>)descriptor).injectMembers(this);
    }
  }
  
  @Override
  public DomainObjectDescriptor getDomainObject() {
    DomainObjectDescriptor _domainObject = super.getDomainObject();
    return ((IMappedElementDescriptor<T>) _domainObject);
  }
  
  @Override
  public void initializeGraphics() {
    super.initializeGraphics();
    RectangleBorderPane _pane = this.getPane();
    Color _rgb = Color.rgb(242, 236, 181);
    Stop _stop = new Stop(0, _rgb);
    Color _rgb_1 = Color.rgb(255, 248, 202);
    Stop _stop_1 = new Stop(1, _rgb_1);
    LinearGradient _linearGradient = new LinearGradient(
      0, 0, 1, 1, 
      true, CycleMethod.NO_CYCLE, 
      Collections.<Stop>unmodifiableList(CollectionLiterals.<Stop>newArrayList(_stop, _stop_1)));
    _pane.setBackgroundPaint(_linearGradient);
  }
  
  @Override
  public void doActivate() {
    super.doActivate();
    final DomainObjectDescriptor descriptor = this.getDomainObject();
    if ((descriptor instanceof IMappedElementDescriptor<?>)) {
      LazyConnectionMappingBehavior.addLazyBehavior(this, ((IMappedElementDescriptor<?>)descriptor));
    }
    OpenElementInEditorBehavior _openElementInEditorBehavior = new OpenElementInEditorBehavior(this);
    this.addBehavior(_openElementInEditorBehavior);
    XDiagram _innerDiagram = this.getInnerDiagram();
    _innerDiagram.setIsLayoutOnActivate(true);
  }
  
  public void populate(final ModelElementImpl modelElement) {
    super.populate(modelElement);
  }
}
