package de.fxdiagram.idea;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import de.fxdiagram.core.*;
import de.fxdiagram.core.command.ParallelAnimationCommand;
import de.fxdiagram.core.command.SelectAndRevealCommand;
import de.fxdiagram.core.layout.LayoutType;
import de.fxdiagram.core.layout.Layouter;
import de.fxdiagram.core.model.DomainObjectProvider;
import de.fxdiagram.core.services.ClassLoaderProvider;
import de.fxdiagram.core.tools.actions.*;
import de.fxdiagram.lib.actions.UndoRedoPlayerAction;
import de.fxdiagram.mapping.*;
import javafx.util.Duration;

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
        diagramActionRegistry.operator_add(new SelectAllAction());
        diagramActionRegistry.operator_add(new ZoomToFitAction());
        diagramActionRegistry.operator_add(new NavigatePreviousAction());
        diagramActionRegistry.operator_add(new NavigateNextAction());
        diagramActionRegistry.operator_add(new FullScreenAction());
        return root;
    }

    public <T> void revealElement(T element, MappingCall<?, ? super T> mappingCall) {
        InterpreterContext interpreterContext = new InterpreterContext(root.getDiagram());
        if(mappingCall instanceof DiagramMappingCall<?, ?>) {
            interpreterContext.setIsReplaceRootDiagram(false);
            configInterpreter.execute((DiagramMappingCall<?, T>)mappingCall, element, interpreterContext);
            interpreterContext.executeCommands(root.getCommandStack());
        } else if(mappingCall instanceof NodeMappingCall<?, ?>) {
            configInterpreter.execute((NodeMappingCall<?, T>)mappingCall, element, interpreterContext, true);
            interpreterContext.executeCommands(root.getCommandStack());
        } else if(mappingCall instanceof ConnectionMappingCall<?, ?>) {
            ConnectionMapping<?> mapping = (ConnectionMapping<?>) mappingCall.getMapping();
            if(mapping.getSource() != null && mapping.getTarget() != null) {
                configInterpreter.execute((ConnectionMappingCall<?, T>) mappingCall, element, null, interpreterContext, true);
                interpreterContext.executeCommands(root.getCommandStack());
            }
        }
        IMappedElementDescriptor<T> descriptor =
                mappingCall
                        .getMapping().getConfig()
                        .getDomainObjectProvider()
                        .createMappedElementDescriptor(element, (AbstractMapping<T>)mappingCall.getMapping());
        XShape centerShape = findShape(interpreterContext.getDiagram(), descriptor);
        ParallelAnimationCommand command = new ParallelAnimationCommand();
        if(interpreterContext.needsLayoutCommand())
            command.operator_add(new Layouter().createLayoutCommand(LayoutType.DOT, interpreterContext.getDiagram(), Duration.millis(500), centerShape));
        command.operator_add(new SelectAndRevealCommand(root, d -> d.equals(centerShape)));
        root.getCommandStack().execute(command);
    }

    private <T> XShape findShape(XDiagram diagram, IMappedElementDescriptor<T> descriptor) {
        Optional<XNode> node = diagram.getNodes().stream().filter(n -> n.getDomainObjectDescriptor().equals(descriptor)).findFirst();
        if(node.isPresent())
            return node.get();
        Optional<XConnection> connection = diagram.getConnections().stream().filter(n -> n.getDomainObjectDescriptor().equals(descriptor)).findFirst();
        return connection.isPresent() ? connection.get() : null;
    }

}
