package de.fxdiagram.lib.simple;

import com.google.common.base.Objects;
import de.fxdiagram.annotations.logging.Logging;
import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XDiagramContainer;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.anchors.Anchors;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.extensions.TooltipExtensions;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.core.model.ToString;
import de.fxdiagram.core.viewport.ViewportTransform;
import de.fxdiagram.lib.anchors.RoundedRectangleAnchors;
import de.fxdiagram.lib.nodes.RectangleBorderPane;
import de.fxdiagram.lib.simple.DiagramScaler;
import java.util.logging.Logger;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

/**
 * An {@link XNode} containing another diagram that is revealed when the user zooms in.
 */
@Logging
@ModelNode
@SuppressWarnings("all")
public class LevelOfDetailDiagramNode extends XNode implements XDiagramContainer {
  private RectangleBorderPane pane = new RectangleBorderPane();
  
  private Text label;
  
  private Group innerDiagramGroup;
  
  private DiagramScaler diagramScaler;
  
  public LevelOfDetailDiagramNode(final String name) {
    super(name);
  }
  
  public LevelOfDetailDiagramNode(final DomainObjectDescriptor domainObject) {
    super(domainObject);
  }
  
  @Override
  protected Node createNode() {
    final Procedure1<RectangleBorderPane> _function = (RectangleBorderPane it) -> {
      ObservableList<Node> _children = it.getChildren();
      Text _text = new Text();
      final Procedure1<Text> _function_1 = (Text it_1) -> {
        it_1.setTextOrigin(VPos.TOP);
        String _name = this.getName();
        it_1.setText(_name);
        Insets _insets = new Insets(10, 20, 10, 20);
        StackPane.setMargin(it_1, _insets);
      };
      Text _doubleArrow = ObjectExtensions.<Text>operator_doubleArrow(_text, _function_1);
      Text _label = (this.label = _doubleArrow);
      _children.add(_label);
    };
    return ObjectExtensions.<RectangleBorderPane>operator_doubleArrow(
      this.pane, _function);
  }
  
  @Override
  public Insets getInsets() {
    return null;
  }
  
  @Override
  public boolean isInnerDiagramActive() {
    return (this.getInnerDiagram().getIsActive() && this.getInnerDiagram().isVisible());
  }
  
  @Override
  protected Anchors createAnchors() {
    return new RoundedRectangleAnchors(this, 12, 12);
  }
  
  @Override
  public void doActivate() {
    super.doActivate();
    final Procedure1<Text> _function = (Text it) -> {
      String _name = this.getName();
      it.setText(_name);
      TooltipExtensions.setTooltip(it, "Zoom to reveal content");
    };
    ObjectExtensions.<Text>operator_doubleArrow(
      this.label, _function);
    XDiagram _innerDiagram = this.getInnerDiagram();
    boolean _equals = Objects.equal(_innerDiagram, null);
    if (_equals) {
      String _name = this.getName();
      String _plus = ("No inner diagram set on node " + _name);
      String _plus_1 = (_plus + ". LOD behavior deactivated");
      LevelOfDetailDiagramNode.LOG.severe(_plus_1);
    } else {
      ObservableList<Node> _children = this.pane.getChildren();
      Group _group = new Group();
      final Procedure1<Group> _function_1 = (Group it) -> {
        ObservableList<Node> _children_1 = it.getChildren();
        XDiagram _innerDiagram_1 = this.getInnerDiagram();
        _children_1.setAll(_innerDiagram_1);
      };
      Group _doubleArrow = ObjectExtensions.<Group>operator_doubleArrow(_group, _function_1);
      Group _innerDiagramGroup = (this.innerDiagramGroup = _doubleArrow);
      _children.add(_innerDiagramGroup);
      XDiagram _innerDiagram_1 = this.getInnerDiagram();
      DiagramScaler _diagramScaler = new DiagramScaler(_innerDiagram_1);
      this.diagramScaler = _diagramScaler;
      XDiagram _diagram = CoreExtensions.getDiagram(this);
      ViewportTransform _viewportTransform = _diagram.getViewportTransform();
      ReadOnlyDoubleProperty _scaleProperty = _viewportTransform.scaleProperty();
      final ChangeListener<Number> _function_2 = (ObservableValue<? extends Number> prop, Number oldVal, Number newVal) -> {
        Bounds _boundsInLocal = this.getBoundsInLocal();
        final Bounds bounds = this.localToScene(_boundsInLocal);
        double _width = bounds.getWidth();
        double _height = bounds.getHeight();
        final double area = (_width * _height);
        if ((area <= 100000)) {
          this.label.setVisible(true);
          this.innerDiagramGroup.setVisible(false);
          this.pane.setBackgroundPaint(RectangleBorderPane.DEFAULT_BACKGROUND);
        } else {
          this.label.setVisible(false);
          this.innerDiagramGroup.setVisible(true);
          XDiagram _innerDiagram_2 = this.getInnerDiagram();
          _innerDiagram_2.activate();
          final Procedure1<DiagramScaler> _function_3 = (DiagramScaler it) -> {
            Bounds _layoutBounds = this.label.getLayoutBounds();
            double _width_1 = _layoutBounds.getWidth();
            double _plus_2 = (_width_1 + 40);
            it.setWidth(_plus_2);
            Bounds _layoutBounds_1 = this.label.getLayoutBounds();
            double _height_1 = _layoutBounds_1.getHeight();
            double _plus_3 = (_height_1 + 20);
            it.setHeight(_plus_3);
            it.activate();
          };
          ObjectExtensions.<DiagramScaler>operator_doubleArrow(
            this.diagramScaler, _function_3);
          XDiagram _innerDiagram_3 = this.getInnerDiagram();
          Paint _backgroundPaint = _innerDiagram_3.getBackgroundPaint();
          this.pane.setBackgroundPaint(_backgroundPaint);
        }
      };
      _scaleProperty.addListener(_function_2);
    }
  }
  
  private static Logger LOG = Logger.getLogger("de.fxdiagram.lib.simple.LevelOfDetailDiagramNode");
    ;
  
  /**
   * Automatically generated by @ModelNode. Needed for deserialization.
   */
  public LevelOfDetailDiagramNode() {
  }
  
  public void populate(final ModelElementImpl modelElement) {
    super.populate(modelElement);
  }
  
  public void postLoad() {
    
  }
  
  public String toString() {
    return ToString.toString(this);
  }
  
  private SimpleObjectProperty<XDiagram> innerDiagramProperty = new SimpleObjectProperty<XDiagram>(this, "innerDiagram");
  
  public XDiagram getInnerDiagram() {
    return this.innerDiagramProperty.get();
  }
  
  public void setInnerDiagram(final XDiagram innerDiagram) {
    this.innerDiagramProperty.set(innerDiagram);
  }
  
  public ObjectProperty<XDiagram> innerDiagramProperty() {
    return this.innerDiagramProperty;
  }
}
