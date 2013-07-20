package de.fxdiagram.examples.lcars;

import com.google.common.base.Objects;
import com.mongodb.DBObject;
import de.fxdiagram.core.Extensions;
import de.fxdiagram.core.XRootDiagram;
import de.fxdiagram.examples.lcars.LcarsAccess;
import de.fxdiagram.examples.lcars.LcarsNode;
import de.fxdiagram.lib.tools.CoverFlowChooser;
import java.util.List;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class LcarsQueryTask extends Task<Void> {
  private LcarsNode host;
  
  private String fieldName;
  
  private String fieldValue;
  
  public LcarsQueryTask(final LcarsNode host, final String fieldName, final String fieldValue) {
    this.host = host;
    this.fieldName = fieldName;
    this.fieldValue = fieldValue;
  }
  
  protected Void call() throws Exception {
    Void _xblockexpression = null;
    {
      LcarsAccess _get = LcarsAccess.get();
      final List<DBObject> siblings = _get.query(this.fieldName, this.fieldValue);
      CoverFlowChooser _coverFlowChooser = new CoverFlowChooser(this.host, Pos.BOTTOM_CENTER);
      final CoverFlowChooser chooser = _coverFlowChooser;
      final Function1<DBObject,Boolean> _function = new Function1<DBObject,Boolean>() {
          public Boolean apply(final DBObject it) {
            Object _get = it.get("_id");
            String _string = _get.toString();
            String _dbId = LcarsQueryTask.this.host.getDbId();
            boolean _notEquals = (!Objects.equal(_string, _dbId));
            return Boolean.valueOf(_notEquals);
          }
        };
      Iterable<DBObject> _filter = IterableExtensions.<DBObject>filter(siblings, _function);
      final Function1<DBObject,LcarsNode> _function_1 = new Function1<DBObject,LcarsNode>() {
          public LcarsNode apply(final DBObject it) {
            LcarsNode _lcarsNode = new LcarsNode(it);
            final Procedure1<LcarsNode> _function = new Procedure1<LcarsNode>() {
                public void apply(final LcarsNode it) {
                  double _width = LcarsQueryTask.this.host.getWidth();
                  it.setWidth(_width);
                  double _height = LcarsQueryTask.this.host.getHeight();
                  it.setHeight(_height);
                }
              };
            LcarsNode _doubleArrow = ObjectExtensions.<LcarsNode>operator_doubleArrow(_lcarsNode, _function);
            return _doubleArrow;
          }
        };
      Iterable<LcarsNode> _map = IterableExtensions.<DBObject, LcarsNode>map(_filter, _function_1);
      chooser.operator_add(_map);
      final Runnable _function_2 = new Runnable() {
          public void run() {
            XRootDiagram _rootDiagram = Extensions.getRootDiagram(LcarsQueryTask.this.host);
            _rootDiagram.setCurrentTool(chooser);
          }
        };
      Platform.runLater(_function_2);
      _xblockexpression = (null);
    }
    return _xblockexpression;
  }
}
