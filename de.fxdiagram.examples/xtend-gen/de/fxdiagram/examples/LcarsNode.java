package de.fxdiagram.examples;

import de.fxdiagram.core.XNode;
import de.fxdiagram.lib.nodes.RectangleBorderPane;
import java.util.List;
import java.util.Map;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class LcarsNode extends XNode {
  private String name;
  
  private String imageUrl;
  
  public LcarsNode(final Map<String,Object> entry) {
    Object _get = entry.get("name");
    String _string = _get.toString();
    this.name = _string;
    Object _get_1 = entry.get("imageUrl");
    String _last = IterableExtensions.<String>last(((List<String>) _get_1));
    this.imageUrl = _last;
    RectangleBorderPane _rectangleBorderPane = new RectangleBorderPane();
    final Procedure1<RectangleBorderPane> _function = new Procedure1<RectangleBorderPane>() {
        public void apply(final RectangleBorderPane it) {
          ObservableList<Node> _children = it.getChildren();
          VBox _vBox = new VBox();
          final Procedure1<VBox> _function = new Procedure1<VBox>() {
              public void apply(final VBox it) {
                ObservableList<Node> _children = it.getChildren();
                ImageView _imageView = new ImageView();
                final Procedure1<ImageView> _function = new Procedure1<ImageView>() {
                    public void apply(final ImageView it) {
                      Image _image = new Image(LcarsNode.this.imageUrl);
                      it.setImage(_image);
                    }
                  };
                ImageView _doubleArrow = ObjectExtensions.<ImageView>operator_doubleArrow(_imageView, _function);
                _children.add(_doubleArrow);
                ObservableList<Node> _children_1 = it.getChildren();
                Text _text = new Text();
                final Procedure1<Text> _function_1 = new Procedure1<Text>() {
                    public void apply(final Text it) {
                      it.setText(LcarsNode.this.name);
                      it.setTextOrigin(VPos.TOP);
                      Insets _insets = new Insets(5, 10, 5, 10);
                      VBox.setMargin(it, _insets);
                    }
                  };
                Text _doubleArrow_1 = ObjectExtensions.<Text>operator_doubleArrow(_text, _function_1);
                _children_1.add(_doubleArrow_1);
              }
            };
          VBox _doubleArrow = ObjectExtensions.<VBox>operator_doubleArrow(_vBox, _function);
          _children.add(_doubleArrow);
        }
      };
    RectangleBorderPane _doubleArrow = ObjectExtensions.<RectangleBorderPane>operator_doubleArrow(_rectangleBorderPane, _function);
    this.setNode(_doubleArrow);
    this.setKey(this.name);
  }
}
