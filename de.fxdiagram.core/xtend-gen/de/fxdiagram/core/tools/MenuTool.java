package de.fxdiagram.core.tools;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import de.fxdiagram.annotations.logging.Logging;
import de.fxdiagram.core.HeadsUpDisplay;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.layout.LayoutType;
import de.fxdiagram.core.tools.XDiagramTool;
import de.fxdiagram.core.tools.actions.CenterAction;
import de.fxdiagram.core.tools.actions.CloseAction;
import de.fxdiagram.core.tools.actions.DeleteAction;
import de.fxdiagram.core.tools.actions.DiagramAction;
import de.fxdiagram.core.tools.actions.ExitAction;
import de.fxdiagram.core.tools.actions.ExportSvgAction;
import de.fxdiagram.core.tools.actions.LayoutAction;
import de.fxdiagram.core.tools.actions.LoadAction;
import de.fxdiagram.core.tools.actions.NavigateNextAction;
import de.fxdiagram.core.tools.actions.NavigatePreviousAction;
import de.fxdiagram.core.tools.actions.OpenAction;
import de.fxdiagram.core.tools.actions.RedoAction;
import de.fxdiagram.core.tools.actions.SaveAction;
import de.fxdiagram.core.tools.actions.SelectAllAction;
import de.fxdiagram.core.tools.actions.UndoAction;
import de.fxdiagram.core.tools.actions.UndoRedoPlayerAction;
import de.fxdiagram.core.tools.actions.ZoomToFitAction;
import eu.hansolo.enzo.radialmenu.MenuItem;
import eu.hansolo.enzo.radialmenu.Options;
import eu.hansolo.enzo.radialmenu.RadialMenu;
import eu.hansolo.enzo.radialmenu.Symbol;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@Logging
@SuppressWarnings("all")
public class MenuTool implements XDiagramTool {
  private XRoot root;
  
  private EventHandler<KeyEvent> keyHandler;
  
  private EventHandler<MouseEvent> mouseHandler;
  
  private Group menuGroup;
  
  private RadialMenu menu;
  
  private MenuItem selection;
  
