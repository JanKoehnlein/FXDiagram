package de.fxdiagram.core.command;

import com.google.common.collect.Iterables;
import de.fxdiagram.core.command.AbstractAnimationCommand;
import de.fxdiagram.core.command.CommandContext;
import java.util.List;
import javafx.animation.Animation;
import javafx.animation.ParallelTransition;
import javafx.collections.ObservableList;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class CompositeAnimationCommand extends AbstractAnimationCommand {
  private List<AbstractAnimationCommand> commands = CollectionLiterals.<AbstractAnimationCommand>newArrayList();
  
  public boolean operator_add(final AbstractAnimationCommand command) {
    return this.commands.add(command);
  }
  
  public Animation createExecuteAnimation(final CommandContext context) {
    ParallelTransition _parallelTransition = new ParallelTransition();
    final Procedure1<ParallelTransition> _function = new Procedure1<ParallelTransition>() {
      public void apply(final ParallelTransition it) {
        ObservableList<Animation> _children = it.getChildren();
        final Function1<AbstractAnimationCommand,Animation> _function = new Function1<AbstractAnimationCommand,Animation>() {
          public Animation apply(final AbstractAnimationCommand it) {
            return it.createExecuteAnimation(context);
          }
        };
        List<Animation> _map = ListExtensions.<AbstractAnimationCommand, Animation>map(CompositeAnimationCommand.this.commands, _function);
        Iterable<Animation> _filterNull = IterableExtensions.<Animation>filterNull(_map);
        Iterables.<Animation>addAll(_children, _filterNull);
      }
    };
    return ObjectExtensions.<ParallelTransition>operator_doubleArrow(_parallelTransition, _function);
  }
  
  public Animation createUndoAnimation(final CommandContext context) {
    ParallelTransition _parallelTransition = new ParallelTransition();
    final Procedure1<ParallelTransition> _function = new Procedure1<ParallelTransition>() {
      public void apply(final ParallelTransition it) {
        ObservableList<Animation> _children = it.getChildren();
        final Function1<AbstractAnimationCommand,Animation> _function = new Function1<AbstractAnimationCommand,Animation>() {
          public Animation apply(final AbstractAnimationCommand it) {
            return it.createUndoAnimation(context);
          }
        };
        List<Animation> _map = ListExtensions.<AbstractAnimationCommand, Animation>map(CompositeAnimationCommand.this.commands, _function);
        Iterable<Animation> _filterNull = IterableExtensions.<Animation>filterNull(_map);
        Iterables.<Animation>addAll(_children, _filterNull);
      }
    };
    return ObjectExtensions.<ParallelTransition>operator_doubleArrow(_parallelTransition, _function);
  }
  
  public Animation createRedoAnimation(final CommandContext context) {
    ParallelTransition _parallelTransition = new ParallelTransition();
    final Procedure1<ParallelTransition> _function = new Procedure1<ParallelTransition>() {
      public void apply(final ParallelTransition it) {
        ObservableList<Animation> _children = it.getChildren();
        final Function1<AbstractAnimationCommand,Animation> _function = new Function1<AbstractAnimationCommand,Animation>() {
          public Animation apply(final AbstractAnimationCommand it) {
            return it.createRedoAnimation(context);
          }
        };
        List<Animation> _map = ListExtensions.<AbstractAnimationCommand, Animation>map(CompositeAnimationCommand.this.commands, _function);
        Iterable<Animation> _filterNull = IterableExtensions.<Animation>filterNull(_map);
        Iterables.<Animation>addAll(_children, _filterNull);
      }
    };
    return ObjectExtensions.<ParallelTransition>operator_doubleArrow(_parallelTransition, _function);
  }
  
  public boolean canUndo() {
    final Function1<AbstractAnimationCommand,Boolean> _function = new Function1<AbstractAnimationCommand,Boolean>() {
      public Boolean apply(final AbstractAnimationCommand it) {
        return Boolean.valueOf(it.canUndo());
      }
    };
    return IterableExtensions.<AbstractAnimationCommand>forall(this.commands, _function);
  }
  
  public boolean canRedo() {
    final Function1<AbstractAnimationCommand,Boolean> _function = new Function1<AbstractAnimationCommand,Boolean>() {
      public Boolean apply(final AbstractAnimationCommand it) {
        return Boolean.valueOf(it.canRedo());
      }
    };
    return IterableExtensions.<AbstractAnimationCommand>forall(this.commands, _function);
  }
}
