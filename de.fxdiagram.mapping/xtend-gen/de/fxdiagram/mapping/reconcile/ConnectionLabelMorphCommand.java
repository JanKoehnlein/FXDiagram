package de.fxdiagram.mapping.reconcile;

import com.google.common.base.Objects;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XConnectionLabel;
import de.fxdiagram.core.XLabel;
import de.fxdiagram.core.command.AbstractCommand;
import de.fxdiagram.core.command.CommandContext;
import de.fxdiagram.mapping.reconcile.AbstractLabelOwnerReconcileBehavior;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.ObservableList;
import org.eclipse.xtend.lib.annotations.FinalFieldsConstructor;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;

@FinalFieldsConstructor
@SuppressWarnings("all")
public class ConnectionLabelMorphCommand extends AbstractCommand implements AbstractLabelOwnerReconcileBehavior.AddKeepRemoveAcceptor {
  private final XConnection host;
  
  private List<XConnectionLabel> oldLabels;
  
  private List<XConnectionLabel> newLabels = CollectionLiterals.<XConnectionLabel>newArrayList();
  
  @Override
  public void add(final XLabel label) {
    this.newLabels.add(((XConnectionLabel) label));
  }
  
  @Override
  public void remove(final XLabel label) {
  }
  
  @Override
  public void keep(final XLabel label) {
    this.newLabels.add(((XConnectionLabel) label));
  }
  
  public boolean isEmpty() {
    List<XConnectionLabel> _elvis = null;
    if (this.oldLabels != null) {
      _elvis = this.oldLabels;
    } else {
      ObservableList<XConnectionLabel> _labels = this.host.getLabels();
      _elvis = _labels;
    }
    return Objects.equal(this.newLabels, _elvis);
  }
  
  @Override
  public void execute(final CommandContext context) {
    ObservableList<XConnectionLabel> _labels = this.host.getLabels();
    ArrayList<XConnectionLabel> _arrayList = new ArrayList<XConnectionLabel>(_labels);
    this.oldLabels = _arrayList;
    ObservableList<XConnectionLabel> _labels_1 = this.host.getLabels();
    _labels_1.setAll(this.newLabels);
  }
  
  @Override
  public void undo(final CommandContext context) {
    ObservableList<XConnectionLabel> _labels = this.host.getLabels();
    _labels.setAll(this.oldLabels);
  }
  
  @Override
  public void redo(final CommandContext context) {
    ObservableList<XConnectionLabel> _labels = this.host.getLabels();
    _labels.setAll(this.newLabels);
  }
  
  public ConnectionLabelMorphCommand(final XConnection host) {
    super();
    this.host = host;
  }
}
