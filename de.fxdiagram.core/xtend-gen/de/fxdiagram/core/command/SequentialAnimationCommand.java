package de.fxdiagram.core.command;

import de.fxdiagram.core.command.AbstractAnimationCommand;
import de.fxdiagram.core.command.AnimationCommand;
import de.fxdiagram.core.command.ChainedAnimationUtil;
import de.fxdiagram.core.command.CommandContext;
import java.util.List;
import javafx.animation.Animation;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.ListExtensions;

@SuppressWarnings("all")
public class SequentialAnimationCommand extends AbstractAnimationCommand {
  private List<AnimationCommand> commands = CollectionLiterals.<AnimationCommand>newArrayList();
  
  public boolean operator_add(final AnimationCommand command) {
    return this.commands.add(command);
  }
  
  @Override
  public Animation createExecuteAnimation(final CommandContext context) {
    final Function1<AnimationCommand, Animation> _function = (AnimationCommand it) -> {
      return it.getExecuteAnimation(context);
    };
    return ChainedAnimationUtil.<AnimationCommand>createChainedAnimation(this.commands, _function);
  }
  
  @Override
  public Animation createUndoAnimation(final CommandContext context) {
    List<AnimationCommand> _reverseView = ListExtensions.<AnimationCommand>reverseView(this.commands);
    final Function1<AnimationCommand, Animation> _function = (AnimationCommand it) -> {
      return it.getUndoAnimation(context);
    };
    return ChainedAnimationUtil.<AnimationCommand>createChainedAnimation(_reverseView, _function);
  }
  
  @Override
  public Animation createRedoAnimation(final CommandContext context) {
    final Function1<AnimationCommand, Animation> _function = (AnimationCommand it) -> {
      return it.getRedoAnimation(context);
    };
    return ChainedAnimationUtil.<AnimationCommand>createChainedAnimation(this.commands, _function);
  }
  
  @Override
  public String toString() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("SequentialAnimationCommand [");
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
