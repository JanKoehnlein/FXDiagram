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
import eu.hansolo.enzo.radialmenu.SymbolType;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
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
    final EventHandler<KeyEvent> _function = (KeyEvent event) -> {
      DiagramActionRegistry _diagramActionRegistry = root.getDiagramActionRegistry();
      ArrayList<DiagramAction> _actions = _diagramActionRegistry.getActions();
      final Function1<DiagramAction, Boolean> _function_1 = (DiagramAction it) -> {
        return Boolean.valueOf(it.matches(event));
      };
      Iterable<DiagramAction> _filter = IterableExtensions.<DiagramAction>filter(_actions, _function_1);
      final Consumer<DiagramAction> _function_2 = (DiagramAction it) -> {
        boolean _isConsumed = event.isConsumed();
        boolean _not = (!_isConsumed);
        if (_not) {
          it.perform(root);
        }
      };
      _filter.forEach(_function_2);
      KeyCode _code = event.getCode();
      boolean _equals = Objects.equal(_code, KeyCode.ESCAPE);
      if (_equals) {
        event.consume();
        RadialMenu.State _state = null;
        if (this.menu!=null) {
          _state=this.menu.getState();
        }
        boolean _equals_1 = Objects.equal(_state, RadialMenu.State.OPENED);
        if (_equals_1) {
          this.closeMenu();
          return;
        } else {
          new CloseAction();
        }
      }
    };
    this.keyHandler = _function;
    final EventHandler<MouseEvent> _function_1 = (MouseEvent it) -> {
      MouseButton _button = it.getButton();
      boolean _equals = Objects.equal(_button, MouseButton.SECONDARY);
      if (_equals) {
        RadialMenu.State _state = null;
        if (this.menu!=null) {
          _state=this.menu.getState();
        }
        boolean _equals_1 = Objects.equal(_state, RadialMenu.State.OPENED);
        if (_equals_1) {
          this.closeMenu();
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
            if (this.menu!=null) {
              _state_1=this.menu.getState();
            }
            boolean _notEquals = (!Objects.equal(_state_1, RadialMenu.State.OPENED));
            _and = _notEquals;
          }
          if (_and) {
            this.openMenu();
            it.consume();
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
      final Procedure1<Options> _function = (Options it) -> {
        it.setDegrees(360);
        it.setOffset((-90));
        it.setRadius(200);
        it.setButtonSize(72);
        it.setButtonAlpha(1.0);
      };
      Options _doubleArrow = ObjectExtensions.<Options>operator_doubleArrow(_options, _function);
      DiagramActionRegistry _diagramActionRegistry = this.root.getDiagramActionRegistry();
      ArrayList<DiagramAction> _actions = _diagramActionRegistry.getActions();
      final Function1<DiagramAction, Boolean> _function_1 = (DiagramAction it) -> {
        SymbolType _symbol = it.getSymbol();
        return Boolean.valueOf((!Objects.equal(_symbol, null)));
      };
      Iterable<DiagramAction> _filter = IterableExtensions.<DiagramAction>filter(_actions, _function_1);
      final Function1<DiagramAction, MenuItem> _function_2 = (DiagramAction action) -> {
        MenuItem _menuItem = new MenuItem();
        final Procedure1<MenuItem> _function_3 = (MenuItem it) -> {
          SymbolType _symbol = action.getSymbol();
          it.setSymbol(_symbol);
          String _tooltip = action.getTooltip();
          it.setTooltip(_tooltip);
          it.setSize(64);
        };
        return ObjectExtensions.<MenuItem>operator_doubleArrow(_menuItem, _function_3);
      };
      Iterable<MenuItem> _map = IterableExtensions.<DiagramAction, MenuItem>map(_filter, _function_2);
      RadialMenu _radialMenu = new RadialMenu(_doubleArrow, ((MenuItem[])Conversions.unwrapArray(_map, MenuItem.class)));
      this.menu = _radialMenu;
      HeadsUpDisplay _headsUpDisplay = this.root.getHeadsUpDisplay();
      ObservableList<Node> _children = _headsUpDisplay.getChildren();
      Group _group = new Group();
      final Procedure1<Group> _function_3 = (Group it) -> {
        BorderPane.setAlignment(it, Pos.CENTER);
        Scene _scene = this.root.getScene();
        double _width = _scene.getWidth();
        double _multiply = (0.5 * _width);
        it.setTranslateX(_multiply);
        Scene _scene_1 = this.root.getScene();
        double _height = _scene_1.getHeight();
        double _multiply_1 = (0.5 * _height);
        it.setTranslateY(_multiply_1);
        ObservableList<Node> _children_1 = it.getChildren();
        final Procedure1<RadialMenu> _function_4 = (RadialMenu it_1) -> {
          it_1.show();
          it_1.open();
          final EventHandler<RadialMenu.ItemEvent> _function_5 = (RadialMenu.ItemEvent it_2) -> {
            MenuItem _item = it_2.getItem();
            this.selection = _item;
          };
          it_1.setOnItemSelected(_function_5);
          final EventHandler<RadialMenu.MenuEvent> _function_6 = (RadialMenu.MenuEvent it_2) -> {
            this.closeMenu();
            boolean _notEquals = (!Objects.equal(this.selection, null));
            if (_notEquals) {
              DiagramActionRegistry _diagramActionRegistry_1 = this.root.getDiagramActionRegistry();
              SymbolType _symbol = this.selection.getSymbol();
              final DiagramAction action = _diagramActionRegistry_1.getBySymbol(_symbol);
              boolean _equals = Objects.equal(action, null);
              if (_equals) {
                DiagramActionTool.LOG.warning(("Unhandled menu item " + this.selection));
              }
              if (action!=null) {
                action.perform(this.root);
              }
            }
            this.selection = null;
          };
          it_1.setOnMenuCloseFinished(_function_6);
        };
        RadialMenu _doubleArrow_1 = ObjectExtensions.<RadialMenu>operator_doubleArrow(this.menu, _function_4);
        _children_1.add(_doubleArrow_1);
      };
      Group _doubleArrow_1 = ObjectExtensions.<Group>operator_doubleArrow(_group, _function_3);
      Group _menuGroup = (this.menuGroup = _doubleArrow_1);
      _xblockexpression = _children.add(_menuGroup);
    }
    return _xblockexpression;
  }
  
  protected boolean closeMenu() {
    boolean _xblockexpression = false;
    {
      this.menu.hide();
      boolean _xifexpression = false;
      if (((!Objects.equal(this.menuGroup, null)) && (!Objects.equal(this.menuGroup.getParent(), null)))) {
        HeadsUpDisplay _headsUpDisplay = this.root.getHeadsUpDisplay();
        ObservableList<Node> _children = _headsUpDisplay.getChildren();
        _xifexpression = _children.remove(this.menuGroup);
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  @Override
  public boolean activate() {
    boolean _xblockexpression = false;
    {
      Scene _scene = this.root.getScene();
      _scene.<KeyEvent>addEventHandler(KeyEvent.KEY_PRESSED, this.keyHandler);
      Scene _scene_1 = this.root.getScene();
      _scene_1.<MouseEvent>addEventHandler(MouseEvent.MOUSE_RELEASED, this.mouseHandler);
      _xblockexpression = true;
    }
    return _xblockexpression;
  }
  
  @Override
  public boolean deactivate() {
    boolean _xblockexpression = false;
    {
      Scene _scene = this.root.getScene();
      _scene.<MouseEvent>removeEventHandler(MouseEvent.MOUSE_RELEASED, this.mouseHandler);
      Scene _scene_1 = this.root.getScene();
      _scene_1.<KeyEvent>removeEventHandler(KeyEvent.KEY_PRESSED, this.keyHandler);
      _xblockexpression = true;
    }
    return _xblockexpression;
  }
  
  private static Logger LOG = Logger.getLogger("de.fxdiagram.core.tools.DiagramActionTool");
    ;
}
