package frontend;

//--------------------------------------
import backend.CanvasState;
import backend.Exceptions.NothingToRedoException;
import backend.Exceptions.NothingToUndoException;
import backend.model.*;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import frontend.FrontFigures.FrontFigure;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Region;
import javafx.scene.web.HTMLEditorSkin;
import java.util.ResourceBundle;

import java.util.*;


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
	ToggleButton formatButton = new ToggleButton("Copiar fmt");

	//Botones barra horizontal
//	String cutIconPath = ResourceBundle.getBundle(HTMLEditorSkin.class.getName()).getString("cutIcon");
//	Image cutIcon = new Image(HTMLEditorSkin.class.getResource(cutIconPath).toString());
//	Button cutButton = new Button("Cortar", new ImageView(cutIcon));
	Button cutButton = new Button("Cortar");
	Button copyButton = new Button("Copiar");
	Button pasteButton = new Button("Pegar");
	Button undoButton = new Button("Deshacer");
	Button redoButton = new Button("Rehacer");

	Label undoLabel = new Label("");

	Label undoCounter = new Label("0");
	Label redoLabel = new Label("");
	Label redoCounter = new Label("0");


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
	FrontFigure formatFigure;
	// Copiar figura
	FrontFigure copyFigure;

	// StatusBar
	StatusPane statusPane;

	// Centro
	private final Point centerPoint = new Point(canvas.getWidth()/2, canvas.getHeight()/2);

	public PaintPane(CanvasState canvasState, StatusPane statusPane) {
		this.canvasState = canvasState;
		this.statusPane = statusPane;
		List<ToggleButton> buttonsList = new ArrayList<>();
		ToggleGroup tools = new ToggleGroup();
		buttonsList.add(selectionButton);
		buttonsList.addAll(Arrays.stream(Buttons.values()).map(Buttons::getButton).toList());
		buttonsList.add(deleteButton);
		buttonsList.add(formatButton);
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
		List<Button> iconList = new ArrayList<>();
		iconList.add(cutButton);
		iconList.add(copyButton);
		iconList.add(pasteButton);

		IconBar.getChildren().addAll(iconList);
		for (Button tool : iconList) {
			tool.setMinWidth(90);
			tool.setCursor(Cursor.HAND);
		}
		IconBar.setPadding(new Insets(5));
		IconBar.setStyle("-fx-background-color: #999");

		List<Button> undoRedo = new ArrayList<>();
		undoRedo.add(undoButton);
		undoRedo.add(redoButton);

		for(Button tool : undoRedo) {
			tool.setMinWidth(90);
			tool.setCursor(Cursor.HAND);
		}

		HBox undoRedoBar = new HBox(10);
		undoRedoBar.getChildren().addAll(undoLabel, undoCounter);
		undoRedoBar.getChildren().addAll(undoRedo);
	 	undoRedoBar.getChildren().addAll(redoLabel, redoCounter);
		undoRedoBar.setPadding(new Insets(5));
		undoRedoBar.setStyle("-fx-background-color: #999; -fx-alignment: center;");

		BorderPane topPane = new BorderPane();
		topPane.setTop(IconBar);
		topPane.setCenter(undoRedoBar);

		setTop(topPane);

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
			updateUndoRedoStatus();
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
				updateUndoRedoStatus();
				redrawCanvas();
			}
			if (formatFigure != null) {
				pasteFormat(eventPoint);
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


		formatButton.setOnAction(event -> {
			if (selectedFigure != null) {
				formatFigure = selectedFigure;
			}
		});

		deleteButton.setOnAction(event -> {
			if (selectedFigure != null) {
				canvasState.deleteFigure(selectedFigure);
				selectedFigure = null;
				updateUndoRedoStatus();
				redrawCanvas();
			}
		});

		lineColorPicker.setOnAction(event -> {
			if (selectedFigure != null) {
				canvasState.recolorBorder(selectedFigure, selectedFigure.getLineColor(), lineColorPicker.getValue());
				selectedFigure.setLineColor(lineColorPicker.getValue());
				redrawCanvas();
			}
		});

		fillColorPicker.setOnAction(event -> {
			if (selectedFigure != null) {
				canvasState.recolorFill(selectedFigure, selectedFigure.getFillColor(), fillColorPicker.getValue());
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

		undoButton.setOnAction(event -> {
			try {
				canvasState.undo();
				updateUndoRedoStatus();
				redrawCanvas();
			} catch (NothingToUndoException ex) {
				createAlert(ex.getMessage());
			}
		});

		redoButton.setOnAction(event -> {
			try {
				canvasState.redo();
				updateUndoRedoStatus();
				redrawCanvas();
			} catch (NothingToRedoException ex) {
				createAlert(ex.getMessage());
			}
		});

		copyButton.setOnAction(event -> {
			if (selectedFigure != null) {
				canvasState.copyFigure(selectedFigure);
				copyFigure = selectedFigure;
			}
		});
		cutButton.setOnAction(event -> {
			if (selectedFigure != null) {
				copyFigure = selectedFigure;
				canvasState.deleteFigure(selectedFigure);
				updateUndoRedoStatus();
				redrawCanvas();
			}
		});
		pasteButton.setOnAction(event -> {
			if(copyFigure != null){
				FrontFigure newFigure = copyFigure.copyFigure(new Point(centerPoint.getX(), centerPoint.getY()));
				canvasState.addFigure(newFigure);
				updateUndoRedoStatus();
				redrawCanvas();
			}
		});

		this.setOnKeyPressed(keyEvent -> {
			if(keyEvent.isControlDown()){
				if (keyEvent.getCode().equals(KeyCode.C) && selectedFigure != null) {
					copyButton.fire();
				}
				if (keyEvent.getCode().equals(KeyCode.V) && copyFigure != null) {
					pasteButton.fire();
				}
				if(keyEvent.getCode().equals(KeyCode.X)){
					cutButton.fire();
				}
		}});

		setLeft(buttonsBox);
		setRight(canvas);
	}
	void pasteFormat(Point eventPoint) {
		for (FrontFigure figure : canvasState.figures()) {
			if (figureBelongs(figure, eventPoint)) {
				canvasState.formatFigure(figure, figure.getLineColor(), formatFigure.getLineColor(), figure.getThicknessBorder(),formatFigure.getThicknessBorder(), figure.getFillColor(), formatFigure.getFillColor());
				figure.copyFormat(formatFigure);
				formatFigure = null;
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

	private void updateUndoRedoStatus() {
		undoLabel.setText(canvasState.previewUndo());
		redoLabel.setText(canvasState.previewRedo());
	}

	boolean figureBelongs(FrontFigure figure, Point eventPoint) {
		return figure.belongs(eventPoint);
	}

	private void createAlert(String alertMessage){
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText(alertMessage);
		alert.showAndWait();
	}

}