  public MenuTool(final XRoot root) {
    this.root = root;
    final EventHandler<KeyEvent> _function = new EventHandler<KeyEvent>() {
      public void handle(final KeyEvent it) {
        DiagramAction _switchResult = null;
        KeyCode _code = it.getCode();
        if (_code != null) {
          switch (_code) {
            case A:
              SelectAllAction _xifexpression = null;
              boolean _isShortcutDown = it.isShortcutDown();
              if (_isShortcutDown) {
                SelectAllAction _xblockexpression = null;
                {
                  it.consume();
                  _xblockexpression = new SelectAllAction();
                }
                _xifexpression = _xblockexpression;
              }
              _switchResult = _xifexpression;
              break;
            case C:
              CenterAction _xifexpression_1 = null;
              boolean _isShortcutDown_1 = it.isShortcutDown();
              if (_isShortcutDown_1) {
                CenterAction _xblockexpression_1 = null;
                {
                  it.consume();
                  _xblockexpression_1 = new CenterAction();
                }
                _xifexpression_1 = _xblockexpression_1;
              }
              _switchResult = _xifexpression_1;
              break;
            case E:
              ExportSvgAction _xifexpression_2 = null;
              boolean _isShortcutDown_2 = it.isShortcutDown();
              if (_isShortcutDown_2) {
                ExportSvgAction _xblockexpression_2 = null;
                {
                  it.consume();
                  _xblockexpression_2 = new ExportSvgAction();
                }
                _xifexpression_2 = _xblockexpression_2;
              }
              _switchResult = _xifexpression_2;
              break;
            case F:
              ZoomToFitAction _xifexpression_3 = null;
              boolean _isShortcutDown_3 = it.isShortcutDown();
              if (_isShortcutDown_3) {
                ZoomToFitAction _xblockexpression_3 = null;
                {
                  it.consume();
                  boolean _isShiftDown = it.isShiftDown();
                  if (_isShiftDown) {
                    Scene _scene = root.getScene();
                    Window _window = _scene.getWindow();
                    final Window window = _window;
                    boolean _matched = false;
                    if (!_matched) {
                      if (window instanceof Stage) {
                        _matched=true;
                        ((Stage)window).setFullScreen(true);
                        return;
                      }
                    }
                  }
                  _xblockexpression_3 = new ZoomToFitAction();
                }
                _xifexpression_3 = _xblockexpression_3;
              }
              _switchResult = _xifexpression_3;
              break;
            case L:
              LayoutAction _xifexpression_4 = null;
              boolean _isShortcutDown_4 = it.isShortcutDown();
              if (_isShortcutDown_4) {
                LayoutAction _xblockexpression_4 = null;
                {
                  it.consume();
                  LayoutType _xifexpression_5 = null;
                  boolean _isShiftDown = it.isShiftDown();
                  if (_isShiftDown) {
                    _xifexpression_5 = LayoutType.NEATO;
                  } else {
                    _xifexpression_5 = LayoutType.DOT;
                  }
                  _xblockexpression_4 = new LayoutAction(_xifexpression_5);
                }
                _xifexpression_4 = _xblockexpression_4;
              }
              _switchResult = _xifexpression_4;
              break;
            case O:
              LoadAction _xifexpression_5 = null;
              boolean _isShortcutDown_5 = it.isShortcutDown();
              if (_isShortcutDown_5) {
                _xifexpression_5 = new LoadAction();
              }
              _switchResult = _xifexpression_5;
              break;
            case P:
              UndoRedoPlayerAction _xifexpression_6 = null;
              boolean _isShortcutDown_6 = it.isShortcutDown();
              if (_isShortcutDown_6) {
                _xifexpression_6 = new UndoRedoPlayerAction();
              }
              _switchResult = _xifexpression_6;
              break;
            case Q:
              ExitAction _xifexpression_7 = null;
              boolean _isShortcutDown_7 = it.isShortcutDown();
              if (_isShortcutDown_7) {
                _xifexpression_7 = new ExitAction();
              }
              _switchResult = _xifexpression_7;
              break;
            case S:
              SaveAction _xifexpression_8 = null;
              boolean _isShortcutDown_8 = it.isShortcutDown();
              if (_isShortcutDown_8) {
                _xifexpression_8 = new SaveAction();
              }
              _switchResult = _xifexpression_8;
              break;
            case Y:
              DiagramAction _xifexpression_9 = null;
              boolean _isShortcutDown_9 = it.isShortcutDown();
              if (_isShortcutDown_9) {
                DiagramAction _xifexpression_10 = null;
                boolean _isShiftDown = it.isShiftDown();
                if (_isShiftDown) {
                  _xifexpression_10 = new RedoAction();
                } else {
                  _xifexpression_10 = new UndoAction();
                }
                _xifexpression_9 = _xifexpression_10;
              }
              _switchResult = _xifexpression_9;
              break;
            case BACK_SPACE:
              _switchResult = new DeleteAction();
              break;
            case DELETE:
              _switchResult = new DeleteAction();
              break;
            case RIGHT:
              _switchResult = new NavigateNextAction();
              break;
            case LEFT:
              _switchResult = new NavigatePreviousAction();
              break;
            case PAGE_DOWN:
              _switchResult = new NavigateNextAction();
              break;
            case PAGE_UP:
              _switchResult = new NavigatePreviousAction();
              break;
            case ENTER:
              _switchResult = new OpenAction();
              break;
            case PERIOD:
              _switchResult = new OpenAction();
              break;
            case ESCAPE:
              CloseAction _xblockexpression_5 = null;
              {
                it.consume();
                CloseAction _xifexpression_11 = null;
                RadialMenu.State _state = MenuTool.this.menu.getState();
                boolean _equals = Objects.equal(_state, RadialMenu.State.OPENED);
                if (_equals) {
                  MenuTool.this.closeMenu();
                  return;
                } else {
                  _xifexpression_11 = new CloseAction();
                }
                _xblockexpression_5 = _xifexpression_11;
              }
              _switchResult = _xblockexpression_5;
              break;
            default:
              _switchResult = null;
              break;
          }
        } else {
          _switchResult = null;
        }
        final DiagramAction action = _switchResult;
        if (action!=null) {
          action.perform(root);
        }
      }
    };
    this.keyHandler = _function;
    Options _options = new Options();
    final Procedure1<Options> _function_1 = new Procedure1<Options>() {
      public void apply(final Options it) {
        it.setDegrees(360);
        it.setOffset((-90));
        it.setRadius(200);
        it.setButtonSize(72);
        it.setButtonAlpha(1.0);
      }
    };
    Options _doubleArrow = ObjectExtensions.<Options>operator_doubleArrow(_options, _function_1);
    final Function1<Symbol.Type,MenuItem> _function_2 = new Function1<Symbol.Type,MenuItem>() {
      public MenuItem apply(final Symbol.Type s) {
        MenuItem _menuItem = new MenuItem();
        final Procedure1<MenuItem> _function = new Procedure1<MenuItem>() {
          public void apply(final MenuItem it) {
            it.setSymbol(s);
            it.setSize(64);
          }
        };
        return ObjectExtensions.<MenuItem>operator_doubleArrow(_menuItem, _function);
      }
    };
    List<MenuItem> _map = ListExtensions.<Symbol.Type, MenuItem>map(Collections.<Symbol.Type>unmodifiableList(Lists.<Symbol.Type>newArrayList(Symbol.Type.EJECT, Symbol.Type.GRAPH, Symbol.Type.CAMERA, Symbol.Type.SELECTION1, Symbol.Type.SELECTION2, Symbol.Type.ZOOM_IN, Symbol.Type.ROCKET, Symbol.Type.CLOUD, Symbol.Type.TRASH, Symbol.Type.FORWARD, Symbol.Type.REWIND)), _function_2);
    RadialMenu _radialMenu = new RadialMenu(_doubleArrow, _map);
    this.menu = _radialMenu;
    final EventHandler<MouseEvent> _function_3 = new EventHandler<MouseEvent>() {
      public void handle(final MouseEvent it) {
        MouseButton _button = it.getButton();
        boolean _equals = Objects.equal(_button, MouseButton.SECONDARY);
        if (_equals) {
          RadialMenu.State _state = MenuTool.this.menu.getState();
          boolean _equals_1 = Objects.equal(_state, RadialMenu.State.OPENED);
          if (_equals_1) {
            MenuTool.this.closeMenu();
            it.consume();
          } else {
            boolean _and = false;
            EventTarget _target = it.getTarget();
            Pane _diagramCanvas = root.getDiagramCanvas();
            boolean _equals_2 = Objects.equal(_target, _diagramCanvas);
            if (!_equals_2) {
              _and = false;
            } else {
              RadialMenu.State _state_1 = MenuTool.this.menu.getState();
              boolean _notEquals = (!Objects.equal(_state_1, RadialMenu.State.OPENED));
              _and = _notEquals;
            }
            if (_and) {
              MenuTool.this.openMenu();
              it.consume();
            }
          }
        }
      }
    };
    this.mouseHandler = _function_3;
  }
  
