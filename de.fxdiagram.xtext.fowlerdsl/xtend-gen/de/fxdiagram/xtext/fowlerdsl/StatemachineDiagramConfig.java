package de.fxdiagram.xtext.fowlerdsl;

import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XConnectionLabel;
import de.fxdiagram.eclipse.mapping.AbstractDiagramConfig;
import de.fxdiagram.eclipse.mapping.ConnectionMapping;
import de.fxdiagram.eclipse.mapping.DiagramMapping;
import de.fxdiagram.eclipse.mapping.IMappedElementDescriptor;
import de.fxdiagram.eclipse.mapping.MappingAcceptor;
import de.fxdiagram.eclipse.mapping.NodeMapping;
import javafx.scene.text.Text;
import org.eclipse.xtext.example.fowlerdsl.statemachine.Event;
import org.eclipse.xtext.example.fowlerdsl.statemachine.State;
import org.eclipse.xtext.example.fowlerdsl.statemachine.Statemachine;
import org.eclipse.xtext.example.fowlerdsl.statemachine.Transition;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

/**
 * A very simple example how to create a diagram mapping for an the Fowler DSL shipped
 * as an Xtext example language.
 * 
 * We implemented it as a fragment such that we can use the example plug-ins as they are,
 * but usually you would just add the code to the UI plug-in of your language.
 * 
 * The configuration contains three mappings:
 * <ol>
 * <li>A {@link Statemachine} is mapped to a diagram,</li>
 * <li>A {@link State} is mapped to a node, and</li>
 * <li>A {@link Transition} is mapped to a connection.</li>
 * </ol>
 * 
 * In addition to this class, you just have to apply two changes to the plugin/fragment.xml:
 * <ol>
 * <li>Register this configuration to the extension point
 * <code>de.fxdiagram.eclipse.fxDiagramConfig</code>.</li>
 * <li>Add a handler for the commandId <code>de.fxdiagram.eclipse.showInDiagramCommand</code>
 * that adds the selected element in the editor to the diagram.
 * </ol>
 */
@SuppressWarnings("all")
public class StatemachineDiagramConfig extends AbstractDiagramConfig {
  private final DiagramMapping<Statemachine> statemachineDiagram = new DiagramMapping<Statemachine>(this, "statemachineDiagram") {
    @Override
    public void calls() {
      final Function1<Statemachine, Iterable<? extends State>> _function = new Function1<Statemachine, Iterable<? extends State>>() {
        @Override
        public Iterable<? extends State> apply(final Statemachine it) {
          return it.getStates();
        }
      };
      this.<State>nodeForEach(StatemachineDiagramConfig.this.stateNode, _function);
    }
  };
  
  private final NodeMapping<State> stateNode = new NodeMapping<State>(this, "stateNode") {
    @Override
    public void calls() {
      final Function1<State, Iterable<? extends Transition>> _function = new Function1<State, Iterable<? extends Transition>>() {
        @Override
        public Iterable<? extends Transition> apply(final State it) {
          return it.getTransitions();
        }
      };
      this.<Transition>outConnectionForEach(StatemachineDiagramConfig.this.transitionConnection, _function);
    }
  };
  
  private final ConnectionMapping<Transition> transitionConnection = new ConnectionMapping<Transition>(this, "transitionConnection") {
    @Override
    public XConnection createConnection(final IMappedElementDescriptor<Transition> descriptor) {
      XConnection _xConnection = new XConnection(descriptor);
      final Procedure1<XConnection> _function = new Procedure1<XConnection>() {
        @Override
        public void apply(final XConnection it) {
          XConnectionLabel _xConnectionLabel = new XConnectionLabel(it);
          final Procedure1<XConnectionLabel> _function = new Procedure1<XConnectionLabel>() {
            @Override
            public void apply(final XConnectionLabel label) {
              Text _text = label.getText();
              final Function1<Transition, String> _function = new Function1<Transition, String>() {
                @Override
                public String apply(final Transition it) {
                  Event _event = it.getEvent();
                  return _event.getName();
                }
              };
              String _withDomainObject = descriptor.<String>withDomainObject(_function);
              _text.setText(_withDomainObject);
            }
          };
          ObjectExtensions.<XConnectionLabel>operator_doubleArrow(_xConnectionLabel, _function);
        }
      };
      return ObjectExtensions.<XConnection>operator_doubleArrow(_xConnection, _function);
    }
    
    @Override
    public void calls() {
      final Function1<Transition, State> _function = new Function1<Transition, State>() {
        @Override
        public State apply(final Transition it) {
          return it.getState();
        }
      };
      this.<State>target(StatemachineDiagramConfig.this.stateNode, _function);
    }
  };
  
  @Override
  protected <ARG extends Object> void entryCalls(final ARG domainArgument, @Extension final MappingAcceptor<ARG> acceptor) {
    boolean _matched = false;
    if (!_matched) {
      if (domainArgument instanceof Statemachine) {
        _matched=true;
        acceptor.add(this.statemachineDiagram);
      }
    }
    if (!_matched) {
      if (domainArgument instanceof State) {
        _matched=true;
        acceptor.add(this.stateNode);
      }
    }
    if (!_matched) {
      if (domainArgument instanceof Transition) {
        _matched=true;
        acceptor.add(this.transitionConnection);
      }
    }
  }
}
