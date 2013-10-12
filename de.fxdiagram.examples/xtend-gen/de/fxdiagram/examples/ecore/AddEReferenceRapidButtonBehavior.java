package de.fxdiagram.examples.ecore;

import com.google.common.base.Objects;
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
import de.fxdiagram.core.behavior.AbstractBehavior;
import de.fxdiagram.core.extensions.ButtonExtensions;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.tools.ChooserConnectionProvider;
import de.fxdiagram.examples.ecore.EClassNode;
import de.fxdiagram.lib.tools.CarusselChooser;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javafx.collections.ListChangeListener;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class AddEReferenceRapidButtonBehavior extends AbstractBehavior<EClassNode> {
  private List<XRapidButton> buttons;
  
  private Set<EReference> availableReferences = new Function0<Set<EReference>>() {
    public Set<EReference> apply() {
      HashSet<EReference> _newHashSet = CollectionLiterals.<EReference>newHashSet();
      return _newHashSet;
    }
  }.apply();
  
  public AddEReferenceRapidButtonBehavior(final EClassNode host) {
    super(host);
  }
  
  protected void doActivate() {
    EClassNode _host = this.getHost();
    EClass _eClass = _host.getEClass();
    EList<EReference> _eReferences = _eClass.getEReferences();
    Iterables.<EReference>addAll(this.availableReferences, _eReferences);
    boolean _isEmpty = this.availableReferences.isEmpty();
    boolean _not = (!_isEmpty);
    if (_not) {
      final Procedure1<XRapidButton> _function = new Procedure1<XRapidButton>() {
        public void apply(final XRapidButton button) {
          AddEReferenceRapidButtonBehavior.this.createChooser(button);
        }
      };
      final Procedure1<XRapidButton> addReferenceAction = _function;
      List<XRapidButton> _createButtons = this.createButtons(addReferenceAction);
      this.buttons = _createButtons;
      EClassNode _host_1 = this.getHost();
      XDiagram _diagram = CoreExtensions.getDiagram(_host_1);
      ObservableList<XRapidButton> _buttons = _diagram.getButtons();
      Iterables.<XRapidButton>addAll(_buttons, this.buttons);
      EClassNode _host_2 = this.getHost();
      XDiagram _diagram_1 = CoreExtensions.getDiagram(_host_2);
      ObservableList<XConnection> _connections = _diagram_1.getConnections();
      final ListChangeListener<XConnection> _function_1 = new ListChangeListener<XConnection>() {
        public void onChanged(final Change<? extends XConnection> change) {
          boolean _next = change.next();
          boolean _while = _next;
          while (_while) {
            boolean _wasAdded = change.wasAdded();
            if (_wasAdded) {
              List<? extends XConnection> _addedSubList = change.getAddedSubList();
              final Procedure1<XConnection> _function = new Procedure1<XConnection>() {
                public void apply(final XConnection it) {
                  Object _key = it.getKey();
                  AddEReferenceRapidButtonBehavior.this.availableReferences.remove(_key);
                }
              };
              IterableExtensions.forEach(_addedSubList, _function);
            }
            boolean _next_1 = change.next();
            _while = _next_1;
          }
          boolean _isEmpty = AddEReferenceRapidButtonBehavior.this.availableReferences.isEmpty();
          if (_isEmpty) {
            EClassNode _host = AddEReferenceRapidButtonBehavior.this.getHost();
            XDiagram _diagram = CoreExtensions.getDiagram(_host);
            ObservableList<XRapidButton> _buttons = _diagram.getButtons();
            Iterables.removeAll(_buttons, AddEReferenceRapidButtonBehavior.this.buttons);
          }
        }
      };
      _connections.addListener(_function_1);
    }
  }
  
  protected List<XRapidButton> createButtons(final Procedure1<? super XRapidButton> addReferencesAction) {
    EClassNode _host = this.getHost();
    SVGPath _arrowButton = ButtonExtensions.getArrowButton(Side.LEFT, "Discover properties");
    XRapidButton _xRapidButton = new XRapidButton(_host, 0, 0.5, _arrowButton, addReferencesAction);
    EClassNode _host_1 = this.getHost();
    SVGPath _arrowButton_1 = ButtonExtensions.getArrowButton(Side.RIGHT, "Discover properties");
    XRapidButton _xRapidButton_1 = new XRapidButton(_host_1, 1, 0.5, _arrowButton_1, addReferencesAction);
    return Collections.<XRapidButton>unmodifiableList(Lists.<XRapidButton>newArrayList(_xRapidButton, _xRapidButton_1));
  }
  
  protected void createChooser(final XRapidButton button) {
    EClassNode _host = this.getHost();
    Pos _chooserPosition = button.getChooserPosition();
    CarusselChooser _carusselChooser = new CarusselChooser(_host, _chooserPosition);
    final CarusselChooser chooser = _carusselChooser;
    final Procedure1<EReference> _function = new Procedure1<EReference>() {
      public void apply(final EReference it) {
        EClass _eReferenceType = it.getEReferenceType();
        EClassNode _eClassNode = new EClassNode(_eReferenceType);
        chooser.addChoice(_eClassNode, it);
      }
    };
    IterableExtensions.<EReference>forEach(this.availableReferences, _function);
    final ChooserConnectionProvider _function_1 = new ChooserConnectionProvider() {
      public XConnection getConnection(final XNode host, final XNode choice, final Object choiceInfo) {
        XConnection _xblockexpression = null;
        {
          final EReference reference = ((EReference) choiceInfo);
          XConnection _xConnection = new XConnection(host, choice, reference);
          final Procedure1<XConnection> _function = new Procedure1<XConnection>() {
            public void apply(final XConnection it) {
              ArrowHead _xifexpression = null;
              boolean _isContainer = reference.isContainer();
              if (_isContainer) {
                DiamondArrowHead _diamondArrowHead = new DiamondArrowHead(it, false);
                _xifexpression = _diamondArrowHead;
              } else {
                LineArrowHead _lineArrowHead = new LineArrowHead(it, false);
                _xifexpression = _lineArrowHead;
              }
              it.setTargetArrowHead(_xifexpression);
              ArrowHead _xifexpression_1 = null;
              boolean _isContainment = reference.isContainment();
              if (_isContainment) {
                DiamondArrowHead _diamondArrowHead_1 = new DiamondArrowHead(it, true);
                _xifexpression_1 = _diamondArrowHead_1;
              } else {
                LineArrowHead _xifexpression_2 = null;
                boolean _and = false;
                boolean _isContainer_1 = reference.isContainer();
                boolean _not = (!_isContainer_1);
                if (!_not) {
                  _and = false;
                } else {
                  EReference _eOpposite = reference.getEOpposite();
                  boolean _notEquals = (!Objects.equal(_eOpposite, null));
                  _and = (_not && _notEquals);
                }
                if (_and) {
                  LineArrowHead _lineArrowHead_1 = new LineArrowHead(it, true);
                  _xifexpression_2 = _lineArrowHead_1;
                }
                _xifexpression_1 = _xifexpression_2;
              }
              it.setSourceArrowHead(_xifexpression_1);
              XConnectionLabel _xConnectionLabel = new XConnectionLabel(it);
              final Procedure1<XConnectionLabel> _function = new Procedure1<XConnectionLabel>() {
                public void apply(final XConnectionLabel it) {
                  Text _text = it.getText();
                  String _name = reference.getName();
                  _text.setText(_name);
                  it.setPosition(0.8);
                }
              };
              ObjectExtensions.<XConnectionLabel>operator_doubleArrow(_xConnectionLabel, _function);
              EReference _eOpposite_1 = reference.getEOpposite();
              boolean _notEquals_1 = (!Objects.equal(_eOpposite_1, null));
              if (_notEquals_1) {
                XConnectionLabel _xConnectionLabel_1 = new XConnectionLabel(it);
                final Procedure1<XConnectionLabel> _function_1 = new Procedure1<XConnectionLabel>() {
                  public void apply(final XConnectionLabel it) {
                    Text _text = it.getText();
                    EReference _eOpposite = reference.getEOpposite();
                    String _name = _eOpposite.getName();
                    _text.setText(_name);
                    it.setPosition(0.2);
                  }
                };
                ObjectExtensions.<XConnectionLabel>operator_doubleArrow(_xConnectionLabel_1, _function_1);
              }
            }
          };
          XConnection _doubleArrow = ObjectExtensions.<XConnection>operator_doubleArrow(_xConnection, _function);
          _xblockexpression = (_doubleArrow);
        }
        return _xblockexpression;
      }
    };
    chooser.setConnectionProvider(_function_1);
    EClassNode _host_1 = this.getHost();
    XRoot _root = CoreExtensions.getRoot(_host_1);
    _root.setCurrentTool(chooser);
  }
}
