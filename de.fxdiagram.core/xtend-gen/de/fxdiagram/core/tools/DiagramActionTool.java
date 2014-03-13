package de.fxdiagram.core.tools;

import com.google.common.base.Objects;
import de.fxdiagram.annotations.logging.Logging;
import de.fxdiagram.core.HeadsUpDisplay;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.tools.XDiagramTool;
import de.fxdiagram.core.tools.actions.CloseAction;
import de.fxdiagram.core.tools.actions.DiagramAction;
import de.fxdiagram.core.tools.actions.DiagramActionRegistry;
import eu.hansolo.enzo.radialmenu.MenuItem;
import eu.hansolo.enzo.radialmenu.Options;
import eu.hansolo.enzo.radialmenu.RadialMenu;
import eu.hansolo.enzo.radialmenu.Symbol;
import java.util.ArrayList;
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
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@Logging
@SuppressWarnings("all")
public class DiagramActionTool implements XDiagramTool {
  private XRoot root;
  
  private EventHandler<KeyEvent> keyHandler;
  
  private EventHandler<MouseEvent> mouseHandler;
  
  private Group menuGroup;
  
  private RadialMenu menu;
  
  private MenuItem selection;
  
  public DiagramActionTool(final XRoot root) {
    this.root = root;
    final EventHandler<KeyEvent> _function = new EventHandler<KeyEvent>() {
      public void handle(final KeyEvent event) {
        DiagramActionRegistry _diagramActionRegistry = root.getDiagramActionRegistry();
        ArrayList<DiagramAction> _actions = _diagramActionRegistry.getActions();
        final Function1<DiagramAction,Boolean> _function = new Function1<DiagramAction,Boolean>() {
          public Boolean apply(final DiagramAction it) {
            return Boolean.valueOf(it.matches(event));
          }
        };
        Iterable<DiagramAction> _filter = IterableExtensions.<DiagramAction>filter(_actions, _function);
        final Procedure1<DiagramAction> _function_1 = new Procedure1<DiagramAction>() {
          public void apply(final DiagramAction it) {
            boolean _isConsumed = event.isConsumed();
            boolean _not = (!_isConsumed);
            if (_not) {
              it.perform(root);
            }
          }
        };
        IterableExtensions.<DiagramAction>forEach(_filter, _function_1);
        KeyCode _code = event.getCode();
        boolean _equals = Objects.equal(_code, KeyCode.ESCAPE);
        if (_equals) {
          event.consume();
          RadialMenu.State _state = DiagramActionTool.this.menu.getState();
          boolean _equals_1 = Objects.equal(_state, RadialMenu.State.OPENED);
          if (_equals_1) {
            DiagramActionTool.this.closeMenu();
            return;
          } else {
            new CloseAction();
          }
        }
      }
    };
    this.keyHandler = _function;
    final EventHandler<MouseEvent> _function_1 = new EventHandler<MouseEvent>() {
      public void handle(final MouseEvent it) {
        MouseButton _button = it.getButton();
        boolean _equals = Objects.equal(_button, MouseButton.SECONDARY);
        if (_equals) {
          RadialMenu.State _state = null;
          if (DiagramActionTool.this.menu!=null) {
            _state=DiagramActionTool.this.menu.getState();
          }
          boolean _equals_1 = Objects.equal(_state, RadialMenu.State.OPENED);
          if (_equals_1) {
            DiagramActionTool.this.closeMenu();
            it.consume();
          } else {
            boolean _and = false;
            EventTarget _target = it.getTarget();
            Pane _diagramCanvas = root.getDiagramCanvas();
            boolean _equals_2 = Objects.equal(_target, _diagramCanvas);
            if (!_equals_2) {
              _and = false;
            } else {
              RadialMenu.State _state_1 = null;
              if (DiagramActionTool.this.menu!=null) {
                _state_1=DiagramActionTool.this.menu.getState();
              }
              boolean _notEquals = (!Objects.equal(_state_1, RadialMenu.State.OPENED));
              _and = _notEquals;
            }
            if (_and) {
              DiagramActionTool.this.openMenu();
              it.consume();
            }
          }
        }
      }
    };
    this.mouseHandler = _function_1;
  }
  
