package de.fxdiagram.examples.java;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XConnectionLabel;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRapidButton;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.anchors.LineArrowHead;
import de.fxdiagram.core.behavior.AbstractBehavior;
import de.fxdiagram.core.extensions.ButtonExtensions;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.tools.ChooserConnectionProvider;
import de.fxdiagram.examples.java.JavaTypeModel;
import de.fxdiagram.examples.java.JavaTypeNode;
import de.fxdiagram.examples.java.Property;
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
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class AddReferenceRapidButtonBehavior extends AbstractBehavior<JavaTypeNode> {
  private List<XRapidButton> buttons;
  
  private Set<Property> availableReferences = new Function0<Set<Property>>() {
    public Set<Property> apply() {
      HashSet<Property> _newHashSet = CollectionLiterals.<Property>newHashSet();
      return _newHashSet;
    }
  }.apply();
  
  public AddReferenceRapidButtonBehavior(final JavaTypeNode host) {
    super(host);
  }
  
  protected void doActivate() {
    JavaTypeNode _host = this.getHost();
    final JavaTypeModel model = _host.getJavaTypeModel();
    List<Property> _references = model.getReferences();
    Iterables.<Property>addAll(this.availableReferences, _references);
    boolean _isEmpty = this.availableReferences.isEmpty();
    boolean _not = (!_isEmpty);
    if (_not) {
      final Procedure1<XRapidButton> _function = new Procedure1<XRapidButton>() {
        public void apply(final XRapidButton button) {
          AddReferenceRapidButtonBehavior.this.createChooser(button);
        }
      };
      final Procedure1<XRapidButton> addSuperTypeAction = _function;
      List<XRapidButton> _createButtons = this.createButtons(addSuperTypeAction);
      this.buttons = _createButtons;
      JavaTypeNode _host_1 = this.getHost();
      XDiagram _diagram = CoreExtensions.getDiagram(_host_1);
      ObservableList<XRapidButton> _buttons = _diagram.getButtons();
      Iterables.<XRapidButton>addAll(_buttons, this.buttons);
      JavaTypeNode _host_2 = this.getHost();
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
                  AddReferenceRapidButtonBehavior.this.availableReferences.remove(_key);
                }
              };
              IterableExtensions.forEach(_addedSubList, _function);
            }
            boolean _next_1 = change.next();
            _while = _next_1;
          }
          boolean _isEmpty = AddReferenceRapidButtonBehavior.this.availableReferences.isEmpty();
          if (_isEmpty) {
            JavaTypeNode _host = AddReferenceRapidButtonBehavior.this.getHost();
            XDiagram _diagram = CoreExtensions.getDiagram(_host);
            ObservableList<XRapidButton> _buttons = _diagram.getButtons();
            Iterables.removeAll(_buttons, AddReferenceRapidButtonBehavior.this.buttons);
          }
        }
      };
      _connections.addListener(_function_1);
    }
  }
  
  protected List<XRapidButton> createButtons(final Procedure1<? super XRapidButton> addReferencesAction) {
    JavaTypeNode _host = this.getHost();
    SVGPath _arrowButton = ButtonExtensions.getArrowButton(Side.LEFT, "Discover properties");
    XRapidButton _xRapidButton = new XRapidButton(_host, 0, 0.5, _arrowButton, addReferencesAction);
    JavaTypeNode _host_1 = this.getHost();
    SVGPath _arrowButton_1 = ButtonExtensions.getArrowButton(Side.RIGHT, "Discover properties");
    XRapidButton _xRapidButton_1 = new XRapidButton(_host_1, 1, 0.5, _arrowButton_1, addReferencesAction);
    return Collections.<XRapidButton>unmodifiableList(Lists.<XRapidButton>newArrayList(_xRapidButton, _xRapidButton_1));
  }
  
  protected void createChooser(final XRapidButton button) {
    JavaTypeNode _host = this.getHost();
    Pos _chooserPosition = button.getChooserPosition();
    CarusselChooser _carusselChooser = new CarusselChooser(_host, _chooserPosition);
    final CarusselChooser chooser = _carusselChooser;
    final Procedure1<Property> _function = new Procedure1<Property>() {
      public void apply(final Property it) {
        Class<? extends Object> _type = it.getType();
        JavaTypeNode _javaTypeNode = new JavaTypeNode(_type);
        chooser.addChoice(_javaTypeNode, it);
      }
    };
    IterableExtensions.<Property>forEach(this.availableReferences, _function);
    final ChooserConnectionProvider _function_1 = new ChooserConnectionProvider() {
      public XConnection getConnection(final XNode host, final XNode choice, final Object choiceInfo) {
        XConnection _xblockexpression = null;
        {
          final Property reference = ((Property) choiceInfo);
          XConnection _xConnection = new XConnection(host, choice, reference);
          final Procedure1<XConnection> _function = new Procedure1<XConnection>() {
            public void apply(final XConnection it) {
              LineArrowHead _lineArrowHead = new LineArrowHead(it, false);
              it.setTargetArrowHead(_lineArrowHead);
              XConnectionLabel _xConnectionLabel = new XConnectionLabel(it);
              final Procedure1<XConnectionLabel> _function = new Procedure1<XConnectionLabel>() {
                public void apply(final XConnectionLabel it) {
                  Text _text = it.getText();
                  String _name = reference.getName();
                  _text.setText(_name);
                }
              };
              ObjectExtensions.<XConnectionLabel>operator_doubleArrow(_xConnectionLabel, _function);
            }
          };
          XConnection _doubleArrow = ObjectExtensions.<XConnection>operator_doubleArrow(_xConnection, _function);
          _xblockexpression = (_doubleArrow);
        }
        return _xblockexpression;
      }
    };
    chooser.setConnectionProvider(_function_1);
    JavaTypeNode _host_1 = this.getHost();
    XRoot _root = CoreExtensions.getRoot(_host_1);
    _root.setCurrentTool(chooser);
  }
}
