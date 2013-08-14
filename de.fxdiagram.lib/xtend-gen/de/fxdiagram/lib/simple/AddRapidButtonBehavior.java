package de.fxdiagram.lib.simple;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import de.fxdiagram.core.Extensions;
import de.fxdiagram.core.Placer;
import de.fxdiagram.core.XAbstractDiagram;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRapidButton;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.XShape;
import de.fxdiagram.core.behavior.AbstractBehavior;
import de.fxdiagram.core.tools.AbstractXNodeChooser;
import de.fxdiagram.lib.simple.NestedDiagramNode;
import de.fxdiagram.lib.simple.SimpleNode;
import de.fxdiagram.lib.tools.CarusselChooser;
import de.fxdiagram.lib.tools.CoverFlowChooser;
import de.fxdiagram.lib.tools.CubeChooser;
import java.util.Collections;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import org.eclipse.xtext.xbase.lib.ExclusiveRange;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class AddRapidButtonBehavior<T extends XShape> extends AbstractBehavior<T> {
  private List<XRapidButton> rapidButtons;
  
  public AddRapidButtonBehavior(final T host) {
    super(host);
  }
  
  public void doActivate() {
    T _host = this.getHost();
    final XNode host = ((XNode) _host);
    final Procedure1<XRapidButton> _function = new Procedure1<XRapidButton>() {
      public void apply(final XRapidButton button) {
        NestedDiagramNode _nestedDiagramNode = new NestedDiagramNode("new");
        final NestedDiagramNode target = _nestedDiagramNode;
        final XNode source = button.getHost();
        XConnection _xConnection = new XConnection(source, target);
        final XConnection connection = _xConnection;
        XAbstractDiagram _diagram = Extensions.getDiagram(host);
        ObservableList<XNode> _nodes = _diagram.getNodes();
        _nodes.add(target);
        XAbstractDiagram _diagram_1 = Extensions.getDiagram(host);
        ObservableList<XConnection> _connections = _diagram_1.getConnections();
        _connections.add(connection);
        Placer _placer = button.getPlacer();
        double _xPos = _placer.getXPos();
        double _minus = (_xPos - 0.5);
        double _multiply = (200 * _minus);
        double _layoutX = source.getLayoutX();
        double _plus = (_multiply + _layoutX);
        target.setLayoutX(_plus);
        Placer _placer_1 = button.getPlacer();
        double _yPos = _placer_1.getYPos();
        double _minus_1 = (_yPos - 0.5);
        double _multiply_1 = (150 * _minus_1);
        double _layoutY = source.getLayoutY();
        double _plus_1 = (_multiply_1 + _layoutY);
        target.setLayoutY(_plus_1);
      }
    };
    final Procedure1<XRapidButton> addAction = _function;
    final Procedure1<XRapidButton> _function_1 = new Procedure1<XRapidButton>() {
      public void apply(final XRapidButton button) {
        Pos _chooserPosition = button.getChooserPosition();
        CarusselChooser _carusselChooser = new CarusselChooser(host, _chooserPosition);
        final CarusselChooser chooser = _carusselChooser;
        AddRapidButtonBehavior.this.addChoices(chooser);
        XRoot _root = Extensions.getRoot(host);
        _root.setCurrentTool(chooser);
      }
    };
    final Procedure1<XRapidButton> chooseAction = _function_1;
    final Procedure1<XRapidButton> _function_2 = new Procedure1<XRapidButton>() {
      public void apply(final XRapidButton button) {
        Pos _chooserPosition = button.getChooserPosition();
        CubeChooser _cubeChooser = new CubeChooser(host, _chooserPosition);
        final CubeChooser chooser = _cubeChooser;
        AddRapidButtonBehavior.this.addChoices(chooser);
        XRoot _root = Extensions.getRoot(host);
        _root.setCurrentTool(chooser);
      }
    };
    final Procedure1<XRapidButton> cubeChooseAction = _function_2;
    final Procedure1<XRapidButton> _function_3 = new Procedure1<XRapidButton>() {
      public void apply(final XRapidButton button) {
        Pos _chooserPosition = button.getChooserPosition();
        CoverFlowChooser _coverFlowChooser = new CoverFlowChooser(host, _chooserPosition);
        final CoverFlowChooser chooser = _coverFlowChooser;
        AddRapidButtonBehavior.this.addChoices(chooser);
        XRoot _root = Extensions.getRoot(host);
        _root.setCurrentTool(chooser);
      }
    };
    final Procedure1<XRapidButton> coverFlowChooseAction = _function_3;
    XRapidButton _xRapidButton = new XRapidButton(host, 0.5, 0, "icons/add_16.png", cubeChooseAction);
    XRapidButton _xRapidButton_1 = new XRapidButton(host, 0.5, 1, "icons/add_16.png", coverFlowChooseAction);
    XRapidButton _xRapidButton_2 = new XRapidButton(host, 0, 0.5, "icons/add_16.png", chooseAction);
    XRapidButton _xRapidButton_3 = new XRapidButton(host, 1, 0.5, "icons/add_16.png", addAction);
    this.rapidButtons = Collections.<XRapidButton>unmodifiableList(Lists.<XRapidButton>newArrayList(_xRapidButton, _xRapidButton_1, _xRapidButton_2, _xRapidButton_3));
    XAbstractDiagram _diagram = Extensions.getDiagram(host);
    ObservableList<XRapidButton> _buttons = _diagram.getButtons();
    Iterables.<XRapidButton>addAll(_buttons, this.rapidButtons);
  }
  
  protected void addChoices(final AbstractXNodeChooser chooser) {
    ExclusiveRange _doubleDotLessThan = new ExclusiveRange(0, 20, true);
    for (final Integer i : _doubleDotLessThan) {
      String _plus = ("node " + i);
      SimpleNode _simpleNode = new SimpleNode(_plus);
      chooser.operator_add(_simpleNode);
    }
  }
}
