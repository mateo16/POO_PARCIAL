package frontend;

import backend.CanvasState;
import backend.model.*;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.control.Label; 
import javafx.scene.control.ChoiceBox;
import javafx.collections.FXCollections;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

import java.util.HashMap;
import java.util.Map;

import javax.swing.text.LabelView;

public class PaintPane extends BorderPane {

	private final int DUPLICATION_OFFSET = 2;

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


	Button turnRightButton = new Button("Girar D");
    Button flipHorButton = new Button("Voltear H");
    Button flipVerButton = new Button("Voltear V");
    Button duplicateButton = new Button("Duplicar");
    Button divideButton = new Button("Dividir");

	// Dibujar una figura
	Point startPoint;

	// Seleccionar una figura
	Figure selectedFigure;

	// StatusBar
	StatusPane statusPane;

	// Colores de relleno de cada figura
	Map<Figure, Color> figureColorMap = new HashMap<>();

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

		Button[] ManipulationArr = {turnRightButton, flipHorButton, flipVerButton, duplicateButton, divideButton};
        for (Button manip : ManipulationArr) {
            manip.setMinWidth(90);
            manip.setCursor(Cursor.HAND);
        }

        // Arrange buttons vertically
        VBox buttonsManipulationBox = new VBox(10);
        buttonsManipulationBox.getChildren().addAll(ManipulationArr);
        buttonsManipulationBox.getChildren().add(fillColorPicker);
        buttonsManipulationBox.setPadding(new Insets(5));
        buttonsManipulationBox.setStyle("-fx-background-color: #999");
        buttonsManipulationBox.setPrefWidth(100);
		HBox layout = new HBox();
		layout.getChildren().addAll(buttonsManipulationBox, canvas);
		setCenter(layout);

