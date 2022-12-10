package frontend;

import backend.model.*;
import backend.model.Point;
import backend.model.Rectangle;
import frontend.FrontFigures.FrontFigure;
import frontend.FrontFigures.FrontRectangle;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.paint.Color;
import frontend.FrontFigures.FrontEllipse;
import java.awt.*;

public enum Buttons {

    RECTANGLE(new ToggleButton("Rectangulo")){

        public FrontFigure createFrontFigure(Point startPoint, Point endPoint, GraphicsContext gc, Color lineColor, double lineWidth, Color fillColor) {
            return new FrontRectangle(new Rectangle(startPoint, endPoint), gc, lineColor, lineWidth, fillColor);
        }
    },
    ELIPSE(new ToggleButton("Elipse")){

        public FrontFigure createFrontFigure(Point startPoint, Point endPoint, GraphicsContext gc, Color lineColor, double lineWidth, Color fillColor) {
            Point centerPoint = new Point(Math.abs(endPoint.x + startPoint.x) / 2, (Math.abs((endPoint.y + startPoint.y)) / 2));
            double sMayorAxis = Math.abs(endPoint.x - startPoint.x);
            double sMinorAxis = Math.abs(endPoint.y - startPoint.y);
            return new FrontEllipse(new Ellipse(centerPoint, sMayorAxis,sMinorAxis), gc, lineColor, lineWidth, fillColor);
        }
    },
    CIRCULO(new ToggleButton("Circulo")){
        public FrontFigure createFrontFigure(Point startPoint, Point endPoint, GraphicsContext gc, Color lineColor, double lineWidth, Color fillColor) {
            double circleRadius = Math.sqrt(Math.pow(Math.abs(startPoint.getX() - endPoint.getX()), 2) + Math.pow(Math.abs(startPoint.getY() - endPoint.getY()), 2));
            return new FrontEllipse(new Circle(startPoint, circleRadius), gc, lineColor, lineWidth, fillColor);
        }
    },
    CUADRADO(new ToggleButton("Cuadrado")){
        public FrontFigure createFrontFigure(Point startPoint, Point endPoint, GraphicsContext gc, Color lineColor, double lineWidth, Color fillColor) {
            double size = Math.abs(endPoint.getX() - startPoint.getX());
            return new FrontRectangle(new Square(startPoint, size), gc, lineColor, lineWidth, fillColor);
        }
    };

    private ToggleButton button;

    Buttons(ToggleButton button) {
        this.button = button;
    }

    public ToggleButton getButton() {
        return button;
    }

    public static FrontFigure getFrontFigure(Point startPoint, Point endPoint, GraphicsContext gc, Color lineColor, double lineWidth, Color fillColor){
        for (Buttons b : values()) {
            if(b.button.isSelected()){
                return b.createFrontFigure(startPoint, endPoint, gc, lineColor, lineWidth, fillColor);
            }
        }
        return null;
    }

    public abstract FrontFigure createFrontFigure(Point startPoint, Point endPoint, GraphicsContext gc, Color lineColor, double lineWidth, Color fillColor);

}
