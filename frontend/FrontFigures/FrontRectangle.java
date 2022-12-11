package frontend.FrontFigures;
import backend.model.Figure;
import backend.model.Rectangle;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.awt.*;


public class FrontRectangle extends FrontFigure {
    Rectangle localRectangle;
    public FrontRectangle(Figure figure, GraphicsContext gc, Color lineColor, double lineWidth, Color fillColor) {
        super(figure, gc, lineColor, lineWidth, fillColor);
        localRectangle = (Rectangle) figure;
    }

    @Override
    public void strokeAndFillFigure() {
        gc.fillRect(localRectangle.getTopLeft().getX(), localRectangle.getTopLeft().getY(),
                localRectangle.getBottomRight().getX() - localRectangle.getTopLeft().getX(),
                localRectangle.getBottomRight().getY() - localRectangle.getTopLeft().getY());
        gc.strokeRect(localRectangle.getTopLeft().getX(), localRectangle.getTopLeft().getY(),
                localRectangle.getBottomRight().getX() - localRectangle.getTopLeft().getX(),
                localRectangle.getBottomRight().getY() - localRectangle.getTopLeft().getY());
    }

    @Override
    public FrontFigure copyFigure() {
        return new FrontRectangle(this.figure, this.gc, this.getLineColor(), this.getThicknessBorder(), this.getFillColor());
    }
}

