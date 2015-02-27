package de.fxdiagram.examples.ecore;

import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.anchors.Anchors;
import de.fxdiagram.core.command.AbstractAnimationCommand;
import de.fxdiagram.core.command.CommandStack;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.core.model.ModelElementImpl;
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
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
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
  }
  
  public EClass getEClass() {
    DomainObjectDescriptor _domainObject = this.getDomainObject();
    return ((EClassDescriptor) _domainObject).getDomainObject();
  }
  
  @Override
  protected Node createNode() {
    RectangleBorderPane _rectangleBorderPane = new RectangleBorderPane();
    final Procedure1<RectangleBorderPane> _function = new Procedure1<RectangleBorderPane>() {
      @Override
      public void apply(final RectangleBorderPane it) {
        ObservableList<Node> _children = it.getChildren();
        final Procedure1<VBox> _function = new Procedure1<VBox>() {
          @Override
          public void apply(final VBox it) {
            Insets _insets = new Insets(15, 20, 15, 20);
            it.setPadding(_insets);
            ObservableList<Node> _children = it.getChildren();
            Text _text = new Text();
            final Procedure1<Text> _function = new Procedure1<Text>() {
              @Override
              public void apply(final Text it) {
                EClass _eClass = EClassNode.this.getEClass();
                String _name = _eClass.getName();
                it.setText(_name);
                it.setTextOrigin(VPos.TOP);
                Font _font = it.getFont();
                String _family = _font.getFamily();
                Font _font_1 = it.getFont();
                double _size = _font_1.getSize();
                double _multiply = (_size * 1.1);
                Font _font_2 = Font.font(_family, FontWeight.BOLD, _multiply);
                it.setFont(_font_2);
              }
            };
            Text _doubleArrow = ObjectExtensions.<Text>operator_doubleArrow(_text, _function);
            _children.add(_doubleArrow);
            it.setAlignment(Pos.CENTER);
          }
        };
        VBox _doubleArrow = ObjectExtensions.<VBox>operator_doubleArrow(EClassNode.this.contentArea, _function);
        _children.add(_doubleArrow);
      }
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
      EClass _eClass = this.getEClass();
      EList<EAttribute> _eAttributes = _eClass.getEAttributes();
      List<EAttribute> _limit = this.<EAttribute>limit(_eAttributes);
      final Consumer<EAttribute> _function = new Consumer<EAttribute>() {
        @Override
        public void accept(final EAttribute attribute) {
          ObservableList<Node> _children = EClassNode.this.attributeCompartment.getChildren();
          Text _text = new Text();
          final Procedure1<Text> _function = new Procedure1<Text>() {
            @Override
            public void apply(final Text it) {
              StringConcatenation _builder = new StringConcatenation();
              String _name = attribute.getName();
              _builder.append(_name, "");
              _builder.append(": ");
              EClassifier _eType = attribute.getEType();
              String _name_1 = _eType.getName();
              _builder.append(_name_1, "");
              it.setText(_builder.toString());
            }
          };
          Text _doubleArrow = ObjectExtensions.<Text>operator_doubleArrow(_text, _function);
          _children.add(_doubleArrow);
        }
      };
      _limit.forEach(_function);
      EClass _eClass_1 = this.getEClass();
      EList<EOperation> _eOperations = _eClass_1.getEOperations();
      List<EOperation> _limit_1 = this.<EOperation>limit(_eOperations);
      final Consumer<EOperation> _function_1 = new Consumer<EOperation>() {
        @Override
        public void accept(final EOperation operation) {
          ObservableList<Node> _children = EClassNode.this.operationCompartment.getChildren();
          Text _text = new Text();
          final Procedure1<Text> _function = new Procedure1<Text>() {
            @Override
            public void apply(final Text it) {
              StringConcatenation _builder = new StringConcatenation();
              String _name = operation.getName();
              _builder.append(_name, "");
              _builder.append("(");
              EList<EParameter> _eParameters = operation.getEParameters();
              final Function1<EParameter, String> _function = new Function1<EParameter, String>() {
                @Override
                public String apply(final EParameter it) {
                  EClassifier _eType = it.getEType();
                  return _eType.getName();
                }
              };
              List<String> _map = ListExtensions.<EParameter, String>map(_eParameters, _function);
              String _join = IterableExtensions.join(_map, ", ");
              _builder.append(_join, "");
              _builder.append("): ");
              EClassifier _eType = operation.getEType();
              String _name_1 = _eType.getName();
              _builder.append(_name_1, "");
              it.setText(_builder.toString());
            }
          };
          Text _doubleArrow = ObjectExtensions.<Text>operator_doubleArrow(_text, _function);
          _children.add(_doubleArrow);
        }
      };
      _limit_1.forEach(_function_1);
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
        int _size = list.size();
        int _min = Math.min(_size, 4);
        _xifexpression_1 = list.subList(0, _min);
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
    XRoot _root = CoreExtensions.getRoot(this);
    CommandStack _commandStack = _root.getCommandStack();
    AbstractAnimationCommand _inflateCommand = inflator.getInflateCommand();
    _commandStack.execute(_inflateCommand);
  }
  
  /**
   * Automatically generated by @ModelNode. Needed for deserialization.
   */
  public EClassNode() {
  }
  
  public void populate(final ModelElementImpl modelElement) {
    super.populate(modelElement);
  }
}
