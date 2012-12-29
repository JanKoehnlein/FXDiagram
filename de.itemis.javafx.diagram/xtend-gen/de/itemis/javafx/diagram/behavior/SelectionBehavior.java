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
          it.setOffsetX(5.0);
          it.setOffsetY(5.0);
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
        }
      };
    _host.setOnMousePressed(new EventHandler<MouseEvent>() {
        public void handle(MouseEvent event) {
          _function.apply(event);
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
        }
      };
    _host_1.setOnMouseReleased(new EventHandler<MouseEvent>() {
        public void handle(MouseEvent event) {
          _function_1.apply(event);
        }
    });
    final Procedure3<ObservableValue<? extends Boolean>,Boolean,Boolean> _function_2 = new Procedure3<ObservableValue<? extends Boolean>,Boolean,Boolean>() {
        public void apply(final ObservableValue<? extends Boolean> observable, final Boolean oldValue, final Boolean newValue) {
          if ((newValue).booleanValue()) {
            XNode _host = SelectionBehavior.this.getHost();
            _host.setEffect(SelectionBehavior.this.selectionEffect);
          } else {
            XNode _host_1 = SelectionBehavior.this.getHost();
            _host_1.setEffect(null);
          }
        }
      };
    final ChangeListener<Boolean> selectionListener = new ChangeListener<Boolean>() {
        public void changed(ObservableValue<? extends Boolean> observable,Boolean oldValue,Boolean newValue) {
          _function_2.apply(observable,oldValue,newValue);
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
