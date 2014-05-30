package de.fxdiagram.lib.actions;

import com.google.common.base.Objects;
import de.fxdiagram.core.HeadsUpDisplay;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.command.AnimationQueue;
import de.fxdiagram.core.command.CommandContext;
import de.fxdiagram.core.command.CommandStack;
import de.fxdiagram.core.extensions.ClassLoaderExtensions;
import de.fxdiagram.core.extensions.DurationExtensions;
import de.fxdiagram.core.tools.actions.DiagramAction;
import eu.hansolo.enzo.radialmenu.Symbol;
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
import javafx.util.Duration;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class UndoRedoPlayerAction implements DiagramAction {
  private XRoot root;
  
  private AnimationQueue.Listener animationQueueListener;
  
  private Node controlPanel;
  
  private FadeTransition fadeTransition;
  
  public boolean matches(final KeyEvent it) {
    boolean _and = false;
    boolean _isShortcutDown = it.isShortcutDown();
    if (!_isShortcutDown) {
      _and = false;
    } else {
      KeyCode _code = it.getCode();
      boolean _equals = Objects.equal(_code, KeyCode.P);
      _and = _equals;
    }
    return _and;
  }
  
  public Symbol.Type getSymbol() {
    return null;
  }
  
  public void perform(final XRoot root) {
    this.root = root;
    StackPane _createControlPanel = this.createControlPanel();
    this.controlPanel = _createControlPanel;
    HeadsUpDisplay _headsUpDisplay = root.getHeadsUpDisplay();
    _headsUpDisplay.add(this.controlPanel, Pos.BOTTOM_CENTER);
  }
  
  protected StackPane createControlPanel() {
    StackPane _stackPane = new StackPane();
    final Procedure1<StackPane> _function = new Procedure1<StackPane>() {
      public void apply(final StackPane it) {
        it.setAlignment(Pos.CENTER);
        ObservableList<Node> _children = it.getChildren();
        Rectangle _rectangle = new Rectangle();
        final Procedure1<Rectangle> _function = new Procedure1<Rectangle>() {
          public void apply(final Rectangle it) {
            it.setWidth(400);
            it.setHeight(60);
            it.setOpacity(0);
          }
        };
        Rectangle _doubleArrow = ObjectExtensions.<Rectangle>operator_doubleArrow(_rectangle, _function);
        _children.add(_doubleArrow);
        ObservableList<Node> _children_1 = it.getChildren();
        HBox _hBox = new HBox();
        final Procedure1<HBox> _function_1 = new Procedure1<HBox>() {
          public void apply(final HBox it) {
            it.setAlignment(Pos.CENTER);
            ObservableList<Node> _children = it.getChildren();
            Button _button = new Button();
            final Procedure1<Button> _function = new Procedure1<Button>() {
              public void apply(final Button it) {
                it.setId("back-button");
                it.setText("Back");
                final EventHandler<ActionEvent> _function = new EventHandler<ActionEvent>() {
                  public void handle(final ActionEvent it) {
                    UndoRedoPlayerAction.this.startFastMode(true);
                  }
                };
                it.setOnAction(_function);
              }
            };
            Button _doubleArrow = ObjectExtensions.<Button>operator_doubleArrow(_button, _function);
            _children.add(_doubleArrow);
            ObservableList<Node> _children_1 = it.getChildren();
            Button _button_1 = new Button();
            final Procedure1<Button> _function_1 = new Procedure1<Button>() {
              public void apply(final Button it) {
                it.setId("reverse-button");
                it.setText("undo");
                final EventHandler<ActionEvent> _function = new EventHandler<ActionEvent>() {
                  public void handle(final ActionEvent it) {
                    UndoRedoPlayerAction.this.stopFastMode();
                    CommandStack _commandStack = UndoRedoPlayerAction.this.root.getCommandStack();
                    _commandStack.undo();
                  }
                };
                it.setOnAction(_function);
              }
            };
            Button _doubleArrow_1 = ObjectExtensions.<Button>operator_doubleArrow(_button_1, _function_1);
            _children_1.add(_doubleArrow_1);
            ObservableList<Node> _children_2 = it.getChildren();
            Button _button_2 = new Button();
            final Procedure1<Button> _function_2 = new Procedure1<Button>() {
              public void apply(final Button it) {
                it.setId("pause-button");
                it.setText("pause");
                final EventHandler<ActionEvent> _function = new EventHandler<ActionEvent>() {
                  public void handle(final ActionEvent it) {
                    UndoRedoPlayerAction.this.stopFastMode();
                  }
                };
                it.setOnAction(_function);
              }
            };
            Button _doubleArrow_2 = ObjectExtensions.<Button>operator_doubleArrow(_button_2, _function_2);
            _children_2.add(_doubleArrow_2);
            ObservableList<Node> _children_3 = it.getChildren();
            Button _button_3 = new Button();
            final Procedure1<Button> _function_3 = new Procedure1<Button>() {
              public void apply(final Button it) {
                it.setId("play-button");
                it.setText("redo");
                final EventHandler<ActionEvent> _function = new EventHandler<ActionEvent>() {
                  public void handle(final ActionEvent it) {
                    UndoRedoPlayerAction.this.stopFastMode();
                    CommandStack _commandStack = UndoRedoPlayerAction.this.root.getCommandStack();
                    _commandStack.redo();
                  }
                };
                it.setOnAction(_function);
              }
            };
            Button _doubleArrow_3 = ObjectExtensions.<Button>operator_doubleArrow(_button_3, _function_3);
            _children_3.add(_doubleArrow_3);
            ObservableList<Node> _children_4 = it.getChildren();
            Button _button_4 = new Button();
            final Procedure1<Button> _function_4 = new Procedure1<Button>() {
              public void apply(final Button it) {
                it.setId("forward-button");
                it.setText("forward");
                final EventHandler<ActionEvent> _function = new EventHandler<ActionEvent>() {
                  public void handle(final ActionEvent it) {
                    UndoRedoPlayerAction.this.startFastMode(false);
                  }
                };
                it.setOnAction(_function);
              }
            };
            Button _doubleArrow_4 = ObjectExtensions.<Button>operator_doubleArrow(_button_4, _function_4);
            _children_4.add(_doubleArrow_4);
            final EventHandler<MouseEvent> _function_5 = new EventHandler<MouseEvent>() {
              public void handle(final MouseEvent it) {
                UndoRedoPlayerAction.this.fade();
              }
            };
            it.setOnMouseExited(_function_5);
            final EventHandler<MouseEvent> _function_6 = new EventHandler<MouseEvent>() {
              public void handle(final MouseEvent it) {
                UndoRedoPlayerAction.this.show();
              }
            };
            it.setOnMouseEntered(_function_6);
          }
        };
        HBox _doubleArrow_1 = ObjectExtensions.<HBox>operator_doubleArrow(_hBox, _function_1);
        _children_1.add(_doubleArrow_1);
        ObservableList<String> _stylesheets = it.getStylesheets();
        String _uRI = ClassLoaderExtensions.toURI(UndoRedoPlayerAction.this, "../media/MovieNode.css");
        _stylesheets.add(_uRI);
      }
    };
    return ObjectExtensions.<StackPane>operator_doubleArrow(_stackPane, _function);
  }
  
  protected void show() {
    boolean _notEquals = (!Objects.equal(this.fadeTransition, null));
    if (_notEquals) {
      this.fadeTransition.stop();
    }
    this.controlPanel.setOpacity(1);
  }
  
  protected FadeTransition fade() {
    FadeTransition _xblockexpression = null;
    {
      this.stopFastMode();
      FadeTransition _fadeTransition = new FadeTransition();
      final Procedure1<FadeTransition> _function = new Procedure1<FadeTransition>() {
        public void apply(final FadeTransition it) {
          it.setNode(UndoRedoPlayerAction.this.controlPanel);
          Duration _seconds = DurationExtensions.seconds(1);
          it.setDuration(_seconds);
          it.setFromValue(1);
          it.setToValue(0);
          final EventHandler<ActionEvent> _function = new EventHandler<ActionEvent>() {
            public void handle(final ActionEvent it) {
              HeadsUpDisplay _headsUpDisplay = UndoRedoPlayerAction.this.root.getHeadsUpDisplay();
              ObservableList<Node> _children = _headsUpDisplay.getChildren();
              _children.remove(UndoRedoPlayerAction.this.controlPanel);
            }
          };
          it.setOnFinished(_function);
          it.play();
        }
      };
      FadeTransition _doubleArrow = ObjectExtensions.<FadeTransition>operator_doubleArrow(_fadeTransition, _function);
      _xblockexpression = this.fadeTransition = _doubleArrow;
    }
    return _xblockexpression;
  }
  
  protected void stopFastMode() {
    boolean _notEquals = (!Objects.equal(this.animationQueueListener, null));
    if (_notEquals) {
      CommandStack _commandStack = this.root.getCommandStack();
      CommandContext _context = _commandStack.getContext();
      AnimationQueue _animationQueue = _context.getAnimationQueue();
      _animationQueue.removeListener(this.animationQueueListener);
    }
  }
  
  protected void startFastMode(final boolean isUndo) {
    this.stopFastMode();
    final CommandStack commandStack = this.root.getCommandStack();
    final AnimationQueue.Listener _function = new AnimationQueue.Listener() {
      public void handleQueueEmpty() {
        boolean _and = false;
        if (!isUndo) {
          _and = false;
        } else {
          boolean _canUndo = commandStack.canUndo();
          _and = _canUndo;
        }
        if (_and) {
          commandStack.undo();
        } else {
          boolean _and_1 = false;
          if (!(!isUndo)) {
            _and_1 = false;
          } else {
            boolean _canRedo = commandStack.canRedo();
            _and_1 = _canRedo;
          }
          if (_and_1) {
            commandStack.redo();
          } else {
            UndoRedoPlayerAction.this.stopFastMode();
          }
        }
      }
    };
    this.animationQueueListener = _function;
    CommandStack _commandStack = this.root.getCommandStack();
    CommandContext _context = _commandStack.getContext();
    AnimationQueue _animationQueue = _context.getAnimationQueue();
    _animationQueue.addListener(this.animationQueueListener);
    if (isUndo) {
      commandStack.undo();
    } else {
      commandStack.redo();
    }
  }
}
