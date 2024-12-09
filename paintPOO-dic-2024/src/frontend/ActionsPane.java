package frontend;

import backend.CanvasState;
import backend.Layer;
import backend.model.*;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Pair;
import javafx.scene.control.Label; 
import javafx.scene.control.Button;

public class ActionsPane extends BorderPane{

    private final double DUPLICATION_OFFSET = 10.0;

    private CanvasState canvasState;
	private PaintPane paintPane;

    Label actionsLabel = new Label("Acciones");

	Button turnRightButton = new Button("Girar D");
    Button flipHorButton = new Button("Voltear H");
    Button flipVerButton = new Button("Voltear V");
    Button duplicateButton = new Button("Duplicar");
    Button divideButton = new Button("Dividir");

    public ActionsPane(CanvasState canvasState, PaintPane paintPane) {
        this.canvasState = canvasState;
		this.paintPane = paintPane;	

        // Right-side buttons
		Button[] ActionArr = {turnRightButton, flipHorButton, flipVerButton, duplicateButton, divideButton};
        for (Button action : ActionArr) {
            action.setMinWidth(90);
            action.setCursor(Cursor.HAND);
        }

        VBox buttonsActionBox = new VBox(10);
		buttonsActionBox.getChildren().add(actionsLabel);
        buttonsActionBox.getChildren().addAll(ActionArr);
        buttonsActionBox.setPadding(new Insets(5));
        buttonsActionBox.setStyle("-fx-background-color: #999");
        buttonsActionBox.setPrefWidth(100);	

        // Handle button actions
        turnRightButton.setOnAction(event -> rotateSelectedFigure());
        flipHorButton.setOnAction(event -> flipHorizontal());
        flipVerButton.setOnAction(event -> flipVertical());
        duplicateButton.setOnAction(event -> duplicateSelectedFigure());
        divideButton.setOnAction(event -> divideSelectedFigure());

        setRight(buttonsActionBox);
    }

    private void copyFigure(Figure f){
		paintPane.getSelectedLayer().addFigure(f);
		paintPane.colorMapPut(f, new Pair<>(paintPane.colorMapGet(paintPane.getSelectedFigure()).getKey(), paintPane.colorMapGet(paintPane.getSelectedFigure()).getValue()));
	}

	private void rotateSelectedFigure() {
        if (paintPane.getSelectedFigure() != null) {
            paintPane.getSelectedFigure().rotate();
            paintPane.redrawCanvas();
        }
    }

    private void flipHorizontal() {
        if (paintPane.getSelectedFigure() != null) {
            paintPane.getSelectedFigure().flipHorizontal();
			if(paintPane.getSelectedFigure() instanceof Rectangle r) {
				if (r.getAngle() == 0 || r.getAngle() == 180) {
					Pair<Color, Color> colorPair = paintPane.colorMapGet(paintPane.getSelectedFigure());
					paintPane.colorMapPut(paintPane.getSelectedFigure(), new Pair<>(colorPair.getValue(), colorPair.getKey()));
				}
			}
			paintPane.redrawCanvas();
        }
    }

    private void flipVertical() {
        if (paintPane.getSelectedFigure() != null) {
            paintPane.getSelectedFigure().flipVertical();
			if(paintPane.getSelectedFigure() instanceof Rectangle r) {
				if (r.getAngle() == 90 || r.getAngle() == 270) {
					Pair<Color, Color> colorPair = paintPane.colorMapGet(paintPane.getSelectedFigure());
					paintPane.colorMapPut(paintPane.getSelectedFigure(), new Pair<>(colorPair.getValue(), colorPair.getKey()));
				}
			}
            paintPane.redrawCanvas();
        }
    }

    private void duplicateSelectedFigure() {
        if (paintPane.getSelectedFigure() != null) {
            Figure duplicate = paintPane.getSelectedFigure().duplicate(DUPLICATION_OFFSET); 
            copyFigure(duplicate);
            paintPane.redrawCanvas();
        }
    }

    private void divideSelectedFigure() {
        if (paintPane.getSelectedFigure() != null) {
            Figure[] dividedFigures = paintPane.getSelectedFigure().divide();
			paintPane.getSelectedLayer().deleteFigure( paintPane.getSelectedFigure());
            for (Figure dividedFigure : dividedFigures) {
                copyFigure(dividedFigure);
            }
			paintPane.setSelectedFigure(null); 
            paintPane.redrawCanvas();

        }
    }
}
