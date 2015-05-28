package de.fxdiagram.idea.psi;

import de.fxdiagram.core.XShape;
import de.fxdiagram.mapping.AbstractDiagramConfig;
import de.fxdiagram.mapping.IMappedElementDescriptorProvider;
import de.fxdiagram.mapping.behavior.OpenElementInEditorBehavior;

/**
 * @author Jan Koehnlein
 */
public abstract class AbstractPsiDiagramConfig extends AbstractDiagramConfig {

    @Override
    public abstract String getID();

    @Override
    public abstract String getLabel();

    @Override
    protected IMappedElementDescriptorProvider createDomainObjectProvider() {
        return new PsiDomainObjectProvider();
    }

    @Override
    public void initialize(XShape shape) {
        shape.addBehavior(new OpenElementInEditorBehavior(shape));
    }
}
