package de.fxdiagram.examples.lcars;

import com.mongodb.DBObject;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.examples.lcars.LcarsAccess;
import de.fxdiagram.examples.lcars.LcarsNode;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class LcarsDiagram extends XDiagram {
  private LcarsAccess lcarsAccess;
  
  public LcarsDiagram() {
    final Procedure1<XDiagram> _function = new Procedure1<XDiagram>() {
      public void apply(final XDiagram it) {
        LcarsAccess _lcarsAccess = new LcarsAccess();
        LcarsDiagram.this.lcarsAccess = _lcarsAccess;
        List<DBObject> _query = LcarsDiagram.this.lcarsAccess.query("name", "James T. Kirk");
        final DBObject kirk = _query.get(0);
        ObservableList<XNode> _nodes = it.getNodes();
        LcarsNode _lcarsNode = new LcarsNode(kirk);
        final Procedure1<LcarsNode> _function = new Procedure1<LcarsNode>() {
          public void apply(final LcarsNode it) {
            it.setWidth(120);
          }
        };
        LcarsNode _doubleArrow = ObjectExtensions.<LcarsNode>operator_doubleArrow(_lcarsNode, _function);
        _nodes.add(_doubleArrow);
      }
    };
    this.setContentsInitializer(_function);
    this.setBackgroundPaint(Color.BLACK);
    this.setForegroundPaint(Color.WHITE);
  }
  
  public LcarsAccess getLcarsAccess() {
    return this.lcarsAccess;
  }
}
