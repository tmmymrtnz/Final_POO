package backend.model;


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
    private double getWidth() {
        return Math.abs(topLeft.getX() - bottomRight.getX());
    }
    private double getHeight() {
        return Math.abs(topLeft.getY() - bottomRight.getY());
    }

    @Override
    public void moveFigure(double x, double y) {
        topLeft.movePoint(x - getWidth()/2, y + getHeight()/2);
        bottomRight.movePoint(x + getWidth()/2, y - getHeight()/2);
    }


    @Override
    public String getFigureName(){
        return "Rectangulo";
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
