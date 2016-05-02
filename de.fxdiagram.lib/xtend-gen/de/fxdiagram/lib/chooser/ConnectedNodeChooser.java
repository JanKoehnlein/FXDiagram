package de.fxdiagram.lib.chooser;

import com.google.common.base.Objects;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.XShape;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.lib.chooser.AbstractBaseChooser;
import de.fxdiagram.lib.chooser.ChoiceGraphics;
import de.fxdiagram.lib.chooser.ChooserConnectionProvider;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Side;
import javafx.scene.Group;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;

/**
 * A {@link AbstractBaseChooser} to add new nodes to the diagram and connect them
 * to the given host {@link XNode}.
 * 
 * @see AbstractBaseChooser
 */
@SuppressWarnings("all")
public class ConnectedNodeChooser extends AbstractBaseChooser {
  private XNode host;
  
  private XNode currentChoice;
  
  private ChooserConnectionProvider connectionProvider = ((ChooserConnectionProvider) (XNode host, XNode choice, DomainObjectDescriptor choiceInfo) -> {
    return new XConnection(host, choice);
  });
  
  private XConnection currentConnection;
  
  protected final Side layoutPosition;
  
  public ConnectedNodeChooser(final XNode host, final Side layoutPosition, final ChoiceGraphics graphics) {
    super(graphics, layoutPosition.isVertical());
    this.host = host;
    this.layoutPosition = layoutPosition;
  }
  
  @Override
  public XRoot getRoot() {
    return CoreExtensions.getRoot(this.host);
  }
  
  @Override
  public XDiagram getDiagram() {
    return CoreExtensions.getDiagram(this.host);
  }
  
  @Override
  public Point2D getPosition() {
    double _layoutX = this.host.getLayoutX();
    Group _placementGroup = this.host.getPlacementGroup();
    double _layoutX_1 = _placementGroup.getLayoutX();
    double _plus = (_layoutX + _layoutX_1);
    double _layoutY = this.host.getLayoutY();
    Group _placementGroup_1 = this.host.getPlacementGroup();
    double _layoutY_1 = _placementGroup_1.getLayoutY();
    double _plus_1 = (_layoutY + _layoutY_1);
    return new Point2D(_plus, _plus_1);
  }
  
  public void setConnectionProvider(final ChooserConnectionProvider connectionProvider) {
    this.connectionProvider = connectionProvider;
  }
  
  @Override
  public boolean deactivate() {
    boolean _xblockexpression = false;
    {
      boolean _isActive = this.getIsActive();
      boolean _not = (!_isActive);
      if (_not) {
        return false;
      }
      this.removeConnection(this.currentConnection);
      _xblockexpression = super.deactivate();
    }
    return _xblockexpression;
  }
  
  @Override
  protected void adjustNewNode(final XNode choice, final double widthDelta, final double heightDelta) {
    final Side layoutPosition = this.layoutPosition;
    if (layoutPosition != null) {
      switch (layoutPosition) {
        case LEFT:
          double _layoutX = choice.getLayoutX();
          double _minus = (_layoutX - (0.5 * widthDelta));
          choice.setLayoutX(_minus);
          break;
        case RIGHT:
          double _layoutX_1 = choice.getLayoutX();
          double _plus = (_layoutX_1 + (0.5 * widthDelta));
          choice.setLayoutX(_plus);
          break;
        case TOP:
          double _layoutY = choice.getLayoutY();
          double _minus_1 = (_layoutY - (0.5 * heightDelta));
          choice.setLayoutY(_minus_1);
          break;
        case BOTTOM:
          double _layoutY_1 = choice.getLayoutY();
          double _plus_1 = (_layoutY_1 + (0.5 * heightDelta));
          choice.setLayoutY(_plus_1);
          break;
        default:
          break;
      }
    }
    choice.setPlacementHint(this.layoutPosition);
  }
  
