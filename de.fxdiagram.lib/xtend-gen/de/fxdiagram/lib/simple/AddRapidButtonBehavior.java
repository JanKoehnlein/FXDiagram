package de.fxdiagram.lib.simple;

import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.behavior.Behavior;
import de.fxdiagram.core.command.AddRemoveCommand;
import de.fxdiagram.core.command.CommandStack;
import de.fxdiagram.core.extensions.ButtonExtensions;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.tools.AbstractChooser;
import de.fxdiagram.lib.buttons.RapidButton;
import de.fxdiagram.lib.buttons.RapidButtonAction;
import de.fxdiagram.lib.buttons.RapidButtonBehavior;
import de.fxdiagram.lib.simple.SimpleNode;
import de.fxdiagram.lib.tools.CarusselChooser;
import de.fxdiagram.lib.tools.CoverFlowChooser;
import de.fxdiagram.lib.tools.CubeChooser;
import javafx.geometry.Side;
import javafx.scene.shape.SVGPath;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class AddRapidButtonBehavior<T extends XNode> extends RapidButtonBehavior<T> {
  private Procedure1<? super AbstractChooser> choiceInitializer;
  
  public AddRapidButtonBehavior(final T host) {
    super(host);
  }
  
  public Class<? extends Behavior> getBehaviorKey() {
    return AddRapidButtonBehavior.class;
  }
  
  public Procedure1<? super AbstractChooser> setChoiceInitializer(final Procedure1<? super AbstractChooser> choiceInitializer) {
    return this.choiceInitializer = choiceInitializer;
  }
  
  public void doActivate() {
    T _host = this.getHost();
    final XNode host = ((XNode) _host);
    final RapidButtonAction _function = new RapidButtonAction() {
      @Override
      public void perform(final RapidButton button) {
        final SimpleNode target = new SimpleNode("New Node");
        final XNode source = button.getHost();
        final XConnection connection = new XConnection(source, target);
        double _layoutX = source.getLayoutX();
        target.setLayoutX(_layoutX);
        double _layoutY = source.getLayoutY();
        target.setLayoutY(_layoutY);
        Side _position = button.getPosition();
        if (_position != null) {
          switch (_position) {
            case TOP:
              double _layoutY_1 = target.getLayoutY();
              double _minus = (_layoutY_1 - 150);
              target.setLayoutY(_minus);
              break;
            case BOTTOM:
              double _layoutY_2 = target.getLayoutY();
              double _plus = (_layoutY_2 + 150);
              target.setLayoutY(_plus);
              break;
            case LEFT:
              double _layoutX_1 = target.getLayoutX();
              double _minus_1 = (_layoutX_1 - 200);
              target.setLayoutX(_minus_1);
              break;
            case RIGHT:
              double _layoutX_2 = target.getLayoutX();
              double _plus_1 = (_layoutX_2 + 200);
              target.setLayoutX(_plus_1);
              break;
            default:
              break;
          }
        }
        XRoot _root = CoreExtensions.getRoot(host);
        CommandStack _commandStack = _root.getCommandStack();
        XDiagram _diagram = CoreExtensions.getDiagram(host);
        AddRemoveCommand _newAddCommand = AddRemoveCommand.newAddCommand(_diagram, target, connection);
        _commandStack.execute(_newAddCommand);
      }
    };
    final RapidButtonAction addAction = _function;
    final RapidButtonAction _function_1 = new RapidButtonAction() {
      @Override
      public void perform(final RapidButton button) {
        Side _position = button.getPosition();
        final CarusselChooser chooser = new CarusselChooser(host, _position);
        AddRapidButtonBehavior.this.addChoices(chooser);
        XRoot _root = CoreExtensions.getRoot(host);
        _root.setCurrentTool(chooser);
      }
    };
    final RapidButtonAction chooseAction = _function_1;
    final RapidButtonAction _function_2 = new RapidButtonAction() {
      @Override
      public void perform(final RapidButton button) {
        Side _position = button.getPosition();
        final CubeChooser chooser = new CubeChooser(host, _position);
        AddRapidButtonBehavior.this.addChoices(chooser);
        XRoot _root = CoreExtensions.getRoot(host);
        _root.setCurrentTool(chooser);
      }
    };
    final RapidButtonAction cubeChooseAction = _function_2;
    final RapidButtonAction _function_3 = new RapidButtonAction() {
      @Override
      public void perform(final RapidButton button) {
        Side _position = button.getPosition();
        final CoverFlowChooser chooser = new CoverFlowChooser(host, _position);
        AddRapidButtonBehavior.this.addChoices(chooser);
        XRoot _root = CoreExtensions.getRoot(host);
        _root.setCurrentTool(chooser);
      }
    };
    final RapidButtonAction coverFlowChooseAction = _function_3;
    SVGPath _filledTriangle = ButtonExtensions.getFilledTriangle(Side.TOP, "Add node");
    RapidButton _rapidButton = new RapidButton(host, Side.TOP, _filledTriangle, cubeChooseAction);
    this.add(_rapidButton);
    SVGPath _filledTriangle_1 = ButtonExtensions.getFilledTriangle(Side.BOTTOM, "Add node");
    RapidButton _rapidButton_1 = new RapidButton(host, Side.BOTTOM, _filledTriangle_1, coverFlowChooseAction);
    this.add(_rapidButton_1);
    SVGPath _filledTriangle_2 = ButtonExtensions.getFilledTriangle(Side.LEFT, "Add node");
    RapidButton _rapidButton_2 = new RapidButton(host, Side.LEFT, _filledTriangle_2, chooseAction);
    this.add(_rapidButton_2);
    SVGPath _filledTriangle_3 = ButtonExtensions.getFilledTriangle(Side.RIGHT, "Add node");
    RapidButton _rapidButton_3 = new RapidButton(host, Side.RIGHT, _filledTriangle_3, addAction);
    this.add(_rapidButton_3);
  }
  
  protected void addChoices(final AbstractChooser chooser) {
    this.choiceInitializer.apply(chooser);
  }
}
