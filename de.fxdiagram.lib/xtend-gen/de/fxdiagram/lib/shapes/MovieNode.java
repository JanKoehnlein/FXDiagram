package de.fxdiagram.lib.shapes;

import com.google.common.base.Objects;
import de.fxdiagram.core.XNode;
import java.net.URL;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.media.MediaView;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class MovieNode extends XNode {
  private Media media;
  
  private MediaPlayer player;
  
  private MediaView view;
  
  private int border = 10;
  
  public MovieNode(final URL movieUrl) {
    StackPane _stackPane = new StackPane();
    final Procedure1<StackPane> _function = new Procedure1<StackPane>() {
        public void apply(final StackPane it) {
          it.setStyle("-fx-background-color: #888888;");
          Insets _insets = new Insets(MovieNode.this.border, MovieNode.this.border, MovieNode.this.border, MovieNode.this.border);
          it.setPadding(_insets);
          ObservableList<Node> _children = it.getChildren();
          MediaView _mediaView = new MediaView();
          final Procedure1<MediaView> _function = new Procedure1<MediaView>() {
              public void apply(final MediaView it) {
                String _string = movieUrl.toString();
                Media _media = new Media(_string);
                MovieNode.this.media = _media;
                MediaPlayer _mediaPlayer = new MediaPlayer(MovieNode.this.media);
                MediaPlayer _player = MovieNode.this.player = _mediaPlayer;
                it.setMediaPlayer(_player);
                final EventHandler<MouseEvent> _function = new EventHandler<MouseEvent>() {
                    public void handle(final MouseEvent it) {
                      Status _status = MovieNode.this.player.getStatus();
                      final Status _switchValue = _status;
                      boolean _matched = false;
                      if (!_matched) {
                        if (Objects.equal(_switchValue,Status.PLAYING)) {
                          _matched=true;
                          MovieNode.this.player.pause();
                        }
                      }
                      if (!_matched) {
                        if (Objects.equal(_switchValue,Status.PAUSED)) {
                          _matched=true;
                          MovieNode.this.player.play();
                        }
                      }
                      if (!_matched) {
                        if (Objects.equal(_switchValue,Status.READY)) {
                          _matched=true;
                          MovieNode.this.player.play();
                        }
                      }
                      if (!_matched) {
                        if (Objects.equal(_switchValue,Status.STOPPED)) {
                          _matched=true;
                          MovieNode.this.player.play();
                        }
                      }
                    }
                  };
                it.setOnMouseClicked(_function);
              }
            };
          MediaView _doubleArrow = ObjectExtensions.<MediaView>operator_doubleArrow(_mediaView, _function);
          MediaView _view = MovieNode.this.view = _doubleArrow;
          _children.add(_view);
        }
      };
    StackPane _doubleArrow = ObjectExtensions.<StackPane>operator_doubleArrow(_stackPane, _function);
    this.setNode(_doubleArrow);
  }
  
  public void setWidth(final double width) {
    int _multiply = (2 * this.border);
    double _minus = (width - _multiply);
    this.view.setFitWidth(_minus);
  }
  
  public void setHeight(final double height) {
    int _multiply = (2 * this.border);
    double _minus = (height - _multiply);
    this.view.setFitHeight(_minus);
  }
  
  public Media getMedia() {
    return this.media;
  }
  
  public MediaPlayer getPlayer() {
    return this.player;
  }
  
  public MediaView getView() {
    return this.view;
  }
}
