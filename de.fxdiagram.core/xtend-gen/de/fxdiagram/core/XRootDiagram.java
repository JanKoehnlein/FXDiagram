package de.fxdiagram.core;

import de.fxdiagram.annotations.logging.Logging;
import de.fxdiagram.core.XAbstractDiagram;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.binding.NumberExpressionExtensions;
import java.util.logging.Logger;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;
import org.eclipse.xtext.xbase.lib.Functions.Function0;

@Logging
@SuppressWarnings("all")
public class XRootDiagram extends XAbstractDiagram {
  private Group nodeLayer = new Function0<Group>() {
    public Group apply() {
      Group _group = new Group();
      return _group;
    }
  }.apply();
  
  private Group buttonLayer = new Function0<Group>() {
    public Group apply() {
      Group _group = new Group();
      return _group;
    }
  }.apply();
  
  public final static double MIN_SCALE = NumberExpressionExtensions.EPSILON;
  
  private XRoot root;
  
  private Affine canvasTransform;
  
  public XRootDiagram(final XRoot root) {
    this.root = root;
    ObservableList<Node> _children = this.getChildren();
    _children.add(this.nodeLayer);
    ObservableList<Node> _children_1 = this.getChildren();
    _children_1.add(this.buttonLayer);
    Affine _affine = new Affine();
    this.canvasTransform = _affine;
    ObservableList<Transform> _transforms = this.getTransforms();
    _transforms.setAll(this.canvasTransform);
  }
  
  public void doActivate() {
    super.doActivate();
  }
  
  public Group getNodeLayer() {
    return this.nodeLayer;
  }
  
  public Group getConnectionLayer() {
    return this.nodeLayer;
  }
  
  public Group getButtonLayer() {
    return this.buttonLayer;
  }
  
  public Affine getCanvasTransform() {
    return this.canvasTransform;
  }
  
  private static Logger LOG = Logger.getLogger("de.fxdiagram.core.XRootDiagram");
    ;
  
  private SimpleDoubleProperty scaleProperty = new SimpleDoubleProperty(this, "scale",_initScale());
  
  private static final double _initScale() {
    return 1.0;
  }
  
  public double getScale() {
    return this.scaleProperty.get();
  }
  
  public void setScale(final double scale) {
    this.scaleProperty.set(scale);
  }
  
  public DoubleProperty scaleProperty() {
    return this.scaleProperty;
  }
}
