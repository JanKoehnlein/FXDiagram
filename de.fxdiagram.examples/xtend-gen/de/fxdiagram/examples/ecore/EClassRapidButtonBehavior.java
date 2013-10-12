package de.fxdiagram.examples.ecore;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XConnectionLabel;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRapidButton;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.anchors.ArrowHead;
import de.fxdiagram.core.anchors.DiamondArrowHead;
import de.fxdiagram.core.anchors.LineArrowHead;
import de.fxdiagram.core.anchors.TriangleArrowHead;
import de.fxdiagram.core.behavior.AbstractBehavior;
import de.fxdiagram.core.extensions.ButtonExtensions;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.tools.ChooserConnectionProvider;
import de.fxdiagram.examples.ecore.EClassNode;
import de.fxdiagram.lib.tools.CarusselChooser;
import de.fxdiagram.lib.tools.CoverFlowChooser;
import java.util.Collections;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.paint.Paint;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class EClassRapidButtonBehavior extends AbstractBehavior<EClassNode> {
  public EClassRapidButtonBehavior(final EClassNode host) {
    super(host);
  }
  
  protected void doActivate() {
    EClassNode _host = this.getHost();
    final EClass eClass = _host.getEClass();
    EList<EClass> _eSuperTypes = eClass.getESuperTypes();
    boolean _isEmpty = _eSuperTypes.isEmpty();
    boolean _not = (!_isEmpty);
    if (_not) {
      final Procedure1<XRapidButton> _function = new Procedure1<XRapidButton>() {
        public void apply(final XRapidButton button) {
          EClassNode _host = EClassRapidButtonBehavior.this.getHost();
          Pos _chooserPosition = button.getChooserPosition();
          CoverFlowChooser _coverFlowChooser = new CoverFlowChooser(_host, _chooserPosition);
          final CoverFlowChooser chooser = _coverFlowChooser;
          EList<EClass> _eSuperTypes = eClass.getESuperTypes();
          final Procedure1<EClass> _function = new Procedure1<EClass>() {
            public void apply(final EClass superType) {
              EClassNode _eClassNode = new EClassNode(superType);
              chooser.addChoice(_eClassNode);
            }
          };
          IterableExtensions.<EClass>forEach(_eSuperTypes, _function);
          final ChooserConnectionProvider _function_1 = new ChooserConnectionProvider() {
            public XConnection getConnection(final XNode host, final XNode choice, final Object choiceInfo) {
              XConnection _xConnection = new XConnection(host, choice);
              final Procedure1<XConnection> _function = new Procedure1<XConnection>() {
                public void apply(final XConnection it) {
                  ObjectProperty<Paint> _strokeProperty = it.strokeProperty();
                  XDiagram _diagram = CoreExtensions.getDiagram(host);
                  ObjectProperty<Paint> _backgroundPaintProperty = _diagram.backgroundPaintProperty();
                  TriangleArrowHead _triangleArrowHead = new TriangleArrowHead(it, 10, 15, _strokeProperty, _backgroundPaintProperty, false);
                  it.setTargetArrowHead(_triangleArrowHead);
                }
              };
              XConnection _doubleArrow = ObjectExtensions.<XConnection>operator_doubleArrow(_xConnection, _function);
              return _doubleArrow;
            }
          };
          chooser.setConnectionProvider(_function_1);
          EClassNode _host_1 = EClassRapidButtonBehavior.this.getHost();
          XRoot _root = CoreExtensions.getRoot(_host_1);
          _root.setCurrentTool(chooser);
        }
      };
      final Procedure1<XRapidButton> addSuperTypeAction = _function;
      EClassNode _host_1 = this.getHost();
      XDiagram _diagram = CoreExtensions.getDiagram(_host_1);
      ObservableList<XRapidButton> _buttons = _diagram.getButtons();
      EClassNode _host_2 = this.getHost();
      SVGPath _triangleButton = ButtonExtensions.getTriangleButton(Side.TOP, "Discover supertypes");
      XRapidButton _xRapidButton = new XRapidButton(_host_2, 0.5, 0, _triangleButton, addSuperTypeAction);
      EClassNode _host_3 = this.getHost();
      SVGPath _triangleButton_1 = ButtonExtensions.getTriangleButton(Side.BOTTOM, "Discover supertypes");
      XRapidButton _xRapidButton_1 = new XRapidButton(_host_3, 0.5, 1, _triangleButton_1, addSuperTypeAction);
      Iterables.<XRapidButton>addAll(_buttons, Collections.<XRapidButton>unmodifiableList(Lists.<XRapidButton>newArrayList(_xRapidButton, _xRapidButton_1)));
    }
    EList<EReference> _eReferences = eClass.getEReferences();
    boolean _isEmpty_1 = _eReferences.isEmpty();
    boolean _not_1 = (!_isEmpty_1);
    if (_not_1) {
      final Procedure1<XRapidButton> _function_1 = new Procedure1<XRapidButton>() {
        public void apply(final XRapidButton button) {
          EClassNode _host = EClassRapidButtonBehavior.this.getHost();
          Pos _chooserPosition = button.getChooserPosition();
          CarusselChooser _carusselChooser = new CarusselChooser(_host, _chooserPosition);
          final CarusselChooser chooser = _carusselChooser;
          EList<EReference> _eReferences = eClass.getEReferences();
          final Procedure1<EReference> _function = new Procedure1<EReference>() {
            public void apply(final EReference reference) {
              EClass _eReferenceType = reference.getEReferenceType();
              EClassNode _eClassNode = new EClassNode(_eReferenceType);
              chooser.addChoice(_eClassNode, reference);
            }
          };
          IterableExtensions.<EReference>forEach(_eReferences, _function);
          final ChooserConnectionProvider _function_1 = new ChooserConnectionProvider() {
            public XConnection getConnection(final XNode host, final XNode choice, final Object choiceInfo) {
              XConnection _xConnection = new XConnection(host, choice);
              final Procedure1<XConnection> _function = new Procedure1<XConnection>() {
                public void apply(final XConnection it) {
                  final EReference reference = ((EReference) choiceInfo);
                  ArrowHead _xifexpression = null;
                  boolean _isContainment = reference.isContainment();
                  if (_isContainment) {
                    DiamondArrowHead _diamondArrowHead = new DiamondArrowHead(it, false);
                    _xifexpression = _diamondArrowHead;
                  } else {
                    ObjectProperty<Paint> _strokeProperty = it.strokeProperty();
                    LineArrowHead _lineArrowHead = new LineArrowHead(it, 7, 10, _strokeProperty, false);
                    _xifexpression = _lineArrowHead;
                  }
                  it.setTargetArrowHead(_xifexpression);
                  XConnectionLabel _xConnectionLabel = new XConnectionLabel(it);
                  final Procedure1<XConnectionLabel> _function = new Procedure1<XConnectionLabel>() {
                    public void apply(final XConnectionLabel it) {
                      Text _text = it.getText();
                      String _name = reference.getName();
                      String _xifexpression = null;
                      boolean _isMany = reference.isMany();
                      if (_isMany) {
                        _xifexpression = " *";
                      } else {
                        _xifexpression = "";
                      }
                      String _plus = (_name + _xifexpression);
                      _text.setText(_plus);
                    }
                  };
                  ObjectExtensions.<XConnectionLabel>operator_doubleArrow(_xConnectionLabel, _function);
                }
              };
              XConnection _doubleArrow = ObjectExtensions.<XConnection>operator_doubleArrow(_xConnection, _function);
              return _doubleArrow;
            }
          };
          chooser.setConnectionProvider(_function_1);
          EClassNode _host_1 = EClassRapidButtonBehavior.this.getHost();
          XRoot _root = CoreExtensions.getRoot(_host_1);
          _root.setCurrentTool(chooser);
        }
      };
      final Procedure1<XRapidButton> addReferencesAction = _function_1;
      EClassNode _host_4 = this.getHost();
      XDiagram _diagram_1 = CoreExtensions.getDiagram(_host_4);
      ObservableList<XRapidButton> _buttons_1 = _diagram_1.getButtons();
      EClassNode _host_5 = this.getHost();
      SVGPath _arrowButton = ButtonExtensions.getArrowButton(Side.LEFT, "Discover referenes");
      XRapidButton _xRapidButton_2 = new XRapidButton(_host_5, 0, 0.5, _arrowButton, addReferencesAction);
      EClassNode _host_6 = this.getHost();
      SVGPath _arrowButton_1 = ButtonExtensions.getArrowButton(Side.RIGHT, "Discover references");
      XRapidButton _xRapidButton_3 = new XRapidButton(_host_6, 1, 0.5, _arrowButton_1, addReferencesAction);
      Iterables.<XRapidButton>addAll(_buttons_1, Collections.<XRapidButton>unmodifiableList(Lists.<XRapidButton>newArrayList(_xRapidButton_2, _xRapidButton_3)));
    }
  }
}
