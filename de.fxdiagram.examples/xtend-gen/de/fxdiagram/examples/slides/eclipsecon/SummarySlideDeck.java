package de.fxdiagram.examples.slides.eclipsecon;

import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.core.services.ImageCache;
import de.fxdiagram.examples.slides.Animations;
import de.fxdiagram.examples.slides.Slide;
import de.fxdiagram.examples.slides.SlideDiagram;
import de.fxdiagram.examples.slides.eclipsecon.EclipseConSlideFactory;
import de.fxdiagram.lib.simple.OpenableDiagramNode;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@ModelNode
@SuppressWarnings("all")
public class SummarySlideDeck extends OpenableDiagramNode {
  public SummarySlideDeck() {
    super("Summary");
  }
  
  public void doActivate() {
    SlideDiagram _slideDiagram = new SlideDiagram();
    final Procedure1<SlideDiagram> _function = new Procedure1<SlideDiagram>() {
      public void apply(final SlideDiagram it) {
        ObservableList<Slide> _slides = it.getSlides();
        Slide _createSlide = EclipseConSlideFactory.createSlide("Summary", 144);
        _slides.add(_createSlide);
        ObservableList<Slide> _slides_1 = it.getSlides();
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("The users must be our top priority.");
        _builder.newLine();
        _builder.append("Not developers.");
        _builder.newLine();
        _builder.append("Not frameworks.");
        _builder.newLine();
        Slide _createSlide_1 = EclipseConSlideFactory.createSlide(_builder.toString(), 48);
        _slides_1.add(_createSlide_1);
        ObservableList<Slide> _slides_2 = it.getSlides();
        Slide _createSlide_2 = EclipseConSlideFactory.createSlide("JavaFX advantages");
        final Procedure1<Slide> _function = new Procedure1<Slide>() {
          public void apply(final Slide it) {
            StackPane _stackPane = it.getStackPane();
            ObservableList<Node> _children = _stackPane.getChildren();
            VBox _vBox = new VBox();
            final Procedure1<VBox> _function = new Procedure1<VBox>() {
              public void apply(final VBox it) {
                it.setAlignment(Pos.CENTER);
                ObservableList<Node> _children = it.getChildren();
                ImageView _imageView = new ImageView();
                final Procedure1<ImageView> _function = new Procedure1<ImageView>() {
                  public void apply(final ImageView it) {
                    ImageCache _get = ImageCache.get();
                    Image _image = _get.getImage(SummarySlideDeck.this, "images/javafx.png");
                    it.setImage(_image);
                    it.setFitWidth(300);
                    it.setPreserveRatio(true);
                    Insets _insets = new Insets(0, 0, (-30), 0);
                    VBox.setMargin(it, _insets);
                  }
                };
                ImageView _doubleArrow = ObjectExtensions.<ImageView>operator_doubleArrow(_imageView, _function);
                _children.add(_doubleArrow);
                ObservableList<Node> _children_1 = it.getChildren();
                StringConcatenation _builder = new StringConcatenation();
                _builder.append("provides superior graphics,");
                _builder.newLine();
                _builder.append("allows to focus on usability,");
                _builder.newLine();
                _builder.append("and leverages the hardware...");
                _builder.newLine();
                Text _createText = EclipseConSlideFactory.createText(_builder.toString(), 48);
                _children_1.add(_createText);
              }
            };
            VBox _doubleArrow = ObjectExtensions.<VBox>operator_doubleArrow(_vBox, _function);
            _children.add(_doubleArrow);
          }
        };
        Slide _doubleArrow = ObjectExtensions.<Slide>operator_doubleArrow(_createSlide_2, _function);
        _slides_2.add(_doubleArrow);
        ObservableList<Slide> _slides_3 = it.getSlides();
        StringConcatenation _builder_1 = new StringConcatenation();
        _builder_1.append("...and makes ");
        _builder_1.newLine();
        _builder_1.append("developing graphical editors");
        _builder_1.newLine();
        _builder_1.append("fun again.");
        _builder_1.newLine();
        Slide _createSlide_3 = EclipseConSlideFactory.createSlide(_builder_1.toString(), 48);
        _slides_3.add(_createSlide_3);
        ObservableList<Slide> _slides_4 = it.getSlides();
        Slide _createSlide_4 = EclipseConSlideFactory.createSlide("Thanks");
        final Procedure1<Slide> _function_1 = new Procedure1<Slide>() {
          public void apply(final Slide it) {
            StackPane _stackPane = it.getStackPane();
            ObservableList<Node> _children = _stackPane.getChildren();
            VBox _vBox = new VBox();
            final Procedure1<VBox> _function = new Procedure1<VBox>() {
              public void apply(final VBox it) {
                it.setAlignment(Pos.CENTER);
                ObservableList<Node> _children = it.getChildren();
                Text _createText = EclipseConSlideFactory.createText("Thanks to", 144);
                final Procedure1<Text> _function = new Procedure1<Text>() {
                  public void apply(final Text it) {
                    Insets _insets = new Insets(0, 0, 30, 0);
                    VBox.setMargin(it, _insets);
                  }
                };
                Text _doubleArrow = ObjectExtensions.<Text>operator_doubleArrow(_createText, _function);
                _children.add(_doubleArrow);
                ObservableList<Node> _children_1 = it.getChildren();
                HBox _createMixedText = SummarySlideDeck.this.createMixedText("Gerrit Grunwald", "for JavaFX inspiration");
                _children_1.add(_createMixedText);
                ObservableList<Node> _children_2 = it.getChildren();
                HBox _createMixedText_1 = SummarySlideDeck.this.createMixedText("Tom Schindl", "for e(fx)clipse");
                _children_2.add(_createMixedText_1);
                ObservableList<Node> _children_3 = it.getChildren();
                HBox _createMixedText_2 = SummarySlideDeck.this.createMixedText("The KIELER framework", "for auto-layout");
                _children_3.add(_createMixedText_2);
                ObservableList<Node> _children_4 = it.getChildren();
                HBox _createMixedText_3 = SummarySlideDeck.this.createMixedText("GraphViz", "for the layout algorithm");
                _children_4.add(_createMixedText_3);
                ObservableList<Node> _children_5 = it.getChildren();
                HBox _createMixedText_4 = SummarySlideDeck.this.createMixedText("Memory Alpha", "for the data on StarTrek");
                _children_5.add(_createMixedText_4);
                ObservableList<Node> _children_6 = it.getChildren();
                HBox _createMixedText_5 = SummarySlideDeck.this.createMixedText("GTJLCARS", "for the StarTrek font");
                _children_6.add(_createMixedText_5);
              }
            };
            VBox _doubleArrow = ObjectExtensions.<VBox>operator_doubleArrow(_vBox, _function);
            _children.add(_doubleArrow);
          }
        };
        Slide _doubleArrow_1 = ObjectExtensions.<Slide>operator_doubleArrow(_createSlide_4, _function_1);
        _slides_4.add(_doubleArrow_1);
      }
    };
    SlideDiagram _doubleArrow = ObjectExtensions.<SlideDiagram>operator_doubleArrow(_slideDiagram, _function);
    this.setInnerDiagram(_doubleArrow);
    super.doActivate();
  }
  