  @Override
  public Iterable<? extends XShape> getAdditionalShapesToAdd(final XNode choice, final DomainObjectDescriptor choiceInfo) {
    XConnection _connectChoice = this.connectChoice(choice, choiceInfo);
    final List<XConnection> result = Collections.<XConnection>unmodifiableList(CollectionLiterals.<XConnection>newArrayList(_connectChoice));
    this.currentConnection = null;
    return result;
  }
  
  @Override
  protected void setInterpolatedPosition(final double interpolatedPosition) {
    super.setInterpolatedPosition(interpolatedPosition);
    ArrayList<XNode> _nodes = this.getNodes();
    boolean _isEmpty = _nodes.isEmpty();
    boolean _not = (!_isEmpty);
    if (_not) {
      ArrayList<XNode> _nodes_1 = this.getNodes();
      double _currentPosition = this.getCurrentPosition();
      double _plus = (_currentPosition + 0.5);
      ArrayList<XNode> _nodes_2 = this.getNodes();
      int _size = _nodes_2.size();
      int _modulo = (((int) _plus) % _size);
      final XNode choice = _nodes_1.get(_modulo);
      DomainObjectDescriptor _choiceInfo = this.getChoiceInfo(choice);
      this.connectChoice(choice, _choiceInfo);
    }
  }
  
  protected XConnection connectChoice(final XNode choice, final DomainObjectDescriptor choiceInfo) {
    XConnection _xblockexpression = null;
    {
      boolean _and = false;
      boolean _isActive = this.getIsActive();
      if (!_isActive) {
        _and = false;
      } else {
        _and = (choice != this.currentChoice);
      }
      if (_and) {
        this.currentChoice = choice;
        final XConnection newConnection = this.connectionProvider.getConnection(this.host, choice, choiceInfo);
        boolean _notEquals = (!Objects.equal(newConnection, this.currentConnection));
        if (_notEquals) {
          this.removeConnection(this.currentConnection);
          this.currentConnection = newConnection;
          this.addConnection(this.currentConnection);
        }
        choice.toFront();
        this.currentConnection.toFront();
      }
      _xblockexpression = this.currentConnection;
    }
    return _xblockexpression;
  }
  
  protected boolean addConnection(final XConnection connection) {
    boolean _xifexpression = false;
    boolean _and = false;
    boolean _and_1 = false;
    boolean _notEquals = (!Objects.equal(connection, null));
    if (!_notEquals) {
      _and_1 = false;
    } else {
      XNode _source = connection.getSource();
      XDiagram _diagram = CoreExtensions.getDiagram(_source);
      boolean _notEquals_1 = (!Objects.equal(_diagram, null));
      _and_1 = _notEquals_1;
    }
    if (!_and_1) {
      _and = false;
    } else {
      XNode _target = connection.getTarget();
      XDiagram _diagram_1 = CoreExtensions.getDiagram(_target);
      boolean _notEquals_2 = (!Objects.equal(_diagram_1, null));
      _and = _notEquals_2;
    }
    if (_and) {
      XDiagram _diagram_2 = this.getDiagram();
      ObservableList<XConnection> _connections = _diagram_2.getConnections();
      _xifexpression = _connections.add(connection);
    }
    return _xifexpression;
  }
  
  protected boolean removeConnection(final XConnection connection) {
    boolean _xifexpression = false;
    boolean _notEquals = (!Objects.equal(connection, null));
    if (_notEquals) {
      boolean _xblockexpression = false;
      {
        XDiagram _diagram = this.getDiagram();
        ObservableList<XConnection> _connections = _diagram.getConnections();
        _connections.remove(connection);
        XNode _source = connection.getSource();
        ObservableList<XConnection> _outgoingConnections = _source.getOutgoingConnections();
        _outgoingConnections.remove(connection);
        XNode _target = connection.getTarget();
        ObservableList<XConnection> _incomingConnections = _target.getIncomingConnections();
        _xblockexpression = _incomingConnections.remove(connection);
      }
      _xifexpression = _xblockexpression;
    }
    return _xifexpression;
  }
  
