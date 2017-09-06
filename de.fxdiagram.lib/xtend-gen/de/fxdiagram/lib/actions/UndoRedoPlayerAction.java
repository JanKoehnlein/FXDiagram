package de.fxdiagram.lib.actions;

import com.google.common.base.Objects;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.command.AnimationQueue;
import de.fxdiagram.core.command.CommandStack;
import de.fxdiagram.core.extensions.ClassLoaderExtensions;
import de.fxdiagram.core.extensions.DurationExtensions;
import de.fxdiagram.core.tools.actions.DiagramAction;
import eu.hansolo.enzo.radialmenu.SymbolType;
import javafx.animation.FadeTransition;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

/**
 * An action adding a media control to rewind/replay changes from the undo stack.
 */
@SuppressWarnings("all")
public class UndoRedoPlayerAction implements DiagramAction {
  private XRoot root;
  
  private AnimationQueue.Listener animationQueueListener;
  
  private Node controlPanel;
  
  private FadeTransition fadeTransition;
  
  @Override
  public boolean matches(final KeyEvent it) {
    return (it.isShortcutDown() && Objects.equal(it.getCode(), KeyCode.P));
  }
  
  @Override
  public SymbolType getSymbol() {
    return SymbolType.PLAY;
  }
  
  @Override
  public String getTooltip() {
    return "Undo player";
  }
  
  @Override
  public void perform(final XRoot root) {
    this.root = root;
    this.controlPanel = this.createControlPanel();
    root.getHeadsUpDisplay().add(this.controlPanel, Pos.BOTTOM_CENTER);
  }
  
  protected StackPane createControlPanel() {
    StackPane _stackPane = new StackPane();
    final Procedure1<StackPane> _function = (StackPane it) -> {
      it.setAlignment(Pos.CENTER);
      ObservableList<Node> _children = it.getChildren();
      Rectangle _rectangle = new Rectangle();
      final Procedure1<Rectangle> _function_1 = (Rectangle it_1) -> {
        it_1.setWidth(400);
        it_1.setHeight(60);
        it_1.setOpacity(0);
      };
      Rectangle _doubleArrow = ObjectExtensions.<Rectangle>operator_doubleArrow(_rectangle, _function_1);
      _children.add(_doubleArrow);
      ObservableList<Node> _children_1 = it.getChildren();
      HBox _hBox = new HBox();
      final Procedure1<HBox> _function_2 = (HBox it_1) -> {
        it_1.setAlignment(Pos.CENTER);
        ObservableList<Node> _children_2 = it_1.getChildren();
        Button _button = new Button();
        final Procedure1<Button> _function_3 = (Button it_2) -> {
          it_2.setId("back-button");
          it_2.setText("Back");
          final EventHandler<ActionEvent> _function_4 = (ActionEvent it_3) -> {
            this.startFastMode(true);
          };
          it_2.setOnAction(_function_4);
        };
        Button _doubleArrow_1 = ObjectExtensions.<Button>operator_doubleArrow(_button, _function_3);
        _children_2.add(_doubleArrow_1);
        ObservableList<Node> _children_3 = it_1.getChildren();
        Button _button_1 = new Button();
        final Procedure1<Button> _function_4 = (Button it_2) -> {
          it_2.setId("reverse-button");
          it_2.setText("undo");
          final EventHandler<ActionEvent> _function_5 = (ActionEvent it_3) -> {
            this.stopFastMode();
            this.root.getCommandStack().undo();
          };
          it_2.setOnAction(_function_5);
        };
        Button _doubleArrow_2 = ObjectExtensions.<Button>operator_doubleArrow(_button_1, _function_4);
        _children_3.add(_doubleArrow_2);
        ObservableList<Node> _children_4 = it_1.getChildren();
        Button _button_2 = new Button();
        final Procedure1<Button> _function_5 = (Button it_2) -> {
          it_2.setId("pause-button");
          it_2.setText("pause");
          final EventHandler<ActionEvent> _function_6 = (ActionEvent it_3) -> {
            this.stopFastMode();
          };
          it_2.setOnAction(_function_6);
        };
        Button _doubleArrow_3 = ObjectExtensions.<Button>operator_doubleArrow(_button_2, _function_5);
        _children_4.add(_doubleArrow_3);
        ObservableList<Node> _children_5 = it_1.getChildren();
        Button _button_3 = new Button();
        final Procedure1<Button> _function_6 = (Button it_2) -> {
          it_2.setId("play-button");
          it_2.setText("redo");
          final EventHandler<ActionEvent> _function_7 = (ActionEvent it_3) -> {
            this.stopFastMode();
            this.root.getCommandStack().redo();
          };
          it_2.setOnAction(_function_7);
        };
        Button _doubleArrow_4 = ObjectExtensions.<Button>operator_doubleArrow(_button_3, _function_6);
        _children_5.add(_doubleArrow_4);
        ObservableList<Node> _children_6 = it_1.getChildren();
        Button _button_4 = new Button();
        final Procedure1<Button> _function_7 = (Button it_2) -> {
          it_2.setId("forward-button");
          it_2.setText("forward");
          final EventHandler<ActionEvent> _function_8 = (ActionEvent it_3) -> {
            this.startFastMode(false);
          };
          it_2.setOnAction(_function_8);
        };
        Button _doubleArrow_5 = ObjectExtensions.<Button>operator_doubleArrow(_button_4, _function_7);
        _children_6.add(_doubleArrow_5);
        final EventHandler<MouseEvent> _function_8 = (MouseEvent it_2) -> {
          this.fade();
        };
        it_1.setOnMouseExited(_function_8);
        final EventHandler<MouseEvent> _function_9 = (MouseEvent it_2) -> {
          this.show();
        };
        it_1.setOnMouseEntered(_function_9);
      };
      HBox _doubleArrow_1 = ObjectExtensions.<HBox>operator_doubleArrow(_hBox, _function_2);
      _children_1.add(_doubleArrow_1);
      ObservableList<String> _stylesheets = it.getStylesheets();
      String _uRI = ClassLoaderExtensions.toURI(this, "../media/MovieNode.css");
      _stylesheets.add(_uRI);
    };
    return ObjectExtensions.<StackPane>operator_doubleArrow(_stackPane, _function);
  }
  
