package de.fxdiagram.examples.lcars;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import com.mongodb.DBObject;
import de.fxdiagram.annotations.logging.Logging;
import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.extensions.DoubleExpressionExtensions;
import de.fxdiagram.core.extensions.DurationExtensions;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.core.model.ToString;
import de.fxdiagram.core.services.ImageCache;
import de.fxdiagram.examples.lcars.LcarsEntryDescriptor;
import de.fxdiagram.examples.lcars.LcarsExtensions;
import de.fxdiagram.examples.lcars.LcarsField;
import de.fxdiagram.examples.lcars.NameShortener;
import de.fxdiagram.lib.nodes.RectangleBorderPane;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.logging.Logger;
import javafx.animation.Timeline;
import javafx.animation.Transition;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.eclipse.xtext.xbase.lib.CollectionExtensions;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Pair;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@Logging
@ModelNode
@SuppressWarnings("all")
public class LcarsNode extends XNode {
  private final static Map<String, List<String>> PAGE_STRUCTURE = Collections.<String, List<String>>unmodifiableMap(CollectionLiterals.<String, List<String>>newHashMap(Pair.<String, List<String>>of("person", Collections.<String>unmodifiableList(CollectionLiterals.<String>newArrayList("gender", "species", "born", "status", "died", "marital_status"))), Pair.<String, List<String>>of("profession", Collections.<String>unmodifiableList(CollectionLiterals.<String>newArrayList("occupation", "affiliation", "rank", "serial_number"))), Pair.<String, List<String>>of("family", Collections.<String>unmodifiableList(CollectionLiterals.<String>newArrayList("spouses", "children", "mother", "father", "siblings", "other_relatives")))));
  
  private final static List<String> pageOrder = Collections.<String>unmodifiableList(CollectionLiterals.<String>newArrayList("person", "profession", "family", "other"));
  
  @Extension
  private NameShortener _nameShortener = new NameShortener();
  
  private Map<String, List<LcarsField>> pages;
  
  private List<String> imageUrls = CollectionLiterals.<String>newArrayList();
  
  private String currentImageUrl;
  
  private VBox vbox;
  
  private HBox infoBox;
  
  private Text nameField;
  
  private Pane infoTextBox;
  
  private ImageView imageView;
  
  private ChangeListener<Bounds> nameShortener;
  
  public LcarsNode(final LcarsEntryDescriptor descriptor) {
    super(descriptor);
  }
  
  @Override
  public LcarsEntryDescriptor getDomainObjectDescriptor() {
    DomainObjectDescriptor _domainObjectDescriptor = super.getDomainObjectDescriptor();
    return ((LcarsEntryDescriptor) _domainObjectDescriptor);
  }
  
