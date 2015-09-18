package de.fxdiagram.mapping.reconcile;

import com.google.common.base.Objects;
import de.fxdiagram.core.XLabel;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.command.AbstractCommand;
import de.fxdiagram.core.command.CommandContext;
import de.fxdiagram.mapping.reconcile.AbstractLabelOwnerReconcileBehavior;
import java.util.List;
import javafx.collections.ObservableList;
import org.eclipse.xtend.lib.annotations.FinalFieldsConstructor;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;

@FinalFieldsConstructor
@SuppressWarnings("all")
public class NodeLabelMorphCommand extends AbstractCommand implements AbstractLabelOwnerReconcileBehavior.AddKeepRemoveAcceptor {
  private final XNode host;
  
  private List<XLabel> oldLabels;
  
  private List<XLabel> newLabels = CollectionLiterals.<XLabel>newArrayList();
  
  @Override
  public void add(final XLabel label) {
    this.newLabels.add(label);
  }
  
  @Override
  public void remove(final XLabel label) {
  }
  
  @Override
  public void keep(final XLabel label) {
    this.newLabels.add(label);
  }
  
  public boolean isEmpty() {
    List<XLabel> _elvis = null;
    if (this.oldLabels != null) {
      _elvis = this.oldLabels;
    } else {
      ObservableList<XLabel> _labels = this.host.getLabels();
      _elvis = _labels;
    }
    return Objects.equal(this.newLabels, _elvis);
  }
  
  @Override
  public void execute(final CommandContext context) {
    ObservableList<XLabel> _labels = this.host.getLabels();
    this.oldLabels = _labels;
    ObservableList<XLabel> _labels_1 = this.host.getLabels();
    _labels_1.setAll(this.newLabels);
  }
  
  @Override
  public void undo(final CommandContext context) {
    ObservableList<XLabel> _labels = this.host.getLabels();
    _labels.setAll(this.oldLabels);
  }
  
  @Override
  public void redo(final CommandContext context) {
    ObservableList<XLabel> _labels = this.host.getLabels();
    _labels.setAll(this.newLabels);
  }
  
  public NodeLabelMorphCommand(final XNode host) {
    super();
    this.host = host;
  }
}
