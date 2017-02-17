package de.fxdiagram.lib.media;

import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.anchors.Anchors;
import de.fxdiagram.core.extensions.ClassLoaderExtensions;
import de.fxdiagram.core.extensions.TooltipExtensions;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.core.model.ToString;
import de.fxdiagram.core.services.ResourceDescriptor;
import de.fxdiagram.lib.anchors.RoundedRectangleAnchors;
import de.fxdiagram.lib.nodes.FlipNode;
import de.fxdiagram.lib.nodes.RectangleBorderPane;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@ModelNode
@SuppressWarnings("all")
public class MovieNode extends FlipNode {
  private Media media;
  
  private StackPane pane = new RectangleBorderPane();
  
  private MediaPlayer player;
  
  private MediaView view = new MediaView();
  
  private Node controlBar;
  
  private int border = 10;
  
  public MovieNode(final ResourceDescriptor movieDescriptor) {
    super(movieDescriptor);
  }
  
  @Override
  protected Node createNode() {
    Node _xblockexpression = null;
    {
      final Node node = super.createNode();
      DomainObjectDescriptor _domainObjectDescriptor = this.getDomainObjectDescriptor();
      String _uRI = ((ResourceDescriptor) _domainObjectDescriptor).toURI();
      Media _media = new Media(_uRI);
      this.media = _media;
      MediaPlayer _mediaPlayer = new MediaPlayer(this.media);
      this.player = _mediaPlayer;
      RectangleBorderPane _rectangleBorderPane = new RectangleBorderPane();
      final Procedure1<RectangleBorderPane> _function = (RectangleBorderPane it) -> {
        ObservableList<Node> _children = it.getChildren();
        Text _text = new Text();
        final Procedure1<Text> _function_1 = (Text it_1) -> {
          String _name = this.getName();
          it_1.setText(_name);
          it_1.setTextOrigin(VPos.TOP);
          Insets _insets = new Insets(10, 20, 10, 20);
          StackPane.setMargin(it_1, _insets);
        };
        Text _doubleArrow = ObjectExtensions.<Text>operator_doubleArrow(_text, _function_1);
        _children.add(_doubleArrow);
      };
      RectangleBorderPane _doubleArrow = ObjectExtensions.<RectangleBorderPane>operator_doubleArrow(_rectangleBorderPane, _function);
      this.setFront(_doubleArrow);
      final Procedure1<StackPane> _function_1 = (StackPane it) -> {
        it.setId("pane");
        Insets _insets = new Insets(this.border, this.border, this.border, this.border);
        it.setPadding(_insets);
        ObservableList<Node> _children = it.getChildren();
        _children.add(this.view);
      };
      StackPane _doubleArrow_1 = ObjectExtensions.<StackPane>operator_doubleArrow(this.pane, _function_1);
      this.setBack(_doubleArrow_1);
      _xblockexpression = node;
    }
    return _xblockexpression;
  }
  
  @Override
  public void initializeGraphics() {
    super.initializeGraphics();
    ObservableList<String> _stylesheets = this.getStylesheets();
    String _uRI = ClassLoaderExtensions.toURI(this, "MovieNode.css");
    _stylesheets.add(_uRI);
  }
  
  @Override
  public void doActivate() {
    super.doActivate();
    Node _front = this.getFront();
    TooltipExtensions.setTooltip(_front, "Double-click to watch");
    BooleanProperty _visibleProperty = this.pane.visibleProperty();
    final ChangeListener<Boolean> _function = (ObservableValue<? extends Boolean> prop, Boolean oldVal, Boolean newVal) -> {
      MediaPlayer _xifexpression = null;
      if ((newVal).booleanValue()) {
        _xifexpression = this.player;
      } else {
        _xifexpression = null;
      }
      this.view.setMediaPlayer(_xifexpression);
    };
    _visibleProperty.addListener(_function);
    HBox _createControlBar = this.createControlBar();
    this.controlBar = _createControlBar;
    final Procedure1<StackPane> _function_1 = (StackPane it) -> {
      final EventHandler<MouseEvent> _function_2 = (MouseEvent it_1) -> {
        FadeTransition _fadeTransition = new FadeTransition();
        final Procedure1<FadeTransition> _function_3 = (FadeTransition it_2) -> {
          it_2.setNode(this.controlBar);
          it_2.setToValue(1.0);
          Duration _millis = Duration.millis(200);
          it_2.setDuration(_millis);
          it_2.setInterpolator(Interpolator.EASE_OUT);
          it_2.play();
        };
        ObjectExtensions.<FadeTransition>operator_doubleArrow(_fadeTransition, _function_3);
      };
      it.setOnMouseEntered(_function_2);
      final EventHandler<MouseEvent> _function_3 = (MouseEvent it_1) -> {
        FadeTransition _fadeTransition = new FadeTransition();
        final Procedure1<FadeTransition> _function_4 = (FadeTransition it_2) -> {
          it_2.setNode(this.controlBar);
          it_2.setToValue(0);
          Duration _millis = Duration.millis(200);
          it_2.setDuration(_millis);
          it_2.setInterpolator(Interpolator.EASE_OUT);
          it_2.play();
        };
        ObjectExtensions.<FadeTransition>operator_doubleArrow(_fadeTransition, _function_4);
      };
      it.setOnMouseExited(_function_3);
      ObservableList<Node> _children = it.getChildren();
      Group _group = new Group();
      final Procedure1<Group> _function_4 = (Group it_1) -> {
        ObservableList<Node> _children_1 = it_1.getChildren();
        _children_1.add(this.controlBar);
        StackPane.setAlignment(it_1, Pos.BOTTOM_CENTER);
      };
      Group _doubleArrow = ObjectExtensions.<Group>operator_doubleArrow(_group, _function_4);
      _children.add(_doubleArrow);
      TooltipExtensions.setTooltip(it, "Double-click to close");
    };
    ObjectExtensions.<StackPane>operator_doubleArrow(
      this.pane, _function_1);
  }
  