  @Override
  protected Node createNode() {
    RectangleBorderPane _xblockexpression = null;
    {
      Object _get = this.getData().get("images");
      final Function1<DBObject, String> _function = (DBObject it) -> {
        return it.get("url").toString();
      };
      this.imageUrls = ListExtensions.<DBObject, String>map(((List<DBObject>) _get), _function);
      VBox _vBox = new VBox();
      this.vbox = _vBox;
      RectangleBorderPane _rectangleBorderPane = new RectangleBorderPane();
      final Procedure1<RectangleBorderPane> _function_1 = (RectangleBorderPane it) -> {
        it.setBackgroundRadius(0);
        it.setBorderRadius(0);
        it.setBackgroundPaint(Color.BLACK);
        it.setBorderPaint(Color.BLACK);
        ObservableList<Node> _children = it.getChildren();
        final Procedure1<VBox> _function_2 = (VBox it_1) -> {
          it_1.setSpacing(2);
          it_1.setFillWidth(true);
          ObservableList<Node> _children_1 = it_1.getChildren();
          HBox _hBox = new HBox();
          final Procedure1<HBox> _function_3 = (HBox it_2) -> {
            VBox.setVgrow(it_2, Priority.ALWAYS);
            ObservableList<Node> _children_2 = it_2.getChildren();
            RectangleBorderPane _createBox = this.createBox(LcarsExtensions.DARKBLUE);
            final Procedure1<RectangleBorderPane> _function_4 = (RectangleBorderPane it_3) -> {
              it_3.setAlignment(Pos.TOP_LEFT);
              HBox.setHgrow(it_3, Priority.ALWAYS);
            };
            RectangleBorderPane _doubleArrow = ObjectExtensions.<RectangleBorderPane>operator_doubleArrow(_createBox, _function_4);
            _children_2.add(_doubleArrow);
            Text _text = new Text();
            this.nameField = _text;
            ObservableList<Node> _children_3 = it_2.getChildren();
            final Procedure1<Text> _function_5 = (Text it_3) -> {
              it_3.setText(this.getName());
              it_3.setFill(LcarsExtensions.FLESH);
              it_3.setFont(LcarsExtensions.lcarsFont(28));
              Insets _insets = new Insets(0, 0, 0, 5);
              HBox.setMargin(it_3, _insets);
            };
            Text _doubleArrow_1 = ObjectExtensions.<Text>operator_doubleArrow(this.nameField, _function_5);
            _children_3.add(_doubleArrow_1);
          };
          HBox _doubleArrow = ObjectExtensions.<HBox>operator_doubleArrow(_hBox, _function_3);
          _children_1.add(_doubleArrow);
          ObservableList<Node> _children_2 = it_1.getChildren();
          RectangleBorderPane _createBox = this.createBox(LcarsExtensions.VIOLET);
          final Procedure1<RectangleBorderPane> _function_4 = (RectangleBorderPane it_2) -> {
            VBox.setVgrow(it_2, Priority.ALWAYS);
          };
          RectangleBorderPane _doubleArrow_1 = ObjectExtensions.<RectangleBorderPane>operator_doubleArrow(_createBox, _function_4);
          _children_2.add(_doubleArrow_1);
          Rectangle _rectangle = new Rectangle();
          final Procedure1<Rectangle> _function_5 = (Rectangle it_2) -> {
            it_2.setX(0);
            it_2.setY(0);
            it_2.setArcHeight(20);
            it_2.setArcWidth(20);
            DoubleProperty _widthProperty = it_2.widthProperty();
            ReadOnlyDoubleProperty _widthProperty_1 = this.vbox.widthProperty();
            DoubleBinding _plus = DoubleExpressionExtensions.operator_plus(_widthProperty_1, 20);
            _widthProperty.bind(_plus);
            it_2.heightProperty().bind(this.vbox.heightProperty());
          };
          Rectangle _doubleArrow_2 = ObjectExtensions.<Rectangle>operator_doubleArrow(_rectangle, _function_5);
          it_1.setClip(_doubleArrow_2);
          Insets _insets = new Insets(5, 5, 5, 5);
          StackPane.setMargin(it_1, _insets);
        };
        VBox _doubleArrow = ObjectExtensions.<VBox>operator_doubleArrow(this.vbox, _function_2);
        _children.add(_doubleArrow);
        ObservableList<Node> _children_1 = it.getChildren();
        RectangleBorderPane _rectangleBorderPane_1 = new RectangleBorderPane();
        final Procedure1<RectangleBorderPane> _function_3 = (RectangleBorderPane it_1) -> {
          it_1.setBackgroundPaint(Color.BLACK);
          it_1.setBackgroundRadius(8);
          it_1.setBorderPaint(Color.BLACK);
          it_1.setBorderRadius(8);
          Insets _insets = new Insets(35, (-3), 10, 25);
          StackPane.setMargin(it_1, _insets);
          HBox _hBox = new HBox();
          this.infoBox = _hBox;
          ObservableList<Node> _children_2 = it_1.getChildren();
          final Procedure1<HBox> _function_4 = (HBox it_2) -> {
            it_2.setSpacing(5);
            ObservableList<Node> _children_3 = it_2.getChildren();
            VBox _vBox_1 = new VBox();
            final Procedure1<VBox> _function_5 = (VBox it_3) -> {
            };
            VBox _doubleArrow_1 = ObjectExtensions.<VBox>operator_doubleArrow(_vBox_1, _function_5);
            Pane _infoTextBox = (this.infoTextBox = _doubleArrow_1);
            _children_3.add(_infoTextBox);
            ObservableList<Node> _children_4 = it_2.getChildren();
            ImageView _imageView = new ImageView();
            final Procedure1<ImageView> _function_6 = (ImageView it_3) -> {
              DoubleProperty _fitWidthProperty = it_3.fitWidthProperty();
              DoubleProperty _widthProperty = this.widthProperty();
              DoubleBinding _minus = DoubleExpressionExtensions.operator_minus(_widthProperty, 20);
              _fitWidthProperty.bind(_minus);
              DoubleProperty _fitHeightProperty = it_3.fitHeightProperty();
              DoubleProperty _heightProperty = this.heightProperty();
              DoubleBinding _minus_1 = DoubleExpressionExtensions.operator_minus(_heightProperty, 50);
              _fitHeightProperty.bind(_minus_1);
              it_3.setPreserveRatio(true);
            };
            ImageView _doubleArrow_2 = ObjectExtensions.<ImageView>operator_doubleArrow(_imageView, _function_6);
            ImageView _imageView_1 = (this.imageView = _doubleArrow_2);
            _children_4.add(_imageView_1);
            this.showImage(IterableExtensions.<String>last(this.imageUrls));
            Insets _insets_1 = new Insets(5, 6, 5, 5);
            StackPane.setMargin(it_2, _insets_1);
          };
          HBox _doubleArrow_1 = ObjectExtensions.<HBox>operator_doubleArrow(this.infoBox, _function_4);
          _children_2.add(_doubleArrow_1);
          Rectangle _rectangle = new Rectangle();
          final Procedure1<Rectangle> _function_5 = (Rectangle it_2) -> {
            DoubleProperty _widthProperty = it_2.widthProperty();
            ReadOnlyDoubleProperty _widthProperty_1 = this.vbox.widthProperty();
            DoubleBinding _minus = DoubleExpressionExtensions.operator_minus(_widthProperty_1, 15);
            _widthProperty.bind(_minus);
            it_2.heightProperty().bind(this.vbox.heightProperty());
          };
          Rectangle _doubleArrow_2 = ObjectExtensions.<Rectangle>operator_doubleArrow(_rectangle, _function_5);
          it_1.setClip(_doubleArrow_2);
        };
        RectangleBorderPane _doubleArrow_1 = ObjectExtensions.<RectangleBorderPane>operator_doubleArrow(_rectangleBorderPane_1, _function_3);
        _children_1.add(_doubleArrow_1);
      };
      final RectangleBorderPane node = ObjectExtensions.<RectangleBorderPane>operator_doubleArrow(_rectangleBorderPane, _function_1);
      final ChangeListener<Bounds> _function_2 = (ObservableValue<? extends Bounds> property, Bounds oldValue, Bounds newValue) -> {
        while ((this.nameField.getBoundsInLocal().getWidth() > newValue.getWidth())) {
          this.nameField.setText(this._nameShortener.shortenName(this.nameField.getText()));
        }
      };
      this.nameShortener = _function_2;
      this.infoBox.boundsInLocalProperty().addListener(this.nameShortener);
      _xblockexpression = node;
    }
    return _xblockexpression;
  }
  