  protected HBox createMixedText(final String jungleText, final String normalText) {
    HBox _hBox = new HBox();
    final Procedure1<HBox> _function = new Procedure1<HBox>() {
      public void apply(final HBox it) {
        it.setAlignment(Pos.CENTER);
        it.setSpacing(16);
        ObservableList<Node> _children = it.getChildren();
        Text _createJungleText = EclipseConSlideFactory.createJungleText(jungleText, 36);
        final Procedure1<Text> _function = new Procedure1<Text>() {
          public void apply(final Text it) {
            Color _jungleDarkGreen = EclipseConSlideFactory.jungleDarkGreen();
            Color _jungleDarkestGreen = EclipseConSlideFactory.jungleDarkestGreen();
            Animations.breathe(it, _jungleDarkGreen, _jungleDarkestGreen);
          }
        };
        Text _doubleArrow = ObjectExtensions.<Text>operator_doubleArrow(_createJungleText, _function);
        _children.add(_doubleArrow);
        ObservableList<Node> _children_1 = it.getChildren();
        Text _createText = EclipseConSlideFactory.createText(normalText, 36);
        _children_1.add(_createText);
      }
    };
    return ObjectExtensions.<HBox>operator_doubleArrow(_hBox, _function);
  }
  
  public void populate(final ModelElementImpl modelElement) {
    super.populate(modelElement);
  }
}
