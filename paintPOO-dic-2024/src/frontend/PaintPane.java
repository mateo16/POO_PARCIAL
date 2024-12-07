package frontend;

import backend.CanvasState;
import backend.Layer;
import backend.model.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.ArcType;
import javafx.util.Pair;
import javafx.scene.control.Label; 
import javafx.scene.control.ChoiceBox;
import javafx.collections.FXCollections;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.control.RadioButton;
import org.w3c.dom.css.Rect;

import java.util.*;

import javax.swing.text.LabelView;

public class PaintPane extends BorderPane {

	private final int DUPLICATION_OFFSET = 10;

	// BackEnd
	CanvasState canvasState;

	// Canvas y relacionados
	Canvas canvas = new Canvas(800, 600);
	GraphicsContext gc = canvas.getGraphicsContext2D();
	Color lineColor = Color.BLACK;
	Color defaultFillColor = Color.YELLOW;

	// Botones Barra Izquierda
	ToggleButton selectionButton = new ToggleButton("Seleccionar");
	ToggleButton rectangleButton = new ToggleButton("Rectángulo");
	ToggleButton circleButton = new ToggleButton("Círculo");
	ToggleButton squareButton = new ToggleButton("Cuadrado");
	ToggleButton ellipseButton = new ToggleButton("Elipse");
	ToggleButton deleteButton = new ToggleButton("Borrar");

	Label formatLabel = new Label("Formato");

	// Opciones de Formato
	String shadowStrings[] = { "Ninguna", "Simple", "Coloreada", "Simple Inversa", "Coloreada Inversa" };
	ChoiceBox<String> shadowChoiceBox = new ChoiceBox<>(FXCollections.observableArrayList(shadowStrings));
	CheckBox biseladoCheckBox = new CheckBox("Biselado");
	ColorPicker fillColorPicker = new ColorPicker(defaultFillColor);
	ColorPicker secondaryfillColorPicker = new ColorPicker(defaultFillColor);
	Button copiarFmtButton = new Button("Copiar Fmt.");

	Label actionsLabel = new Label("Acciones");

	Button turnRightButton = new Button("Girar D");
    Button flipHorButton = new Button("Voltear H");
    Button flipVerButton = new Button("Voltear V");
    Button duplicateButton = new Button("Duplicar");
    Button divideButton = new Button("Dividir");

	Button FrontButton = new Button("Traer al Frente");
    Button BackButton = new Button("Enviar al Fondo");
	Label layersLabel = new Label("Capas");
	//hacemos la choicebox vacia y luego agregamos las capas dinamicamente
	ChoiceBox<Layer> layersChoiceBox = new ChoiceBox<>();
    RadioButton showButton = new RadioButton("Mostrar");
    RadioButton hideButton = new RadioButton("Ocultar");
    Button addLayerButton = new Button("Agregar Capa");
	Button deleteLayerButton = new Button("Eliminar Capa");

	// Dibujar una figura
	Point startPoint;

	// Seleccionar una figura
	Figure selectedFigure;

	// StatusBar
	StatusPane statusPane;

	// Colores de relleno de cada figura
	Map<Figure, Pair<Color, Color>> figureColorMap = new HashMap<>();

	Figure figureCopyFormat = null;