  protected RectangleBorderPane createBox(final Color color) {
    RectangleBorderPane _rectangleBorderPane = new RectangleBorderPane();
    final Procedure1<RectangleBorderPane> _function = (RectangleBorderPane it) -> {
      it.setBorderPaint(color);
      it.setBackgroundPaint(color);
      it.setBorderRadius(0);
      it.setBackgroundRadius(0);
      it.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
      it.setAlignment(Pos.CENTER_LEFT);
    };
    return ObjectExtensions.<RectangleBorderPane>operator_doubleArrow(_rectangleBorderPane, _function);
  }
  
  protected Text createButtonText(final String buttonText, final VPos alignment) {
    Text _text = new Text();
    final Procedure1<Text> _function = (Text it) -> {
      it.setFont(LcarsExtensions.lcarsFont(4));
      it.setText(buttonText);
      it.setFill(Color.BLACK);
      it.setTextOrigin(VPos.TOP);
      if (alignment != null) {
        switch (alignment) {
          case TOP:
            Insets _insets = new Insets(0, 0, 5, 3);
            StackPane.setMargin(it, _insets);
            break;
          case BOTTOM:
            Insets _insets_1 = new Insets(5, 0, 0, 3);
            StackPane.setMargin(it, _insets_1);
            break;
          default:
            Insets _insets_2 = new Insets(1, 0, 1, 3);
            StackPane.setMargin(it, _insets_2);
            break;
        }
      } else {
        Insets _insets_2 = new Insets(1, 0, 1, 3);
        StackPane.setMargin(it, _insets_2);
      }
    };
    return ObjectExtensions.<Text>operator_doubleArrow(_text, _function);
  }
  
