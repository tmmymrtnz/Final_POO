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
    public boolean belongs(Point point) {
        return Math.pow(point.getX() - centerPoint.getX(), 2) / Math.pow(sMayorAxis, 2) + Math.pow(point.getY() - centerPoint.getY(), 2) / Math.pow(sMinorAxis, 2) <= 1;
    }

    @Override
    public String toString() {
        return String.format("Elipse [Centro: %s, DMayor: %.2f, DMenor: %.2f]", centerPoint, sMayorAxis, sMinorAxis);
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

    @Override
    public void moveCenter(double x, double y) {
        this.centerPoint.movePoint(x, y);
    }

    @Override
    public void strokeAndFillFigure(GraphicsContext gc) {
        gc.fillOval(getCenterPoint().getX() - getsMayorAxis() / 2,
                getCenterPoint().getY() - getsMinorAxis() / 2,
                getsMayorAxis(), getsMinorAxis());
        gc.strokeOval(getCenterPoint().getX() - getsMayorAxis() / 2,
                getCenterPoint().getY() - getsMinorAxis() / 2,
                getsMayorAxis(), getsMinorAxis());
    }

}
