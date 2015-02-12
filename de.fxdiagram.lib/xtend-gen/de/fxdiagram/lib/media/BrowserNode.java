package de.fxdiagram.lib.media;

import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.anchors.Anchors;
import de.fxdiagram.core.extensions.TooltipExtensions;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.lib.anchors.RoundedRectangleAnchors;
import de.fxdiagram.lib.nodes.FlipNode;
import de.fxdiagram.lib.nodes.RectangleBorderPane;
import java.net.URL;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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

@ModelNode("pageUrl")
@SuppressWarnings("all")
public class BrowserNode extends FlipNode {
  public BrowserNode(final String name) {
    super(name);
  }
  
  @Override
  protected Node createNode() {
    Node _xblockexpression = null;
    {
      final Node node = super.createNode();
      RectangleBorderPane _rectangleBorderPane = new RectangleBorderPane();
      final Procedure1<RectangleBorderPane> _function = new Procedure1<RectangleBorderPane>() {
        @Override
        public void apply(final RectangleBorderPane it) {
          ObservableList<Node> _children = it.getChildren();
          Text _text = new Text();
          final Procedure1<Text> _function = new Procedure1<Text>() {
            @Override
            public void apply(final Text it) {
              String _name = BrowserNode.this.getName();
              it.setText(_name);
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
      this.setFront(_doubleArrow);
      WebView _webView = new WebView();
      final Procedure1<WebView> _function_1 = new Procedure1<WebView>() {
        @Override
        public void apply(final WebView it) {
          WebEngine _engine = it.getEngine();
          String _pageUrl = BrowserNode.this.getPageUrl();
          _engine.load(_pageUrl);
        }
      };
      WebView _doubleArrow_1 = ObjectExtensions.<WebView>operator_doubleArrow(_webView, _function_1);
      this.setBack(_doubleArrow_1);
      _xblockexpression = node;
    }
    return _xblockexpression;
  }
  
  @Override
  public void doActivate() {
    super.doActivate();
    Node _front = this.getFront();
    TooltipExtensions.setTooltip(_front, "Double-click to browse");
    Node _back = this.getBack();
    TooltipExtensions.setTooltip(_back, "Double-click to close");
  }
  
  public void setPageUrl(final URL pageUrl) {
    String _string = pageUrl.toString();
    this.setPageUrl(_string);
  }
  
  @Override
  protected Anchors createAnchors() {
    return new RoundedRectangleAnchors(this, 12, 12);
  }
  
  /**
   * Automatically generated by @ModelNode. Needed for deserialization.
   */
  public BrowserNode() {
  }
  
  public void populate(final ModelElementImpl modelElement) {
    super.populate(modelElement);
    modelElement.addProperty(pageUrlProperty, String.class);
  }
  
  private SimpleStringProperty pageUrlProperty = new SimpleStringProperty(this, "pageUrl");
  
  public String getPageUrl() {
    return this.pageUrlProperty.get();
  }
  
  public void setPageUrl(final String pageUrl) {
    this.pageUrlProperty.set(pageUrl);
  }
  
  public StringProperty pageUrlProperty() {
    return this.pageUrlProperty;
  }
}
