package de.fxdiagram.mapping.reconcile;

import de.fxdiagram.core.XLabel;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.extensions.InitializingListListener;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.mapping.AbstractMapping;
import de.fxdiagram.mapping.IMappedElementDescriptor;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Pair;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class MappingLabelListener<T extends XLabel> extends InitializingListListener<T> {
  private Map<String, Pane> map = CollectionLiterals.<String, Pane>newHashMap();
  
  protected MappingLabelListener(final Pair<String, Pane>... labelMappings) {
    final Consumer<Pair<String, Pane>> _function = (Pair<String, Pane> it) -> {
      String _key = it.getKey();
      Pane _value = it.getValue();
      this.map.put(_key, _value);
    };
    ((List<Pair<String, Pane>>)Conversions.doWrapArray(labelMappings)).forEach(_function);
    final Procedure1<T> _function_1 = (T it) -> {
      it.getNode();
      Pane _pane = this.getPane(it);
      ObservableList<Node> _children = null;
      if (_pane!=null) {
        _children=_pane.getChildren();
      }
      if (_children!=null) {
        _children.add(it);
      }
    };
    this.setAdd(_function_1);
    final Procedure1<T> _function_2 = (T it) -> {
      Pane _pane = this.getPane(it);
      ObservableList<Node> _children = null;
      if (_pane!=null) {
        _children=_pane.getChildren();
      }
      if (_children!=null) {
        _children.remove(it);
      }
    };
    this.setRemove(_function_2);
  }
  
  protected Pane getPane(final XLabel label) {
    final DomainObjectDescriptor descriptor = label.getDomainObjectDescriptor();
    if ((descriptor instanceof IMappedElementDescriptor<?>)) {
      AbstractMapping<?> _mapping = ((IMappedElementDescriptor<?>)descriptor).getMapping();
      String _iD = _mapping.getID();
      return this.map.get(_iD);
    } else {
      return null;
    }
  }
  
  public static <T extends Object> void addMappingLabelListener(final ObservableList<T> labelList, final Pair<String, Pane>... labelMappings) {
    MappingLabelListener _mappingLabelListener = new MappingLabelListener(labelMappings);
    CoreExtensions.<T>addInitializingListener(labelList, _mappingLabelListener);
  }
}