  protected void show() {
    if ((this.fadeTransition != null)) {
      this.fadeTransition.stop();
    }
    this.controlPanel.setOpacity(1);
  }
  
  protected FadeTransition fade() {
    FadeTransition _xblockexpression = null;
    {
      this.stopFastMode();
      FadeTransition _fadeTransition = new FadeTransition();
      final Procedure1<FadeTransition> _function = (FadeTransition it) -> {
        it.setNode(this.controlPanel);
        it.setDuration(DurationExtensions.seconds(1));
        it.setFromValue(1);
        it.setToValue(0);
        final EventHandler<ActionEvent> _function_1 = (ActionEvent it_1) -> {
          ObservableList<Node> _children = this.root.getHeadsUpDisplay().getChildren();
          _children.remove(this.controlPanel);
        };
        it.setOnFinished(_function_1);
        it.play();
      };
      FadeTransition _doubleArrow = ObjectExtensions.<FadeTransition>operator_doubleArrow(_fadeTransition, _function);
      _xblockexpression = this.fadeTransition = _doubleArrow;
    }
    return _xblockexpression;
  }
  
  protected void stopFastMode() {
    if ((this.animationQueueListener != null)) {
      this.root.getCommandStack().getContext().getAnimationQueue().removeListener(this.animationQueueListener);
    }
  }
  
  protected void startFastMode(final boolean isUndo) {
    this.stopFastMode();
    final CommandStack commandStack = this.root.getCommandStack();
    final AnimationQueue.Listener _function = () -> {
      if ((isUndo && commandStack.canUndo())) {
        commandStack.undo();
      } else {
        if (((!isUndo) && commandStack.canRedo())) {
          commandStack.redo();
        } else {
          this.stopFastMode();
        }
      }
    };
    this.animationQueueListener = _function;
    this.root.getCommandStack().getContext().getAnimationQueue().addListener(this.animationQueueListener);
    if (isUndo) {
      commandStack.undo();
    } else {
      commandStack.redo();
    }
  }
}
