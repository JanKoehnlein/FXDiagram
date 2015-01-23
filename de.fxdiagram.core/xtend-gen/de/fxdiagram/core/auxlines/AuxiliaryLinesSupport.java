package de.fxdiagram.core.auxlines;

import com.google.common.collect.Iterables;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XShape;
import de.fxdiagram.core.auxlines.AuxiliaryLine;
import de.fxdiagram.core.auxlines.AuxiliaryLinesCache;
import java.util.function.Consumer;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class AuxiliaryLinesSupport {
  private AuxiliaryLinesCache cache;
  
  private Group group = new Group();
  
  public AuxiliaryLinesSupport(final XDiagram diagram) {
    AuxiliaryLinesCache _auxiliaryLinesCache = new AuxiliaryLinesCache(diagram);
    this.cache = _auxiliaryLinesCache;
    Group _buttonLayer = diagram.getButtonLayer();
    ObservableList<Node> _children = _buttonLayer.getChildren();
    _children.add(this.group);
  }
  
  public void show(final Iterable<? extends XShape> selection) {
    ObservableList<Node> _children = this.group.getChildren();
    _children.clear();
    final Iterable<XNode> selectedNodes = Iterables.<XNode>filter(selection, XNode.class);
    int _size = IterableExtensions.size(selectedNodes);
    boolean _equals = (_size == 1);
    if (_equals) {
      XNode _head = IterableExtensions.<XNode>head(selectedNodes);
      final Iterable<AuxiliaryLine> lines = this.cache.getAuxiliaryLines(_head);
      final Consumer<AuxiliaryLine> _function = new Consumer<AuxiliaryLine>() {
        public void accept(final AuxiliaryLine it) {
          ObservableList<Node> _children = AuxiliaryLinesSupport.this.group.getChildren();
          Node _createNode = it.createNode();
          _children.add(_createNode);
        }
      };
      lines.forEach(_function);
    }
  }
  
  public void hide() {
    ObservableList<Node> _children = this.group.getChildren();
    _children.clear();
  }
}