  protected LinkedHashMap<String, List<LcarsField>> createPages(final DBObject data) {
    LinkedHashMap<String, List<LcarsField>> _xblockexpression = null;
    {
      final LinkedHashMap<String, List<LcarsField>> pages = CollectionLiterals.<String, List<LcarsField>>newLinkedHashMap();
      final HashSet<String> handledKeys = CollectionLiterals.<String>newHashSet();
      CollectionExtensions.<String>addAll(handledKeys, "name", "_id", "images");
      Set<String> _keySet = LcarsNode.PAGE_STRUCTURE.keySet();
      for (final String pageTitle : _keySet) {
        {
          final ArrayList<LcarsField> page = this.createPageText(data, LcarsNode.PAGE_STRUCTURE.get(pageTitle));
          boolean _isEmpty = page.isEmpty();
          boolean _not = (!_isEmpty);
          if (_not) {
            pages.put(pageTitle, page);
          }
          handledKeys.addAll(LcarsNode.PAGE_STRUCTURE.get(pageTitle));
        }
      }
      final Function1<String, Boolean> _function = (String it) -> {
        boolean _contains = handledKeys.contains(it);
        return Boolean.valueOf((!_contains));
      };
      final Iterable<String> otherKeys = IterableExtensions.<String>filter(data.keySet(), _function);
      boolean _isEmpty = IterableExtensions.isEmpty(otherKeys);
      boolean _not = (!_isEmpty);
      if (_not) {
        pages.put("other", this.createPageText(data, otherKeys));
      }
      _xblockexpression = pages;
    }
    return _xblockexpression;
  }
  
  protected ArrayList<LcarsField> createPageText(final DBObject data, final Iterable<String> keys) {
    ArrayList<LcarsField> _xblockexpression = null;
    {
      final ArrayList<LcarsField> fields = CollectionLiterals.<LcarsField>newArrayList();
      for (final String key : keys) {
        boolean _containsField = data.containsField(key);
        if (_containsField) {
          String _string = data.get(key).toString();
          LcarsField _lcarsField = new LcarsField(this, key, _string);
          fields.add(_lcarsField);
        }
      }
      _xblockexpression = fields;
    }
    return _xblockexpression;
  }
  