  @Override
  protected void alignGroup(final Group group, final double maxWidth, final double maxHeight) {
    final Point2D position = this.getPosition();
    double _switchResult = (double) 0;
    final Side layoutPosition = this.layoutPosition;
    if (layoutPosition != null) {
      switch (layoutPosition) {
        case LEFT:
          double _x = position.getX();
          double _layoutDistance = this.getLayoutDistance();
          double _minus = (_x - _layoutDistance);
          _switchResult = (_minus - (0.5 * maxWidth));
          break;
        case RIGHT:
          double _x_1 = position.getX();
          Bounds _layoutBounds = this.host.getLayoutBounds();
          double _width = _layoutBounds.getWidth();
          double _plus = (_x_1 + _width);
          double _layoutDistance_1 = this.getLayoutDistance();
          double _plus_1 = (_plus + _layoutDistance_1);
          _switchResult = (_plus_1 + (0.5 * maxWidth));
          break;
        default:
          double _x_2 = position.getX();
          Bounds _layoutBounds_1 = this.host.getLayoutBounds();
          double _width_1 = _layoutBounds_1.getWidth();
          double _multiply = (0.5 * _width_1);
          _switchResult = (_x_2 + _multiply);
          break;
      }
    } else {
      double _x_2 = position.getX();
      Bounds _layoutBounds_1 = this.host.getLayoutBounds();
      double _width_1 = _layoutBounds_1.getWidth();
      double _multiply = (0.5 * _width_1);
      _switchResult = (_x_2 + _multiply);
    }
    group.setLayoutX(_switchResult);
    double _switchResult_1 = (double) 0;
    final Side layoutPosition_1 = this.layoutPosition;
    if (layoutPosition_1 != null) {
      switch (layoutPosition_1) {
        case TOP:
          double _y = position.getY();
          double _layoutDistance_2 = this.getLayoutDistance();
          double _minus_1 = (_y - _layoutDistance_2);
          _switchResult_1 = (_minus_1 - (0.5 * maxHeight));
          break;
        case BOTTOM:
          double _y_1 = position.getY();
          Bounds _layoutBounds_2 = this.host.getLayoutBounds();
          double _height = _layoutBounds_2.getHeight();
          double _plus_2 = (_y_1 + _height);
          double _layoutDistance_3 = this.getLayoutDistance();
          double _plus_3 = (_plus_2 + _layoutDistance_3);
          _switchResult_1 = (_plus_3 + (0.5 * maxHeight));
          break;
        default:
          double _y_2 = position.getY();
          Bounds _layoutBounds_3 = this.host.getLayoutBounds();
          double _height_1 = _layoutBounds_3.getHeight();
          double _multiply_1 = (0.5 * _height_1);
          _switchResult_1 = (_y_2 + _multiply_1);
          break;
      }
    } else {
      double _y_2 = position.getY();
      Bounds _layoutBounds_3 = this.host.getLayoutBounds();
      double _height_1 = _layoutBounds_3.getHeight();
      double _multiply_1 = (0.5 * _height_1);
      _switchResult_1 = (_y_2 + _multiply_1);
    }
    group.setLayoutY(_switchResult_1);
  }
  
  private SimpleDoubleProperty layoutDistanceProperty = new SimpleDoubleProperty(this, "layoutDistance",_initLayoutDistance());
  
  private static final double _initLayoutDistance() {
    return 60;
  }
  
  public double getLayoutDistance() {
    return this.layoutDistanceProperty.get();
  }
  
  public void setLayoutDistance(final double layoutDistance) {
    this.layoutDistanceProperty.set(layoutDistance);
  }
  
  public DoubleProperty layoutDistanceProperty() {
    return this.layoutDistanceProperty;
  }
}
