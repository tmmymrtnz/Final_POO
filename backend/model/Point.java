package backend.model;

import java.util.Objects;

public class Point {

    public double x, y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public String toString() {
        return String.format("{%.2f , %.2f}", x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Point))
            return false;
        Point point = (Point) o;
        if (Double.compare(point.x, x) != 0)
            return false;
        return Double.compare(point.y, y) == 0;
    }

    public void movePoint(double x, double y) {
        this.x += x;
        this.y += y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
