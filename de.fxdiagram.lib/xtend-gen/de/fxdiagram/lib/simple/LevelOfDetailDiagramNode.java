package de.fxdiagram.lib.simple;

import com.google.common.base.Objects;
import de.fxdiagram.annotations.logging.Logging;
import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.anchors.Anchors;
import de.fxdiagram.core.extensions.AccumulativeTransform2D;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.extensions.TooltipExtensions;
import de.fxdiagram.core.model.DomainObjectHandle;
import de.fxdiagram.core.model.ModelElement;
import de.fxdiagram.lib.anchors.RoundedRectangleAnchors;
import de.fxdiagram.lib.nodes.RectangleBorderPane;
import de.fxdiagram.lib.simple.DiagramScaler;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyDoubleProperty;
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

@Logging
@ModelNode({ "layoutX", "layoutY", "domainObject", "width", "height" })
@SuppressWarnings("all")
public class LevelOfDetailDiagramNode extends XNode {
  private RectangleBorderPane pane = new RectangleBorderPane();
  
  private Text label;
  
  private Group innerDiagramGroup;
  
  private XDiagram innerDiagram;
  
  private DiagramScaler diagramScaler;
  
  public LevelOfDetailDiagramNode(final String name) {
    super(name);
  }
  
  public LevelOfDetailDiagramNode(final DomainObjectHandle domainObject) {
    super(domainObject);
  }
  
  public void doActivatePreview() {
    super.doActivatePreview();
    final Procedure1<RectangleBorderPane> _function = new Procedure1<RectangleBorderPane>() {
      public void apply(final RectangleBorderPane it) {
        ObservableList<Node> _children = it.getChildren();
        Text _text = new Text();
        final Procedure1<Text> _function = new Procedure1<Text>() {
          public void apply(final Text it) {
            it.setTextOrigin(VPos.TOP);
            String _key = LevelOfDetailDiagramNode.this.getKey();
            it.setText(_key);
            Insets _insets = new Insets(10, 20, 10, 20);
            StackPane.setMargin(it, _insets);
            TooltipExtensions.setTooltip(it, "Zoom to reveal content");
          }
        };
        Text _doubleArrow = ObjectExtensions.<Text>operator_doubleArrow(_text, _function);
        Text _label = LevelOfDetailDiagramNode.this.label = _doubleArrow;
        _children.add(_label);
      }
    };
    RectangleBorderPane _doubleArrow = ObjectExtensions.<RectangleBorderPane>operator_doubleArrow(this.pane, _function);
    this.setNode(_doubleArrow);
  }
  
  public DiagramScaler setInnerDiagram(final XDiagram innerDiagram) {
    DiagramScaler _xblockexpression = null;
    {
      this.innerDiagram = innerDiagram;
      ObservableList<Node> _children = this.pane.getChildren();
      Group _group = new Group();
      final Procedure1<Group> _function = new Procedure1<Group>() {
        public void apply(final Group it) {
          ObservableList<Node> _children = it.getChildren();
          _children.setAll(innerDiagram);
        }
      };
      Group _doubleArrow = ObjectExtensions.<Group>operator_doubleArrow(_group, _function);
      Group _innerDiagramGroup = this.innerDiagramGroup = _doubleArrow;
      _children.add(_innerDiagramGroup);
      DiagramScaler _diagramScaler = new DiagramScaler(innerDiagram);
      _xblockexpression = this.diagramScaler = _diagramScaler;
    }
    return _xblockexpression;
  }
  
  protected Anchors createAnchors() {
    return new RoundedRectangleAnchors(this, 12, 12);
  }
  
  public void doActivate() {
    super.doActivate();
    DomainObjectHandle _domainObject = this.getDomainObject();
    String _key = null;
    if (_domainObject!=null) {
      _key=_domainObject.getKey();
    }
    this.label.setText(_key);
    boolean _equals = Objects.equal(this.innerDiagram, null);
    if (_equals) {
      String _key_1 = this.getKey();
      String _plus = ("No inner diagram set on node " + _key_1);
      String _plus_1 = (_plus + ". LOD behavior deactivated");
      LevelOfDetailDiagramNode.LOG.severe(_plus_1);
    } else {
      XRoot _root = CoreExtensions.getRoot(this);
      AccumulativeTransform2D _diagramTransform = _root.getDiagramTransform();
      ReadOnlyDoubleProperty _scaleProperty = _diagramTransform.scaleProperty();
      final ChangeListener<Number> _function = new ChangeListener<Number>() {
        public void changed(final ObservableValue<? extends Number> prop, final Number oldVal, final Number newVal) {
          Bounds _boundsInLocal = LevelOfDetailDiagramNode.this.getBoundsInLocal();
          final Bounds bounds = LevelOfDetailDiagramNode.this.localToScene(_boundsInLocal);
          double _width = bounds.getWidth();
          double _height = bounds.getHeight();
          final double area = (_width * _height);
          if ((area <= 100000)) {
            LevelOfDetailDiagramNode.this.label.setVisible(true);
            LevelOfDetailDiagramNode.this.innerDiagramGroup.setVisible(false);
            LevelOfDetailDiagramNode.this.pane.setBackgroundPaint(RectangleBorderPane.DEFAULT_BACKGROUND);
          } else {
            LevelOfDetailDiagramNode.this.label.setVisible(false);
            LevelOfDetailDiagramNode.this.innerDiagramGroup.setVisible(true);
            LevelOfDetailDiagramNode.this.innerDiagram.activate();
            final Procedure1<DiagramScaler> _function = new Procedure1<DiagramScaler>() {
              public void apply(final DiagramScaler it) {
                Bounds _layoutBounds = LevelOfDetailDiagramNode.this.label.getLayoutBounds();
                double _width = _layoutBounds.getWidth();
                double _plus = (_width + 40);
                it.setWidth(_plus);
                Bounds _layoutBounds_1 = LevelOfDetailDiagramNode.this.label.getLayoutBounds();
                double _height = _layoutBounds_1.getHeight();
                double _plus_1 = (_height + 20);
                it.setHeight(_plus_1);
                it.activate();
              }
            };
            ObjectExtensions.<DiagramScaler>operator_doubleArrow(
              LevelOfDetailDiagramNode.this.diagramScaler, _function);
            Paint _backgroundPaint = LevelOfDetailDiagramNode.this.innerDiagram.getBackgroundPaint();
            LevelOfDetailDiagramNode.this.pane.setBackgroundPaint(_backgroundPaint);
          }
        }
      };
      _scaleProperty.addListener(_function);
    }
  }
  
  private static Logger LOG = Logger.getLogger("de.fxdiagram.lib.simple.LevelOfDetailDiagramNode");
    ;
  
  /**
   * Automatically generated by @ModelNode. Needed for deserialization.
   */
  public LevelOfDetailDiagramNode() {
  }
  
  public void populate(final ModelElement modelElement) {
    modelElement.addProperty(layoutXProperty(), Double.class);
    modelElement.addProperty(layoutYProperty(), Double.class);
    modelElement.addProperty(domainObjectProperty(), DomainObjectHandle.class);
    modelElement.addProperty(widthProperty(), Double.class);
    modelElement.addProperty(heightProperty(), Double.class);
  }
}
