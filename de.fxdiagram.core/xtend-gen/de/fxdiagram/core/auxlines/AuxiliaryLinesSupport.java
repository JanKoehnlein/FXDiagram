package de.fxdiagram.core.auxlines;

import com.google.common.collect.Iterables;
import de.fxdiagram.core.XAbstractDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XShape;
import de.fxdiagram.core.auxlines.AuxiliaryLine;
import de.fxdiagram.core.auxlines.AuxiliaryLinesCache;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class AuxiliaryLinesSupport {
  private AuxiliaryLinesCache cache;
  
  private Group group = new Function0<Group>() {
    public Group apply() {
      Group _group = new Group();
      return _group;
    }
  }.apply();
  
  public AuxiliaryLinesSupport(final XAbstractDiagram diagram) {
    AuxiliaryLinesCache _auxiliaryLinesCache = new AuxiliaryLinesCache(diagram);
    this.cache = _auxiliaryLinesCache;
    Group _buttonLayer = diagram.getButtonLayer();
    ObservableList<Node> _children = _buttonLayer.getChildren();
    _children.add(this.group);
  }
  
  public void show(final Iterable<XShape> selection) {
    ObservableList<Node> _children = this.group.getChildren();
    _children.clear();
    final Iterable<XNode> selectedNodes = Iterables.<XNode>filter(selection, XNode.class);
    int _size = IterableExtensions.size(selectedNodes);
    boolean _equals = (_size == 1);
    if (_equals) {
      XNode _head = IterableExtensions.<XNode>head(selectedNodes);
      final Iterable<AuxiliaryLine> lines = this.cache.getAuxiliaryLines(_head);
      final Procedure1<AuxiliaryLine> _function = new Procedure1<AuxiliaryLine>() {
          public void apply(final AuxiliaryLine it) {
            ObservableList<Node> _children = AuxiliaryLinesSupport.this.group.getChildren();
            Node _createNode = it.createNode();
            _children.add(_createNode);
          }
        };
      IterableExtensions.<AuxiliaryLine>forEach(lines, _function);
    }
  }
  
  public void hide() {
    ObservableList<Node> _children = this.group.getChildren();
    _children.clear();
  }
}