  public void showImage(final String imageUrl) {
    final ImageCache imageCache = ImageCache.get();
    final Function0<byte[]> _function = () -> {
      Object _get = this.getData().get("images");
      final Function1<DBObject, Boolean> _function_1 = (DBObject it) -> {
        String _string = it.get("url").toString();
        return Boolean.valueOf(Objects.equal(_string, imageUrl));
      };
      Object _get_1 = IterableExtensions.<DBObject>head(IterableExtensions.<DBObject>filter(((List<DBObject>) _get), _function_1)).get("data");
      return ((byte[]) _get_1);
    };
    final Image image = imageCache.getImage(imageUrl, _function);
    if ((image != null)) {
      this.currentImageUrl = imageUrl;
      final Procedure1<ImageView> _function_1 = (ImageView it) -> {
        it.setImage(image);
        double _width = image.getWidth();
        double _height = image.getHeight();
        final double ratio = (_width / _height);
        double _imageRatio = this.getImageRatio();
        boolean _lessThan = (ratio < _imageRatio);
        if (_lessThan) {
          double _width_1 = image.getWidth();
          double _imageRatio_1 = this.getImageRatio();
          final double newHeight = (_width_1 / _imageRatio_1);
          double _height_1 = image.getHeight();
          double _minus = (_height_1 - newHeight);
          double _multiply = (0.5 * _minus);
          double _width_2 = image.getWidth();
          Rectangle2D _rectangle2D = new Rectangle2D(
            0, _multiply, _width_2, newHeight);
          it.setViewport(_rectangle2D);
        } else {
          double _imageRatio_2 = this.getImageRatio();
          double _height_2 = image.getHeight();
          final double newWidth = (_imageRatio_2 * _height_2);
          double _width_3 = image.getWidth();
          double _minus_1 = (_width_3 - newWidth);
          double _multiply_1 = (0.5 * _minus_1);
          double _height_3 = image.getHeight();
          Rectangle2D _rectangle2D_1 = new Rectangle2D(_multiply_1, 
            0, newWidth, _height_3);
          it.setViewport(_rectangle2D_1);
        }
      };
      ObjectExtensions.<ImageView>operator_doubleArrow(
        this.imageView, _function_1);
    }
  }
  
  public void showPage(final String page) {
    final List<LcarsField> fields = this.pages.get(page);
    this.infoTextBox.getChildren().clear();
    ObservableList<Node> _children = this.infoTextBox.getChildren();
    Iterables.<Node>addAll(_children, fields);
    final Timeline timeline = new Timeline();
    final Consumer<LcarsField> _function = (LcarsField it) -> {
      it.addAnimation(timeline);
    };
    fields.forEach(_function);
    timeline.play();
  }
  
