package de.fxdiagram.examples.lcars;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import com.mongodb.DBObject;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.examples.lcars.LcarsConnectionDescriptor;
import de.fxdiagram.examples.lcars.LcarsEntryDescriptor;
import de.fxdiagram.examples.lcars.LcarsField;
import de.fxdiagram.examples.lcars.LcarsModelProvider;
import de.fxdiagram.examples.lcars.LcarsNode;
import de.fxdiagram.lib.chooser.ChooserConnectionProvider;
import de.fxdiagram.lib.chooser.ConnectedNodeChooser;
import de.fxdiagram.lib.chooser.CoverFlowChoice;
import java.util.List;
import java.util.Set;
import javafx.application.Platform;
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
  
  @Override
  protected Void call() throws Exception {
    Object _xblockexpression = null;
    {
      final LcarsModelProvider modelProvider = CoreExtensions.getRoot(this.host).<LcarsModelProvider>getDomainObjectProvider(LcarsModelProvider.class);
      final LcarsConnectionDescriptor connectionDescriptor = modelProvider.createLcarsConnectionDescriptor(this.fieldName);
      final List<DBObject> siblings = modelProvider.query(this.fieldName, this.fieldValue);
      final LcarsNode lcarsNode = this.host.getLcarsNode();
      CoverFlowChoice _coverFlowChoice = new CoverFlowChoice();
      final ConnectedNodeChooser chooser = new ConnectedNodeChooser(lcarsNode, Side.BOTTOM, _coverFlowChoice);
      chooser.setConnectionProvider(this.connectionProvider);
      final Function1<XConnection, Boolean> _function = (XConnection it) -> {
        DomainObjectDescriptor _domainObjectDescriptor = it.getDomainObjectDescriptor();
        return Boolean.valueOf(Objects.equal(_domainObjectDescriptor, connectionDescriptor));
      };
      final Function1<XConnection, XNode> _function_1 = (XConnection it) -> {
        return it.getSource();
      };
      Iterable<XNode> _map = IterableExtensions.<XConnection, XNode>map(IterableExtensions.<XConnection>filter(this.host.getLcarsNode().getIncomingConnections(), _function), _function_1);
      final Function1<XConnection, Boolean> _function_2 = (XConnection it) -> {
        DomainObjectDescriptor _domainObjectDescriptor = it.getDomainObjectDescriptor();
        return Boolean.valueOf(Objects.equal(_domainObjectDescriptor, connectionDescriptor));
      };
      final Function1<XConnection, XNode> _function_3 = (XConnection it) -> {
        return it.getTarget();
      };
      Iterable<XNode> _map_1 = IterableExtensions.<XConnection, XNode>map(IterableExtensions.<XConnection>filter(this.host.getLcarsNode().getOutgoingConnections(), _function_2), _function_3);
      final Function1<LcarsNode, String> _function_4 = (LcarsNode it) -> {
        return it.getDomainObjectDescriptor().getId();
      };
      final Set<String> alreadyConnected = IterableExtensions.<String>toSet(IterableExtensions.<LcarsNode, String>map(Iterables.<LcarsNode>filter(Iterables.<XNode>concat(_map, _map_1), LcarsNode.class), _function_4));
      alreadyConnected.add(lcarsNode.getDomainObjectDescriptor().getId());
      final Function1<DBObject, Boolean> _function_5 = (DBObject it) -> {
        boolean _contains = alreadyConnected.contains(it.get("_id").toString());
        return Boolean.valueOf((!_contains));
      };
      final Procedure2<DBObject, Integer> _function_6 = (DBObject it, Integer i) -> {
        final LcarsEntryDescriptor descriptor = modelProvider.createLcarsEntryDescriptor(it);
        LcarsNode _lcarsNode = new LcarsNode(descriptor);
        final Procedure1<LcarsNode> _function_7 = (LcarsNode it_1) -> {
          it_1.setWidth(lcarsNode.getWidth());
          it_1.setHeight(lcarsNode.getHeight());
        };
        LcarsNode _doubleArrow = ObjectExtensions.<LcarsNode>operator_doubleArrow(_lcarsNode, _function_7);
        chooser.addChoice(_doubleArrow);
      };
      IterableExtensions.<DBObject>forEach(IterableExtensions.<DBObject>filter(siblings, _function_5), _function_6);
      final Runnable _function_7 = () -> {
        XRoot _root = CoreExtensions.getRoot(this.host);
        _root.setCurrentTool(chooser);
        this.host.resetVisuals();
      };
      Platform.runLater(_function_7);
      _xblockexpression = null;
    }
    return ((Void)_xblockexpression);
  }
}
