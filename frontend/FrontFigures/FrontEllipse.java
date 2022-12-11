package frontend.FrontFigures;
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
        Figure newFig = new Ellipse(centerPoint, localEllipse.getsMayorAxis(), localEllipse.getsMinorAxis());
        return new FrontEllipse(newFig, getGraphicsContext(), this.getLineColor(), this.getThicknessBorder(), this.getFillColor());
    }

}

