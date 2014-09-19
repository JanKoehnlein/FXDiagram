package de.fxdiagram.xtext.fowlerdsl;

import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XConnectionLabel;
import de.fxdiagram.xtext.glue.mapping.AbstractDiagramConfig;
import de.fxdiagram.xtext.glue.mapping.AbstractXtextDescriptor;
import de.fxdiagram.xtext.glue.mapping.ConnectionMapping;
import de.fxdiagram.xtext.glue.mapping.DiagramMapping;
import de.fxdiagram.xtext.glue.mapping.MappingAcceptor;
import de.fxdiagram.xtext.glue.mapping.NodeMapping;
import javafx.scene.text.Text;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.example.fowlerdsl.statemachine.Event;
import org.eclipse.xtext.example.fowlerdsl.statemachine.State;
import org.eclipse.xtext.example.fowlerdsl.statemachine.Statemachine;
import org.eclipse.xtext.example.fowlerdsl.statemachine.Transition;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class StatemachineDiagramConfig extends AbstractDiagramConfig {
  private final DiagramMapping<Statemachine> statemachineDiagram = new DiagramMapping<Statemachine>(this, "statemachineDiagram") {
    public void calls() {
      final Function1<Statemachine, EList<State>> _function = new Function1<Statemachine, EList<State>>() {
        public EList<State> apply(final Statemachine it) {
          return it.getStates();
        }
      };
      this.<State>nodeForEach(StatemachineDiagramConfig.this.stateNode, _function);
    }
  };
  
  private final NodeMapping<State> stateNode = new NodeMapping<State>(this, "stateNode") {
    public void calls() {
      final Function1<State, EList<Transition>> _function = new Function1<State, EList<Transition>>() {
        public EList<Transition> apply(final State it) {
          return it.getTransitions();
        }
      };
      this.<Transition>outConnectionForEach(StatemachineDiagramConfig.this.transitionConnection, _function);
    }
  };
  
  private final ConnectionMapping<Transition> transitionConnection = new ConnectionMapping<Transition>(this, "transitionConnection") {
    public XConnection createConnection(final AbstractXtextDescriptor<Transition> descriptor) {
      XConnection _xConnection = new XConnection(descriptor);
      final Procedure1<XConnection> _function = new Procedure1<XConnection>() {
        public void apply(final XConnection it) {
          XConnectionLabel _xConnectionLabel = new XConnectionLabel(it);
          final Procedure1<XConnectionLabel> _function = new Procedure1<XConnectionLabel>() {
            public void apply(final XConnectionLabel label) {
              Text _text = label.getText();
              final Function1<Transition, String> _function = new Function1<Transition, String>() {
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
    
    public void calls() {
      final Function1<Transition, State> _function = new Function1<Transition, State>() {
        public State apply(final Transition it) {
          return it.getState();
        }
      };
      this.<State>target(StatemachineDiagramConfig.this.stateNode, _function);
    }
  };
  
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
