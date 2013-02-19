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
            SelectionBehavior.this.isSelected.set(_not);
          }
          it.consume();
        }
      };
    _host_1.setOnMouseReleased(_function_1);
    BooleanProperty _selectedProperty = this.getSelectedProperty();
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
    _selectedProperty.addListener(_function_2);
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
