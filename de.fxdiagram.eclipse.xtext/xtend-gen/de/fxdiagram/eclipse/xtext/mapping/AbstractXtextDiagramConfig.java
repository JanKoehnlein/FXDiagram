package de.fxdiagram.eclipse.xtext.mapping;

import de.fxdiagram.core.XDomainObjectShape;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.extensions.InitializingListener;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.eclipse.mapping.AbstractEclipseDiagramConfig;
import de.fxdiagram.eclipse.xtext.AbstractXtextDescriptor;
import de.fxdiagram.eclipse.xtext.XtextDomainObjectProvider;
import de.fxdiagram.mapping.IMappedElementDescriptorProvider;
import javafx.beans.property.ReadOnlyObjectProperty;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public abstract class AbstractXtextDiagramConfig extends AbstractEclipseDiagramConfig {
  @Override
  public void initialize(final XDomainObjectShape shape) {
    ReadOnlyObjectProperty<DomainObjectDescriptor> _domainObjectDescriptorProperty = shape.domainObjectDescriptorProperty();
    if (_domainObjectDescriptorProperty!=null) {
      InitializingListener<DomainObjectDescriptor> _initializingListener = new InitializingListener<DomainObjectDescriptor>();
      final Procedure1<InitializingListener<DomainObjectDescriptor>> _function = (InitializingListener<DomainObjectDescriptor> it) -> {
        final Procedure1<Object> _function_1 = (Object it_1) -> {
          if ((it_1 instanceof AbstractXtextDescriptor<?>)) {
            ((AbstractXtextDescriptor<?>)it_1).injectMembers(shape);
          }
        };
        it.setSet(_function_1);
      };
      InitializingListener<DomainObjectDescriptor> _doubleArrow = ObjectExtensions.<InitializingListener<DomainObjectDescriptor>>operator_doubleArrow(_initializingListener, _function);
      CoreExtensions.<DomainObjectDescriptor>addInitializingListener(_domainObjectDescriptorProperty, _doubleArrow);
    }
    super.initialize(shape);
  }
  
  @Override
  protected IMappedElementDescriptorProvider createDomainObjectProvider() {
    return new XtextDomainObjectProvider();
  }
}
