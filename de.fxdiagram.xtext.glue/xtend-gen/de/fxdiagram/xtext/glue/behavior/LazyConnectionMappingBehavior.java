package de.fxdiagram.xtext.glue.behavior;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRapidButton;
import de.fxdiagram.core.XRapidButtonAction;
import de.fxdiagram.core.behavior.AbstractHostBehavior;
import de.fxdiagram.core.behavior.Behavior;
import de.fxdiagram.core.extensions.ButtonExtensions;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.xtext.glue.behavior.LazyConnectionRapidButtonAction;
import de.fxdiagram.xtext.glue.mapping.AbstractConnectionMappingCall;
import de.fxdiagram.xtext.glue.mapping.XDiagramConfigInterpreter;
import java.util.Collections;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.geometry.Side;
import javafx.scene.shape.SVGPath;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;

@SuppressWarnings("all")
public class LazyConnectionMappingBehavior<MODEL extends Object, ARG extends Object> extends AbstractHostBehavior<XNode> {
  private List<XRapidButton> buttons = CollectionLiterals.<XRapidButton>newArrayList();
  
  private String tooltip;
  
  private XRapidButtonAction action;
  
  public LazyConnectionMappingBehavior(final XNode host, final AbstractConnectionMappingCall<MODEL, ARG> mappingCall, final XDiagramConfigInterpreter configInterpreter, final boolean hostIsSource) {
    super(host);
    String _role = mappingCall.getRole();
    String _plus = ("Add " + _role);
    this.tooltip = _plus;
    XRapidButtonAction _createAction = this.createAction(mappingCall, configInterpreter, hostIsSource);
    this.action = _createAction;
  }
  
  public Class<? extends Behavior> getBehaviorKey() {
    return this.getClass();
  }
  
  protected void doActivate() {
    Iterable<XRapidButton> _createButtons = this.createButtons();
    Iterables.<XRapidButton>addAll(this.buttons, _createButtons);
    XNode _host = this.getHost();
    XDiagram _diagram = CoreExtensions.getDiagram(_host);
    ObservableList<XRapidButton> _buttons = _diagram.getButtons();
    Iterables.<XRapidButton>addAll(_buttons, this.buttons);
  }
  
  protected XRapidButtonAction createAction(final AbstractConnectionMappingCall<MODEL, ARG> mappingCall, final XDiagramConfigInterpreter configInterpreter, final boolean hostIsSource) {
    return new LazyConnectionRapidButtonAction<MODEL, ARG>(mappingCall, configInterpreter, hostIsSource);
  }
  
  protected Iterable<XRapidButton> createButtons() {
    XNode _host = this.getHost();
    SVGPath _triangleButton = ButtonExtensions.getTriangleButton(Side.LEFT, this.tooltip);
    XRapidButton _xRapidButton = new XRapidButton(_host, 0, 0.5, _triangleButton, this.action);
    XNode _host_1 = this.getHost();
    SVGPath _triangleButton_1 = ButtonExtensions.getTriangleButton(Side.RIGHT, this.tooltip);
    XRapidButton _xRapidButton_1 = new XRapidButton(_host_1, 1, 0.5, _triangleButton_1, this.action);
    XNode _host_2 = this.getHost();
    SVGPath _triangleButton_2 = ButtonExtensions.getTriangleButton(Side.TOP, this.tooltip);
    XRapidButton _xRapidButton_2 = new XRapidButton(_host_2, 0.5, 0, _triangleButton_2, this.action);
    XNode _host_3 = this.getHost();
    SVGPath _triangleButton_3 = ButtonExtensions.getTriangleButton(Side.BOTTOM, this.tooltip);
    XRapidButton _xRapidButton_3 = new XRapidButton(_host_3, 0.5, 1, _triangleButton_3, this.action);
    return Collections.<XRapidButton>unmodifiableList(Lists.<XRapidButton>newArrayList(_xRapidButton, _xRapidButton_1, _xRapidButton_2, _xRapidButton_3));
  }
}