	public PaintPane(CanvasState canvasState, StatusPane statusPane) {
		this.canvasState = canvasState;
		this.statusPane = statusPane;
		ToggleButton[] toolsArr = {selectionButton, rectangleButton, circleButton, squareButton, ellipseButton, deleteButton};
		ToggleGroup tools = new ToggleGroup();
		for (ToggleButton tool : toolsArr) {
			tool.setMinWidth(90);
			tool.setToggleGroup(tools);
			tool.setCursor(Cursor.HAND);
		}
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

		// Left-side buttons
		VBox buttonsBox = new VBox(10);
		buttonsBox.getChildren().addAll(toolsArr);
		buttonsBox.getChildren().addAll(formatLabel, shadowChoiceBox, biseladoCheckBox, fillColorPicker, secondaryfillColorPicker, copiarFmtButton);
		shadowChoiceBox.setValue(shadowStrings[0]);
		copiarFmtButton.setMinWidth(90);
		copiarFmtButton.setCursor(Cursor.HAND);
		buttonsBox.setPadding(new Insets(5));
		buttonsBox.setStyle("-fx-background-color: #999");
		buttonsBox.setPrefWidth(100);
		gc.setLineWidth(1);

		// Right-side buttons
		Button[] ManipulationArr = {turnRightButton, flipHorButton, flipVerButton, duplicateButton, divideButton};
        for (Button manip : ManipulationArr) {
            manip.setMinWidth(90);
            manip.setCursor(Cursor.HAND);
        }

        VBox buttonsManipulationBox = new VBox(10);
		buttonsManipulationBox.getChildren().add(actionsLabel);
        buttonsManipulationBox.getChildren().addAll(ManipulationArr);
        buttonsManipulationBox.setPadding(new Insets(5));
        buttonsManipulationBox.setStyle("-fx-background-color: #999");
        buttonsManipulationBox.setPrefWidth(100);

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

        // Handle button actions
        turnRightButton.setOnAction(event -> rotateSelectedFigure());
        flipHorButton.setOnAction(event -> flipHorizontal());
        flipVerButton.setOnAction(event -> flipVertical());
        duplicateButton.setOnAction(event -> duplicateSelectedFigure());
        divideButton.setOnAction(event -> divideSelectedFigure());
		copiarFmtButton.setOnAction(event -> copyFigureFormat());
		FrontButton.setOnAction(event -> setFrontWrapper());
		BackButton.setOnAction(event -> setBackWrapper());
		addLayerButton.setOnAction(event -> addLayer());
		deleteLayerButton.setOnAction(event -> deleteLayer());
		showButton.setOnAction(event -> showLayer());
		hideButton.setOnAction(event -> hideLayer());
		shadowChoiceBox.setOnAction(event -> changeShadow());
		biseladoCheckBox.setOnAction(event -> changeBiselado());
		fillColorPicker.setOnAction(event -> changeFillColor());
		secondaryfillColorPicker.setOnAction(event -> changeSecondaryFillColor());



		canvas.setOnMousePressed(event -> {
			startPoint = new Point(event.getX(), event.getY());
		});

		canvas.setOnMouseReleased(event -> {
			Point endPoint = new Point(event.getX(), event.getY());
			if(startPoint == null) {
				return ;
			}
			if(endPoint.getX() < startPoint.getX() || endPoint.getY() < startPoint.getY()) {
				return ;
			}
			Figure newFigure = null;
			ShadowType shadowType = getShadowType();
			if(rectangleButton.isSelected()) {
				newFigure = new Rectangle(shadowType, biseladoCheckBox.isSelected(), startPoint, endPoint);
			}
			else if(circleButton.isSelected()) {
				double circleRadius = Math.abs(endPoint.getX() - startPoint.getX());
				newFigure = new Circle(shadowType, biseladoCheckBox.isSelected(), startPoint, circleRadius);
			} else if(squareButton.isSelected()) {
				double size = Math.abs(endPoint.getX() - startPoint.getX());
				newFigure = new Square(shadowType, biseladoCheckBox.isSelected(), startPoint, size);
			} else if(ellipseButton.isSelected()) {
				Point centerPoint = new Point(Math.abs(endPoint.x + startPoint.x) / 2, (Math.abs((endPoint.y + startPoint.y)) / 2));
				double sMayorAxis = Math.abs(endPoint.x - startPoint.x);
				double sMinorAxis = Math.abs(endPoint.y - startPoint.y);
				newFigure = new Ellipse(shadowType, biseladoCheckBox.isSelected(), centerPoint, sMayorAxis, sMinorAxis);
			} else {
				return ;
			}
			figureColorMap.put(newFigure, new Pair<>(fillColorPicker.getValue(), secondaryfillColorPicker.getValue()));
			canvasState.addFigure(layersChoiceBox.getValue(), newFigure);
			startPoint = null;
			redrawCanvas();
		});

		canvas.setOnMouseMoved(event -> {
			Point eventPoint = new Point(event.getX(), event.getY());
			boolean found = false;
			StringBuilder label = new StringBuilder();
			for(Figure figure : canvasState.figuresShown()) {
				if(figureBelongs(figure, eventPoint)) {
					found = true;
					label.append(figure.toString());
				}
			}
			if(found) {
				statusPane.updateStatus(label.toString());
			} else {
				statusPane.updateStatus(eventPoint.toString());
			}
		});

		canvas.setOnMouseClicked(event -> {
			if(selectionButton.isSelected()) {
				Point eventPoint = new Point(event.getX(), event.getY());
				boolean found = false;
				StringBuilder label = new StringBuilder("Se seleccionó: ");
				for (Figure figure : canvasState.figuresShown()) {
					if(figureBelongs(figure, eventPoint)) {
						found = true;
						selectedFigure = figure;
						label.append(figure.toString());
					}
				}

				if(figureCopyFormat != null && selectedFigure != null){
					selectedFigure.setShadowType(figureCopyFormat.getShadowType());
					selectedFigure.setBiselado(figureCopyFormat.getBiselado());
					figureColorMap.put(selectedFigure, figureColorMap.get(figureCopyFormat));
					figureCopyFormat = null;
				}

				if (found) {
					statusPane.updateStatus(label.toString());
				} else {
					selectedFigure = null;
					statusPane.updateStatus("Ninguna figura encontrada");
				}
				redrawCanvas();
			}
		});

		canvas.setOnMouseDragged(event -> {
			if(selectionButton.isSelected()) {
				Point eventPoint = new Point(event.getX(), event.getY());
				double diffX = (eventPoint.getX() - startPoint.getX()) / 100;
				double diffY = (eventPoint.getY() - startPoint.getY()) / 100;
				if(selectedFigure instanceof Rectangle) {
					Rectangle rectangle = (Rectangle) selectedFigure;
					rectangle.getTopLeft().x += diffX;
					rectangle.getBottomRight().x += diffX;
					rectangle.getTopLeft().y += diffY;
					rectangle.getBottomRight().y += diffY;
				} else if(selectedFigure instanceof Circle) {
					Circle circle = (Circle) selectedFigure;
					circle.getCenterPoint().x += diffX;
					circle.getCenterPoint().y += diffY;
				} else if(selectedFigure instanceof Square) {
					Square square = (Square) selectedFigure;
					square.getTopLeft().x += diffX;
					square.getBottomRight().x += diffX;
					square.getTopLeft().y += diffY;
					square.getBottomRight().y += diffY;
				} else if(selectedFigure instanceof Ellipse) {
					Ellipse ellipse = (Ellipse) selectedFigure;
					ellipse.getCenterPoint().x += diffX;
					ellipse.getCenterPoint().y += diffY;
				}
				redrawCanvas();
			}
		});

		deleteButton.setOnAction(event -> {
			if (selectedFigure != null) {
				canvasState.deleteFigure(layersChoiceBox.getValue(), selectedFigure);
				selectedFigure = null;
				redrawCanvas();
			}
		});

		setLeft(buttonsBox);
		setCenter(canvas);
		setRight(buttonsManipulationBox);
		setTop(layerControlsBox);
		setBottom(statusPane);
	}

