package de.fxdiagram.examples.neonsign;

import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.extensions.ClassLoaderExtensions;
import de.fxdiagram.core.extensions.TooltipExtensions;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.core.model.ToString;
import de.fxdiagram.core.services.ImageCache;
import de.fxdiagram.lib.nodes.FlipNode;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@ModelNode
@SuppressWarnings("all")
public class NeonSignNode extends FlipNode {
  private TextField textField;
  
  private Text neonText;
  
  public NeonSignNode(final String name) {
    super(name);
  }
  
  @Override
  protected Node createNode() {
    Node _xblockexpression = null;
    {
      final Node node = super.createNode();
      VBox _neonSign = this.getNeonSign();
      this.setFront(_neonSign);
      ImageView _imageView = new ImageView();
      final Procedure1<ImageView> _function = (ImageView it) -> {
        ImageCache _get = ImageCache.get();
        Image _image = _get.getImage(this, "code.png");
        it.setImage(_image);
      };
      ImageView _doubleArrow = ObjectExtensions.<ImageView>operator_doubleArrow(_imageView, _function);
      this.setBack(_doubleArrow);
      _xblockexpression = node;
    }
    return _xblockexpression;
  }
  
  @Override
  public void doActivate() {
    super.doActivate();
    Node _front = this.getFront();
    final Procedure1<Node> _function = (Node it) -> {
      TooltipExtensions.setTooltip(it, "Double-click for Xtend code");
      final EventHandler<MouseEvent> _function_1 = (MouseEvent it_1) -> {
        Timeline _timeline = new Timeline();
        final Procedure1<Timeline> _function_2 = (Timeline it_2) -> {
          it_2.setCycleCount(20);
          ObservableList<KeyFrame> _keyFrames = it_2.getKeyFrames();
          Duration _millis = Duration.millis(10);
          DoubleProperty _opacityProperty = this.neonText.opacityProperty();
          KeyValue _keyValue = new <Number>KeyValue(_opacityProperty, Double.valueOf(0.45));
          KeyFrame _keyFrame = new KeyFrame(_millis, _keyValue);
          _keyFrames.add(_keyFrame);
          ObservableList<KeyFrame> _keyFrames_1 = it_2.getKeyFrames();
          Duration _millis_1 = Duration.millis(20);
          DoubleProperty _opacityProperty_1 = this.neonText.opacityProperty();
          KeyValue _keyValue_1 = new <Number>KeyValue(_opacityProperty_1, Double.valueOf(0.95));
          KeyFrame _keyFrame_1 = new KeyFrame(_millis_1, _keyValue_1);
          _keyFrames_1.add(_keyFrame_1);
          ObservableList<KeyFrame> _keyFrames_2 = it_2.getKeyFrames();
          Duration _millis_2 = Duration.millis(40);
          DoubleProperty _opacityProperty_2 = this.neonText.opacityProperty();
          KeyValue _keyValue_2 = new <Number>KeyValue(_opacityProperty_2, Double.valueOf(0.65));
          KeyFrame _keyFrame_2 = new KeyFrame(_millis_2, _keyValue_2);
          _keyFrames_2.add(_keyFrame_2);
          ObservableList<KeyFrame> _keyFrames_3 = it_2.getKeyFrames();
          Duration _millis_3 = Duration.millis(50);
          DoubleProperty _opacityProperty_3 = this.neonText.opacityProperty();
          KeyValue _keyValue_3 = new <Number>KeyValue(_opacityProperty_3, Integer.valueOf(1));
          KeyFrame _keyFrame_3 = new KeyFrame(_millis_3, _keyValue_3);
          _keyFrames_3.add(_keyFrame_3);
          it_2.play();
        };
        ObjectExtensions.<Timeline>operator_doubleArrow(_timeline, _function_2);
      };
      it.setOnMouseClicked(_function_1);
    };
    ObjectExtensions.<Node>operator_doubleArrow(_front, _function);
  }
  
  protected VBox getNeonSign() {
    VBox _vBox = new VBox();
    final Procedure1<VBox> _function = (VBox it) -> {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("-fx-background-image: url(\"");
      String _uRI = ClassLoaderExtensions.toURI(this, "brick.jpg");
      _builder.append(_uRI, "");
      _builder.append("\");");
      _builder.newLineIfNotEmpty();
      it.setStyle(_builder.toString());
      ObservableList<Node> _children = it.getChildren();
      TextField _textField = new TextField();
      final Procedure1<TextField> _function_1 = (TextField it_1) -> {
        it_1.setText("JavaFX loves Xtend");
        Insets _insets = new Insets(10, 40, 10, 40);
        VBox.setMargin(it_1, _insets);
      };
      TextField _doubleArrow = ObjectExtensions.<TextField>operator_doubleArrow(_textField, _function_1);
      TextField _textField_1 = (this.textField = _doubleArrow);
      _children.add(_textField_1);
      ObservableList<Node> _children_1 = it.getChildren();
      Text _text = new Text();
      final Procedure1<Text> _function_2 = (Text it_1) -> {
        StringProperty _textProperty = it_1.textProperty();
        StringProperty _textProperty_1 = this.textField.textProperty();
        _textProperty.bind(_textProperty_1);
        it_1.setWrappingWidth(580);
        it_1.setTextAlignment(TextAlignment.CENTER);
        it_1.setRotate((-7));
        Font _font = Font.font("Nanum Pen Script", 100);
        it_1.setFont(_font);
        Color _web = Color.web("#feeb42");
        it_1.setFill(_web);
        Blend _blend = new Blend();
        final Procedure1<Blend> _function_3 = (Blend it_2) -> {
          it_2.setMode(BlendMode.MULTIPLY);
          Bloom _bloom = new Bloom();
          it_2.setTopInput(_bloom);
          InnerShadow _innerShadow = new InnerShadow();
          final Procedure1<InnerShadow> _function_4 = (InnerShadow it_3) -> {
            Color _web_1 = Color.web("#f13a00");
            it_3.setColor(_web_1);
            it_3.setRadius(5);
            it_3.setChoke(0.4);
          };
          InnerShadow _doubleArrow_1 = ObjectExtensions.<InnerShadow>operator_doubleArrow(_innerShadow, _function_4);
          it_2.setBottomInput(_doubleArrow_1);
        };
        Blend _doubleArrow_1 = ObjectExtensions.<Blend>operator_doubleArrow(_blend, _function_3);
        it_1.setEffect(_doubleArrow_1);
      };
      Text _doubleArrow_1 = ObjectExtensions.<Text>operator_doubleArrow(_text, _function_2);
      Text _neonText = (this.neonText = _doubleArrow_1);
      _children_1.add(_neonText);
    };
    return ObjectExtensions.<VBox>operator_doubleArrow(_vBox, _function);
  }
  
  /**
   * Automatically generated by @ModelNode. Needed for deserialization.
   */
  public NeonSignNode() {
  }
  
  public void populate(final ModelElementImpl modelElement) {
    super.populate(modelElement);
  }
  
  public String toString() {
    return ToString.toString(this);
  }
}
