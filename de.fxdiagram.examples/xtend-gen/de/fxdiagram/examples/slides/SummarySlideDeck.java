package de.fxdiagram.examples.slides;

import de.fxdiagram.core.services.ImageCache;
import de.fxdiagram.examples.slides.Slide;
import de.fxdiagram.examples.slides.SlideDiagram;
import de.fxdiagram.examples.slides.Styles;
import de.fxdiagram.lib.simple.OpenableDiagramNode;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class SummarySlideDeck extends OpenableDiagramNode {
  public SummarySlideDeck() {
    super("Summary");
    SlideDiagram _slideDiagram = new SlideDiagram();
    final Procedure1<SlideDiagram> _function = new Procedure1<SlideDiagram>() {
      public void apply(final SlideDiagram it) {
        List<Slide> _slides = it.getSlides();
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("Diagrams are for humans");
        _builder.newLine();
        _builder.append("not for computers.");
        _builder.newLine();
        _builder.append("Visual design and usability");
        _builder.newLine();
        _builder.append("are top priority.");
        _builder.newLine();
        Slide _slide = new Slide(_builder.toString(), 48);
        _slides.add(_slide);
        List<Slide> _slides_1 = it.getSlides();
        Slide _slide_1 = new Slide("JavaFX advantages");
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
                    int _minus = (-30);
                    Insets _insets = new Insets(0, 0, _minus, 0);
                    VBox.setMargin(it, _insets);
                  }
                };
                ImageView _doubleArrow = ObjectExtensions.<ImageView>operator_doubleArrow(_imageView, _function);
                _children.add(_doubleArrow);
                ObservableList<Node> _children_1 = it.getChildren();
                StringConcatenation _builder = new StringConcatenation();
                _builder.append("allows superior visual design,");
                _builder.newLine();
                _builder.append("helps to focus on usability,");
                _builder.newLine();
                _builder.append("and leverages the hardware...");
                _builder.newLine();
                Text _createText = Styles.createText(_builder.toString(), 48);
                _children_1.add(_createText);
              }
            };
            VBox _doubleArrow = ObjectExtensions.<VBox>operator_doubleArrow(_vBox, _function);
            _children.add(_doubleArrow);
          }
        };
        Slide _doubleArrow = ObjectExtensions.<Slide>operator_doubleArrow(_slide_1, _function);
        _slides_1.add(_doubleArrow);
        List<Slide> _slides_2 = it.getSlides();
        StringConcatenation _builder_1 = new StringConcatenation();
        _builder_1.append("...and makes developing graphical editors");
        _builder_1.newLine();
        _builder_1.append("fun again.");
        _builder_1.newLine();
        Slide _slide_2 = new Slide(_builder_1.toString(), 48);
        _slides_2.add(_slide_2);
        List<Slide> _slides_3 = it.getSlides();
        Slide _slide_3 = new Slide("Thanks");
        final Procedure1<Slide> _function_1 = new Procedure1<Slide>() {
          public void apply(final Slide it) {
            StackPane _stackPane = it.getStackPane();
            ObservableList<Node> _children = _stackPane.getChildren();
            VBox _vBox = new VBox();
            final Procedure1<VBox> _function = new Procedure1<VBox>() {
              public void apply(final VBox it) {
                it.setAlignment(Pos.CENTER);
                ObservableList<Node> _children = it.getChildren();
                Text _createText = Styles.createText("Thanks to", 144);
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
                HBox _createMixedText_3 = SummarySlideDeck.this.createMixedText("GaphViz", "for the layout algorithm");
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
        Slide _doubleArrow_1 = ObjectExtensions.<Slide>operator_doubleArrow(_slide_3, _function_1);
        _slides_3.add(_doubleArrow_1);
      }
    };
    SlideDiagram _doubleArrow = ObjectExtensions.<SlideDiagram>operator_doubleArrow(_slideDiagram, _function);
    this.setInnerDiagram(_doubleArrow);
  }
  
  protected HBox createMixedText(final String jungleText, final String normalText) {
    HBox _hBox = new HBox();
    final Procedure1<HBox> _function = new Procedure1<HBox>() {
      public void apply(final HBox it) {
        it.setAlignment(Pos.CENTER);
        it.setSpacing(16);
        ObservableList<Node> _children = it.getChildren();
        Text _createJungleText = Styles.createJungleText(jungleText, 36);
        _children.add(_createJungleText);
        ObservableList<Node> _children_1 = it.getChildren();
        Text _createText = Styles.createText(normalText, 36);
        _children_1.add(_createText);
      }
    };
    HBox _doubleArrow = ObjectExtensions.<HBox>operator_doubleArrow(_hBox, _function);
    return _doubleArrow;
  }
}