	ShadowType getShadowType() {
		String selectedValue = shadowChoiceBox.getValue();
		switch (selectedValue) {
			case "Simple":
				return ShadowType.SIMPLE;
			case "Coloreada":
				return ShadowType.COLOREADA;
			case "Simple Inversa":
				return ShadowType.INVSIMPLE;
			case "Coloreada Inversa":
				return ShadowType.INVCOLOREADA;
			case "Ninguna":
			default:
				return ShadowType.NINGUNA;
		}
	}

	private void copyFigure(Figure f){
		canvasState.addFigure(layersChoiceBox.getValue(), f);
		figureColorMap.put(f, new Pair<>(figureColorMap.get(selectedFigure).getKey(), figureColorMap.get(selectedFigure).getValue()));
	}

	private void rotateSelectedFigure() {
        if (selectedFigure != null) {
            selectedFigure.rotate();
			//Pair<Color, Color> colorPair = figureColorMap.get(selectedFigure);
			//figureColorMap.put(selectedFigure, new Pair<>(colorPair.getValue(), colorPair.getKey()));
            redrawCanvas();
        }
    }

    private void flipHorizontal() {
        if (selectedFigure != null) {
            selectedFigure.flipHorizontal();
			if(selectedFigure instanceof Rectangle r) {
				if (r.getAngle() == 0 || r.getAngle() == 180) {
					Pair<Color, Color> colorPair = figureColorMap.get(selectedFigure);
					figureColorMap.put(selectedFigure, new Pair<>(colorPair.getValue(), colorPair.getKey()));
				}
			}
			redrawCanvas();
        }
    }

