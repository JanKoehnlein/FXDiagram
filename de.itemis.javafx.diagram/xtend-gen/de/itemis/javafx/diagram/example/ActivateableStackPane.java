package de.itemis.javafx.diagram.example;

import com.google.common.collect.Iterables;
import de.itemis.javafx.diagram.XActivatable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class ActivateableStackPane extends StackPane implements XActivatable {
  public void activate() {
    ObservableList<Node> _children = this.getChildren();
    Iterable<XActivatable> _filter = Iterables.<XActivatable>filter(_children, XActivatable.class);
    final Procedure1<XActivatable> _function = new Procedure1<XActivatable>() {
        public void apply(final XActivatable it) {
          it.activate();
        }
      };
    IterableExtensions.<XActivatable>forEach(_filter, _function);
    BooleanProperty _visibleProperty = this.visibleProperty();
    final ChangeListener<Boolean> _function_1 = new ChangeListener<Boolean>() {
        public void changed(final ObservableValue<? extends Boolean> element, final Boolean oldVal, final Boolean newVal) {
          ObservableList<Node> _children = ActivateableStackPane.this.getChildren();
          final Procedure1<Node> _function = new Procedure1<Node>() {
              public void apply(final Node it) {
                it.setVisible((newVal).booleanValue());
              }
            };
          IterableExtensions.<Node>forEach(_children, _function);
        }
      };
    _visibleProperty.addListener(_function_1);
  }
}
