package de.fxdiagram.core.command;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.fxdiagram.core.command.AbstractAnimationCommand;
import de.fxdiagram.core.command.AnimationCommand;
import de.fxdiagram.core.command.CommandContext;
import java.util.List;
import java.util.function.Consumer;
import javafx.animation.Animation;
import javafx.animation.ParallelTransition;
import javafx.collections.ObservableList;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class ParallelAnimationCommand extends AbstractAnimationCommand {
  private List<AnimationCommand> commands = CollectionLiterals.<AnimationCommand>newArrayList();
  
  public void operator_add(final AnimationCommand command) {
    boolean _notEquals = (!Objects.equal(command, null));
    if (_notEquals) {
      this.commands.add(command);
      command.skipViewportRestore();
    }
  }
  
  public void operator_add(final Iterable<? extends AnimationCommand> commands) {
    final Consumer<AnimationCommand> _function = (AnimationCommand it) -> {
      this.operator_add(it);
    };
    commands.forEach(_function);
  }
  
  @Override
  public Animation createExecuteAnimation(final CommandContext context) {
    final Function1<AnimationCommand, Animation> _function = (AnimationCommand it) -> {
      return it.getExecuteAnimation(context);
    };
    return this.getParallelTransition(ListExtensions.<AnimationCommand, Animation>map(this.commands, _function));
  }
  
  @Override
  public Animation createUndoAnimation(final CommandContext context) {
    final Function1<AnimationCommand, Animation> _function = (AnimationCommand it) -> {
      return it.getUndoAnimation(context);
    };
    return this.getParallelTransition(ListExtensions.<AnimationCommand, Animation>map(this.commands, _function));
  }
  
  @Override
  public Animation createRedoAnimation(final CommandContext context) {
    final Function1<AnimationCommand, Animation> _function = (AnimationCommand it) -> {
      return it.getRedoAnimation(context);
    };
    return this.getParallelTransition(ListExtensions.<AnimationCommand, Animation>map(this.commands, _function));
  }
  
  protected ParallelTransition getParallelTransition(final List<Animation> animations) {
    final Iterable<Animation> validAnimations = IterableExtensions.<Animation>filterNull(animations);
    boolean _isEmpty = IterableExtensions.isEmpty(validAnimations);
    if (_isEmpty) {
      return null;
    } else {
      ParallelTransition _parallelTransition = new ParallelTransition();
      final Procedure1<ParallelTransition> _function = (ParallelTransition it) -> {
        ObservableList<Animation> _children = it.getChildren();
        Iterables.<Animation>addAll(_children, validAnimations);
      };
      return ObjectExtensions.<ParallelTransition>operator_doubleArrow(_parallelTransition, _function);
    }
  }
  
  @Override
  public String toString() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("ParallelAnimationCommand [");
    _builder.newLine();
    {
      for(final AnimationCommand command : this.commands) {
        _builder.append("\t");
        _builder.append(command, "\t");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("]");
    return _builder.toString();
  }
}
