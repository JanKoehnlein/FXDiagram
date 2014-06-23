package de.fxdiagram.xtext.glue.shapes;

import com.google.common.base.Objects;
import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.lib.nodes.RectangleBorderPane;
import de.fxdiagram.lib.simple.OpenableDiagramNode;
import de.fxdiagram.xtext.glue.XtextDomainObjectDescriptor;
import de.fxdiagram.xtext.glue.XtextDomainObjectProvider;
import de.fxdiagram.xtext.glue.behavior.LazyConnectionMappingBehavior;
import de.fxdiagram.xtext.glue.behavior.OpenElementInEditorBehavior;
import de.fxdiagram.xtext.glue.mapping.AbstractConnectionMappingCall;
import de.fxdiagram.xtext.glue.mapping.AbstractMapping;
import de.fxdiagram.xtext.glue.mapping.NodeMapping;
import de.fxdiagram.xtext.glue.mapping.XDiagramConfigInterpreter;
import java.util.List;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.paint.Color;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

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
    super.doActivate();
    XtextDomainObjectDescriptor<T> _descriptor = this.getDescriptor();
    AbstractMapping<T> _mapping = _descriptor.getMapping();
    if ((_mapping instanceof NodeMapping<?>)) {
      XtextDomainObjectDescriptor<T> _descriptor_1 = this.getDescriptor();
      AbstractMapping<T> _mapping_1 = _descriptor_1.getMapping();
      final NodeMapping<T> nodeMapping = ((NodeMapping<T>) _mapping_1);
      LazyConnectionMappingBehavior<T> lazyBehavior = null;
      List<AbstractConnectionMappingCall<?, T>> _outgoing = nodeMapping.getOutgoing();
      final Function1<AbstractConnectionMappingCall<?, T>, Boolean> _function = new Function1<AbstractConnectionMappingCall<?, T>, Boolean>() {
        public Boolean apply(final AbstractConnectionMappingCall<?, T> it) {
          return Boolean.valueOf(it.isLazy());
        }
      };
      final Iterable<AbstractConnectionMappingCall<?, T>> lazyOutgoing = IterableExtensions.<AbstractConnectionMappingCall<?, T>>filter(_outgoing, _function);
      boolean _isEmpty = IterableExtensions.isEmpty(lazyOutgoing);
      boolean _not = (!_isEmpty);
      if (_not) {
        LazyConnectionMappingBehavior<T> _elvis = null;
        if (lazyBehavior != null) {
          _elvis = lazyBehavior;
        } else {
          LazyConnectionMappingBehavior<T> _lazyConnectionMappingBehavior = new LazyConnectionMappingBehavior<T>(this);
          _elvis = _lazyConnectionMappingBehavior;
        }
        lazyBehavior = _elvis;
        for (final AbstractConnectionMappingCall<?, T> out : lazyOutgoing) {
          XtextDomainObjectDescriptor<T> _descriptor_2 = this.getDescriptor();
          XtextDomainObjectProvider _provider = _descriptor_2.getProvider();
          XDiagramConfigInterpreter _xDiagramConfigInterpreter = new XDiagramConfigInterpreter(_provider);
          lazyBehavior.addConnectionMappingCall(out, _xDiagramConfigInterpreter, true);
        }
      }
      List<AbstractConnectionMappingCall<?, T>> _incoming = nodeMapping.getIncoming();
      final Function1<AbstractConnectionMappingCall<?, T>, Boolean> _function_1 = new Function1<AbstractConnectionMappingCall<?, T>, Boolean>() {
        public Boolean apply(final AbstractConnectionMappingCall<?, T> it) {
          return Boolean.valueOf(it.isLazy());
        }
      };
      final Iterable<AbstractConnectionMappingCall<?, T>> lazyIncoming = IterableExtensions.<AbstractConnectionMappingCall<?, T>>filter(_incoming, _function_1);
      boolean _isEmpty_1 = IterableExtensions.isEmpty(lazyIncoming);
      boolean _not_1 = (!_isEmpty_1);
      if (_not_1) {
        LazyConnectionMappingBehavior<T> _elvis_1 = null;
        if (lazyBehavior != null) {
          _elvis_1 = lazyBehavior;
        } else {
          LazyConnectionMappingBehavior<T> _lazyConnectionMappingBehavior_1 = new LazyConnectionMappingBehavior<T>(this);
          _elvis_1 = _lazyConnectionMappingBehavior_1;
        }
        lazyBehavior = _elvis_1;
        for (final AbstractConnectionMappingCall<?, T> in : lazyIncoming) {
          XtextDomainObjectDescriptor<T> _descriptor_3 = this.getDescriptor();
          XtextDomainObjectProvider _provider_1 = _descriptor_3.getProvider();
          XDiagramConfigInterpreter _xDiagramConfigInterpreter_1 = new XDiagramConfigInterpreter(_provider_1);
          lazyBehavior.addConnectionMappingCall(in, _xDiagramConfigInterpreter_1, false);
        }
      }
      boolean _notEquals = (!Objects.equal(lazyBehavior, null));
      if (_notEquals) {
        this.addBehavior(lazyBehavior);
      }
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
