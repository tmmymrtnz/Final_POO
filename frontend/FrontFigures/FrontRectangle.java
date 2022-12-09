package frontend.FrontFigures;
import backend.model.Figure;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class FrontRectangle extends FrontFigure {
    public FrontRectangle(Figure figure, GraphicsContext gc, Color lineColor, double lineWidth, Color fillColor) {
        super(figure, gc, lineColor, lineWidth, fillColor);
    }

    @Override
//    public void strokeAndFill() {
//        gc.fillRect(figure.getCenterPoint().getX() - figure.getDimensions().getLeftElement() / 2,
//                figure.getCenterPoint().getY() - figure.getDimensions().getRightElement() / 2,
//                figure.getDimensions().getLeftElement(), figure.getDimensions().getRightElement());
//        gc.strokeRect(figure.getCenterPoint().getX() - figure.getDimensions().getLeftElement() / 2,
//                figure.getCenterPoint().getY() - figure.getDimensions().getRightElement() / 2,
//                figure.getDimensions().getLeftElement(), figure.getDimensions().getRightElement());
//    }
    //arreglarlo
}

