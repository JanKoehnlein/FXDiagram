package de.itemis.javafx.diagram.behavior;

import de.itemis.javafx.diagram.XNode;
import de.itemis.javafx.diagram.behavior.AbstractBehavior;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.input.MouseEvent;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure3;

@SuppressWarnings("all")
public class SelectionBehavior extends AbstractBehavior {
  private BooleanProperty isSelected = new Function0<BooleanProperty>() {
    public BooleanProperty apply() {
      SimpleBooleanProperty _simpleBooleanProperty = new SimpleBooleanProperty();
      return _simpleBooleanProperty;
    }
  }.apply();
  
  private Effect selectionEffect;
  
  private boolean wasSelected;
  
  public SelectionBehavior(final XNode host) {
    super(host);
    DropShadow _dropShadow = new DropShadow();
    final Procedure1<DropShadow> _function = new Procedure1<DropShadow>() {
        public void apply(final DropShadow it) {
          it.setOffsetX(4.0);
          it.setOffsetY(4.0);
        }
      };
    DropShadow _doubleArrow = ObjectExtensions.<DropShadow>operator_doubleArrow(_dropShadow, _function);
    this.selectionEffect = _doubleArrow;
  }
  
  public void doActivate() {
    XNode _host = this.getHost();
    final Procedure1<MouseEvent> _function = new Procedure1<MouseEvent>() {
        public void apply(final MouseEvent it) {
          SelectionBehavior.this.mousePressed(it);
          it.consume();
        }
      };
    _host.setOnMousePressed(new EventHandler<MouseEvent>() {
        public void handle(MouseEvent arg0) {
          _function.apply(arg0);
        }
    });
    XNode _host_1 = this.getHost();
    final Procedure1<MouseEvent> _function_1 = new Procedure1<MouseEvent>() {
        public void apply(final MouseEvent it) {
          boolean _isShortcutDown = it.isShortcutDown();
          if (_isShortcutDown) {
            boolean _not = (!SelectionBehavior.this.wasSelected);
            SelectionBehavior.this.isSelected.set(_not);
          }
          it.consume();
        }
      };
    _host_1.setOnMouseReleased(new EventHandler<MouseEvent>() {
        public void handle(MouseEvent arg0) {
          _function_1.apply(arg0);
        }
    });
    final Procedure3<ObservableValue<? extends Boolean>,Boolean,Boolean> _function_2 = new Procedure3<ObservableValue<? extends Boolean>,Boolean,Boolean>() {
        public void apply(final ObservableValue<? extends Boolean> observable, final Boolean oldValue, final Boolean newValue) {
          XNode _host = SelectionBehavior.this.getHost();
          String _plus = ("Selection of " + _host);
          String _plus_1 = (_plus + " ");
          String _plus_2 = (_plus_1 + newValue);
          InputOutput.<String>println(_plus_2);
          if ((newValue).booleanValue()) {
            XNode _host_1 = SelectionBehavior.this.getHost();
            _host_1.setEffect(SelectionBehavior.this.selectionEffect);
            XNode _host_2 = SelectionBehavior.this.getHost();
            _host_2.setScaleX(1.05);
            XNode _host_3 = SelectionBehavior.this.getHost();
            _host_3.setScaleY(1.05);
          } else {
            XNode _host_4 = SelectionBehavior.this.getHost();
            _host_4.setEffect(null);
            XNode _host_5 = SelectionBehavior.this.getHost();
            _host_5.setScaleX(1.0);
            XNode _host_6 = SelectionBehavior.this.getHost();
            _host_6.setScaleY(1.0);
          }
        }
      };
    final ChangeListener<Boolean> selectionListener = new ChangeListener<Boolean>() {
        public void changed(ObservableValue<? extends Boolean> arg0,Boolean arg1,Boolean arg2) {
          _function_2.apply(arg0,arg1,arg2);
        }
    };
    BooleanProperty _selectedProperty = this.getSelectedProperty();
    _selectedProperty.addListener(selectionListener);
  }
  
  public void mousePressed(final MouseEvent it) {
    boolean _get = this.isSelected.get();
    this.wasSelected = _get;
    this.isSelected.set(true);
  }
  
  public BooleanProperty getSelectedProperty() {
    return this.isSelected;
  }
  
  public boolean isSelected() {
    BooleanProperty _selectedProperty = this.getSelectedProperty();
    boolean _get = _selectedProperty.get();
    return _get;
  }
  
  public void setSelected(final boolean isSelected) {
    BooleanProperty _selectedProperty = this.getSelectedProperty();
    _selectedProperty.set(isSelected);
  }
}
