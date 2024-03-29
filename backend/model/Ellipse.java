package backend.model;

import javafx.scene.canvas.GraphicsContext;

public class Ellipse extends Figure {

    protected Point centerPoint;
    protected final double sMayorAxis, sMinorAxis;

    public Ellipse(Point centerPoint, double sMayorAxis, double sMinorAxis) {
        this.centerPoint = centerPoint;
        this.sMayorAxis = sMayorAxis;
        this.sMinorAxis = sMinorAxis;
    }

    @Override
    public void moveFigure(double x, double y) {
        centerPoint.movePoint(x, y);
    }
    @Override
    public void changeCenter(double x, double y) {
        moveFigure(x, y);
    }
    @Override
    public String getFigureName(){
        return "Elipse";
    }

    @Override
    public boolean belongs(Point point) {
        return (Math.pow(point.getX() - getCenterPoint().getX(), 2) / Math.pow(getsMayorAxis(), 2)) +
                (Math.pow(point.getY() - getCenterPoint().getY(), 2) / Math.pow(getsMinorAxis(), 2))<= 0.30;
    }

    @Override
    public String toString() {
        return String.format("Elipse [Centro: %s, DMayor: %.2f, DMenor: %.2f]", centerPoint, sMayorAxis, sMinorAxis);
    }

    @Override
    public double getWidth() {
        return sMayorAxis*2;
    }

    @Override
    public double getHeight() {
        return sMinorAxis*2;
    }

    public Point getCenterPoint() {
        return centerPoint;
    }

    public double getsMayorAxis() {
        return sMayorAxis;
    }

    public double getsMinorAxis() {
        return sMinorAxis;
    }




}
