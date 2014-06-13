package de.fxdiagram.examples.slides.democamp;

import de.fxdiagram.annotations.properties.ModelNode;
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
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure0;
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
                    Text _createText = DemoCampSlideFactory.createText("Eclipse Diagram Editors", 120);
                    _children.add(_createText);
                    ObservableList<Node> _children_1 = it.getChildren();
                    Text _createText_1 = DemoCampSlideFactory.createText("The FXed Generation", 64);
                    _children_1.add(_createText_1);
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
                    Text _createText = DemoCampSlideFactory.createText("Draw2D", 64);
                    final Procedure1<Text> _function = new Procedure1<Text>() {
                      public void apply(final Text it) {
                        it.setRotate((-16));
                        it.setLayoutX(110);
                        it.setLayoutY(145);
                        Color _textColor = DemoCampSlideFactory.getTextColor();
                        Color _darkTextColor = DemoCampSlideFactory.getDarkTextColor();
                        Animations.flicker(it, _textColor, _darkTextColor);
                      }
                    };
                    Text _doubleArrow = ObjectExtensions.<Text>operator_doubleArrow(_createText, _function);
                    _children.add(_doubleArrow);
                    ObservableList<Node> _children_1 = it.getChildren();
                    Text _createText_1 = DemoCampSlideFactory.createText("Graphiti", 72);
                    final Procedure1<Text> _function_1 = new Procedure1<Text>() {
                      public void apply(final Text it) {
                        it.setRotate(5);
                        it.setLayoutX(120);
                        it.setLayoutY(652);
                        final Procedure0 _function = new Procedure0() {
                          public void apply() {
                            Animations.warp(it, 1000);
                          }
                        };
                        slide.setOnActivate(_function);
                      }
                    };
                    Text _doubleArrow_1 = ObjectExtensions.<Text>operator_doubleArrow(_createText_1, _function_1);
                    _children_1.add(_doubleArrow_1);
                    ObservableList<Node> _children_2 = it.getChildren();
                    Text _createText_2 = DemoCampSlideFactory.createText("GMF", 72);
                    final Procedure1<Text> _function_2 = new Procedure1<Text>() {
                      public void apply(final Text it) {
                        it.setRotate(30);
                        it.setLayoutX(840);
                        it.setLayoutY(180);
                        Color _textColor = DemoCampSlideFactory.getTextColor();
                        Color _darkTextColor = DemoCampSlideFactory.getDarkTextColor();
                        Animations.breathe(it, _textColor, _darkTextColor);
                      }
                    };
                    Text _doubleArrow_2 = ObjectExtensions.<Text>operator_doubleArrow(_createText_2, _function_2);
                    _children_2.add(_doubleArrow_2);
                    ObservableList<Node> _children_3 = it.getChildren();
                    Group _group = new Group();
                    final Procedure1<Group> _function_3 = new Procedure1<Group>() {
                      public void apply(final Group it) {
                        it.setLayoutX(501);
                        it.setLayoutY(367);
                        it.setRotate((-15));
                        ObservableList<Node> _children = it.getChildren();
                        Text _createText = DemoCampSlideFactory.createText("Sirius", 72);
                        final Procedure1<Text> _function = new Procedure1<Text>() {
                          public void apply(final Text it) {
                            Animations.orbit(it, 370, 150);
                          }
                        };
                        Text _doubleArrow = ObjectExtensions.<Text>operator_doubleArrow(_createText, _function);
                        _children.add(_doubleArrow);
                      }
                    };
                    Group _doubleArrow_3 = ObjectExtensions.<Group>operator_doubleArrow(_group, _function_3);
                    _children_3.add(_doubleArrow_3);
                    ObservableList<Node> _children_4 = it.getChildren();
                    Text _createText_3 = DemoCampSlideFactory.createText("GEF", 92);
                    final Procedure1<Text> _function_4 = new Procedure1<Text>() {
                      public void apply(final Text it) {
                        it.setRotate(5);
                        it.setLayoutX(490);
                        it.setLayoutY(380);
                        Animations.spin(it);
                      }
                    };
                    Text _doubleArrow_4 = ObjectExtensions.<Text>operator_doubleArrow(_createText_3, _function_4);
                    _children_4.add(_doubleArrow_4);
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
                DemoCampIntroSlides.this.addComparison(it, "Framework", "Product", 72, 100);
                DemoCampIntroSlides.this.addComparison(it, "Designed For Developers", "Designed For Users", 48, 250);
                DemoCampIntroSlides.this.addComparison(it, "High-level Abstractions", "Customizability", 48, 325);
                DemoCampIntroSlides.this.addComparison(it, "Max Features w/ Min Effort", "Embrace All Usecases", 48, 400);
                DemoCampIntroSlides.this.addComparison(it, "Pragmatic Behavior", "Fun to Use", 48, 475);
                DemoCampIntroSlides.this.addComparison(it, "Simplify Rendering", "Appealing Visuals", 48, 550);
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
        _builder_1.append("is not an option.");
        _builder_1.newLine();
        Slide _createSlideWithText_1 = DemoCampSlideFactory.createSlideWithText("Problem", _builder_1.toString(), 72);
        _slides_4.add(_createSlideWithText_1);
        ObservableList<Slide> _slides_5 = it.getSlides();
        ClickThroughSlide _createClickThroughSlide_1 = DemoCampSlideFactory.createClickThroughSlide("JavaFX");
        final Procedure1<ClickThroughSlide> _function_3 = new Procedure1<ClickThroughSlide>() {
          public void apply(final ClickThroughSlide it) {
            Pane _pane = it.getPane();
            final Procedure1<Pane> _function = new Procedure1<Pane>() {
              public void apply(final Pane it) {
                ObservableList<Node> _children = it.getChildren();
                ImageView _imageView = new ImageView();
                final Procedure1<ImageView> _function = new Procedure1<ImageView>() {
                  public void apply(final ImageView it) {
                    ImageCache _get = ImageCache.get();
                    Image _image = _get.getImage(DemoCampIntroSlides.this, "images/javafx.png");
                    it.setImage(_image);
                    it.setFitWidth(587);
                    it.setPreserveRatio(true);
                    it.setLayoutX(120);
                    it.setLayoutY(140);
                  }
                };
                ImageView _doubleArrow = ObjectExtensions.<ImageView>operator_doubleArrow(_imageView, _function);
                _children.add(_doubleArrow);
                ObservableList<Node> _children_1 = it.getChildren();
                ImageView _imageView_1 = new ImageView();
                final Procedure1<ImageView> _function_1 = new Procedure1<ImageView>() {
                  public void apply(final ImageView it) {
                    ImageCache _get = ImageCache.get();
                    Image _image = _get.getImage(DemoCampIntroSlides.this, "images/xtend.png");
                    it.setImage(_image);
                    it.setLayoutX(520);
                    it.setLayoutY(440);
                  }
                };
                ImageView _doubleArrow_1 = ObjectExtensions.<ImageView>operator_doubleArrow(_imageView_1, _function_1);
                _children_1.add(_doubleArrow_1);
              }
            };
            ObjectExtensions.<Pane>operator_doubleArrow(_pane, _function);
          }
        };
        ClickThroughSlide _doubleArrow_3 = ObjectExtensions.<ClickThroughSlide>operator_doubleArrow(_createClickThroughSlide_1, _function_3);
        _slides_5.add(_doubleArrow_3);
      }
    };
    SlideDiagram _doubleArrow = ObjectExtensions.<SlideDiagram>operator_doubleArrow(_slideDiagram, _function);
    this.setInnerDiagram(_doubleArrow);
    super.doActivate();
  }
  
  protected Pane addComparison(final Pane pane, final String left, final String right, final int size, final int y) {
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
        ObservableList<Node> _children_1 = it.getChildren();
        Text _createText_1 = DemoCampSlideFactory.createText(right, size);
        final Procedure1<Text> _function_1 = new Procedure1<Text>() {
          public void apply(final Text it) {
            it.setLayoutX(612);
            it.setLayoutY(y);
            it.setTextAlignment(TextAlignment.LEFT);
            it.setTextOrigin(VPos.TOP);
          }
        };
        Text _doubleArrow_1 = ObjectExtensions.<Text>operator_doubleArrow(_createText_1, _function_1);
        _children_1.add(_doubleArrow_1);
      }
    };
    return ObjectExtensions.<Pane>operator_doubleArrow(pane, _function);
  }
  
  public void populate(final ModelElementImpl modelElement) {
    super.populate(modelElement);
  }
}
