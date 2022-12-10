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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.web.HTMLEditorSkin;
import javafx.scene.web.WebView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;


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
	ToggleButton deleteButton = new ToggleButton("Borrar");
	ToggleButton copyFormat = new ToggleButton("Copiar fmt");

	//Botones barra horizontal

	ToggleButton cutButton = new ToggleButton("Cortar");

	ToggleButton copyButton = new ToggleButton("Copiar");
	ToggleButton pasteButton = new ToggleButton("Pegar");

	ToggleButton undoButton = new ToggleButton("Deshacer");
	ToggleButton redoButton = new ToggleButton("Rehacer");

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
	FrontFigure copyFigure;

	// StatusBar
	StatusPane statusPane;

	// Centro
	private final Point centerPoint = new Point(400, 300);

	public PaintPane(CanvasState canvasState, StatusPane statusPane) {
		this.canvasState = canvasState;
		this.statusPane = statusPane;
		List<ToggleButton> buttonsList = new ArrayList<>();
		//ToggleButton[] toolsArr = {selectionButton, rectangleButton, circleButton, squareButton, ellipseButton, deleteButton, copyFormat};
		ToggleGroup tools = new ToggleGroup();
		buttonsList.add(selectionButton);
		buttonsList.addAll(Arrays.stream(Buttons.values()).map(Buttons::getButton).toList());
		buttonsList.add(deleteButton);
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


	//barra horizontal copiar pegar cortar
		HBox IconBar = new HBox(10);
		List<ToggleButton> iconList = new ArrayList<>();
		iconList.add(cutButton);
		iconList.add(copyButton);
		iconList.add(pasteButton);
		iconList.add(undoButton);
		iconList.add(redoButton);
		IconBar.getChildren().addAll(iconList);
		for (ToggleButton tool : iconList) {
			tool.setMinWidth(90);
			tool.setToggleGroup(tools);
			tool.setCursor(Cursor.HAND);
		}
		setTop(IconBar);

		canvas.setOnMousePressed(event -> {
			startPoint = new Point(event.getX(), event.getY());
		});

		canvas.setOnMouseReleased(event -> {
			Point endPoint = new Point(event.getX(), event.getY());
			if (startPoint == null) {
				return;
			}
			if (endPoint.getX() < startPoint.getX() || endPoint.getY() < startPoint.getY()) {
				return;
			}
			FrontFigure newFigure = Buttons.getFrontFigure(startPoint, endPoint, gc, lineColor, thicknessBorder.getValue(), fillColor);
			if (newFigure == null) {
				return;
			}
			canvasState.addFigure(newFigure);
			startPoint = null;
			redrawCanvas();
		});

		canvas.setOnMouseMoved(event -> {
			Point eventPoint = new Point(event.getX(), event.getY());
			boolean found = false;
			StringBuilder label = new StringBuilder();
			for (FrontFigure figure : canvasState.figures()) {
				if (figureBelongs(figure, eventPoint)) {
					found = true;
					label.append(figure.toString());
				}
			}
			if (found) {
				statusPane.updateStatus(label.toString());
			} else {
				statusPane.updateStatus(eventPoint.toString());
			}
		});

		canvas.setOnMouseClicked(event -> {
			Point eventPoint = new Point(event.getX(), event.getY());
			boolean found = false;
			if (selectionButton.isSelected()) {
				StringBuilder label = new StringBuilder("Se seleccionó: ");
				for (FrontFigure figure : canvasState.figures()) {
					if (figureBelongs(figure, eventPoint)) {
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
//			 	if(copyButton.isSelected()){
//					 copyFigure = selectedFigure;
//				}
				redrawCanvas();
			}
			if (copyFigure != null) {
				pasteFormat(found, eventPoint);
			}

		});

		canvas.setOnMouseDragged(event -> {
			if (selectionButton.isSelected()) {
				Point eventPoint = new Point(event.getX(), event.getY());
				double diffX = (eventPoint.getX() - startPoint.getX()) / 500;
				double diffY = (eventPoint.getY() - startPoint.getY()) / 500;

				if (selectedFigure != null) {
					selectedFigure.moveFigure(diffX, diffY);
					//startPoint = eventPoint;
					redrawCanvas();
				}
			}
		});


		copyFormat.setOnAction(event -> {
			if (selectedFigure != null) {
				copyFigure = selectedFigure;
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

//		canvas.setOnKeyPressed(event -> {
//			if (event.getCode() == KeyCode.C && selectedFigure != null){
//				copyFigure = selectedFigure;
//			}
//			if(event.getCode() == KeyCode.V && copyFigure != null){
//				canvasState.addFigure(copyFigure);
//				redrawCanvas();
//			}
//			if(event.getCode() == KeyCode.X && selectedFigure != null){
//				canvasState.deleteFigure(selectedFigure);
//				copyFigure = selectedFigure;
//				selectedFigure = null;
//				redrawCanvas();
//			}
//		});

		setLeft(buttonsBox);
		setRight(canvas);
	}

//	void pasteFigureCenter(FrontFigure figure){
//		double x = (canvas.getWidth() - figure.getWidth()) / 2;
//		double y = (canvas.getHeight() - figure.getHeight()) / 2;
//		figure.moveFigure(x, y);
//		canvasState.addFigure(figure);
//	}
	void pasteFigure(FrontFigure toPaste){
		FrontFigure newFigure = toPaste.copyFigure(toPaste);
		newFigure.moveCenter(canvas.getWidth()/2,canvas.getHeight()/2); //corregir moves
		canvasState.addFigure(newFigure);
		redrawCanvas();
	}
	void pasteFormat(Point eventPoint) {
		for (FrontFigure figure : canvasState.figures()) {
			if (figureBelongs(figure, eventPoint)) {
				found = true;
				figure.setConf(copyFigure.getLineColor(), copyFigure.getThicknessBorder(), copyFigure.getFillColor());
				copyFigure = null;
				redrawCanvas();
			}
		}
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
			figure.strokeAndFillFigure();
		}
	}


	boolean figureBelongs(FrontFigure figure, Point eventPoint) {
		return figure.belongs(eventPoint);
	}

}
