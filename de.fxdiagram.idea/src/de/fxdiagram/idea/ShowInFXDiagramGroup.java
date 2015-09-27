package de.fxdiagram.idea;

import com.google.common.collect.Iterables;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.extensions.ExtensionPointName;
import com.intellij.psi.PsiElement;
import de.fxdiagram.mapping.MappingCall;
import de.fxdiagram.mapping.XDiagramConfig;
import de.fxdiagram.mapping.execution.EntryCall;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static de.fxdiagram.idea.ActionUtil.getPsiElement;

/**
 * @author Jan Koehnlein
 */
public class ShowInFXDiagramGroup extends ActionGroup {

    @NotNull
    @Override
    public AnAction[] getChildren(AnActionEvent e) {
        if (e == null)
            return new AnAction[0];
        PsiElement element = getPsiElement(e);
        List<AnAction> actions = new ArrayList<>();

        XDiagramConfig.Registry instance = XDiagramConfig.Registry.getInstance();
        if (!instance.getConfigurations().iterator().hasNext()) {
            ExtensionPointName<XDiagramConfig> DIAGRAM_CONFIG_EP = ExtensionPointName.create("de.fxdiagram.idea.fxDiagramConfig");
            for (XDiagramConfig config : DIAGRAM_CONFIG_EP.getExtensions())
                instance.addConfig(config);
        }

        for (XDiagramConfig config : instance.getConfigurations()) {
            for (EntryCall<PsiElement> entryCall : config.getEntryCalls(element)) {
                actions.add(new ShowInFXDiagramAction(entryCall, element));
            }
        }
        return Iterables.toArray(actions, AnAction.class);
    }
}
