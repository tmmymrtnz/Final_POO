package frontend.FrontFigures;

import backend.model.Figure;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import backend.model.Point;


import java.awt.*;

public class FrontFigure {

    protected GraphicsContext gc;
    protected Figure figure;
    private Color lineColor, fillColor;

    public FrontFigure(Figure figure, GraphicsContext gc, Color lineColor, double lineWidth, Color fillColor) {
        this.figure = figure;
        this.gc = gc;
        setConf(lineColor, lineWidth, fillColor);
    }

    private void setConf(Color lineColor, double lineWidth, Color fillColor) {
        setLineColor(lineColor);
        setThicknessBorder(lineWidth);
        setFillColor(fillColor);
    }

    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
    }

    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    public Color getLineColor() {
        return lineColor;
    }

    public Color getFillColor() {
        return fillColor;
    }

    public boolean containsPoint(Point point) {
        return figure.belongs(point);
    }

    public boolean belongs(Point eventPoint) {
        return figure.belongs(eventPoint);
    }

    public double getThicknessBorder() {
        return figure.getThicknessBorder();
    }

    public void strokeAndFillFigure() { figure.strokeAndFillFigure(gc); }

    public void setThicknessBorder(double value) {
        figure.setThicknessBorder(value);
    }

    public void moveFigure(double diffX, double diffY) {
        figure.moveFigure(diffX, diffY);
    }

    public void moveCenter(double diffX, double diffY) {
        figure.moveCenter(diffX, diffY);
    }

    public String toString() {
        return figure.toString();
    }

    public void copyFormat(FrontFigure figure) {
        setConf(figure.getLineColor(), figure.getThicknessBorder(), figure.getFillColor());
    }

    public Figure getFigure() {
        return figure;
    }

    //raro
    public FrontFigure copyFigure(FrontFigure toCopy) {
        return new FrontFigure(toCopy.figure, toCopy.gc, toCopy.getLineColor(), toCopy.getThicknessBorder(), toCopy.getFillColor());
    }


}