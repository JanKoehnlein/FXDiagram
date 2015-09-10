package de.fxdigram.lib.layout.tests;

import com.google.common.collect.Iterables;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.command.AbstractAnimationCommand;
import de.fxdiagram.core.command.AnimationCommand;
import de.fxdiagram.core.command.CommandContext;
import de.fxdiagram.core.command.CommandStack;
import de.fxdiagram.core.command.SequentialAnimationCommand;
import de.fxdiagram.lib.animations.Inflator;
import de.fxdiagram.lib.nodes.AbstractClassNode;
import de.fxdiagram.lib.nodes.ClassModel;
import java.util.Collections;
import javafx.animation.Animation;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class InflatorTest extends Application {
  public static void main(final String[] args) {
    Application.launch();
  }
  
  @Override
  public void start(final Stage stage) throws Exception {
    final XRoot root = new XRoot();
    Scene _scene = new Scene(root, 640, 480);
    stage.setScene(_scene);
    final AbstractClassNode node = new AbstractClassNode() {
      @Override
      public ClassModel inferClassModel() {
        ClassModel _classModel = new ClassModel();
        final Procedure1<ClassModel> _function = (ClassModel it) -> {
          it.setName("Foo");
          ObservableList<String> _attributes = it.getAttributes();
          Iterables.<String>addAll(_attributes, Collections.<String>unmodifiableList(CollectionLiterals.<String>newArrayList("a", "b")));
          ObservableList<String> _operations = it.getOperations();
          Iterables.<String>addAll(_operations, Collections.<String>unmodifiableList(CollectionLiterals.<String>newArrayList("a", "b")));
        };
        return ObjectExtensions.<ClassModel>operator_doubleArrow(_classModel, _function);
      }
    };
    final Procedure1<XRoot> _function = (XRoot it) -> {
      XDiagram _xDiagram = new XDiagram();
      final Procedure1<XDiagram> _function_1 = (XDiagram it_1) -> {
        it_1.activate();
        ObservableList<XNode> _nodes = it_1.getNodes();
        _nodes.add(node);
      };
      XDiagram _doubleArrow = ObjectExtensions.<XDiagram>operator_doubleArrow(_xDiagram, _function_1);
      it.setDiagram(_doubleArrow);
    };
    ObjectExtensions.<XRoot>operator_doubleArrow(root, _function);
    stage.show();
    final EventHandler<MouseEvent> _function_1 = (MouseEvent it) -> {
      CommandStack _commandStack = root.getCommandStack();
      SequentialAnimationCommand _sequentialAnimationCommand = new SequentialAnimationCommand();
      final Procedure1<SequentialAnimationCommand> _function_2 = (SequentialAnimationCommand it_1) -> {
        Inflator _inflator = node.getInflator();
        AbstractAnimationCommand _deflateCommand = _inflator.getDeflateCommand();
        it_1.operator_add(_deflateCommand);
        it_1.operator_add(new AnimationCommand() {
          @Override
          public Animation getExecuteAnimation(final CommandContext context) {
            Object _xblockexpression = null;
            {
              ClassModel _classModel = new ClassModel();
              final Procedure1<ClassModel> _function = (ClassModel it_2) -> {
                it_2.setName("Bar");
                ObservableList<String> _attributes = it_2.getAttributes();
                ClassModel _model = node.getModel();
                ObservableList<String> _attributes_1 = _model.getAttributes();
                Iterables.<String>addAll(_attributes, _attributes_1);
                ObservableList<String> _attributes_2 = it_2.getAttributes();
                _attributes_2.add("foo");
                ObservableList<String> _operations = it_2.getOperations();
                ClassModel _model_1 = node.getModel();
                ObservableList<String> _operations_1 = _model_1.getOperations();
                Iterables.<String>addAll(_operations, _operations_1);
                ObservableList<String> _operations_2 = it_2.getOperations();
                _operations_2.add("foo");
              };
              ClassModel _doubleArrow = ObjectExtensions.<ClassModel>operator_doubleArrow(_classModel, _function);
              node.setModel(_doubleArrow);
              _xblockexpression = null;
            }
            return ((Animation)_xblockexpression);
          }
          
          @Override
          public Animation getUndoAnimation(final CommandContext context) {
            return null;
          }
          
          @Override
          public Animation getRedoAnimation(final CommandContext context) {
            return null;
          }
          
          @Override
          public boolean clearRedoStackOnExecute() {
            return false;
          }
          
          @Override
          public void skipViewportRestore() {
          }
        });
        Inflator _inflator_1 = node.getInflator();
        AbstractAnimationCommand _inflateCommand = _inflator_1.getInflateCommand();
        it_1.operator_add(_inflateCommand);
      };
      SequentialAnimationCommand _doubleArrow = ObjectExtensions.<SequentialAnimationCommand>operator_doubleArrow(_sequentialAnimationCommand, _function_2);
      _commandStack.execute(_doubleArrow);
    };
    root.setOnMouseClicked(_function_1);
  }
}
