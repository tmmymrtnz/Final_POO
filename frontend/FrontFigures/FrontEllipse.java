package frontend.FrontFigures;
import backend.model.Circle;
import backend.model.Ellipse;
import backend.model.Figure;
import backend.model.Point;
import java.awt.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class FrontEllipse extends FrontFigure {
    Ellipse localEllipse;
    public FrontEllipse(Figure figure, GraphicsContext gc, Color lineColor, double lineWidth, Color fillColor) {
        super(figure, gc, lineColor, lineWidth, fillColor);
        localEllipse = (Ellipse) figure;
    }

    @Override
    public void strokeAndFillFigure() {
        gc.fillOval(localEllipse.getCenterPoint().getX() - localEllipse.getsMayorAxis() / 2,
                localEllipse.getCenterPoint().getY() - localEllipse.getsMinorAxis() / 2  ,
                localEllipse.getsMayorAxis(), localEllipse.getsMinorAxis());
        gc.strokeOval(localEllipse.getCenterPoint().getX() - localEllipse.getsMayorAxis() / 2 ,
                localEllipse.getCenterPoint().getY() - localEllipse.getsMinorAxis() / 2,
                localEllipse.getsMayorAxis(), localEllipse.getsMinorAxis());
    }

    @Override
    public FrontFigure copyFigure(Point centerPoint) {
        if (figure instanceof Circle) {
            return new FrontEllipse(new Circle(centerPoint, localEllipse.getsMayorAxis()/2), gc, getLineColor(), getThicknessBorder(), getFillColor());
            }
        else{
            return new FrontEllipse(new Ellipse(centerPoint, localEllipse.getsMayorAxis(), localEllipse.getsMinorAxis()), gc, getLineColor(), getThicknessBorder(), getFillColor());
        }
    }

}

