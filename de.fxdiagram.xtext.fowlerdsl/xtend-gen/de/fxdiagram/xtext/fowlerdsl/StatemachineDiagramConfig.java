package de.fxdiagram.xtext.fowlerdsl;

import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XConnectionLabel;
import de.fxdiagram.xtext.glue.XtextDomainObjectDescriptor;
import de.fxdiagram.xtext.glue.mapping.AbstractDiagramConfig;
import de.fxdiagram.xtext.glue.mapping.ConnectionMapping;
import de.fxdiagram.xtext.glue.mapping.DiagramMapping;
import de.fxdiagram.xtext.glue.mapping.NodeMapping;
import javafx.scene.text.Text;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.example.fowlerdsl.statemachine.Event;
import org.eclipse.xtext.example.fowlerdsl.statemachine.State;
import org.eclipse.xtext.example.fowlerdsl.statemachine.Statemachine;
import org.eclipse.xtext.example.fowlerdsl.statemachine.Transition;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class StatemachineDiagramConfig extends AbstractDiagramConfig {
  public StatemachineDiagramConfig() {
    final DiagramMapping<Statemachine> statemachineDiagram = new DiagramMapping<Statemachine>(Statemachine.class);
    final NodeMapping<State> stateNode = new NodeMapping<State>(State.class);
    ConnectionMapping<Transition> _connectionMapping = new ConnectionMapping<Transition>(Transition.class);
    final Procedure1<ConnectionMapping<Transition>> _function = new Procedure1<ConnectionMapping<Transition>>() {
      public void apply(final ConnectionMapping<Transition> it) {
        final Function1<XtextDomainObjectDescriptor<Transition>, XConnection> _function = new Function1<XtextDomainObjectDescriptor<Transition>, XConnection>() {
          public XConnection apply(final XtextDomainObjectDescriptor<Transition> descriptor) {
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
        };
        it.setCreateConnection(_function);
      }
    };
    final ConnectionMapping<Transition> transitionConnection = ObjectExtensions.<ConnectionMapping<Transition>>operator_doubleArrow(_connectionMapping, _function);
    final Procedure1<DiagramMapping<Statemachine>> _function_1 = new Procedure1<DiagramMapping<Statemachine>>() {
      public void apply(final DiagramMapping<Statemachine> it) {
        final Function1<Statemachine, EList<State>> _function = new Function1<Statemachine, EList<State>>() {
          public EList<State> apply(final Statemachine it) {
            return it.getStates();
          }
        };
        it.<State>nodeForEach(stateNode, _function);
      }
    };
    DiagramMapping<Statemachine> _doubleArrow = ObjectExtensions.<DiagramMapping<Statemachine>>operator_doubleArrow(statemachineDiagram, _function_1);
    this.addMapping(_doubleArrow);
    final Procedure1<NodeMapping<State>> _function_2 = new Procedure1<NodeMapping<State>>() {
      public void apply(final NodeMapping<State> it) {
        final Function1<State, EList<Transition>> _function = new Function1<State, EList<Transition>>() {
          public EList<Transition> apply(final State it) {
            return it.getTransitions();
          }
        };
        it.<Transition>outConnectionForEach(transitionConnection, _function);
      }
    };
    NodeMapping<State> _doubleArrow_1 = ObjectExtensions.<NodeMapping<State>>operator_doubleArrow(stateNode, _function_2);
    this.addMapping(_doubleArrow_1);
    final Procedure1<ConnectionMapping<Transition>> _function_3 = new Procedure1<ConnectionMapping<Transition>>() {
      public void apply(final ConnectionMapping<Transition> it) {
        final Function1<Transition, State> _function = new Function1<Transition, State>() {
          public State apply(final Transition it) {
            return it.getState();
          }
        };
        it.<State>target(stateNode, _function);
      }
    };
    ConnectionMapping<Transition> _doubleArrow_2 = ObjectExtensions.<ConnectionMapping<Transition>>operator_doubleArrow(transitionConnection, _function_3);
    this.addMapping(_doubleArrow_2);
  }
}
