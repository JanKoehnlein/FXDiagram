package de.fxdiagram.lib.chooser;

import de.fxdiagram.core.XNode;
import de.fxdiagram.lib.chooser.AbstractBaseChooser;
import de.fxdiagram.lib.chooser.ChoiceGraphics;
import java.util.ArrayList;
import javafx.scene.Group;
import javafx.scene.Node;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtext.xbase.lib.Pure;

@SuppressWarnings("all")
public abstract class AbstractChoiceGraphics implements ChoiceGraphics {
  @Accessors
  private AbstractBaseChooser chooser;
  
  protected ArrayList<XNode> getChoiceNodes() {
    return this.chooser.getNodes();
  }
  
  protected Group getChoiceGroup() {
    return this.chooser.getGroup();
  }
  
  public void activate() {
  }
  
  public void nodeChosen(final XNode choice) {
  }
  
  public void relocateButtons(final Node minusButton, final Node plusButton) {
  }
  
  public boolean hasButtons() {
    return true;
  }
  
  @Pure
  public AbstractBaseChooser getChooser() {
    return this.chooser;
  }
  
  public void setChooser(final AbstractBaseChooser chooser) {
    this.chooser = chooser;
  }
}
