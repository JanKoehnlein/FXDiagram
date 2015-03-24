package de.fxdiagram.examples.slides.eclipsecon;

import com.google.common.collect.Iterables;
import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.model.ModelElementImpl;
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
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@ModelNode
@SuppressWarnings("all")
public class IntroductionSlideDeck extends OpenableDiagramNode {
  public IntroductionSlideDeck() {
    super("Introduction");
  }
  
  @Override
  public void doActivate() {
    SlideDiagram _slideDiagram = new SlideDiagram();
    final Procedure1<SlideDiagram> _function = (SlideDiagram it) -> {
      ObservableList<Slide> _slides = it.getSlides();
      Slide _createSlide = EclipseConSlideFactory.createSlide("Title");
      final Procedure1<Slide> _function_1 = (Slide it_1) -> {
        StackPane _stackPane = it_1.getStackPane();
        final Procedure1<StackPane> _function_2 = (StackPane it_2) -> {
          ObservableList<Node> _children = it_2.getChildren();
          VBox _vBox = new VBox();
          final Procedure1<VBox> _function_3 = (VBox it_3) -> {
            it_3.setAlignment(Pos.CENTER);
            Insets _insets = new Insets(200, 0, 0, 0);
            StackPane.setMargin(it_3, _insets);
            ObservableList<Node> _children_1 = it_3.getChildren();
            Text _createText = EclipseConSlideFactory.createText("Eclipse Discovery Channel", 36);
            final Procedure1<Text> _function_4 = (Text it_4) -> {
              Color _jungleDarkGreen = EclipseConSlideFactory.jungleDarkGreen();
              it_4.setFill(_jungleDarkGreen);
            };
            Text _doubleArrow = ObjectExtensions.<Text>operator_doubleArrow(_createText, _function_4);
            _children_1.add(_doubleArrow);
            ObservableList<Node> _children_2 = it_3.getChildren();
            Text _createText_1 = EclipseConSlideFactory.createText("presents", 30);
            final Procedure1<Text> _function_5 = (Text it_4) -> {
              Color _jungleDarkGreen = EclipseConSlideFactory.jungleDarkGreen();
              it_4.setFill(_jungleDarkGreen);
            };
            Text _doubleArrow_1 = ObjectExtensions.<Text>operator_doubleArrow(_createText_1, _function_5);
            _children_2.add(_doubleArrow_1);
            ObservableList<Node> _children_3 = it_3.getChildren();
            Text _createText_2 = EclipseConSlideFactory.createText("Eclipse Diagram Editors", 93);
            final Procedure1<Text> _function_6 = (Text it_4) -> {
              Color _rgb = Color.rgb(238, 191, 171);
              it_4.setFill(_rgb);
            };
            Text _doubleArrow_2 = ObjectExtensions.<Text>operator_doubleArrow(_createText_2, _function_6);
            _children_3.add(_doubleArrow_2);
            ObservableList<Node> _children_4 = it_3.getChildren();
            Text _createText_3 = EclipseConSlideFactory.createText("An Endangered Species", 48);
            final Procedure1<Text> _function_7 = (Text it_4) -> {
            };
            Text _doubleArrow_3 = ObjectExtensions.<Text>operator_doubleArrow(_createText_3, _function_7);
            _children_4.add(_doubleArrow_3);
          };
          VBox _doubleArrow = ObjectExtensions.<VBox>operator_doubleArrow(_vBox, _function_3);
          _children.add(_doubleArrow);
        };
        ObjectExtensions.<StackPane>operator_doubleArrow(_stackPane, _function_2);
      };
      Slide _doubleArrow = ObjectExtensions.<Slide>operator_doubleArrow(_createSlide, _function_1);
      _slides.add(_doubleArrow);
      ObservableList<Slide> _slides_1 = it.getSlides();
      Slide _createSlide_1 = EclipseConSlideFactory.createSlide("The Eclipse Jungle", 110);
      _slides_1.add(_createSlide_1);
      ObservableList<Slide> _slides_2 = it.getSlides();
      Slide _createSlide_2 = EclipseConSlideFactory.createSlide("Jungle images");
      final Procedure1<Slide> _function_2 = (Slide it_1) -> {
        StackPane _stackPane = it_1.getStackPane();
        final Procedure1<StackPane> _function_3 = (StackPane it_2) -> {
          ObservableList<Node> _children = it_2.getChildren();
          Pane _pane = new Pane();
          final Procedure1<Pane> _function_4 = (Pane it_3) -> {
            it_3.setPrefSize(1024, 768);
            ObservableList<Node> _children_1 = it_3.getChildren();
            Text _createJungleText = EclipseConSlideFactory.createJungleText("GEF", 48);
            final Procedure1<Text> _function_5 = (Text it_4) -> {
              Color _jungleDarkGreen = EclipseConSlideFactory.jungleDarkGreen();
              it_4.setFill(_jungleDarkGreen);
              it_4.setRotate(16);
              it_4.setLayoutX(80);
              it_4.setLayoutY(665);
              Color _jungleDarkGreen_1 = EclipseConSlideFactory.jungleDarkGreen();
              Color _jungleDarkestGreen = EclipseConSlideFactory.jungleDarkestGreen();
              Animations.flicker(it_4, _jungleDarkGreen_1, _jungleDarkestGreen);
            };
            Text _doubleArrow_1 = ObjectExtensions.<Text>operator_doubleArrow(_createJungleText, _function_5);
            _children_1.add(_doubleArrow_1);
            ObservableList<Node> _children_2 = it_3.getChildren();
            Text _createJungleText_1 = EclipseConSlideFactory.createJungleText("Draw2D", 48);
            final Procedure1<Text> _function_6 = (Text it_4) -> {
              Color _jungleDarkGreen = EclipseConSlideFactory.jungleDarkGreen();
              it_4.setFill(_jungleDarkGreen);
              it_4.setRotate(338);
              it_4.setLayoutX(380);
              it_4.setLayoutY(132);
              Animations.crawl(it_4);
            };
            Text _doubleArrow_2 = ObjectExtensions.<Text>operator_doubleArrow(_createJungleText_1, _function_6);
            _children_2.add(_doubleArrow_2);
            ObservableList<Node> _children_3 = it_3.getChildren();
            Text _createJungleText_2 = EclipseConSlideFactory.createJungleText("GMF RT", 48);
            final Procedure1<Text> _function_7 = (Text it_4) -> {
              Color _jungleDarkGreen = EclipseConSlideFactory.jungleDarkGreen();
              it_4.setFill(_jungleDarkGreen);
              it_4.setRotate(10);
              it_4.setLayoutX(560);
              it_4.setLayoutY(300);
              Animations.crawl(it_4);
            };
            Text _doubleArrow_3 = ObjectExtensions.<Text>operator_doubleArrow(_createJungleText_2, _function_7);
            _children_3.add(_doubleArrow_3);
            ObservableList<Node> _children_4 = it_3.getChildren();
            Text _createJungleText_3 = EclipseConSlideFactory.createJungleText("GMF Tooling", 48);
            final Procedure1<Text> _function_8 = (Text it_4) -> {
              Color _jungleDarkGreen = EclipseConSlideFactory.jungleDarkGreen();
              it_4.setFill(_jungleDarkGreen);
              it_4.setRotate(332);
              it_4.setLayoutX(640);
              it_4.setLayoutY(620);
              Color _jungleDarkGreen_1 = EclipseConSlideFactory.jungleDarkGreen();
              Color _jungleDarkestGreen = EclipseConSlideFactory.jungleDarkestGreen();
              Animations.breathe(it_4, _jungleDarkGreen_1, _jungleDarkestGreen);
            };
            Text _doubleArrow_4 = ObjectExtensions.<Text>operator_doubleArrow(_createJungleText_3, _function_8);
            _children_4.add(_doubleArrow_4);
            ObservableList<Node> _children_5 = it_3.getChildren();
            Text _createJungleText_4 = EclipseConSlideFactory.createJungleText("Graphiti", 48);
            final Procedure1<Text> _function_9 = (Text it_4) -> {
              Color _jungleDarkGreen = EclipseConSlideFactory.jungleDarkGreen();
              it_4.setFill(_jungleDarkGreen);
              it_4.setLayoutX(111);
              it_4.setLayoutY(167);
              Animations.dangle(it_4);
            };
            Text _doubleArrow_5 = ObjectExtensions.<Text>operator_doubleArrow(_createJungleText_4, _function_9);
            _children_5.add(_doubleArrow_5);
            ObservableList<Node> _children_6 = it_3.getChildren();
            Text _createJungleText_5 = EclipseConSlideFactory.createJungleText("Sirius", 48);
            final Procedure1<Text> _function_10 = (Text it_4) -> {
              Color _jungleDarkGreen = EclipseConSlideFactory.jungleDarkGreen();
              it_4.setFill(_jungleDarkGreen);
              it_4.setRotate(5);
              it_4.setLayoutX(190);
              it_4.setLayoutY(480);
              Color _jungleDarkGreen_1 = EclipseConSlideFactory.jungleDarkGreen();
              Color _jungleDarkestGreen = EclipseConSlideFactory.jungleDarkestGreen();
              Animations.breathe(it_4, _jungleDarkGreen_1, _jungleDarkestGreen);
            };
            Text _doubleArrow_6 = ObjectExtensions.<Text>operator_doubleArrow(_createJungleText_5, _function_10);
            _children_6.add(_doubleArrow_6);
          };
          Pane _doubleArrow_1 = ObjectExtensions.<Pane>operator_doubleArrow(_pane, _function_4);
          _children.add(_doubleArrow_1);
        };
        ObjectExtensions.<StackPane>operator_doubleArrow(_stackPane, _function_3);
      };
      Slide _doubleArrow_1 = ObjectExtensions.<Slide>operator_doubleArrow(_createSlide_2, _function_2);
      _slides_2.add(_doubleArrow_1);
      ObservableList<Slide> _slides_3 = it.getSlides();
      Slide _createSlide_3 = EclipseConSlideFactory.createSlide("Appearance", 144);
      _slides_3.add(_createSlide_3);
      ObservableList<Slide> _slides_4 = it.getSlides();
      ClickThroughSlide _createClickThroughSlide = EclipseConSlideFactory.createClickThroughSlide("Darkness images");
      final Procedure1<ClickThroughSlide> _function_3 = (ClickThroughSlide it_1) -> {
        Pane _pane = it_1.getPane();
        ObservableList<Node> _children = _pane.getChildren();
        ImageView _imageView = new ImageView();
        final Procedure1<ImageView> _function_4 = (ImageView it_2) -> {
          ImageCache _get = ImageCache.get();
          Image _image = _get.getImage(this, "images/darkness1.png");
          it_2.setImage(_image);
          it_2.setLayoutX(45);
          it_2.setLayoutY(45);
        };
        ImageView _doubleArrow_2 = ObjectExtensions.<ImageView>operator_doubleArrow(_imageView, _function_4);
        _children.add(_doubleArrow_2);
        Pane _pane_1 = it_1.getPane();
        ObservableList<Node> _children_1 = _pane_1.getChildren();
        ImageView _imageView_1 = new ImageView();
        final Procedure1<ImageView> _function_5 = (ImageView it_2) -> {
          ImageCache _get = ImageCache.get();
          Image _image = _get.getImage(this, "images/darkness2.png");
          it_2.setImage(_image);
          it_2.setLayoutX(420);
          it_2.setLayoutY(374);
        };
        ImageView _doubleArrow_3 = ObjectExtensions.<ImageView>operator_doubleArrow(_imageView_1, _function_5);
        _children_1.add(_doubleArrow_3);
      };
      ClickThroughSlide _doubleArrow_2 = ObjectExtensions.<ClickThroughSlide>operator_doubleArrow(_createClickThroughSlide, _function_3);
      _slides_4.add(_doubleArrow_2);
      ObservableList<Slide> _slides_5 = it.getSlides();
      Slide _createSlide_4 = EclipseConSlideFactory.createSlide("Behavior", 144);
      _slides_5.add(_createSlide_4);
      ObservableList<Slide> _slides_6 = it.getSlides();
      ClickThroughSlide _createClickThroughSlide_1 = EclipseConSlideFactory.createClickThroughSlide("Behavior images");
      final Procedure1<ClickThroughSlide> _function_4 = (ClickThroughSlide it_1) -> {
        Pane _pane = it_1.getPane();
        ObservableList<Node> _children = _pane.getChildren();
        ImageView _imageView = new ImageView();
        final Procedure1<ImageView> _function_5 = (ImageView it_2) -> {
          ImageCache _get = ImageCache.get();
          Image _image = _get.getImage(this, "images/graphiti.png");
          it_2.setImage(_image);
          it_2.setLayoutX(50);
          it_2.setLayoutY(44);
        };
        ImageView _doubleArrow_3 = ObjectExtensions.<ImageView>operator_doubleArrow(_imageView, _function_5);
        _children.add(_doubleArrow_3);
        Pane _pane_1 = it_1.getPane();
        ObservableList<Node> _children_1 = _pane_1.getChildren();
        ImageView _imageView_1 = new ImageView();
        final Procedure1<ImageView> _function_6 = (ImageView it_2) -> {
          ImageCache _get = ImageCache.get();
          Image _image = _get.getImage(this, "images/properties.png");
          it_2.setImage(_image);
          it_2.setLayoutX(295);
          it_2.setLayoutY(332);
        };
        ImageView _doubleArrow_4 = ObjectExtensions.<ImageView>operator_doubleArrow(_imageView_1, _function_6);
        _children_1.add(_doubleArrow_4);
      };
      ClickThroughSlide _doubleArrow_3 = ObjectExtensions.<ClickThroughSlide>operator_doubleArrow(_createClickThroughSlide_1, _function_4);
      _slides_6.add(_doubleArrow_3);
      ObservableList<Slide> _slides_7 = it.getSlides();
      Slide _createSlide_5 = EclipseConSlideFactory.createSlide("Recycling", 144);
      _slides_7.add(_createSlide_5);
      ObservableList<Slide> _slides_8 = it.getSlides();
      ClickThroughSlide _createClickThroughSlide_2 = EclipseConSlideFactory.createClickThroughSlide("Recycling images");
      final Procedure1<ClickThroughSlide> _function_5 = (ClickThroughSlide it_1) -> {
        Pane _pane = it_1.getPane();
        final Procedure1<Pane> _function_6 = (Pane it_2) -> {
          ObservableList<Node> _children = it_2.getChildren();
          ImageView _imageView = new ImageView();
          final Procedure1<ImageView> _function_7 = (ImageView it_3) -> {
            ImageCache _get = ImageCache.get();
            Image _image = _get.getImage(this, "images/onion.png");
            it_3.setImage(_image);
            it_3.setFitWidth(570);
            it_3.setPreserveRatio(true);
            it_3.setLayoutX(227);
            it_3.setLayoutY(110);
          };
          ImageView _doubleArrow_4 = ObjectExtensions.<ImageView>operator_doubleArrow(_imageView, _function_7);
          _children.add(_doubleArrow_4);
          ObservableList<Node> _children_1 = it_2.getChildren();
          Group _group = new Group();
          final Procedure1<Group> _function_8 = (Group it_3) -> {
            ObservableList<Node> _children_2 = it_3.getChildren();
            Text _createJungleText = EclipseConSlideFactory.createJungleText("OS", 48);
            final Procedure1<Text> _function_9 = (Text it_4) -> {
              it_4.setLayoutX(173);
              it_4.setLayoutY(216);
            };
            Text _doubleArrow_5 = ObjectExtensions.<Text>operator_doubleArrow(_createJungleText, _function_9);
            _children_2.add(_doubleArrow_5);
            ObservableList<Node> _children_3 = it_3.getChildren();
            Polyline _polyline = new Polyline();
            final Procedure1<Polyline> _function_10 = (Polyline it_4) -> {
              ObservableList<Double> _points = it_4.getPoints();
              Iterables.<Double>addAll(_points, Collections.<Double>unmodifiableList(CollectionLiterals.<Double>newArrayList(Double.valueOf(244.0), Double.valueOf(226.0), Double.valueOf(537.0), Double.valueOf(356.0))));
              Color _jungleGreen = EclipseConSlideFactory.jungleGreen();
              it_4.setStroke(_jungleGreen);
              it_4.setStrokeWidth(2);
            };
            Polyline _doubleArrow_6 = ObjectExtensions.<Polyline>operator_doubleArrow(_polyline, _function_10);
            _children_3.add(_doubleArrow_6);
          };
          Group _doubleArrow_5 = ObjectExtensions.<Group>operator_doubleArrow(_group, _function_8);
          _children_1.add(_doubleArrow_5);
          ObservableList<Node> _children_2 = it_2.getChildren();
          Group _group_1 = new Group();
          final Procedure1<Group> _function_9 = (Group it_3) -> {
            ObservableList<Node> _children_3 = it_3.getChildren();
            Text _createJungleText = EclipseConSlideFactory.createJungleText("SWT", 48);
            final Procedure1<Text> _function_10 = (Text it_4) -> {
              it_4.setLayoutX(62);
              it_4.setLayoutY(458);
            };
            Text _doubleArrow_6 = ObjectExtensions.<Text>operator_doubleArrow(_createJungleText, _function_10);
            _children_3.add(_doubleArrow_6);
            ObservableList<Node> _children_4 = it_3.getChildren();
            Polyline _polyline = new Polyline();
            final Procedure1<Polyline> _function_11 = (Polyline it_4) -> {
              ObservableList<Double> _points = it_4.getPoints();
              Iterables.<Double>addAll(_points, Collections.<Double>unmodifiableList(CollectionLiterals.<Double>newArrayList(Double.valueOf(176.0), Double.valueOf(439.0), Double.valueOf(501.0), Double.valueOf(367.0))));
              Color _jungleGreen = EclipseConSlideFactory.jungleGreen();
              it_4.setStroke(_jungleGreen);
              it_4.setStrokeWidth(2);
            };
            Polyline _doubleArrow_7 = ObjectExtensions.<Polyline>operator_doubleArrow(_polyline, _function_11);
            _children_4.add(_doubleArrow_7);
          };
          Group _doubleArrow_6 = ObjectExtensions.<Group>operator_doubleArrow(_group_1, _function_9);
          _children_2.add(_doubleArrow_6);
          ObservableList<Node> _children_3 = it_2.getChildren();
          Group _group_2 = new Group();
          final Procedure1<Group> _function_10 = (Group it_3) -> {
            ObservableList<Node> _children_4 = it_3.getChildren();
            Text _createJungleText = EclipseConSlideFactory.createJungleText("Draw2D", 48);
            final Procedure1<Text> _function_11 = (Text it_4) -> {
              it_4.setLayoutX(129);
              it_4.setLayoutY(666);
            };
            Text _doubleArrow_7 = ObjectExtensions.<Text>operator_doubleArrow(_createJungleText, _function_11);
            _children_4.add(_doubleArrow_7);
            ObservableList<Node> _children_5 = it_3.getChildren();
            Polyline _polyline = new Polyline();
            final Procedure1<Polyline> _function_12 = (Polyline it_4) -> {
              ObservableList<Double> _points = it_4.getPoints();
              Iterables.<Double>addAll(_points, Collections.<Double>unmodifiableList(CollectionLiterals.<Double>newArrayList(Double.valueOf(307.0), Double.valueOf(611.0), Double.valueOf(489.0), Double.valueOf(433.0))));
              Color _jungleGreen = EclipseConSlideFactory.jungleGreen();
              it_4.setStroke(_jungleGreen);
              it_4.setStrokeWidth(2);
            };
            Polyline _doubleArrow_8 = ObjectExtensions.<Polyline>operator_doubleArrow(_polyline, _function_12);
            _children_5.add(_doubleArrow_8);
          };
          Group _doubleArrow_7 = ObjectExtensions.<Group>operator_doubleArrow(_group_2, _function_10);
          _children_3.add(_doubleArrow_7);
          ObservableList<Node> _children_4 = it_2.getChildren();
          Group _group_3 = new Group();
          final Procedure1<Group> _function_11 = (Group it_3) -> {
            ObservableList<Node> _children_5 = it_3.getChildren();
            Text _createJungleText = EclipseConSlideFactory.createJungleText("GEF MVC", 48);
            final Procedure1<Text> _function_12 = (Text it_4) -> {
              it_4.setLayoutX(581);
              it_4.setLayoutY(712);
            };
            Text _doubleArrow_8 = ObjectExtensions.<Text>operator_doubleArrow(_createJungleText, _function_12);
            _children_5.add(_doubleArrow_8);
            ObservableList<Node> _children_6 = it_3.getChildren();
            Polyline _polyline = new Polyline();
            final Procedure1<Polyline> _function_13 = (Polyline it_4) -> {
              ObservableList<Double> _points = it_4.getPoints();
              Iterables.<Double>addAll(_points, Collections.<Double>unmodifiableList(CollectionLiterals.<Double>newArrayList(Double.valueOf(714.0), Double.valueOf(662.0), Double.valueOf(588.0), Double.valueOf(458.0))));
              Color _jungleGreen = EclipseConSlideFactory.jungleGreen();
              it_4.setStroke(_jungleGreen);
              it_4.setStrokeWidth(2);
            };
            Polyline _doubleArrow_9 = ObjectExtensions.<Polyline>operator_doubleArrow(_polyline, _function_13);
            _children_6.add(_doubleArrow_9);
          };
          Group _doubleArrow_8 = ObjectExtensions.<Group>operator_doubleArrow(_group_3, _function_11);
          _children_4.add(_doubleArrow_8);
          ObservableList<Node> _children_5 = it_2.getChildren();
          Group _group_4 = new Group();
          final Procedure1<Group> _function_12 = (Group it_3) -> {
            ObservableList<Node> _children_6 = it_3.getChildren();
            StringConcatenation _builder = new StringConcatenation();
            _builder.append("GMF");
            _builder.newLine();
            _builder.append("Runtime");
            Text _createJungleText = EclipseConSlideFactory.createJungleText(_builder.toString(), 48);
            final Procedure1<Text> _function_13 = (Text it_4) -> {
              it_4.setLayoutX(770);
              it_4.setLayoutY(462);
            };
            Text _doubleArrow_9 = ObjectExtensions.<Text>operator_doubleArrow(_createJungleText, _function_13);
            _children_6.add(_doubleArrow_9);
            ObservableList<Node> _children_7 = it_3.getChildren();
            Polyline _polyline = new Polyline();
            final Procedure1<Polyline> _function_14 = (Polyline it_4) -> {
              ObservableList<Double> _points = it_4.getPoints();
              Iterables.<Double>addAll(_points, Collections.<Double>unmodifiableList(CollectionLiterals.<Double>newArrayList(Double.valueOf(803.0), Double.valueOf(462.0), Double.valueOf(658.0), Double.valueOf(416.0))));
              Color _jungleGreen = EclipseConSlideFactory.jungleGreen();
              it_4.setStroke(_jungleGreen);
              it_4.setStrokeWidth(2);
            };
            Polyline _doubleArrow_10 = ObjectExtensions.<Polyline>operator_doubleArrow(_polyline, _function_14);
            _children_7.add(_doubleArrow_10);
          };
          Group _doubleArrow_9 = ObjectExtensions.<Group>operator_doubleArrow(_group_4, _function_12);
          _children_5.add(_doubleArrow_9);
          ObservableList<Node> _children_6 = it_2.getChildren();
          Group _group_5 = new Group();
          final Procedure1<Group> _function_13 = (Group it_3) -> {
            ObservableList<Node> _children_7 = it_3.getChildren();
            StringConcatenation _builder = new StringConcatenation();
            _builder.append("GMF");
            _builder.newLine();
            _builder.append("Tooling");
            Text _createJungleText = EclipseConSlideFactory.createJungleText(_builder.toString(), 48);
            final Procedure1<Text> _function_14 = (Text it_4) -> {
              it_4.setLayoutX(770);
              it_4.setLayoutY(184);
            };
            Text _doubleArrow_10 = ObjectExtensions.<Text>operator_doubleArrow(_createJungleText, _function_14);
            _children_7.add(_doubleArrow_10);
            ObservableList<Node> _children_8 = it_3.getChildren();
            Polyline _polyline = new Polyline();
            final Procedure1<Polyline> _function_15 = (Polyline it_4) -> {
              ObservableList<Double> _points = it_4.getPoints();
              Iterables.<Double>addAll(_points, Collections.<Double>unmodifiableList(CollectionLiterals.<Double>newArrayList(Double.valueOf(766.0), Double.valueOf(206.0), Double.valueOf(662.0), Double.valueOf(281.0))));
              Color _jungleGreen = EclipseConSlideFactory.jungleGreen();
              it_4.setStroke(_jungleGreen);
              it_4.setStrokeWidth(2);
            };
            Polyline _doubleArrow_11 = ObjectExtensions.<Polyline>operator_doubleArrow(_polyline, _function_15);
            _children_8.add(_doubleArrow_11);
          };
          Group _doubleArrow_10 = ObjectExtensions.<Group>operator_doubleArrow(_group_5, _function_13);
          _children_6.add(_doubleArrow_10);
          ObservableList<Node> _children_7 = it_2.getChildren();
          Group _group_6 = new Group();
          final Procedure1<Group> _function_14 = (Group it_3) -> {
            ObservableList<Node> _children_8 = it_3.getChildren();
            Text _createJungleText = EclipseConSlideFactory.createJungleText("...", 48);
            final Procedure1<Text> _function_15 = (Text it_4) -> {
              it_4.setLayoutX(405);
              it_4.setLayoutY(88);
            };
            Text _doubleArrow_11 = ObjectExtensions.<Text>operator_doubleArrow(_createJungleText, _function_15);
            _children_8.add(_doubleArrow_11);
            ObservableList<Node> _children_9 = it_3.getChildren();
            Polyline _polyline = new Polyline();
            final Procedure1<Polyline> _function_16 = (Polyline it_4) -> {
              ObservableList<Double> _points = it_4.getPoints();
              Iterables.<Double>addAll(_points, Collections.<Double>unmodifiableList(CollectionLiterals.<Double>newArrayList(Double.valueOf(519.0), Double.valueOf(101.0), Double.valueOf(525.0), Double.valueOf(188.0))));
              Color _jungleGreen = EclipseConSlideFactory.jungleGreen();
              it_4.setStroke(_jungleGreen);
              it_4.setStrokeWidth(2);
            };
            Polyline _doubleArrow_12 = ObjectExtensions.<Polyline>operator_doubleArrow(_polyline, _function_16);
            _children_9.add(_doubleArrow_12);
          };
          Group _doubleArrow_11 = ObjectExtensions.<Group>operator_doubleArrow(_group_6, _function_14);
          _children_7.add(_doubleArrow_11);
        };
        ObjectExtensions.<Pane>operator_doubleArrow(_pane, _function_6);
      };
      ClickThroughSlide _doubleArrow_4 = ObjectExtensions.<ClickThroughSlide>operator_doubleArrow(_createClickThroughSlide_2, _function_5);
      _slides_8.add(_doubleArrow_4);
      ObservableList<Slide> _slides_9 = it.getSlides();
      Slide _createSlide_6 = EclipseConSlideFactory.createSlide("Reproduction", 144);
      _slides_9.add(_createSlide_6);
      ObservableList<Slide> _slides_10 = it.getSlides();
      ClickThroughSlide _createClickThroughSlide_3 = EclipseConSlideFactory.createClickThroughSlide("Reproduction images");
      final Procedure1<ClickThroughSlide> _function_6 = (ClickThroughSlide it_1) -> {
        Pane _pane = it_1.getPane();
        ObservableList<Node> _children = _pane.getChildren();
        ImageView _imageView = new ImageView();
        final Procedure1<ImageView> _function_7 = (ImageView it_2) -> {
          ImageCache _get = ImageCache.get();
          Image _image = _get.getImage(this, "images/graphiti_code.png");
          it_2.setImage(_image);
          it_2.setLayoutX(43);
          it_2.setLayoutY(41);
        };
        ImageView _doubleArrow_5 = ObjectExtensions.<ImageView>operator_doubleArrow(_imageView, _function_7);
        _children.add(_doubleArrow_5);
        Pane _pane_1 = it_1.getPane();
        ObservableList<Node> _children_1 = _pane_1.getChildren();
        VBox _vBox = new VBox();
        final Procedure1<VBox> _function_8 = (VBox it_2) -> {
          it_2.setLayoutX(313);
          it_2.setLayoutY(81);
          Insets _insets = new Insets(5, 5, 5, 5);
          it_2.setPadding(_insets);
          it_2.setAlignment(Pos.CENTER);
          StringConcatenation _builder = new StringConcatenation();
          _builder.append("-fx-border-color: black;");
          _builder.newLine();
          _builder.append("-fx-border-width: 1;");
          _builder.newLine();
          _builder.append("-fx-background-color: rgb(252,228,153);");
          _builder.newLine();
          it_2.setStyle(_builder.toString());
          ObservableList<Node> _children_2 = it_2.getChildren();
          Text _createText = EclipseConSlideFactory.createText("34 Files", 36);
          final Procedure1<Text> _function_9 = (Text it_3) -> {
            it_3.setFill(Color.BLACK);
          };
          Text _doubleArrow_6 = ObjectExtensions.<Text>operator_doubleArrow(_createText, _function_9);
          _children_2.add(_doubleArrow_6);
          ObservableList<Node> _children_3 = it_2.getChildren();
          Text _createText_1 = EclipseConSlideFactory.createText("2730 LOC", 36);
          final Procedure1<Text> _function_10 = (Text it_3) -> {
            it_3.setFill(Color.BLACK);
          };
          Text _doubleArrow_7 = ObjectExtensions.<Text>operator_doubleArrow(_createText_1, _function_10);
          _children_3.add(_doubleArrow_7);
        };
        VBox _doubleArrow_6 = ObjectExtensions.<VBox>operator_doubleArrow(_vBox, _function_8);
        _children_1.add(_doubleArrow_6);
        Pane _pane_2 = it_1.getPane();
        ObservableList<Node> _children_2 = _pane_2.getChildren();
        ImageView _imageView_1 = new ImageView();
        final Procedure1<ImageView> _function_9 = (ImageView it_2) -> {
          ImageCache _get = ImageCache.get();
          Image _image = _get.getImage(this, "images/gmf_dashboard.png");
          it_2.setImage(_image);
          it_2.setLayoutX(284);
          it_2.setLayoutY(406);
        };
        ImageView _doubleArrow_7 = ObjectExtensions.<ImageView>operator_doubleArrow(_imageView_1, _function_9);
        _children_2.add(_doubleArrow_7);
      };
      ClickThroughSlide _doubleArrow_5 = ObjectExtensions.<ClickThroughSlide>operator_doubleArrow(_createClickThroughSlide_3, _function_6);
      _slides_10.add(_doubleArrow_5);
      ObservableList<Slide> _slides_11 = it.getSlides();
      Slide _createSlide_7 = EclipseConSlideFactory.createSlide("Endangerment", 144);
      _slides_11.add(_createSlide_7);
      ObservableList<Slide> _slides_12 = it.getSlides();
      ClickThroughSlide _createClickThroughSlide_4 = EclipseConSlideFactory.createClickThroughSlide("Tablet");
      final Procedure1<ClickThroughSlide> _function_7 = (ClickThroughSlide it_1) -> {
        Pane _pane = it_1.getPane();
        ObservableList<Node> _children = _pane.getChildren();
        ImageView _imageView = new ImageView();
        final Procedure1<ImageView> _function_8 = (ImageView it_2) -> {
          ImageCache _get = ImageCache.get();
          Image _image = _get.getImage(this, "images/tablet.png");
          it_2.setImage(_image);
          it_2.setLayoutX(183);
          it_2.setLayoutY(210);
          it_2.setFitWidth(587);
          it_2.setPreserveRatio(true);
        };
        ImageView _doubleArrow_6 = ObjectExtensions.<ImageView>operator_doubleArrow(_imageView, _function_8);
        _children.add(_doubleArrow_6);
        Pane _pane_1 = it_1.getPane();
        ObservableList<Node> _children_1 = _pane_1.getChildren();
        ImageView _imageView_1 = new ImageView();
        final Procedure1<ImageView> _function_9 = (ImageView it_2) -> {
          ImageCache _get = ImageCache.get();
          Image _image = _get.getImage(this, "images/hand.png");
          it_2.setImage(_image);
          it_2.setLayoutX(540);
          it_2.setLayoutY(244);
        };
        ImageView _doubleArrow_7 = ObjectExtensions.<ImageView>operator_doubleArrow(_imageView_1, _function_9);
        _children_1.add(_doubleArrow_7);
      };
      ClickThroughSlide _doubleArrow_6 = ObjectExtensions.<ClickThroughSlide>operator_doubleArrow(_createClickThroughSlide_4, _function_7);
      _slides_12.add(_doubleArrow_6);
      ObservableList<Slide> _slides_13 = it.getSlides();
      Slide _createSlide_8 = EclipseConSlideFactory.createSlide("Help");
      final Procedure1<Slide> _function_8 = (Slide it_1) -> {
        StackPane _stackPane = it_1.getStackPane();
        final Procedure1<StackPane> _function_9 = (StackPane it_2) -> {
          ObservableList<Node> _children = it_2.getChildren();
          VBox _vBox = new VBox();
          final Procedure1<VBox> _function_10 = (VBox it_3) -> {
            it_3.setAlignment(Pos.CENTER);
            it_3.setSpacing(50);
            ObservableList<Node> _children_1 = it_3.getChildren();
            Text _createText = EclipseConSlideFactory.createText("Help Us", 144);
            final Procedure1<Text> _function_11 = (Text it_4) -> {
            };
            Text _doubleArrow_7 = ObjectExtensions.<Text>operator_doubleArrow(_createText, _function_11);
            _children_1.add(_doubleArrow_7);
            ObservableList<Node> _children_2 = it_3.getChildren();
            Text _createText_1 = EclipseConSlideFactory.createText("save the", 72);
            final Procedure1<Text> _function_12 = (Text it_4) -> {
            };
            Text _doubleArrow_8 = ObjectExtensions.<Text>operator_doubleArrow(_createText_1, _function_12);
            _children_2.add(_doubleArrow_8);
            ObservableList<Node> _children_3 = it_3.getChildren();
            Text _createText_2 = EclipseConSlideFactory.createText("Eclipse Diagram Editors", 96);
            final Procedure1<Text> _function_13 = (Text it_4) -> {
              Color _rgb = Color.rgb(238, 191, 171);
              it_4.setFill(_rgb);
            };
            Text _doubleArrow_9 = ObjectExtensions.<Text>operator_doubleArrow(_createText_2, _function_13);
            _children_3.add(_doubleArrow_9);
          };
          VBox _doubleArrow_7 = ObjectExtensions.<VBox>operator_doubleArrow(_vBox, _function_10);
          _children.add(_doubleArrow_7);
        };
        ObjectExtensions.<StackPane>operator_doubleArrow(_stackPane, _function_9);
      };
      Slide _doubleArrow_7 = ObjectExtensions.<Slide>operator_doubleArrow(_createSlide_8, _function_8);
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
      final Procedure1<Slide> _function_9 = (Slide it_1) -> {
        StackPane _stackPane = it_1.getStackPane();
        final Procedure1<StackPane> _function_10 = (StackPane it_2) -> {
          ObservableList<Node> _children = it_2.getChildren();
          ImageView _imageView = new ImageView();
          final Procedure1<ImageView> _function_11 = (ImageView it_3) -> {
            ImageCache _get = ImageCache.get();
            Image _image = _get.getImage(this, "images/javafx.png");
            it_3.setImage(_image);
            it_3.setFitWidth(587);
            it_3.setPreserveRatio(true);
          };
          ImageView _doubleArrow_8 = ObjectExtensions.<ImageView>operator_doubleArrow(_imageView, _function_11);
          _children.add(_doubleArrow_8);
        };
        ObjectExtensions.<StackPane>operator_doubleArrow(_stackPane, _function_10);
      };
      Slide _doubleArrow_8 = ObjectExtensions.<Slide>operator_doubleArrow(_createSlide_11, _function_9);
      _slides_16.add(_doubleArrow_8);
    };
    SlideDiagram _doubleArrow = ObjectExtensions.<SlideDiagram>operator_doubleArrow(_slideDiagram, _function);
    this.setInnerDiagram(_doubleArrow);
    super.doActivate();
  }
  
  public void populate(final ModelElementImpl modelElement) {
    super.populate(modelElement);
  }
}
