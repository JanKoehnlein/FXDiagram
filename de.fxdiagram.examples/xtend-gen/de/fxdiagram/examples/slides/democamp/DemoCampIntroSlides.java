package de.fxdiagram.examples.slides.democamp;

import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.model.ModelElement;
import de.fxdiagram.core.model.ModelLoad;
import de.fxdiagram.core.services.ImageCache;
import de.fxdiagram.examples.slides.Animations;
import de.fxdiagram.examples.slides.Slide;
import de.fxdiagram.examples.slides.SlideDiagram;
import de.fxdiagram.examples.slides.democamp.DemoCampSlideFactory;
import de.fxdiagram.lib.simple.OpenableDiagramNode;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@ModelNode({ "layoutX", "layoutY", "domainObject", "width", "height" })
@SuppressWarnings("all")
public class DemoCampIntroSlides extends OpenableDiagramNode {
  public DemoCampIntroSlides() {
    super("Summary");
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
                    Text _createText_1 = DemoCampSlideFactory.createText("The Next Generation", 64);
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
        Slide _createSlide_1 = DemoCampSlideFactory.createSlide("Jungle images");
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
                    Text _createText = DemoCampSlideFactory.createText("GEF", 64);
                    final Procedure1<Text> _function = new Procedure1<Text>() {
                      public void apply(final Text it) {
                        it.setRotate(16);
                        it.setLayoutX(180);
                        it.setLayoutY(665);
                        Color _textColor = DemoCampSlideFactory.getTextColor();
                        Color _darkTextColor = DemoCampSlideFactory.getDarkTextColor();
                        Animations.flicker(it, _textColor, _darkTextColor);
                      }
                    };
                    Text _doubleArrow = ObjectExtensions.<Text>operator_doubleArrow(_createText, _function);
                    _children.add(_doubleArrow);
                    ObservableList<Node> _children_1 = it.getChildren();
                    Text _createText_1 = DemoCampSlideFactory.createText("Draw2D", 64);
                    final Procedure1<Text> _function_1 = new Procedure1<Text>() {
                      public void apply(final Text it) {
                        it.setRotate(338);
                        it.setLayoutX(480);
                        it.setLayoutY(232);
                        Animations.crawl(it);
                      }
                    };
                    Text _doubleArrow_1 = ObjectExtensions.<Text>operator_doubleArrow(_createText_1, _function_1);
                    _children_1.add(_doubleArrow_1);
                    ObservableList<Node> _children_2 = it.getChildren();
                    Text _createText_2 = DemoCampSlideFactory.createText("GMF RT", 64);
                    final Procedure1<Text> _function_2 = new Procedure1<Text>() {
                      public void apply(final Text it) {
                        it.setRotate(10);
                        it.setLayoutX(700);
                        it.setLayoutY(300);
                        Animations.crawl(it);
                      }
                    };
                    Text _doubleArrow_2 = ObjectExtensions.<Text>operator_doubleArrow(_createText_2, _function_2);
                    _children_2.add(_doubleArrow_2);
                    ObservableList<Node> _children_3 = it.getChildren();
                    Text _createText_3 = DemoCampSlideFactory.createText("GMF Tooling", 64);
                    final Procedure1<Text> _function_3 = new Procedure1<Text>() {
                      public void apply(final Text it) {
                        it.setRotate(332);
                        it.setLayoutX(740);
                        it.setLayoutY(620);
                        Color _textColor = DemoCampSlideFactory.getTextColor();
                        Color _darkTextColor = DemoCampSlideFactory.getDarkTextColor();
                        Animations.breathe(it, _textColor, _darkTextColor);
                      }
                    };
                    Text _doubleArrow_3 = ObjectExtensions.<Text>operator_doubleArrow(_createText_3, _function_3);
                    _children_3.add(_doubleArrow_3);
                    ObservableList<Node> _children_4 = it.getChildren();
                    Text _createText_4 = DemoCampSlideFactory.createText("Graphiti", 64);
                    final Procedure1<Text> _function_4 = new Procedure1<Text>() {
                      public void apply(final Text it) {
                        it.setLayoutX(211);
                        it.setLayoutY(167);
                        Animations.dangle(it);
                      }
                    };
                    Text _doubleArrow_4 = ObjectExtensions.<Text>operator_doubleArrow(_createText_4, _function_4);
                    _children_4.add(_doubleArrow_4);
                    ObservableList<Node> _children_5 = it.getChildren();
                    Text _createText_5 = DemoCampSlideFactory.createText("Sirius", 64);
                    final Procedure1<Text> _function_5 = new Procedure1<Text>() {
                      public void apply(final Text it) {
                        it.setRotate(5);
                        it.setLayoutX(290);
                        it.setLayoutY(480);
                        Color _textColor = DemoCampSlideFactory.getTextColor();
                        Color _darkTextColor = DemoCampSlideFactory.getDarkTextColor();
                        Animations.breathe(it, _textColor, _darkTextColor);
                      }
                    };
                    Text _doubleArrow_5 = ObjectExtensions.<Text>operator_doubleArrow(_createText_5, _function_5);
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
        Slide _doubleArrow_1 = ObjectExtensions.<Slide>operator_doubleArrow(_createSlide_1, _function_1);
        _slides_1.add(_doubleArrow_1);
        ObservableList<Slide> _slides_2 = it.getSlides();
        Slide _createSlide_2 = DemoCampSlideFactory.createSlide("Title");
        final Procedure1<Slide> _function_2 = new Procedure1<Slide>() {
          public void apply(final Slide it) {
            StackPane _stackPane = it.getStackPane();
            final Procedure1<StackPane> _function = new Procedure1<StackPane>() {
              public void apply(final StackPane it) {
                ObservableList<Node> _children = it.getChildren();
                VBox _vBox = new VBox();
                final Procedure1<VBox> _function = new Procedure1<VBox>() {
                  public void apply(final VBox it) {
                    it.setAlignment(Pos.CENTER);
                    ObservableList<Node> _children = it.getChildren();
                    Text _createText = DemoCampSlideFactory.createText("Frustration", 100);
                    _children.add(_createText);
                  }
                };
                VBox _doubleArrow = ObjectExtensions.<VBox>operator_doubleArrow(_vBox, _function);
                _children.add(_doubleArrow);
              }
            };
            ObjectExtensions.<StackPane>operator_doubleArrow(_stackPane, _function);
          }
        };
        Slide _doubleArrow_2 = ObjectExtensions.<Slide>operator_doubleArrow(_createSlide_2, _function_2);
        _slides_2.add(_doubleArrow_2);
        ObservableList<Slide> _slides_3 = it.getSlides();
        Slide _createSlide_3 = DemoCampSlideFactory.createSlide("JavaFX");
        final Procedure1<Slide> _function_3 = new Procedure1<Slide>() {
          public void apply(final Slide it) {
            StackPane _stackPane = it.getStackPane();
            final Procedure1<StackPane> _function = new Procedure1<StackPane>() {
              public void apply(final StackPane it) {
                ObservableList<Node> _children = it.getChildren();
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
            ObjectExtensions.<StackPane>operator_doubleArrow(_stackPane, _function);
          }
        };
        Slide _doubleArrow_3 = ObjectExtensions.<Slide>operator_doubleArrow(_createSlide_3, _function_3);
        _slides_3.add(_doubleArrow_3);
      }
    };
    SlideDiagram _doubleArrow = ObjectExtensions.<SlideDiagram>operator_doubleArrow(_slideDiagram, _function);
    this.setInnerDiagram(_doubleArrow);
    super.doActivate();
  }
  
  /**
   * Automatically generated by @ModelNode. Used in model deserialization.
   */
  public DemoCampIntroSlides(final ModelLoad modelLoad) {
    super(modelLoad);
  }
  
  public void populate(final ModelElement modelElement) {
    modelElement.addProperty(layoutXProperty(), Double.class);
    modelElement.addProperty(layoutYProperty(), Double.class);
    modelElement.addProperty(widthProperty(), Double.class);
    modelElement.addProperty(heightProperty(), Double.class);
  }
}
