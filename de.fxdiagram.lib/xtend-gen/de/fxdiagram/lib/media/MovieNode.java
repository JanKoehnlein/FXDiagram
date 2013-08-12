package de.fxdiagram.lib.media;

import de.fxdiagram.core.XNode;
import de.fxdiagram.core.anchors.Anchors;
import de.fxdiagram.lib.anchors.RoundedRectangleAnchors;
import de.fxdiagram.lib.nodes.FlipNode;
import de.fxdiagram.lib.nodes.RectangleBorderPane;
import java.net.URL;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
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

@SuppressWarnings("all")
public class MovieNode extends XNode {
  private StackPane pane;
  
  private MediaPlayer player;
  
  private MediaView view;
  
  private Node controlBar;
  
  private int border = 10;
  
  public MovieNode() {
    HBox _createControlBar = this.createControlBar();
    this.controlBar = _createControlBar;
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
                it.setText("Movie");
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
        RectangleBorderPane _rectangleBorderPane_1 = new RectangleBorderPane();
        final Procedure1<RectangleBorderPane> _function_1 = new Procedure1<RectangleBorderPane>() {
          public void apply(final RectangleBorderPane it) {
            it.setId("pane");
            Insets _insets = new Insets(MovieNode.this.border, MovieNode.this.border, MovieNode.this.border, MovieNode.this.border);
            it.setPadding(_insets);
            ObservableList<Node> _children = it.getChildren();
            MediaView _mediaView = new MediaView();
            MediaView _view = MovieNode.this.view = _mediaView;
            _children.add(_view);
            final EventHandler<MouseEvent> _function = new EventHandler<MouseEvent>() {
              public void handle(final MouseEvent it) {
                FadeTransition _fadeTransition = new FadeTransition();
                final Procedure1<FadeTransition> _function = new Procedure1<FadeTransition>() {
                  public void apply(final FadeTransition it) {
                    it.setNode(MovieNode.this.controlBar);
                    it.setToValue(1.0);
                    Duration _millis = Duration.millis(200);
                    it.setDuration(_millis);
                    it.setInterpolator(Interpolator.EASE_OUT);
                    it.play();
                  }
                };
                ObjectExtensions.<FadeTransition>operator_doubleArrow(_fadeTransition, _function);
              }
            };
            it.setOnMouseEntered(_function);
            final EventHandler<MouseEvent> _function_1 = new EventHandler<MouseEvent>() {
              public void handle(final MouseEvent it) {
                FadeTransition _fadeTransition = new FadeTransition();
                final Procedure1<FadeTransition> _function = new Procedure1<FadeTransition>() {
                  public void apply(final FadeTransition it) {
                    it.setNode(MovieNode.this.controlBar);
                    it.setToValue(0);
                    Duration _millis = Duration.millis(200);
                    it.setDuration(_millis);
                    it.setInterpolator(Interpolator.EASE_OUT);
                    it.play();
                  }
                };
                ObjectExtensions.<FadeTransition>operator_doubleArrow(_fadeTransition, _function);
              }
            };
            it.setOnMouseExited(_function_1);
            ObservableList<Node> _children_1 = it.getChildren();
            Group _group = new Group();
            final Procedure1<Group> _function_2 = new Procedure1<Group>() {
              public void apply(final Group it) {
                ObservableList<Node> _children = it.getChildren();
                _children.add(MovieNode.this.controlBar);
                StackPane.setAlignment(it, Pos.BOTTOM_CENTER);
              }
            };
            Group _doubleArrow = ObjectExtensions.<Group>operator_doubleArrow(_group, _function_2);
            _children_1.add(_doubleArrow);
          }
        };
        RectangleBorderPane _doubleArrow_1 = ObjectExtensions.<RectangleBorderPane>operator_doubleArrow(_rectangleBorderPane_1, _function_1);
        StackPane _pane = MovieNode.this.pane = _doubleArrow_1;
        it.setBack(_pane);
      }
    };
    FlipNode _doubleArrow = ObjectExtensions.<FlipNode>operator_doubleArrow(_flipNode, _function);
    this.setNode(_doubleArrow);
    ObservableList<String> _stylesheets = this.getStylesheets();
    _stylesheets.add("de/fxdiagram/lib/media/MovieNode.css");
  }
  
  protected Anchors createAnchors() {
    RoundedRectangleAnchors _roundedRectangleAnchors = new RoundedRectangleAnchors(this, 12, 12);
    return _roundedRectangleAnchors;
  }
  
  protected HBox createControlBar() {
    HBox _hBox = new HBox();
    final Procedure1<HBox> _function = new Procedure1<HBox>() {
      public void apply(final HBox it) {
        it.setId("controlbar");
        it.setSpacing(0);
        it.setAlignment(Pos.CENTER);
        ObservableList<Node> _children = it.getChildren();
        Button _button = new Button();
        final Procedure1<Button> _function = new Procedure1<Button>() {
          public void apply(final Button it) {
            it.setId("back-button");
            it.setText("Back");
            final EventHandler<ActionEvent> _function = new EventHandler<ActionEvent>() {
              public void handle(final ActionEvent it) {
                MovieNode.this.player.seek(Duration.ZERO);
              }
            };
            it.setOnAction(_function);
          }
        };
        Button _doubleArrow = ObjectExtensions.<Button>operator_doubleArrow(_button, _function);
        _children.add(_doubleArrow);
        ObservableList<Node> _children_1 = it.getChildren();
        Button _button_1 = new Button();
        final Procedure1<Button> _function_1 = new Procedure1<Button>() {
          public void apply(final Button it) {
            it.setId("stop-button");
            it.setText("Stop");
            final EventHandler<ActionEvent> _function = new EventHandler<ActionEvent>() {
              public void handle(final ActionEvent it) {
                MovieNode.this.player.stop();
              }
            };
            it.setOnAction(_function);
          }
        };
        Button _doubleArrow_1 = ObjectExtensions.<Button>operator_doubleArrow(_button_1, _function_1);
        _children_1.add(_doubleArrow_1);
        ObservableList<Node> _children_2 = it.getChildren();
        Button _button_2 = new Button();
        final Procedure1<Button> _function_2 = new Procedure1<Button>() {
          public void apply(final Button it) {
            it.setId("play-button");
            it.setText("Play");
            final EventHandler<ActionEvent> _function = new EventHandler<ActionEvent>() {
              public void handle(final ActionEvent it) {
                MovieNode.this.player.play();
              }
            };
            it.setOnAction(_function);
          }
        };
        Button _doubleArrow_2 = ObjectExtensions.<Button>operator_doubleArrow(_button_2, _function_2);
        _children_2.add(_doubleArrow_2);
        ObservableList<Node> _children_3 = it.getChildren();
        Button _button_3 = new Button();
        final Procedure1<Button> _function_3 = new Procedure1<Button>() {
          public void apply(final Button it) {
            it.setId("pause-button");
            it.setText("Pause");
            final EventHandler<ActionEvent> _function = new EventHandler<ActionEvent>() {
              public void handle(final ActionEvent it) {
                MovieNode.this.player.pause();
              }
            };
            it.setOnAction(_function);
          }
        };
        Button _doubleArrow_3 = ObjectExtensions.<Button>operator_doubleArrow(_button_3, _function_3);
        _children_3.add(_doubleArrow_3);
        ObservableList<Node> _children_4 = it.getChildren();
        Button _button_4 = new Button();
        final Procedure1<Button> _function_4 = new Procedure1<Button>() {
          public void apply(final Button it) {
            it.setId("forward-button");
            it.setText("Forward");
            final EventHandler<ActionEvent> _function = new EventHandler<ActionEvent>() {
              public void handle(final ActionEvent it) {
                Duration _currentTime = MovieNode.this.player.getCurrentTime();
                double _seconds = _currentTime.toSeconds();
                double _plus = (_seconds + 10);
                Duration _seconds_1 = Duration.seconds(_plus);
                MovieNode.this.player.seek(_seconds_1);
              }
            };
            it.setOnAction(_function);
          }
        };
        Button _doubleArrow_4 = ObjectExtensions.<Button>operator_doubleArrow(_button_4, _function_4);
        _children_4.add(_doubleArrow_4);
        it.setOpacity(0);
      }
    };
    HBox _doubleArrow = ObjectExtensions.<HBox>operator_doubleArrow(_hBox, _function);
    return _doubleArrow;
  }
  
  public void setWidth(final double width) {
    super.setWidth(width);
    int _multiply = (2 * this.border);
    double _minus = (width - _multiply);
    this.view.setFitWidth(_minus);
  }
  
  public void setHeight(final double height) {
    super.setHeight(height);
    int _multiply = (2 * this.border);
    double _minus = (height - _multiply);
    this.view.setFitHeight(_minus);
  }
  
  public void setMovieUrl(final URL movieUrl) {
    String _string = movieUrl.toString();
    Media _media = new Media(_string);
    this.mediaProperty.set(_media);
    Media _media_1 = this.getMedia();
    MediaPlayer _mediaPlayer = new MediaPlayer(_media_1);
    this.player = _mediaPlayer;
    BooleanProperty _visibleProperty = this.pane.visibleProperty();
    final ChangeListener<Boolean> _function = new ChangeListener<Boolean>() {
      public void changed(final ObservableValue<? extends Boolean> prop, final Boolean oldVal, final Boolean newVal) {
        MediaPlayer _xifexpression = null;
        if ((newVal).booleanValue()) {
          _xifexpression = MovieNode.this.player;
        } else {
          _xifexpression = null;
        }
        MovieNode.this.view.setMediaPlayer(_xifexpression);
      }
    };
    _visibleProperty.addListener(_function);
  }
  
  public MediaPlayer getPlayer() {
    return this.player;
  }
  
  public MediaView getView() {
    return this.view;
  }
  
  private ReadOnlyObjectWrapper<Media> mediaProperty = new ReadOnlyObjectWrapper<Media>(this, "media");
  
  public Media getMedia() {
    return this.mediaProperty.get();
  }
  
  public ReadOnlyObjectProperty<Media> mediaProperty() {
    return this.mediaProperty.getReadOnlyProperty();
  }
}
