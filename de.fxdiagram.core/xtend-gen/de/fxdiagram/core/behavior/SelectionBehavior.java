package de.fxdiagram.core.behavior;

import de.fxdiagram.core.XNode;
import de.fxdiagram.core.behavior.AbstractBehavior;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.input.MouseEvent;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class SelectionBehavior extends AbstractBehavior {
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
    final EventHandler<MouseEvent> _function = new EventHandler<MouseEvent>() {
        public void handle(final MouseEvent it) {
          SelectionBehavior.this.mousePressed(it);
          it.consume();
        }
      };
    _host.setOnMousePressed(_function);
    XNode _host_1 = this.getHost();
    final EventHandler<MouseEvent> _function_1 = new EventHandler<MouseEvent>() {
        public void handle(final MouseEvent it) {
          boolean _isShortcutDown = it.isShortcutDown();
          if (_isShortcutDown) {
            boolean _not = (!SelectionBehavior.this.wasSelected);
            SelectionBehavior.this.selectedProperty.set(_not);
          }
          it.consume();
        }
      };
    _host_1.setOnMouseReleased(_function_1);
    final ChangeListener<Boolean> _function_2 = new ChangeListener<Boolean>() {
        public void changed(final ObservableValue<? extends Boolean> observable, final Boolean oldValue, final Boolean newValue) {
          if ((newValue).booleanValue()) {
            XNode _host = SelectionBehavior.this.getHost();
            _host.setEffect(SelectionBehavior.this.selectionEffect);
            XNode _host_1 = SelectionBehavior.this.getHost();
            _host_1.setScaleX(1.05);
            XNode _host_2 = SelectionBehavior.this.getHost();
            _host_2.setScaleY(1.05);
          } else {
            XNode _host_3 = SelectionBehavior.this.getHost();
            _host_3.setEffect(null);
            XNode _host_4 = SelectionBehavior.this.getHost();
            _host_4.setScaleX(1.0);
            XNode _host_5 = SelectionBehavior.this.getHost();
            _host_5.setScaleY(1.0);
          }
        }
      };
    this.selectedProperty.addListener(_function_2);
  }
  
  public void mousePressed(final MouseEvent it) {
    boolean _selected = this.getSelected();
    this.wasSelected = _selected;
    this.setSelected(true);
  }
  
  private SimpleBooleanProperty selectedProperty = new SimpleBooleanProperty(this, "selected");
  
  public boolean getSelected() {
    return this.selectedProperty.get();
    
  }
  
  public void setSelected(final boolean selected) {
    this.selectedProperty.set(selected);
    
  }
  
  public BooleanProperty selectedProperty() {
    return this.selectedProperty;
    
  }
}