    private void flipVertical() {
        if (selectedFigure != null) {
            selectedFigure.flipVertical();
			if(selectedFigure instanceof Rectangle r) {
				if (r.getAngle() == 90 || r.getAngle() == 270) {
					Pair<Color, Color> colorPair = figureColorMap.get(selectedFigure);
					figureColorMap.put(selectedFigure, new Pair<>(colorPair.getValue(), colorPair.getKey()));
				}
			}
            redrawCanvas();
        }
    }

    private void duplicateSelectedFigure() {
        if (selectedFigure != null) {
            Figure duplicate = selectedFigure.duplicate(DUPLICATION_OFFSET); 
            copyFigure(duplicate);
            redrawCanvas();
        }
    }

    private void divideSelectedFigure() {
        if (selectedFigure != null) {
            Figure[] dividedFigures = selectedFigure.divide();
			canvasState.deleteFigure(layersChoiceBox.getValue(), selectedFigure);
            for (Figure dividedFigure : dividedFigures) {
                copyFigure(dividedFigure);
            }
            redrawCanvas();
        }
    }

	private void copyFigureFormat() {
		figureCopyFormat = selectedFigure;
    }

	private void setFrontWrapper() {
		if(selectedFigure != null) {
			canvasState.setFront(layersChoiceBox.getValue(), selectedFigure);
			redrawCanvas();
		}
	}

