package frontend;

import backend.CanvasState;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;

public class MainFrame extends BorderPane  {

    public MainFrame(CanvasState canvasState) {
        // Create the main menu bar and status pane
        getChildren().add(new AppMenuBar());

        // Create the StatusPane for displaying the status
        StatusPane statusPane = new StatusPane();
        setBottom(statusPane);

        // Create the PaintPane for drawing figures (this is the canvas)
        PaintPane paintPane = new PaintPane(canvasState, statusPane);
        setCenter(paintPane);

        // Create the ActionsPane
        ActionsPane actionsPane = new ActionsPane(canvasState, paintPane);
        setRight(actionsPane); 
        
        // Create the LayerPane
        LayerPane layerPane = new LayerPane(canvasState, paintPane);
        setTop(layerPane);

    }
}
