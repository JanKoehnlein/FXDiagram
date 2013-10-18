package de.fxdiagram.examples.java;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRapidButton;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.anchors.TriangleArrowHead;
import de.fxdiagram.core.behavior.AbstractHostBehavior;
import de.fxdiagram.core.behavior.Behavior;
import de.fxdiagram.core.extensions.ButtonExtensions;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.tools.ChooserConnectionProvider;
import de.fxdiagram.examples.java.JavaTypeModel;
import de.fxdiagram.examples.java.JavaTypeNode;
import de.fxdiagram.examples.java.SuperTypeKey;
import de.fxdiagram.lib.tools.CoverFlowChooser;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.paint.Paint;
import javafx.scene.shape.SVGPath;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class AddSuperTypeRapidButtonBehavior extends AbstractHostBehavior<JavaTypeNode> {
  private List<XRapidButton> buttons;
  
  private Set<SuperTypeKey> availableKeys = new Function0<Set<SuperTypeKey>>() {
    public Set<SuperTypeKey> apply() {
      LinkedHashSet<SuperTypeKey> _newLinkedHashSet = CollectionLiterals.<SuperTypeKey>newLinkedHashSet();
      return _newLinkedHashSet;
    }
  }.apply();
  
  private Set<SuperTypeKey> unavailableKeys = new Function0<Set<SuperTypeKey>>() {
    public Set<SuperTypeKey> apply() {
      HashSet<SuperTypeKey> _newHashSet = CollectionLiterals.<SuperTypeKey>newHashSet();
      return _newHashSet;
    }
  }.apply();
  
  public AddSuperTypeRapidButtonBehavior(final JavaTypeNode host) {
    super(host);
  }
  
  public Class<? extends Behavior> getBehaviorKey() {
    return AddSuperTypeRapidButtonBehavior.class;
  }
  
  protected void doActivate() {
    JavaTypeNode _host = this.getHost();
    JavaTypeModel _javaTypeModel = _host.getJavaTypeModel();
    List<Class<? extends Object>> _superTypes = _javaTypeModel.getSuperTypes();
    final Function1<Class<? extends Object>,SuperTypeKey> _function = new Function1<Class<? extends Object>,SuperTypeKey>() {
      public SuperTypeKey apply(final Class<? extends Object> it) {
        SuperTypeKey _key = AddSuperTypeRapidButtonBehavior.this.getKey(it);
        return _key;
      }
    };
    List<SuperTypeKey> _map = ListExtensions.<Class<? extends Object>, SuperTypeKey>map(_superTypes, _function);
    Iterables.<SuperTypeKey>addAll(this.availableKeys, _map);
    boolean _isEmpty = this.availableKeys.isEmpty();
    boolean _not = (!_isEmpty);
    if (_not) {
      final Procedure1<XRapidButton> _function_1 = new Procedure1<XRapidButton>() {
        public void apply(final XRapidButton button) {
          AddSuperTypeRapidButtonBehavior.this.createChooser(button);
        }
      };
      final Procedure1<XRapidButton> addConnectionAction = _function_1;
      List<XRapidButton> _createButtons = this.createButtons(addConnectionAction);
      this.buttons = _createButtons;
      JavaTypeNode _host_1 = this.getHost();
      XDiagram _diagram = CoreExtensions.getDiagram(_host_1);
      ObservableList<XRapidButton> _buttons = _diagram.getButtons();
      Iterables.<XRapidButton>addAll(_buttons, this.buttons);
      JavaTypeNode _host_2 = this.getHost();
      XDiagram _diagram_1 = CoreExtensions.getDiagram(_host_2);
      ObservableList<XConnection> _connections = _diagram_1.getConnections();
      final ListChangeListener<XConnection> _function_2 = new ListChangeListener<XConnection>() {
        public void onChanged(final Change<? extends XConnection> change) {
          boolean _next = change.next();
          boolean _while = _next;
          while (_while) {
            {
              boolean _wasAdded = change.wasAdded();
              if (_wasAdded) {
                List<? extends XConnection> _addedSubList = change.getAddedSubList();
                final Procedure1<XConnection> _function = new Procedure1<XConnection>() {
                  public void apply(final XConnection it) {
                    Object _key = it.getKey();
                    boolean _remove = AddSuperTypeRapidButtonBehavior.this.availableKeys.remove(_key);
                    if (_remove) {
                      Object _key_1 = it.getKey();
                      AddSuperTypeRapidButtonBehavior.this.unavailableKeys.add(((SuperTypeKey) _key_1));
                    }
                  }
                };
                IterableExtensions.forEach(_addedSubList, _function);
              }
              boolean _wasRemoved = change.wasRemoved();
              if (_wasRemoved) {
                List<? extends XConnection> _removed = change.getRemoved();
                final Procedure1<XConnection> _function_1 = new Procedure1<XConnection>() {
                  public void apply(final XConnection it) {
                    Object _key = it.getKey();
                    boolean _remove = AddSuperTypeRapidButtonBehavior.this.unavailableKeys.remove(_key);
                    if (_remove) {
                      Object _key_1 = it.getKey();
                      AddSuperTypeRapidButtonBehavior.this.availableKeys.add(((SuperTypeKey) _key_1));
                    }
                  }
                };
                IterableExtensions.forEach(_removed, _function_1);
              }
            }
            boolean _next_1 = change.next();
            _while = _next_1;
          }
          boolean _isEmpty = AddSuperTypeRapidButtonBehavior.this.availableKeys.isEmpty();
          if (_isEmpty) {
            JavaTypeNode _host = AddSuperTypeRapidButtonBehavior.this.getHost();
            XDiagram _diagram = CoreExtensions.getDiagram(_host);
            ObservableList<XRapidButton> _buttons = _diagram.getButtons();
            Iterables.removeAll(_buttons, AddSuperTypeRapidButtonBehavior.this.buttons);
          }
        }
      };
      _connections.addListener(_function_2);
    }
  }
  
  protected SuperTypeKey getKey(final Class<? extends Object> superType) {
    JavaTypeNode _host = this.getHost();
    Class<? extends Object> _javaType = ((JavaTypeNode) _host).getJavaType();
    SuperTypeKey _superTypeKey = new SuperTypeKey(_javaType, superType);
    return _superTypeKey;
  }
  
  protected List<XRapidButton> createButtons(final Procedure1<? super XRapidButton> addSuperTypeAction) {
    JavaTypeNode _host = this.getHost();
    SVGPath _triangleButton = ButtonExtensions.getTriangleButton(Side.TOP, "Discover supertypes");
    XRapidButton _xRapidButton = new XRapidButton(_host, 0.5, 0, _triangleButton, addSuperTypeAction);
    JavaTypeNode _host_1 = this.getHost();
    SVGPath _triangleButton_1 = ButtonExtensions.getTriangleButton(Side.BOTTOM, "Discover supertypes");
    XRapidButton _xRapidButton_1 = new XRapidButton(_host_1, 0.5, 1, _triangleButton_1, addSuperTypeAction);
    return Collections.<XRapidButton>unmodifiableList(Lists.<XRapidButton>newArrayList(_xRapidButton, _xRapidButton_1));
  }
  
  protected void createChooser(final XRapidButton button) {
    JavaTypeNode _host = this.getHost();
    Pos _chooserPosition = button.getChooserPosition();
    CoverFlowChooser _coverFlowChooser = new CoverFlowChooser(_host, _chooserPosition);
    final CoverFlowChooser chooser = _coverFlowChooser;
    final Procedure1<SuperTypeKey> _function = new Procedure1<SuperTypeKey>() {
      public void apply(final SuperTypeKey it) {
        Class<? extends Object> _superType = it.getSuperType();
        JavaTypeNode _javaTypeNode = new JavaTypeNode(_superType);
        chooser.addChoice(_javaTypeNode, it);
      }
    };
    IterableExtensions.<SuperTypeKey>forEach(this.availableKeys, _function);
    final ChooserConnectionProvider _function_1 = new ChooserConnectionProvider() {
      public XConnection getConnection(final XNode host, final XNode choice, final Object choiceInfo) {
        Class<? extends Object> _javaType = ((JavaTypeNode) choice).getJavaType();
        SuperTypeKey _key = AddSuperTypeRapidButtonBehavior.this.getKey(_javaType);
        XConnection _xConnection = new XConnection(host, choice, _key);
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
    JavaTypeNode _host_1 = this.getHost();
    XRoot _root = CoreExtensions.getRoot(_host_1);
    _root.setCurrentTool(chooser);
  }
}