  @Override
  protected Anchors createAnchors() {
    return new RoundedRectangleAnchors(this, 12, 12);
  }
  
  protected HBox createControlBar() {
    HBox _hBox = new HBox();
    final Procedure1<HBox> _function = (HBox it) -> {
      it.setId("controlbar");
      it.setSpacing(0);
      it.setAlignment(Pos.CENTER);
      ObservableList<Node> _children = it.getChildren();
      Button _button = new Button();
      final Procedure1<Button> _function_1 = (Button it_1) -> {
        it_1.setId("back-button");
        it_1.setText("Back");
        final EventHandler<ActionEvent> _function_2 = (ActionEvent it_2) -> {
          this.player.seek(Duration.ZERO);
        };
        it_1.setOnAction(_function_2);
      };
      Button _doubleArrow = ObjectExtensions.<Button>operator_doubleArrow(_button, _function_1);
      _children.add(_doubleArrow);
      ObservableList<Node> _children_1 = it.getChildren();
      Button _button_1 = new Button();
      final Procedure1<Button> _function_2 = (Button it_1) -> {
        it_1.setId("stop-button");
        it_1.setText("Stop");
        final EventHandler<ActionEvent> _function_3 = (ActionEvent it_2) -> {
          this.player.stop();
        };
        it_1.setOnAction(_function_3);
      };
      Button _doubleArrow_1 = ObjectExtensions.<Button>operator_doubleArrow(_button_1, _function_2);
      _children_1.add(_doubleArrow_1);
      ObservableList<Node> _children_2 = it.getChildren();
      Button _button_2 = new Button();
      final Procedure1<Button> _function_3 = (Button it_1) -> {
        it_1.setId("play-button");
        it_1.setText("Play");
        final EventHandler<ActionEvent> _function_4 = (ActionEvent it_2) -> {
          this.player.play();
        };
        it_1.setOnAction(_function_4);
      };
      Button _doubleArrow_2 = ObjectExtensions.<Button>operator_doubleArrow(_button_2, _function_3);
      _children_2.add(_doubleArrow_2);
      ObservableList<Node> _children_3 = it.getChildren();
      Button _button_3 = new Button();
      final Procedure1<Button> _function_4 = (Button it_1) -> {
        it_1.setId("pause-button");
        it_1.setText("Pause");
        final EventHandler<ActionEvent> _function_5 = (ActionEvent it_2) -> {
          this.player.pause();
        };
        it_1.setOnAction(_function_5);
      };
      Button _doubleArrow_3 = ObjectExtensions.<Button>operator_doubleArrow(_button_3, _function_4);
      _children_3.add(_doubleArrow_3);
      ObservableList<Node> _children_4 = it.getChildren();
      Button _button_4 = new Button();
      final Procedure1<Button> _function_5 = (Button it_1) -> {
        it_1.setId("forward-button");
        it_1.setText("Forward");
        final EventHandler<ActionEvent> _function_6 = (ActionEvent it_2) -> {
          Duration _currentTime = this.player.getCurrentTime();
          double _seconds = _currentTime.toSeconds();
          double _plus = (_seconds + 10);
          Duration _seconds_1 = Duration.seconds(_plus);
          this.player.seek(_seconds_1);
        };
        it_1.setOnAction(_function_6);
      };
      Button _doubleArrow_4 = ObjectExtensions.<Button>operator_doubleArrow(_button_4, _function_5);
      _children_4.add(_doubleArrow_4);
      it.setOpacity(0);
    };
    return ObjectExtensions.<HBox>operator_doubleArrow(_hBox, _function);
  }
  
  @Override
  public void setWidth(final double width) {
    super.setWidth(width);
    this.view.setFitWidth((width - (2 * this.border)));
  }
  
  @Override
  public void setHeight(final double height) {
    super.setHeight(height);
    this.view.setFitHeight((height - (2 * this.border)));
  }
  
  public MediaPlayer getPlayer() {
    return this.player;
  }
  
  public MediaView getView() {
    return this.view;
  }
  
  /**
   * Automatically generated by @ModelNode. Needed for deserialization.
   */
  public MovieNode() {
  }
  
  public void populate(final ModelElementImpl modelElement) {
    super.populate(modelElement);
  }
  
  public void postLoad() {
    
  }
  
  public String toString() {
    return ToString.toString(this);
  }
}
