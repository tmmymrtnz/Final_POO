package backend.model;

import javafx.scene.canvas.GraphicsContext;

public class Rectangle extends Figure {

    private Point topLeft, bottomRight;

    public Rectangle(Point topLeft, Point bottomRight) {
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
    }

    public Point getTopLeft() {
        return topLeft;
    }

    public Point getBottomRight() {
        return bottomRight;
    }

    @Override
    public void moveFigure(double x, double y) {
        topLeft.movePoint(x, y);
        bottomRight.movePoint(x, y);
    }

    @Override
    public void moveCenter(double x, double y) {
        this.centerPoint.changePoint(x, y);
    }

    @Override
    public boolean belongs(Point point) {
        return point.getX() >= topLeft.getX() && point.getX() <= bottomRight.getX() && point.getY() >= topLeft.getY() && point.getY() <= bottomRight.getY();
    }

    @Override
    public String toString() {
        return String.format("Rectángulo [ %s , %s ]", topLeft, bottomRight);
    }



}
