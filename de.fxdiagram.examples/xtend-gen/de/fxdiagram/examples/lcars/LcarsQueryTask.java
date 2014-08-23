package de.fxdiagram.examples.lcars;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import com.mongodb.DBObject;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.core.tools.ChooserConnectionProvider;
import de.fxdiagram.examples.lcars.LcarsConnectionDescriptor;
import de.fxdiagram.examples.lcars.LcarsEntryDescriptor;
import de.fxdiagram.examples.lcars.LcarsField;
import de.fxdiagram.examples.lcars.LcarsModelProvider;
import de.fxdiagram.examples.lcars.LcarsNode;
import de.fxdiagram.lib.tools.CoverFlowChooser;
import java.util.List;
import java.util.Set;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Side;
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
    Object _xblockexpression = null;
    {
      XRoot _root = CoreExtensions.getRoot(this.host);
      final LcarsModelProvider modelProvider = _root.<LcarsModelProvider>getDomainObjectProvider(LcarsModelProvider.class);
      final LcarsConnectionDescriptor connectionDescriptor = modelProvider.createLcarsConnectionDescriptor(this.fieldName);
      final List<DBObject> siblings = modelProvider.query(this.fieldName, this.fieldValue);
      final LcarsNode lcarsNode = this.host.getLcarsNode();
      final CoverFlowChooser chooser = new CoverFlowChooser(lcarsNode, Side.BOTTOM);
      chooser.setConnectionProvider(this.connectionProvider);
      LcarsNode _lcarsNode = this.host.getLcarsNode();
      ObservableList<XConnection> _incomingConnections = _lcarsNode.getIncomingConnections();
      final Function1<XConnection, Boolean> _function = new Function1<XConnection, Boolean>() {
        public Boolean apply(final XConnection it) {
          DomainObjectDescriptor _domainObject = it.getDomainObject();
          return Boolean.valueOf(Objects.equal(_domainObject, connectionDescriptor));
        }
      };
      Iterable<XConnection> _filter = IterableExtensions.<XConnection>filter(_incomingConnections, _function);
      final Function1<XConnection, XNode> _function_1 = new Function1<XConnection, XNode>() {
        public XNode apply(final XConnection it) {
          return it.getSource();
        }
      };
      Iterable<XNode> _map = IterableExtensions.<XConnection, XNode>map(_filter, _function_1);
      LcarsNode _lcarsNode_1 = this.host.getLcarsNode();
      ObservableList<XConnection> _outgoingConnections = _lcarsNode_1.getOutgoingConnections();
      final Function1<XConnection, Boolean> _function_2 = new Function1<XConnection, Boolean>() {
        public Boolean apply(final XConnection it) {
          DomainObjectDescriptor _domainObject = it.getDomainObject();
          return Boolean.valueOf(Objects.equal(_domainObject, connectionDescriptor));
        }
      };
      Iterable<XConnection> _filter_1 = IterableExtensions.<XConnection>filter(_outgoingConnections, _function_2);
      final Function1<XConnection, XNode> _function_3 = new Function1<XConnection, XNode>() {
        public XNode apply(final XConnection it) {
          return it.getTarget();
        }
      };
      Iterable<XNode> _map_1 = IterableExtensions.<XConnection, XNode>map(_filter_1, _function_3);
      Iterable<XNode> _plus = Iterables.<XNode>concat(_map, _map_1);
      Iterable<LcarsNode> _filter_2 = Iterables.<LcarsNode>filter(_plus, LcarsNode.class);
      final Function1<LcarsNode, String> _function_4 = new Function1<LcarsNode, String>() {
        public String apply(final LcarsNode it) {
          DomainObjectDescriptor _domainObject = it.getDomainObject();
          return _domainObject.getId();
        }
      };
      Iterable<String> _map_2 = IterableExtensions.<LcarsNode, String>map(_filter_2, _function_4);
      final Set<String> alreadyConnected = IterableExtensions.<String>toSet(_map_2);
      DomainObjectDescriptor _domainObject = lcarsNode.getDomainObject();
      String _id = _domainObject.getId();
      alreadyConnected.add(_id);
      final Function1<DBObject, Boolean> _function_5 = new Function1<DBObject, Boolean>() {
        public Boolean apply(final DBObject it) {
          Object _get = it.get("_id");
          String _string = _get.toString();
          boolean _contains = alreadyConnected.contains(_string);
          return Boolean.valueOf((!_contains));
        }
      };
      Iterable<DBObject> _filter_3 = IterableExtensions.<DBObject>filter(siblings, _function_5);
      final Procedure2<DBObject, Integer> _function_6 = new Procedure2<DBObject, Integer>() {
        public void apply(final DBObject it, final Integer i) {
          final LcarsEntryDescriptor descriptor = modelProvider.createLcarsEntryDescriptor(it);
          LcarsNode _lcarsNode = new LcarsNode(descriptor);
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
      _xblockexpression = null;
    }
    return ((Void)_xblockexpression);
  }
}
