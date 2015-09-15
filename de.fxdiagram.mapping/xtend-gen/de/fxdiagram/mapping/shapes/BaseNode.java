package de.fxdiagram.mapping.shapes;

import com.google.common.collect.Iterables;
import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.XLabel;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.anchors.Anchors;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.lib.anchors.RoundedRectangleAnchors;
import de.fxdiagram.lib.nodes.RectangleBorderPane;
import de.fxdiagram.mapping.ConnectionMapping;
import de.fxdiagram.mapping.IMappedElementDescriptor;
import de.fxdiagram.mapping.behavior.DefaultReconcileBehavior;
import de.fxdiagram.mapping.behavior.LazyConnectionMappingBehavior;
import de.fxdiagram.mapping.shapes.BaseShapeInitializer;
import de.fxdiagram.mapping.shapes.INodeWithLazyMappings;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

/**
 * Base implementation for an {@link XNode} that belongs to an {@link IMappedElementDescriptor}.
 * 
 * If the descriptor is an {@link AbstractXtextDescriptor}, members are automatically injected using
 * the Xtext language's injector.
 */
@ModelNode
@SuppressWarnings("all")
public class BaseNode<T extends Object> extends XNode implements INodeWithLazyMappings {
  public BaseNode() {
    BaseShapeInitializer.initializeLazily(this);
  }
  
  public BaseNode(final IMappedElementDescriptor<T> descriptor) {
    super(descriptor);
  }
  
  @Override
  public IMappedElementDescriptor<T> getDomainObjectDescriptor() {
    DomainObjectDescriptor _domainObjectDescriptor = super.getDomainObjectDescriptor();
    return ((IMappedElementDescriptor<T>) _domainObjectDescriptor);
  }
  
  @Override
  protected Node createNode() {
    RectangleBorderPane _rectangleBorderPane = new RectangleBorderPane();
    final Procedure1<RectangleBorderPane> _function = (RectangleBorderPane it) -> {
      ObservableList<XLabel> _labels = this.getLabels();
      boolean _isEmpty = _labels.isEmpty();
      boolean _not = (!_isEmpty);
      if (_not) {
        ObservableList<Node> _children = it.getChildren();
        ObservableList<XLabel> _labels_1 = this.getLabels();
        Iterables.<Node>addAll(_children, _labels_1);
        ObservableList<XLabel> _labels_2 = this.getLabels();
        final Consumer<XLabel> _function_1 = (XLabel it_1) -> {
          it_1.getNode();
        };
        _labels_2.forEach(_function_1);
        this.styleLabels();
      }
      Color _rgb = Color.rgb(158, 188, 227);
      Stop _stop = new Stop(0, _rgb);
      Color _rgb_1 = Color.rgb(220, 230, 255);
      Stop _stop_1 = new Stop(1, _rgb_1);
      LinearGradient _linearGradient = new LinearGradient(
        0, 0, 1, 1, 
        true, CycleMethod.NO_CYCLE, 
        Collections.<Stop>unmodifiableList(CollectionLiterals.<Stop>newArrayList(_stop, _stop_1)));
      it.setBackgroundPaint(_linearGradient);
    };
    return ObjectExtensions.<RectangleBorderPane>operator_doubleArrow(_rectangleBorderPane, _function);
  }
  
  protected void styleLabels() {
    ObservableList<XLabel> _labels = this.getLabels();
    XLabel _head = IterableExtensions.<XLabel>head(_labels);
    Insets _insets = new Insets(10, 20, 10, 20);
    StackPane.setMargin(_head, _insets);
    ObservableList<XLabel> _labels_1 = this.getLabels();
    XLabel _head_1 = IterableExtensions.<XLabel>head(_labels_1);
    Text _text = _head_1.getText();
    final Font font = _text.getFont();
    ObservableList<XLabel> _labels_2 = this.getLabels();
    XLabel _head_2 = IterableExtensions.<XLabel>head(_labels_2);
    Text _text_1 = _head_2.getText();
    String _family = font.getFamily();
    double _size = font.getSize();
    double _multiply = (_size * 1.1);
    Font _font = Font.font(_family, FontWeight.BOLD, _multiply);
    _text_1.setFont(_font);
  }
  
  @Override
  protected Anchors createAnchors() {
    return new RoundedRectangleAnchors(this, 6, 6);
  }
  
  @Override
  public void doActivate() {
    super.doActivate();
    IMappedElementDescriptor<T> _domainObjectDescriptor = this.getDomainObjectDescriptor();
    LazyConnectionMappingBehavior.<T>addLazyBehavior(this, _domainObjectDescriptor);
    DefaultReconcileBehavior<BaseNode<T>> _defaultReconcileBehavior = new DefaultReconcileBehavior<BaseNode<T>>(this);
    this.addBehavior(_defaultReconcileBehavior);
  }
  
  @Override
  public List<Side> getButtonSides(final ConnectionMapping<?> mapping) {
    return Collections.<Side>unmodifiableList(CollectionLiterals.<Side>newArrayList(Side.TOP, Side.BOTTOM, Side.LEFT, Side.RIGHT));
  }
  
  public void populate(final ModelElementImpl modelElement) {
    super.populate(modelElement);
  }
}