  protected boolean openMenu() {
    boolean _xblockexpression = false;
    {
      Group _group = new Group();
      this.menuGroup = _group;
      HeadsUpDisplay _headsUpDisplay = this.root.getHeadsUpDisplay();
      ObservableList<Node> _children = _headsUpDisplay.getChildren();
      final Procedure1<Group> _function = new Procedure1<Group>() {
        public void apply(final Group it) {
          BorderPane.setAlignment(it, Pos.CENTER);
          Scene _scene = MenuTool.this.root.getScene();
          double _width = _scene.getWidth();
          double _multiply = (0.5 * _width);
          it.setTranslateX(_multiply);
          Scene _scene_1 = MenuTool.this.root.getScene();
          double _height = _scene_1.getHeight();
          double _multiply_1 = (0.5 * _height);
          it.setTranslateY(_multiply_1);
          ObservableList<Node> _children = it.getChildren();
          final Procedure1<RadialMenu> _function = new Procedure1<RadialMenu>() {
            public void apply(final RadialMenu it) {
              it.show();
              it.open();
              final EventHandler<RadialMenu.ItemEvent> _function = new EventHandler<RadialMenu.ItemEvent>() {
                public void handle(final RadialMenu.ItemEvent it) {
                  MenuItem _item = it.getItem();
                  MenuTool.this.selection = _item;
                }
              };
              it.setOnItemSelected(_function);
              final EventHandler<RadialMenu.MenuEvent> _function_1 = new EventHandler<RadialMenu.MenuEvent>() {
                public void handle(final RadialMenu.MenuEvent it) {
                  MenuTool.this.closeMenu();
                  boolean _notEquals = (!Objects.equal(MenuTool.this.selection, null));
                  if (_notEquals) {
                    DiagramAction _switchResult = null;
                    Symbol.Type _symbol = MenuTool.this.selection.getSymbol();
                    if (_symbol != null) {
                      switch (_symbol) {
                        case CAMERA:
                          _switchResult = new ExportSvgAction();
                          break;
                        case CLOUD:
                          _switchResult = new LoadAction();
                          break;
                        case EJECT:
                          _switchResult = new ExitAction();
                          break;
                        case FORWARD:
                          _switchResult = new RedoAction();
                          break;
                        case GRAPH:
                          _switchResult = new LayoutAction(LayoutType.DOT);
                          break;
                        case REWIND:
                          _switchResult = new UndoAction();
                          break;
                        case ROCKET:
                          _switchResult = new SaveAction();
                          break;
                        case SELECTION1:
                          _switchResult = new SelectAllAction();
                          break;
                        case SELECTION2:
                          _switchResult = new CenterAction();
                          break;
                        case TRASH:
                          _switchResult = new DeleteAction();
                          break;
                        case ZOOM_IN:
                          _switchResult = new ZoomToFitAction();
                          break;
                        default:
                          DiagramAction _xblockexpression = null;
                          {
                            MenuTool.LOG.warning(("Unhandled menu item " + MenuTool.this.selection));
                            _xblockexpression = null;
                          }
                          _switchResult = _xblockexpression;
                          break;
                      }
                    } else {
                      DiagramAction _xblockexpression = null;
                      {
                        MenuTool.LOG.warning(("Unhandled menu item " + MenuTool.this.selection));
                        _xblockexpression = null;
                      }
                      _switchResult = _xblockexpression;
                    }
                    final DiagramAction action = _switchResult;
                    if (action!=null) {
                      action.perform(MenuTool.this.root);
                    }
                  }
                  MenuTool.this.selection = null;
                }
              };
              it.setOnMenuCloseFinished(_function_1);
            }
          };
          RadialMenu _doubleArrow = ObjectExtensions.<RadialMenu>operator_doubleArrow(MenuTool.this.menu, _function);
          _children.add(_doubleArrow);
        }
      };
      Group _doubleArrow = ObjectExtensions.<Group>operator_doubleArrow(this.menuGroup, _function);
      _xblockexpression = _children.add(_doubleArrow);
    }
    return _xblockexpression;
  }
  