  @Override
  public void doActivate() {
    super.doActivate();
    this.infoBox.boundsInLocalProperty().removeListener(this.nameShortener);
    this.nameField.setText(this.getName());
    this.pages = this.createPages(this.getData());
    final Procedure1<VBox> _function = (VBox it) -> {
      final Node lastStripe = IterableExtensions.<Node>last(it.getChildren());
      ObservableList<Node> _children = it.getChildren();
      _children.remove(lastStripe);
      final Function1<String, Boolean> _function_1 = (String it_1) -> {
        return Boolean.valueOf(this.pages.containsKey(it_1));
      };
      Iterable<String> _filter = IterableExtensions.<String>filter(LcarsNode.pageOrder, _function_1);
      for (final String pageTitle : _filter) {
        {
          final RectangleBorderPane box = this.createBox(LcarsExtensions.FLESH);
          ObservableList<Node> _children_1 = it.getChildren();
          final Procedure1<RectangleBorderPane> _function_2 = (RectangleBorderPane it_1) -> {
            VBox.setVgrow(it_1, Priority.SOMETIMES);
            ObservableList<Node> _children_2 = it_1.getChildren();
            VPos _switchResult = null;
            int _indexOf = LcarsNode.pageOrder.indexOf(pageTitle);
            boolean _matched = false;
            if (Objects.equal(_indexOf, 0)) {
              _matched=true;
              _switchResult = VPos.BOTTOM;
            }
            if (!_matched) {
              int _size = this.pages.keySet().size();
              int _minus = (_size - 1);
              if (Objects.equal(_indexOf, _minus)) {
                _matched=true;
                _switchResult = VPos.TOP;
              }
            }
            if (!_matched) {
              _switchResult = VPos.CENTER;
            }
            Text _createButtonText = this.createButtonText(pageTitle, _switchResult);
            _children_2.add(_createButtonText);
            final EventHandler<MouseEvent> _function_3 = (MouseEvent it_2) -> {
              this.showPage(pageTitle);
              this.invertColors(box);
            };
            it_1.setOnMousePressed(_function_3);
            final EventHandler<MouseEvent> _function_4 = (MouseEvent it_2) -> {
              this.invertColors(box);
            };
            it_1.setOnMouseReleased(_function_4);
          };
          RectangleBorderPane _doubleArrow = ObjectExtensions.<RectangleBorderPane>operator_doubleArrow(box, _function_2);
          _children_1.add(_doubleArrow);
        }
      }
      int _size = this.imageUrls.size();
      boolean _greaterThan = (_size > 1);
      if (_greaterThan) {
        final RectangleBorderPane previous_box = this.createBox(LcarsExtensions.RED);
        ObservableList<Node> _children_1 = it.getChildren();
        final Procedure1<RectangleBorderPane> _function_2 = (RectangleBorderPane it_1) -> {
          VBox.setVgrow(it_1, Priority.SOMETIMES);
          ObservableList<Node> _children_2 = it_1.getChildren();
          Text _createButtonText = this.createButtonText("previous pic", VPos.BOTTOM);
          _children_2.add(_createButtonText);
          final EventHandler<MouseEvent> _function_3 = (MouseEvent it_2) -> {
            int _indexOf = this.imageUrls.indexOf(this.currentImageUrl);
            int _plus = (_indexOf + 1);
            int _size_1 = this.imageUrls.size();
            int _modulo = (_plus % _size_1);
            this.showImage(this.imageUrls.get(_modulo));
            this.invertColors(previous_box);
          };
          it_1.setOnMousePressed(_function_3);
          final EventHandler<MouseEvent> _function_4 = (MouseEvent it_2) -> {
            this.invertColors(previous_box);
          };
          it_1.setOnMouseReleased(_function_4);
        };
        RectangleBorderPane _doubleArrow = ObjectExtensions.<RectangleBorderPane>operator_doubleArrow(previous_box, _function_2);
        _children_1.add(_doubleArrow);
        final RectangleBorderPane next_box = this.createBox(LcarsExtensions.RED);
        ObservableList<Node> _children_2 = it.getChildren();
        final Procedure1<RectangleBorderPane> _function_3 = (RectangleBorderPane it_1) -> {
          VBox.setVgrow(it_1, Priority.SOMETIMES);
          ObservableList<Node> _children_3 = it_1.getChildren();
          Text _createButtonText = this.createButtonText("next pic", VPos.TOP);
          _children_3.add(_createButtonText);
          final EventHandler<MouseEvent> _function_4 = (MouseEvent it_2) -> {
            int _indexOf = this.imageUrls.indexOf(this.currentImageUrl);
            int _size_1 = this.imageUrls.size();
            int _plus = (_indexOf + _size_1);
            int _minus = (_plus - 1);
            int _size_2 = this.imageUrls.size();
            int _modulo = (_minus % _size_2);
            this.showImage(this.imageUrls.get(_modulo));
            this.invertColors(next_box);
          };
          it_1.setOnMousePressed(_function_4);
          final EventHandler<MouseEvent> _function_5 = (MouseEvent it_2) -> {
            this.invertColors(next_box);
          };
          it_1.setOnMouseReleased(_function_5);
        };
        RectangleBorderPane _doubleArrow_1 = ObjectExtensions.<RectangleBorderPane>operator_doubleArrow(next_box, _function_3);
        _children_2.add(_doubleArrow_1);
      } else {
        ObservableList<Node> _children_3 = it.getChildren();
        RectangleBorderPane _createBox = this.createBox(LcarsExtensions.RED);
        final Procedure1<RectangleBorderPane> _function_4 = (RectangleBorderPane it_1) -> {
          VBox.setVgrow(it_1, Priority.ALWAYS);
        };
        RectangleBorderPane _doubleArrow_2 = ObjectExtensions.<RectangleBorderPane>operator_doubleArrow(_createBox, _function_4);
        _children_3.add(_doubleArrow_2);
      }
      ObservableList<Node> _children_4 = it.getChildren();
      _children_4.add(lastStripe);
    };
    ObjectExtensions.<VBox>operator_doubleArrow(
      this.vbox, _function);
    this.inflateInfoTextBox();
  }
  
