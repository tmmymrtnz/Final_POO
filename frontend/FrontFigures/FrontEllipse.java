package frontend.FrontFigures;
import backend.model.Ellipse;
import backend.model.Figure;
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
    public FrontFigure copyFigure() {
        return new FrontEllipse(this.figure, this.gc, this.getLineColor(), this.getThicknessBorder(), this.getFillColor());
    }
}

