package de.fxdiagram.lib.shapes;

import com.google.common.base.Objects;
import de.fxdiagram.core.Extensions;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.export.SvgExportable;
import de.fxdiagram.core.export.SvgExporter;
import de.fxdiagram.core.geometry.TransformExtensions;
import java.net.URL;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.media.MediaView;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Transform;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class MovieNode extends XNode implements SvgExportable {
  private static int instanceCount;
  
  private MediaPlayer player;
  
  private MediaView view;
  
  private int border = 10;
  
  public MovieNode() {
    StackPane _stackPane = new StackPane();
    final Procedure1<StackPane> _function = new Procedure1<StackPane>() {
        public void apply(final StackPane it) {
          it.setStyle("-fx-background-color: #888888;");
          Insets _insets = new Insets(MovieNode.this.border, MovieNode.this.border, MovieNode.this.border, MovieNode.this.border);
          it.setPadding(_insets);
          ObservableList<Node> _children = it.getChildren();
          MediaView _mediaView = new MediaView();
          MediaView _view = MovieNode.this.view = _mediaView;
          _children.add(_view);
        }
      };
    StackPane _doubleArrow = ObjectExtensions.<StackPane>operator_doubleArrow(_stackPane, _function);
    this.setNode(_doubleArrow);
    String _plus = ("MovieNode" + Integer.valueOf(MovieNode.instanceCount));
    this.setKey(_plus);
    int _plus_1 = (MovieNode.instanceCount + 1);
    MovieNode.instanceCount = _plus_1;
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
    MediaPlayer _player = this.player = _mediaPlayer;
    this.view.setMediaPlayer(_player);
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
    this.setOnMouseClicked(_function);
  }
  
  public MediaPlayer getPlayer() {
    return this.player;
  }
  
  public MediaView getView() {
    return this.view;
  }
  
  public CharSequence toSvgElement(@Extension final SvgExporter exporter) {
    CharSequence _xblockexpression = null;
    {
      double _fitWidth = this.view.getFitWidth();
      Media _media = this.getMedia();
      int _width = _media.getWidth();
      double _divide = (_fitWidth / _width);
      double _fitHeight = this.view.getFitHeight();
      Media _media_1 = this.getMedia();
      int _height = _media_1.getHeight();
      double _divide_1 = (_fitHeight / _height);
      final double mediaScale = Math.min(_divide, _divide_1);
      Media _media_2 = this.getMedia();
      int _width_1 = _media_2.getWidth();
      int _multiply = (2 * this.border);
      double _divide_2 = (_multiply / mediaScale);
      double _plus = (_width_1 + _divide_2);
      final int imageWidth = ((int) _plus);
      Media _media_3 = this.getMedia();
      int _height_1 = _media_3.getHeight();
      int _multiply_1 = (2 * this.border);
      double _divide_3 = (_multiply_1 / mediaScale);
      double _plus_1 = (_height_1 + _divide_3);
      final int imageHeight = ((int) _plus_1);
      WritableImage _writableImage = new WritableImage(imageWidth, imageHeight);
      final WritableImage image = _writableImage;
      Bounds _layoutBounds = this.getLayoutBounds();
      double _width_2 = _layoutBounds.getWidth();
      final double scale = (imageWidth / _width_2);
      Transform _localToDiagramTransform = Extensions.localToDiagramTransform(this);
      Scale _scale = new Scale(scale, scale, scale);
      final Transform t = TransformExtensions.operator_multiply(_localToDiagramTransform, _scale);
      SnapshotParameters _snapshotParameters = new SnapshotParameters();
      final Procedure1<SnapshotParameters> _function = new Procedure1<SnapshotParameters>() {
          public void apply(final SnapshotParameters it) {
            it.setTransform(t);
          }
        };
      SnapshotParameters _doubleArrow = ObjectExtensions.<SnapshotParameters>operator_doubleArrow(_snapshotParameters, _function);
      this.snapshot(_doubleArrow, image);
      CharSequence _svgImage = exporter.toSvgImage(this, image);
      _xblockexpression = (_svgImage);
    }
    return _xblockexpression;
  }
  
  private ReadOnlyObjectWrapper<Media> mediaProperty = new ReadOnlyObjectWrapper<Media>(this, "media");
  
  public Media getMedia() {
    return this.mediaProperty.get();
    
  }
  
  public ReadOnlyObjectProperty<Media> mediaProperty() {
    return this.mediaProperty.getReadOnlyProperty();
    
  }
}