  protected boolean openMenu() {
    boolean _xblockexpression = false;
    {
      Options _options = new Options();
      final Procedure1<Options> _function = new Procedure1<Options>() {
        public void apply(final Options it) {
          it.setDegrees(360);
          it.setOffset((-90));
          it.setRadius(200);
          it.setButtonSize(72);
          it.setButtonAlpha(1.0);
        }
      };
      Options _doubleArrow = ObjectExtensions.<Options>operator_doubleArrow(_options, _function);
      DiagramActionRegistry _diagramActionRegistry = this.root.getDiagramActionRegistry();
      ArrayList<DiagramAction> _actions = _diagramActionRegistry.getActions();
      final Function1<DiagramAction,Symbol.Type> _function_1 = new Function1<DiagramAction,Symbol.Type>() {
        public Symbol.Type apply(final DiagramAction it) {
          return it.getSymbol();
        }
      };
      List<Symbol.Type> _map = ListExtensions.<DiagramAction, Symbol.Type>map(_actions, _function_1);
      Iterable<Symbol.Type> _filterNull = IterableExtensions.<Symbol.Type>filterNull(_map);
      final Function1<Symbol.Type,MenuItem> _function_2 = new Function1<Symbol.Type,MenuItem>() {
        public MenuItem apply(final Symbol.Type actionSymbol) {
          MenuItem _menuItem = new MenuItem();
          final Procedure1<MenuItem> _function = new Procedure1<MenuItem>() {
            public void apply(final MenuItem it) {
              it.setSymbol(actionSymbol);
              it.setSize(64);
            }
          };
          return ObjectExtensions.<MenuItem>operator_doubleArrow(_menuItem, _function);
        }
      };
      Iterable<MenuItem> _map_1 = IterableExtensions.<Symbol.Type, MenuItem>map(_filterNull, _function_2);
      RadialMenu _radialMenu = new RadialMenu(_doubleArrow, ((MenuItem[])Conversions.unwrapArray(_map_1, MenuItem.class)));
      this.menu = _radialMenu;
      HeadsUpDisplay _headsUpDisplay = this.root.getHeadsUpDisplay();
      ObservableList<Node> _children = _headsUpDisplay.getChildren();
      Group _group = new Group();
      final Procedure1<Group> _function_3 = new Procedure1<Group>() {
        public void apply(final Group it) {
          BorderPane.setAlignment(it, Pos.CENTER);
          Scene _scene = DiagramActionTool.this.root.getScene();
          double _width = _scene.getWidth();
          double _multiply = (0.5 * _width);
          it.setTranslateX(_multiply);
          Scene _scene_1 = DiagramActionTool.this.root.getScene();
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
                  DiagramActionTool.this.selection = _item;
                }
              };
              it.setOnItemSelected(_function);
              final EventHandler<RadialMenu.MenuEvent> _function_1 = new EventHandler<RadialMenu.MenuEvent>() {
                public void handle(final RadialMenu.MenuEvent it) {
                  DiagramActionTool.this.closeMenu();
                  boolean _notEquals = (!Objects.equal(DiagramActionTool.this.selection, null));
                  if (_notEquals) {
                    DiagramActionRegistry _diagramActionRegistry = DiagramActionTool.this.root.getDiagramActionRegistry();
                    Symbol.Type _symbol = DiagramActionTool.this.selection.getSymbol();
                    final DiagramAction action = _diagramActionRegistry.getBySymbol(_symbol);
                    boolean _equals = Objects.equal(action, null);
                    if (_equals) {
                      DiagramActionTool.LOG.warning(("Unhandled menu item " + DiagramActionTool.this.selection));
                    }
                    if (action!=null) {
                      action.perform(DiagramActionTool.this.root);
                    }
                  }
                  DiagramActionTool.this.selection = null;
                }
              };
              it.setOnMenuCloseFinished(_function_1);
            }
          };
          RadialMenu _doubleArrow = ObjectExtensions.<RadialMenu>operator_doubleArrow(DiagramActionTool.this.menu, _function);
          _children.add(_doubleArrow);
        }
      };
      Group _doubleArrow_1 = ObjectExtensions.<Group>operator_doubleArrow(_group, _function_3);
      Group _menuGroup = this.menuGroup = _doubleArrow_1;
      _xblockexpression = _children.add(_menuGroup);
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
  
  private static Logger LOG = Logger.getLogger("de.fxdiagram.core.tools.DiagramActionTool");
    ;
}