  public Transition inflateInfoTextBox() {
    abstract class __LcarsNode_1 extends Transition {
      public abstract void setDuration(final Duration duration);
    }
    
    __LcarsNode_1 _xblockexpression = null;
    {
      final Rectangle spacer = new Rectangle(0, 0, 1, 1);
      ObservableList<Node> _children = this.infoTextBox.getChildren();
      _children.add(spacer);
      __LcarsNode_1 ___LcarsNode_1 = new __LcarsNode_1() {
        public void setDuration(final Duration duration) {
          this.setCycleDuration(duration);
        }
        
        @Override
        protected void interpolate(final double alpha) {
          spacer.setWidth((150 * alpha));
        }
      };
      final Procedure1<__LcarsNode_1> _function = (__LcarsNode_1 it) -> {
        it.setDuration(DurationExtensions.millis(500));
        final EventHandler<ActionEvent> _function_1 = (ActionEvent it_1) -> {
          ObservableList<Node> _children_1 = this.infoTextBox.getChildren();
          _children_1.remove(spacer);
          this.showPage(this.pages.keySet().iterator().next());
          this.infoTextBox.boundsInLocalProperty().addListener(this.nameShortener);
        };
        it.setOnFinished(_function_1);
        it.play();
      };
      _xblockexpression = ObjectExtensions.<__LcarsNode_1>operator_doubleArrow(___LcarsNode_1, _function);
    }
    return _xblockexpression;
  }
  
  protected void invertColors(final RectangleBorderPane box) {
    Node _head = IterableExtensions.<Node>head(box.getChildren());
    final Text text = ((Text) _head);
    final Paint textColor = text.getFill();
    text.setFill(box.getBackgroundPaint());
    box.setBackgroundPaint(textColor);
    box.setBorderPaint(textColor);
  }
  
  public DBObject getData() {
    LcarsEntryDescriptor _domainObjectDescriptor = this.getDomainObjectDescriptor();
    return ((LcarsEntryDescriptor) _domainObjectDescriptor).getDomainObject();
  }
  
  @Override
  public void selectionFeedback(final boolean isSelected) {
    ObservableList<XConnection> _outgoingConnections = this.getOutgoingConnections();
    ObservableList<XConnection> _incomingConnections = this.getIncomingConnections();
    final Consumer<XConnection> _function = (XConnection it) -> {
      it.toFront();
    };
    Iterables.<XConnection>concat(_outgoingConnections, _incomingConnections).forEach(_function);
  }
  
  private static Logger LOG = Logger.getLogger("de.fxdiagram.examples.lcars.LcarsNode");
    ;
  
  /**
   * Automatically generated by @ModelNode. Needed for deserialization.
   */
  public LcarsNode() {
  }
  
  public void populate(final ModelElementImpl modelElement) {
    super.populate(modelElement);
  }
  
  public void postLoad() {
    
  }
  
  public String toString() {
    return ToString.toString(this);
  }
  
  private SimpleDoubleProperty imageRatioProperty = new SimpleDoubleProperty(this, "imageRatio",_initImageRatio());
  
  private static final double _initImageRatio() {
    return 0.7;
  }
  
  public double getImageRatio() {
    return this.imageRatioProperty.get();
  }
  
  public void setImageRatio(final double imageRatio) {
    this.imageRatioProperty.set(imageRatio);
  }
  
  public DoubleProperty imageRatioProperty() {
    return this.imageRatioProperty;
  }
}
