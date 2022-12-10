package backend;

import backend.model.Figure;
import frontend.FrontFigures.FrontFigure;

import java.util.ArrayList;
import java.util.List;

public class CanvasState {

    private final List<FrontFigure> list = new ArrayList<>();

    public void addFigure(FrontFigure figure) {
        list.add(figure);
    }

    public void deleteFigure(FrontFigure figure) {
        list.remove(figure);
    }

    public Iterable<FrontFigure> figures() {
        return new ArrayList<>(list);
    }

}
