package de.fxdiagram.examples.lcars;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import com.mongodb.DBObject;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.tools.ChooserConnectionProvider;
import de.fxdiagram.examples.lcars.LcarsAccess;
import de.fxdiagram.examples.lcars.LcarsDiagram;
import de.fxdiagram.examples.lcars.LcarsExtensions;
import de.fxdiagram.examples.lcars.LcarsField;
import de.fxdiagram.examples.lcars.LcarsNode;
import de.fxdiagram.lib.tools.CoverFlowChooser;
import java.util.List;
import java.util.Set;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure2;

@SuppressWarnings("all")
public class LcarsQueryTask extends Task<Void> {
  private LcarsField host;
  
  private String fieldName;
  
  private String fieldValue;
  
  private ChooserConnectionProvider connectionProvider;
  
  public LcarsQueryTask(final LcarsField host, final String fieldName, final String fieldValue, final ChooserConnectionProvider connectionProvider) {
    this.host = host;
    this.fieldName = fieldName;
    this.fieldValue = fieldValue;
    this.connectionProvider = connectionProvider;
  }
  
  protected Void call() throws Exception {
    Void _xblockexpression = null;
    {
      LcarsDiagram _lcarsDiagram = LcarsExtensions.getLcarsDiagram(this.host);
      LcarsAccess _lcarsAccess = _lcarsDiagram.getLcarsAccess();
      final List<DBObject> siblings = _lcarsAccess.query(this.fieldName, this.fieldValue);
      final LcarsNode lcarsNode = this.host.getLcarsNode();
      CoverFlowChooser _coverFlowChooser = new CoverFlowChooser(lcarsNode, Pos.BOTTOM_CENTER);
      final CoverFlowChooser chooser = _coverFlowChooser;
      chooser.setConnectionProvider(this.connectionProvider);
      LcarsNode _lcarsNode = this.host.getLcarsNode();
      ObservableList<XConnection> _incomingConnections = _lcarsNode.getIncomingConnections();
      final Function1<XConnection,Boolean> _function = new Function1<XConnection,Boolean>() {
        public Boolean apply(final XConnection it) {
          Object _key = it.getKey();
          boolean _equals = Objects.equal(_key, LcarsQueryTask.this.fieldName);
          return Boolean.valueOf(_equals);
        }
      };
      Iterable<XConnection> _filter = IterableExtensions.<XConnection>filter(_incomingConnections, _function);
      final Function1<XConnection,XNode> _function_1 = new Function1<XConnection,XNode>() {
        public XNode apply(final XConnection it) {
          XNode _source = it.getSource();
          return _source;
        }
      };
      Iterable<XNode> _map = IterableExtensions.<XConnection, XNode>map(_filter, _function_1);
      LcarsNode _lcarsNode_1 = this.host.getLcarsNode();
      ObservableList<XConnection> _outgoingConnections = _lcarsNode_1.getOutgoingConnections();
      final Function1<XConnection,Boolean> _function_2 = new Function1<XConnection,Boolean>() {
        public Boolean apply(final XConnection it) {
          Object _key = it.getKey();
          boolean _equals = Objects.equal(_key, LcarsQueryTask.this.fieldName);
          return Boolean.valueOf(_equals);
        }
      };
      Iterable<XConnection> _filter_1 = IterableExtensions.<XConnection>filter(_outgoingConnections, _function_2);
      final Function1<XConnection,XNode> _function_3 = new Function1<XConnection,XNode>() {
        public XNode apply(final XConnection it) {
          XNode _target = it.getTarget();
          return _target;
        }
      };
      Iterable<XNode> _map_1 = IterableExtensions.<XConnection, XNode>map(_filter_1, _function_3);
      Iterable<XNode> _plus = Iterables.<XNode>concat(_map, _map_1);
      Iterable<LcarsNode> _filter_2 = Iterables.<LcarsNode>filter(_plus, LcarsNode.class);
      final Function1<LcarsNode,String> _function_4 = new Function1<LcarsNode,String>() {
        public String apply(final LcarsNode it) {
          String _dbId = it.getDbId();
          return _dbId;
        }
      };
      Iterable<String> _map_2 = IterableExtensions.<LcarsNode, String>map(_filter_2, _function_4);
      final Set<String> alreadyConnected = IterableExtensions.<String>toSet(_map_2);
      String _dbId = lcarsNode.getDbId();
      alreadyConnected.add(_dbId);
      final Function1<DBObject,Boolean> _function_5 = new Function1<DBObject,Boolean>() {
        public Boolean apply(final DBObject it) {
          Object _get = it.get("_id");
          String _string = _get.toString();
          boolean _contains = alreadyConnected.contains(_string);
          boolean _not = (!_contains);
          return Boolean.valueOf(_not);
        }
      };
      Iterable<DBObject> _filter_3 = IterableExtensions.<DBObject>filter(siblings, _function_5);
      final Procedure2<DBObject,Integer> _function_6 = new Procedure2<DBObject,Integer>() {
        public void apply(final DBObject it, final Integer i) {
          LcarsNode _lcarsNode = new LcarsNode(it);
          final Procedure1<LcarsNode> _function = new Procedure1<LcarsNode>() {
            public void apply(final LcarsNode it) {
              double _width = lcarsNode.getWidth();
              it.setWidth(_width);
              double _height = lcarsNode.getHeight();
              it.setHeight(_height);
            }
          };
          LcarsNode _doubleArrow = ObjectExtensions.<LcarsNode>operator_doubleArrow(_lcarsNode, _function);
          chooser.addChoice(_doubleArrow);
        }
      };
      IterableExtensions.<DBObject>forEach(_filter_3, _function_6);
      final Runnable _function_7 = new Runnable() {
        public void run() {
          XRoot _root = CoreExtensions.getRoot(LcarsQueryTask.this.host);
          _root.setCurrentTool(chooser);
          LcarsQueryTask.this.host.resetVisuals();
        }
      };
      Platform.runLater(_function_7);
      _xblockexpression = (null);
    }
    return _xblockexpression;
  }
}
