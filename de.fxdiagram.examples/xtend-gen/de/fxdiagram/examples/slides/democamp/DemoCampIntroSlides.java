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
  
  @Override
  public void doActivate() {
    SlideDiagram _slideDiagram = new SlideDiagram();
    final Procedure1<SlideDiagram> _function = (SlideDiagram it) -> {
      ObservableList<Slide> _slides = it.getSlides();
      Slide _createSlide = DemoCampSlideFactory.createSlide("Title");
      final Procedure1<Slide> _function_1 = (Slide it_1) -> {
        final VBox vbox = new VBox();
        StackPane _stackPane = it_1.getStackPane();
        final Procedure1<StackPane> _function_2 = (StackPane it_2) -> {
          ObservableList<Node> _children = it_2.getChildren();
          final Procedure1<VBox> _function_3 = (VBox it_3) -> {
            it_3.setAlignment(Pos.CENTER);
            Insets _insets = new Insets(350, 0, 0, 0);
            StackPane.setMargin(it_3, _insets);
            ObservableList<Node> _children_1 = it_3.getChildren();
            Text _createText = DemoCampSlideFactory.createText("Eclipse Diagram Editors", 120);
            _children_1.add(_createText);
            ObservableList<Node> _children_2 = it_3.getChildren();
            Text _createText_1 = DemoCampSlideFactory.createText("The FXed Generation", 72);
            _children_2.add(_createText_1);
            ObservableList<Node> _children_3 = it_3.getChildren();
            Text _createText_2 = DemoCampSlideFactory.createText("Jan Koehnlein - itemis", 32);
            final Procedure1<Text> _function_4 = (Text it_4) -> {
              Insets _insets_1 = new Insets(30, 0, 0, 0);
              VBox.setMargin(it_4, _insets_1);
            };
            Text _doubleArrow = ObjectExtensions.<Text>operator_doubleArrow(_createText_2, _function_4);
            _children_3.add(_doubleArrow);
          };
          VBox _doubleArrow = ObjectExtensions.<VBox>operator_doubleArrow(vbox, _function_3);
          _children.add(_doubleArrow);
        };
        ObjectExtensions.<StackPane>operator_doubleArrow(_stackPane, _function_2);
      };
      Slide _doubleArrow = ObjectExtensions.<Slide>operator_doubleArrow(_createSlide, _function_1);
      _slides.add(_doubleArrow);
      ObservableList<Slide> _slides_1 = it.getSlides();
      Slide _createSlide_1 = DemoCampSlideFactory.createSlide("Frameworks");
      final Procedure1<Slide> _function_2 = (Slide slide) -> {
        StackPane _stackPane = slide.getStackPane();
        final Procedure1<StackPane> _function_3 = (StackPane it_1) -> {
          ObservableList<Node> _children = it_1.getChildren();
          Pane _pane = new Pane();
          final Procedure1<Pane> _function_4 = (Pane it_2) -> {
            Rectangle _rectangle = new Rectangle(0, 0, 1024, 768);
            it_2.setClip(_rectangle);
            ObservableList<Node> _children_1 = it_2.getChildren();
            Group _group = new Group();
            final Procedure1<Group> _function_5 = (Group it_3) -> {
              it_3.setLayoutX(501);
              it_3.setLayoutY(367);
              ObservableList<Node> _children_2 = it_3.getChildren();
              Group _group_1 = new Group();
              final Procedure1<Group> _function_6 = (Group it_4) -> {
                it_4.setRotate(60);
                ObservableList<Node> _children_3 = it_4.getChildren();
                Text _createText = DemoCampSlideFactory.createText("GMF", 50);
                final Procedure1<Text> _function_7 = (Text it_5) -> {
                  Duration _seconds = DurationExtensions.seconds(10);
                  Animations.orbit(it_5, 110, 100, _seconds, 0);
                };
                Text _doubleArrow_1 = ObjectExtensions.<Text>operator_doubleArrow(_createText, _function_7);
                _children_3.add(_doubleArrow_1);
              };
              Group _doubleArrow_1 = ObjectExtensions.<Group>operator_doubleArrow(_group_1, _function_6);
              _children_2.add(_doubleArrow_1);
            };
            Group _doubleArrow_1 = ObjectExtensions.<Group>operator_doubleArrow(_group, _function_5);
            _children_1.add(_doubleArrow_1);
            ObservableList<Node> _children_2 = it_2.getChildren();
            Group _group_1 = new Group();
            final Procedure1<Group> _function_6 = (Group it_3) -> {
              it_3.setRotate(30);
              it_3.setLayoutX(501);
              it_3.setLayoutY(367);
              ObservableList<Node> _children_3 = it_3.getChildren();
              Text _createText = DemoCampSlideFactory.createText("Graphiti", 50);
              final Procedure1<Text> _function_7 = (Text it_4) -> {
                Duration _seconds = DurationExtensions.seconds(30);
                Animations.orbit(it_4, 400, 250, _seconds, 0);
              };
              Text _doubleArrow_2 = ObjectExtensions.<Text>operator_doubleArrow(_createText, _function_7);
              _children_3.add(_doubleArrow_2);
            };
            Group _doubleArrow_2 = ObjectExtensions.<Group>operator_doubleArrow(_group_1, _function_6);
            _children_2.add(_doubleArrow_2);
            ObservableList<Node> _children_3 = it_2.getChildren();
            Group _group_2 = new Group();
            final Procedure1<Group> _function_7 = (Group it_3) -> {
              it_3.setLayoutX(501);
              it_3.setLayoutY(367);
              it_3.setRotate((-15));
              ObservableList<Node> _children_4 = it_3.getChildren();
              Text _createText = DemoCampSlideFactory.createText("Sirius", 50);
              final Procedure1<Text> _function_8 = (Text it_4) -> {
                Duration _seconds = DurationExtensions.seconds(20);
                Animations.orbit(it_4, 300, 200, _seconds, 0);
              };
              Text _doubleArrow_3 = ObjectExtensions.<Text>operator_doubleArrow(_createText, _function_8);
              _children_4.add(_doubleArrow_3);
            };
            Group _doubleArrow_3 = ObjectExtensions.<Group>operator_doubleArrow(_group_2, _function_7);
            _children_3.add(_doubleArrow_3);
            ObservableList<Node> _children_4 = it_2.getChildren();
            Text _createText = DemoCampSlideFactory.createText("GEF", 92);
            final Procedure1<Text> _function_8 = (Text it_3) -> {
              it_3.setRotate(5);
              it_3.setLayoutX(490);
              it_3.setLayoutY(380);
              Animations.spin(it_3);
            };
            Text _doubleArrow_4 = ObjectExtensions.<Text>operator_doubleArrow(_createText, _function_8);
            _children_4.add(_doubleArrow_4);
          };
          Pane _doubleArrow_1 = ObjectExtensions.<Pane>operator_doubleArrow(_pane, _function_4);
          _children.add(_doubleArrow_1);
        };
        ObjectExtensions.<StackPane>operator_doubleArrow(_stackPane, _function_3);
      };
      Slide _doubleArrow_1 = ObjectExtensions.<Slide>operator_doubleArrow(_createSlide_1, _function_2);
      _slides_1.add(_doubleArrow_1);
      ObservableList<Slide> _slides_2 = it.getSlides();
      ClickThroughSlide _createClickThroughSlide = DemoCampSlideFactory.createClickThroughSlide("Title");
      final Procedure1<ClickThroughSlide> _function_3 = (ClickThroughSlide it_1) -> {
        Pane _pane = it_1.getPane();
        final Procedure1<Pane> _function_4 = (Pane it_2) -> {
          this.addComparisonLeft(it_2, "Framework", 72, 100);
          this.addComparisonLeft(it_2, "Designed For Developers", 48, 250);
          this.addComparisonLeft(it_2, "High-level Abstractions", 48, 310);
          this.addComparisonLeft(it_2, "Max Features w/ Min Effort", 48, 370);
          this.addComparisonLeft(it_2, "Unified Usecase", 48, 430);
          this.addComparisonLeft(it_2, "Standard Behavior", 48, 490);
          this.addComparisonLeft(it_2, "Rendering Issues", 48, 550);
          this.addComparisonRight(it_2, "Product", 72, 100);
          this.addComparisonRight(it_2, "Designed For Users", 48, 250);
          this.addComparisonRight(it_2, "Custom Solution", 48, 310);
          this.addComparisonRight(it_2, "Usability", 48, 370);
          this.addComparisonRight(it_2, "Specific Usecase", 48, 430);
          this.addComparisonRight(it_2, "Fun to Use", 48, 490);
          this.addComparisonRight(it_2, "Stunning Visuals", 48, 550);
        };
        ObjectExtensions.<Pane>operator_doubleArrow(_pane, _function_4);
      };
      ClickThroughSlide _doubleArrow_2 = ObjectExtensions.<ClickThroughSlide>operator_doubleArrow(_createClickThroughSlide, _function_3);
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
      final Procedure1<Slide> _function_4 = (Slide it_1) -> {
        StackPane _stackPane = it_1.getStackPane();
        ObservableList<Node> _children = _stackPane.getChildren();
        ImageView _imageView = new ImageView();
        final Procedure1<ImageView> _function_5 = (ImageView it_2) -> {
          ImageCache _get = ImageCache.get();
          Image _image = _get.getImage(this, "images/javafx.png");
          it_2.setImage(_image);
          it_2.setFitWidth(587);
          it_2.setPreserveRatio(true);
        };
        ImageView _doubleArrow_3 = ObjectExtensions.<ImageView>operator_doubleArrow(_imageView, _function_5);
        _children.add(_doubleArrow_3);
      };
      Slide _doubleArrow_3 = ObjectExtensions.<Slide>operator_doubleArrow(_createSlide_2, _function_4);
      _slides_5.add(_doubleArrow_3);
      ObservableList<Slide> _slides_6 = it.getSlides();
      Slide _createSlide_3 = DemoCampSlideFactory.createSlide("Xtend");
      final Procedure1<Slide> _function_5 = (Slide it_1) -> {
        StackPane _stackPane = it_1.getStackPane();
        ObservableList<Node> _children = _stackPane.getChildren();
        ImageView _imageView = new ImageView();
        final Procedure1<ImageView> _function_6 = (ImageView it_2) -> {
          ImageCache _get = ImageCache.get();
          Image _image = _get.getImage(this, "images/xtend.png");
          it_2.setImage(_image);
        };
        ImageView _doubleArrow_4 = ObjectExtensions.<ImageView>operator_doubleArrow(_imageView, _function_6);
        _children.add(_doubleArrow_4);
      };
      Slide _doubleArrow_4 = ObjectExtensions.<Slide>operator_doubleArrow(_createSlide_3, _function_5);
      _slides_6.add(_doubleArrow_4);
    };
    SlideDiagram _doubleArrow = ObjectExtensions.<SlideDiagram>operator_doubleArrow(_slideDiagram, _function);
    this.setInnerDiagram(_doubleArrow);
    super.doActivate();
  }
  
  protected Pane addComparisonLeft(final Pane pane, final String left, final int size, final int y) {
    final Procedure1<Pane> _function = (Pane it) -> {
      ObservableList<Node> _children = it.getChildren();
      Text _createText = DemoCampSlideFactory.createText(left, size);
      final Procedure1<Text> _function_1 = (Text it_1) -> {
        it_1.setLayoutX(100);
        it_1.setLayoutY(y);
        it_1.setTextAlignment(TextAlignment.LEFT);
        it_1.setTextOrigin(VPos.TOP);
        Color _darkTextColor = DemoCampSlideFactory.getDarkTextColor();
        it_1.setFill(_darkTextColor);
      };
      Text _doubleArrow = ObjectExtensions.<Text>operator_doubleArrow(_createText, _function_1);
      _children.add(_doubleArrow);
    };
    return ObjectExtensions.<Pane>operator_doubleArrow(pane, _function);
  }
  
  protected Pane addComparisonRight(final Pane pane, final String right, final int size, final int y) {
    final Procedure1<Pane> _function = (Pane it) -> {
      ObservableList<Node> _children = it.getChildren();
      Text _createText = DemoCampSlideFactory.createText(right, size);
      final Procedure1<Text> _function_1 = (Text it_1) -> {
        it_1.setLayoutX(612);
        it_1.setLayoutY(y);
        it_1.setTextAlignment(TextAlignment.LEFT);
        it_1.setTextOrigin(VPos.TOP);
      };
      Text _doubleArrow = ObjectExtensions.<Text>operator_doubleArrow(_createText, _function_1);
      _children.add(_doubleArrow);
    };
    return ObjectExtensions.<Pane>operator_doubleArrow(pane, _function);
  }
  
  public void populate(final ModelElementImpl modelElement) {
    super.populate(modelElement);
  }
}
