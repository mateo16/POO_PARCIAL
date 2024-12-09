package frontend;

import backend.CanvasState;
import javafx.scene.layout.VBox;

public class MainFrame extends VBox {

    public MainFrame(CanvasState canvasState) {
        // Create the main menu bar and status pane
        getChildren().add(new AppMenuBar());

        // Create the StatusPane for displaying the status
        StatusPane statusPane = new StatusPane();
        getChildren().add(statusPane);

        // Create the PaintPane for drawing figures (this is the canvas)
        PaintPane paintPane = new PaintPane(canvasState, statusPane);
        
        // Create the LayerPane
        LayerPane layerPane = new LayerPane(canvasState, paintPane);
        getChildren().add(layerPane); // Add the LayerPane to the layout
        getChildren().add(paintPane); // Add the PaintPane to the layout

        // Optionally, set layout properties for the VBox
        setFillWidth(true); // Ensures the layout expands horizontally if needed
    }
}
