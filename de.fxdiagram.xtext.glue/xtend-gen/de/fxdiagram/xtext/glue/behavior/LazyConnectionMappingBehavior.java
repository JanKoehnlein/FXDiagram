package de.fxdiagram.xtext.glue.behavior;

import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.lib.buttons.RapidButton;
import de.fxdiagram.lib.buttons.RapidButtonBehavior;
import de.fxdiagram.xtext.glue.behavior.LazyConnectionRapidButtonAction;
import de.fxdiagram.xtext.glue.mapping.AbstractConnectionMappingCall;
import de.fxdiagram.xtext.glue.mapping.XDiagramConfigInterpreter;
import java.util.List;
import javafx.beans.property.ListProperty;
import javafx.collections.ListChangeListener;
import javafx.geometry.Side;
import javafx.scene.Node;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class LazyConnectionMappingBehavior<ARG extends Object> extends RapidButtonBehavior<XNode> {
  private List<LazyConnectionRapidButtonAction<?, ARG>> actions = CollectionLiterals.<LazyConnectionRapidButtonAction<?, ARG>>newArrayList();
  
  public LazyConnectionMappingBehavior(final XNode host) {
    super(host);
  }
  
  public boolean addConnectionMappingCall(final AbstractConnectionMappingCall<?, ARG> mappingCall, final XDiagramConfigInterpreter configInterpreter, final boolean hostIsSource) {
    LazyConnectionRapidButtonAction<?, ARG> _createAction = this.createAction(mappingCall, configInterpreter, hostIsSource);
    return this.actions.add(_createAction);
  }
  
  protected LazyConnectionRapidButtonAction<?, ARG> createAction(final AbstractConnectionMappingCall<?, ARG> mappingCall, final XDiagramConfigInterpreter configInterpreter, final boolean hostIsSource) {
    LazyConnectionRapidButtonAction<?, ARG> _xblockexpression = null;
    {
      final LazyConnectionRapidButtonAction<?, ARG> action = new LazyConnectionRapidButtonAction(mappingCall, configInterpreter, hostIsSource);
      XNode _host = this.getHost();
      Node _image = mappingCall.getImage(Side.LEFT);
      RapidButton _rapidButton = new RapidButton(_host, Side.LEFT, _image, action);
      this.add(_rapidButton);
      XNode _host_1 = this.getHost();
      Node _image_1 = mappingCall.getImage(Side.RIGHT);
      RapidButton _rapidButton_1 = new RapidButton(_host_1, Side.RIGHT, _image_1, action);
      this.add(_rapidButton_1);
      XNode _host_2 = this.getHost();
      Node _image_2 = mappingCall.getImage(Side.TOP);
      RapidButton _rapidButton_2 = new RapidButton(_host_2, Side.TOP, _image_2, action);
      this.add(_rapidButton_2);
      XNode _host_3 = this.getHost();
      Node _image_3 = mappingCall.getImage(Side.BOTTOM);
      RapidButton _rapidButton_3 = new RapidButton(_host_3, Side.BOTTOM, _image_3, action);
      this.add(_rapidButton_3);
      _xblockexpression = action;
    }
    return _xblockexpression;
  }
  
  protected void doActivate() {
    super.doActivate();
    final Procedure1<LazyConnectionRapidButtonAction<?, ARG>> _function = new Procedure1<LazyConnectionRapidButtonAction<?, ARG>>() {
      public void apply(final LazyConnectionRapidButtonAction<?, ARG> it) {
        XNode _host = LazyConnectionMappingBehavior.this.getHost();
        it.updateEnablement(_host);
      }
    };
    IterableExtensions.<LazyConnectionRapidButtonAction<?, ARG>>forEach(this.actions, _function);
    XNode _host = this.getHost();
    XDiagram _diagram = CoreExtensions.getDiagram(_host);
    ListProperty<XConnection> _connectionsProperty = _diagram.connectionsProperty();
    final ListChangeListener<XConnection> _function_1 = new ListChangeListener<XConnection>() {
      public void onChanged(final ListChangeListener.Change<? extends XConnection> it) {
        final Procedure1<LazyConnectionRapidButtonAction<?, ARG>> _function = new Procedure1<LazyConnectionRapidButtonAction<?, ARG>>() {
          public void apply(final LazyConnectionRapidButtonAction<?, ARG> it) {
            XNode _host = LazyConnectionMappingBehavior.this.getHost();
            it.updateEnablement(_host);
          }
        };
        IterableExtensions.<LazyConnectionRapidButtonAction<?, ARG>>forEach(LazyConnectionMappingBehavior.this.actions, _function);
      }
    };
    _connectionsProperty.addListener(_function_1);
  }
}
