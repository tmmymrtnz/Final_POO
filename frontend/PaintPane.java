package frontend;

import backend.CanvasState;
import backend.model.*;
import frontend.FrontFigures.FrontFigure;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class PaintPane extends BorderPane {

	// BackEnd
	CanvasState canvasState;

	// Canvas y relacionados
	Canvas canvas = new Canvas(800, 600);
	GraphicsContext gc = canvas.getGraphicsContext2D();
	Color lineColor = Color.BLACK;
	Color fillColor = Color.YELLOW;

	// Botones Barra Izquierda
	ToggleButton selectionButton = new ToggleButton("Seleccionar");
	ToggleButton rectangleButton = new ToggleButton("Rectángulo");
	ToggleButton circleButton = new ToggleButton("Círculo");
	ToggleButton squareButton = new ToggleButton("Cuadrado");
	ToggleButton ellipseButton = new ToggleButton("Elipse");
	ToggleButton deleteButton = new ToggleButton("Borrar");
	ToggleButton copyFormat = new ToggleButton("Copiar fmt");
	Slider thicknessBorder = new Slider(1.0, 50.0, 1.0);
	ColorPicker lineColorPicker = new ColorPicker(lineColor);
	ColorPicker fillColorPicker = new ColorPicker(fillColor);

	Label thicknessLabel = new Label("Grosor");
	Label labelThickness = new Label("Borde");
	Label labelColor = new Label("Relleno");

	// Dibujar una figura
	Point startPoint;

	// Seleccionar una figura
	FrontFigure selectedFigure;

	// Copiar formato
	Figure copyFigure;

	// StatusBar
	StatusPane statusPane;

	public PaintPane(CanvasState canvasState, StatusPane statusPane) {
		this.canvasState = canvasState;
		this.statusPane = statusPane;
		List<ToggleButton> buttonsList = new ArrayList<>();
		//ToggleButton[] toolsArr = {selectionButton, rectangleButton, circleButton, squareButton, ellipseButton, deleteButton, copyFormat};
		ToggleGroup tools = new ToggleGroup();
		buttonsList.add(selectionButton);
		buttonsList.addAll(Arrays.stream(Buttons.values()).map(Buttons::getButton).toList());
		buttonsList.add(copyFormat);
		for (ToggleButton tool : buttonsList) {
			tool.setMinWidth(90);
			tool.setToggleGroup(tools);
			tool.setCursor(Cursor.HAND);
		}

		thicknessBorder.setShowTickLabels(true);
		thicknessBorder.setShowTickMarks(true);
		thicknessBorder.setMajorTickUnit(10);
		thicknessBorder.setBlockIncrement(5);


		VBox buttonsBox = new VBox(10);
		buttonsBox.getChildren().addAll(buttonsList);
		buttonsBox.setPadding(new Insets(5));
		buttonsBox.setStyle("-fx-background-color: #999");
		buttonsBox.setPrefWidth(100);
		gc.setLineWidth(1);

		Region[] borderAndFill = {thicknessLabel, thicknessBorder, labelThickness, lineColorPicker, labelColor, fillColorPicker};
		buttonsBox.getChildren().addAll(borderAndFill);

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
			FrontFigure newFigure = Buttons.getFrontFigure(startPoint, endPoint, gc, lineColor, thicknessBorder.getValue(), fillColor);
			if(newFigure == null) {
				return;
			}

//			if(rectangleButton.isSelected()) {
//				newFigure = new Rectangle(startPoint, endPoint);
//			}
//			else if(circleButton.isSelected()) {
//				double circleRadius = Math.abs(endPoint.getX() - startPoint.getX());
//				newFigure = new Circle(startPoint, circleRadius);
//			} else if(squareButton.isSelected()) {
//				double size = Math.abs(endPoint.getX() - startPoint.getX());
//				newFigure = new Square(startPoint, size);
//			} else if(ellipseButton.isSelected()) {
//				Point centerPoint = new Point(Math.abs(endPoint.x + startPoint.x) / 2, (Math.abs((endPoint.y + startPoint.y)) / 2));
//				double sMayorAxis = Math.abs(endPoint.x - startPoint.x);
//				double sMinorAxis = Math.abs(endPoint.y - startPoint.y);
//				newFigure = new Ellipse(centerPoint, sMayorAxis, sMinorAxis);
//			} else {
//				return ;
//			}
			canvasState.addFigure(newFigure);
			startPoint = null;
			redrawCanvas();
		});

		canvas.setOnMouseMoved(event -> {
			Point eventPoint = new Point(event.getX(), event.getY());
			boolean found = false;
			StringBuilder label = new StringBuilder();
			for(FrontFigure figure : canvasState.figures()) {
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
				for (FrontFigure figure : canvasState.figures()) {
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
				double diffX = (eventPoint.getX() - startPoint.getX())/500;
				double diffY = (eventPoint.getY() - startPoint.getY())/500;

				if(selectedFigure != null) {
					selectedFigure.moveFigure(diffX, diffY);
					//startPoint = eventPoint;
					redrawCanvas();
				}
			}
		});

		deleteButton.setOnAction(event -> {
			if (selectedFigure != null) {
				canvasState.deleteFigure(selectedFigure);
				selectedFigure = null;
				redrawCanvas();
			}
		});

		lineColorPicker.setOnAction(event -> {
			if (selectedFigure != null) {
				selectedFigure.setLineColor(lineColorPicker.getValue());
				redrawCanvas();
			}
		});

		fillColorPicker.setOnAction(event -> {
			if (selectedFigure != null) {
				selectedFigure.setFillColor(fillColorPicker.getValue());
				redrawCanvas();
			}
		});

		thicknessBorder.setOnMouseDragged(event -> {
			if (selectedFigure != null) {
				selectedFigure.setThicknessBorder(thicknessBorder.getValue());
				redrawCanvas();
			}
		});


//		copyFormat.setOnAction(event -> {
//			if (selectedFigure != null) {
//				copyFigure = selectedFigure;
//			}
//		});
//
//		canvas.setOnMouseClicked(event -> {
//			if (!selectionButton.isSelected() && copyFigure != null){
//				Point eventPoint = new Point(event.getX(), event.getY());
//				boolean found = false;
//				for (Figure figure : canvasState.figures()) {
//					if (figureBelongs(figure, eventPoint)) {
//						found = true;
//						figure.setLineColor(copyFigure.getLineColor());
//						figure.setFillColor(copyFigure.getFillColor());
//						figure.setThicknessBorder(copyFigure.getThicknessBorder());
//						copyFigure = null;
//						redrawCanvas();
//					}
//				}
//			}
//
//
//		});

		setLeft(buttonsBox);
		setRight(canvas);
	}


	void redrawCanvas() {
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		for(FrontFigure figure : canvasState.figures()) {
			gc.setLineWidth(figure.getThicknessBorder());
			if(figure == selectedFigure) {
				gc.setStroke(Color.RED);
			} else {
				gc.setStroke(figure.getLineColor());
			}
			gc.setFill(figure.getFillColor());
			figure.strokeAndFillFigure(); //chequear si es necesario pasar el gc
		}
	}


	boolean figureBelongs(FrontFigure figure, Point eventPoint) {
		return figure.belongs(eventPoint);

	}

}
