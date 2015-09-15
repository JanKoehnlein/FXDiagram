package de.fxdiagram.mapping.behavior;

import com.google.common.base.Objects;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XConnectionLabel;
import de.fxdiagram.core.command.AbstractCommand;
import de.fxdiagram.core.command.CommandContext;
import de.fxdiagram.mapping.behavior.AddRemoveAcceptor;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javafx.collections.ObservableList;
import org.eclipse.xtend.lib.annotations.FinalFieldsConstructor;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;

@FinalFieldsConstructor
@SuppressWarnings("all")
public class ConnectionLabelMorphCommand extends AbstractCommand implements AddRemoveAcceptor {
  private final XConnection connection;
  
  private List<XConnectionLabel> added;
  
  private List<XConnectionLabel> removed;
  
  @Override
  public void add(final XConnectionLabel label) {
    List<XConnectionLabel> _elvis = null;
    if (this.added != null) {
      _elvis = this.added;
    } else {
      ArrayList<XConnectionLabel> _newArrayList = CollectionLiterals.<XConnectionLabel>newArrayList();
      _elvis = (this.added = _newArrayList);
    }
    _elvis.add(label);
  }
  
  @Override
  public void remove(final XConnectionLabel label) {
    List<XConnectionLabel> _elvis = null;
    if (this.removed != null) {
      _elvis = this.removed;
    } else {
      ArrayList<XConnectionLabel> _newArrayList = CollectionLiterals.<XConnectionLabel>newArrayList();
      _elvis = (this.removed = _newArrayList);
    }
    _elvis.add(label);
  }
  
  @Override
  public void keep(final XConnectionLabel label) {
  }
  
  public boolean isEmpty() {
    boolean _and = false;
    boolean _equals = Objects.equal(this.added, null);
    if (!_equals) {
      _and = false;
    } else {
      boolean _equals_1 = Objects.equal(this.removed, null);
      _and = _equals_1;
    }
    return _and;
  }
  
  @Override
  public void execute(final CommandContext context) {
    if (this.added!=null) {
      final Consumer<XConnectionLabel> _function = (XConnectionLabel it) -> {
        ObservableList<XConnectionLabel> _labels = this.connection.getLabels();
        _labels.add(it);
      };
      this.added.forEach(_function);
    }
    if (this.removed!=null) {
      final Consumer<XConnectionLabel> _function_1 = (XConnectionLabel it) -> {
        ObservableList<XConnectionLabel> _labels = this.connection.getLabels();
        _labels.remove(it);
      };
      this.removed.forEach(_function_1);
    }
  }
  
  @Override
  public void undo(final CommandContext context) {
    if (this.added!=null) {
      final Consumer<XConnectionLabel> _function = (XConnectionLabel it) -> {
        ObservableList<XConnectionLabel> _labels = this.connection.getLabels();
        _labels.remove(it);
      };
      this.added.forEach(_function);
    }
    if (this.removed!=null) {
      final Consumer<XConnectionLabel> _function_1 = (XConnectionLabel it) -> {
        ObservableList<XConnectionLabel> _labels = this.connection.getLabels();
        _labels.add(it);
      };
      this.removed.forEach(_function_1);
    }
  }
  
  @Override
  public void redo(final CommandContext context) {
    this.execute(context);
  }
  
  public ConnectionLabelMorphCommand(final XConnection connection) {
    super();
    this.connection = connection;
  }
}
