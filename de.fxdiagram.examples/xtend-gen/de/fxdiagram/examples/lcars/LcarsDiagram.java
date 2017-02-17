package de.fxdiagram.examples.lcars;

import com.mongodb.DBObject;
import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.core.model.ToString;
import de.fxdiagram.examples.lcars.LcarsEntryDescriptor;
import de.fxdiagram.examples.lcars.LcarsModelProvider;
import de.fxdiagram.examples.lcars.LcarsNode;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@ModelNode
@SuppressWarnings("all")
public class LcarsDiagram extends XDiagram {
  public LcarsDiagram() {
    this.setBackgroundPaint(Color.BLACK);
    this.setForegroundPaint(Color.WHITE);
    this.setConnectionPaint(Color.WHITE);
  }
  
  @Override
  public void doActivate() {
    ObservableList<XNode> _nodes = this.getNodes();
    boolean _isEmpty = _nodes.isEmpty();
    if (_isEmpty) {
      final Procedure1<XDiagram> _function = (XDiagram it) -> {
        XRoot _root = CoreExtensions.getRoot(it);
        final LcarsModelProvider provider = _root.<LcarsModelProvider>getDomainObjectProvider(LcarsModelProvider.class);
        List<DBObject> _query = provider.query("name", "James T. Kirk");
        final DBObject kirk = _query.get(0);
        final LcarsEntryDescriptor handle = provider.createLcarsEntryDescriptor(kirk);
        ObservableList<XNode> _nodes_1 = it.getNodes();
        LcarsNode _lcarsNode = new LcarsNode(handle);
        final Procedure1<LcarsNode> _function_1 = (LcarsNode it_1) -> {
          it_1.setWidth(120);
        };
        LcarsNode _doubleArrow = ObjectExtensions.<LcarsNode>operator_doubleArrow(_lcarsNode, _function_1);
        _nodes_1.add(_doubleArrow);
      };
      this.setContentsInitializer(_function);
    }
    super.doActivate();
  }
  
  public void populate(final ModelElementImpl modelElement) {
    super.populate(modelElement);
  }
  
  public void postLoad() {
    
  }
  
  public String toString() {
    return ToString.toString(this);
  }
}
