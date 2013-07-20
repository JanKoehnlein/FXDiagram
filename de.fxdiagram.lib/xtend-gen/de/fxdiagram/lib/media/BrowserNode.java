package de.fxdiagram.lib.media;

import de.fxdiagram.core.XNode;
import de.fxdiagram.core.anchors.Anchors;
import de.fxdiagram.lib.anchors.RoundedRectangleAnchors;
import de.fxdiagram.lib.nodes.FlipNode;
import de.fxdiagram.lib.nodes.RectangleBorderPane;
import java.net.URL;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class BrowserNode extends XNode {
  private WebView view;
  
  public BrowserNode() {
    FlipNode _flipNode = new FlipNode();
    final Procedure1<FlipNode> _function = new Procedure1<FlipNode>() {
        public void apply(final FlipNode it) {
          RectangleBorderPane _rectangleBorderPane = new RectangleBorderPane();
          final Procedure1<RectangleBorderPane> _function = new Procedure1<RectangleBorderPane>() {
              public void apply(final RectangleBorderPane it) {
                ObservableList<Node> _children = it.getChildren();
                Text _text = new Text();
                final Procedure1<Text> _function = new Procedure1<Text>() {
                    public void apply(final Text it) {
                      it.setText("My Blog");
                      it.setTextOrigin(VPos.TOP);
                      Insets _insets = new Insets(10, 20, 10, 20);
                      StackPane.setMargin(it, _insets);
                    }
                  };
                Text _doubleArrow = ObjectExtensions.<Text>operator_doubleArrow(_text, _function);
                _children.add(_doubleArrow);
              }
            };
          RectangleBorderPane _doubleArrow = ObjectExtensions.<RectangleBorderPane>operator_doubleArrow(_rectangleBorderPane, _function);
          it.setFront(_doubleArrow);
          WebView _webView = new WebView();
          WebView _view = BrowserNode.this.view = _webView;
          it.setBack(_view);
        }
      };
    FlipNode _doubleArrow = ObjectExtensions.<FlipNode>operator_doubleArrow(_flipNode, _function);
    this.setNode(_doubleArrow);
  }
  
  public void setPageUrl(final URL pageUrl) {
    WebEngine _engine = this.view.getEngine();
    String _string = pageUrl.toString();
    _engine.load(_string);
  }
  
  public WebView getView() {
    return this.view;
  }
  
  protected Anchors createAnchors() {
    RoundedRectangleAnchors _roundedRectangleAnchors = new RoundedRectangleAnchors(this, 12, 12);
    return _roundedRectangleAnchors;
  }
}
