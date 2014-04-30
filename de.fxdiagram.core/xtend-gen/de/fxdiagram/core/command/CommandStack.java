package de.fxdiagram.core.command;

import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.command.AnimationCommand;
import de.fxdiagram.core.command.AnimationQueue;
import de.fxdiagram.core.command.CommandContext;
import java.util.LinkedList;
import javafx.animation.Animation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function0;

@SuppressWarnings("all")
public class CommandStack {
  private LinkedList<AnimationCommand> undoStack = CollectionLiterals.<AnimationCommand>newLinkedList();
  
  private LinkedList<AnimationCommand> redoStack = CollectionLiterals.<AnimationCommand>newLinkedList();
  
  private CommandContext context;
  
  public CommandStack(final XRoot root) {
    CommandContext _commandContext = new CommandContext(root);
    this.context = _commandContext;
  }
  
  public boolean canUndo() {
    boolean _isEmpty = this.undoStack.isEmpty();
    return (!_isEmpty);
  }
  
  public boolean canRedo() {
    boolean _isEmpty = this.redoStack.isEmpty();
    return (!_isEmpty);
  }
  
  public void undo() {
    boolean _canUndo = this.canUndo();
    if (_canUndo) {
      final AnimationCommand command = this.undoStack.pop();
      AnimationQueue _animationQueue = this.context.getAnimationQueue();
      final Function0<Animation> _function = new Function0<Animation>() {
        public Animation apply() {
          return command.getUndoAnimation(CommandStack.this.context);
        }
      };
      _animationQueue.enqueue(_function);
      this.redoStack.push(command);
    }
  }
  
  public void redo() {
    boolean _canRedo = this.canRedo();
    if (_canRedo) {
      final AnimationCommand command = this.redoStack.pop();
      AnimationQueue _animationQueue = this.context.getAnimationQueue();
      final Function0<Animation> _function = new Function0<Animation>() {
        public Animation apply() {
          return command.getRedoAnimation(CommandStack.this.context);
        }
      };
      _animationQueue.enqueue(_function);
      this.undoStack.push(command);
    }
  }
  
  public void execute(final AnimationCommand command) {
    AnimationQueue _animationQueue = this.context.getAnimationQueue();
    final Function0<Animation> _function = new Function0<Animation>() {
      public Animation apply() {
        return command.getExecuteAnimation(CommandStack.this.context);
      }
    };
    _animationQueue.enqueue(_function);
    this.undoStack.push(command);
    boolean _clearRedoStackOnExecute = command.clearRedoStackOnExecute();
    if (_clearRedoStackOnExecute) {
      this.redoStack.clear();
    }
  }
  
  public CommandContext getContext() {
    return this.context;
  }
}
