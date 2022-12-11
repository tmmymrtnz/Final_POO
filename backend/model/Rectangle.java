package backend.model;


public class Rectangle extends Figure {

    private Point topLeft, bottomRight, centerPoint;

    public Rectangle(Point topLeft, Point bottomRight) {
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
        this.centerPoint = new Point((topLeft.getX() + bottomRight.getX())/2, (topLeft.getY() + bottomRight.getY())/2);
    }

    public Point getTopLeft() {
        return topLeft;
    }

    public Point getBottomRight() {
        return bottomRight;
    }
    public double getWidth() {
        return Math.abs(topLeft.getX() - bottomRight.getX());
    }
    public double getHeight() {
        return Math.abs(topLeft.getY() - bottomRight.getY());
    }

    @Override
    public void moveFigure(double x, double y) {
//        topLeft.movePoint(x - getWidth()/2, y + getHeight()/2);
//        bottomRight.movePoint(x + getWidth()/2, y - getHeight()/2);
        topLeft.movePoint(x, y);
        bottomRight.movePoint(x, y);
    }

    @Override
    public void changeCenter(double x, double y){
        centerPoint.changePoint(x, y);
        topLeft.changePoint(x - getWidth()/2, y - getHeight()/2);
        bottomRight.changePoint(x + getWidth()/2, y + getHeight()/2);
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
