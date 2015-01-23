package de.fxdiagram.core.layout.tests;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class OfflineLayoutTest extends Application {
  public static void main(final String[] args) {
    Application.launch();
  }
  
  @Override
  public void start(final Stage primaryStage) throws Exception {
    final Text text = new Text("foo");
    VBox _vBox = new VBox();
    final Procedure1<VBox> _function = (VBox it) -> {
      it.setSpacing(10);
      ObservableList<Node> _children = it.getChildren();
      _children.add(text);
      ObservableList<Node> _children_1 = it.getChildren();
      Text _text = new Text("barbar");
      _children_1.add(_text);
    };
    final VBox root = ObjectExtensions.<VBox>operator_doubleArrow(_vBox, _function);
    final Scene scene = new Scene(root);
    root.autosize();
    double _width = root.getWidth();
    String _plus = (Double.valueOf(_width) + " ");
    double _height = root.getHeight();
    String _plus_1 = (_plus + Double.valueOf(_height));
    InputOutput.<String>println(_plus_1);
  }
}
