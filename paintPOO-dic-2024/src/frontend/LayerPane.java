package frontend;

import backend.CanvasState;
import backend.Layer;
import backend.model.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.control.Label; 
import javafx.scene.control.ChoiceBox;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.control.RadioButton;

public class LayerPane extends BorderPane {

	private CanvasState canvasState;
	private PaintPane paintPane;

	Button FrontButton = new Button("Traer al Frente");
    Button BackButton = new Button("Enviar al Fondo");
	Label layersLabel = new Label("Capas");
	//hacemos la choicebox vacia y luego agregamos las capas dinamicamente
	ChoiceBox<Layer> layersChoiceBox = new ChoiceBox<>();
    RadioButton showButton = new RadioButton("Mostrar");
    RadioButton hideButton = new RadioButton("Ocultar");
    Button addLayerButton = new Button("Agregar Capa");
	Button deleteLayerButton = new Button("Eliminar Capa");

	public LayerPane(CanvasState canvasState, PaintPane paintPane) {
		this.canvasState = canvasState;
		this.paintPane = paintPane;	

		// Top buttons
		Button[] layerButtons = {FrontButton, BackButton, addLayerButton, deleteLayerButton};
		for (Button layerButton : layerButtons) {
			layerButton.setMinWidth(90);
			layerButton.setCursor(Cursor.HAND);
		}

		ToggleGroup visibilityGroup = new ToggleGroup();
		showButton.setToggleGroup(visibilityGroup);
		hideButton.setToggleGroup(visibilityGroup);
	
		HBox layerControlsBox = new HBox(10);
		layerControlsBox.getChildren().addAll(FrontButton, BackButton, layersLabel, layersChoiceBox, showButton, hideButton, addLayerButton, deleteLayerButton);
		//que cuando se cree se seleccione la primer capa
		layersChoiceBox.setValue(canvasState.getLayers().get(0));
		layerControlsBox.setPadding(new Insets(5));
		layerControlsBox.setAlignment(Pos.CENTER);
		layerControlsBox.setStyle("-fx-background-color: #999");

		//canvasState ya inicia con 3 capas, las agrego al choicebox
		layersChoiceBox.getItems().addAll(FXCollections.observableArrayList(canvasState.getLayers()));
		layersChoiceBox.setValue(canvasState.getLayers().getFirst());
				
		//se muestra el estado de la primer capa al iniciar la aplicacion
		//(luego se hace automaticamente)
		showButton.fire();
				
		//actualiza los botones de mostrar y ocultar segun el estado de la capa
		layersChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldLayer, newLayer) -> {
			if(newLayer.getShown()) {
				showButton.fire();
			} else {
				hideButton.fire();
			}
		});

		// Handle button actions
		FrontButton.setOnAction(event -> setFrontWrapper());
		BackButton.setOnAction(event -> setBackWrapper());
		addLayerButton.setOnAction(event -> addLayer());
		deleteLayerButton.setOnAction(event -> deleteLayer());
		showButton.setOnAction(event -> showLayer());
		hideButton.setOnAction(event -> hideLayer());

		setTop(layerControlsBox);

		paintPane.setSelectedLayer(layersChoiceBox.getValue());
		layersChoiceBox.setOnAction(event -> {
			paintPane.setSelectedLayer(layersChoiceBox.getValue());
		});
	}

	private void setFrontWrapper() {
		if(paintPane.getSelectedFigure() != null) {
			layersChoiceBox.getValue().setFront(paintPane.getSelectedFigure());
			paintPane.redrawCanvas();
		}
	}

	private void setBackWrapper() {
		if(paintPane.getSelectedFigure() != null) {
			layersChoiceBox.getValue().setBack(paintPane.getSelectedFigure());
			paintPane.redrawCanvas();
		}
	}

	private void addLayer() {
		canvasState.addLayer();
		layersChoiceBox.getItems().add(canvasState.getLayers().getLast());

		layersChoiceBox.setValue(canvasState.getLayers().getLast());
	}

	private void deleteLayer() {
		Layer currentLayer = layersChoiceBox.getValue();

		if(currentLayer.getNum() > 3) {
			layersChoiceBox.getItems().remove(currentLayer);
			canvasState.deleteLayer(currentLayer);
			layersChoiceBox.setValue(canvasState.getLayers().getFirst());
		}

		paintPane.redrawCanvas();
	}

	private void showLayer() {
		layersChoiceBox.getValue().setShown(true);
		paintPane.redrawCanvas();
	}

	private void hideLayer() {
		layersChoiceBox.getValue().setShown(false);
		paintPane.redrawCanvas();
	}
	
}