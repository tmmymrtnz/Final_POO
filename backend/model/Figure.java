package backend.model;

import javafx.scene.paint.Color;

public abstract class Figure {
    //Colores default
    private Color lineColor = Color.BLACK;
    private Color fillColor = Color.YELLOW;
    

    public Color getLineColor() {
        return lineColor;
    }

    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
    }

    public Color getFillColor() {
        return fillColor;
    }

    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    public abstract String toString();

}
