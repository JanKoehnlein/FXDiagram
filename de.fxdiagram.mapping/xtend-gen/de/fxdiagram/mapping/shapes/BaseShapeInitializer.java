package de.fxdiagram.mapping.shapes;

import de.fxdiagram.core.XDomainObjectShape;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.mapping.AbstractMapping;
import de.fxdiagram.mapping.IMappedElementDescriptor;
import de.fxdiagram.mapping.XDiagramConfig;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 * Call {@link XDiagramConfig#initialize()} on load, as soon as the
 * {@link XDomainObjectShape#domainObjectDescriptor} is set.
 */
@SuppressWarnings("all")
public class BaseShapeInitializer {
  public static void initializeLazily(final XDomainObjectShape shape) {
    ReadOnlyObjectProperty<DomainObjectDescriptor> _domainObjectDescriptorProperty = shape.domainObjectDescriptorProperty();
    final ChangeListener<DomainObjectDescriptor> _function = new ChangeListener<DomainObjectDescriptor>() {
      @Override
      public void changed(final ObservableValue<? extends DomainObjectDescriptor> p, final DomainObjectDescriptor o, final DomainObjectDescriptor newDescriptor) {
        if ((newDescriptor instanceof IMappedElementDescriptor<?>)) {
          AbstractMapping<?> _mapping = null;
          if (((IMappedElementDescriptor<?>)newDescriptor)!=null) {
            _mapping=((IMappedElementDescriptor<?>)newDescriptor).getMapping();
          }
          XDiagramConfig _config = null;
          if (_mapping!=null) {
            _config=_mapping.getConfig();
          }
          if (_config!=null) {
            _config.initialize(shape);
          }
        }
        p.removeListener(this);
      }
    };
    _domainObjectDescriptorProperty.addListener(_function);
  }
}