	private void setBackWrapper() {
		if(selectedFigure != null) {
			canvasState.setBack(layersChoiceBox.getValue(), selectedFigure);
			redrawCanvas();
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

		redrawCanvas();
	}

	private void showLayer() {
		canvasState.showLayer(layersChoiceBox.getValue());

		redrawCanvas();
	}

	private void hideLayer() {
		canvasState.hideLayer(layersChoiceBox.getValue());

		redrawCanvas();
	}

	private void changeShadow() {
		if(selectionButton.isSelected() && selectedFigure != null) {
			canvasState.changeShadow(selectedFigure, getShadowType());
		}
		redrawCanvas();
	}
	private void changeBiselado() {
		if(selectionButton.isSelected() && selectedFigure != null) {
			canvasState.changeBiselado(selectedFigure, biseladoCheckBox.isSelected());
		}
		redrawCanvas();

	}
	private void changeFillColor() {
		if(selectionButton.isSelected() && selectedFigure != null) {
			figureColorMap.put(selectedFigure, new Pair<>(fillColorPicker.getValue(), secondaryfillColorPicker.getValue()));
		}
		redrawCanvas();
	}
	private void changeSecondaryFillColor() {
		if(selectionButton.isSelected() && selectedFigure != null) {
			figureColorMap.put(selectedFigure, new Pair<>(fillColorPicker.getValue(), secondaryfillColorPicker.getValue()));
		}
		redrawCanvas();
	}
    
	void drawFigure(Figure figure, double offset){
		
		if(figure instanceof Rectangle) {
			Rectangle rectangle = (Rectangle) figure;
			gc.fillRect(rectangle.getTopLeft().getX() + offset, rectangle.getTopLeft().getY() + offset,
					Math.abs(rectangle.getTopLeft().getX() - rectangle.getBottomRight().getX()), Math.abs(rectangle.getTopLeft().getY() - rectangle.getBottomRight().getY()));
		} else if(figure instanceof Ellipse) {
			Ellipse ellipse = (Ellipse) figure;
			gc.fillOval(ellipse.getCenterPoint().getX() - (ellipse.getsMayorAxis() / 2) + offset, ellipse.getCenterPoint().getY() - (ellipse.getsMinorAxis() / 2) + offset, ellipse.getsMayorAxis(), ellipse.getsMinorAxis());
		}
	}

	void drawBorder(Figure figure){
		Color chosenColor;
		if(figure == selectedFigure) {
			chosenColor = Color.RED;
		} else {
			chosenColor = lineColor;
		}

		if(figure instanceof Rectangle) {
			Rectangle rectangle = (Rectangle) figure;
			if(figure.getBiselado()){
				double x = rectangle.getTopLeft().getX();
				double y = rectangle.getTopLeft().getY();
				double width = Math.abs(x - rectangle.getBottomRight().getX());
				double height = Math.abs(y - rectangle.getBottomRight().getY());
				gc.setLineWidth(10);
				gc.setStroke(Color.LIGHTGRAY);
				gc.strokeLine(x, y, x + width, y);
				gc.strokeLine(x, y, x, y + height);
				gc.setStroke(Color.BLACK);
				gc.strokeLine(x + width, y, x + width, y + height);
				gc.strokeLine(x, y + height, x + width, y + height);
			}
			gc.setLineWidth(1);
			gc.setStroke(chosenColor);
			gc.strokeRect(rectangle.getTopLeft().getX(), rectangle.getTopLeft().getY(),
					Math.abs(rectangle.getTopLeft().getX() - rectangle.getBottomRight().getX()), Math.abs(rectangle.getTopLeft().getY() - rectangle.getBottomRight().getY()));
			
		} else if(figure instanceof Ellipse) {
			Ellipse ellipse = (Ellipse) figure;
			if(figure.getBiselado()){
				double arcX = ellipse.getCenterPoint().getX() - (ellipse.getsMayorAxis() / 2);
				double arcY = ellipse.getCenterPoint().getY() - (ellipse.getsMinorAxis() / 2);
				gc.setLineWidth(10);
				gc.setStroke(Color.LIGHTGRAY);
				gc.strokeArc(arcX, arcY, ellipse.getsMayorAxis(), ellipse.getsMinorAxis(), 45, 180, ArcType.OPEN);
				gc.setStroke(Color.BLACK);
				gc.strokeArc(arcX, arcY, ellipse.getsMayorAxis(), ellipse.getsMinorAxis(), 225, 180, ArcType.OPEN);
			}
			gc.setLineWidth(1);
			gc.setStroke(chosenColor);
			gc.strokeOval(ellipse.getCenterPoint().getX() - (ellipse.getsMayorAxis() / 2), ellipse.getCenterPoint().getY() - (ellipse.getsMinorAxis() / 2), ellipse.getsMayorAxis(), ellipse.getsMinorAxis());		
		}
	}

	void redrawCanvas() {
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		for(Figure figure : canvasState.figuresShown()) {
			Color firstFillColor = figureColorMap.get(figure).getKey();
			Color secondFillColor = figureColorMap.get(figure).getValue();
			
			if(figure.getShadowType().getColored()){
				gc.setFill(firstFillColor.darker());
			}else{
				gc.setFill(Color.GRAY);
			}
			
			drawFigure(figure, figure.getShadowType().getOffset());
			

			drawBorder(figure);

			if(figure instanceof Rectangle) {
				double angleRadian = Math.toRadians(((Rectangle) figure).getAngle());
				LinearGradient linearGradient = new LinearGradient(
						0.5 - 0.5 * Math.cos(angleRadian),
						0.5 - 0.5 * Math.sin(angleRadian),
						0.5 + 0.5 * Math.cos(angleRadian),
						0.5 + 0.5 * Math.sin(angleRadian),
						true,
      			CycleMethod.NO_CYCLE,
      			new Stop(0, firstFillColor),
      			new Stop(1, secondFillColor));
				gc.setFill(linearGradient);
			}else{
				RadialGradient radialGradient = new RadialGradient(0, 0, 0.5, 0.5, 0.5, true,
				CycleMethod.NO_CYCLE,
				new Stop(0, firstFillColor),
				new Stop(1, secondFillColor));
			  gc.setFill(radialGradient);
			}

			drawFigure(figure, 0);

		}
	}

	boolean figureBelongs(Figure figure, Point eventPoint) {
		boolean found = false;
		if(figure instanceof Rectangle) {
			Rectangle rectangle = (Rectangle) figure;
			found = eventPoint.getX() > rectangle.getTopLeft().getX() && eventPoint.getX() < rectangle.getBottomRight().getX() &&
					eventPoint.getY() > rectangle.getTopLeft().getY() && eventPoint.getY() < rectangle.getBottomRight().getY();
		} else if(figure instanceof Ellipse) {
			Ellipse ellipse = (Ellipse) figure;
			// Nota: Fórmula aproximada. No es necesario corregirla.
			found = ((Math.pow(eventPoint.getX() - ellipse.getCenterPoint().getX(), 2) / Math.pow(ellipse.getsMayorAxis(), 2)) +
					(Math.pow(eventPoint.getY() - ellipse.getCenterPoint().getY(), 2) / Math.pow(ellipse.getsMinorAxis(), 2))) <= 0.30;
		}
		return found;
	}

}
