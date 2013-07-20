package de.fxdiagram.examples.lcars;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mongodb.DBObject;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.binding.DoubleExpressionExtensions;
import de.fxdiagram.examples.lcars.LcarsAccess;
import de.fxdiagram.examples.lcars.LcarsExtensions;
import de.fxdiagram.examples.lcars.LcarsField;
import de.fxdiagram.lib.nodes.RectangleBorderPane;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javafx.animation.Timeline;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
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
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.eclipse.xtext.xbase.lib.CollectionExtensions;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class LcarsNode extends XNode {
  private final static Map<String,List<String>> PAGE_STRUCTURE = new Function0<Map<String,List<String>>>() {
    public Map<String,List<String>> apply() {
      Map<String,List<String>> _xsetliteral = null;
      Map<String,List<String>> _tempMap = Maps.<String, List<String>>newHashMap();
      _tempMap.put("person", Collections.<String>unmodifiableList(Lists.<String>newArrayList("gender", "species", "born", "status", "died", "marital_status")));
      _tempMap.put("profession", Collections.<String>unmodifiableList(Lists.<String>newArrayList("occupation", "affiliation", "rank", "serial_number")));
      _tempMap.put("family", Collections.<String>unmodifiableList(Lists.<String>newArrayList("spouses", "children", "mother", "father", "siblings", "other_relatives")));
      _xsetliteral = Collections.<String, List<String>>unmodifiableMap(_tempMap);
      return _xsetliteral;
    }
  }.apply();
  
  private final static List<String> pageOrder = Collections.<String>unmodifiableList(Lists.<String>newArrayList("person", "profession", "family", "other"));
  
  private DBObject data;
  
  private String name;
  
  private String dbId;
  
  private Map<String,List<LcarsField>> pages;
  
  private List<String> imageUrls = new Function0<List<String>>() {
    public List<String> apply() {
      ArrayList<String> _newArrayList = CollectionLiterals.<String>newArrayList();
      return _newArrayList;
    }
  }.apply();
  
  private String currentImageUrl;
  
  private VBox vbox;
  
  private Pane infoTextBox;
  
  private ImageView imageView;
  
  public LcarsNode(final DBObject data) {
    this.data = data;
    Object _get = data.get("name");
    String _string = _get.toString();
    this.name = _string;
    Object _get_1 = data.get("_id");
    String _string_1 = _get_1.toString();
    this.dbId = _string_1;
    Object _get_2 = data.get("images");
    final Function1<DBObject,String> _function = new Function1<DBObject,String>() {
        public String apply(final DBObject it) {
          Object _get = it.get("url");
          String _string = _get.toString();
          return _string;
        }
      };
    List<String> _map = ListExtensions.<DBObject, String>map(((List<DBObject>) _get_2), _function);
    this.imageUrls = _map;
    VBox _vBox = new VBox();
    this.vbox = _vBox;
    RectangleBorderPane _rectangleBorderPane = new RectangleBorderPane();
    final Procedure1<RectangleBorderPane> _function_1 = new Procedure1<RectangleBorderPane>() {
        public void apply(final RectangleBorderPane it) {
          it.setBackgroundRadius(0);
          it.setBorderRadius(0);
          it.setBackgroundPaint(Color.BLACK);
          it.setBorderPaint(Color.BLACK);
          ObservableList<Node> _children = it.getChildren();
          final Procedure1<VBox> _function = new Procedure1<VBox>() {
              public void apply(final VBox it) {
                it.setSpacing(2);
                it.setFillWidth(true);
                ObservableList<Node> _children = it.getChildren();
                RectangleBorderPane _createBox = LcarsNode.this.createBox(LcarsExtensions.DARKBLUE);
                final Procedure1<RectangleBorderPane> _function = new Procedure1<RectangleBorderPane>() {
                    public void apply(final RectangleBorderPane it) {
                      VBox.setVgrow(it, Priority.ALWAYS);
                      it.setAlignment(Pos.TOP_RIGHT);
                      ObservableList<Node> _children = it.getChildren();
                      Text _text = new Text();
                      final Procedure1<Text> _function = new Procedure1<Text>() {
                          public void apply(final Text it) {
                            it.setText(LcarsNode.this.name);
                            it.setFill(LcarsExtensions.FLESH);
                            Font _lcarsFont = LcarsExtensions.lcarsFont(28);
                            it.setFont(_lcarsFont);
                            Insets _insets = new Insets(0, 5, 0, 0);
                            StackPane.setMargin(it, _insets);
                          }
                        };
                      Text _doubleArrow = ObjectExtensions.<Text>operator_doubleArrow(_text, _function);
                      _children.add(_doubleArrow);
                    }
                  };
                RectangleBorderPane _doubleArrow = ObjectExtensions.<RectangleBorderPane>operator_doubleArrow(_createBox, _function);
                _children.add(_doubleArrow);
                ObservableList<Node> _children_1 = it.getChildren();
                RectangleBorderPane _createBox_1 = LcarsNode.this.createBox(LcarsExtensions.VIOLET);
                final Procedure1<RectangleBorderPane> _function_1 = new Procedure1<RectangleBorderPane>() {
                    public void apply(final RectangleBorderPane it) {
                      VBox.setVgrow(it, Priority.ALWAYS);
                    }
                  };
                RectangleBorderPane _doubleArrow_1 = ObjectExtensions.<RectangleBorderPane>operator_doubleArrow(_createBox_1, _function_1);
                _children_1.add(_doubleArrow_1);
                Rectangle _rectangle = new Rectangle();
                final Procedure1<Rectangle> _function_2 = new Procedure1<Rectangle>() {
                    public void apply(final Rectangle it) {
                      it.setX(0);
                      it.setY(0);
                      it.setArcHeight(20);
                      it.setArcWidth(20);
                      DoubleProperty _widthProperty = it.widthProperty();
                      ReadOnlyDoubleProperty _widthProperty_1 = LcarsNode.this.vbox.widthProperty();
                      DoubleBinding _plus = DoubleExpressionExtensions.operator_plus(_widthProperty_1, 20);
                      _widthProperty.bind(_plus);
                      DoubleProperty _heightProperty = it.heightProperty();
                      ReadOnlyDoubleProperty _heightProperty_1 = LcarsNode.this.vbox.heightProperty();
                      _heightProperty.bind(_heightProperty_1);
                    }
                  };
                Rectangle _doubleArrow_2 = ObjectExtensions.<Rectangle>operator_doubleArrow(_rectangle, _function_2);
                it.setClip(_doubleArrow_2);
                Insets _insets = new Insets(5, 5, 5, 5);
                StackPane.setMargin(it, _insets);
              }
            };
          VBox _doubleArrow = ObjectExtensions.<VBox>operator_doubleArrow(LcarsNode.this.vbox, _function);
          _children.add(_doubleArrow);
          ObservableList<Node> _children_1 = it.getChildren();
          RectangleBorderPane _rectangleBorderPane = new RectangleBorderPane();
          final Procedure1<RectangleBorderPane> _function_1 = new Procedure1<RectangleBorderPane>() {
              public void apply(final RectangleBorderPane it) {
                it.setBackgroundPaint(Color.BLACK);
                it.setBackgroundRadius(8);
                it.setBorderPaint(Color.BLACK);
                it.setBorderRadius(8);
                int _minus = (-3);
                Insets _insets = new Insets(35, _minus, 10, 25);
                StackPane.setMargin(it, _insets);
                ObservableList<Node> _children = it.getChildren();
                HBox _hBox = new HBox();
                final Procedure1<HBox> _function = new Procedure1<HBox>() {
                    public void apply(final HBox it) {
                      it.setSpacing(5);
                      ObservableList<Node> _children = it.getChildren();
                      VBox _vBox = new VBox();
                      final Procedure1<VBox> _function = new Procedure1<VBox>() {
                          public void apply(final VBox it) {
                          }
                        };
                      VBox _doubleArrow = ObjectExtensions.<VBox>operator_doubleArrow(_vBox, _function);
                      Pane _infoTextBox = LcarsNode.this.infoTextBox = _doubleArrow;
                      _children.add(_infoTextBox);
                      ObservableList<Node> _children_1 = it.getChildren();
                      ImageView _imageView = new ImageView();
                      final Procedure1<ImageView> _function_1 = new Procedure1<ImageView>() {
                          public void apply(final ImageView it) {
                            DoubleProperty _fitWidthProperty = it.fitWidthProperty();
                            DoubleProperty _widthProperty = LcarsNode.this.widthProperty();
                            DoubleBinding _minus = DoubleExpressionExtensions.operator_minus(_widthProperty, 20);
                            _fitWidthProperty.bind(_minus);
                            DoubleProperty _fitHeightProperty = it.fitHeightProperty();
                            DoubleProperty _heightProperty = LcarsNode.this.heightProperty();
                            DoubleBinding _minus_1 = DoubleExpressionExtensions.operator_minus(_heightProperty, 50);
                            _fitHeightProperty.bind(_minus_1);
                            it.setPreserveRatio(true);
                          }
                        };
                      ImageView _doubleArrow_1 = ObjectExtensions.<ImageView>operator_doubleArrow(_imageView, _function_1);
                      ImageView _imageView_1 = LcarsNode.this.imageView = _doubleArrow_1;
                      _children_1.add(_imageView_1);
                      String _last = IterableExtensions.<String>last(LcarsNode.this.imageUrls);
                      LcarsNode.this.showImage(_last);
                      Insets _insets = new Insets(5, 6, 5, 5);
                      StackPane.setMargin(it, _insets);
                    }
                  };
                HBox _doubleArrow = ObjectExtensions.<HBox>operator_doubleArrow(_hBox, _function);
                _children.add(_doubleArrow);
                Rectangle _rectangle = new Rectangle();
                final Procedure1<Rectangle> _function_1 = new Procedure1<Rectangle>() {
                    public void apply(final Rectangle it) {
                      DoubleProperty _widthProperty = it.widthProperty();
                      ReadOnlyDoubleProperty _widthProperty_1 = LcarsNode.this.vbox.widthProperty();
                      DoubleBinding _minus = DoubleExpressionExtensions.operator_minus(_widthProperty_1, 15);
                      _widthProperty.bind(_minus);
                      DoubleProperty _heightProperty = it.heightProperty();
                      ReadOnlyDoubleProperty _heightProperty_1 = LcarsNode.this.vbox.heightProperty();
                      _heightProperty.bind(_heightProperty_1);
                    }
                  };
                Rectangle _doubleArrow_1 = ObjectExtensions.<Rectangle>operator_doubleArrow(_rectangle, _function_1);
                it.setClip(_doubleArrow_1);
              }
            };
          RectangleBorderPane _doubleArrow_1 = ObjectExtensions.<RectangleBorderPane>operator_doubleArrow(_rectangleBorderPane, _function_1);
          _children_1.add(_doubleArrow_1);
        }
      };
    RectangleBorderPane _doubleArrow = ObjectExtensions.<RectangleBorderPane>operator_doubleArrow(_rectangleBorderPane, _function_1);
    this.setNode(_doubleArrow);
    this.setKey(this.name);
  }
  
  protected RectangleBorderPane createBox(final Color color) {
    RectangleBorderPane _rectangleBorderPane = new RectangleBorderPane();
    final Procedure1<RectangleBorderPane> _function = new Procedure1<RectangleBorderPane>() {
        public void apply(final RectangleBorderPane it) {
          it.setBorderPaint(color);
          it.setBackgroundPaint(color);
          it.setBorderRadius(0);
          it.setBackgroundRadius(0);
          it.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
          it.setAlignment(Pos.CENTER_LEFT);
        }
      };
    RectangleBorderPane _doubleArrow = ObjectExtensions.<RectangleBorderPane>operator_doubleArrow(_rectangleBorderPane, _function);
    return _doubleArrow;
  }
  
  protected Text createButtonText(final String buttonText, final VPos alignment) {
    Text _text = new Text();
    final Procedure1<Text> _function = new Procedure1<Text>() {
        public void apply(final Text it) {
          Font _lcarsFont = LcarsExtensions.lcarsFont(4);
          it.setFont(_lcarsFont);
          it.setText(buttonText);
          it.setFill(Color.BLACK);
          it.setTextOrigin(VPos.TOP);
          boolean _matched = false;
          if (!_matched) {
            if (Objects.equal(alignment,VPos.TOP)) {
              _matched=true;
              Insets _insets = new Insets(0, 0, 5, 3);
              StackPane.setMargin(it, _insets);
            }
          }
          if (!_matched) {
            if (Objects.equal(alignment,VPos.BOTTOM)) {
              _matched=true;
              Insets _insets_1 = new Insets(5, 0, 0, 3);
              StackPane.setMargin(it, _insets_1);
            }
          }
          if (!_matched) {
            Insets _insets_2 = new Insets(1, 0, 1, 3);
            StackPane.setMargin(it, _insets_2);
          }
        }
      };
    Text _doubleArrow = ObjectExtensions.<Text>operator_doubleArrow(_text, _function);
    return _doubleArrow;
  }
  
  protected LinkedHashMap<String,List<LcarsField>> createPages(final DBObject data) {
    LinkedHashMap<String,List<LcarsField>> _xblockexpression = null;
    {
      final LinkedHashMap<String,List<LcarsField>> pages = CollectionLiterals.<String, List<LcarsField>>newLinkedHashMap();
      final HashSet<String> handledKeys = CollectionLiterals.<String>newHashSet();
      CollectionExtensions.<String>addAll(handledKeys, "name", "_id", "images");
      Set<String> _keySet = LcarsNode.PAGE_STRUCTURE.keySet();
      for (final String pageTitle : _keySet) {
        {
          List<String> _get = LcarsNode.PAGE_STRUCTURE.get(pageTitle);
          final ArrayList<LcarsField> page = this.createPageText(data, _get);
          boolean _isEmpty = page.isEmpty();
          boolean _not = (!_isEmpty);
          if (_not) {
            pages.put(pageTitle, page);
          }
          List<String> _get_1 = LcarsNode.PAGE_STRUCTURE.get(pageTitle);
          handledKeys.addAll(_get_1);
        }
      }
      Set<String> _keySet_1 = data.keySet();
      final Function1<String,Boolean> _function = new Function1<String,Boolean>() {
          public Boolean apply(final String it) {
            boolean _contains = handledKeys.contains(it);
            boolean _not = (!_contains);
            return Boolean.valueOf(_not);
          }
        };
      final Iterable<String> otherKeys = IterableExtensions.<String>filter(_keySet_1, _function);
      boolean _isEmpty = IterableExtensions.isEmpty(otherKeys);
      boolean _not = (!_isEmpty);
      if (_not) {
        ArrayList<LcarsField> _createPageText = this.createPageText(data, otherKeys);
        pages.put("other", _createPageText);
      }
      _xblockexpression = (pages);
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
          Object _get = data.get(key);
          String _string = _get.toString();
          LcarsField _lcarsField = new LcarsField(this, key, _string);
          fields.add(_lcarsField);
        }
      }
      _xblockexpression = (fields);
    }
    return _xblockexpression;
  }
  
  public ImageView showImage(final String imageUrl) {
    ImageView _xblockexpression = null;
    {
      Object _get = this.data.get("images");
      final Function1<DBObject,Boolean> _function = new Function1<DBObject,Boolean>() {
          public Boolean apply(final DBObject it) {
            Object _get = it.get("url");
            String _string = _get.toString();
            boolean _equals = Objects.equal(_string, imageUrl);
            return Boolean.valueOf(_equals);
          }
        };
      Iterable<DBObject> _filter = IterableExtensions.<DBObject>filter(((List<DBObject>) _get), _function);
      DBObject _head = IterableExtensions.<DBObject>head(_filter);
      Object _get_1 = _head.get("data");
      final byte[] imageData = ((byte[]) _get_1);
      ImageView _xifexpression = null;
      boolean _notEquals = (!Objects.equal(imageData, null));
      if (_notEquals) {
        ImageView _xblockexpression_1 = null;
        {
          LcarsAccess _get_2 = LcarsAccess.get();
          final Image image = _get_2.getImage(imageUrl, imageData);
          this.currentImageUrl = imageUrl;
          final Procedure1<ImageView> _function_1 = new Procedure1<ImageView>() {
              public void apply(final ImageView it) {
                it.setImage(image);
                double _width = image.getWidth();
                double _height = image.getHeight();
                final double ratio = (_width / _height);
                double _imageRatio = LcarsNode.this.getImageRatio();
                boolean _lessThan = (ratio < _imageRatio);
                if (_lessThan) {
                  double _width_1 = image.getWidth();
                  double _imageRatio_1 = LcarsNode.this.getImageRatio();
                  final double newHeight = (_width_1 / _imageRatio_1);
                  double _height_1 = image.getHeight();
                  double _minus = (_height_1 - newHeight);
                  double _multiply = (0.5 * _minus);
                  double _width_2 = image.getWidth();
                  Rectangle2D _rectangle2D = new Rectangle2D(
                    0, _multiply, _width_2, newHeight);
                  it.setViewport(_rectangle2D);
                } else {
                  double _imageRatio_2 = LcarsNode.this.getImageRatio();
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
              }
            };
          ImageView _doubleArrow = ObjectExtensions.<ImageView>operator_doubleArrow(
            this.imageView, _function_1);
          _xblockexpression_1 = (_doubleArrow);
        }
        _xifexpression = _xblockexpression_1;
      }
      _xblockexpression = (_xifexpression);
    }
    return _xblockexpression;
  }
  
  public void showPage(final String page) {
    final List<LcarsField> fields = this.pages.get(page);
    ObservableList<Node> _children = this.infoTextBox.getChildren();
    _children.clear();
    ObservableList<Node> _children_1 = this.infoTextBox.getChildren();
    Iterables.<Node>addAll(_children_1, fields);
    Timeline _timeline = new Timeline();
    final Timeline timeline = _timeline;
    final Procedure1<LcarsField> _function = new Procedure1<LcarsField>() {
        public void apply(final LcarsField it) {
          it.addAnimation(timeline);
        }
      };
    IterableExtensions.<LcarsField>forEach(fields, _function);
    timeline.play();
  }
  
  public void activate() {
    super.activate();
    LinkedHashMap<String,List<LcarsField>> _createPages = this.createPages(this.data);
    this.pages = _createPages;
    final Procedure1<VBox> _function = new Procedure1<VBox>() {
        public void apply(final VBox it) {
          ObservableList<Node> _children = it.getChildren();
          final Node lastStripe = IterableExtensions.<Node>last(_children);
          ObservableList<Node> _children_1 = it.getChildren();
          _children_1.remove(lastStripe);
          final Function1<String,Boolean> _function = new Function1<String,Boolean>() {
              public Boolean apply(final String it) {
                boolean _containsKey = LcarsNode.this.pages.containsKey(it);
                return Boolean.valueOf(_containsKey);
              }
            };
          Iterable<String> _filter = IterableExtensions.<String>filter(LcarsNode.pageOrder, _function);
          for (final String pageTitle : _filter) {
            ObservableList<Node> _children_2 = it.getChildren();
            RectangleBorderPane _createBox = LcarsNode.this.createBox(LcarsExtensions.FLESH);
            final Procedure1<RectangleBorderPane> _function_1 = new Procedure1<RectangleBorderPane>() {
                public void apply(final RectangleBorderPane it) {
                  VBox.setVgrow(it, Priority.SOMETIMES);
                  ObservableList<Node> _children = it.getChildren();
                  VPos _switchResult = null;
                  int _indexOf = LcarsNode.pageOrder.indexOf(pageTitle);
                  final int _switchValue = _indexOf;
                  boolean _matched = false;
                  if (!_matched) {
                    if (Objects.equal(_switchValue,0)) {
                      _matched=true;
                      _switchResult = VPos.BOTTOM;
                    }
                  }
                  if (!_matched) {
                    Set<String> _keySet = LcarsNode.this.pages.keySet();
                    int _size = _keySet.size();
                    int _minus = (_size - 1);
                    if (Objects.equal(_switchValue,_minus)) {
                      _matched=true;
                      _switchResult = VPos.TOP;
                    }
                  }
                  if (!_matched) {
                    _switchResult = VPos.CENTER;
                  }
                  Text _createButtonText = LcarsNode.this.createButtonText(pageTitle, _switchResult);
                  _children.add(_createButtonText);
                  final EventHandler<MouseEvent> _function = new EventHandler<MouseEvent>() {
                      public void handle(final MouseEvent it) {
                        LcarsNode.this.showPage(pageTitle);
                      }
                    };
                  it.setOnMousePressed(_function);
                }
              };
            RectangleBorderPane _doubleArrow = ObjectExtensions.<RectangleBorderPane>operator_doubleArrow(_createBox, _function_1);
            _children_2.add(_doubleArrow);
          }
          int _size = LcarsNode.this.imageUrls.size();
          boolean _greaterThan = (_size > 1);
          if (_greaterThan) {
            ObservableList<Node> _children_3 = it.getChildren();
            RectangleBorderPane _createBox_1 = LcarsNode.this.createBox(LcarsExtensions.RED);
            final Procedure1<RectangleBorderPane> _function_2 = new Procedure1<RectangleBorderPane>() {
                public void apply(final RectangleBorderPane it) {
                  VBox.setVgrow(it, Priority.SOMETIMES);
                  ObservableList<Node> _children = it.getChildren();
                  Text _createButtonText = LcarsNode.this.createButtonText("previous pic", VPos.BOTTOM);
                  _children.add(_createButtonText);
                  final EventHandler<MouseEvent> _function = new EventHandler<MouseEvent>() {
                      public void handle(final MouseEvent it) {
                        int _indexOf = LcarsNode.this.imageUrls.indexOf(LcarsNode.this.currentImageUrl);
                        int _plus = (_indexOf + 1);
                        int _size = LcarsNode.this.imageUrls.size();
                        int _modulo = (_plus % _size);
                        String _get = LcarsNode.this.imageUrls.get(_modulo);
                        LcarsNode.this.showImage(_get);
                      }
                    };
                  it.setOnMousePressed(_function);
                }
              };
            RectangleBorderPane _doubleArrow_1 = ObjectExtensions.<RectangleBorderPane>operator_doubleArrow(_createBox_1, _function_2);
            _children_3.add(_doubleArrow_1);
            ObservableList<Node> _children_4 = it.getChildren();
            RectangleBorderPane _createBox_2 = LcarsNode.this.createBox(LcarsExtensions.RED);
            final Procedure1<RectangleBorderPane> _function_3 = new Procedure1<RectangleBorderPane>() {
                public void apply(final RectangleBorderPane it) {
                  VBox.setVgrow(it, Priority.SOMETIMES);
                  ObservableList<Node> _children = it.getChildren();
                  Text _createButtonText = LcarsNode.this.createButtonText("next pic", VPos.TOP);
                  _children.add(_createButtonText);
                  final EventHandler<MouseEvent> _function = new EventHandler<MouseEvent>() {
                      public void handle(final MouseEvent it) {
                        int _indexOf = LcarsNode.this.imageUrls.indexOf(LcarsNode.this.currentImageUrl);
                        int _size = LcarsNode.this.imageUrls.size();
                        int _plus = (_indexOf + _size);
                        int _minus = (_plus - 1);
                        int _size_1 = LcarsNode.this.imageUrls.size();
                        int _modulo = (_minus % _size_1);
                        String _get = LcarsNode.this.imageUrls.get(_modulo);
                        LcarsNode.this.showImage(_get);
                      }
                    };
                  it.setOnMousePressed(_function);
                }
              };
            RectangleBorderPane _doubleArrow_2 = ObjectExtensions.<RectangleBorderPane>operator_doubleArrow(_createBox_2, _function_3);
            _children_4.add(_doubleArrow_2);
          } else {
            ObservableList<Node> _children_5 = it.getChildren();
            RectangleBorderPane _createBox_3 = LcarsNode.this.createBox(LcarsExtensions.RED);
            final Procedure1<RectangleBorderPane> _function_4 = new Procedure1<RectangleBorderPane>() {
                public void apply(final RectangleBorderPane it) {
                  VBox.setVgrow(it, Priority.ALWAYS);
                }
              };
            RectangleBorderPane _doubleArrow_3 = ObjectExtensions.<RectangleBorderPane>operator_doubleArrow(_createBox_3, _function_4);
            _children_5.add(_doubleArrow_3);
          }
          ObservableList<Node> _children_6 = it.getChildren();
          _children_6.add(lastStripe);
        }
      };
    ObjectExtensions.<VBox>operator_doubleArrow(
      this.vbox, _function);
    Set<String> _keySet = this.pages.keySet();
    Iterator<String> _iterator = _keySet.iterator();
    String _next = _iterator.next();
    this.showPage(_next);
  }
  
  public String getDbId() {
    return this.dbId;
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
