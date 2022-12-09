package frontend.FrontFigures;

import backend.model.Figure;
import javafx.scene.canvas.GraphicsContext;

import java.awt.*;

public abstract class FrontFigure {

    protected GraphicsContext gc;
    protected Figure figure;
    private Color lineColor, fillColor;
    private double lineWidth;

    public FrontFigure(Figure figure, GraphicsContext gc, Color lineColor, double lineWidth, Color fillColor) {
        this.figure = figure;
        this.gc = gc;
        setConf(lineColor, lineWidth, fillColor);
    }

    public void setConf(Color lineColor, double lineWidth, Color fillColor) {
        setLineColor(lineColor);
        setLineWidth(lineWidth);
        setFillColor(fillColor);
    }

    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
    }
    public void setLineWidth(double lineWidth) {
        this.lineWidth = lineWidth;
    }
    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    public Color getLineColor() {
        return lineColor;
    }
    public double getLineWidth() {
        return lineWidth;
    }
    public Color getFillColor() {
        return fillColor;
    }

    public boolean containsPoint(Point point) {
        return figure.belongs(point);
    }

    public abstract void strokeAndFill();

}