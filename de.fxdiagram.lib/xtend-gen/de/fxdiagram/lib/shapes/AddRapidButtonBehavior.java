package de.fxdiagram.lib.shapes;

import com.google.common.collect.Lists;
import de.fxdiagram.core.Extensions;
import de.fxdiagram.core.Placer;
import de.fxdiagram.core.XAbstractDiagram;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRapidButton;
import de.fxdiagram.core.XRootDiagram;
import de.fxdiagram.core.behavior.AbstractBehavior;
import de.fxdiagram.core.tools.chooser.AbstractXNodeChooser;
import de.fxdiagram.core.tools.chooser.CarusselChooser;
import de.fxdiagram.core.tools.chooser.CoverFlowChooser;
import de.fxdiagram.core.tools.chooser.CubeChooser;
import de.fxdiagram.lib.shapes.NestedDiagramNode;
import de.fxdiagram.lib.shapes.SimpleNode;
import java.util.Collections;
import java.util.List;
import javafx.geometry.Pos;
import org.eclipse.xtext.xbase.lib.ExclusiveRange;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class AddRapidButtonBehavior extends AbstractBehavior {
  private List<XRapidButton> rapidButtons;
  
  public AddRapidButtonBehavior(final XNode host) {
    super(host);
  }
  
  public void doActivate() {
    final Procedure1<XRapidButton> _function = new Procedure1<XRapidButton>() {
        public void apply(final XRapidButton button) {
          NestedDiagramNode _nestedDiagramNode = new NestedDiagramNode("new");
          final NestedDiagramNode target = _nestedDiagramNode;
          final XNode source = button.getHost();
          XConnection _xConnection = new XConnection(source, target);
          final XConnection connection = _xConnection;
          XNode _host = AddRapidButtonBehavior.this.getHost();
          XAbstractDiagram _diagram = Extensions.getDiagram(_host);
          _diagram.addNode(target);
          XNode _host_1 = AddRapidButtonBehavior.this.getHost();
          XAbstractDiagram _diagram_1 = Extensions.getDiagram(_host_1);
          _diagram_1.addConnection(connection);
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
          XNode _host = AddRapidButtonBehavior.this.getHost();
          Pos _chooserPosition = button.getChooserPosition();
          CarusselChooser _carusselChooser = new CarusselChooser(_host, _chooserPosition);
          final CarusselChooser chooser = _carusselChooser;
          AddRapidButtonBehavior.this.addChoices(chooser);
          XNode _host_1 = AddRapidButtonBehavior.this.getHost();
          XRootDiagram _rootDiagram = Extensions.getRootDiagram(_host_1);
          _rootDiagram.setCurrentTool(chooser);
        }
      };
    final Procedure1<XRapidButton> chooseAction = _function_1;
    final Procedure1<XRapidButton> _function_2 = new Procedure1<XRapidButton>() {
        public void apply(final XRapidButton button) {
          XNode _host = AddRapidButtonBehavior.this.getHost();
          Pos _chooserPosition = button.getChooserPosition();
          CubeChooser _cubeChooser = new CubeChooser(_host, _chooserPosition);
          final CubeChooser chooser = _cubeChooser;
          AddRapidButtonBehavior.this.addChoices(chooser);
          XNode _host_1 = AddRapidButtonBehavior.this.getHost();
          XRootDiagram _rootDiagram = Extensions.getRootDiagram(_host_1);
          _rootDiagram.setCurrentTool(chooser);
        }
      };
    final Procedure1<XRapidButton> cubeChooseAction = _function_2;
    final Procedure1<XRapidButton> _function_3 = new Procedure1<XRapidButton>() {
        public void apply(final XRapidButton button) {
          XNode _host = AddRapidButtonBehavior.this.getHost();
          Pos _chooserPosition = button.getChooserPosition();
          CoverFlowChooser _coverFlowChooser = new CoverFlowChooser(_host, _chooserPosition);
          final CoverFlowChooser chooser = _coverFlowChooser;
          AddRapidButtonBehavior.this.addChoices(chooser);
          XNode _host_1 = AddRapidButtonBehavior.this.getHost();
          XRootDiagram _rootDiagram = Extensions.getRootDiagram(_host_1);
          _rootDiagram.setCurrentTool(chooser);
        }
      };
    final Procedure1<XRapidButton> coverFlowChooseAction = _function_3;
    XNode _host = this.getHost();
    XRapidButton _xRapidButton = new XRapidButton(_host, 0.5, 0, "icons/add_16.png", cubeChooseAction);
    XNode _host_1 = this.getHost();
    XRapidButton _xRapidButton_1 = new XRapidButton(_host_1, 0.5, 1, "icons/add_16.png", coverFlowChooseAction);
    XNode _host_2 = this.getHost();
    XRapidButton _xRapidButton_2 = new XRapidButton(_host_2, 0, 0.5, "icons/add_16.png", chooseAction);
    XNode _host_3 = this.getHost();
    XRapidButton _xRapidButton_3 = new XRapidButton(_host_3, 1, 0.5, "icons/add_16.png", addAction);
    this.rapidButtons = Collections.<XRapidButton>unmodifiableList(Lists.<XRapidButton>newArrayList(_xRapidButton, _xRapidButton_1, _xRapidButton_2, _xRapidButton_3));
    final Procedure1<XRapidButton> _function_4 = new Procedure1<XRapidButton>() {
        public void apply(final XRapidButton it) {
          XNode _host = it.getHost();
          XAbstractDiagram _diagram = Extensions.getDiagram(_host);
          _diagram.addButton(it);
        }
      };
    IterableExtensions.<XRapidButton>forEach(this.rapidButtons, _function_4);
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
