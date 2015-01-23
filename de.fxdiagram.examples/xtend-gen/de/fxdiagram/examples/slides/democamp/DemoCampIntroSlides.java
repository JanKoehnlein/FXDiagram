package de.fxdiagram.examples.slides.democamp;

import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.extensions.DurationExtensions;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.core.services.ImageCache;
import de.fxdiagram.examples.slides.Animations;
import de.fxdiagram.examples.slides.ClickThroughSlide;
import de.fxdiagram.examples.slides.Slide;
import de.fxdiagram.examples.slides.SlideDiagram;
import de.fxdiagram.examples.slides.democamp.DemoCampSlideFactory;
import de.fxdiagram.lib.simple.OpenableDiagramNode;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@ModelNode
@SuppressWarnings("all")
public class DemoCampIntroSlides extends OpenableDiagramNode {
  public DemoCampIntroSlides() {
    super("Introduction");
  }
  
  public void doActivate() {
    SlideDiagram _slideDiagram = new SlideDiagram();
    final Procedure1<SlideDiagram> _function = new Procedure1<SlideDiagram>() {
      public void apply(final SlideDiagram it) {
        ObservableList<Slide> _slides = it.getSlides();
        Slide _createSlide = DemoCampSlideFactory.createSlide("Title");
        final Procedure1<Slide> _function = new Procedure1<Slide>() {
          public void apply(final Slide it) {
            final VBox vbox = new VBox();
            StackPane _stackPane = it.getStackPane();
            final Procedure1<StackPane> _function = new Procedure1<StackPane>() {
              public void apply(final StackPane it) {
                ObservableList<Node> _children = it.getChildren();
                final Procedure1<VBox> _function = new Procedure1<VBox>() {
                  public void apply(final VBox it) {
                    it.setAlignment(Pos.CENTER);
                    Insets _insets = new Insets(350, 0, 0, 0);
                    StackPane.setMargin(it, _insets);
                    ObservableList<Node> _children = it.getChildren();
                    Text _createText = DemoCampSlideFactory.createText("Eclipse Diagram Editors", 120);
                    _children.add(_createText);
                    ObservableList<Node> _children_1 = it.getChildren();
                    Text _createText_1 = DemoCampSlideFactory.createText("The FXed Generation", 72);
                    _children_1.add(_createText_1);
                    ObservableList<Node> _children_2 = it.getChildren();
                    Text _createText_2 = DemoCampSlideFactory.createText("Jan Koehnlein - itemis", 32);
                    final Procedure1<Text> _function = new Procedure1<Text>() {
                      public void apply(final Text it) {
                        Insets _insets = new Insets(30, 0, 0, 0);
                        VBox.setMargin(it, _insets);
                      }
                    };
                    Text _doubleArrow = ObjectExtensions.<Text>operator_doubleArrow(_createText_2, _function);
                    _children_2.add(_doubleArrow);
                  }
                };
                VBox _doubleArrow = ObjectExtensions.<VBox>operator_doubleArrow(vbox, _function);
                _children.add(_doubleArrow);
              }
            };
            ObjectExtensions.<StackPane>operator_doubleArrow(_stackPane, _function);
          }
        };
        Slide _doubleArrow = ObjectExtensions.<Slide>operator_doubleArrow(_createSlide, _function);
        _slides.add(_doubleArrow);
        ObservableList<Slide> _slides_1 = it.getSlides();
        Slide _createSlide_1 = DemoCampSlideFactory.createSlide("Frameworks");
        final Procedure1<Slide> _function_1 = new Procedure1<Slide>() {
          public void apply(final Slide slide) {
            StackPane _stackPane = slide.getStackPane();
            final Procedure1<StackPane> _function = new Procedure1<StackPane>() {
              public void apply(final StackPane it) {
                ObservableList<Node> _children = it.getChildren();
                Pane _pane = new Pane();
                final Procedure1<Pane> _function = new Procedure1<Pane>() {
                  public void apply(final Pane it) {
                    Rectangle _rectangle = new Rectangle(0, 0, 1024, 768);
                    it.setClip(_rectangle);
                    ObservableList<Node> _children = it.getChildren();
                    Group _group = new Group();
                    final Procedure1<Group> _function = new Procedure1<Group>() {
                      public void apply(final Group it) {
                        it.setLayoutX(501);
                        it.setLayoutY(367);
                        ObservableList<Node> _children = it.getChildren();
                        Group _group = new Group();
                        final Procedure1<Group> _function = new Procedure1<Group>() {
                          public void apply(final Group it) {
                            it.setRotate(60);
                            ObservableList<Node> _children = it.getChildren();
                            Text _createText = DemoCampSlideFactory.createText("GMF", 50);
                            final Procedure1<Text> _function = new Procedure1<Text>() {
                              public void apply(final Text it) {
                                Duration _seconds = DurationExtensions.seconds(10);
                                Animations.orbit(it, 110, 100, _seconds, 0);
                              }
                            };
                            Text _doubleArrow = ObjectExtensions.<Text>operator_doubleArrow(_createText, _function);
                            _children.add(_doubleArrow);
                          }
                        };
                        Group _doubleArrow = ObjectExtensions.<Group>operator_doubleArrow(_group, _function);
                        _children.add(_doubleArrow);
                      }
                    };
                    Group _doubleArrow = ObjectExtensions.<Group>operator_doubleArrow(_group, _function);
                    _children.add(_doubleArrow);
                    ObservableList<Node> _children_1 = it.getChildren();
                    Group _group_1 = new Group();
                    final Procedure1<Group> _function_1 = new Procedure1<Group>() {
                      public void apply(final Group it) {
                        it.setRotate(30);
                        it.setLayoutX(501);
                        it.setLayoutY(367);
                        ObservableList<Node> _children = it.getChildren();
                        Text _createText = DemoCampSlideFactory.createText("Graphiti", 50);
                        final Procedure1<Text> _function = new Procedure1<Text>() {
                          public void apply(final Text it) {
                            Duration _seconds = DurationExtensions.seconds(30);
                            Animations.orbit(it, 400, 250, _seconds, 0);
                          }
                        };
                        Text _doubleArrow = ObjectExtensions.<Text>operator_doubleArrow(_createText, _function);
                        _children.add(_doubleArrow);
                      }
                    };
                    Group _doubleArrow_1 = ObjectExtensions.<Group>operator_doubleArrow(_group_1, _function_1);
                    _children_1.add(_doubleArrow_1);
                    ObservableList<Node> _children_2 = it.getChildren();
                    Group _group_2 = new Group();
                    final Procedure1<Group> _function_2 = new Procedure1<Group>() {
                      public void apply(final Group it) {
                        it.setLayoutX(501);
                        it.setLayoutY(367);
                        it.setRotate((-15));
                        ObservableList<Node> _children = it.getChildren();
                        Text _createText = DemoCampSlideFactory.createText("Sirius", 50);
                        final Procedure1<Text> _function = new Procedure1<Text>() {
                          public void apply(final Text it) {
                            Duration _seconds = DurationExtensions.seconds(20);
                            Animations.orbit(it, 300, 200, _seconds, 0);
                          }
                        };
                        Text _doubleArrow = ObjectExtensions.<Text>operator_doubleArrow(_createText, _function);
                        _children.add(_doubleArrow);
                      }
                    };
                    Group _doubleArrow_2 = ObjectExtensions.<Group>operator_doubleArrow(_group_2, _function_2);
                    _children_2.add(_doubleArrow_2);
                    ObservableList<Node> _children_3 = it.getChildren();
                    Text _createText = DemoCampSlideFactory.createText("GEF", 92);
                    final Procedure1<Text> _function_3 = new Procedure1<Text>() {
                      public void apply(final Text it) {
                        it.setRotate(5);
                        it.setLayoutX(490);
                        it.setLayoutY(380);
                        Animations.spin(it);
                      }
                    };
                    Text _doubleArrow_3 = ObjectExtensions.<Text>operator_doubleArrow(_createText, _function_3);
                    _children_3.add(_doubleArrow_3);
                  }
                };
                Pane _doubleArrow = ObjectExtensions.<Pane>operator_doubleArrow(_pane, _function);
                _children.add(_doubleArrow);
              }
            };
            ObjectExtensions.<StackPane>operator_doubleArrow(_stackPane, _function);
          }
        };
        Slide _doubleArrow_1 = ObjectExtensions.<Slide>operator_doubleArrow(_createSlide_1, _function_1);
        _slides_1.add(_doubleArrow_1);
        ObservableList<Slide> _slides_2 = it.getSlides();
        ClickThroughSlide _createClickThroughSlide = DemoCampSlideFactory.createClickThroughSlide("Title");
        final Procedure1<ClickThroughSlide> _function_2 = new Procedure1<ClickThroughSlide>() {
          public void apply(final ClickThroughSlide it) {
            Pane _pane = it.getPane();
            final Procedure1<Pane> _function = new Procedure1<Pane>() {
              public void apply(final Pane it) {
                DemoCampIntroSlides.this.addComparisonLeft(it, "Framework", 72, 100);
                DemoCampIntroSlides.this.addComparisonLeft(it, "Designed For Developers", 48, 250);
                DemoCampIntroSlides.this.addComparisonLeft(it, "High-level Abstractions", 48, 310);
                DemoCampIntroSlides.this.addComparisonLeft(it, "Max Features w/ Min Effort", 48, 370);
                DemoCampIntroSlides.this.addComparisonLeft(it, "Unified Usecase", 48, 430);
                DemoCampIntroSlides.this.addComparisonLeft(it, "Standard Behavior", 48, 490);
                DemoCampIntroSlides.this.addComparisonLeft(it, "Rendering Issues", 48, 550);
                DemoCampIntroSlides.this.addComparisonRight(it, "Product", 72, 100);
                DemoCampIntroSlides.this.addComparisonRight(it, "Designed For Users", 48, 250);
                DemoCampIntroSlides.this.addComparisonRight(it, "Custom Solution", 48, 310);
                DemoCampIntroSlides.this.addComparisonRight(it, "Usability", 48, 370);
                DemoCampIntroSlides.this.addComparisonRight(it, "Specific Usecase", 48, 430);
                DemoCampIntroSlides.this.addComparisonRight(it, "Fun to Use", 48, 490);
                DemoCampIntroSlides.this.addComparisonRight(it, "Stunning Visuals", 48, 550);
              }
            };
            ObjectExtensions.<Pane>operator_doubleArrow(_pane, _function);
          }
        };
        ClickThroughSlide _doubleArrow_2 = ObjectExtensions.<ClickThroughSlide>operator_doubleArrow(_createClickThroughSlide, _function_2);
        _slides_2.add(_doubleArrow_2);
        ObservableList<Slide> _slides_3 = it.getSlides();
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("From a user\'s perspective");
        _builder.newLine();
        _builder.append("you shouldn\'t use a framework...");
        _builder.newLine();
        Slide _createSlideWithText = DemoCampSlideFactory.createSlideWithText("Problem", _builder.toString(), 72);
        _slides_3.add(_createSlideWithText);
        ObservableList<Slide> _slides_4 = it.getSlides();
        StringConcatenation _builder_1 = new StringConcatenation();
        _builder_1.append("...but coding everything from scratch");
        _builder_1.newLine();
        _builder_1.append("doesn\'t seem to be an option.");
        _builder_1.newLine();
        Slide _createSlideWithText_1 = DemoCampSlideFactory.createSlideWithText("Problem", _builder_1.toString(), 72);
        _slides_4.add(_createSlideWithText_1);
        ObservableList<Slide> _slides_5 = it.getSlides();
        Slide _createSlide_2 = DemoCampSlideFactory.createSlide("JavaFX");
        final Procedure1<Slide> _function_3 = new Procedure1<Slide>() {
          public void apply(final Slide it) {
            StackPane _stackPane = it.getStackPane();
            ObservableList<Node> _children = _stackPane.getChildren();
            ImageView _imageView = new ImageView();
            final Procedure1<ImageView> _function = new Procedure1<ImageView>() {
              public void apply(final ImageView it) {
                ImageCache _get = ImageCache.get();
                Image _image = _get.getImage(DemoCampIntroSlides.this, "images/javafx.png");
                it.setImage(_image);
                it.setFitWidth(587);
                it.setPreserveRatio(true);
              }
            };
            ImageView _doubleArrow = ObjectExtensions.<ImageView>operator_doubleArrow(_imageView, _function);
            _children.add(_doubleArrow);
          }
        };
        Slide _doubleArrow_3 = ObjectExtensions.<Slide>operator_doubleArrow(_createSlide_2, _function_3);
        _slides_5.add(_doubleArrow_3);
        ObservableList<Slide> _slides_6 = it.getSlides();
        Slide _createSlide_3 = DemoCampSlideFactory.createSlide("Xtend");
        final Procedure1<Slide> _function_4 = new Procedure1<Slide>() {
          public void apply(final Slide it) {
            StackPane _stackPane = it.getStackPane();
            ObservableList<Node> _children = _stackPane.getChildren();
            ImageView _imageView = new ImageView();
            final Procedure1<ImageView> _function = new Procedure1<ImageView>() {
              public void apply(final ImageView it) {
                ImageCache _get = ImageCache.get();
                Image _image = _get.getImage(DemoCampIntroSlides.this, "images/xtend.png");
                it.setImage(_image);
              }
            };
            ImageView _doubleArrow = ObjectExtensions.<ImageView>operator_doubleArrow(_imageView, _function);
            _children.add(_doubleArrow);
          }
        };
        Slide _doubleArrow_4 = ObjectExtensions.<Slide>operator_doubleArrow(_createSlide_3, _function_4);
        _slides_6.add(_doubleArrow_4);
      }
    };
    SlideDiagram _doubleArrow = ObjectExtensions.<SlideDiagram>operator_doubleArrow(_slideDiagram, _function);
    this.setInnerDiagram(_doubleArrow);
    super.doActivate();
  }
  
