package de.fxdiagram.lib.model;

import com.google.common.collect.Iterables;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRapidButton;
import de.fxdiagram.core.XRapidButtonAction;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.behavior.AbstractHostBehavior;
import de.fxdiagram.core.behavior.Behavior;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.extensions.InitializingListListener;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.core.tools.AbstractChooser;
import java.util.List;
import java.util.Set;
import javafx.collections.ObservableList;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public abstract class AbstractConnectionRapidButtonBehavior<HOST extends XNode, MODEL extends Object, KEY extends DomainObjectDescriptor> extends AbstractHostBehavior<HOST> {
  private Set<KEY> availableChoiceKeys = CollectionLiterals.<KEY>newLinkedHashSet();
  
  private Set<KEY> unavailableChoiceKeys = CollectionLiterals.<KEY>newHashSet();
  
  private List<XRapidButton> buttons = CollectionLiterals.<XRapidButton>newArrayList();
  
  public AbstractConnectionRapidButtonBehavior(final HOST host) {
    super(host);
  }
  
  public Class<? extends Behavior> getBehaviorKey() {
    return this.getClass();
  }
  
  protected void doActivate() {
    Iterable<MODEL> _initialModelChoices = this.getInitialModelChoices();
    final Function1<MODEL, KEY> _function = new Function1<MODEL, KEY>() {
      public KEY apply(final MODEL it) {
        return AbstractConnectionRapidButtonBehavior.this.getChoiceKey(it);
      }
    };
    Iterable<KEY> _map = IterableExtensions.<MODEL, KEY>map(_initialModelChoices, _function);
    Iterables.<KEY>addAll(this.availableChoiceKeys, _map);
    boolean _isEmpty = this.availableChoiceKeys.isEmpty();
    boolean _not = (!_isEmpty);
    if (_not) {
      final XRapidButtonAction _function_1 = new XRapidButtonAction() {
        @Override
        public void perform(final XRapidButton button) {
          final AbstractChooser chooser = AbstractConnectionRapidButtonBehavior.this.createChooser(button, AbstractConnectionRapidButtonBehavior.this.availableChoiceKeys, AbstractConnectionRapidButtonBehavior.this.unavailableChoiceKeys);
          HOST _host = AbstractConnectionRapidButtonBehavior.this.getHost();
          XRoot _root = CoreExtensions.getRoot(_host);
          _root.setCurrentTool(chooser);
        }
      };
      final XRapidButtonAction addConnectionAction = _function_1;
      Iterable<XRapidButton> _createButtons = this.createButtons(addConnectionAction);
      Iterables.<XRapidButton>addAll(this.buttons, _createButtons);
      HOST _host = this.getHost();
      XDiagram _diagram = CoreExtensions.getDiagram(_host);
      ObservableList<XRapidButton> _buttons = _diagram.getButtons();
      Iterables.<XRapidButton>addAll(_buttons, this.buttons);
      HOST _host_1 = this.getHost();
      XDiagram _diagram_1 = CoreExtensions.getDiagram(_host_1);
      ObservableList<XConnection> _connections = _diagram_1.getConnections();
      InitializingListListener<XConnection> _initializingListListener = new InitializingListListener<XConnection>();
      final Procedure1<InitializingListListener<XConnection>> _function_2 = new Procedure1<InitializingListListener<XConnection>>() {
        public void apply(final InitializingListListener<XConnection> it) {
          final Procedure1<XConnection> _function = new Procedure1<XConnection>() {
            public void apply(final XConnection it) {
              DomainObjectDescriptor _domainObject = it.getDomainObject();
              boolean _remove = AbstractConnectionRapidButtonBehavior.this.availableChoiceKeys.remove(_domainObject);
              if (_remove) {
                boolean _isEmpty = AbstractConnectionRapidButtonBehavior.this.availableChoiceKeys.isEmpty();
                if (_isEmpty) {
                  HOST _host = AbstractConnectionRapidButtonBehavior.this.getHost();
                  XDiagram _diagram = CoreExtensions.getDiagram(_host);
                  ObservableList<XRapidButton> _buttons = _diagram.getButtons();
                  Iterables.removeAll(_buttons, AbstractConnectionRapidButtonBehavior.this.buttons);
                }
                DomainObjectDescriptor _domainObject_1 = it.getDomainObject();
                AbstractConnectionRapidButtonBehavior.this.unavailableChoiceKeys.add(((KEY) _domainObject_1));
              }
            }
          };
          it.setAdd(_function);
          final Procedure1<XConnection> _function_1 = new Procedure1<XConnection>() {
            public void apply(final XConnection it) {
              DomainObjectDescriptor _domainObject = it.getDomainObject();
              boolean _remove = AbstractConnectionRapidButtonBehavior.this.unavailableChoiceKeys.remove(_domainObject);
              if (_remove) {
                boolean _isEmpty = AbstractConnectionRapidButtonBehavior.this.availableChoiceKeys.isEmpty();
                if (_isEmpty) {
                  HOST _host = AbstractConnectionRapidButtonBehavior.this.getHost();
                  XDiagram _diagram = CoreExtensions.getDiagram(_host);
                  ObservableList<XRapidButton> _buttons = _diagram.getButtons();
                  Iterables.<XRapidButton>addAll(_buttons, AbstractConnectionRapidButtonBehavior.this.buttons);
                }
                DomainObjectDescriptor _domainObject_1 = it.getDomainObject();
                AbstractConnectionRapidButtonBehavior.this.availableChoiceKeys.add(((KEY) _domainObject_1));
              }
            }
          };
          it.setRemove(_function_1);
        }
      };
      InitializingListListener<XConnection> _doubleArrow = ObjectExtensions.<InitializingListListener<XConnection>>operator_doubleArrow(_initializingListListener, _function_2);
      CoreExtensions.<XConnection>addInitializingListener(_connections, _doubleArrow);
    }
  }
  
  protected abstract Iterable<MODEL> getInitialModelChoices();
  
  protected abstract KEY getChoiceKey(final MODEL model);
  
  protected abstract XNode createNode(final KEY key);
  
  protected abstract Iterable<XRapidButton> createButtons(final XRapidButtonAction addConnectionAction);
  
  protected abstract AbstractChooser createChooser(final XRapidButton button, final Set<KEY> availableChoiceKeys, final Set<KEY> unavailableChoiceKeys);
}
