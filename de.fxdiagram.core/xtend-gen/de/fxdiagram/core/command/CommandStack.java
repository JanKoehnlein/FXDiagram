package de.fxdiagram.core.command;

import de.fxdiagram.core.command.Command;
import de.fxdiagram.core.command.CommandContext;
import java.util.LinkedList;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;

@SuppressWarnings("all")
public class CommandStack {
  private LinkedList<Command> undoStack = CollectionLiterals.<Command>newLinkedList();
  
  private LinkedList<Command> redoStack = CollectionLiterals.<Command>newLinkedList();
  
  private CommandContext context = new CommandContext();
  
  public boolean canUndo() {
    boolean _and = false;
    boolean _isEmpty = this.undoStack.isEmpty();
    boolean _not = (!_isEmpty);
    if (!_not) {
      _and = false;
    } else {
      Command _last = this.undoStack.getLast();
      boolean _canUndo = false;
      if (_last!=null) {
        _canUndo=_last.canUndo();
      }
      _and = _canUndo;
    }
    return _and;
  }
  
  public boolean canRedo() {
    boolean _and = false;
    boolean _isEmpty = this.redoStack.isEmpty();
    boolean _not = (!_isEmpty);
    if (!_not) {
      _and = false;
    } else {
      Command _last = this.redoStack.getLast();
      boolean _canRedo = false;
      if (_last!=null) {
        _canRedo=_last.canRedo();
      }
      _and = _canRedo;
    }
    return _and;
  }
  
  public void undo() {
    boolean _canUndo = this.canUndo();
    if (_canUndo) {
      final Command command = this.undoStack.pop();
      command.undo(this.context);
      this.redoStack.push(command);
    }
  }
  
  public void redo() {
    boolean _canRedo = this.canRedo();
    if (_canRedo) {
      final Command command = this.redoStack.pop();
      command.redo(this.context);
      this.undoStack.push(command);
    }
  }
  
  public void execute(final Command command) {
    command.execute(this.context);
    this.undoStack.push(command);
  }
}
