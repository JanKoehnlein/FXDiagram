package de.fxdiagram.core.tools;

import com.google.common.base.Charsets;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.XRootDiagram;
import de.fxdiagram.core.export.SvgExporter;
import de.fxdiagram.core.layout.Layouter;
import de.fxdiagram.core.tools.XDiagramTool;
import eu.hansolo.enzo.radialmenu.MenuItem;
import eu.hansolo.enzo.radialmenu.Options;
import eu.hansolo.enzo.radialmenu.RadialMenu;
import eu.hansolo.enzo.radialmenu.RadialMenu.ItemEvent;
import eu.hansolo.enzo.radialmenu.RadialMenu.MenuEvent;
import eu.hansolo.enzo.radialmenu.RadialMenu.State;
import eu.hansolo.enzo.radialmenu.Symbol.Type;
import java.io.File;
import java.util.Collections;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.StringExtensions;

@SuppressWarnings("all")
public class MenuTool implements XDiagramTool {
  private XRootDiagram diagram;
  
  private EventHandler<KeyEvent> keyHandler;
  
  private EventHandler<MouseEvent> mouseHandler;
  
  private Group menuGroup;
  
  private RadialMenu menu;
  
  private MenuItem selection;
  
  public MenuTool(final XRootDiagram diagram) {
    this.diagram = diagram;
    final EventHandler<KeyEvent> _function = new EventHandler<KeyEvent>() {
      public void handle(final KeyEvent it) {
        KeyCode _code = it.getCode();
        final KeyCode getCode = _code;
        boolean _matched = false;
        if (!_matched) {
          if (Objects.equal(getCode,KeyCode.E)) {
            _matched=true;
            boolean _isShortcutDown = it.isShortcutDown();
            if (_isShortcutDown) {
              MenuTool.this.doExportSvg();
              it.consume();
            }
          }
        }
        if (!_matched) {
          if (Objects.equal(getCode,KeyCode.L)) {
            _matched=true;
            boolean _isShortcutDown_1 = it.isShortcutDown();
            if (_isShortcutDown_1) {
              MenuTool.this.doLayout();
              it.consume();
            }
          }
        }
        if (!_matched) {
          if (Objects.equal(getCode,KeyCode.Q)) {
            _matched=true;
            boolean _isShortcutDown_2 = it.isShortcutDown();
            if (_isShortcutDown_2) {
              MenuTool.this.doExit();
            }
          }
        }
        if (!_matched) {
          if (Objects.equal(getCode,KeyCode.ESCAPE)) {
            _matched=true;
            MenuTool.this.closeMenu();
            it.consume();
          }
        }
      }
    };
    this.keyHandler = _function;
    Options _options = new Options();
    final Procedure1<Options> _function_1 = new Procedure1<Options>() {
      public void apply(final Options it) {
        it.setDegrees(360);
        int _minus = (-90);
        it.setOffset(_minus);
        it.setRadius(200);
        it.setButtonSize(72);
        it.setButtonAlpha(1.0);
      }
    };
    Options _doubleArrow = ObjectExtensions.<Options>operator_doubleArrow(_options, _function_1);
    final Function1<Type,MenuItem> _function_2 = new Function1<Type,MenuItem>() {
      public MenuItem apply(final Type s) {
        MenuItem _menuItem = new MenuItem();
        final Procedure1<MenuItem> _function = new Procedure1<MenuItem>() {
          public void apply(final MenuItem it) {
            it.setSymbol(s);
            String _string = s.toString();
            String _lowerCase = _string.toLowerCase();
            String _firstUpper = StringExtensions.toFirstUpper(_lowerCase);
            it.setTooltip(_firstUpper);
            it.setSize(64);
          }
        };
        MenuItem _doubleArrow = ObjectExtensions.<MenuItem>operator_doubleArrow(_menuItem, _function);
        return _doubleArrow;
      }
    };
    List<MenuItem> _map = ListExtensions.<Type, MenuItem>map(Collections.<Type>unmodifiableList(Lists.<Type>newArrayList(Type.EJECT, Type.GRAPH, Type.CAMERA, Type.PHOTO)), _function_2);
    RadialMenu _radialMenu = new RadialMenu(_doubleArrow, _map);
    this.menu = _radialMenu;
    final EventHandler<MouseEvent> _function_3 = new EventHandler<MouseEvent>() {
      public void handle(final MouseEvent it) {
        State _state = MenuTool.this.menu.getState();
        boolean _equals = Objects.equal(_state, State.OPENED);
        if (_equals) {
          MenuTool.this.closeMenu();
          it.consume();
        } else {
          boolean _and = false;
          EventTarget _target = it.getTarget();
          Scene _scene = diagram.getScene();
          boolean _equals_1 = Objects.equal(_target, _scene);
          if (!_equals_1) {
            _and = false;
          } else {
            State _state_1 = MenuTool.this.menu.getState();
            boolean _notEquals = (!Objects.equal(_state_1, State.OPENED));
            _and = (_equals_1 && _notEquals);
          }
          if (_and) {
            MenuTool.this.openMenu();
            it.consume();
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
      XRoot _root = this.diagram.getRoot();
      ObservableList<Node> _children = _root.getChildren();
      final Procedure1<Group> _function = new Procedure1<Group>() {
        public void apply(final Group it) {
          Scene _scene = MenuTool.this.diagram.getScene();
          double _width = _scene.getWidth();
          double _multiply = (0.5 * _width);
          it.setTranslateX(_multiply);
          Scene _scene_1 = MenuTool.this.diagram.getScene();
          double _height = _scene_1.getHeight();
          double _multiply_1 = (0.5 * _height);
          it.setTranslateY(_multiply_1);
          ObservableList<Node> _children = it.getChildren();
          final Procedure1<RadialMenu> _function = new Procedure1<RadialMenu>() {
            public void apply(final RadialMenu it) {
              it.show();
              it.open();
              final EventHandler<ItemEvent> _function = new EventHandler<ItemEvent>() {
                public void handle(final ItemEvent it) {
                  MenuItem _item = it.getItem();
                  MenuTool.this.selection = _item;
                }
              };
              it.setOnItemSelected(_function);
              final EventHandler<MenuEvent> _function_1 = new EventHandler<MenuEvent>() {
                public void handle(final MenuEvent it) {
                  MenuTool.this.closeMenu();
                  boolean _notEquals = (!Objects.equal(MenuTool.this.selection, null));
                  if (_notEquals) {
                    Type _symbol = MenuTool.this.selection.getSymbol();
                    final Type _switchValue = _symbol;
                    boolean _matched = false;
                    if (!_matched) {
                      if (Objects.equal(_switchValue,Type.GRAPH)) {
                        _matched=true;
                        MenuTool.this.doLayout();
                      }
                    }
                    if (!_matched) {
                      if (Objects.equal(_switchValue,Type.CAMERA)) {
                        _matched=true;
                        MenuTool.this.doExportSvg();
                      }
                    }
                    if (!_matched) {
                      if (Objects.equal(_switchValue,Type.EJECT)) {
                        _matched=true;
                        MenuTool.this.doExit();
                      }
                    }
                    if (!_matched) {
                      String _plus = ("Unhandled menu item " + MenuTool.this.selection);
                      InputOutput.<String>println(_plus);
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
      boolean _add = _children.add(_doubleArrow);
      _xblockexpression = (_add);
    }
    return _xblockexpression;
  }
  
  protected Boolean closeMenu() {
    Boolean _xblockexpression = null;
    {
      this.menu.hide();
      Boolean _xifexpression = null;
      boolean _and = false;
      boolean _notEquals = (!Objects.equal(this.menuGroup, null));
      if (!_notEquals) {
        _and = false;
      } else {
        Parent _parent = this.menuGroup.getParent();
        boolean _notEquals_1 = (!Objects.equal(_parent, null));
        _and = (_notEquals && _notEquals_1);
      }
      if (_and) {
        XRoot _root = this.diagram.getRoot();
        ObservableList<Node> _children = _root.getChildren();
        boolean _remove = _children.remove(this.menuGroup);
        _xifexpression = Boolean.valueOf(_remove);
      }
      _xblockexpression = (_xifexpression);
    }
    return _xblockexpression;
  }
  
  public boolean activate() {
    boolean _xblockexpression = false;
    {
      Scene _scene = this.diagram.getScene();
      _scene.<KeyEvent>addEventHandler(KeyEvent.KEY_PRESSED, this.keyHandler);
      Scene _scene_1 = this.diagram.getScene();
      _scene_1.<MouseEvent>addEventHandler(MouseEvent.MOUSE_PRESSED, this.mouseHandler);
      _xblockexpression = (true);
    }
    return _xblockexpression;
  }
  
  public boolean deactivate() {
    boolean _xblockexpression = false;
    {
      Scene _scene = this.diagram.getScene();
      _scene.<MouseEvent>removeEventHandler(MouseEvent.MOUSE_PRESSED, this.mouseHandler);
      Scene _scene_1 = this.diagram.getScene();
      _scene_1.<KeyEvent>removeEventHandler(KeyEvent.KEY_PRESSED, this.keyHandler);
      _xblockexpression = (true);
    }
    return _xblockexpression;
  }
  
  public void doLayout() {
    Layouter _layouter = new Layouter();
    Duration _seconds = Duration.seconds(2);
    _layouter.layout(this.diagram, _seconds);
  }
  
  public void doExportSvg() {
    try {
      SvgExporter _svgExporter = new SvgExporter();
      final CharSequence svgCode = _svgExporter.toSvg(this.diagram);
      File _file = new File("Diagram.svg");
      Files.write(svgCode, _file, Charsets.UTF_8);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public void doExit() {
    System.exit(0);
  }
}