  protected Pane addComparisonLeft(final Pane pane, final String left, final int size, final int y) {
    final Procedure1<Pane> _function = new Procedure1<Pane>() {
      public void apply(final Pane it) {
        ObservableList<Node> _children = it.getChildren();
        Text _createText = DemoCampSlideFactory.createText(left, size);
        final Procedure1<Text> _function = new Procedure1<Text>() {
          public void apply(final Text it) {
            it.setLayoutX(100);
            it.setLayoutY(y);
            it.setTextAlignment(TextAlignment.LEFT);
            it.setTextOrigin(VPos.TOP);
            Color _darkTextColor = DemoCampSlideFactory.getDarkTextColor();
            it.setFill(_darkTextColor);
          }
        };
        Text _doubleArrow = ObjectExtensions.<Text>operator_doubleArrow(_createText, _function);
        _children.add(_doubleArrow);
      }
    };
    return ObjectExtensions.<Pane>operator_doubleArrow(pane, _function);
  }
  
  protected Pane addComparisonRight(final Pane pane, final String right, final int size, final int y) {
    final Procedure1<Pane> _function = new Procedure1<Pane>() {
      public void apply(final Pane it) {
        ObservableList<Node> _children = it.getChildren();
        Text _createText = DemoCampSlideFactory.createText(right, size);
        final Procedure1<Text> _function = new Procedure1<Text>() {
          public void apply(final Text it) {
            it.setLayoutX(612);
            it.setLayoutY(y);
            it.setTextAlignment(TextAlignment.LEFT);
            it.setTextOrigin(VPos.TOP);
          }
        };
        Text _doubleArrow = ObjectExtensions.<Text>operator_doubleArrow(_createText, _function);
        _children.add(_doubleArrow);
      }
    };
    return ObjectExtensions.<Pane>operator_doubleArrow(pane, _function);
  }
  
  public void populate(final ModelElementImpl modelElement) {
    super.populate(modelElement);
  }
}