  protected boolean closeMenu() {
    boolean _xblockexpression = false;
    {
      this.menu.hide();
      boolean _xifexpression = false;
      boolean _and = false;
      boolean _notEquals = (!Objects.equal(this.menuGroup, null));
      if (!_notEquals) {
        _and = false;
      } else {
        Parent _parent = this.menuGroup.getParent();
        boolean _notEquals_1 = (!Objects.equal(_parent, null));
        _and = _notEquals_1;
      }
      if (_and) {
        HeadsUpDisplay _headsUpDisplay = this.root.getHeadsUpDisplay();
        ObservableList<Node> _children = _headsUpDisplay.getChildren();
        _xifexpression = _children.remove(this.menuGroup);
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  public boolean activate() {
    boolean _xblockexpression = false;
    {
      Scene _scene = this.root.getScene();
      _scene.<KeyEvent>addEventHandler(KeyEvent.KEY_PRESSED, this.keyHandler);
      Pane _diagramCanvas = this.root.getDiagramCanvas();
      _diagramCanvas.<MouseEvent>addEventHandler(MouseEvent.MOUSE_PRESSED, this.mouseHandler);
      _xblockexpression = true;
    }
    return _xblockexpression;
  }
  
  public boolean deactivate() {
    boolean _xblockexpression = false;
    {
      Pane _diagramCanvas = this.root.getDiagramCanvas();
      _diagramCanvas.<MouseEvent>removeEventHandler(MouseEvent.MOUSE_PRESSED, this.mouseHandler);
      Scene _scene = this.root.getScene();
      _scene.<KeyEvent>removeEventHandler(KeyEvent.KEY_PRESSED, this.keyHandler);
      _xblockexpression = true;
    }
    return _xblockexpression;
  }
  
  private static Logger LOG = Logger.getLogger("de.fxdiagram.core.tools.MenuTool");
    ;
}