        // Handle button actions
        turnRightButton.setOnAction(event -> rotateSelectedFigure());
        flipHorButton.setOnAction(event -> flipHorizontal());
        flipVerButton.setOnAction(event -> flipVertical());
        duplicateButton.setOnAction(event -> duplicateSelectedFigure());
        divideButton.setOnAction(event -> divideSelectedFigure());

        

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
				newFigure = new Rectangle(shadowType, startPoint, endPoint);
			}
			else if(circleButton.isSelected()) {
				double circleRadius = Math.abs(endPoint.getX() - startPoint.getX());
				newFigure = new Circle(shadowType, startPoint, circleRadius);
			} else if(squareButton.isSelected()) {
				double size = Math.abs(endPoint.getX() - startPoint.getX());
				newFigure = new Square(shadowType, startPoint, size);
			} else if(ellipseButton.isSelected()) {
				Point centerPoint = new Point(Math.abs(endPoint.x + startPoint.x) / 2, (Math.abs((endPoint.y + startPoint.y)) / 2));
				double sMayorAxis = Math.abs(endPoint.x - startPoint.x);
				double sMinorAxis = Math.abs(endPoint.y - startPoint.y);
				newFigure = new Ellipse(shadowType, centerPoint, sMayorAxis, sMinorAxis);
			} else {
				return ;
			}
			figureColorMap.put(newFigure, fillColorPicker.getValue());
			canvasState.addFigure(newFigure);
			startPoint = null;
			redrawCanvas();
		});

		canvas.setOnMouseMoved(event -> {
			Point eventPoint = new Point(event.getX(), event.getY());
			boolean found = false;
			StringBuilder label = new StringBuilder();
			for(Figure figure : canvasState.figures()) {
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
				for (Figure figure : canvasState.figures()) {
					if(figureBelongs(figure, eventPoint)) {
						found = true;
						selectedFigure = figure;
						label.append(figure.toString());
					}
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
				canvasState.deleteFigure(selectedFigure);
				selectedFigure = null;
				redrawCanvas();
			}
		});

		setLeft(buttonsBox);
		setRight(canvas);
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

	void drawFigure(Figure figure, double offset){
		if(figure instanceof Rectangle) {
			Rectangle rectangle = (Rectangle) figure;
			gc.fillRect(rectangle.getTopLeft().getX() + offset, rectangle.getTopLeft().getY() + offset,
					Math.abs(rectangle.getTopLeft().getX() - rectangle.getBottomRight().getX()), Math.abs(rectangle.getTopLeft().getY() - rectangle.getBottomRight().getY()));
			gc.strokeRect(rectangle.getTopLeft().getX(), rectangle.getTopLeft().getY(),
					Math.abs(rectangle.getTopLeft().getX() - rectangle.getBottomRight().getX() + offset), Math.abs(rectangle.getTopLeft().getY() - rectangle.getBottomRight().getY() + offset));
		} else if(figure instanceof Circle) {
			Circle circle = (Circle) figure;
			double diameter = circle.getRadius() * 2;
			gc.fillOval(circle.getCenterPoint().getX() - circle.getRadius() + offset, circle.getCenterPoint().getY() - circle.getRadius() + offset, diameter, diameter);
			gc.strokeOval(circle.getCenterPoint().getX() - circle.getRadius() + offset, circle.getCenterPoint().getY() - circle.getRadius() + offset, diameter, diameter);
		} else if(figure instanceof Square) {
			Square square = (Square) figure;
			gc.fillRect(square.getTopLeft().getX() + offset, square.getTopLeft().getY() + offset,
					Math.abs(square.getTopLeft().getX() - square.getBottomRight().getX()), Math.abs(square.getTopLeft().getY() - square.getBottomRight().getY()));
			gc.strokeRect(square.getTopLeft().getX(), square.getTopLeft().getY(),
					Math.abs(square.getTopLeft().getX() - square.getBottomRight().getX()), Math.abs(square.getTopLeft().getY() - square.getBottomRight().getY()));
		} else if(figure instanceof Ellipse) {
			Ellipse ellipse = (Ellipse) figure;
			gc.strokeOval(ellipse.getCenterPoint().getX() - (ellipse.getsMayorAxis() / 2) + offset, ellipse.getCenterPoint().getY() - (ellipse.getsMinorAxis() / 2) + offset, ellipse.getsMayorAxis(), ellipse.getsMinorAxis());
			gc.fillOval(ellipse.getCenterPoint().getX() - (ellipse.getsMayorAxis() / 2) + offset, ellipse.getCenterPoint().getY() - (ellipse.getsMinorAxis() / 2) + offset, ellipse.getsMayorAxis(), ellipse.getsMinorAxis());
		}
	}
	private void rotateSelectedFigure() {
        if (selectedFigure != null) {
            selectedFigure.rotate(); 
            redrawCanvas();
        }
    }

    private void flipHorizontal() {
        if (selectedFigure != null) {
            selectedFigure.flipHorizontal();
            redrawCanvas();
        }
    }

    private void flipVertical() {
        if (selectedFigure != null) {
            selectedFigure.flipVertical();
            redrawCanvas();
        }
    }

    private void duplicateSelectedFigure() {
        if (selectedFigure != null) {
            Figure duplicate = selectedFigure.duplicate(DUPLICATION_OFFSET); 
            canvasState.addFigure(duplicate);
            redrawCanvas();
        }
    }

    private void divideSelectedFigure() {
        if (selectedFigure != null) {
            Figure[] dividedFigures = selectedFigure.divide();
            for (Figure dividedFigure : dividedFigures) {
                canvasState.addFigure(dividedFigure);
            }
            redrawCanvas();
        }
    }
    

	void redrawCanvas() {
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		for(Figure figure : canvasState.figures()) {
			gc.setStroke(Color.TRANSPARENT);
			if(figure.getShadowType().getColored()){
				gc.setFill(figureColorMap.get(figure).darker());
			}else{
				gc.setFill(Color.GRAY);
			}
			
			drawFigure(figure, figure.getShadowType().getOffset());
			
			if(figure == selectedFigure) {
				gc.setStroke(Color.RED);
			} else {
				gc.setStroke(lineColor);
			}
			gc.setFill(figureColorMap.get(figure));
			drawFigure(figure, 0);

		}
	}

	boolean figureBelongs(Figure figure, Point eventPoint) {
		boolean found = false;
		if(figure instanceof Rectangle) {
			Rectangle rectangle = (Rectangle) figure;
			found = eventPoint.getX() > rectangle.getTopLeft().getX() && eventPoint.getX() < rectangle.getBottomRight().getX() &&
					eventPoint.getY() > rectangle.getTopLeft().getY() && eventPoint.getY() < rectangle.getBottomRight().getY();
		} else if(figure instanceof Circle) {
			Circle circle = (Circle) figure;
			found = Math.sqrt(Math.pow(circle.getCenterPoint().getX() - eventPoint.getX(), 2) +
					Math.pow(circle.getCenterPoint().getY() - eventPoint.getY(), 2)) < circle.getRadius();
		} else if(figure instanceof Square) {
			Square square = (Square) figure;
			found = eventPoint.getX() > square.getTopLeft().getX() && eventPoint.getX() < square.getBottomRight().getX() &&
					eventPoint.getY() > square.getTopLeft().getY() && eventPoint.getY() < square.getBottomRight().getY();
		} else if(figure instanceof Ellipse) {
			Ellipse ellipse = (Ellipse) figure;
			// Nota: Fórmula aproximada. No es necesario corregirla.
			found = ((Math.pow(eventPoint.getX() - ellipse.getCenterPoint().getX(), 2) / Math.pow(ellipse.getsMayorAxis(), 2)) +
					(Math.pow(eventPoint.getY() - ellipse.getCenterPoint().getY(), 2) / Math.pow(ellipse.getsMinorAxis(), 2))) <= 0.30;
		}
		return found;
	}

}
