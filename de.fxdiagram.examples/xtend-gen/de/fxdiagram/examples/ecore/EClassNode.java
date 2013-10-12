package de.fxdiagram.examples.ecore;

import de.fxdiagram.core.XNode;
import de.fxdiagram.core.anchors.Anchors;
import de.fxdiagram.examples.ecore.AddEReferenceRapidButtonBehavior;
import de.fxdiagram.examples.ecore.AddESuperTypeRapidButtonBehavior;
import de.fxdiagram.lib.anchors.RoundedRectangleAnchors;
import de.fxdiagram.lib.nodes.RectangleBorderPane;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Separator;
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
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class EClassNode extends XNode {
  private EClass eClass;
  
  private final Text name = new Function0<Text>() {
    public Text apply() {
      Text _text = new Text();
      return _text;
    }
  }.apply();
  
  private final VBox attributeCompartment = new Function0<VBox>() {
    public VBox apply() {
      VBox _vBox = new VBox();
      return _vBox;
    }
  }.apply();
  
  private final VBox operationCompartment = new Function0<VBox>() {
    public VBox apply() {
      VBox _vBox = new VBox();
      return _vBox;
    }
  }.apply();
  
  public EClassNode(final EClass eClass) {
    super(new Function0<String>() {
      public String apply() {
        String _name = eClass.getName();
        return _name;
      }
    }.apply());
    this.eClass = eClass;
    String _name = eClass.getName();
    this.name.setText(_name);
    RectangleBorderPane _rectangleBorderPane = new RectangleBorderPane();
    final Procedure1<RectangleBorderPane> _function = new Procedure1<RectangleBorderPane>() {
      public void apply(final RectangleBorderPane it) {
        ObservableList<Node> _children = it.getChildren();
        VBox _vBox = new VBox();
        final Procedure1<VBox> _function = new Procedure1<VBox>() {
          public void apply(final VBox it) {
            ObservableList<Node> _children = it.getChildren();
            final Procedure1<Text> _function = new Procedure1<Text>() {
              public void apply(final Text it) {
                it.setTextOrigin(VPos.TOP);
                Font _font = it.getFont();
                String _family = _font.getFamily();
                Font _font_1 = it.getFont();
                double _size = _font_1.getSize();
                double _multiply = (_size * 1.1);
                Font _font_2 = Font.font(_family, FontWeight.BOLD, _multiply);
                it.setFont(_font_2);
                Insets _insets = new Insets(12, 12, 12, 12);
                VBox.setMargin(it, _insets);
              }
            };
            Text _doubleArrow = ObjectExtensions.<Text>operator_doubleArrow(EClassNode.this.name, _function);
            _children.add(_doubleArrow);
            ObservableList<Node> _children_1 = it.getChildren();
            Separator _separator = new Separator();
            _children_1.add(_separator);
            ObservableList<Node> _children_2 = it.getChildren();
            final Procedure1<VBox> _function_1 = new Procedure1<VBox>() {
              public void apply(final VBox it) {
                Insets _insets = new Insets(5, 10, 5, 10);
                VBox.setMargin(it, _insets);
              }
            };
            VBox _doubleArrow_1 = ObjectExtensions.<VBox>operator_doubleArrow(EClassNode.this.attributeCompartment, _function_1);
            _children_2.add(_doubleArrow_1);
            ObservableList<Node> _children_3 = it.getChildren();
            Separator _separator_1 = new Separator();
            _children_3.add(_separator_1);
            ObservableList<Node> _children_4 = it.getChildren();
            final Procedure1<VBox> _function_2 = new Procedure1<VBox>() {
              public void apply(final VBox it) {
                Insets _insets = new Insets(5, 10, 5, 10);
                VBox.setMargin(it, _insets);
              }
            };
            VBox _doubleArrow_2 = ObjectExtensions.<VBox>operator_doubleArrow(EClassNode.this.operationCompartment, _function_2);
            _children_4.add(_doubleArrow_2);
            it.setAlignment(Pos.CENTER);
          }
        };
        VBox _doubleArrow = ObjectExtensions.<VBox>operator_doubleArrow(_vBox, _function);
        _children.add(_doubleArrow);
      }
    };
    RectangleBorderPane _doubleArrow = ObjectExtensions.<RectangleBorderPane>operator_doubleArrow(_rectangleBorderPane, _function);
    this.setNode(_doubleArrow);
  }
  
  protected Anchors createAnchors() {
    RoundedRectangleAnchors _roundedRectangleAnchors = new RoundedRectangleAnchors(this, 12, 12);
    return _roundedRectangleAnchors;
  }
  
  public void populateCompartments() {
    EList<EAttribute> _eAttributes = this.eClass.getEAttributes();
    List<EAttribute> _limit = this.<EAttribute>limit(_eAttributes);
    final Procedure1<EAttribute> _function = new Procedure1<EAttribute>() {
      public void apply(final EAttribute attribute) {
        ObservableList<Node> _children = EClassNode.this.attributeCompartment.getChildren();
        Text _text = new Text();
        final Procedure1<Text> _function = new Procedure1<Text>() {
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
    IterableExtensions.<EAttribute>forEach(_limit, _function);
    EList<EOperation> _eOperations = this.eClass.getEOperations();
    List<EOperation> _limit_1 = this.<EOperation>limit(_eOperations);
    final Procedure1<EOperation> _function_1 = new Procedure1<EOperation>() {
      public void apply(final EOperation operation) {
        ObservableList<Node> _children = EClassNode.this.operationCompartment.getChildren();
        Text _text = new Text();
        final Procedure1<Text> _function = new Procedure1<Text>() {
          public void apply(final Text it) {
            StringConcatenation _builder = new StringConcatenation();
            String _name = operation.getName();
            _builder.append(_name, "");
            _builder.append("(");
            EList<EParameter> _eParameters = operation.getEParameters();
            final Function1<EParameter,String> _function = new Function1<EParameter,String>() {
              public String apply(final EParameter it) {
                EClassifier _eType = it.getEType();
                String _name = _eType.getName();
                return _name;
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
    IterableExtensions.<EOperation>forEach(_limit_1, _function_1);
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
        List<T> _subList = list.subList(0, _min);
        _xifexpression_1 = _subList;
      }
      _xifexpression = _xifexpression_1;
    }
    return _xifexpression;
  }
  
  public EClass getEClass() {
    return this.eClass;
  }
  
  public void activate() {
    super.activate();
    this.populateCompartments();
    AddESuperTypeRapidButtonBehavior _addESuperTypeRapidButtonBehavior = new AddESuperTypeRapidButtonBehavior(this);
    _addESuperTypeRapidButtonBehavior.activate();
    AddEReferenceRapidButtonBehavior _addEReferenceRapidButtonBehavior = new AddEReferenceRapidButtonBehavior(this);
    _addEReferenceRapidButtonBehavior.activate();
  }
}
