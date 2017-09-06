package de.fxdiagram.examples.ecore;

import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.anchors.Anchors;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.core.model.ToString;
import de.fxdiagram.examples.ecore.AddEReferenceRapidButtonBehavior;
import de.fxdiagram.examples.ecore.AddESuperTypeRapidButtonBehavior;
import de.fxdiagram.examples.ecore.EClassDescriptor;
import de.fxdiagram.lib.anchors.RoundedRectangleAnchors;
import de.fxdiagram.lib.animations.Inflator;
import de.fxdiagram.lib.nodes.RectangleBorderPane;
import java.util.List;
import java.util.function.Consumer;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@ModelNode
@SuppressWarnings("all")
public class EClassNode extends XNode {
  private final VBox attributeCompartment = new VBox();
  
  private final VBox operationCompartment = new VBox();
  
  private final VBox contentArea = new VBox();
  
  public EClassNode(final EClassDescriptor domainObject) {
    super(domainObject);
    this.setPlacementHint(Side.BOTTOM);
  }
  
  public EClass getEClass() {
    DomainObjectDescriptor _domainObjectDescriptor = this.getDomainObjectDescriptor();
    return ((EClassDescriptor) _domainObjectDescriptor).getDomainObject();
  }
  
  @Override
  protected Node createNode() {
    RectangleBorderPane _rectangleBorderPane = new RectangleBorderPane();
    final Procedure1<RectangleBorderPane> _function = (RectangleBorderPane it) -> {
      ObservableList<Node> _children = it.getChildren();
      final Procedure1<VBox> _function_1 = (VBox it_1) -> {
        Insets _insets = new Insets(15, 20, 15, 20);
        it_1.setPadding(_insets);
        ObservableList<Node> _children_1 = it_1.getChildren();
        Text _text = new Text();
        final Procedure1<Text> _function_2 = (Text it_2) -> {
          it_2.setText(this.getEClass().getName());
          it_2.setTextOrigin(VPos.TOP);
          String _family = it_2.getFont().getFamily();
          double _size = it_2.getFont().getSize();
          double _multiply = (_size * 1.1);
          it_2.setFont(Font.font(_family, FontWeight.BOLD, _multiply));
        };
        Text _doubleArrow = ObjectExtensions.<Text>operator_doubleArrow(_text, _function_2);
        _children_1.add(_doubleArrow);
        it_1.setAlignment(Pos.CENTER);
      };
      VBox _doubleArrow = ObjectExtensions.<VBox>operator_doubleArrow(this.contentArea, _function_1);
      _children.add(_doubleArrow);
    };
    return ObjectExtensions.<RectangleBorderPane>operator_doubleArrow(_rectangleBorderPane, _function);
  }
  
  @Override
  protected Anchors createAnchors() {
    return new RoundedRectangleAnchors(this, 12, 12);
  }
  
  public Object populateCompartments() {
    Object _xblockexpression = null;
    {
      final Consumer<EAttribute> _function = (EAttribute attribute) -> {
        ObservableList<Node> _children = this.attributeCompartment.getChildren();
        Text _text = new Text();
        final Procedure1<Text> _function_1 = (Text it) -> {
          StringConcatenation _builder = new StringConcatenation();
          String _name = attribute.getName();
          _builder.append(_name);
          _builder.append(": ");
          String _name_1 = attribute.getEType().getName();
          _builder.append(_name_1);
          it.setText(_builder.toString());
        };
        Text _doubleArrow = ObjectExtensions.<Text>operator_doubleArrow(_text, _function_1);
        _children.add(_doubleArrow);
      };
      this.<EAttribute>limit(this.getEClass().getEAttributes()).forEach(_function);
      final Consumer<EOperation> _function_1 = (EOperation operation) -> {
        ObservableList<Node> _children = this.operationCompartment.getChildren();
        Text _text = new Text();
        final Procedure1<Text> _function_2 = (Text it) -> {
          StringConcatenation _builder = new StringConcatenation();
          String _name = operation.getName();
          _builder.append(_name);
          _builder.append("(");
          final Function1<EParameter, String> _function_3 = (EParameter it_1) -> {
            return it_1.getEType().getName();
          };
          String _join = IterableExtensions.join(ListExtensions.<EParameter, String>map(operation.getEParameters(), _function_3), ", ");
          _builder.append(_join);
          _builder.append("): ");
          String _name_1 = operation.getEType().getName();
          _builder.append(_name_1);
          it.setText(_builder.toString());
        };
        Text _doubleArrow = ObjectExtensions.<Text>operator_doubleArrow(_text, _function_2);
        _children.add(_doubleArrow);
      };
      this.<EOperation>limit(this.getEClass().getEOperations()).forEach(_function_1);
      _xblockexpression = null;
    }
    return _xblockexpression;
  }
  
  protected <T extends Object> List<T> limit(final List<T> list) {
    List<T> _xifexpression = null;
    boolean _isEmpty = list.isEmpty();
    if (_isEmpty) {
      _xifexpression = list;
    } else {
      List<T> _xifexpression_1 = null;
      boolean _isActive = this.getIsActive();
      if (_isActive) {
        _xifexpression_1 = list;
      } else {
        _xifexpression_1 = list.subList(0, Math.min(list.size(), 4));
      }
      _xifexpression = _xifexpression_1;
    }
    return _xifexpression;
  }
  
  @Override
  public void doActivate() {
    super.doActivate();
    Insets _insets = new Insets(15, 0, 0, 0);
    this.attributeCompartment.setPadding(_insets);
    Insets _insets_1 = new Insets(10, 0, 0, 0);
    this.operationCompartment.setPadding(_insets_1);
    this.populateCompartments();
    final Inflator inflator = new Inflator(this, this.contentArea);
    inflator.addInflatable(this.attributeCompartment, 1);
    inflator.addInflatable(this.operationCompartment, 2);
    AddESuperTypeRapidButtonBehavior _addESuperTypeRapidButtonBehavior = new AddESuperTypeRapidButtonBehavior(this);
    this.addBehavior(_addESuperTypeRapidButtonBehavior);
    AddEReferenceRapidButtonBehavior _addEReferenceRapidButtonBehavior = new AddEReferenceRapidButtonBehavior(this);
    this.addBehavior(_addEReferenceRapidButtonBehavior);
    CoreExtensions.getRoot(this).getCommandStack().execute(inflator.getInflateCommand());
  }
  
  /**
   * Automatically generated by @ModelNode. Needed for deserialization.
   */
  public EClassNode() {
  }
  
  public void populate(final ModelElementImpl modelElement) {
    super.populate(modelElement);
  }
  
  public void postLoad() {
    
  }
  
  public String toString() {
    return ToString.toString(this);
  }
}
