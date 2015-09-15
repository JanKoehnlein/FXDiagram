package de.fxdiagram.xtext.fowlerdsl;

import com.google.common.collect.Iterables;
import de.fxdiagram.core.extensions.ButtonExtensions;
import de.fxdiagram.eclipse.xtext.mapping.AbstractXtextDiagramConfig;
import de.fxdiagram.mapping.ConnectionLabelMapping;
import de.fxdiagram.mapping.ConnectionMapping;
import de.fxdiagram.mapping.DiagramMapping;
import de.fxdiagram.mapping.MappingAcceptor;
import de.fxdiagram.mapping.MultiConnectionMappingCall;
import de.fxdiagram.mapping.NodeLabelMapping;
import de.fxdiagram.mapping.NodeMapping;
import java.util.List;
import javafx.geometry.Side;
import javafx.scene.Node;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.example.fowlerdsl.statemachine.Event;
import org.eclipse.xtext.example.fowlerdsl.statemachine.State;
import org.eclipse.xtext.example.fowlerdsl.statemachine.Statemachine;
import org.eclipse.xtext.example.fowlerdsl.statemachine.Transition;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.ListExtensions;

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
public class StatemachineDiagramConfig extends AbstractXtextDiagramConfig {
  private final DiagramMapping<Statemachine> statemachineDiagram = new DiagramMapping<Statemachine>(this, "statemachineDiagram", "Statemachine") {
    @Override
    public void calls() {
      final Function1<Statemachine, Iterable<? extends State>> _function = (Statemachine it) -> {
        return it.getStates();
      };
      this.<State>nodeForEach(StatemachineDiagramConfig.this.stateNode, _function);
      final Function1<Statemachine, Iterable<? extends Transition>> _function_1 = (Statemachine it) -> {
        EList<State> _states = it.getStates();
        final Function1<State, EList<Transition>> _function_2 = (State it_1) -> {
          return it_1.getTransitions();
        };
        List<EList<Transition>> _map = ListExtensions.<State, EList<Transition>>map(_states, _function_2);
        return Iterables.<Transition>concat(_map);
      };
      this.<Transition>connectionForEach(StatemachineDiagramConfig.this.transitionConnection, _function_1);
    }
  };
  
  private final NodeMapping<State> stateNode = new NodeMapping<State>(this, "stateNode", "State") {
    @Override
    protected void calls() {
      final Function1<State, State> _function = (State it) -> {
        return it;
      };
      this.<State>labelFor(StatemachineDiagramConfig.this.stateLabel, _function);
      final Function1<State, Iterable<? extends Transition>> _function_1 = (State it) -> {
        return it.getTransitions();
      };
      MultiConnectionMappingCall<Transition, State> _outConnectionForEach = this.<Transition>outConnectionForEach(StatemachineDiagramConfig.this.transitionConnection, _function_1);
      final Function1<Side, Node> _function_2 = (Side it) -> {
        return ButtonExtensions.getArrowButton(it, "Add transition");
      };
      _outConnectionForEach.asButton(_function_2);
    }
  };
  
  private final NodeLabelMapping<State> stateLabel = new NodeLabelMapping<State>(this, "stateLabel", "") {
    @Override
    public String getText(final State it) {
      return it.getName();
    }
  };
  
  private final ConnectionMapping<Transition> transitionConnection = new ConnectionMapping<Transition>(this, "transitionConnection", "Transition") {
    @Override
    protected void calls() {
      final Function1<Transition, Event> _function = (Transition it) -> {
        return it.getEvent();
      };
      this.<Event>labelFor(StatemachineDiagramConfig.this.eventLabel, _function);
      final Function1<Transition, State> _function_1 = (Transition it) -> {
        EObject _eContainer = it.eContainer();
        return ((State) _eContainer);
      };
      this.<State>source(StatemachineDiagramConfig.this.stateNode, _function_1);
      final Function1<Transition, State> _function_2 = (Transition it) -> {
        return it.getState();
      };
      this.<State>target(StatemachineDiagramConfig.this.stateNode, _function_2);
    }
  };
  
  private final ConnectionLabelMapping<Event> eventLabel = new ConnectionLabelMapping<Event>(this, "eventLabel", "") {
    @Override
    public String getText(final Event it) {
      return it.getName();
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
        final Function1<ARG, Statemachine> _function = (ARG it) -> {
          return EcoreUtil2.<Statemachine>getContainerOfType(((State)domainArgument), Statemachine.class);
        };
        acceptor.<Statemachine>add(this.statemachineDiagram, _function);
        final Function1<ARG, State> _function_1 = (ARG it) -> {
          return ((State)((ARG)domainArgument));
        };
        acceptor.<State>add(this.stateNode, _function_1);
      }
    }
    if (!_matched) {
      if (domainArgument instanceof Transition) {
        _matched=true;
        final Function1<ARG, Statemachine> _function = (ARG it) -> {
          return EcoreUtil2.<Statemachine>getContainerOfType(((Transition)domainArgument), Statemachine.class);
        };
        acceptor.<Statemachine>add(this.statemachineDiagram, _function);
        final Function1<ARG, Transition> _function_1 = (ARG it) -> {
          return ((Transition)((ARG)domainArgument));
        };
        acceptor.<Transition>add(this.transitionConnection, _function_1);
      }
    }
  }
}
