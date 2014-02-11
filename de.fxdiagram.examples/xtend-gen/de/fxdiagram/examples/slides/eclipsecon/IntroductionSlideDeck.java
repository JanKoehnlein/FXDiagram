package de.fxdiagram.examples.slides.eclipsecon;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.model.DomainObjectHandle;
import de.fxdiagram.core.model.ModelElement;
import de.fxdiagram.core.services.ImageCache;
import de.fxdiagram.examples.slides.Animations;
import de.fxdiagram.examples.slides.ClickThroughSlide;
import de.fxdiagram.examples.slides.Slide;
import de.fxdiagram.examples.slides.SlideDiagram;
import de.fxdiagram.examples.slides.eclipsecon.EclipseConSlideFactory;
import de.fxdiagram.lib.simple.OpenableDiagramNode;
import java.util.Collections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;
import javafx.scene.text.Text;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@ModelNode({ "layoutX", "layoutY", "domainObject", "width", "height" })
@SuppressWarnings("all")
public class IntroductionSlideDeck extends OpenableDiagramNode {
  public IntroductionSlideDeck() {
    super("Introduction");
  }
  
  public void doActivate() {
    SlideDiagram _slideDiagram = new SlideDiagram();
    final Procedure1<SlideDiagram> _function = new Procedure1<SlideDiagram>() {
      public void apply(final SlideDiagram it) {
        ObservableList<Slide> _slides = it.getSlides();
        Slide _createSlide = EclipseConSlideFactory.createSlide("Title");
        final Procedure1<Slide> _function = new Procedure1<Slide>() {
          public void apply(final Slide it) {
            StackPane _stackPane = it.getStackPane();
            final Procedure1<StackPane> _function = new Procedure1<StackPane>() {
              public void apply(final StackPane it) {
                ObservableList<Node> _children = it.getChildren();
                VBox _vBox = new VBox();
                final Procedure1<VBox> _function = new Procedure1<VBox>() {
                  public void apply(final VBox it) {
                    it.setAlignment(Pos.CENTER);
                    Insets _insets = new Insets(200, 0, 0, 0);
                    StackPane.setMargin(it, _insets);
                    ObservableList<Node> _children = it.getChildren();
                    Text _createText = EclipseConSlideFactory.createText("Eclipse Discovery Channel", 36);
                    final Procedure1<Text> _function = new Procedure1<Text>() {
                      public void apply(final Text it) {
                        Color _jungleDarkGreen = EclipseConSlideFactory.jungleDarkGreen();
                        it.setFill(_jungleDarkGreen);
                      }
                    };
                    Text _doubleArrow = ObjectExtensions.<Text>operator_doubleArrow(_createText, _function);
                    _children.add(_doubleArrow);
                    ObservableList<Node> _children_1 = it.getChildren();
                    Text _createText_1 = EclipseConSlideFactory.createText("presents", 30);
                    final Procedure1<Text> _function_1 = new Procedure1<Text>() {
                      public void apply(final Text it) {
                        Color _jungleDarkGreen = EclipseConSlideFactory.jungleDarkGreen();
                        it.setFill(_jungleDarkGreen);
                      }
                    };
                    Text _doubleArrow_1 = ObjectExtensions.<Text>operator_doubleArrow(_createText_1, _function_1);
                    _children_1.add(_doubleArrow_1);
                    ObservableList<Node> _children_2 = it.getChildren();
                    Text _createText_2 = EclipseConSlideFactory.createText("Eclipse Diagram Editors", 93);
                    final Procedure1<Text> _function_2 = new Procedure1<Text>() {
                      public void apply(final Text it) {
                        Color _rgb = Color.rgb(238, 191, 171);
                        it.setFill(_rgb);
                      }
                    };
                    Text _doubleArrow_2 = ObjectExtensions.<Text>operator_doubleArrow(_createText_2, _function_2);
                    _children_2.add(_doubleArrow_2);
                    ObservableList<Node> _children_3 = it.getChildren();
                    Text _createText_3 = EclipseConSlideFactory.createText("An Endangered Species", 48);
                    final Procedure1<Text> _function_3 = new Procedure1<Text>() {
                      public void apply(final Text it) {
                      }
                    };
                    Text _doubleArrow_3 = ObjectExtensions.<Text>operator_doubleArrow(_createText_3, _function_3);
                    _children_3.add(_doubleArrow_3);
                  }
                };
                VBox _doubleArrow = ObjectExtensions.<VBox>operator_doubleArrow(_vBox, _function);
                _children.add(_doubleArrow);
              }
            };
            ObjectExtensions.<StackPane>operator_doubleArrow(_stackPane, _function);
          }
        };
        Slide _doubleArrow = ObjectExtensions.<Slide>operator_doubleArrow(_createSlide, _function);
        _slides.add(_doubleArrow);
        ObservableList<Slide> _slides_1 = it.getSlides();
        Slide _createSlide_1 = EclipseConSlideFactory.createSlide("The Eclipse Jungle", 110);
        _slides_1.add(_createSlide_1);
        ObservableList<Slide> _slides_2 = it.getSlides();
        Slide _createSlide_2 = EclipseConSlideFactory.createSlide("Jungle images");
        final Procedure1<Slide> _function_1 = new Procedure1<Slide>() {
          public void apply(final Slide it) {
            StackPane _stackPane = it.getStackPane();
            final Procedure1<StackPane> _function = new Procedure1<StackPane>() {
              public void apply(final StackPane it) {
                ObservableList<Node> _children = it.getChildren();
                Pane _pane = new Pane();
                final Procedure1<Pane> _function = new Procedure1<Pane>() {
                  public void apply(final Pane it) {
                    it.setPrefSize(1024, 768);
                    ObservableList<Node> _children = it.getChildren();
                    Text _createJungleText = EclipseConSlideFactory.createJungleText("GEF", 48);
                    final Procedure1<Text> _function = new Procedure1<Text>() {
                      public void apply(final Text it) {
                        Color _jungleDarkGreen = EclipseConSlideFactory.jungleDarkGreen();
                        it.setFill(_jungleDarkGreen);
                        it.setRotate(16);
                        it.setLayoutX(80);
                        it.setLayoutY(665);
                        Color _jungleDarkGreen_1 = EclipseConSlideFactory.jungleDarkGreen();
                        Color _jungleDarkestGreen = EclipseConSlideFactory.jungleDarkestGreen();
                        Animations.flicker(it, _jungleDarkGreen_1, _jungleDarkestGreen);
                      }
                    };
                    Text _doubleArrow = ObjectExtensions.<Text>operator_doubleArrow(_createJungleText, _function);
                    _children.add(_doubleArrow);
                    ObservableList<Node> _children_1 = it.getChildren();
                    Text _createJungleText_1 = EclipseConSlideFactory.createJungleText("Draw2D", 48);
                    final Procedure1<Text> _function_1 = new Procedure1<Text>() {
                      public void apply(final Text it) {
                        Color _jungleDarkGreen = EclipseConSlideFactory.jungleDarkGreen();
                        it.setFill(_jungleDarkGreen);
                        it.setRotate(338);
                        it.setLayoutX(380);
                        it.setLayoutY(132);
                        Animations.crawl(it);
                      }
                    };
                    Text _doubleArrow_1 = ObjectExtensions.<Text>operator_doubleArrow(_createJungleText_1, _function_1);
                    _children_1.add(_doubleArrow_1);
                    ObservableList<Node> _children_2 = it.getChildren();
                    Text _createJungleText_2 = EclipseConSlideFactory.createJungleText("GMF RT", 48);
                    final Procedure1<Text> _function_2 = new Procedure1<Text>() {
                      public void apply(final Text it) {
                        Color _jungleDarkGreen = EclipseConSlideFactory.jungleDarkGreen();
                        it.setFill(_jungleDarkGreen);
                        it.setRotate(10);
                        it.setLayoutX(560);
                        it.setLayoutY(300);
                        Animations.crawl(it);
                      }
                    };
                    Text _doubleArrow_2 = ObjectExtensions.<Text>operator_doubleArrow(_createJungleText_2, _function_2);
                    _children_2.add(_doubleArrow_2);
                    ObservableList<Node> _children_3 = it.getChildren();
                    Text _createJungleText_3 = EclipseConSlideFactory.createJungleText("GMF Tooling", 48);
                    final Procedure1<Text> _function_3 = new Procedure1<Text>() {
                      public void apply(final Text it) {
                        Color _jungleDarkGreen = EclipseConSlideFactory.jungleDarkGreen();
                        it.setFill(_jungleDarkGreen);
                        it.setRotate(332);
                        it.setLayoutX(640);
                        it.setLayoutY(620);
                        Color _jungleDarkGreen_1 = EclipseConSlideFactory.jungleDarkGreen();
                        Color _jungleDarkestGreen = EclipseConSlideFactory.jungleDarkestGreen();
                        Animations.breathe(it, _jungleDarkGreen_1, _jungleDarkestGreen);
                      }
                    };
                    Text _doubleArrow_3 = ObjectExtensions.<Text>operator_doubleArrow(_createJungleText_3, _function_3);
                    _children_3.add(_doubleArrow_3);
                    ObservableList<Node> _children_4 = it.getChildren();
                    Text _createJungleText_4 = EclipseConSlideFactory.createJungleText("Graphiti", 48);
                    final Procedure1<Text> _function_4 = new Procedure1<Text>() {
                      public void apply(final Text it) {
                        Color _jungleDarkGreen = EclipseConSlideFactory.jungleDarkGreen();
                        it.setFill(_jungleDarkGreen);
                        it.setLayoutX(111);
                        it.setLayoutY(167);
                        Animations.dangle(it);
                      }
                    };
                    Text _doubleArrow_4 = ObjectExtensions.<Text>operator_doubleArrow(_createJungleText_4, _function_4);
                    _children_4.add(_doubleArrow_4);
                    ObservableList<Node> _children_5 = it.getChildren();
                    Text _createJungleText_5 = EclipseConSlideFactory.createJungleText("Sirius", 48);
                    final Procedure1<Text> _function_5 = new Procedure1<Text>() {
                      public void apply(final Text it) {
                        Color _jungleDarkGreen = EclipseConSlideFactory.jungleDarkGreen();
                        it.setFill(_jungleDarkGreen);
                        it.setRotate(5);
                        it.setLayoutX(190);
                        it.setLayoutY(480);
                        Color _jungleDarkGreen_1 = EclipseConSlideFactory.jungleDarkGreen();
                        Color _jungleDarkestGreen = EclipseConSlideFactory.jungleDarkestGreen();
                        Animations.breathe(it, _jungleDarkGreen_1, _jungleDarkestGreen);
                      }
                    };
                    Text _doubleArrow_5 = ObjectExtensions.<Text>operator_doubleArrow(_createJungleText_5, _function_5);
                    _children_5.add(_doubleArrow_5);
                  }
                };
                Pane _doubleArrow = ObjectExtensions.<Pane>operator_doubleArrow(_pane, _function);
                _children.add(_doubleArrow);
              }
            };
            ObjectExtensions.<StackPane>operator_doubleArrow(_stackPane, _function);
          }
        };
        Slide _doubleArrow_1 = ObjectExtensions.<Slide>operator_doubleArrow(_createSlide_2, _function_1);
        _slides_2.add(_doubleArrow_1);
        ObservableList<Slide> _slides_3 = it.getSlides();
        Slide _createSlide_3 = EclipseConSlideFactory.createSlide("Appearance", 144);
        _slides_3.add(_createSlide_3);
        ObservableList<Slide> _slides_4 = it.getSlides();
        ClickThroughSlide _createClickThroughSlide = EclipseConSlideFactory.createClickThroughSlide("Darkness images");
        final Procedure1<ClickThroughSlide> _function_2 = new Procedure1<ClickThroughSlide>() {
          public void apply(final ClickThroughSlide it) {
            Group _pane = it.getPane();
            ObservableList<Node> _children = _pane.getChildren();
            ImageView _imageView = new ImageView();
            final Procedure1<ImageView> _function = new Procedure1<ImageView>() {
              public void apply(final ImageView it) {
                ImageCache _get = ImageCache.get();
                Image _image = _get.getImage(IntroductionSlideDeck.this, "images/darkness1.png");
                it.setImage(_image);
                it.setLayoutX(45);
                it.setLayoutY(45);
              }
            };
            ImageView _doubleArrow = ObjectExtensions.<ImageView>operator_doubleArrow(_imageView, _function);
            _children.add(_doubleArrow);
            Group _pane_1 = it.getPane();
            ObservableList<Node> _children_1 = _pane_1.getChildren();
            ImageView _imageView_1 = new ImageView();
            final Procedure1<ImageView> _function_1 = new Procedure1<ImageView>() {
              public void apply(final ImageView it) {
                ImageCache _get = ImageCache.get();
                Image _image = _get.getImage(IntroductionSlideDeck.this, "images/darkness2.png");
                it.setImage(_image);
                it.setLayoutX(420);
                it.setLayoutY(374);
              }
            };
            ImageView _doubleArrow_1 = ObjectExtensions.<ImageView>operator_doubleArrow(_imageView_1, _function_1);
            _children_1.add(_doubleArrow_1);
          }
        };
        ClickThroughSlide _doubleArrow_2 = ObjectExtensions.<ClickThroughSlide>operator_doubleArrow(_createClickThroughSlide, _function_2);
        _slides_4.add(_doubleArrow_2);
        ObservableList<Slide> _slides_5 = it.getSlides();
        Slide _createSlide_4 = EclipseConSlideFactory.createSlide("Behavior", 144);
        _slides_5.add(_createSlide_4);
        ObservableList<Slide> _slides_6 = it.getSlides();
        ClickThroughSlide _createClickThroughSlide_1 = EclipseConSlideFactory.createClickThroughSlide("Behavior images");
        final Procedure1<ClickThroughSlide> _function_3 = new Procedure1<ClickThroughSlide>() {
          public void apply(final ClickThroughSlide it) {
            Group _pane = it.getPane();
            ObservableList<Node> _children = _pane.getChildren();
            ImageView _imageView = new ImageView();
            final Procedure1<ImageView> _function = new Procedure1<ImageView>() {
              public void apply(final ImageView it) {
                ImageCache _get = ImageCache.get();
                Image _image = _get.getImage(IntroductionSlideDeck.this, "images/graphiti.png");
                it.setImage(_image);
                it.setLayoutX(50);
                it.setLayoutY(44);
              }
            };
            ImageView _doubleArrow = ObjectExtensions.<ImageView>operator_doubleArrow(_imageView, _function);
            _children.add(_doubleArrow);
            Group _pane_1 = it.getPane();
            ObservableList<Node> _children_1 = _pane_1.getChildren();
            ImageView _imageView_1 = new ImageView();
            final Procedure1<ImageView> _function_1 = new Procedure1<ImageView>() {
              public void apply(final ImageView it) {
                ImageCache _get = ImageCache.get();
                Image _image = _get.getImage(IntroductionSlideDeck.this, "images/properties.png");
                it.setImage(_image);
                it.setLayoutX(295);
                it.setLayoutY(332);
              }
            };
            ImageView _doubleArrow_1 = ObjectExtensions.<ImageView>operator_doubleArrow(_imageView_1, _function_1);
            _children_1.add(_doubleArrow_1);
          }
        };
        ClickThroughSlide _doubleArrow_3 = ObjectExtensions.<ClickThroughSlide>operator_doubleArrow(_createClickThroughSlide_1, _function_3);
        _slides_6.add(_doubleArrow_3);
        ObservableList<Slide> _slides_7 = it.getSlides();
        Slide _createSlide_5 = EclipseConSlideFactory.createSlide("Recycling", 144);
        _slides_7.add(_createSlide_5);
        ObservableList<Slide> _slides_8 = it.getSlides();
        ClickThroughSlide _createClickThroughSlide_2 = EclipseConSlideFactory.createClickThroughSlide("Recycling images");
        final Procedure1<ClickThroughSlide> _function_4 = new Procedure1<ClickThroughSlide>() {
          public void apply(final ClickThroughSlide it) {
            Group _pane = it.getPane();
            final Procedure1<Group> _function = new Procedure1<Group>() {
              public void apply(final Group it) {
                ObservableList<Node> _children = it.getChildren();
                ImageView _imageView = new ImageView();
                final Procedure1<ImageView> _function = new Procedure1<ImageView>() {
                  public void apply(final ImageView it) {
                    ImageCache _get = ImageCache.get();
                    Image _image = _get.getImage(IntroductionSlideDeck.this, "images/onion.png");
                    it.setImage(_image);
                    it.setFitWidth(570);
                    it.setPreserveRatio(true);
                    it.setLayoutX(227);
                    it.setLayoutY(110);
                  }
                };
                ImageView _doubleArrow = ObjectExtensions.<ImageView>operator_doubleArrow(_imageView, _function);
                _children.add(_doubleArrow);
                ObservableList<Node> _children_1 = it.getChildren();
                Group _group = new Group();
                final Procedure1<Group> _function_1 = new Procedure1<Group>() {
                  public void apply(final Group it) {
                    ObservableList<Node> _children = it.getChildren();
                    Text _createJungleText = EclipseConSlideFactory.createJungleText("OS", 48);
                    final Procedure1<Text> _function = new Procedure1<Text>() {
                      public void apply(final Text it) {
                        it.setLayoutX(173);
                        it.setLayoutY(216);
                      }
                    };
                    Text _doubleArrow = ObjectExtensions.<Text>operator_doubleArrow(_createJungleText, _function);
                    _children.add(_doubleArrow);
                    ObservableList<Node> _children_1 = it.getChildren();
                    Polyline _polyline = new Polyline();
                    final Procedure1<Polyline> _function_1 = new Procedure1<Polyline>() {
                      public void apply(final Polyline it) {
                        ObservableList<Double> _points = it.getPoints();
                        Iterables.<Double>addAll(_points, Collections.<Double>unmodifiableList(Lists.<Double>newArrayList(Double.valueOf(244.0), Double.valueOf(226.0), Double.valueOf(537.0), Double.valueOf(356.0))));
                        Color _jungleGreen = EclipseConSlideFactory.jungleGreen();
                        it.setStroke(_jungleGreen);
                        it.setStrokeWidth(2);
                      }
                    };
                    Polyline _doubleArrow_1 = ObjectExtensions.<Polyline>operator_doubleArrow(_polyline, _function_1);
                    _children_1.add(_doubleArrow_1);
                  }
                };
                Group _doubleArrow_1 = ObjectExtensions.<Group>operator_doubleArrow(_group, _function_1);
                _children_1.add(_doubleArrow_1);
                ObservableList<Node> _children_2 = it.getChildren();
                Group _group_1 = new Group();
                final Procedure1<Group> _function_2 = new Procedure1<Group>() {
                  public void apply(final Group it) {
                    ObservableList<Node> _children = it.getChildren();
                    Text _createJungleText = EclipseConSlideFactory.createJungleText("SWT", 48);
                    final Procedure1<Text> _function = new Procedure1<Text>() {
                      public void apply(final Text it) {
                        it.setLayoutX(62);
                        it.setLayoutY(458);
                      }
                    };
                    Text _doubleArrow = ObjectExtensions.<Text>operator_doubleArrow(_createJungleText, _function);
                    _children.add(_doubleArrow);
                    ObservableList<Node> _children_1 = it.getChildren();
                    Polyline _polyline = new Polyline();
                    final Procedure1<Polyline> _function_1 = new Procedure1<Polyline>() {
                      public void apply(final Polyline it) {
                        ObservableList<Double> _points = it.getPoints();
                        Iterables.<Double>addAll(_points, Collections.<Double>unmodifiableList(Lists.<Double>newArrayList(Double.valueOf(176.0), Double.valueOf(439.0), Double.valueOf(501.0), Double.valueOf(367.0))));
                        Color _jungleGreen = EclipseConSlideFactory.jungleGreen();
                        it.setStroke(_jungleGreen);
                        it.setStrokeWidth(2);
                      }
                    };
                    Polyline _doubleArrow_1 = ObjectExtensions.<Polyline>operator_doubleArrow(_polyline, _function_1);
                    _children_1.add(_doubleArrow_1);
                  }
                };
                Group _doubleArrow_2 = ObjectExtensions.<Group>operator_doubleArrow(_group_1, _function_2);
                _children_2.add(_doubleArrow_2);
                ObservableList<Node> _children_3 = it.getChildren();
                Group _group_2 = new Group();
                final Procedure1<Group> _function_3 = new Procedure1<Group>() {
                  public void apply(final Group it) {
                    ObservableList<Node> _children = it.getChildren();
                    Text _createJungleText = EclipseConSlideFactory.createJungleText("Draw2D", 48);
                    final Procedure1<Text> _function = new Procedure1<Text>() {
                      public void apply(final Text it) {
                        it.setLayoutX(129);
                        it.setLayoutY(666);
                      }
                    };
                    Text _doubleArrow = ObjectExtensions.<Text>operator_doubleArrow(_createJungleText, _function);
                    _children.add(_doubleArrow);
                    ObservableList<Node> _children_1 = it.getChildren();
                    Polyline _polyline = new Polyline();
                    final Procedure1<Polyline> _function_1 = new Procedure1<Polyline>() {
                      public void apply(final Polyline it) {
                        ObservableList<Double> _points = it.getPoints();
                        Iterables.<Double>addAll(_points, Collections.<Double>unmodifiableList(Lists.<Double>newArrayList(Double.valueOf(307.0), Double.valueOf(611.0), Double.valueOf(489.0), Double.valueOf(433.0))));
                        Color _jungleGreen = EclipseConSlideFactory.jungleGreen();
                        it.setStroke(_jungleGreen);
                        it.setStrokeWidth(2);
                      }
                    };
                    Polyline _doubleArrow_1 = ObjectExtensions.<Polyline>operator_doubleArrow(_polyline, _function_1);
                    _children_1.add(_doubleArrow_1);
                  }
                };
                Group _doubleArrow_3 = ObjectExtensions.<Group>operator_doubleArrow(_group_2, _function_3);
                _children_3.add(_doubleArrow_3);
                ObservableList<Node> _children_4 = it.getChildren();
                Group _group_3 = new Group();
                final Procedure1<Group> _function_4 = new Procedure1<Group>() {
                  public void apply(final Group it) {
                    ObservableList<Node> _children = it.getChildren();
                    Text _createJungleText = EclipseConSlideFactory.createJungleText("GEF MVC", 48);
                    final Procedure1<Text> _function = new Procedure1<Text>() {
                      public void apply(final Text it) {
                        it.setLayoutX(581);
                        it.setLayoutY(712);
                      }
                    };
                    Text _doubleArrow = ObjectExtensions.<Text>operator_doubleArrow(_createJungleText, _function);
                    _children.add(_doubleArrow);
                    ObservableList<Node> _children_1 = it.getChildren();
                    Polyline _polyline = new Polyline();
                    final Procedure1<Polyline> _function_1 = new Procedure1<Polyline>() {
                      public void apply(final Polyline it) {
                        ObservableList<Double> _points = it.getPoints();
                        Iterables.<Double>addAll(_points, Collections.<Double>unmodifiableList(Lists.<Double>newArrayList(Double.valueOf(714.0), Double.valueOf(662.0), Double.valueOf(588.0), Double.valueOf(458.0))));
                        Color _jungleGreen = EclipseConSlideFactory.jungleGreen();
                        it.setStroke(_jungleGreen);
                        it.setStrokeWidth(2);
                      }
                    };
                    Polyline _doubleArrow_1 = ObjectExtensions.<Polyline>operator_doubleArrow(_polyline, _function_1);
                    _children_1.add(_doubleArrow_1);
                  }
                };
                Group _doubleArrow_4 = ObjectExtensions.<Group>operator_doubleArrow(_group_3, _function_4);
                _children_4.add(_doubleArrow_4);
                ObservableList<Node> _children_5 = it.getChildren();
                Group _group_4 = new Group();
                final Procedure1<Group> _function_5 = new Procedure1<Group>() {
                  public void apply(final Group it) {
                    ObservableList<Node> _children = it.getChildren();
                    StringConcatenation _builder = new StringConcatenation();
                    _builder.append("GMF");
                    _builder.newLine();
                    _builder.append("Runtime");
                    Text _createJungleText = EclipseConSlideFactory.createJungleText(_builder.toString(), 48);
                    final Procedure1<Text> _function = new Procedure1<Text>() {
                      public void apply(final Text it) {
                        it.setLayoutX(770);
                        it.setLayoutY(462);
                      }
                    };
                    Text _doubleArrow = ObjectExtensions.<Text>operator_doubleArrow(_createJungleText, _function);
                    _children.add(_doubleArrow);
                    ObservableList<Node> _children_1 = it.getChildren();
                    Polyline _polyline = new Polyline();
                    final Procedure1<Polyline> _function_1 = new Procedure1<Polyline>() {
                      public void apply(final Polyline it) {
                        ObservableList<Double> _points = it.getPoints();
                        Iterables.<Double>addAll(_points, Collections.<Double>unmodifiableList(Lists.<Double>newArrayList(Double.valueOf(803.0), Double.valueOf(462.0), Double.valueOf(658.0), Double.valueOf(416.0))));
                        Color _jungleGreen = EclipseConSlideFactory.jungleGreen();
                        it.setStroke(_jungleGreen);
                        it.setStrokeWidth(2);
                      }
                    };
                    Polyline _doubleArrow_1 = ObjectExtensions.<Polyline>operator_doubleArrow(_polyline, _function_1);
                    _children_1.add(_doubleArrow_1);
                  }
                };
                Group _doubleArrow_5 = ObjectExtensions.<Group>operator_doubleArrow(_group_4, _function_5);
                _children_5.add(_doubleArrow_5);
                ObservableList<Node> _children_6 = it.getChildren();
                Group _group_5 = new Group();
                final Procedure1<Group> _function_6 = new Procedure1<Group>() {
                  public void apply(final Group it) {
                    ObservableList<Node> _children = it.getChildren();
                    StringConcatenation _builder = new StringConcatenation();
                    _builder.append("GMF");
                    _builder.newLine();
                    _builder.append("Tooling");
                    Text _createJungleText = EclipseConSlideFactory.createJungleText(_builder.toString(), 48);
                    final Procedure1<Text> _function = new Procedure1<Text>() {
                      public void apply(final Text it) {
                        it.setLayoutX(770);
                        it.setLayoutY(184);
                      }
                    };
                    Text _doubleArrow = ObjectExtensions.<Text>operator_doubleArrow(_createJungleText, _function);
                    _children.add(_doubleArrow);
                    ObservableList<Node> _children_1 = it.getChildren();
                    Polyline _polyline = new Polyline();
                    final Procedure1<Polyline> _function_1 = new Procedure1<Polyline>() {
                      public void apply(final Polyline it) {
                        ObservableList<Double> _points = it.getPoints();
                        Iterables.<Double>addAll(_points, Collections.<Double>unmodifiableList(Lists.<Double>newArrayList(Double.valueOf(766.0), Double.valueOf(206.0), Double.valueOf(662.0), Double.valueOf(281.0))));
                        Color _jungleGreen = EclipseConSlideFactory.jungleGreen();
                        it.setStroke(_jungleGreen);
                        it.setStrokeWidth(2);
                      }
                    };
                    Polyline _doubleArrow_1 = ObjectExtensions.<Polyline>operator_doubleArrow(_polyline, _function_1);
                    _children_1.add(_doubleArrow_1);
                  }
                };
                Group _doubleArrow_6 = ObjectExtensions.<Group>operator_doubleArrow(_group_5, _function_6);
                _children_6.add(_doubleArrow_6);
                ObservableList<Node> _children_7 = it.getChildren();
                Group _group_6 = new Group();
                final Procedure1<Group> _function_7 = new Procedure1<Group>() {
                  public void apply(final Group it) {
                    ObservableList<Node> _children = it.getChildren();
                    Text _createJungleText = EclipseConSlideFactory.createJungleText("...", 48);
                    final Procedure1<Text> _function = new Procedure1<Text>() {
                      public void apply(final Text it) {
                        it.setLayoutX(405);
                        it.setLayoutY(88);
                      }
                    };
                    Text _doubleArrow = ObjectExtensions.<Text>operator_doubleArrow(_createJungleText, _function);
                    _children.add(_doubleArrow);
                    ObservableList<Node> _children_1 = it.getChildren();
                    Polyline _polyline = new Polyline();
                    final Procedure1<Polyline> _function_1 = new Procedure1<Polyline>() {
                      public void apply(final Polyline it) {
                        ObservableList<Double> _points = it.getPoints();
                        Iterables.<Double>addAll(_points, Collections.<Double>unmodifiableList(Lists.<Double>newArrayList(Double.valueOf(519.0), Double.valueOf(101.0), Double.valueOf(525.0), Double.valueOf(188.0))));
                        Color _jungleGreen = EclipseConSlideFactory.jungleGreen();
                        it.setStroke(_jungleGreen);
                        it.setStrokeWidth(2);
                      }
                    };
                    Polyline _doubleArrow_1 = ObjectExtensions.<Polyline>operator_doubleArrow(_polyline, _function_1);
                    _children_1.add(_doubleArrow_1);
                  }
                };
                Group _doubleArrow_7 = ObjectExtensions.<Group>operator_doubleArrow(_group_6, _function_7);
                _children_7.add(_doubleArrow_7);
              }
            };
            ObjectExtensions.<Group>operator_doubleArrow(_pane, _function);
          }
        };
        ClickThroughSlide _doubleArrow_4 = ObjectExtensions.<ClickThroughSlide>operator_doubleArrow(_createClickThroughSlide_2, _function_4);
        _slides_8.add(_doubleArrow_4);
        ObservableList<Slide> _slides_9 = it.getSlides();
        Slide _createSlide_6 = EclipseConSlideFactory.createSlide("Reproduction", 144);
        _slides_9.add(_createSlide_6);
        ObservableList<Slide> _slides_10 = it.getSlides();
        ClickThroughSlide _createClickThroughSlide_3 = EclipseConSlideFactory.createClickThroughSlide("Reproduction images");
        final Procedure1<ClickThroughSlide> _function_5 = new Procedure1<ClickThroughSlide>() {
          public void apply(final ClickThroughSlide it) {
            Group _pane = it.getPane();
            ObservableList<Node> _children = _pane.getChildren();
            ImageView _imageView = new ImageView();
            final Procedure1<ImageView> _function = new Procedure1<ImageView>() {
              public void apply(final ImageView it) {
                ImageCache _get = ImageCache.get();
                Image _image = _get.getImage(IntroductionSlideDeck.this, "images/graphiti_code.png");
                it.setImage(_image);
                it.setLayoutX(43);
                it.setLayoutY(41);
              }
            };
            ImageView _doubleArrow = ObjectExtensions.<ImageView>operator_doubleArrow(_imageView, _function);
            _children.add(_doubleArrow);
            Group _pane_1 = it.getPane();
            ObservableList<Node> _children_1 = _pane_1.getChildren();
            VBox _vBox = new VBox();
            final Procedure1<VBox> _function_1 = new Procedure1<VBox>() {
              public void apply(final VBox it) {
                it.setLayoutX(313);
                it.setLayoutY(81);
                Insets _insets = new Insets(5, 5, 5, 5);
                it.setPadding(_insets);
                it.setAlignment(Pos.CENTER);
                StringConcatenation _builder = new StringConcatenation();
                _builder.append("-fx-border-color: black;");
                _builder.newLine();
                _builder.append("-fx-border-width: 1;");
                _builder.newLine();
                _builder.append("-fx-background-color: rgb(252,228,153);");
                _builder.newLine();
                it.setStyle(_builder.toString());
                ObservableList<Node> _children = it.getChildren();
                Text _createText = EclipseConSlideFactory.createText("34 Files", 36);
                final Procedure1<Text> _function = new Procedure1<Text>() {
                  public void apply(final Text it) {
                    it.setFill(Color.BLACK);
                  }
                };
                Text _doubleArrow = ObjectExtensions.<Text>operator_doubleArrow(_createText, _function);
                _children.add(_doubleArrow);
                ObservableList<Node> _children_1 = it.getChildren();
                Text _createText_1 = EclipseConSlideFactory.createText("2730 LOC", 36);
                final Procedure1<Text> _function_1 = new Procedure1<Text>() {
                  public void apply(final Text it) {
                    it.setFill(Color.BLACK);
                  }
                };
                Text _doubleArrow_1 = ObjectExtensions.<Text>operator_doubleArrow(_createText_1, _function_1);
                _children_1.add(_doubleArrow_1);
              }
            };
            VBox _doubleArrow_1 = ObjectExtensions.<VBox>operator_doubleArrow(_vBox, _function_1);
            _children_1.add(_doubleArrow_1);
            Group _pane_2 = it.getPane();
            ObservableList<Node> _children_2 = _pane_2.getChildren();
            ImageView _imageView_1 = new ImageView();
            final Procedure1<ImageView> _function_2 = new Procedure1<ImageView>() {
              public void apply(final ImageView it) {
                ImageCache _get = ImageCache.get();
                Image _image = _get.getImage(IntroductionSlideDeck.this, "images/gmf_dashboard.png");
                it.setImage(_image);
                it.setLayoutX(284);
                it.setLayoutY(406);
              }
            };
            ImageView _doubleArrow_2 = ObjectExtensions.<ImageView>operator_doubleArrow(_imageView_1, _function_2);
            _children_2.add(_doubleArrow_2);
          }
        };
        ClickThroughSlide _doubleArrow_5 = ObjectExtensions.<ClickThroughSlide>operator_doubleArrow(_createClickThroughSlide_3, _function_5);
        _slides_10.add(_doubleArrow_5);
        ObservableList<Slide> _slides_11 = it.getSlides();
        Slide _createSlide_7 = EclipseConSlideFactory.createSlide("Endangerment", 144);
        _slides_11.add(_createSlide_7);
        ObservableList<Slide> _slides_12 = it.getSlides();
        ClickThroughSlide _createClickThroughSlide_4 = EclipseConSlideFactory.createClickThroughSlide("Tablet");
        final Procedure1<ClickThroughSlide> _function_6 = new Procedure1<ClickThroughSlide>() {
          public void apply(final ClickThroughSlide it) {
            Group _pane = it.getPane();
            ObservableList<Node> _children = _pane.getChildren();
            ImageView _imageView = new ImageView();
            final Procedure1<ImageView> _function = new Procedure1<ImageView>() {
              public void apply(final ImageView it) {
                ImageCache _get = ImageCache.get();
                Image _image = _get.getImage(IntroductionSlideDeck.this, "images/tablet.png");
                it.setImage(_image);
                it.setLayoutX(183);
                it.setLayoutY(210);
                it.setFitWidth(587);
                it.setPreserveRatio(true);
              }
            };
            ImageView _doubleArrow = ObjectExtensions.<ImageView>operator_doubleArrow(_imageView, _function);
            _children.add(_doubleArrow);
            Group _pane_1 = it.getPane();
            ObservableList<Node> _children_1 = _pane_1.getChildren();
            ImageView _imageView_1 = new ImageView();
            final Procedure1<ImageView> _function_1 = new Procedure1<ImageView>() {
              public void apply(final ImageView it) {
                ImageCache _get = ImageCache.get();
                Image _image = _get.getImage(IntroductionSlideDeck.this, "images/hand.png");
                it.setImage(_image);
                it.setLayoutX(540);
                it.setLayoutY(244);
              }
            };
            ImageView _doubleArrow_1 = ObjectExtensions.<ImageView>operator_doubleArrow(_imageView_1, _function_1);
            _children_1.add(_doubleArrow_1);
          }
        };
        ClickThroughSlide _doubleArrow_6 = ObjectExtensions.<ClickThroughSlide>operator_doubleArrow(_createClickThroughSlide_4, _function_6);
        _slides_12.add(_doubleArrow_6);
        ObservableList<Slide> _slides_13 = it.getSlides();
        Slide _createSlide_8 = EclipseConSlideFactory.createSlide("Help");
        final Procedure1<Slide> _function_7 = new Procedure1<Slide>() {
          public void apply(final Slide it) {
            StackPane _stackPane = it.getStackPane();
            final Procedure1<StackPane> _function = new Procedure1<StackPane>() {
              public void apply(final StackPane it) {
                ObservableList<Node> _children = it.getChildren();
                VBox _vBox = new VBox();
                final Procedure1<VBox> _function = new Procedure1<VBox>() {
                  public void apply(final VBox it) {
                    it.setAlignment(Pos.CENTER);
                    it.setSpacing(50);
                    ObservableList<Node> _children = it.getChildren();
                    Text _createText = EclipseConSlideFactory.createText("Help Us", 144);
                    final Procedure1<Text> _function = new Procedure1<Text>() {
                      public void apply(final Text it) {
                      }
                    };
                    Text _doubleArrow = ObjectExtensions.<Text>operator_doubleArrow(_createText, _function);
                    _children.add(_doubleArrow);
                    ObservableList<Node> _children_1 = it.getChildren();
                    Text _createText_1 = EclipseConSlideFactory.createText("save the", 72);
                    final Procedure1<Text> _function_1 = new Procedure1<Text>() {
                      public void apply(final Text it) {
                      }
                    };
                    Text _doubleArrow_1 = ObjectExtensions.<Text>operator_doubleArrow(_createText_1, _function_1);
                    _children_1.add(_doubleArrow_1);
                    ObservableList<Node> _children_2 = it.getChildren();
                    Text _createText_2 = EclipseConSlideFactory.createText("Eclipse Diagram Editors", 96);
                    final Procedure1<Text> _function_2 = new Procedure1<Text>() {
                      public void apply(final Text it) {
                        Color _rgb = Color.rgb(238, 191, 171);
                        it.setFill(_rgb);
                      }
                    };
                    Text _doubleArrow_2 = ObjectExtensions.<Text>operator_doubleArrow(_createText_2, _function_2);
                    _children_2.add(_doubleArrow_2);
                  }
                };
                VBox _doubleArrow = ObjectExtensions.<VBox>operator_doubleArrow(_vBox, _function);
                _children.add(_doubleArrow);
              }
            };
            ObjectExtensions.<StackPane>operator_doubleArrow(_stackPane, _function);
          }
        };
        Slide _doubleArrow_7 = ObjectExtensions.<Slide>operator_doubleArrow(_createSlide_8, _function_7);
        _slides_13.add(_doubleArrow_7);
        ObservableList<Slide> _slides_14 = it.getSlides();
        Slide _createSlide_9 = EclipseConSlideFactory.createSlide("What Can We Do?", 96);
        _slides_14.add(_createSlide_9);
        ObservableList<Slide> _slides_15 = it.getSlides();
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("We have improve visual design,");
        _builder.newLine();
        _builder.append("haptic behavior,");
        _builder.newLine();
        _builder.append("usability,");
        _builder.newLine();
        _builder.append("and customizability");
        _builder.newLine();
        _builder.append("in order to save them from extinction.");
        _builder.newLine();
        Slide _createSlide_10 = EclipseConSlideFactory.createSlide(_builder.toString(), 48);
        _slides_15.add(_createSlide_10);
        ObservableList<Slide> _slides_16 = it.getSlides();
        Slide _createSlide_11 = EclipseConSlideFactory.createSlide("JavaFX");
        final Procedure1<Slide> _function_8 = new Procedure1<Slide>() {
          public void apply(final Slide it) {
            StackPane _stackPane = it.getStackPane();
            final Procedure1<StackPane> _function = new Procedure1<StackPane>() {
              public void apply(final StackPane it) {
                ObservableList<Node> _children = it.getChildren();
                ImageView _imageView = new ImageView();
                final Procedure1<ImageView> _function = new Procedure1<ImageView>() {
                  public void apply(final ImageView it) {
                    ImageCache _get = ImageCache.get();
                    Image _image = _get.getImage(IntroductionSlideDeck.this, "images/javafx.png");
                    it.setImage(_image);
                    it.setFitWidth(587);
                    it.setPreserveRatio(true);
                  }
                };
                ImageView _doubleArrow = ObjectExtensions.<ImageView>operator_doubleArrow(_imageView, _function);
                _children.add(_doubleArrow);
              }
            };
            ObjectExtensions.<StackPane>operator_doubleArrow(_stackPane, _function);
          }
        };
        Slide _doubleArrow_8 = ObjectExtensions.<Slide>operator_doubleArrow(_createSlide_11, _function_8);
        _slides_16.add(_doubleArrow_8);
      }
    };
    SlideDiagram _doubleArrow = ObjectExtensions.<SlideDiagram>operator_doubleArrow(_slideDiagram, _function);
    this.setInnerDiagram(_doubleArrow);
    super.doActivate();
  }
  
  public void populate(final ModelElement modelElement) {
    modelElement.addProperty(layoutXProperty(), Double.class);
    modelElement.addProperty(layoutYProperty(), Double.class);
    modelElement.addProperty(domainObjectProperty(), DomainObjectHandle.class);
    modelElement.addProperty(widthProperty(), Double.class);
    modelElement.addProperty(heightProperty(), Double.class);
  }
}
