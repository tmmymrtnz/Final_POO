package frontend.FrontFigures;
import backend.model.Figure;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class FrontEllipse extends FrontFigure {
    public FrontEllipse(Figure figure, GraphicsContext gc, Color lineColor, double lineWidth, Color fillColor) {
        super(figure, gc, lineColor, lineWidth, fillColor);
    }
    @Override
    public void strokeAndFill() {
        gc.fillOval(figure.getCenterPoint().getX() - figure.getsMayorAxis() / 2,
                figure.getCenterPoint().getY() - figure.getsMinorAxis() / 2,
                figure.getsMayorAxis(), figure.getsMinorAxis());
        gc.strokeOval(figure.getCenterPoint().getX() - figure.getsMayorAxis() / 2,
                figure.getCenterPoint().getY() - figure.getsMinorAxis() / 2,
                figure.getsMayorAxis(), figure.getsMinorAxis());
    }
}

