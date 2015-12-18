package de.fxdiagram.idea;

import com.google.common.collect.Iterables;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiTreeChangeAdapter;
import com.intellij.psi.PsiTreeChangeEvent;
import com.intellij.psi.impl.source.resolve.ResolveCache;
import de.fxdiagram.core.*;
import de.fxdiagram.core.behavior.ReconcileBehavior;
import de.fxdiagram.core.command.ParallelAnimationCommand;
import de.fxdiagram.core.command.SelectAndRevealCommand;
import de.fxdiagram.core.layout.LayoutType;
import de.fxdiagram.core.layout.Layouter;
import de.fxdiagram.core.model.DomainObjectProvider;
import de.fxdiagram.core.services.ClassLoaderProvider;
import de.fxdiagram.core.tools.actions.*;
import de.fxdiagram.lib.actions.UndoRedoPlayerAction;
import de.fxdiagram.mapping.*;
import de.fxdiagram.mapping.execution.EntryCall;
import de.fxdiagram.mapping.execution.InterpreterContext;
import de.fxdiagram.mapping.execution.XDiagramConfigInterpreter;
import javafx.util.Duration;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Jan Koehnlein
 */
public class FXDiagramPane {

    public static FXDiagramPane getInstance(Project project) {
        return ServiceManager.getService(project, FXDiagramPane.class);
    }

    private XRoot root;

    private XDiagramConfigInterpreter configInterpreter = new XDiagramConfigInterpreter();

    public XRoot createRoot() {
        root = new XRoot();
        root.setRootDiagram(new XDiagram());
        List<DomainObjectProvider> domainObjectProviders = root.getDomainObjectProviders();
        domainObjectProviders.add(new ClassLoaderProvider());
        for(XDiagramConfig config: XDiagramConfig.Registry.getInstance().getConfigurations()) {
            DomainObjectProvider dop = config.getDomainObjectProvider();
            if(!domainObjectProviders.contains(dop))
                domainObjectProviders.add(dop);
        }
        DiagramActionRegistry diagramActionRegistry = root.getDiagramActionRegistry();
        diagramActionRegistry.operator_add(new CenterAction());
        diagramActionRegistry.operator_add(new DeleteAction());
        diagramActionRegistry.operator_add(new LayoutAction(LayoutType.DOT));
        diagramActionRegistry.operator_add(new ExportSvgAction());
        diagramActionRegistry.operator_add(new RedoAction());
        diagramActionRegistry.operator_add(new UndoRedoPlayerAction());
        diagramActionRegistry.operator_add(new UndoAction());
        diagramActionRegistry.operator_add(new RevealAction());
        diagramActionRegistry.operator_add(new LoadAction());
        diagramActionRegistry.operator_add(new SaveAction());
        diagramActionRegistry.operator_add(new ReconcileAction());
        diagramActionRegistry.operator_add(new SelectAllAction());
        diagramActionRegistry.operator_add(new ZoomToFitAction());
        diagramActionRegistry.operator_add(new NavigatePreviousAction());
        diagramActionRegistry.operator_add(new NavigateNextAction());
        diagramActionRegistry.operator_add(new FullScreenAction());
        return root;
    }
    public <T> void revealElement(T element, EntryCall<? super T> entryCall) {
        InterpreterContext interpreterContext = new InterpreterContext(root.getDiagram());
        entryCall.execute(element, configInterpreter, interpreterContext);
        interpreterContext.executeCommands(root.getCommandStack());

        IMappedElementDescriptor<T> descriptor = createMappedDescriptor(element);
        XShape centerShape = findShape(interpreterContext.getDiagram(), descriptor);
        ParallelAnimationCommand command = new ParallelAnimationCommand();
        if(interpreterContext.needsLayoutCommand())
            command.operator_add(new Layouter().createLayoutCommand(interpreterContext.getDiagram().getLayoutParameters(), interpreterContext.getDiagram(), Duration.millis(500), centerShape));
        command.operator_add(new SelectAndRevealCommand(root, d -> d.equals(centerShape)));
        root.getCommandStack().execute(command);
    }

    protected <T extends Object, U extends Object> IMappedElementDescriptor<T> createMappedDescriptor(final T domainObject) {
        IMappedElementDescriptor<T> _xblockexpression = null;
        for(XDiagramConfig config: XDiagramConfig.Registry.getInstance().getConfigurations()) {
            AbstractMapping<T> mapping = Iterables.getFirst(config.getMappings(domainObject), null);
            if(mapping != null)
              return config.getDomainObjectProvider().createMappedElementDescriptor(domainObject, mapping);
        }
        return null;
    }

    private <T> XShape findShape(XDiagram diagram, IMappedElementDescriptor<T> descriptor) {
        Optional<XNode> node = diagram.getNodes().stream().filter(n -> n.getDomainObjectDescriptor().equals(descriptor)).findFirst();
        if(node.isPresent())
            return node.get();
        Optional<XConnection> connection = diagram.getConnections().stream().filter(n -> n.getDomainObjectDescriptor().equals(descriptor)).findFirst();
        return connection.isPresent() ? connection.get() : null;
    }

    public void refreshUpdateState() {
        XDiagram diagram = root.getDiagram();
        List<XDomainObjectShape> allShapes = new ArrayList<>();
        allShapes.addAll(diagram.getNodes());
        allShapes.addAll(diagram.getConnections());
        allShapes.forEach(it -> {
            ReconcileBehavior behavior = it.getBehavior(ReconcileBehavior.class);
            if (behavior != null) {
                behavior.showDirtyState(behavior.getDirtyState());
            }
        });
    }
}
