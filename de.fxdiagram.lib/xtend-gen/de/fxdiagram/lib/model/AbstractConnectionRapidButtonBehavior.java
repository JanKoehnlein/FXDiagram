package de.fxdiagram.lib.model;

import com.google.common.collect.Iterables;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRapidButton;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.behavior.AbstractHostBehavior;
import de.fxdiagram.core.behavior.Behavior;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.model.DomainObjectHandle;
import de.fxdiagram.core.tools.AbstractChooser;
import java.util.List;
import java.util.Set;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public abstract class AbstractConnectionRapidButtonBehavior<HOST extends XNode, MODEL extends Object, KEY extends DomainObjectHandle> extends AbstractHostBehavior<HOST> {
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
    final Function1<MODEL,KEY> _function = new Function1<MODEL,KEY>() {
      public KEY apply(final MODEL it) {
        return AbstractConnectionRapidButtonBehavior.this.getChoiceKey(it);
      }
    };
    Iterable<KEY> _map = IterableExtensions.<MODEL, KEY>map(_initialModelChoices, _function);
    Iterables.<KEY>addAll(this.availableChoiceKeys, _map);
    boolean _isEmpty = this.availableChoiceKeys.isEmpty();
    boolean _not = (!_isEmpty);
    if (_not) {
      final Procedure1<XRapidButton> _function_1 = new Procedure1<XRapidButton>() {
        public void apply(final XRapidButton button) {
          final AbstractChooser chooser = AbstractConnectionRapidButtonBehavior.this.createChooser(button, AbstractConnectionRapidButtonBehavior.this.availableChoiceKeys, AbstractConnectionRapidButtonBehavior.this.unavailableChoiceKeys);
          HOST _host = AbstractConnectionRapidButtonBehavior.this.getHost();
          XRoot _root = CoreExtensions.getRoot(_host);
          _root.setCurrentTool(chooser);
        }
      };
      final Procedure1<XRapidButton> addConnectionAction = _function_1;
      Iterable<XRapidButton> _createButtons = this.createButtons(addConnectionAction);
      Iterables.<XRapidButton>addAll(this.buttons, _createButtons);
      HOST _host = this.getHost();
      XDiagram _diagram = CoreExtensions.getDiagram(_host);
      ObservableList<XRapidButton> _buttons = _diagram.getButtons();
      Iterables.<XRapidButton>addAll(_buttons, this.buttons);
      HOST _host_1 = this.getHost();
      XDiagram _diagram_1 = CoreExtensions.getDiagram(_host_1);
      ObservableList<XConnection> _connections = _diagram_1.getConnections();
      final Procedure1<XConnection> _function_2 = new Procedure1<XConnection>() {
        public void apply(final XConnection it) {
          DomainObjectHandle _domainObject = it.getDomainObject();
          boolean _remove = AbstractConnectionRapidButtonBehavior.this.availableChoiceKeys.remove(_domainObject);
          if (_remove) {
            DomainObjectHandle _domainObject_1 = it.getDomainObject();
            AbstractConnectionRapidButtonBehavior.this.unavailableChoiceKeys.add(((KEY) _domainObject_1));
          }
        }
      };
      IterableExtensions.<XConnection>forEach(_connections, _function_2);
      HOST _host_2 = this.getHost();
      XDiagram _diagram_2 = CoreExtensions.getDiagram(_host_2);
      ObservableList<XConnection> _connections_1 = _diagram_2.getConnections();
      final ListChangeListener<XConnection> _function_3 = new ListChangeListener<XConnection>() {
        public void onChanged(final ListChangeListener.Change<? extends XConnection> change) {
          boolean _isEmpty = AbstractConnectionRapidButtonBehavior.this.availableChoiceKeys.isEmpty();
          final boolean hadChoices = (!_isEmpty);
          boolean _next = change.next();
          boolean _while = _next;
          while (_while) {
            {
              boolean _wasAdded = change.wasAdded();
              if (_wasAdded) {
                List<? extends XConnection> _addedSubList = change.getAddedSubList();
                final Procedure1<XConnection> _function = new Procedure1<XConnection>() {
                  public void apply(final XConnection it) {
                    DomainObjectHandle _domainObject = it.getDomainObject();
                    boolean _remove = AbstractConnectionRapidButtonBehavior.this.availableChoiceKeys.remove(_domainObject);
                    if (_remove) {
                      DomainObjectHandle _domainObject_1 = it.getDomainObject();
                      AbstractConnectionRapidButtonBehavior.this.unavailableChoiceKeys.add(((KEY) _domainObject_1));
                    }
                  }
                };
                IterableExtensions.forEach(_addedSubList, _function);
              }
              boolean _wasRemoved = change.wasRemoved();
              if (_wasRemoved) {
                List<? extends XConnection> _removed = change.getRemoved();
                final Procedure1<XConnection> _function_1 = new Procedure1<XConnection>() {
                  public void apply(final XConnection it) {
                    DomainObjectHandle _domainObject = it.getDomainObject();
                    boolean _remove = AbstractConnectionRapidButtonBehavior.this.unavailableChoiceKeys.remove(_domainObject);
                    if (_remove) {
                      DomainObjectHandle _domainObject_1 = it.getDomainObject();
                      AbstractConnectionRapidButtonBehavior.this.availableChoiceKeys.add(((KEY) _domainObject_1));
                    }
                  }
                };
                IterableExtensions.forEach(_removed, _function_1);
              }
            }
            boolean _next_1 = change.next();
            _while = _next_1;
          }
          boolean _isEmpty_1 = AbstractConnectionRapidButtonBehavior.this.availableChoiceKeys.isEmpty();
          if (_isEmpty_1) {
            HOST _host = AbstractConnectionRapidButtonBehavior.this.getHost();
            XDiagram _diagram = CoreExtensions.getDiagram(_host);
            ObservableList<XRapidButton> _buttons = _diagram.getButtons();
            Iterables.removeAll(_buttons, AbstractConnectionRapidButtonBehavior.this.buttons);
          } else {
            if ((!hadChoices)) {
              HOST _host_1 = AbstractConnectionRapidButtonBehavior.this.getHost();
              XDiagram _diagram_1 = CoreExtensions.getDiagram(_host_1);
              ObservableList<XRapidButton> _buttons_1 = _diagram_1.getButtons();
              Iterables.<XRapidButton>addAll(_buttons_1, AbstractConnectionRapidButtonBehavior.this.buttons);
            }
          }
        }
      };
      _connections_1.addListener(_function_3);
    }
  }
  
  protected abstract Iterable<MODEL> getInitialModelChoices();
  
  protected abstract KEY getChoiceKey(final MODEL model);
  
  protected abstract XNode createNode(final KEY key);
  
  protected abstract Iterable<XRapidButton> createButtons(final Procedure1<? super XRapidButton> addConnectionAction);
  
  protected abstract AbstractChooser createChooser(final XRapidButton button, final Set<KEY> availableChoiceKeys, final Set<KEY> unavailableChoiceKeys);
}
