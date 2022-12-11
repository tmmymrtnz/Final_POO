package backend.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public abstract class Figure {
    //Colores default
    private Color lineColor = Color.BLACK;
    private Color fillColor = Color.YELLOW;
    private double thicknessBorder = 1.0;

    public void setThicknessBorder(double thicknessBorder) {
        this.thicknessBorder = thicknessBorder;
    }

    public double getThicknessBorder() {
        return thicknessBorder;
    }

    public abstract void moveFigure(double x, double y);
    public abstract String getFigureName();
    public abstract boolean belongs(Point point);
    public abstract String toString();

    public abstract double getWidth();
    public abstract double getHeight();
    public abstract void changeCenter(double x, double y);
}
