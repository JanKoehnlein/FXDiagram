package de.fxdiagram.examples.slides.democamp;

import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.examples.slides.Animations;
import de.fxdiagram.examples.slides.Slide;
import de.fxdiagram.examples.slides.SlideDiagram;
import de.fxdiagram.examples.slides.democamp.DemoCampSlideFactory;
import de.fxdiagram.lib.simple.OpenableDiagramNode;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
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
public class DemoCampSummarySlides extends OpenableDiagramNode {
  public DemoCampSummarySlides() {
    super("Summary");
  }
  
  public void doActivate() {
    SlideDiagram _slideDiagram = new SlideDiagram();
    final Procedure1<SlideDiagram> _function = new Procedure1<SlideDiagram>() {
      public void apply(final SlideDiagram it) {
        ObservableList<Slide> _slides = it.getSlides();
        Slide _createSlide = DemoCampSlideFactory.createSlide("Conclusion");
        final Procedure1<Slide> _function = new Procedure1<Slide>() {
          public void apply(final Slide it) {
            StackPane _stackPane = it.getStackPane();
            ObservableList<Node> _children = _stackPane.getChildren();
            Text _createText = DemoCampSlideFactory.createText("Conclusion", 100);
            _children.add(_createText);
          }
        };
        Slide _doubleArrow = ObjectExtensions.<Slide>operator_doubleArrow(_createSlide, _function);
        _slides.add(_doubleArrow);
        ObservableList<Slide> _slides_1 = it.getSlides();
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("Focus on users");
        _builder.newLine();
        _builder.append("not on developers.");
        _builder.newLine();
        Slide _createSlideWithText = DemoCampSlideFactory.createSlideWithText("Conclusion 2", _builder.toString(), 72);
        _slides_1.add(_createSlideWithText);
        ObservableList<Slide> _slides_2 = it.getSlides();
        StringConcatenation _builder_1 = new StringConcatenation();
        _builder_1.append("Take back control and");
        _builder_1.newLine();
        _builder_1.append("start programming again.");
        _builder_1.newLine();
        Slide _createSlideWithText_1 = DemoCampSlideFactory.createSlideWithText("Conclusion 3", _builder_1.toString(), 72);
        _slides_2.add(_createSlideWithText_1);
        ObservableList<Slide> _slides_3 = it.getSlides();
        Slide _createSlide_1 = DemoCampSlideFactory.createSlide("Credits");
        final Procedure1<Slide> _function_1 = new Procedure1<Slide>() {
          public void apply(final Slide it) {
            StackPane _stackPane = it.getStackPane();
            ObservableList<Node> _children = _stackPane.getChildren();
            VBox _vBox = new VBox();
            final Procedure1<VBox> _function = new Procedure1<VBox>() {
              public void apply(final VBox it) {
                it.setAlignment(Pos.CENTER);
                ObservableList<Node> _children = it.getChildren();
                Text _createText = DemoCampSlideFactory.createText("Thanks to", 144);
                final Procedure1<Text> _function = new Procedure1<Text>() {
                  public void apply(final Text it) {
                    Insets _insets = new Insets(0, 0, 30, 0);
                    VBox.setMargin(it, _insets);
                  }
                };
                Text _doubleArrow = ObjectExtensions.<Text>operator_doubleArrow(_createText, _function);
                _children.add(_doubleArrow);
                ObservableList<Node> _children_1 = it.getChildren();
                HBox _createMixedText = DemoCampSummarySlides.this.createMixedText("Gerrit Grunwald", "for JavaFX inspiration");
                _children_1.add(_createMixedText);
                ObservableList<Node> _children_2 = it.getChildren();
                HBox _createMixedText_1 = DemoCampSummarySlides.this.createMixedText("Tom Schindl", "for e(fx)clipse");
                _children_2.add(_createMixedText_1);
                ObservableList<Node> _children_3 = it.getChildren();
                HBox _createMixedText_2 = DemoCampSummarySlides.this.createMixedText("The KIELER framework", "for auto-layout");
                _children_3.add(_createMixedText_2);
                ObservableList<Node> _children_4 = it.getChildren();
                HBox _createMixedText_3 = DemoCampSummarySlides.this.createMixedText("GraphViz", "for the layout algorithm");
                _children_4.add(_createMixedText_3);
                ObservableList<Node> _children_5 = it.getChildren();
                HBox _createMixedText_4 = DemoCampSummarySlides.this.createMixedText("Memory Alpha", "for the data on StarTrek");
                _children_5.add(_createMixedText_4);
                ObservableList<Node> _children_6 = it.getChildren();
                HBox _createMixedText_5 = DemoCampSummarySlides.this.createMixedText("GTJLCARS", "for the StarTrek font");
                _children_6.add(_createMixedText_5);
              }
            };
            VBox _doubleArrow = ObjectExtensions.<VBox>operator_doubleArrow(_vBox, _function);
            _children.add(_doubleArrow);
          }
        };
        Slide _doubleArrow_1 = ObjectExtensions.<Slide>operator_doubleArrow(_createSlide_1, _function_1);
        _slides_3.add(_doubleArrow_1);
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
        it.setSpacing(24);
        ObservableList<Node> _children = it.getChildren();
        Text _createText = DemoCampSlideFactory.createText(jungleText, 40);
        final Procedure1<Text> _function = new Procedure1<Text>() {
          public void apply(final Text it) {
            Color _textColor = DemoCampSlideFactory.getTextColor();
            Color _darkTextColor = DemoCampSlideFactory.getDarkTextColor();
            Animations.flicker(it, _textColor, _darkTextColor);
          }
        };
        Text _doubleArrow = ObjectExtensions.<Text>operator_doubleArrow(_createText, _function);
        _children.add(_doubleArrow);
        ObservableList<Node> _children_1 = it.getChildren();
        Text _createText_1 = DemoCampSlideFactory.createText(normalText, 36);
        _children_1.add(_createText_1);
      }
    };
    return ObjectExtensions.<HBox>operator_doubleArrow(_hBox, _function);
  }
  
  public void populate(final ModelElementImpl modelElement) {
    super.populate(modelElement);
  }
}
